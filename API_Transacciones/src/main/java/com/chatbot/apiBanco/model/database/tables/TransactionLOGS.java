package com.chatbot.apiBanco.model.database.tables;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class TransactionLOGS {


    @Id
    @Column(name = "Transaction_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Transaction_id;


    private Long From_User_ID;

    private Long To_User_ID;


    private Date DATED;
    private String LOGGER;
    private String Level;
    private String MESSAGE;
    private BigDecimal Amount;

    public BigDecimal getAmount() {
        return Amount;
    }

    public void setAmount(BigDecimal amount) {
        Amount = amount;
    }

    public Date getDATED() {
        return DATED;
    }

    public void setDATED(Date DATED) {
        this.DATED = DATED;
    }

    public String getLOGGER() {
        return LOGGER;
    }

    public void setLOGGER(String LOGGER) {
        this.LOGGER = LOGGER;
    }

    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
        Level = level;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public void setCliente(Long cl){
        this.From_User_ID = cl;
    }

    public void setToCliente(Long cl){
        this.To_User_ID = cl;
    }

    public TransactionLOGS() {
    }
}
