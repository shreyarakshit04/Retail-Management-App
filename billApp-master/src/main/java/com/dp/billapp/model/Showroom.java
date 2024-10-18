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

@Table(name = "showroom")

public class Showroom {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "primary_contact")
    private String primaryContact;

    @Column(name = "secondary_contact")
    private String secondaryContact;

    @Column(name = "email")
    private String email;

    @Column(name = "gst_in")
    private String gstIn;

    @Column(name = "hsn_code")
    private String hsnNo;

    @Column(name = "showroom_logo")
    private String showroomLogo;

    @Column(name = "hallmark_logo")

    private String hallMarkLogo;


}
