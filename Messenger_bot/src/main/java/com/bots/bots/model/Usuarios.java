package com.bots.bots.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usuarios", catalog = "bots", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuarios.findAll", query = "SELECT u FROM Usuarios u")})
public class Usuarios implements Serializable{
    
	private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "idpagina")
    private String idpagina;
    @Id
    @Basic(optional = false)
    @Column(name = "iduser")
    private String iduser;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iduser")
    @JsonIgnore
    private List<Tarjetas> tarjetasList;

    public Usuarios() {
    }

    public Usuarios(String iduser) {
        this.iduser = iduser;
    }

    public Usuarios(String iduser, Date fecha, String idpagina) {
        this.iduser = iduser;
        this.fecha = fecha;
        this.idpagina = idpagina;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getIdpagina() {
        return idpagina;
    }

    public void setIdpagina(String idpagina) {
        this.idpagina = idpagina;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    @XmlTransient
    public List<Tarjetas> getTarjetasList() {
        return tarjetasList;
    }

    public void setTarjetasList(List<Tarjetas> tarjetasList) {
        this.tarjetasList = tarjetasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iduser != null ? iduser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Usuarios)) {
            return false;
        }
        Usuarios other = (Usuarios) object;
        return ((this.iduser == null && other.iduser != null) || (this.iduser != null && !this.iduser.equals(other.iduser)))  ? false : true;
    }

	@Override
	public String toString() {
		return "Usuarios [fecha=" + fecha + ", idpagina=" + idpagina + ", iduser=" + iduser + ", tarjetasList="
				+ tarjetasList + "]";
	}

    
}
