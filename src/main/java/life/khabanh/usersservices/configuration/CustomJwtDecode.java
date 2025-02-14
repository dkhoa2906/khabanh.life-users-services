package life.khabanh.usersservices.configuration;

import com.nimbusds.jose.JOSEException;
import life.khabanh.usersservices.dto.request.IntrospectRequest;
import life.khabanh.usersservices.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;

@Component
public class CustomJwtDecode implements JwtDecoder {

    private final AuthenticationService authenticationService;
    private final NimbusJwtDecoder nimbusJwtDecoder;

    public CustomJwtDecode(@Autowired AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;

        String signerKey = System.getenv("JWT_SECRET_KEY");
        if (signerKey == null || signerKey.isEmpty()) {
            throw new IllegalArgumentException("JWT_SECRET_KEY environment variable is not set.");
        }

        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
        this.nimbusJwtDecoder = NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            var response = authenticationService.introspect(IntrospectRequest.builder()
                    .token(token)
                    .build());

            if (!response.isValid()) {
                throw new JwtException("Token invalid");
            }
        } catch (JOSEException | ParseException e) {
            throw new JwtException("Error during token introspection: " + e.getMessage());
        }

        return nimbusJwtDecoder.decode(token);
    }
}
