package com.cf.auto.service.impl;

import com.cf.auto.model.ServicePlan;
import com.cf.auto.repository.IServicePlanRepository;
import com.cf.auto.service.IServicePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dipen on 12/26/2017.
 */
@Service
public class ServicePlanService implements IServicePlanService{
    @Autowired
    private IServicePlanRepository servicePlanRepository;

    @Override
    public ServicePlan saveServicePlan(ServicePlan servicePlan) {
        return servicePlanRepository.save(servicePlan);
    }

    @Override
    public ServicePlan updateServicePlan(ServicePlan servicePlan) {
        return servicePlanRepository.save(servicePlan);
    }

    @Override
    public ServicePlan getServicePlanById(long id) {
        return servicePlanRepository.findOne(id);
    }

    @Override
    public List<ServicePlan> getServicePlans() {
        return (List<ServicePlan>) servicePlanRepository.findAll();
    }

    @Override
    public void deleteServicePlan(long id) {
        servicePlanRepository.delete(id);
    }
}
