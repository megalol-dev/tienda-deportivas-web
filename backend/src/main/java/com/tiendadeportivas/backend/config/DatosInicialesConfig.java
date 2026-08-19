// Carga el catálogo inicial si la base de datos está vacía.
package com.tiendadeportivas.backend.config;

import java.io.InputStream;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.tiendadeportivas.backend.model.Producto;
import com.tiendadeportivas.backend.repository.ProductoRepository;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Component
public class DatosInicialesConfig implements CommandLineRunner {

    private final ProductoRepository productoRepository;
    private final ObjectMapper objectMapper;

    // Crea una instancia de DatosInicialesConfig.
    public DatosInicialesConfig(
            ProductoRepository productoRepository,
            ObjectMapper objectMapper) {

        this.productoRepository = productoRepository;
        this.objectMapper = objectMapper;
    }

    // Importa el catálogo inicial cuando corresponde.
    @Override
    public void run(String... args) throws Exception {

        if (productoRepository.count() > 0) {

            System.out.println(
                    "Catálogo ya existente en MySQL. No se realiza la importación.");

            return;
        }

        ClassPathResource recurso = new ClassPathResource("catalogo.json");

        try (InputStream inputStream = recurso.getInputStream()) {

            List<Producto> productos = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<Producto>>() {
                    });

            for (Producto producto : productos) {

                producto.setActivo(true);

                producto.setId(null);
            }

            productoRepository.saveAll(productos);

            System.out.println(
                    "Catálogo importado correctamente a MySQL.");

            System.out.println(
                    "Productos importados: " + productos.size());
        }
    }
}
