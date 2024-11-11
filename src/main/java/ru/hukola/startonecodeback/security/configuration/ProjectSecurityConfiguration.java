package ru.hukola.startonecodeback.security.configuration;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import ru.hukola.startonecodeback.security.filter.CsrfCookieFilter;
import ru.hukola.startonecodeback.security.filter.JWTTokenGeneratorFilter;
import ru.hukola.startonecodeback.security.filter.JWTTokenValidatorFilter;
import ru.hukola.startonecodeback.security.filter.RequestValidationBeforeFilter;
import ru.hukola.startonecodeback.user.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class ProjectSecurityConfiguration {
    private final UserService userService;

    @Value("${start-one-code.security.csrf-prefix}")
    private String prefix;

    @Value("${start-one-code.frontend-address}")
    private String frontedAddress;

    @Value("${start-one-code.security.jwt-header}")
    private String jwtHeader;

    @Value("${start-one-code.security.token-expiration-time}")
    private long tokenExpirationTime;

    @Value("${start-one-code.security.jwt-key}")
    private String jwtKey;

    @Value("${start-one-code.application.name}")
    private String applicationName;

    @Value("${start-one-code.application.token-name}")
    private String tokenName;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        Set<String> csrfAllowedMethods = Set.of("GET", "HEAD", "TRACE", "OPTIONS");
        CookieCsrfTokenRepository csrfTokenRepository = new CookieCsrfTokenRepository();
        csrfTokenRepository.setCookieCustomizer(builder -> {
            builder.value(UUID.randomUUID().toString()).sameSite("string").maxAge(8600).httpOnly(false).build();
        });
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(request ->
                        new CorsServices(frontedAddress, jwtHeader, tokenExpirationTime).getCorsConfiguration(request))
                )
                .csrf(csrf ->
                        csrf.ignoringRequestMatchers("/login", "/logout", "/register")
                        .requireCsrfProtectionMatcher(request -> !csrfAllowedMethods.contains(request.getMethod()))
                        .csrfTokenRepository(csrfTokenRepository)
                        .csrfTokenRequestHandler(requestHandler)
                )
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JWTTokenGeneratorFilter(userService, jwtKey, applicationName,
                                tokenName, jwtHeader, tokenExpirationTime), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTTokenValidatorFilter(jwtKey, jwtHeader), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/lessons/**").authenticated()
                        .requestMatchers("/login", "/logout", "/register").permitAll())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}

@AllArgsConstructor
class CorsServices implements CorsConfigurationSource {
    private final String frontedAddress;
    private final String jwtHeader;
    private final long tokenExpirationTime;

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList(frontedAddress));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setExposedHeaders(List.of(jwtHeader));
        config.setMaxAge(tokenExpirationTime);
        return config;
    }
}