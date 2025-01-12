package com.krzywdek19.flashcards;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {
    @GetMapping("/test")
    public String hello(){
        return "test";
    }
}
