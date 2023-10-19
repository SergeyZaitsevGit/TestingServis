package ru.fqw.TestingServis.site.servise;

import lombok.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.site.models.User;
import ru.fqw.TestingServis.site.repo.UserRepository;

@Service
@AllArgsConstructor
public class UserServise {
    UserRepository userRepository;
    private BCryptPasswordEncoder encoder(){return new BCryptPasswordEncoder();}
    public User saveUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            user.setPassword(encoder().encode(user.getPassword()));
            user.setActivite(true);
            return userRepository.save(user);
        }
        return null;
    }

    public User getAuthenticationUser(){return  userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow();
    }

}
