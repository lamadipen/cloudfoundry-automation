package com.cf.auto.repository;

import com.cf.auto.model.ServiceLabel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by dipen on 12/26/2017.
 */
@Repository
public interface IServiceLabelRepository extends CrudRepository<ServiceLabel, Long> {
}
