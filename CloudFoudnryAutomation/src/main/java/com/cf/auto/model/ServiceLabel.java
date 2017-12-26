package com.cf.auto.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by dipen on 12/26/2017.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ServiceLabel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String lableName;
}
