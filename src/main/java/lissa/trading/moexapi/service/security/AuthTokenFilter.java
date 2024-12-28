package lissa.trading.moexapi.service.security;

import lissa.trading.lissa.auth.lib.dto.UserInfoDto;
import lissa.trading.lissa.auth.lib.feign.AuthServiceClient;
import lissa.trading.lissa.auth.lib.security.BaseAuthTokenFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends BaseAuthTokenFilter<UserInfoDto> {

    private final AuthServiceClient authServiceClient;

    @Override
    protected List<String> parseRoles(UserInfoDto userInfoDto) {
        return userInfoDto.getRoles();
    }

    @Override
    protected UserInfoDto retrieveUserInfo(String token) {
        try {
            return authServiceClient.getUserInfo("Bearer " + token);
        } catch (Exception ex) {
            log.error("Failed to retrieve user info from auth service: {}", ex.getMessage());
            return null;
        }
    }
}
