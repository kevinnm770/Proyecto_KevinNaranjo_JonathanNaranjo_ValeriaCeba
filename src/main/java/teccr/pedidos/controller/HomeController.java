package teccr.pedidos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** Sirve las paginas publicas: inicio, login y registro. */
@Controller
public class HomeController {

    @GetMapping("/")
    public String inicio() {
        return "index";   // templates/index.html
    }

    @GetMapping("/login")
    public String login() {
        return "login";   // templates/login.html
    }

    @GetMapping("/registro")
    public String registro() {
        // TODO: pasar un objeto Usuario vacio al modelo para el formulario de registro.
        return "registro";
    }
}
