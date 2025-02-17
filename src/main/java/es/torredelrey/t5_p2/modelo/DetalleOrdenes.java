/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.torredelrey.t5_p2.modelo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author compr
 */
@Entity
@Table(name = "detalle_ordenes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleOrdenes.findAll", query = "SELECT d FROM DetalleOrdenes d"),
    @NamedQuery(name = "DetalleOrdenes.findByOrdenid", query = "SELECT d FROM DetalleOrdenes d WHERE d.detalleOrdenesPK.ordenid = :ordenid"),
    @NamedQuery(name = "DetalleOrdenes.findByDetalleid", query = "SELECT d FROM DetalleOrdenes d WHERE d.detalleOrdenesPK.detalleid = :detalleid"),
    @NamedQuery(name = "DetalleOrdenes.findByCantidad", query = "SELECT d FROM DetalleOrdenes d WHERE d.cantidad = :cantidad")})
public class DetalleOrdenes implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetalleOrdenesPK detalleOrdenesPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD")
    private int cantidad;
    @JoinColumn(name = "ORDENID", referencedColumnName = "ORDENID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Ordenes ordenes;
    @JoinColumn(name = "PRODUCTOID", referencedColumnName = "PRODUCTOID")
    @ManyToOne(optional = false)
    private Productos productoid;

    public DetalleOrdenes() {
    }

    public DetalleOrdenes(DetalleOrdenesPK detalleOrdenesPK) {
        this.detalleOrdenesPK = detalleOrdenesPK;
    }

    public DetalleOrdenes(DetalleOrdenesPK detalleOrdenesPK, int cantidad) {
        this.detalleOrdenesPK = detalleOrdenesPK;
        this.cantidad = cantidad;
    }

    public DetalleOrdenes(int ordenid, int detalleid) {
        this.detalleOrdenesPK = new DetalleOrdenesPK(ordenid, detalleid);
    }

    public DetalleOrdenesPK getDetalleOrdenesPK() {
        return detalleOrdenesPK;
    }

    public void setDetalleOrdenesPK(DetalleOrdenesPK detalleOrdenesPK) {
        this.detalleOrdenesPK = detalleOrdenesPK;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Ordenes getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(Ordenes ordenes) {
        this.ordenes = ordenes;
    }

    public Productos getProductoid() {
        return productoid;
    }

    public void setProductoid(Productos productoid) {
        this.productoid = productoid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detalleOrdenesPK != null ? detalleOrdenesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleOrdenes)) {
            return false;
        }
        DetalleOrdenes other = (DetalleOrdenes) object;
        if ((this.detalleOrdenesPK == null && other.detalleOrdenesPK != null) || (this.detalleOrdenesPK != null && !this.detalleOrdenesPK.equals(other.detalleOrdenesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.torredelrey.t5_p2.modelo.DetalleOrdenes[ detalleOrdenesPK=" + detalleOrdenesPK + " ]";
    }
    
}
