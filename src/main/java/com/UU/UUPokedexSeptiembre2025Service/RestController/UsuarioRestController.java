package com.UU.UUPokedexSeptiembre2025Service.RestController;

import com.UU.UUPokedexSeptiembre2025Service.DAO.UsuarioJPADAOImplementation;
import com.UU.UUPokedexSeptiembre2025Service.JPA.Result;
import com.UU.UUPokedexSeptiembre2025Service.JPA.UsuariosJPA;
import com.UU.UUPokedexSeptiembre2025Service.Service.EmailVerificationTokenService;
import com.UU.UUPokedexSeptiembre2025Service.Service.UsuarioService;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("usuario")
public class UsuarioRestController {

    @Autowired
    private UsuarioJPADAOImplementation usuarioJPADAOImplementation;

    @Autowired
    private EmailVerificationTokenService emailVerificationTokenService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/{user_Id}")
    public ResponseEntity GetById(@PathVariable("user_Id") int user_Id) {

        Result result = new Result();

        try {

            if (user_Id != 0) {

                result = usuarioJPADAOImplementation.GetByIdUser(user_Id);
                result.correct = true;
                result.status = 200;

            } else {
                result.correct = false;
                result.errorMessage = "No se encuentra el dato solicitado";
                result.status = 400;
            }

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.status = 500;

        }
        return ResponseEntity.status(result.status).body(result);
    }

    @PostMapping("/registrar")
    public ResponseEntity AddUsuario(@RequestBody UsuariosJPA usuariosJPA) {

        Result result = new Result();

        try {
            result = usuarioJPADAOImplementation.AddJPA(usuariosJPA);

            if (result.status == 0) {

                if (result.correct) {

                    result.status = 201;

                } else {

                    if (result.ex instanceof EntityExistsException) {

                        result.status = 409;

                    } else {
                        result.status = 500;
                    }
                }
            }
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.status = 500;
        }
        return ResponseEntity.status(result.status).body(result);
    }
    
}
