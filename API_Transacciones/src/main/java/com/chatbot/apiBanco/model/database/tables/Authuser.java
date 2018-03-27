package com.chatbot.apiBanco.model.database.tables;

import javax.persistence.*;

@Entity
public class Authuser {
    @Id
    @Column(name = "idauthuser")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idauthuser;

    private String username;
    private String password;
    private String rol;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
