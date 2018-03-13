package com.chatbot.apiBanco.model.database.tables;

import javax.persistence.*;
import mx.openpay.client.Card;
import java.util.Date;

@Entity
public class TarjetaLog {

    @Id
    @Column(name = "idTarjeta")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idTarjeta;

    private Date creationDate;

    private String id;

    private Long idCliente;


    public TarjetaLog(Card response) {
        this.creationDate = response.getCreationDate();
        this.id = response.getId();
    }
    
    public void setCliente(Long cl){
        this.idCliente = cl;
    }

	public void setId(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public TarjetaLog() {
    }
}
