package ru.fqw.TestingServis.site.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.fqw.TestingServis.site.models.user.User;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {
    private Long id;
    private final String username;
    private String password;
    private boolean activite;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String password, boolean activite, String email, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.activite = activite;
        this.email = email;
        this.authorities = authorities;
    }

    public UserDetailsImpl(Long id, String username, String password, boolean activite, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.activite = activite;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User userEntity) {
        List<GrantedAuthority> authorityList = List.of(new SimpleGrantedAuthority(userEntity.getRole().name()));
        return new UserDetailsImpl(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isActivite(),
                userEntity.getEmail(),
                authorityList);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return activite;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return activite;
    }

    @Override
    public boolean isEnabled() {
        return activite;
    }
}