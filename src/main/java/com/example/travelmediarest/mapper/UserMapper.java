package com.example.travelmediarest.mapper;

import com.example.travelmediarest.dto.UserDto;
import com.example.travelmediarest.model.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class UserMapper {

    public User toUser(UserDto userDto, PasswordEncoder passwordEncoder) {
        return new User(userDto.getMail(), passwordEncoder.encode(userDto.getPassword()), userDto.getUsername());
    }
}
