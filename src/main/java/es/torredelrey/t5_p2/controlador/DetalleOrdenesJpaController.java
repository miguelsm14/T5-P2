/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.torredelrey.t5_p2.controlador;

import es.torredelrey.t5_p2.controlador.exceptions.NonexistentEntityException;
import es.torredelrey.t5_p2.controlador.exceptions.PreexistingEntityException;
import es.torredelrey.t5_p2.modelo.DetalleOrdenes;
import es.torredelrey.t5_p2.modelo.DetalleOrdenesPK;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import es.torredelrey.t5_p2.modelo.Ordenes;
import es.torredelrey.t5_p2.modelo.Productos;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

/**
 *
 * @author compr
 */
public class DetalleOrdenesJpaController implements Serializable {

    public DetalleOrdenesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetalleOrdenes detalleOrdenes) throws PreexistingEntityException, Exception {
        if (detalleOrdenes.getDetalleOrdenesPK() == null) {
            detalleOrdenes.setDetalleOrdenesPK(new DetalleOrdenesPK());
        }
        detalleOrdenes.getDetalleOrdenesPK().setOrdenid(detalleOrdenes.getOrdenes().getOrdenid());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ordenes ordenes = detalleOrdenes.getOrdenes();
            if (ordenes != null) {
                ordenes = em.getReference(ordenes.getClass(), ordenes.getOrdenid());
                detalleOrdenes.setOrdenes(ordenes);
            }
            Productos productoid = detalleOrdenes.getProductoid();
            if (productoid != null) {
                productoid = em.getReference(productoid.getClass(), productoid.getProductoid());
                detalleOrdenes.setProductoid(productoid);
            }
            em.persist(detalleOrdenes);
            if (ordenes != null) {
                ordenes.getDetalleOrdenesCollection().add(detalleOrdenes);
                ordenes = em.merge(ordenes);
            }
            if (productoid != null) {
                productoid.getDetalleOrdenesCollection().add(detalleOrdenes);
                productoid = em.merge(productoid);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDetalleOrdenes(detalleOrdenes.getDetalleOrdenesPK()) != null) {
                throw new PreexistingEntityException("DetalleOrdenes " + detalleOrdenes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetalleOrdenes detalleOrdenes) throws NonexistentEntityException, Exception {
        detalleOrdenes.getDetalleOrdenesPK().setOrdenid(detalleOrdenes.getOrdenes().getOrdenid());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetalleOrdenes persistentDetalleOrdenes = em.find(DetalleOrdenes.class, detalleOrdenes.getDetalleOrdenesPK());
            Ordenes ordenesOld = persistentDetalleOrdenes.getOrdenes();
            Ordenes ordenesNew = detalleOrdenes.getOrdenes();
            Productos productoidOld = persistentDetalleOrdenes.getProductoid();
            Productos productoidNew = detalleOrdenes.getProductoid();
            if (ordenesNew != null) {
                ordenesNew = em.getReference(ordenesNew.getClass(), ordenesNew.getOrdenid());
                detalleOrdenes.setOrdenes(ordenesNew);
            }
            if (productoidNew != null) {
                productoidNew = em.getReference(productoidNew.getClass(), productoidNew.getProductoid());
                detalleOrdenes.setProductoid(productoidNew);
            }
            detalleOrdenes = em.merge(detalleOrdenes);
            if (ordenesOld != null && !ordenesOld.equals(ordenesNew)) {
                ordenesOld.getDetalleOrdenesCollection().remove(detalleOrdenes);
                ordenesOld = em.merge(ordenesOld);
            }
            if (ordenesNew != null && !ordenesNew.equals(ordenesOld)) {
                ordenesNew.getDetalleOrdenesCollection().add(detalleOrdenes);
                ordenesNew = em.merge(ordenesNew);
            }
            if (productoidOld != null && !productoidOld.equals(productoidNew)) {
                productoidOld.getDetalleOrdenesCollection().remove(detalleOrdenes);
                productoidOld = em.merge(productoidOld);
            }
            if (productoidNew != null && !productoidNew.equals(productoidOld)) {
                productoidNew.getDetalleOrdenesCollection().add(detalleOrdenes);
                productoidNew = em.merge(productoidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                DetalleOrdenesPK id = detalleOrdenes.getDetalleOrdenesPK();
                if (findDetalleOrdenes(id) == null) {
                    throw new NonexistentEntityException("The detalleOrdenes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(DetalleOrdenesPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetalleOrdenes detalleOrdenes;
            try {
                detalleOrdenes = em.getReference(DetalleOrdenes.class, id);
                detalleOrdenes.getDetalleOrdenesPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleOrdenes with id " + id + " no longer exists.", enfe);
            }
            Ordenes ordenes = detalleOrdenes.getOrdenes();
            if (ordenes != null) {
                ordenes.getDetalleOrdenesCollection().remove(detalleOrdenes);
                ordenes = em.merge(ordenes);
            }
            Productos productoid = detalleOrdenes.getProductoid();
            if (productoid != null) {
                productoid.getDetalleOrdenesCollection().remove(detalleOrdenes);
                productoid = em.merge(productoid);
            }
            em.remove(detalleOrdenes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DetalleOrdenes> findDetalleOrdenesEntities() {
        return findDetalleOrdenesEntities(true, -1, -1);
    }

    public List<DetalleOrdenes> findDetalleOrdenesEntities(int maxResults, int firstResult) {
        return findDetalleOrdenesEntities(false, maxResults, firstResult);
    }

    private List<DetalleOrdenes> findDetalleOrdenesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetalleOrdenes.class));
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

    public DetalleOrdenes findDetalleOrdenes(DetalleOrdenesPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetalleOrdenes.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleOrdenesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetalleOrdenes> rt = cq.from(DetalleOrdenes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
