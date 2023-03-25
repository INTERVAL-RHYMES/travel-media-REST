package com.example.travelmediarest.dto;

import lombok.Data;

@Data
public class UserDto {
    private String mail;
    private String password;
    private String username;
    private String role;

    public UserDto(String mail, String password, String username, String role) {
        this.mail = mail;
        this.password = password;
        this.username = username;
        this.role = role;
    }

    public UserDto() {

    }
}
