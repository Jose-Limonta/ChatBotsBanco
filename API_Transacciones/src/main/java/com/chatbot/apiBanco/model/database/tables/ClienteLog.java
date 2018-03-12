package com.chatbot.apiBanco.model.database.tables;

import javax.persistence.*;
import java.util.List;

@Entity
public class ClienteLog {

    @Id
    @Column(name = "idcliente")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idcliente;

    private String Token;
    private String idBanco;
    private String nombre;
    private String email;

    @OneToMany(cascade= CascadeType.ALL)
    @JoinColumn (name="idCliente")
    private List<TarjetaLog> tarjetas;

    @OneToMany(cascade= CascadeType.ALL)
    @JoinColumn (name="idCliente")
    private List<CuentaLog> cuentas;


    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(String idBanco) {
        this.idBanco = idBanco;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
