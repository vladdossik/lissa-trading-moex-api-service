package lissa.trading.moexapi.service.security.internal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Getter
@Slf4j
@Component
@RequiredArgsConstructor
public class InternalTokenService {

    @Value("${security.internal.token}")
    private String internalToken;

    protected boolean validateInternalToken(String token) {
        return new String(Base64.getDecoder().decode(internalToken))
                .trim().equals(token) && !token.isEmpty();
    }

    protected String getServiceNameFromToken(String token) {
        return token;
    }

    protected List<String> getRolesFromToken(String token) {
        return validateInternalToken(token)
                ? Collections.singletonList("ROLE_INTERNAL_SERVICE")
                : Collections.emptyList();
    }
}