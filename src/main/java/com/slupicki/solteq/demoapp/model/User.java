package com.slupicki.solteq.demoapp.model;

import com.slupicki.solteq.demoapp.common.Util;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class User {
    @Id
    private UUID id = UUID.randomUUID();

    private String login;
    private String password;
    private Access access;

    public User() {
    }

    public User(String login, String password, Access access) {
        this.login = login;
        this.password = Util.hashPassword(password);
        this.access = access;
    }

    public UUID getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = Util.hashPassword(password);
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public enum Access {
        ADMIN,
        REGULAR
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("login", login)
                .append("password", password)
                .append("access", access)
                .toString();
    }

}
