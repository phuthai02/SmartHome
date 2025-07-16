package project.smarthome.coreservice.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import project.smarthome.common.dto.response.ResponseAPI;
import project.smarthome.common.utils.Constants;
import project.smarthome.common.utils.JsonUtils;
import project.smarthome.coreservice.service.jwt.JwtService;
import project.smarthome.coreservice.service.user.UserDetailsSecurityService;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsSecurityService userDetailsSecurityService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractAccessTokenFromRequest(request);
            if (token != null) {
                String username = jwtService.extractUsername(token);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsSecurityService.loadUserByUsername(username);
                    if (jwtService.isAccessTokenValid(token, userDetails.getUsername())) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        log.info("Invalid token for user: {}", username);
                        sendError(response, "Invalid token", HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                }
            }
        } catch (ExpiredJwtException e) {
            log.info("JWT token expired: {}", e.getMessage());
            sendError(response, "Token expired", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token: {}", e.getMessage());
            sendError(response, "Invalid token format", HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (SignatureException e) {
            log.info("JWT signature validation failed: {}", e.getMessage());
            sendError(response, "Invalid token signature", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (Exception e) {
            log.info("JWT authentication error: {}", e.getMessage());
            sendError(response, "Authentication failed", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (path.startsWith("/smarthome-core")) {
            path = path.substring("/smarthome-core".length());
        }
        return path.startsWith("/pub/");
    }

    private String extractAccessTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(Constants.JWT_BEARER)) return bearerToken.substring(7);
        return null;
    }

    private void sendError(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String error = JsonUtils.toJson(ResponseAPI.error(message));
        response.getWriter().write(error);
    }
}