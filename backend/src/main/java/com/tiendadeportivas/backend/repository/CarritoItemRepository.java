package com.tiendadeportivas.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendadeportivas.backend.model.CarritoItem;

public interface CarritoItemRepository
        extends JpaRepository<CarritoItem, Long> {

    Optional<CarritoItem> findByCarritoIdAndProductoIdAndTallaAndColor(
            Long carritoId,
            Long productoId,
            int talla,
            String color);
}