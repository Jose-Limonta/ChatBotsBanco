package com.chatbot.apiBanco.model.database.tables;

import javax.persistence.*;

import com.chatbot.apiBanco.model.client.Client;

import java.util.List;

@Entity
public class ClienteLog {

    @Id
    @Column(name = "idcliente")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idcliente;

    private String token;
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
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public ClienteLog (Client cli){
        this.email = cli.getEmail();
        this.nombre = cli.getName() + " " + cli.getLastName();
        this.token = cli.getId();
        this.idBanco = cli.getExternalId();
    }

    public ClienteLog() {
    }

    public Long getId(){
        return this.idcliente;
    }
}
