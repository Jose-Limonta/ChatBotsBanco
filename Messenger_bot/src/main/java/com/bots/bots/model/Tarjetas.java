package com.bots.bots.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "tarjetas", catalog = "bots", schema = "")
public class Tarjetas implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "nbanco")
    private String nbanco;
    @Id
    @Basic(optional = false)
    @Column(name = "ntarjeta")
    private String ntarjeta;
    @Basic(optional = false)
    @Column(name = "ttarjeta")
    private String ttarjeta;
    @JoinColumn(name = "iduser", referencedColumnName = "iduser")
    @ManyToOne(optional = false)
    private Usuarios iduser;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ndtarjeta")
    private List<Transacciones> transaccionesList;

    public Tarjetas() {
    }

    public Tarjetas(String ntarjeta) {
        this.ntarjeta = ntarjeta;
    }

    public Tarjetas(String ntarjeta, Date fecha, String nbanco, String ttarjeta) {
        this.ntarjeta = ntarjeta;
        this.fecha = fecha;
        this.nbanco = nbanco;
        this.ttarjeta = ttarjeta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNbanco() {
        return nbanco;
    }

    public void setNbanco(String nbanco) {
        this.nbanco = nbanco;
    }

    public String getNtarjeta() {
        return ntarjeta;
    }

    public void setNtarjeta(String ntarjeta) {
        this.ntarjeta = ntarjeta;
    }

    public String getTtarjeta() {
        return ttarjeta;
    }

    public void setTtarjeta(String ttarjeta) {
        this.ttarjeta = ttarjeta;
    }

    public Usuarios getIduser() {
        return iduser;
    }

    public void setIduser(Usuarios iduser) {
        this.iduser = iduser;
    }

    @XmlTransient
    public List<Transacciones> getTransaccionesList() {
        return transaccionesList;
    }

    public void setTransaccionesList(List<Transacciones> transaccionesList) {
        this.transaccionesList = transaccionesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ntarjeta != null ? ntarjeta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tarjetas)) {
            return false;
        }
        Tarjetas other = (Tarjetas) object;
        if ((this.ntarjeta == null && other.ntarjeta != null) || (this.ntarjeta != null && !this.ntarjeta.equals(other.ntarjeta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Tarjetas[ ntarjeta=" + ntarjeta + " ]";
    }
    
}

