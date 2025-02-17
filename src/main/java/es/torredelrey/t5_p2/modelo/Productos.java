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
@Table(name = "productos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Productos.findAll", query = "SELECT p FROM Productos p"),
    @NamedQuery(name = "Productos.findByProductoid", query = "SELECT p FROM Productos p WHERE p.productoid = :productoid"),
    @NamedQuery(name = "Productos.findByDescripcion", query = "SELECT p FROM Productos p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "Productos.findByPreciounit", query = "SELECT p FROM Productos p WHERE p.preciounit = :preciounit"),
    @NamedQuery(name = "Productos.findByExistencia", query = "SELECT p FROM Productos p WHERE p.existencia = :existencia")})
public class Productos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRODUCTOID")
    private Integer productoid;
    @Size(max = 50)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRECIOUNIT")
    private double preciounit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EXISTENCIA")
    private int existencia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productoid")
    private Collection<DetalleOrdenes> detalleOrdenesCollection;
    @JoinColumn(name = "CATEGORIAID", referencedColumnName = "CATEGORIAID")
    @ManyToOne(optional = false)
    private Categorias categoriaid;
    @JoinColumn(name = "PROVEEDORID", referencedColumnName = "PROVEEDORID")
    @ManyToOne(optional = false)
    private Proveedores proveedorid;

    public Productos() {
    }

    public Productos(Integer productoid) {
        this.productoid = productoid;
    }

    public Productos(Integer productoid, double preciounit, int existencia) {
        this.productoid = productoid;
        this.preciounit = preciounit;
        this.existencia = existencia;
    }

    public Integer getProductoid() {
        return productoid;
    }

    public void setProductoid(Integer productoid) {
        this.productoid = productoid;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPreciounit() {
        return preciounit;
    }

    public void setPreciounit(double preciounit) {
        this.preciounit = preciounit;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    @XmlTransient
    public Collection<DetalleOrdenes> getDetalleOrdenesCollection() {
        return detalleOrdenesCollection;
    }

    public void setDetalleOrdenesCollection(Collection<DetalleOrdenes> detalleOrdenesCollection) {
        this.detalleOrdenesCollection = detalleOrdenesCollection;
    }

    public Categorias getCategoriaid() {
        return categoriaid;
    }

    public void setCategoriaid(Categorias categoriaid) {
        this.categoriaid = categoriaid;
    }

    public Proveedores getProveedorid() {
        return proveedorid;
    }

    public void setProveedorid(Proveedores proveedorid) {
        this.proveedorid = proveedorid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productoid != null ? productoid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Productos)) {
            return false;
        }
        Productos other = (Productos) object;
        if ((this.productoid == null && other.productoid != null) || (this.productoid != null && !this.productoid.equals(other.productoid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.torredelrey.t5_p2.modelo.Productos[ productoid=" + productoid + " ]";
    }
    
}
