package com.bots.bots.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "transacciones", catalog = "bots", schema = "")
public class Transacciones implements Serializable{
    
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idtransaccion")
    private Integer idtransaccion;
    @Basic(optional = false)
    @Column(name = "clavetransaccion")
    private String clavetransaccion;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @JoinColumn(name = "ndtarjeta", referencedColumnName = "ntarjeta")
    @ManyToOne(optional = false)
    private Tarjetas ndtarjeta;
    @JoinColumn(name = "iduser", referencedColumnName = "iduser")
    @ManyToOne(optional = false)
    private Usuarios iduser;

    public Transacciones() {
    }

    public Transacciones(Integer idtransaccion) {
        this.idtransaccion = idtransaccion;
    }

    public Transacciones(Integer idtransaccion, String clavetransaccion, Date fecha) {
        this.idtransaccion = idtransaccion;
        this.clavetransaccion = clavetransaccion;
        this.fecha = fecha;
    }

    public Integer getIdtransaccion() {
        return idtransaccion;
    }

    public void setIdtransaccion(Integer idtransaccion) {
        this.idtransaccion = idtransaccion;
    }

    public String getClavetransaccion() {
        return clavetransaccion;
    }

    public void setClavetransaccion(String clavetransaccion) {
        this.clavetransaccion = clavetransaccion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Tarjetas getNdtarjeta() {
        return ndtarjeta;
    }

    public void setNdtarjeta(Tarjetas ndtarjeta) {
        this.ndtarjeta = ndtarjeta;
    }

    public Usuarios getIduser() {
        return iduser;
    }

    public void setIduser(Usuarios iduser) {
        this.iduser = iduser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtransaccion != null ? idtransaccion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Transacciones)) {
            return false;
        }
        Transacciones other = (Transacciones) object;
        return ((this.idtransaccion == null && other.idtransaccion != null) || (this.idtransaccion != null && !this.idtransaccion.equals(other.idtransaccion)))  ? false : true;
    }

    @Override
    public String toString() {
        return "entidades.Transacciones[ idtransaccion=" + idtransaccion + " ]";
    }
    
}
