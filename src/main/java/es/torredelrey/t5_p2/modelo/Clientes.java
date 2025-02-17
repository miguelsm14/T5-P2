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
@Table(name = "clientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Clientes.findAll", query = "SELECT c FROM Clientes c"),
    @NamedQuery(name = "Clientes.findByClienteid", query = "SELECT c FROM Clientes c WHERE c.clienteid = :clienteid"),
    @NamedQuery(name = "Clientes.findByCedulaRuc", query = "SELECT c FROM Clientes c WHERE c.cedulaRuc = :cedulaRuc"),
    @NamedQuery(name = "Clientes.findByNombrecia", query = "SELECT c FROM Clientes c WHERE c.nombrecia = :nombrecia"),
    @NamedQuery(name = "Clientes.findByNombrecontacto", query = "SELECT c FROM Clientes c WHERE c.nombrecontacto = :nombrecontacto"),
    @NamedQuery(name = "Clientes.findByDireccioncli", query = "SELECT c FROM Clientes c WHERE c.direccioncli = :direccioncli"),
    @NamedQuery(name = "Clientes.findByFax", query = "SELECT c FROM Clientes c WHERE c.fax = :fax"),
    @NamedQuery(name = "Clientes.findByEmail", query = "SELECT c FROM Clientes c WHERE c.email = :email"),
    @NamedQuery(name = "Clientes.findByCelular", query = "SELECT c FROM Clientes c WHERE c.celular = :celular"),
    @NamedQuery(name = "Clientes.findByFijo", query = "SELECT c FROM Clientes c WHERE c.fijo = :fijo")})
public class Clientes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CLIENTEID")
    private Integer clienteid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "CEDULA_RUC")
    private String cedulaRuc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "NOMBRECIA")
    private String nombrecia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOMBRECONTACTO")
    private String nombrecontacto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "DIRECCIONCLI")
    private String direccioncli;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 12)
    @Column(name = "FAX")
    private String fax;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 12)
    @Column(name = "CELULAR")
    private String celular;
    @Size(max = 12)
    @Column(name = "FIJO")
    private String fijo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clienteid")
    private Collection<Ordenes> ordenesCollection;

    public Clientes() {
    }

    public Clientes(Integer clienteid) {
        this.clienteid = clienteid;
    }

    public Clientes(Integer clienteid, String cedulaRuc, String nombrecia, String nombrecontacto, String direccioncli) {
        this.clienteid = clienteid;
        this.cedulaRuc = cedulaRuc;
        this.nombrecia = nombrecia;
        this.nombrecontacto = nombrecontacto;
        this.direccioncli = direccioncli;
    }

    public Integer getClienteid() {
        return clienteid;
    }

    public void setClienteid(Integer clienteid) {
        this.clienteid = clienteid;
    }

    public String getCedulaRuc() {
        return cedulaRuc;
    }

    public void setCedulaRuc(String cedulaRuc) {
        this.cedulaRuc = cedulaRuc;
    }

    public String getNombrecia() {
        return nombrecia;
    }

    public void setNombrecia(String nombrecia) {
        this.nombrecia = nombrecia;
    }

    public String getNombrecontacto() {
        return nombrecontacto;
    }

    public void setNombrecontacto(String nombrecontacto) {
        this.nombrecontacto = nombrecontacto;
    }

    public String getDireccioncli() {
        return direccioncli;
    }

    public void setDireccioncli(String direccioncli) {
        this.direccioncli = direccioncli;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getFijo() {
        return fijo;
    }

    public void setFijo(String fijo) {
        this.fijo = fijo;
    }

    @XmlTransient
    public Collection<Ordenes> getOrdenesCollection() {
        return ordenesCollection;
    }

    public void setOrdenesCollection(Collection<Ordenes> ordenesCollection) {
        this.ordenesCollection = ordenesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clienteid != null ? clienteid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Clientes)) {
            return false;
        }
        Clientes other = (Clientes) object;
        if ((this.clienteid == null && other.clienteid != null) || (this.clienteid != null && !this.clienteid.equals(other.clienteid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.torredelrey.t5_p2.modelo.Clientes[ clienteid=" + clienteid + " ]";
    }
    
}
