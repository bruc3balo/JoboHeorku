package com.api.jobo.JoboApi.api.specification;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import java.util.Date;

import static com.api.jobo.JoboApi.api.domain.Models.*;

public class PaymentSpecification implements Specification<Payment> {

    private Payment payment;
    private String mobileNumber;
    private String checkoutRequestID;
    private Date createdAt;
    private Boolean paid;

    public PaymentSpecification(Date createdAt, Boolean paid) {
        this.createdAt = createdAt;
        this.paid = paid;
    }

    public PaymentSpecification(Payment payment) {
        this.payment = payment;
    }

    public PaymentSpecification(String mobileNumber, String checkoutRequestId) {
        this.mobileNumber = mobileNumber;
        this.checkoutRequestID = checkoutRequestId;
    }

    @Override
    public Predicate toPredicate(@NotNull Root<Payment> root,@NotNull CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate p = cb.and();

        if (payment != null) {
            if (payment.getId() != null) {
                p.getExpressions().add(cb.equal(root.get("id"), payment.getId()));
            }

            if (payment.getPhoneNumber() != null) {
                p.getExpressions().add(cb.equal(root.get("phoneNumber"), payment.getPhoneNumber()));
            }


            if (payment.getCheckoutRequestId() != null) {
                p.getExpressions().add(cb.equal(root.get("checkoutRequestId"), payment.getCheckoutRequestId()));
            }


        }

        if (mobileNumber != null) {
            p.getExpressions().add(cb.equal(root.get("phoneNumber"), mobileNumber));
        }

        if (checkoutRequestID != null) {
            p.getExpressions().add(cb.equal(root.get("checkoutRequestId"), checkoutRequestID));
        }


        return p;
    }
}
