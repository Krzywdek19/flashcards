package com.krzywdek19.flashcards.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LoginUserDto {
    private String username;
    private String password;
}
