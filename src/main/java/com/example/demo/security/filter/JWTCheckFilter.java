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

        if (uri.startsWith("/api/admin/register")) {
            return true;
        }

        if (uri.startsWith("/api/admin/spot/user/list")) {
            return true;
        }

        if (uri.startsWith("/api/storeowner/luggagemove/create")) {
            return true;
        }

        if (uri.startsWith("/api/storeowner/luggagestorage/create")) {
            return true;
        }

        if (uri.startsWith("/api/storeowner/order/receive")) {
            return true;
        }

        if (uri.startsWith("/api/admin/register")) {
            return true;
        }

        if (uri.startsWith("/api/admin/member/save")) {
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
            log.warn("Missing Authorization header for URI: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            Map<String, Object> claims = jwtUtil.validateToken(token, false);

            String username = (String) claims.get("id");
            String role = (String) claims.get("role");

            if (role == null || role.isBlank()) {
                throw new IllegalArgumentException("Role not found in JWT claims");
            }

            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            log.info("User '{}' authenticated with role '{}'", username, role);

        } catch (Exception e) {
            log.error("Authentication error: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
