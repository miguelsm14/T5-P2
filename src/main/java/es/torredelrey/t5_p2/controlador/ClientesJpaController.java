/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.torredelrey.t5_p2.controlador;

import es.torredelrey.t5_p2.controlador.exceptions.IllegalOrphanException;
import es.torredelrey.t5_p2.controlador.exceptions.NonexistentEntityException;
import es.torredelrey.t5_p2.controlador.exceptions.PreexistingEntityException;
import es.torredelrey.t5_p2.modelo.Clientes;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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
public class ClientesJpaController implements Serializable {

    public ClientesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clientes clientes) throws PreexistingEntityException, Exception {
        if (clientes.getOrdenesCollection() == null) {
            clientes.setOrdenesCollection(new ArrayList<Ordenes>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Ordenes> attachedOrdenesCollection = new ArrayList<Ordenes>();
            for (Ordenes ordenesCollectionOrdenesToAttach : clientes.getOrdenesCollection()) {
                ordenesCollectionOrdenesToAttach = em.getReference(ordenesCollectionOrdenesToAttach.getClass(), ordenesCollectionOrdenesToAttach.getOrdenid());
                attachedOrdenesCollection.add(ordenesCollectionOrdenesToAttach);
            }
            clientes.setOrdenesCollection(attachedOrdenesCollection);
            em.persist(clientes);
            for (Ordenes ordenesCollectionOrdenes : clientes.getOrdenesCollection()) {
                Clientes oldClienteidOfOrdenesCollectionOrdenes = ordenesCollectionOrdenes.getClienteid();
                ordenesCollectionOrdenes.setClienteid(clientes);
                ordenesCollectionOrdenes = em.merge(ordenesCollectionOrdenes);
                if (oldClienteidOfOrdenesCollectionOrdenes != null) {
                    oldClienteidOfOrdenesCollectionOrdenes.getOrdenesCollection().remove(ordenesCollectionOrdenes);
                    oldClienteidOfOrdenesCollectionOrdenes = em.merge(oldClienteidOfOrdenesCollectionOrdenes);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findClientes(clientes.getClienteid()) != null) {
                throw new PreexistingEntityException("Clientes " + clientes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clientes clientes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clientes persistentClientes = em.find(Clientes.class, clientes.getClienteid());
            Collection<Ordenes> ordenesCollectionOld = persistentClientes.getOrdenesCollection();
            Collection<Ordenes> ordenesCollectionNew = clientes.getOrdenesCollection();
            List<String> illegalOrphanMessages = null;
            for (Ordenes ordenesCollectionOldOrdenes : ordenesCollectionOld) {
                if (!ordenesCollectionNew.contains(ordenesCollectionOldOrdenes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ordenes " + ordenesCollectionOldOrdenes + " since its clienteid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Ordenes> attachedOrdenesCollectionNew = new ArrayList<Ordenes>();
            for (Ordenes ordenesCollectionNewOrdenesToAttach : ordenesCollectionNew) {
                ordenesCollectionNewOrdenesToAttach = em.getReference(ordenesCollectionNewOrdenesToAttach.getClass(), ordenesCollectionNewOrdenesToAttach.getOrdenid());
                attachedOrdenesCollectionNew.add(ordenesCollectionNewOrdenesToAttach);
            }
            ordenesCollectionNew = attachedOrdenesCollectionNew;
            clientes.setOrdenesCollection(ordenesCollectionNew);
            clientes = em.merge(clientes);
            for (Ordenes ordenesCollectionNewOrdenes : ordenesCollectionNew) {
                if (!ordenesCollectionOld.contains(ordenesCollectionNewOrdenes)) {
                    Clientes oldClienteidOfOrdenesCollectionNewOrdenes = ordenesCollectionNewOrdenes.getClienteid();
                    ordenesCollectionNewOrdenes.setClienteid(clientes);
                    ordenesCollectionNewOrdenes = em.merge(ordenesCollectionNewOrdenes);
                    if (oldClienteidOfOrdenesCollectionNewOrdenes != null && !oldClienteidOfOrdenesCollectionNewOrdenes.equals(clientes)) {
                        oldClienteidOfOrdenesCollectionNewOrdenes.getOrdenesCollection().remove(ordenesCollectionNewOrdenes);
                        oldClienteidOfOrdenesCollectionNewOrdenes = em.merge(oldClienteidOfOrdenesCollectionNewOrdenes);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = clientes.getClienteid();
                if (findClientes(id) == null) {
                    throw new NonexistentEntityException("The clientes with id " + id + " no longer exists.");
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
            Clientes clientes;
            try {
                clientes = em.getReference(Clientes.class, id);
                clientes.getClienteid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clientes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Ordenes> ordenesCollectionOrphanCheck = clientes.getOrdenesCollection();
            for (Ordenes ordenesCollectionOrphanCheckOrdenes : ordenesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clientes (" + clientes + ") cannot be destroyed since the Ordenes " + ordenesCollectionOrphanCheckOrdenes + " in its ordenesCollection field has a non-nullable clienteid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(clientes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Clientes> findClientesEntities() {
        return findClientesEntities(true, -1, -1);
    }

    public List<Clientes> findClientesEntities(int maxResults, int firstResult) {
        return findClientesEntities(false, maxResults, firstResult);
    }

    private List<Clientes> findClientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clientes.class));
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

    public Clientes findClientes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clientes> rt = cq.from(Clientes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
