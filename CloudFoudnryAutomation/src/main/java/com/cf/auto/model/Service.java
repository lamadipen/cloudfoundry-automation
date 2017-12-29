package com.cf.auto.model;

import lombok.*;

import javax.persistence.*;

/**
 * Created by dipen on 12/26/2017.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"servicePlan"})
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String serviceName;
    private String description;
    @Column(columnDefinition = "longtext",length = 16000000)
    @Lob
    private String parameters;
    @OneToOne
    private ServicePlan servicePlan;
    @Transient
    private Pool pool;
}
