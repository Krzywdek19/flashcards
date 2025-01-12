package com.krzywdek19.flashcards.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Getter
@Setter
public class UserRegisterDto {
    private String username;
    private String password;
    private String email;
}
