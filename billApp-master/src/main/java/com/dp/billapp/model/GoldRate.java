package com.dp.billapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity

@Table(name = "goldrate")
public class GoldRate {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "rate_for_22k")
    private String rateFor22K;

    @Column(name = "rate_for_18k")
    private String rateFor18K;
}
