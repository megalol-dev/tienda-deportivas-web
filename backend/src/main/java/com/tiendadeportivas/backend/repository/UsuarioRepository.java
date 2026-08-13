package com.tiendadeportivas.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendadeportivas.backend.model.RolUsuario;
import com.tiendadeportivas.backend.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    // =====================================================
    // OBTENER USUARIOS POR ROLES
    // -----------------------------------------------------
    // Permite recuperar únicamente los usuarios
    // pertenecientes al personal de UrbanSneakers.
    // =====================================================

    List<Usuario> findByRolInOrderByIdAsc(List<RolUsuario> roles);
}
