package life.khabanh.usersservices.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import life.khabanh.usersservices.dto.request.AuthenticationRequest;
import life.khabanh.usersservices.dto.request.IntrospectRequest;
import life.khabanh.usersservices.dto.request.LogOutRequest;
import life.khabanh.usersservices.dto.request.TokenRefreshRequest;
import life.khabanh.usersservices.dto.response.AuthenticationResponse;
import life.khabanh.usersservices.dto.response.IntrospectResponse;
import life.khabanh.usersservices.entity.InvalidatedToken;
import life.khabanh.usersservices.entity.User;
import life.khabanh.usersservices.exception.AppException;
import life.khabanh.usersservices.exception.ErrorCode;
import life.khabanh.usersservices.repository.InvalidatedTokenRepository;
import life.khabanh.usersservices.repository.UserRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new AppException(ErrorCode.INCORRECT_PASSWORD);


        return AuthenticationResponse.builder()
                .isAuthenticated(true)
                .token(generateToken(user,false))
                .refreshToken(generateToken(user,true))
                .build();
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        boolean isTokenValid = true;

        verifyToken(request.getToken());

        return IntrospectResponse.builder()
                .isValid(isTokenValid)
                .build();
    }

    public AuthenticationResponse refreshToken(TokenRefreshRequest request) throws ParseException, JOSEException {
        if (request == null || request.getRefreshToken() == null || request.getToken() == null)
            throw new AppException(ErrorCode.MISSING_REQUIRED_FIELDS);

        if (request.getRefreshToken().trim().isEmpty() || request.getToken().trim().isEmpty())
            throw new AppException(ErrorCode.MISSING_REQUIRED_FIELDS);

        if (invalidatedTokenRepository.existsById(SignedJWT.parse(request.getToken()).getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        verifyToken(request.getRefreshToken());

        SignedJWT signedJWT = SignedJWT.parse(request.getRefreshToken());

        Date expTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (expTime.before(new Date())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String email = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.UNAUTHENTICATED)
        );

        var token = generateToken(user, false);
        blockToken(request.getToken());

        return AuthenticationResponse.builder()
                .token(token)
                .refreshToken(request.getRefreshToken())
                .isAuthenticated(true)
                .build();
    }


    public void logout(LogOutRequest request) throws ParseException, JOSEException {
        try {
            verifyToken(request.getToken());
            blockToken(request.getToken());
        } catch (ParseException | JOSEException ignored) {}

        try {
            verifyToken(request.getRefreshToken());
            blockToken(request.getRefreshToken());
        } catch (ParseException | JOSEException ignored) {}
    }

    String generateToken(User user, boolean isRefresh) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("khabanh.life")
                .issueTime(new Date())
                .expirationTime( isRefresh
                                ? new Date(Instant.now().plus(1209600, ChronoUnit.SECONDS).toEpochMilli())
                                : new Date(Instant.now().plus(900, ChronoUnit.SECONDS).toEpochMilli())
                )
                .jwtID(UUID.randomUUID().toString())
                .claim("role", buildRole(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            String secretKey = System.getenv("JWT_SECRET_KEY");
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token");
            throw new RuntimeException(e);
        }
    }

    String buildRole(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(stringJoiner::add);

        return stringJoiner.toString();
    }

    void verifyToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean isExpired = expTime.before(new Date());

        JWSVerifier verifier = new MACVerifier(System.getenv("JWT_SECRET_KEY"));
        boolean isVerified = signedJWT.verify(verifier);

        if (isExpired || !isVerified)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

    }

    void blockToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);

        String tokenId = signedJWT.getJWTClaimsSet().getJWTID();
        Date expTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(tokenId)
                .expTime(expTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }
}
