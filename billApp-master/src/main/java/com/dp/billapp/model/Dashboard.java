package com.dp.billapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class Dashboard {

    private long totalProducts;
    private long totalProductsInStock;
    private long totalCustomers;
    private long totalActiveCustomers;
    private long totalOrders;
    private long totalDrafts;
    private long totalEmployees;
    private long totalActiveEmployees;
    private String rateFor22K;
    private String rateFor18K;

}
