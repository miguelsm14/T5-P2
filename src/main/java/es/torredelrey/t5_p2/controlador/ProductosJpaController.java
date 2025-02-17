/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.torredelrey.t5_p2.controlador;

import es.torredelrey.t5_p2.controlador.exceptions.IllegalOrphanException;
import es.torredelrey.t5_p2.controlador.exceptions.NonexistentEntityException;
import es.torredelrey.t5_p2.controlador.exceptions.PreexistingEntityException;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import es.torredelrey.t5_p2.modelo.Categorias;
import es.torredelrey.t5_p2.modelo.Proveedores;
import es.torredelrey.t5_p2.modelo.DetalleOrdenes;
import es.torredelrey.t5_p2.modelo.Productos;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author compr
 */
public class ProductosJpaController implements Serializable {

    public ProductosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Productos productos) throws PreexistingEntityException, Exception {
        if (productos.getDetalleOrdenesCollection() == null) {
            productos.setDetalleOrdenesCollection(new ArrayList<DetalleOrdenes>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categorias categoriaid = productos.getCategoriaid();
            if (categoriaid != null) {
                categoriaid = em.getReference(categoriaid.getClass(), categoriaid.getCategoriaid());
                productos.setCategoriaid(categoriaid);
            }
            Proveedores proveedorid = productos.getProveedorid();
            if (proveedorid != null) {
                proveedorid = em.getReference(proveedorid.getClass(), proveedorid.getProveedorid());
                productos.setProveedorid(proveedorid);
            }
            Collection<DetalleOrdenes> attachedDetalleOrdenesCollection = new ArrayList<DetalleOrdenes>();
            for (DetalleOrdenes detalleOrdenesCollectionDetalleOrdenesToAttach : productos.getDetalleOrdenesCollection()) {
                detalleOrdenesCollectionDetalleOrdenesToAttach = em.getReference(detalleOrdenesCollectionDetalleOrdenesToAttach.getClass(), detalleOrdenesCollectionDetalleOrdenesToAttach.getDetalleOrdenesPK());
                attachedDetalleOrdenesCollection.add(detalleOrdenesCollectionDetalleOrdenesToAttach);
            }
            productos.setDetalleOrdenesCollection(attachedDetalleOrdenesCollection);
            em.persist(productos);
            if (categoriaid != null) {
                categoriaid.getProductosCollection().add(productos);
                categoriaid = em.merge(categoriaid);
            }
            if (proveedorid != null) {
                proveedorid.getProductosCollection().add(productos);
                proveedorid = em.merge(proveedorid);
            }
            for (DetalleOrdenes detalleOrdenesCollectionDetalleOrdenes : productos.getDetalleOrdenesCollection()) {
                Productos oldProductoidOfDetalleOrdenesCollectionDetalleOrdenes = detalleOrdenesCollectionDetalleOrdenes.getProductoid();
                detalleOrdenesCollectionDetalleOrdenes.setProductoid(productos);
                detalleOrdenesCollectionDetalleOrdenes = em.merge(detalleOrdenesCollectionDetalleOrdenes);
                if (oldProductoidOfDetalleOrdenesCollectionDetalleOrdenes != null) {
                    oldProductoidOfDetalleOrdenesCollectionDetalleOrdenes.getDetalleOrdenesCollection().remove(detalleOrdenesCollectionDetalleOrdenes);
                    oldProductoidOfDetalleOrdenesCollectionDetalleOrdenes = em.merge(oldProductoidOfDetalleOrdenesCollectionDetalleOrdenes);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProductos(productos.getProductoid()) != null) {
                throw new PreexistingEntityException("Productos " + productos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Productos productos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Productos persistentProductos = em.find(Productos.class, productos.getProductoid());
            Categorias categoriaidOld = persistentProductos.getCategoriaid();
            Categorias categoriaidNew = productos.getCategoriaid();
            Proveedores proveedoridOld = persistentProductos.getProveedorid();
            Proveedores proveedoridNew = productos.getProveedorid();
            Collection<DetalleOrdenes> detalleOrdenesCollectionOld = persistentProductos.getDetalleOrdenesCollection();
            Collection<DetalleOrdenes> detalleOrdenesCollectionNew = productos.getDetalleOrdenesCollection();
            List<String> illegalOrphanMessages = null;
            for (DetalleOrdenes detalleOrdenesCollectionOldDetalleOrdenes : detalleOrdenesCollectionOld) {
                if (!detalleOrdenesCollectionNew.contains(detalleOrdenesCollectionOldDetalleOrdenes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleOrdenes " + detalleOrdenesCollectionOldDetalleOrdenes + " since its productoid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (categoriaidNew != null) {
                categoriaidNew = em.getReference(categoriaidNew.getClass(), categoriaidNew.getCategoriaid());
                productos.setCategoriaid(categoriaidNew);
            }
            if (proveedoridNew != null) {
                proveedoridNew = em.getReference(proveedoridNew.getClass(), proveedoridNew.getProveedorid());
                productos.setProveedorid(proveedoridNew);
            }
            Collection<DetalleOrdenes> attachedDetalleOrdenesCollectionNew = new ArrayList<DetalleOrdenes>();
            for (DetalleOrdenes detalleOrdenesCollectionNewDetalleOrdenesToAttach : detalleOrdenesCollectionNew) {
                detalleOrdenesCollectionNewDetalleOrdenesToAttach = em.getReference(detalleOrdenesCollectionNewDetalleOrdenesToAttach.getClass(), detalleOrdenesCollectionNewDetalleOrdenesToAttach.getDetalleOrdenesPK());
                attachedDetalleOrdenesCollectionNew.add(detalleOrdenesCollectionNewDetalleOrdenesToAttach);
            }
            detalleOrdenesCollectionNew = attachedDetalleOrdenesCollectionNew;
            productos.setDetalleOrdenesCollection(detalleOrdenesCollectionNew);
            productos = em.merge(productos);
            if (categoriaidOld != null && !categoriaidOld.equals(categoriaidNew)) {
                categoriaidOld.getProductosCollection().remove(productos);
                categoriaidOld = em.merge(categoriaidOld);
            }
            if (categoriaidNew != null && !categoriaidNew.equals(categoriaidOld)) {
                categoriaidNew.getProductosCollection().add(productos);
                categoriaidNew = em.merge(categoriaidNew);
            }
            if (proveedoridOld != null && !proveedoridOld.equals(proveedoridNew)) {
                proveedoridOld.getProductosCollection().remove(productos);
                proveedoridOld = em.merge(proveedoridOld);
            }
            if (proveedoridNew != null && !proveedoridNew.equals(proveedoridOld)) {
                proveedoridNew.getProductosCollection().add(productos);
                proveedoridNew = em.merge(proveedoridNew);
            }
            for (DetalleOrdenes detalleOrdenesCollectionNewDetalleOrdenes : detalleOrdenesCollectionNew) {
                if (!detalleOrdenesCollectionOld.contains(detalleOrdenesCollectionNewDetalleOrdenes)) {
                    Productos oldProductoidOfDetalleOrdenesCollectionNewDetalleOrdenes = detalleOrdenesCollectionNewDetalleOrdenes.getProductoid();
                    detalleOrdenesCollectionNewDetalleOrdenes.setProductoid(productos);
                    detalleOrdenesCollectionNewDetalleOrdenes = em.merge(detalleOrdenesCollectionNewDetalleOrdenes);
                    if (oldProductoidOfDetalleOrdenesCollectionNewDetalleOrdenes != null && !oldProductoidOfDetalleOrdenesCollectionNewDetalleOrdenes.equals(productos)) {
                        oldProductoidOfDetalleOrdenesCollectionNewDetalleOrdenes.getDetalleOrdenesCollection().remove(detalleOrdenesCollectionNewDetalleOrdenes);
                        oldProductoidOfDetalleOrdenesCollectionNewDetalleOrdenes = em.merge(oldProductoidOfDetalleOrdenesCollectionNewDetalleOrdenes);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = productos.getProductoid();
                if (findProductos(id) == null) {
                    throw new NonexistentEntityException("The productos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Productos productos;
            try {
                productos = em.getReference(Productos.class, id);
                productos.getProductoid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DetalleOrdenes> detalleOrdenesCollectionOrphanCheck = productos.getDetalleOrdenesCollection();
            for (DetalleOrdenes detalleOrdenesCollectionOrphanCheckDetalleOrdenes : detalleOrdenesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Productos (" + productos + ") cannot be destroyed since the DetalleOrdenes " + detalleOrdenesCollectionOrphanCheckDetalleOrdenes + " in its detalleOrdenesCollection field has a non-nullable productoid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Categorias categoriaid = productos.getCategoriaid();
            if (categoriaid != null) {
                categoriaid.getProductosCollection().remove(productos);
                categoriaid = em.merge(categoriaid);
            }
            Proveedores proveedorid = productos.getProveedorid();
            if (proveedorid != null) {
                proveedorid.getProductosCollection().remove(productos);
                proveedorid = em.merge(proveedorid);
            }
            em.remove(productos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Productos> findProductosEntities() {
        return findProductosEntities(true, -1, -1);
    }

    public List<Productos> findProductosEntities(int maxResults, int firstResult) {
        return findProductosEntities(false, maxResults, firstResult);
    }

    private List<Productos> findProductosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Productos.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Productos findProductos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Productos.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Productos> rt = cq.from(Productos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
