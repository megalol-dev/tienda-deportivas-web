package com.tiendadeportivas.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendadeportivas.backend.model.Carrito;

public interface CarritoRepository
        extends JpaRepository<Carrito, Long> {

    Optional<Carrito> findByUsuarioEmail(String email);
}