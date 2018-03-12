package com.chatbot.apiBanco.model.database.tables;

import javax.persistence.*;

@Entity
public class CuentaLog {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String idCuenta;
    private String bank;
    private String holder;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private ClienteLog cliente;

    public String getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(String idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }
}
