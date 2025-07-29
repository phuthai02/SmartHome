package project.smarthome.coreservice.service.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.smarthome.common.dto.response.SessionResponse;
import project.smarthome.common.utils.Constants;
import project.smarthome.coreservice.service.redis.RedisService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    private RedisService redisService;

    public List<SessionResponse> getAllUserSessions() {
        Set<String> keys = redisService.keys(Constants.Pattern.ACCESS_TOKEN);
        List<SessionResponse> result = new ArrayList<>();
        for (String key : keys) {
            try {
                String username = getUsername(key);

                String accessKey = String.format(Constants.Format.ACCESS_TOKEN, username);
                String refreshKey = String.format(Constants.Format.REFRESH_TOKEN, username);

                String accessToken = redisService.get(accessKey);
                String refreshToken = redisService.get(refreshKey);

                Long accessTtl = redisService.getTtl(accessKey);
                Long refreshTtl = redisService.getTtl(refreshKey);

                boolean accessExpired = accessTtl <= 0;
                boolean refreshExpired = refreshTtl <= 0;

                result.add(new SessionResponse(username, accessToken, refreshToken, accessExpired, refreshExpired, accessTtl, refreshTtl));
            } catch (Exception e) {
                log.error("Error processing session for key: {}", key, e);
            }
        }
        return result;
    }

    private String getUsername(String key) {
        return key.replace(Constants.Prefix.ACCESS_TOKEN, "");
    }
}
