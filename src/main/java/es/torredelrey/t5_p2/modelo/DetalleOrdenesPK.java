/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.torredelrey.t5_p2.modelo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author compr
 */
@Embeddable
public class DetalleOrdenesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "ORDENID")
    private int ordenid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DETALLEID")
    private int detalleid;

    public DetalleOrdenesPK() {
    }

    public DetalleOrdenesPK(int ordenid, int detalleid) {
        this.ordenid = ordenid;
        this.detalleid = detalleid;
    }

    public int getOrdenid() {
        return ordenid;
    }

    public void setOrdenid(int ordenid) {
        this.ordenid = ordenid;
    }

    public int getDetalleid() {
        return detalleid;
    }

    public void setDetalleid(int detalleid) {
        this.detalleid = detalleid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ordenid;
        hash += (int) detalleid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleOrdenesPK)) {
            return false;
        }
        DetalleOrdenesPK other = (DetalleOrdenesPK) object;
        if (this.ordenid != other.ordenid) {
            return false;
        }
        if (this.detalleid != other.detalleid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.torredelrey.t5_p2.modelo.DetalleOrdenesPK[ ordenid=" + ordenid + ", detalleid=" + detalleid + " ]";
    }
    
}
