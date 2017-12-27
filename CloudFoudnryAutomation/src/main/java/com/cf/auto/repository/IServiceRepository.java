package com.cf.auto.repository;

import com.cf.auto.model.Service;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by dipen on 12/26/2017.
 */
@Repository
public interface IServiceRepository extends CrudRepository<Service, Long> {
}
