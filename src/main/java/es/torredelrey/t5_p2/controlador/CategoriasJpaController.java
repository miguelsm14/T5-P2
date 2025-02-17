/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.torredelrey.t5_p2.controlador;

import es.torredelrey.t5_p2.controlador.exceptions.IllegalOrphanException;
import es.torredelrey.t5_p2.controlador.exceptions.NonexistentEntityException;
import es.torredelrey.t5_p2.controlador.exceptions.PreexistingEntityException;
import es.torredelrey.t5_p2.modelo.Categorias;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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
public class CategoriasJpaController implements Serializable {

    public CategoriasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Categorias categorias) throws PreexistingEntityException, Exception {
        if (categorias.getProductosCollection() == null) {
            categorias.setProductosCollection(new ArrayList<Productos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Productos> attachedProductosCollection = new ArrayList<Productos>();
            for (Productos productosCollectionProductosToAttach : categorias.getProductosCollection()) {
                productosCollectionProductosToAttach = em.getReference(productosCollectionProductosToAttach.getClass(), productosCollectionProductosToAttach.getProductoid());
                attachedProductosCollection.add(productosCollectionProductosToAttach);
            }
            categorias.setProductosCollection(attachedProductosCollection);
            em.persist(categorias);
            for (Productos productosCollectionProductos : categorias.getProductosCollection()) {
                Categorias oldCategoriaidOfProductosCollectionProductos = productosCollectionProductos.getCategoriaid();
                productosCollectionProductos.setCategoriaid(categorias);
                productosCollectionProductos = em.merge(productosCollectionProductos);
                if (oldCategoriaidOfProductosCollectionProductos != null) {
                    oldCategoriaidOfProductosCollectionProductos.getProductosCollection().remove(productosCollectionProductos);
                    oldCategoriaidOfProductosCollectionProductos = em.merge(oldCategoriaidOfProductosCollectionProductos);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCategorias(categorias.getCategoriaid()) != null) {
                throw new PreexistingEntityException("Categorias " + categorias + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Categorias categorias) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categorias persistentCategorias = em.find(Categorias.class, categorias.getCategoriaid());
            Collection<Productos> productosCollectionOld = persistentCategorias.getProductosCollection();
            Collection<Productos> productosCollectionNew = categorias.getProductosCollection();
            List<String> illegalOrphanMessages = null;
            for (Productos productosCollectionOldProductos : productosCollectionOld) {
                if (!productosCollectionNew.contains(productosCollectionOldProductos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Productos " + productosCollectionOldProductos + " since its categoriaid field is not nullable.");
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
            categorias.setProductosCollection(productosCollectionNew);
            categorias = em.merge(categorias);
            for (Productos productosCollectionNewProductos : productosCollectionNew) {
                if (!productosCollectionOld.contains(productosCollectionNewProductos)) {
                    Categorias oldCategoriaidOfProductosCollectionNewProductos = productosCollectionNewProductos.getCategoriaid();
                    productosCollectionNewProductos.setCategoriaid(categorias);
                    productosCollectionNewProductos = em.merge(productosCollectionNewProductos);
                    if (oldCategoriaidOfProductosCollectionNewProductos != null && !oldCategoriaidOfProductosCollectionNewProductos.equals(categorias)) {
                        oldCategoriaidOfProductosCollectionNewProductos.getProductosCollection().remove(productosCollectionNewProductos);
                        oldCategoriaidOfProductosCollectionNewProductos = em.merge(oldCategoriaidOfProductosCollectionNewProductos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = categorias.getCategoriaid();
                if (findCategorias(id) == null) {
                    throw new NonexistentEntityException("The categorias with id " + id + " no longer exists.");
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
            Categorias categorias;
            try {
                categorias = em.getReference(Categorias.class, id);
                categorias.getCategoriaid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categorias with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Productos> productosCollectionOrphanCheck = categorias.getProductosCollection();
            for (Productos productosCollectionOrphanCheckProductos : productosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Categorias (" + categorias + ") cannot be destroyed since the Productos " + productosCollectionOrphanCheckProductos + " in its productosCollection field has a non-nullable categoriaid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(categorias);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Categorias> findCategoriasEntities() {
        return findCategoriasEntities(true, -1, -1);
    }

    public List<Categorias> findCategoriasEntities(int maxResults, int firstResult) {
        return findCategoriasEntities(false, maxResults, firstResult);
    }

    private List<Categorias> findCategoriasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Categorias.class));
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

    public Categorias findCategorias(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Categorias.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoriasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Categorias> rt = cq.from(Categorias.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
