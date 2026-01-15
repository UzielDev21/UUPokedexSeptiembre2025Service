package com.UU.UUPokedexSeptiembre2025Service.Filter;

import com.UU.UUPokedexSeptiembre2025Service.Service.ITokenInvalidationService;
import com.UU.UUPokedexSeptiembre2025Service.Service.JwtService;
import com.UU.UUPokedexSeptiembre2025Service.Service.JwtTokenUsoService;
import com.UU.UUPokedexSeptiembre2025Service.Service.TokenBlackListService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenBlackListService tokenBlackListService;
    private final JwtTokenUsoService jwtTokenUsoService;

    public JwtAuthFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            TokenBlackListService tokenBlackListService,
            JwtTokenUsoService jwtTokenUsoService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenBlackListService = tokenBlackListService;
        this.jwtTokenUsoService = jwtTokenUsoService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.equals("/api/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtService.isTokenValid(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Inválido");
            return;
        }

        Claims claims = jwtService.GetAllClaims(token);
        String username = claims.getSubject();
        String jti = claims.getId();

        if (tokenBlackListService.isTokenInvalid(jti)) {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Token inhabilitado por logout"
            );

            System.out.println("Token bloqueado por BlackList (Redis): " + jti);
            return;
        }

        if (jwtTokenUsoService.excedioLimite(jti)) {

            tokenBlackListService.invalidateToken(jti);
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Token expirado por limite de uso, Vuelve a iniciar sesión"
            );

            return;
        }

        jwtTokenUsoService.registrarUso(jti);

        if (SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!userDetails.isEnabled()) {
                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Usuario deshabilitado"
                );
                return;
            }

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }

}
