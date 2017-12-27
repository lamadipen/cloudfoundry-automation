package com.cf.auto.service.impl;

import com.cf.auto.model.Service;
import com.cf.auto.repository.IServiceRepository;
import com.cf.auto.service.IServiceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by dipen on 12/26/2017.
 */
@org.springframework.stereotype.Service
public class ServiceServiceImpl implements IServiceService {
    @Autowired
    private IServiceRepository serviceRepository;

    @Override
    public Service saveService(Service service) {
        return serviceRepository.save(service);
    }

    @Override
    public Service updateService(Service service) {
        return serviceRepository.save(service);
    }

    @Override
    public Service getServiceById(long id) {
        return serviceRepository.findOne(id);
    }

    @Override
    public List<Service> getServices() {
        return (List<Service>) serviceRepository.findAll();
    }

    @Override
    public void deleteService(long id) {
        serviceRepository.delete(id);
    }
}
