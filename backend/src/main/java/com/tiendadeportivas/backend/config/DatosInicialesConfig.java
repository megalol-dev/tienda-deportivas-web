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

    public DatosInicialesConfig(
            ProductoRepository productoRepository,
            ObjectMapper objectMapper) {

        this.productoRepository = productoRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {

        // =====================================================
        // COMPROBAR SI YA EXISTEN PRODUCTOS
        // -----------------------------------------------------
        // La migración solo se realiza cuando la tabla
        // productos está completamente vacía.
        // =====================================================

        if (productoRepository.count() > 0) {

            System.out.println(
                    "Catálogo ya existente en MySQL. No se realiza la importación.");

            return;
        }

        // =====================================================
        // LEER CATÁLOGO JSON
        // =====================================================

        ClassPathResource recurso = new ClassPathResource("catalogo.json");

        try (InputStream inputStream = recurso.getInputStream()) {

            List<Producto> productos = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<Producto>>() {
                    });

            // =================================================
            // PREPARAR PRODUCTOS
            // =================================================

            for (Producto producto : productos) {

                /*
                 * Todos los productos importados inicialmente
                 * estarán disponibles en la tienda.
                 */

                producto.setActivo(true);

                /*
                 * IMPORTANTE:
                 *
                 * Producto utiliza IDENTITY.
                 * Dejamos que MySQL genere los identificadores.
                 *
                 * Como la tabla está vacía y el JSON está
                 * ordenado del producto 1 al 50, esperamos
                 * obtener los identificadores 1-50.
                 */

                producto.setId(null);
            }

            // =================================================
            // GUARDAR CATÁLOGO
            // =================================================

            productoRepository.saveAll(productos);

            System.out.println(
                    "Catálogo importado correctamente a MySQL.");

            System.out.println(
                    "Productos importados: " + productos.size());
        }
    }
}
