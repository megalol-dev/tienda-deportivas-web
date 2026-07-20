package com.tiendadeportivas.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiendadeportivas.backend.model.Saludo;

@RestController
public class SaludoController {

    @GetMapping("/api/saludo")
    public Saludo saludo() {

        Saludo saludo = new Saludo("Servidor funcionando correctamente, esto es un objeto en JAVA");

        return saludo;
    }

}
