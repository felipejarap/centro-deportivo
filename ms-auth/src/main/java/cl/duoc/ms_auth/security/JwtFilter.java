package cl.duoc.ms_auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Leer el header Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. Si no tiene header o no empieza con "Bearer ", dejar pasar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("Request sin token: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraer el token (quitar "Bearer ")
        String token = authHeader.substring(7);

        // 4. Validar el token
        if (!jwtUtil.validateToken(token)) {
            log.warn("Token inválido o expirado en: {}", request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token inválido o expirado");
            return;
        }

        // 5. Extraer el username y autenticar en Spring Security
        String username = jwtUtil.extractUsername(token);
        String rol = jwtUtil.extractRol(token);
        log.debug("Token válido para usuario: {} con rol: {}", username, rol);

        // CORRECCIÓN AQUÍ: Declarar explícitamente el tipo de la lista como GrantedAuthority
        List<GrantedAuthority> authorities = (rol != null && !rol.isBlank())
                ? List.of(new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase()))
                : List.of();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 6. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}