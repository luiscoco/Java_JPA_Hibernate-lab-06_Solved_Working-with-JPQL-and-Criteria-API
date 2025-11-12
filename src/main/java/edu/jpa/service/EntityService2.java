package edu.jpa.service;

import edu.jpa.entity.Employee;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import java.util.List;

public class EntityService2 extends EntityService {

    @Override
    public List<Employee> getEmployeesByDepartmentName(String name) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
            Root<Employee> e = cq.from(Employee.class);
            ParameterExpression<String> nameParam = cb.parameter(String.class, "name");
            cq.where(cb.equal(e.get("department").get("name"), nameParam));

            em.getTransaction().begin();
            TypedQuery<Employee> query = em.createQuery(cq);
            query.setParameter("name", name);
            List<Employee> result = query.getResultList();
            em.getTransaction().commit();
            return result;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    @Override
    public List<DepartmentInfo> getDepartmentsInfo() {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<DepartmentInfo> cq = cb.createQuery(DepartmentInfo.class);
            Root<Employee> e = cq.from(Employee.class);
            cq.select(cb.construct(
                    DepartmentInfo.class,
                    e.get("department").get("name"),
                    cb.count(e.get("department"))
            ));
            cq.groupBy(e.get("department"));

            em.getTransaction().begin();
            TypedQuery<DepartmentInfo> query = em.createQuery(cq);
            List<DepartmentInfo> result = query.getResultList();
            em.getTransaction().commit();
            return result;
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}
