package com.cf.auto.service;

import com.cf.auto.model.Service;

import java.util.List;

/**
 * Created by dipen on 12/26/2017.
 */
public interface IServiceService {
    public Service saveService(Service service);
    public Service updateService(Service service);
    public Service getServiceById(long id);
    public List<Service> getServices();
    public void deleteService(long id);
}
