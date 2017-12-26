package com.cf.auto.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created by dipen on 12/26/2017.
 */
@Entity
@Getter @Setter @NoArgsConstructor @ToString(exclude = {"serviceLabel"})
public class ServicePlan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String planName;
    private String Planschema;
    @ManyToOne
    private ServiceLabel serviceLabel;



}
