package com.chatbot.apiBanco.model.database.tables;

import javax.persistence.*;
import mx.openpay.client.BankAccount;

@Entity
public class CuentaLog {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String idCuenta;
    private String bank;
    private String holder;

    private long idCliente;

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

    public void setCliente(Long cl){
        this.idCliente = cl;
    }

    public CuentaLog(BankAccount ba){
        this.bank = ba.getBankName();
        this.holder = ba.getHolderName();
        this.idCuenta = ba.getId();
        //this.client = ba.ge
    }

    public CuentaLog() {
    }
}
