package com.lemberskay.reactpizza.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import java.util.Arrays;
import java.util.Collection;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class User extends AbstractEntity implements UserDetails {
    @Email
    private String login;
    private String password;
    private Role role;

    public enum Role {
        ROLE_ADMIN,
        ROLE_USER,
    }

    public User(long id,String login, String password){
        super(id);
        this.login = login;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(getRole().toString()));
    }

    @Override
    public String getUsername() {
        return getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
