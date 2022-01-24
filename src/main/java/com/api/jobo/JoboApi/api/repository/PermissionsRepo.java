package com.api.jobo.JoboApi.api.repository;

import com.api.jobo.JoboApi.api.domain.Models;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PermissionsRepo extends JpaRepository<Models.Permissions, Long>, JpaSpecificationExecutor<Models.Permissions> {
      Optional<Models.Permissions> findByName(String name);
}
