package com.api.jobo.JoboApi.api.service;

import com.api.jobo.JoboApi.api.domain.Models;
import com.api.jobo.JoboApi.api.model.ServiceRequestForm;
import com.api.jobo.JoboApi.api.model.ServiceUpdateForm;
import com.api.jobo.JoboApi.api.specification.ServicePredicate;
import com.api.jobo.JoboApi.utils.DataOps;
import com.sun.jdi.request.DuplicateRequestException;
import javassist.NotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import static com.api.jobo.JoboApi.globals.GlobalRepo.serviceRepo;

@Service
@Transactional
public class ServiceServiceImpl implements ServiceService{

    @Override
    public List<Models.Service> getAllServices(Specification<Models.Service> serviceSpecification) {
        return serviceRepo.findAll(serviceSpecification);
    }

    @Override
    public Models.Service saveService(ServiceRequestForm serviceRequestForm) throws ParseException {

        Optional<Models.Service> existing = findByName(serviceRequestForm.getName());

        if (existing.isPresent()) {
            throw new DuplicateRequestException("Service exists already");
        }

        return serviceRepo.save(new Models.Service(serviceRequestForm.getDescription(),serviceRequestForm.getName(), DataOps.getNowFormattedFullDate(),DataOps.getNowFormattedFullDate(),false));
    }

    @Override
    public Models.Service updateService(String serviceName,ServiceUpdateForm serviceUpdateForm) throws NotFoundException, ParseException {

        Optional<Models.Service> oldService = findByName(serviceName);

        if (oldService.isEmpty()) {
            throw new NotFoundException("Service not found");
        }

        Models.Service newService = oldService.get();

        if (serviceUpdateForm.getDisabled() != null) {
            newService.setDisabled(serviceUpdateForm.getDisabled());
        }
        if (serviceUpdateForm.getName() != null) {
            newService.setName(serviceUpdateForm.getName());
        }
        if (serviceUpdateForm.getDescription() != null) {
            newService.setDescription(serviceUpdateForm.getDescription());
        }

        newService.setUpdatedAt(DataOps.getNowFormattedFullDate());

        return serviceRepo.save(newService);
    }

    @Override
    public Optional<Models.Service> findByName(String name) {
        return serviceRepo.findByName(name);
    }

    @Override
    public Optional<Models.Service> findById(Long id) {
        return serviceRepo.findById(id);
    }


}
