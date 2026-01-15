package com.UU.UUPokedexSeptiembre2025Service.RestController;

import com.UU.UUPokedexSeptiembre2025Service.DAO.IUsuarioRepository;
import com.UU.UUPokedexSeptiembre2025Service.JPA.Result;
import com.UU.UUPokedexSeptiembre2025Service.JPA.UsuariosJPA;
import com.UU.UUPokedexSeptiembre2025Service.Service.JwtService;
import com.UU.UUPokedexSeptiembre2025Service.Service.TokenBlackListService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class LoginRestController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final IUsuarioRepository iUsuarioRepository;
    private final TokenBlackListService tokenBlackListService;
    
    public LoginRestController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            IUsuarioRepository iUsuarioRepository,
            TokenBlackListService tokenBlackListService) throws Exception {
        
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.iUsuarioRepository = iUsuarioRepository;
        this.tokenBlackListService = tokenBlackListService;
    }
    
    @PostMapping("/login")
    public ResponseEntity Login(@RequestBody Map<String, String> json) {
        
        Result result = new Result();
        
        try {
            
            String userName = json.get("userName");
            String password_Hash = json.get("password_Hash");
            
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userName,
                    password_Hash);
            
            try {
                authenticationManager.authenticate(auth);
                
            } catch (Exception ex) {
                result.correct = false;
                result.errorMessage = "Credenciales Invalidas";
                result.status = 401;
                return ResponseEntity.status(result.status).body(result);
            }
            
            UsuariosJPA usuarioJPA = iUsuarioRepository.findByUserName(userName);
            
            if (usuarioJPA == null) {
                result.correct = false;
                result.errorMessage = "Usuario no encontrado";
                result.status = 401;
                return ResponseEntity.status(result.status).body(result);
            }
            
            String rol = usuarioJPA.rolJPA.getRole_Name();
            int user_Id = usuarioJPA.getUser_Id();
            
            String jwt = jwtService.GenerateTokenUser(userName, user_Id, rol);
            result.correct = true;
            result.status = 200;
            result.object = jwt;
            
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.status = 500;
        }
        return ResponseEntity.status(result.status).body(result);
    }

    @PostMapping("/logout")
    public ResponseEntity Logout(HttpServletRequest request) {

        Result result = new Result();

        try {

            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                result.correct = false;
                result.errorMessage = "No se encontro el token";
                result.status = 400;
                return ResponseEntity.status(result.status).body(result);
            }

            String token = authHeader.substring(7);
            
            if (!jwtService.isTokenValid(token)) {
                result.correct = false;
                result.errorMessage = "Token invalidado o expirado";
                result.status = 401;
                return ResponseEntity.status(result.status).body(result);
            }
            
            String jti = jwtService.getJtiFromToken(token);
            tokenBlackListService.invalidateToken(jti);
            SecurityContextHolder.clearContext();
            
            result.correct = true;
            result.status = 200;
            result.object = "Logout Exitoso";

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.status = 500;
        }
        return ResponseEntity.status(result.status).body(result);
    }

}
