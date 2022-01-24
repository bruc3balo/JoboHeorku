package com.api.jobo.JoboApi.api.specification;


import com.api.jobo.JoboApi.api.domain.Models;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ReviewPredicate implements Specification<Models.Review> {
    private Models.Review review;
    private String clientUsername;
    private String localServiceProviderUsername;
    private Long jobId;
    private Boolean reported;
    private Long id;

    public ReviewPredicate(Long id) {
        this.id = id;
    }

    public ReviewPredicate(Models.Review review) {
        this.review = review;
    }

    public ReviewPredicate(String clientUsername, String localServiceProviderUsername) {
        this.clientUsername = clientUsername;
        this.localServiceProviderUsername = localServiceProviderUsername;
    }

    public ReviewPredicate(Boolean reported) {
        this.reported = reported;
    }


    @Override
    public Predicate toPredicate(Root<Models.Review> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Predicate p = cb.and();


        if (review != null) {
            if (review.getId() != null) {
                p.getExpressions().add(cb.equal(root.get("id"), review.getId()));
            }

            if (review.getClientUsername() != null) {
                p.getExpressions().add(cb.equal(root.get("clientUsername"), review.getClientUsername()));
            }

            if (review.getLocalServiceProviderUsername() != null) {
                p.getExpressions().add(cb.equal(root.get("localServiceProviderUsername"), review.getLocalServiceProviderUsername()));
            }

            if (review.getJobId() != null) {
                p.getExpressions().add(cb.equal(root.get("jobId"), review.getJobId()));
            }

            if (review.getReported() != null) {
                p.getExpressions().add(cb.equal(root.get("reported"), review.getReported()));
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

        if (jobId != null) {
            p.getExpressions().add(cb.equal(root.get("jobStatus"), jobId));
        }

        if (reported != null) {
            p.getExpressions().add(cb.equal(root.get("reported"), reported));
        }

        return p;
    }

}
