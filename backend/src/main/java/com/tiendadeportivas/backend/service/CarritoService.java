package com.tiendadeportivas.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tiendadeportivas.backend.model.CarritoItem;

@Service
public class CarritoService {

    private final List<CarritoItem> carrito = new ArrayList<>();

    public List<CarritoItem> obtenerCarrito() {
        return carrito;
    }

    public void agregarProducto(CarritoItem item) {

        for (CarritoItem existente : carrito) {

            if (existente.getIdProducto() == item.getIdProducto()
                    && existente.getTalla() == item.getTalla()
                    && existente.getColor().equals(item.getColor())) {

                existente.setCantidad(
                        existente.getCantidad() + item.getCantidad());

                return;
            }
        }

        carrito.add(item);
    }

    public void eliminarProducto(int idProducto, int talla, String color) {

        carrito.removeIf(item -> item.getIdProducto() == idProducto
                && item.getTalla() == talla
                && item.getColor().equals(color));
    }

    public void vaciarCarrito() {
        carrito.clear();
    }

}
