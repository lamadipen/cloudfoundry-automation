package com.cf.auto.service;

import com.cf.auto.model.ServicePlan;

import java.util.List;

/**
 * Created by dipen on 12/26/2017.
 */
public interface IServicePlanService {
    public ServicePlan saveServicePlan(ServicePlan servicePlan);
    public ServicePlan updateServicePlan(ServicePlan servicePlan);
    public ServicePlan getServicePlanById(long id);
    public List<ServicePlan> getServicePlans();
    public void deleteServicePlan(long id);
}
