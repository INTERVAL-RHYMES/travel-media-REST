package com.example.travelmediarest.service;

import com.example.travelmediarest.model.User;
import com.example.travelmediarest.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class DefaultUserDetailService implements UserDetailsService {
    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByMail(username);
        User user = null;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        }
//        log.info("loaduser: "+user.getMail()+" "+user.getPassword()+" "+user.getAuthorities());
        if (user != null) {
            return new org.springframework.security.core.userdetails.User(user.getMail(), user.getPassword(), user.getAuthorities());
        }
        throw new UsernameNotFoundException("Invalid email '" + username + "' or password ");
    }
}
