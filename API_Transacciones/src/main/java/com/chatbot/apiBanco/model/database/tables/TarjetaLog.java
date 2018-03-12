package com.chatbot.apiBanco.model.database.tables;

import javax.persistence.*;
import java.util.Date;

@Entity
public class TarjetaLog {

    @Id
    @Column(name = "idTarjeta")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idTarjeta;

    private Date creationDate;

    private String id;


    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private ClienteLog cliente;

    public String getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}
