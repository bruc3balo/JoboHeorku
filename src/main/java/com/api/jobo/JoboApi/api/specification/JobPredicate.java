package com.api.jobo.JoboApi.api.specification;


import com.api.jobo.JoboApi.api.domain.Models;
import com.api.jobo.JoboApi.api.domain.Models.Service;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class JobPredicate implements Specification<Models.Job> {
    private Models.Job job;
    private String clientUsername;
    private String localServiceProviderUsername;
    private Integer jobStatus;
    private Long id;

    public JobPredicate(Long id) {
        this.id = id;
    }


    public JobPredicate(Models.Job job) {
        this.job = job;
    }

    public JobPredicate(String clientUsername, String localServiceProviderUsername) {
        this.clientUsername = clientUsername;
        this.localServiceProviderUsername = localServiceProviderUsername;
    }

    public JobPredicate(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }

    @Override
    public Predicate toPredicate(Root<Models.Job> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Predicate p = cb.and();


        if (job != null) {
            if (job.getId() != null) {
                p.getExpressions().add(cb.equal(root.get("id"), job.getId()));
            }

            if (job.getClientUsername() != null) {
                p.getExpressions().add(cb.equal(root.get("clientUsername"), job.getClientUsername()));
            }

            if (job.getLocalServiceProviderUsername() != null) {
                p.getExpressions().add(cb.equal(root.get("localServiceProviderUsername"), job.getLocalServiceProviderUsername()));
            }

            if (job.getJobStatus() != null) {
                p.getExpressions().add(cb.equal(root.get("jobStatus"), job.getJobStatus()));
            }

        }


        if (id != null) {
            p.getExpressions().add(cb.equal(root.get("id"), id));
        }


        if (clientUsername != null) {
            p.getExpressions().add(cb.equal(root.get("clientUsername"), clientUsername));
        }

        if (localServiceProviderUsername != null) {
            p.getExpressions().add(cb.equal(root.get("localServiceProviderUsername"), localServiceProviderUsername));
        }

        if (jobStatus != null) {
            p.getExpressions().add(cb.equal(root.get("jobStatus"), jobStatus));
        }

        return p;
    }

}
