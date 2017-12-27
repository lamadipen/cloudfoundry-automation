package com.cf.auto.service;

import com.cf.auto.model.ServiceLabel;

import java.util.List;

/**
 * Created by dipen on 12/26/2017.
 */
public interface IServiceLabelService {
    public ServiceLabel saveServiceLabel(ServiceLabel serviceLabel);
    public ServiceLabel updateServiceLabel(ServiceLabel serviceLabel);
    public ServiceLabel getServiceLabelById(long id);
    public List<ServiceLabel> getServiceLabels();
    public void deleteServiceLabel(long id);
}
