package api_teste.ds.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import api_teste.ds.models.User;
import api_teste.ds.models.enums.ProfileEnums;

public class UserSpringSecurity extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = 1L;

    private final Long id;

    public UserSpringSecurity(User user) {
        super(
                user.getUsername(),
                user.getPassword(),
                user.getProfiles().stream()
                        .map(ProfileEnums::getDescription)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()));
        this.id = user.getId();
    }

    public Long getId() {
        return this.id;
    }

    public boolean hasRole(ProfileEnums profile) {
        return getAuthorities().contains(
                new SimpleGrantedAuthority(profile.getDescription()));
    }
}