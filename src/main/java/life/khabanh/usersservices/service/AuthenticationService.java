package life.khabanh.usersservices.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import life.khabanh.usersservices.dto.request.AuthenticationRequest;
import life.khabanh.usersservices.dto.request.IntrospectRequest;
import life.khabanh.usersservices.dto.request.LogOutRequest;
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
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
@Builder
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

        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .isAuthenticated(true)
                .token(token)
                .build();
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        boolean isTokenValid = true;

        try {
            verifyToken(request.getToken());
        } catch (AppException e) {
            isTokenValid = false;
        }

        return IntrospectResponse.builder()
                .isValid(isTokenValid)
                .build();
    }

    public String logout(LogOutRequest request) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(request.getToken());

        String tokenId = signedJWT.getJWTClaimsSet().getJWTID();
        Date expTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(tokenId)
                .expTime(expTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);

        return ("Log out successfully");
    }

    String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("khabanh.life")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
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

    boolean verifyToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean isExpired = expTime.after(new Date());

        JWSVerifier verifier = new MACVerifier(System.getenv("JWT_SECRET_KEY"));
        boolean isVerified = signedJWT.verify(verifier);

        if (!(isExpired && isVerified))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(token))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return true;
    }
}
