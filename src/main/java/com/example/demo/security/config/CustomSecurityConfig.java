package com.example.demo.security.config;

import com.example.demo.security.filter.JWTCheckFilter;
import com.example.demo.security.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
@RequiredArgsConstructor
@Log4j2
public class CustomSecurityConfig {

    private final JWTUtil jwtUtil;

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
                        // 공통적으로 인증 없이 접근 가능한 경로
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/admin/register",
                                "/api/nationality/**",
                                "/api/stock/**",
                                "/api/storeowner/luggagemove/**",
                                "/api/storeowner/luggagestorage/**",
                                "/api/member/**",
                                "api/cart/**"
                        ).permitAll()
                        .requestMatchers("/api/auth/**", "/api/admin/register", "/api/nationality/**", "/api/stock/**", "/api/product/image/**").permitAll()

                        // 관리자 전용 경로
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 점주 전용 경로
                        .requestMatchers("/api/storeowner/**").hasRole("STOREOWNER")

                        .requestMatchers("/api/storeowner/**").hasRole("STOREOWNER")

                        .requestMatchers("/api/member/**").permitAll()
                        .requestMatchers("/api/cart/**").permitAll()

                        // 인증 필요 경로
                        //.requestMatchers(HttpMethod.GET, "/api/admin/storeOwners").authenticated()
                        .anyRequest().authenticated()
                )
                .anonymous(anonymous -> anonymous
                        .authorities("ROLE_ANONYMOUS") // 익명 사용자에게 ROLE_ANONYMOUS 부여
                )
                .addFilterBefore(new JWTCheckFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.error("Unauthorized request - {}", authException.getMessage());
                            log.error("Request URI: {}", request.getRequestURI());

                            authException.printStackTrace();

                            response.sendError(401, "Unauthorized");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            log.error("Access denied - {}", accessDeniedException.getMessage());
                            log.error("Request URI: {}", request.getRequestURI());


                            accessDeniedException.printStackTrace();

                            response.sendError(403, "Access Denied");
                        })

                );

        log.info("Security configuration applied successfully");
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:3000", "http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cache-Control"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        log.info("CORS configuration applied successfully");
        return source;
    }
}
