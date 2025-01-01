package com.tripwhiz.tripwhizadminback.security.config;

import com.tripwhiz.tripwhizadminback.security.filter.JWTCheckFilter;
import com.tripwhiz.tripwhizadminback.security.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@Log4j2
public class CustomSecurityConfig {

    private final JWTUtil jwtUtil;

    @Value("${server.url}")
    private String serverUrl;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/admin/register",
                                "/api/admin/spot/user/list", // 지점 조회 경로(유저)
                                "/api/admin/product/image",
                                "/api/nationality/**",
                                "/api/stock/**",
                                "/api/storeowner/luggagemove/create",
                                "/api/storeowner/luggagestorage/create",
                                "/api/admin/member/save",
                                "/api/storeowner/order/receive",
                                "/api/cart/**",
                                "/error",
                                "/api/scraping/spots", // 지점 스크래핑 경로
                                "/"
                        ).permitAll()
                        .requestMatchers("/api/admin/spot/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/product/image/**").permitAll()
                        .requestMatchers("/api/admin/product/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/member/list").hasRole("ADMIN")
                        .requestMatchers("/api/storeowner/**").hasRole("STOREOWNER")
                        .requestMatchers("/api/storeowner/luggagemove/**").hasRole("STOREOWNER")
                        .requestMatchers("/api/storeowner/luggagestorage/**").hasRole("STOREOWNER")

                        .anyRequest().authenticated()

                )
                .anonymous(anonymous -> anonymous
                        .authorities("ROLE_ANONYMOUS")
                )
                .addFilterBefore(new JWTCheckFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.error("Unauthorized request - {}", authException.getMessage());
                            response.sendError(401, "Unauthorized");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            log.error("Access denied - {}", accessDeniedException.getMessage());
                            response.sendError(403, "Access Denied");
                        })
                );

        log.info("Security configuration applied successfully");
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("https://tripwhiz.store", "https://tripwhiz.shop"));
        configuration.setAllowedOriginPatterns(List.of(serverUrl));
//        configuration.setAllowedOriginPatterns(List.of("http://localhost:3000", "http://localhost:5173", serverUrl));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
