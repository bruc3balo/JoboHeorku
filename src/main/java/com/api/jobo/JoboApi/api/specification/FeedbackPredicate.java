package com.api.jobo.JoboApi.api.specification;


import com.api.jobo.JoboApi.api.domain.Models;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class FeedbackPredicate implements Specification<Models.Feedback> {
    private Models.Feedback feedback;
    private Integer rating;
    private Long id;
    private Long userId;

    public FeedbackPredicate(Models.Feedback feedback) {
        this.feedback = feedback;
    }

    public FeedbackPredicate(Long id) {
        this.id = id;
    }

    public FeedbackPredicate(Integer rating,Long userId) {
        this.rating = rating;
        this.userId = userId;
    }

    @Override
    public Predicate toPredicate(Root<Models.Feedback> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Predicate p = cb.and();


        if (feedback != null) {
            if (feedback.getId() != null) {
                p.getExpressions().add(cb.equal(root.get("id"), feedback.getId()));
            }

            if (feedback.getRating() != null) {
                p.getExpressions().add(cb.equal(root.get("rating"), feedback.getRating()));
            }

            if (feedback.getUser().getId() != null) {
                p.getExpressions().add(cb.equal(root.join("user").get("id"), feedback.getUser().getId()));
            }

        }


        if (id != null) {
            p.getExpressions().add(cb.equal(root.get("id"), id));
        }


        if (rating != null) {
            p.getExpressions().add(cb.equal(root.get("rating"), rating));
        }

        if (userId != null) {
            p.getExpressions().add(cb.equal(root.join("user").get("id"), userId));
        }

        return p;
    }

}
