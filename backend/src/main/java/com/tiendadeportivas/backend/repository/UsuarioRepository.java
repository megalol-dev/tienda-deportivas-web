// Accede a los usuarios almacenados.
package com.tiendadeportivas.backend.repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import com.tiendadeportivas.backend.model.RolUsuario;
import com.tiendadeportivas.backend.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca un usuario por email.
    Optional<Usuario> findByEmail(String email);

    // Busca un usuario por email y bloquea su fila durante la transacción.
    // Busca un usuario por email y bloquea su fila durante la transacción.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    Optional<Usuario> findByEmailForUpdate(
            @Param("email") String email);

    // Comprueba si ya existe un email.
    boolean existsByEmail(String email);

    // Busca usuarios por rol ordenados por identificador.
    List<Usuario> findByRolInOrderByIdAsc(List<RolUsuario> roles);
}
