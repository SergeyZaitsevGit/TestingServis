package ru.fqw.TestingServis.site.config;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.repo.UserRepository;

@Service
@AllArgsConstructor
public class UserDetailsServisImpl implements UserDetailsService {
    UserRepository userRepo;

    @Override
    @Transactional
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User with email '" + username + "' not found"));
        return UserDetailsImpl.build(user);
    }
}