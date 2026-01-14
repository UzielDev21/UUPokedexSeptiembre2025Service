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

    /*
        *aqui se indica en que endpoint no se debe de ejecutar el filtro
        *principalmente para endpoint publicos
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getServletPath();
        return //endpoint donde se genera el token
                path.equals("/api/login")
                || path.equals("/api/logout")
                //ruta de login form
                || path.equals("/auth/login")
                || path.equals("/auth/logout")
                //recursos publicos
                || path.startsWith("/static.css/")
                || path.startsWith("/static.js/")
                //endpoint publicos
                || path.startsWith("/api/pokedex/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Si no hay token, continuar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        // Validar estructura y firma del token
        if (!jwtService.isTokenValid(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Inválido");
            return;
        }

        Claims claims = jwtService.GetAllClaims(token);
        String username = claims.getSubject();
        String jti = claims.getId();

        // Invalidación de token por logout
        if (tokenBlackListService.isTokenInvalid(jti)) {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Token inhabilitado por logout"
            );
            return;
        }

        // Invalidación por limite de uso del token
        if (jwtTokenUsoService.excedioLimite(jti)) {

            tokenBlackListService.invalidateToken(jti);
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Token expirado por limite de uso, Vuelve a iniciar sesión"
            );

            return;
        }
        jwtTokenUsoService.registrarUso(jti);

        //Autenticación 
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
