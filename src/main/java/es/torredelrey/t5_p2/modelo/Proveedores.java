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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author compr
 */
@Entity
@Table(name = "proveedores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Proveedores.findAll", query = "SELECT p FROM Proveedores p"),
    @NamedQuery(name = "Proveedores.findByProveedorid", query = "SELECT p FROM Proveedores p WHERE p.proveedorid = :proveedorid"),
    @NamedQuery(name = "Proveedores.findByNombreprov", query = "SELECT p FROM Proveedores p WHERE p.nombreprov = :nombreprov"),
    @NamedQuery(name = "Proveedores.findByContacto", query = "SELECT p FROM Proveedores p WHERE p.contacto = :contacto"),
    @NamedQuery(name = "Proveedores.findByCeluprov", query = "SELECT p FROM Proveedores p WHERE p.celuprov = :celuprov"),
    @NamedQuery(name = "Proveedores.findByFijoprov", query = "SELECT p FROM Proveedores p WHERE p.fijoprov = :fijoprov")})
public class Proveedores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "PROVEEDORID")
    private Integer proveedorid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOMBREPROV")
    private String nombreprov;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "CONTACTO")
    private String contacto;
    @Size(max = 12)
    @Column(name = "CELUPROV")
    private String celuprov;
    @Size(max = 12)
    @Column(name = "FIJOPROV")
    private String fijoprov;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proveedorid")
    private Collection<Productos> productosCollection;

    public Proveedores() {
    }

    public Proveedores(Integer proveedorid) {
        this.proveedorid = proveedorid;
    }

    public Proveedores(Integer proveedorid, String nombreprov, String contacto) {
        this.proveedorid = proveedorid;
        this.nombreprov = nombreprov;
        this.contacto = contacto;
    }

    public Integer getProveedorid() {
        return proveedorid;
    }

    public void setProveedorid(Integer proveedorid) {
        this.proveedorid = proveedorid;
    }

    public String getNombreprov() {
        return nombreprov;
    }

    public void setNombreprov(String nombreprov) {
        this.nombreprov = nombreprov;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getCeluprov() {
        return celuprov;
    }

    public void setCeluprov(String celuprov) {
        this.celuprov = celuprov;
    }

    public String getFijoprov() {
        return fijoprov;
    }

    public void setFijoprov(String fijoprov) {
        this.fijoprov = fijoprov;
    }

    @XmlTransient
    public Collection<Productos> getProductosCollection() {
        return productosCollection;
    }

    public void setProductosCollection(Collection<Productos> productosCollection) {
        this.productosCollection = productosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (proveedorid != null ? proveedorid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Proveedores)) {
            return false;
        }
        Proveedores other = (Proveedores) object;
        if ((this.proveedorid == null && other.proveedorid != null) || (this.proveedorid != null && !this.proveedorid.equals(other.proveedorid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.torredelrey.t5_p2.modelo.Proveedores[ proveedorid=" + proveedorid + " ]";
    }
    
}
