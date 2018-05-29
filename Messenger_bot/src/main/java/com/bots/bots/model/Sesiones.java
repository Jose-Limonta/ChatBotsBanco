package com.bots.bots.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "sesiones", catalog = "bots", schema = "")
public class Sesiones implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "id_sesion")
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
    private String idSesion;
    @Size(max = 20)
    @Column(name = "accion")
    private String accion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "registro")
    private Short registro;
    @Lob
    @Column(name = "notarjeta")
    private String notarjeta;

    public Sesiones() {
    }

    public Sesiones(String idSesion) {
        this.idSesion = idSesion;
    }

    public Sesiones(String idSesion, Date fecha) {
        this.idSesion = idSesion;
        this.fecha = fecha;
    }

    public String getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(String idSesion) {
        this.idSesion = idSesion;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Short getRegistro() {
        return registro;
    }

    public void setRegistro(Short registro) {
        this.registro = registro;
    }

    public String getNotarjeta() {
		return notarjeta;
	}

	public void setNotarjeta(String notarjeta) {
		this.notarjeta = notarjeta;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idSesion != null ? idSesion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Sesiones)) {
            return false;
        }
        Sesiones other = (Sesiones) object;
        return ((this.idSesion == null && other.idSesion != null) || (this.idSesion != null && !this.idSesion.equals(other.idSesion))) ? false : true;
    }

	@Override
	public String toString() {
		return "Sesiones [idSesion=" + idSesion + ", accion=" + accion + ", fecha=" + fecha + ", registro=" + registro
				+ ", notarjeta=" + notarjeta + "]";
	}

}
