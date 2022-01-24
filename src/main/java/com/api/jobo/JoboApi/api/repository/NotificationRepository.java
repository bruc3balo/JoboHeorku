package com.api.jobo.JoboApi.api.repository;

import com.api.jobo.JoboApi.api.domain.Models;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Models.NotificationModels,Long>, JpaSpecificationExecutor<Models.NotificationModels> {

}
