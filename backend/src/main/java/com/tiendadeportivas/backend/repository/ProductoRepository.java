// Accede a los productos almacenados.
package com.tiendadeportivas.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendadeportivas.backend.model.Producto;

public interface ProductoRepository
        extends JpaRepository<Producto, Long> {

    // Busca los productos activos ordenados por identificador.
    List<Producto> findByActivoTrueOrderByIdAsc();
}
