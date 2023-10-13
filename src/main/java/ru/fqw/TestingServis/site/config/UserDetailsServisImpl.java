package ru.fqw.TestingServis.site.config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fqw.TestingServis.site.models.User;
import ru.fqw.TestingServis.site.repo.UserRepository;

@Service
@AllArgsConstructor
public class UserDetailsServisImpl implements UserDetailsService {
    UserRepository userRepo;
    @Override
    @Transactional
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepo.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException(
                        "User with email '"+username+"' not found"));
        return UserDetailsImpl.build(userEntity);
    }
}