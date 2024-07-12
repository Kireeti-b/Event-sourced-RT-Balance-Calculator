package dev.codescreen.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("")
    public String home(){
        return "<h1>Welcome to the Real time Balance Calculator Application</h1>";
    }
}
