package com.example.demo.security.filter;

import com.example.demo.security.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class JWTCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();

        // 인증이 필요 없는 경로는 필터링하지 않음
        if (uri.startsWith("/api/auth")) {
            return true;
        }

//        if (uri.startsWith("/api/product")) {
//            return true;
//        }

        if (uri.startsWith("/api/admin/register")) {
            return true;
        }

        if (uri.startsWith("/api/storeowner/luggagemove")) {
            return true;
        }

        if (uri.startsWith("/api/storeowner/luggagestorage")) {
            return true;
        }

        if (uri.startsWith("/api/member/save")) {
            return true;
        }

        if (uri.startsWith("/api/cart/save")) {
            return true;
        }

        return false;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Authorization header missing or invalid for URI: {}", request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Authorization header missing or invalid\"}");
            return;
        }

        String token = authHeader.substring(7);

        try {
            // JWT 토큰 검증
            Map<String, Object> claims = jwtUtil.validateToken(token, false);

            // 사용자 정보 추출
            String username = (String) claims.get("id");
            String role = (String) claims.get("role");

            if (role == null || role.isBlank()) {
                throw new IllegalArgumentException("Role not found in JWT claims");
            }

            // 권한 설정
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            // SecurityContext에 인증 정보 설정
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            log.info("User '{}' authenticated with role '{}'", username, role);
            filterChain.doFilter(request, response);

        } catch (IllegalArgumentException e) {
            log.error("Invalid JWT claims: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\": \"Invalid JWT claims\"}");
        } catch (Exception e) {
            log.error("Unauthorized access attempt: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Unauthorized: Invalid or expired token\"}");
        }
    }
}
