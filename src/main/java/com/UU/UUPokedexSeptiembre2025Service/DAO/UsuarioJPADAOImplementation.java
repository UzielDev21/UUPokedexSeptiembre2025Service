package com.UU.UUPokedexSeptiembre2025Service.DAO;

import com.UU.UUPokedexSeptiembre2025Service.DTO.UsuarioRegisterDTO;
import com.UU.UUPokedexSeptiembre2025Service.JPA.Result;
import com.UU.UUPokedexSeptiembre2025Service.JPA.UsuariosJPA;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioJPADAOImplementation implements IUsuarioJPA {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private ModelMapper modelMapper; 

//-----------------------------------Metodos de Consulta-----------------------------------//    
    /*
--------------------------------------
    Metodo que permite la visualización de todos los datos de la BD
--------------------------------------    
     */
    @Override
    public Result GetAll() {
        Result result = new Result();

        try {
            TypedQuery<UsuariosJPA> queryUsuarios = entityManager.createQuery("FROM UsuariosJPA", UsuariosJPA.class);
            List<UsuariosJPA> usuariosJPA = queryUsuarios.getResultList();
            result.objects = (List<Object>) (List<?>) usuariosJPA;
            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

    /*
--------------------------------------
    Metodo que permite la visualización de un solo dato de la BD
--------------------------------------    
     */
    @Override
    public Result GetByIdUser(int user_Id) {
        Result result = new Result();

        try {
            UsuariosJPA usuariosJPA = entityManager.find(UsuariosJPA.class, user_Id);
            result.object = usuariosJPA;
            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

//-----------------------------------Metodos de verificación-----------------------------------//    

    /*
--------------------------------------
    Metodo que permite verificar el correo del usuario registrado
--------------------------------------    
     */
    @Override
    public Result VerifyUser(int user_Id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

//-----------------------------------Metodos Crud-----------------------------------//
    /*
--------------------------------------
    Metodo que permite el almcenamiento de un nuevo usuarios
--------------------------------------    
     */
    @Override
    @Transactional
    public Result AddJPA(UsuarioRegisterDTO usuarioDTO) {
        Result result = new Result();

        try {
            TypedQuery<UsuariosJPA> queryUsuario
                    = entityManager.createQuery("FROM UsuariosJPA usuarioJPA "
                            + "WHERE usuarioJPA.userName = :username", UsuariosJPA.class)
                            .setParameter("username", usuarioDTO.getUserName());
            List<UsuariosJPA> usuarios = queryUsuario.getResultList();
            
            if (!usuarios.isEmpty()) {
                throw new EntityExistsException("El username " + usuarioDTO.getUserName() + " ya existe en la base de datos");
            }
            
//            if (usuarioDTO.favoritosJPA != null && !usuarioDTO.favoritosJPA.isEmpty()) {
//                usuarioDTO.favoritosJPA.forEach(favoritos -> favoritos.usuarioDTO = usuarioDTO);
//            }
            
//            String passwordPlano = usuarioDTO.getPassword_Hash();
            UsuariosJPA usuarioEntity = modelMapper.map(usuarioDTO, UsuariosJPA.class);
            entityManager.persist(usuarioEntity);
            result.correct = true;
            
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

    /*
--------------------------------------
    Metodo que modificar la informaci[on del dato almacenado en la BD
--------------------------------------    
     */
    @Override
    public Result Update(UsuariosJPA usuariosJPA) {
        Result result = new Result();
        
        try {
            UsuariosJPA usuarioPutJPA = entityManager.find(UsuariosJPA.class, usuariosJPA.getUser_Id());
            
            if (usuarioPutJPA == null) {
                if (usuariosJPA.favoritosJPA != null || usuariosJPA.favoritosJPA.isEmpty()) {
                    
//                    entityManager.persist(usuario);
                    
                } else {
                }
            }
            
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

    /*
--------------------------------------
    Metodo que permite la eliminaci[on de un usuario
--------------------------------------    
     */
    @Override
    public Result Delete(int user_id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
