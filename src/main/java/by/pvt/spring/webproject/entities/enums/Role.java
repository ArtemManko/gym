package by.pvt.spring.webproject.entities.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN, COACH, CLIENT;

    @Override
    public String getAuthority() {
        return name();
    }
}
