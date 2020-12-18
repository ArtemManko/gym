package by.pvt.spring.webproject.entities.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Level implements GrantedAuthority {
    BEGINNER, AMATEUR, PROFESSIONAL;

    @Override
    public String getAuthority() {
        return name();
    }
}
