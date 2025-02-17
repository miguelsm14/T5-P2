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
import es.torredelrey.t5_p2.modelo.Clientes;
import es.torredelrey.t5_p2.modelo.Empleados;
import es.torredelrey.t5_p2.modelo.DetalleOrdenes;
import es.torredelrey.t5_p2.modelo.Ordenes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author compr
 */
public class OrdenesJpaController implements Serializable {

    public OrdenesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ordenes ordenes) throws PreexistingEntityException, Exception {
        if (ordenes.getDetalleOrdenesCollection() == null) {
            ordenes.setDetalleOrdenesCollection(new ArrayList<DetalleOrdenes>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clientes clienteid = ordenes.getClienteid();
            if (clienteid != null) {
                clienteid = em.getReference(clienteid.getClass(), clienteid.getClienteid());
                ordenes.setClienteid(clienteid);
            }
            Empleados empleadoid = ordenes.getEmpleadoid();
            if (empleadoid != null) {
                empleadoid = em.getReference(empleadoid.getClass(), empleadoid.getEmpleadoid());
                ordenes.setEmpleadoid(empleadoid);
            }
            Collection<DetalleOrdenes> attachedDetalleOrdenesCollection = new ArrayList<DetalleOrdenes>();
            for (DetalleOrdenes detalleOrdenesCollectionDetalleOrdenesToAttach : ordenes.getDetalleOrdenesCollection()) {
                detalleOrdenesCollectionDetalleOrdenesToAttach = em.getReference(detalleOrdenesCollectionDetalleOrdenesToAttach.getClass(), detalleOrdenesCollectionDetalleOrdenesToAttach.getDetalleOrdenesPK());
                attachedDetalleOrdenesCollection.add(detalleOrdenesCollectionDetalleOrdenesToAttach);
            }
            ordenes.setDetalleOrdenesCollection(attachedDetalleOrdenesCollection);
            em.persist(ordenes);
            if (clienteid != null) {
                clienteid.getOrdenesCollection().add(ordenes);
                clienteid = em.merge(clienteid);
            }
            if (empleadoid != null) {
                empleadoid.getOrdenesCollection().add(ordenes);
                empleadoid = em.merge(empleadoid);
            }
            for (DetalleOrdenes detalleOrdenesCollectionDetalleOrdenes : ordenes.getDetalleOrdenesCollection()) {
                Ordenes oldOrdenesOfDetalleOrdenesCollectionDetalleOrdenes = detalleOrdenesCollectionDetalleOrdenes.getOrdenes();
                detalleOrdenesCollectionDetalleOrdenes.setOrdenes(ordenes);
                detalleOrdenesCollectionDetalleOrdenes = em.merge(detalleOrdenesCollectionDetalleOrdenes);
                if (oldOrdenesOfDetalleOrdenesCollectionDetalleOrdenes != null) {
                    oldOrdenesOfDetalleOrdenesCollectionDetalleOrdenes.getDetalleOrdenesCollection().remove(detalleOrdenesCollectionDetalleOrdenes);
                    oldOrdenesOfDetalleOrdenesCollectionDetalleOrdenes = em.merge(oldOrdenesOfDetalleOrdenesCollectionDetalleOrdenes);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findOrdenes(ordenes.getOrdenid()) != null) {
                throw new PreexistingEntityException("Ordenes " + ordenes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ordenes ordenes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ordenes persistentOrdenes = em.find(Ordenes.class, ordenes.getOrdenid());
            Clientes clienteidOld = persistentOrdenes.getClienteid();
            Clientes clienteidNew = ordenes.getClienteid();
            Empleados empleadoidOld = persistentOrdenes.getEmpleadoid();
            Empleados empleadoidNew = ordenes.getEmpleadoid();
            Collection<DetalleOrdenes> detalleOrdenesCollectionOld = persistentOrdenes.getDetalleOrdenesCollection();
            Collection<DetalleOrdenes> detalleOrdenesCollectionNew = ordenes.getDetalleOrdenesCollection();
            List<String> illegalOrphanMessages = null;
            for (DetalleOrdenes detalleOrdenesCollectionOldDetalleOrdenes : detalleOrdenesCollectionOld) {
                if (!detalleOrdenesCollectionNew.contains(detalleOrdenesCollectionOldDetalleOrdenes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleOrdenes " + detalleOrdenesCollectionOldDetalleOrdenes + " since its ordenes field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (clienteidNew != null) {
                clienteidNew = em.getReference(clienteidNew.getClass(), clienteidNew.getClienteid());
                ordenes.setClienteid(clienteidNew);
            }
            if (empleadoidNew != null) {
                empleadoidNew = em.getReference(empleadoidNew.getClass(), empleadoidNew.getEmpleadoid());
                ordenes.setEmpleadoid(empleadoidNew);
            }
            Collection<DetalleOrdenes> attachedDetalleOrdenesCollectionNew = new ArrayList<DetalleOrdenes>();
            for (DetalleOrdenes detalleOrdenesCollectionNewDetalleOrdenesToAttach : detalleOrdenesCollectionNew) {
                detalleOrdenesCollectionNewDetalleOrdenesToAttach = em.getReference(detalleOrdenesCollectionNewDetalleOrdenesToAttach.getClass(), detalleOrdenesCollectionNewDetalleOrdenesToAttach.getDetalleOrdenesPK());
                attachedDetalleOrdenesCollectionNew.add(detalleOrdenesCollectionNewDetalleOrdenesToAttach);
            }
            detalleOrdenesCollectionNew = attachedDetalleOrdenesCollectionNew;
            ordenes.setDetalleOrdenesCollection(detalleOrdenesCollectionNew);
            ordenes = em.merge(ordenes);
            if (clienteidOld != null && !clienteidOld.equals(clienteidNew)) {
                clienteidOld.getOrdenesCollection().remove(ordenes);
                clienteidOld = em.merge(clienteidOld);
            }
            if (clienteidNew != null && !clienteidNew.equals(clienteidOld)) {
                clienteidNew.getOrdenesCollection().add(ordenes);
                clienteidNew = em.merge(clienteidNew);
            }
            if (empleadoidOld != null && !empleadoidOld.equals(empleadoidNew)) {
                empleadoidOld.getOrdenesCollection().remove(ordenes);
                empleadoidOld = em.merge(empleadoidOld);
            }
            if (empleadoidNew != null && !empleadoidNew.equals(empleadoidOld)) {
                empleadoidNew.getOrdenesCollection().add(ordenes);
                empleadoidNew = em.merge(empleadoidNew);
            }
            for (DetalleOrdenes detalleOrdenesCollectionNewDetalleOrdenes : detalleOrdenesCollectionNew) {
                if (!detalleOrdenesCollectionOld.contains(detalleOrdenesCollectionNewDetalleOrdenes)) {
                    Ordenes oldOrdenesOfDetalleOrdenesCollectionNewDetalleOrdenes = detalleOrdenesCollectionNewDetalleOrdenes.getOrdenes();
                    detalleOrdenesCollectionNewDetalleOrdenes.setOrdenes(ordenes);
                    detalleOrdenesCollectionNewDetalleOrdenes = em.merge(detalleOrdenesCollectionNewDetalleOrdenes);
                    if (oldOrdenesOfDetalleOrdenesCollectionNewDetalleOrdenes != null && !oldOrdenesOfDetalleOrdenesCollectionNewDetalleOrdenes.equals(ordenes)) {
                        oldOrdenesOfDetalleOrdenesCollectionNewDetalleOrdenes.getDetalleOrdenesCollection().remove(detalleOrdenesCollectionNewDetalleOrdenes);
                        oldOrdenesOfDetalleOrdenesCollectionNewDetalleOrdenes = em.merge(oldOrdenesOfDetalleOrdenesCollectionNewDetalleOrdenes);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ordenes.getOrdenid();
                if (findOrdenes(id) == null) {
                    throw new NonexistentEntityException("The ordenes with id " + id + " no longer exists.");
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
            Ordenes ordenes;
            try {
                ordenes = em.getReference(Ordenes.class, id);
                ordenes.getOrdenid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ordenes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DetalleOrdenes> detalleOrdenesCollectionOrphanCheck = ordenes.getDetalleOrdenesCollection();
            for (DetalleOrdenes detalleOrdenesCollectionOrphanCheckDetalleOrdenes : detalleOrdenesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ordenes (" + ordenes + ") cannot be destroyed since the DetalleOrdenes " + detalleOrdenesCollectionOrphanCheckDetalleOrdenes + " in its detalleOrdenesCollection field has a non-nullable ordenes field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Clientes clienteid = ordenes.getClienteid();
            if (clienteid != null) {
                clienteid.getOrdenesCollection().remove(ordenes);
                clienteid = em.merge(clienteid);
            }
            Empleados empleadoid = ordenes.getEmpleadoid();
            if (empleadoid != null) {
                empleadoid.getOrdenesCollection().remove(ordenes);
                empleadoid = em.merge(empleadoid);
            }
            em.remove(ordenes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ordenes> findOrdenesEntities() {
        return findOrdenesEntities(true, -1, -1);
    }

    public List<Ordenes> findOrdenesEntities(int maxResults, int firstResult) {
        return findOrdenesEntities(false, maxResults, firstResult);
    }

    private List<Ordenes> findOrdenesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ordenes.class));
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

    public Ordenes findOrdenes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ordenes.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrdenesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ordenes> rt = cq.from(Ordenes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
