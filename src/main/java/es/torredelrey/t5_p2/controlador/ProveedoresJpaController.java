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
import es.torredelrey.t5_p2.modelo.Productos;
import es.torredelrey.t5_p2.modelo.Proveedores;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author compr
 */
public class ProveedoresJpaController implements Serializable {

    public ProveedoresJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Proveedores proveedores) throws PreexistingEntityException, Exception {
        if (proveedores.getProductosCollection() == null) {
            proveedores.setProductosCollection(new ArrayList<Productos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Productos> attachedProductosCollection = new ArrayList<Productos>();
            for (Productos productosCollectionProductosToAttach : proveedores.getProductosCollection()) {
                productosCollectionProductosToAttach = em.getReference(productosCollectionProductosToAttach.getClass(), productosCollectionProductosToAttach.getProductoid());
                attachedProductosCollection.add(productosCollectionProductosToAttach);
            }
            proveedores.setProductosCollection(attachedProductosCollection);
            em.persist(proveedores);
            for (Productos productosCollectionProductos : proveedores.getProductosCollection()) {
                Proveedores oldProveedoridOfProductosCollectionProductos = productosCollectionProductos.getProveedorid();
                productosCollectionProductos.setProveedorid(proveedores);
                productosCollectionProductos = em.merge(productosCollectionProductos);
                if (oldProveedoridOfProductosCollectionProductos != null) {
                    oldProveedoridOfProductosCollectionProductos.getProductosCollection().remove(productosCollectionProductos);
                    oldProveedoridOfProductosCollectionProductos = em.merge(oldProveedoridOfProductosCollectionProductos);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProveedores(proveedores.getProveedorid()) != null) {
                throw new PreexistingEntityException("Proveedores " + proveedores + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Proveedores proveedores) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedores persistentProveedores = em.find(Proveedores.class, proveedores.getProveedorid());
            Collection<Productos> productosCollectionOld = persistentProveedores.getProductosCollection();
            Collection<Productos> productosCollectionNew = proveedores.getProductosCollection();
            List<String> illegalOrphanMessages = null;
            for (Productos productosCollectionOldProductos : productosCollectionOld) {
                if (!productosCollectionNew.contains(productosCollectionOldProductos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Productos " + productosCollectionOldProductos + " since its proveedorid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Productos> attachedProductosCollectionNew = new ArrayList<Productos>();
            for (Productos productosCollectionNewProductosToAttach : productosCollectionNew) {
                productosCollectionNewProductosToAttach = em.getReference(productosCollectionNewProductosToAttach.getClass(), productosCollectionNewProductosToAttach.getProductoid());
                attachedProductosCollectionNew.add(productosCollectionNewProductosToAttach);
            }
            productosCollectionNew = attachedProductosCollectionNew;
            proveedores.setProductosCollection(productosCollectionNew);
            proveedores = em.merge(proveedores);
            for (Productos productosCollectionNewProductos : productosCollectionNew) {
                if (!productosCollectionOld.contains(productosCollectionNewProductos)) {
                    Proveedores oldProveedoridOfProductosCollectionNewProductos = productosCollectionNewProductos.getProveedorid();
                    productosCollectionNewProductos.setProveedorid(proveedores);
                    productosCollectionNewProductos = em.merge(productosCollectionNewProductos);
                    if (oldProveedoridOfProductosCollectionNewProductos != null && !oldProveedoridOfProductosCollectionNewProductos.equals(proveedores)) {
                        oldProveedoridOfProductosCollectionNewProductos.getProductosCollection().remove(productosCollectionNewProductos);
                        oldProveedoridOfProductosCollectionNewProductos = em.merge(oldProveedoridOfProductosCollectionNewProductos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = proveedores.getProveedorid();
                if (findProveedores(id) == null) {
                    throw new NonexistentEntityException("The proveedores with id " + id + " no longer exists.");
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
            Proveedores proveedores;
            try {
                proveedores = em.getReference(Proveedores.class, id);
                proveedores.getProveedorid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proveedores with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Productos> productosCollectionOrphanCheck = proveedores.getProductosCollection();
            for (Productos productosCollectionOrphanCheckProductos : productosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proveedores (" + proveedores + ") cannot be destroyed since the Productos " + productosCollectionOrphanCheckProductos + " in its productosCollection field has a non-nullable proveedorid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(proveedores);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Proveedores> findProveedoresEntities() {
        return findProveedoresEntities(true, -1, -1);
    }

    public List<Proveedores> findProveedoresEntities(int maxResults, int firstResult) {
        return findProveedoresEntities(false, maxResults, firstResult);
    }

    private List<Proveedores> findProveedoresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proveedores.class));
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

    public Proveedores findProveedores(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proveedores.class, id);
        } finally {
            em.close();
        }
    }

    public int getProveedoresCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proveedores> rt = cq.from(Proveedores.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
