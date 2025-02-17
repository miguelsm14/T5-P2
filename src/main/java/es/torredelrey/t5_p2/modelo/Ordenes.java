/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.torredelrey.t5_p2.modelo;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author compr
 */
@Entity
@Table(name = "ordenes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ordenes.findAll", query = "SELECT o FROM Ordenes o"),
    @NamedQuery(name = "Ordenes.findByOrdenid", query = "SELECT o FROM Ordenes o WHERE o.ordenid = :ordenid"),
    @NamedQuery(name = "Ordenes.findByFechaorden", query = "SELECT o FROM Ordenes o WHERE o.fechaorden = :fechaorden"),
    @NamedQuery(name = "Ordenes.findByDescuento", query = "SELECT o FROM Ordenes o WHERE o.descuento = :descuento")})
public class Ordenes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ORDENID")
    private Integer ordenid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAORDEN")
    @Temporal(TemporalType.DATE)
    private Date fechaorden;
    @Column(name = "DESCUENTO")
    private Integer descuento;
    @JoinColumn(name = "CLIENTEID", referencedColumnName = "CLIENTEID")
    @ManyToOne(optional = false)
    private Clientes clienteid;
    @JoinColumn(name = "EMPLEADOID", referencedColumnName = "EMPLEADOID")
    @ManyToOne(optional = false)
    private Empleados empleadoid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ordenes")
    private Collection<DetalleOrdenes> detalleOrdenesCollection;

    public Ordenes() {
    }

    public Ordenes(Integer ordenid) {
        this.ordenid = ordenid;
    }

    public Ordenes(Integer ordenid, Date fechaorden) {
        this.ordenid = ordenid;
        this.fechaorden = fechaorden;
    }

    public Integer getOrdenid() {
        return ordenid;
    }

    public void setOrdenid(Integer ordenid) {
        this.ordenid = ordenid;
    }

    public Date getFechaorden() {
        return fechaorden;
    }

    public void setFechaorden(Date fechaorden) {
        this.fechaorden = fechaorden;
    }

    public Integer getDescuento() {
        return descuento;
    }

    public void setDescuento(Integer descuento) {
        this.descuento = descuento;
    }

    public Clientes getClienteid() {
        return clienteid;
    }

    public void setClienteid(Clientes clienteid) {
        this.clienteid = clienteid;
    }

    public Empleados getEmpleadoid() {
        return empleadoid;
    }

    public void setEmpleadoid(Empleados empleadoid) {
        this.empleadoid = empleadoid;
    }

    @XmlTransient
    public Collection<DetalleOrdenes> getDetalleOrdenesCollection() {
        return detalleOrdenesCollection;
    }

    public void setDetalleOrdenesCollection(Collection<DetalleOrdenes> detalleOrdenesCollection) {
        this.detalleOrdenesCollection = detalleOrdenesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ordenid != null ? ordenid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ordenes)) {
            return false;
        }
        Ordenes other = (Ordenes) object;
        if ((this.ordenid == null && other.ordenid != null) || (this.ordenid != null && !this.ordenid.equals(other.ordenid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.torredelrey.t5_p2.modelo.Ordenes[ ordenid=" + ordenid + " ]";
    }
    
}
