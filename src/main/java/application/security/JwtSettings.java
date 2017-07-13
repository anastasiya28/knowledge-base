package application.security;

import org.springframework.stereotype.Component;

@Component
public class JwtSettings {
    public static final String ACCESS_JWT_TOKEN_HEADER_PARAM = "Authorization";
    private String SigningKey = "secret_signing_key";

    public JwtSettings() {
    }

    public String getSigningKey() {
        return SigningKey;
    }

    public void setSigningKey(String signingKey) {
        SigningKey = signingKey;
    }
}
