/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.ups.controlador;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author ASUS
 */
public abstract class AbstractControlador<E> {

    private List<E> listaGenerica;
    private Class<E> tipo;
    private EntityManager em;

    public AbstractControlador(Class<E> tipo, EntityManager em) {
        listaGenerica = new ArrayList<>();
        this.em = em;
        this.tipo = tipo;
        this.listaGenerica = findAll();
    }

    public boolean crear(E obj) {
        em.getTransaction().begin();
        em.persist(obj);
        em.getTransaction().commit();
        this.listaGenerica = findAll();
        return true;
    }

    public E buscar(int id) {
        E resultado = em.find(tipo, id);
        return resultado;
    }

    public boolean eliminar(E obj) {
        em.getTransaction().begin();
        em.remove(obj);
        em.getTransaction().commit();
        this.listaGenerica = findAll();
        return true;
    }

    public boolean actualizar(E obj) {
        em.getTransaction().begin();
        em.merge(obj);
        em.getTransaction().commit();
        this.listaGenerica = findAll();
        return true;
    }
    
    public abstract List<E> findAll();

    public List<E> getListado() {
        return listaGenerica;
    }

    public void setListado(List<E> listado) {
        this.listaGenerica = listado;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

}
