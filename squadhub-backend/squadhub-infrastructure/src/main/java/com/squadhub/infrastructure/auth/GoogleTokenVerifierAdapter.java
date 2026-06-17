package com.squadhub.infrastructure.auth;

import com.squadhub.domain.ports.out.TokenVerifierPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class GoogleTokenVerifierAdapter implements TokenVerifierPort {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String GOOGLE_VERIFY_URL = "https://oauth2.googleapis.com/tokeninfo?id_token=";

    @Override
    public Optional<ExtUser> verify(String externalToken) {
        try {
            String url = GOOGLE_VERIFY_URL + externalToken;
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("email")) {
                String email = (String) response.get("email");
                String name = (String) response.get("name");
                String picture = (String) response.get("picture");
                return Optional.of(new ExtUser(email, name, picture));
            }
        } catch (Exception e) {
            log.warn("Failed to verify Google ID Token due to external API error: {}", e.getMessage());
        }
        return Optional.empty();
    }
}