package by.pvt.spring.webproject.entities.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Day implements GrantedAuthority {
   MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY;

    @Override
    public String getAuthority() {
        return name();
    }
}
