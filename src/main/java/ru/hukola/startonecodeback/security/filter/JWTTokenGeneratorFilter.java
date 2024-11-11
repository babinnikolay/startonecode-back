package ru.hukola.startonecodeback.security.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.hukola.startonecodeback.user.User;
import ru.hukola.startonecodeback.user.UserService;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String jwtKey;
    private final String applicationName;
    private final String tokenName;
    private final String jwtHeader;
    private long tokenExpirationTime;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (null != authentication) {
            SecretKey key = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
            User user = userService.findUserByName(authentication.getName());
            UUID uuid = user != null ? user.getUuid() : null;
            String email = user != null ? user.getEmail() : null;

            String[] param = request.getParameterValues("remember");
            if (param != null && param.length > 0)
            {
                boolean isRemember = Boolean.parseBoolean(param[0]);
                if (isRemember)
                    tokenExpirationTime += (86400000L * 30);
            }

            String jwt = Jwts.builder()
                    .issuer(applicationName)
                    .subject(tokenName)
                    .claim("username", authentication.getName())
                    .claim("email", email)
                    .claim("uuid", uuid)
                    .issuedAt(new Date())
                    .expiration(new Date((new Date()).getTime() + tokenExpirationTime))
                    .signWith(key).compact();
            response.setHeader(jwtHeader, jwt);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/login");
    }
}