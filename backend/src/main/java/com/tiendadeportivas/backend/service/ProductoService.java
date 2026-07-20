package com.tiendadeportivas.backend.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.tiendadeportivas.backend.model.Producto;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Service
public class ProductoService {

    private final ObjectMapper objectMapper;

    public ProductoService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<Producto> obtenerProductos() {

        ClassPathResource recurso = new ClassPathResource("catalogo.json");

        try (InputStream inputStream = recurso.getInputStream()) {

            return objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<Producto>>() {
                    });

        } catch (IOException e) {
            throw new RuntimeException(
                    "No se pudo leer el catálogo de productos",
                    e);
        }
    }
}
