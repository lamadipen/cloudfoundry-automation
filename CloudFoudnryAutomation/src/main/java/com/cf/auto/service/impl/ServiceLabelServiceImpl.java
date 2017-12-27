package com.cf.auto.service.impl;

import com.cf.auto.model.ServiceLabel;
import com.cf.auto.repository.IServiceLabelRepository;
import com.cf.auto.service.IServiceLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dipen on 12/26/2017.
 */
@Service
public class ServiceLabelServiceImpl implements IServiceLabelService{
    @Autowired
    private IServiceLabelRepository serviceLabelRepository;

    @Override
    public ServiceLabel saveServiceLabel(ServiceLabel serviceLabel) {
        return serviceLabelRepository.save(serviceLabel);
    }

    @Override
    public ServiceLabel updateServiceLabel(ServiceLabel serviceLabel) {
        return serviceLabelRepository.save(serviceLabel);
    }

    @Override
    public ServiceLabel getServiceLabelById(long id) {
        return serviceLabelRepository.findOne(id);
    }

    @Override
    public List<ServiceLabel> getServiceLabels() {
        return (List<ServiceLabel>) serviceLabelRepository.findAll();
    }

    @Override
    public void deleteServiceLabel(long id) {
        serviceLabelRepository.delete(id);
    }
}
