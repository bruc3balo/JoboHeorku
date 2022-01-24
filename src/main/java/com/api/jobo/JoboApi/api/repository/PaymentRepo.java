package com.api.jobo.JoboApi.api.repository;

import com.api.jobo.JoboApi.api.domain.Models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentRepo extends JpaRepository<Payment,Long>, JpaSpecificationExecutor<Payment> {

}
