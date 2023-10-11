package br.com.kaiopostal.todolist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/primeira-rota")
public class PrimeiroController {

    @GetMapping("")
    public String primeiraMensagem() {
        return  "Olá mundo";
    }
}
