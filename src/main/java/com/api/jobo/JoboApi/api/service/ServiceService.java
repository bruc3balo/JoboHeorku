package com.api.jobo.JoboApi.api.service;

import com.api.jobo.JoboApi.api.domain.Models;
import com.api.jobo.JoboApi.api.domain.Models.Service;
import com.api.jobo.JoboApi.api.model.ServiceRequestForm;
import com.api.jobo.JoboApi.api.model.ServiceUpdateForm;
import javassist.NotFoundException;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface ServiceService {
    List<Service> getAllServices(Specification<Service> specification);
    Service saveService(ServiceRequestForm serviceRequestForm) throws ParseException;
    Optional<Service> findByName(String name);
    Optional<Service> findById(Long id);
    Service updateService(String serviceName,ServiceUpdateForm serviceUpdateForm) throws NotFoundException, ParseException;
}
