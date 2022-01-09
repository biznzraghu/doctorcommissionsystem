package org.nh.artha.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
@Repository
public class CommonValueSetCodeRepository {

    @Autowired
    private EntityManager em;

    public <T> List<T> findAll(Class<T> tClass) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(tClass);
        cq.from(tClass);
        return em.createQuery(cq).getResultList();
    }

    public <T> T findOne(Class<T> tClass, Long id) {
        return em.find(tClass, id);
    }
}
