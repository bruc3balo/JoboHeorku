package com.api.jobo.JoboApi.api.specification;


import com.api.jobo.JoboApi.api.domain.Models.Service;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ServicePredicate implements Specification<Service> {
    private Service service;
    private String name;
    private Long id;



    public ServicePredicate(Service service) {
        this.service = service;
    }

    public ServicePredicate(String name) {
        this.name = name;
    }

    public ServicePredicate(Long id) {
        this.id = id;
    }


    @Override
    public Predicate toPredicate(Root<Service> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Predicate p = cb.and();


        if (service != null) {
            if (service.getId() != null) {
                p.getExpressions().add(cb.equal(root.get("id"), service.getId()));
            }


            if (service.getName() != null) {
                p.getExpressions().add(cb.equal(cb.upper(root.get("name")), service.getName().toUpperCase()));
            }

        }


        if (name != null) {
            p.getExpressions().add(cb.equal(cb.upper(root.get("name")), name.toUpperCase()));
        }

        if (id != null) {
            p.getExpressions().add(cb.equal(root.get("id"), id));
        }
        return p;
    }

}
