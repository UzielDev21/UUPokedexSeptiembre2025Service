package com.UU.UUPokedexSeptiembre2025Service.DAO;

import com.UU.UUPokedexSeptiembre2025Service.JPA.Result;
import com.UU.UUPokedexSeptiembre2025Service.JPA.RolesJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class RolesJPADAOImplementation implements IRolesJPA {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Result GetAllJPA() {
        Result result = new Result();

        try {

            TypedQuery<RolesJPA> rolesJPA = entityManager.createQuery(
                    "FROM RolesJPA", RolesJPA.class);
            List<RolesJPA> roles = rolesJPA.getResultList();

            result.objects = (List<Object>) (List<?>) roles;
            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

}
