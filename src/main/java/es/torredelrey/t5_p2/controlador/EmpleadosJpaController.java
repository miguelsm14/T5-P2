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
import es.torredelrey.t5_p2.modelo.Empleados;
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
public class EmpleadosJpaController implements Serializable {

    public EmpleadosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleados empleados) throws PreexistingEntityException, Exception {
        if (empleados.getOrdenesCollection() == null) {
            empleados.setOrdenesCollection(new ArrayList<Ordenes>());
        }
        if (empleados.getEmpleadosCollection() == null) {
            empleados.setEmpleadosCollection(new ArrayList<Empleados>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleados reportaA = empleados.getReportaA();
            if (reportaA != null) {
                reportaA = em.getReference(reportaA.getClass(), reportaA.getEmpleadoid());
                empleados.setReportaA(reportaA);
            }
            Collection<Ordenes> attachedOrdenesCollection = new ArrayList<Ordenes>();
            for (Ordenes ordenesCollectionOrdenesToAttach : empleados.getOrdenesCollection()) {
                ordenesCollectionOrdenesToAttach = em.getReference(ordenesCollectionOrdenesToAttach.getClass(), ordenesCollectionOrdenesToAttach.getOrdenid());
                attachedOrdenesCollection.add(ordenesCollectionOrdenesToAttach);
            }
            empleados.setOrdenesCollection(attachedOrdenesCollection);
            Collection<Empleados> attachedEmpleadosCollection = new ArrayList<Empleados>();
            for (Empleados empleadosCollectionEmpleadosToAttach : empleados.getEmpleadosCollection()) {
                empleadosCollectionEmpleadosToAttach = em.getReference(empleadosCollectionEmpleadosToAttach.getClass(), empleadosCollectionEmpleadosToAttach.getEmpleadoid());
                attachedEmpleadosCollection.add(empleadosCollectionEmpleadosToAttach);
            }
            empleados.setEmpleadosCollection(attachedEmpleadosCollection);
            em.persist(empleados);
            if (reportaA != null) {
                reportaA.getEmpleadosCollection().add(empleados);
                reportaA = em.merge(reportaA);
            }
            for (Ordenes ordenesCollectionOrdenes : empleados.getOrdenesCollection()) {
                Empleados oldEmpleadoidOfOrdenesCollectionOrdenes = ordenesCollectionOrdenes.getEmpleadoid();
                ordenesCollectionOrdenes.setEmpleadoid(empleados);
                ordenesCollectionOrdenes = em.merge(ordenesCollectionOrdenes);
                if (oldEmpleadoidOfOrdenesCollectionOrdenes != null) {
                    oldEmpleadoidOfOrdenesCollectionOrdenes.getOrdenesCollection().remove(ordenesCollectionOrdenes);
                    oldEmpleadoidOfOrdenesCollectionOrdenes = em.merge(oldEmpleadoidOfOrdenesCollectionOrdenes);
                }
            }
            for (Empleados empleadosCollectionEmpleados : empleados.getEmpleadosCollection()) {
                Empleados oldReportaAOfEmpleadosCollectionEmpleados = empleadosCollectionEmpleados.getReportaA();
                empleadosCollectionEmpleados.setReportaA(empleados);
                empleadosCollectionEmpleados = em.merge(empleadosCollectionEmpleados);
                if (oldReportaAOfEmpleadosCollectionEmpleados != null) {
                    oldReportaAOfEmpleadosCollectionEmpleados.getEmpleadosCollection().remove(empleadosCollectionEmpleados);
                    oldReportaAOfEmpleadosCollectionEmpleados = em.merge(oldReportaAOfEmpleadosCollectionEmpleados);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmpleados(empleados.getEmpleadoid()) != null) {
                throw new PreexistingEntityException("Empleados " + empleados + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empleados empleados) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleados persistentEmpleados = em.find(Empleados.class, empleados.getEmpleadoid());
            Empleados reportaAOld = persistentEmpleados.getReportaA();
            Empleados reportaANew = empleados.getReportaA();
            Collection<Ordenes> ordenesCollectionOld = persistentEmpleados.getOrdenesCollection();
            Collection<Ordenes> ordenesCollectionNew = empleados.getOrdenesCollection();
            Collection<Empleados> empleadosCollectionOld = persistentEmpleados.getEmpleadosCollection();
            Collection<Empleados> empleadosCollectionNew = empleados.getEmpleadosCollection();
            List<String> illegalOrphanMessages = null;
            for (Ordenes ordenesCollectionOldOrdenes : ordenesCollectionOld) {
                if (!ordenesCollectionNew.contains(ordenesCollectionOldOrdenes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ordenes " + ordenesCollectionOldOrdenes + " since its empleadoid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (reportaANew != null) {
                reportaANew = em.getReference(reportaANew.getClass(), reportaANew.getEmpleadoid());
                empleados.setReportaA(reportaANew);
            }
            Collection<Ordenes> attachedOrdenesCollectionNew = new ArrayList<Ordenes>();
            for (Ordenes ordenesCollectionNewOrdenesToAttach : ordenesCollectionNew) {
                ordenesCollectionNewOrdenesToAttach = em.getReference(ordenesCollectionNewOrdenesToAttach.getClass(), ordenesCollectionNewOrdenesToAttach.getOrdenid());
                attachedOrdenesCollectionNew.add(ordenesCollectionNewOrdenesToAttach);
            }
            ordenesCollectionNew = attachedOrdenesCollectionNew;
            empleados.setOrdenesCollection(ordenesCollectionNew);
            Collection<Empleados> attachedEmpleadosCollectionNew = new ArrayList<Empleados>();
            for (Empleados empleadosCollectionNewEmpleadosToAttach : empleadosCollectionNew) {
                empleadosCollectionNewEmpleadosToAttach = em.getReference(empleadosCollectionNewEmpleadosToAttach.getClass(), empleadosCollectionNewEmpleadosToAttach.getEmpleadoid());
                attachedEmpleadosCollectionNew.add(empleadosCollectionNewEmpleadosToAttach);
            }
            empleadosCollectionNew = attachedEmpleadosCollectionNew;
            empleados.setEmpleadosCollection(empleadosCollectionNew);
            empleados = em.merge(empleados);
            if (reportaAOld != null && !reportaAOld.equals(reportaANew)) {
                reportaAOld.getEmpleadosCollection().remove(empleados);
                reportaAOld = em.merge(reportaAOld);
            }
            if (reportaANew != null && !reportaANew.equals(reportaAOld)) {
                reportaANew.getEmpleadosCollection().add(empleados);
                reportaANew = em.merge(reportaANew);
            }
            for (Ordenes ordenesCollectionNewOrdenes : ordenesCollectionNew) {
                if (!ordenesCollectionOld.contains(ordenesCollectionNewOrdenes)) {
                    Empleados oldEmpleadoidOfOrdenesCollectionNewOrdenes = ordenesCollectionNewOrdenes.getEmpleadoid();
                    ordenesCollectionNewOrdenes.setEmpleadoid(empleados);
                    ordenesCollectionNewOrdenes = em.merge(ordenesCollectionNewOrdenes);
                    if (oldEmpleadoidOfOrdenesCollectionNewOrdenes != null && !oldEmpleadoidOfOrdenesCollectionNewOrdenes.equals(empleados)) {
                        oldEmpleadoidOfOrdenesCollectionNewOrdenes.getOrdenesCollection().remove(ordenesCollectionNewOrdenes);
                        oldEmpleadoidOfOrdenesCollectionNewOrdenes = em.merge(oldEmpleadoidOfOrdenesCollectionNewOrdenes);
                    }
                }
            }
            for (Empleados empleadosCollectionOldEmpleados : empleadosCollectionOld) {
                if (!empleadosCollectionNew.contains(empleadosCollectionOldEmpleados)) {
                    empleadosCollectionOldEmpleados.setReportaA(null);
                    empleadosCollectionOldEmpleados = em.merge(empleadosCollectionOldEmpleados);
                }
            }
            for (Empleados empleadosCollectionNewEmpleados : empleadosCollectionNew) {
                if (!empleadosCollectionOld.contains(empleadosCollectionNewEmpleados)) {
                    Empleados oldReportaAOfEmpleadosCollectionNewEmpleados = empleadosCollectionNewEmpleados.getReportaA();
                    empleadosCollectionNewEmpleados.setReportaA(empleados);
                    empleadosCollectionNewEmpleados = em.merge(empleadosCollectionNewEmpleados);
                    if (oldReportaAOfEmpleadosCollectionNewEmpleados != null && !oldReportaAOfEmpleadosCollectionNewEmpleados.equals(empleados)) {
                        oldReportaAOfEmpleadosCollectionNewEmpleados.getEmpleadosCollection().remove(empleadosCollectionNewEmpleados);
                        oldReportaAOfEmpleadosCollectionNewEmpleados = em.merge(oldReportaAOfEmpleadosCollectionNewEmpleados);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = empleados.getEmpleadoid();
                if (findEmpleados(id) == null) {
                    throw new NonexistentEntityException("The empleados with id " + id + " no longer exists.");
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
            Empleados empleados;
            try {
                empleados = em.getReference(Empleados.class, id);
                empleados.getEmpleadoid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleados with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Ordenes> ordenesCollectionOrphanCheck = empleados.getOrdenesCollection();
            for (Ordenes ordenesCollectionOrphanCheckOrdenes : ordenesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleados (" + empleados + ") cannot be destroyed since the Ordenes " + ordenesCollectionOrphanCheckOrdenes + " in its ordenesCollection field has a non-nullable empleadoid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Empleados reportaA = empleados.getReportaA();
            if (reportaA != null) {
                reportaA.getEmpleadosCollection().remove(empleados);
                reportaA = em.merge(reportaA);
            }
            Collection<Empleados> empleadosCollection = empleados.getEmpleadosCollection();
            for (Empleados empleadosCollectionEmpleados : empleadosCollection) {
                empleadosCollectionEmpleados.setReportaA(null);
                empleadosCollectionEmpleados = em.merge(empleadosCollectionEmpleados);
            }
            em.remove(empleados);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empleados> findEmpleadosEntities() {
        return findEmpleadosEntities(true, -1, -1);
    }

    public List<Empleados> findEmpleadosEntities(int maxResults, int firstResult) {
        return findEmpleadosEntities(false, maxResults, firstResult);
    }

    private List<Empleados> findEmpleadosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleados.class));
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

    public Empleados findEmpleados(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleados.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleados> rt = cq.from(Empleados.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
