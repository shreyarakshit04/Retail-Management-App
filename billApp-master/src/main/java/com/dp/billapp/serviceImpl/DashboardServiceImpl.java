package com.dp.billapp.serviceImpl;

import com.dp.billapp.model.Dashboard;
import com.dp.billapp.model.GoldRate;
import com.dp.billapp.model.UserConstants;
import com.dp.billapp.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DashboardServiceImpl {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    GoldrateRepository goldrateRepository;
    @Autowired
    DraftRepository draftRepository;

    public Dashboard getDashboard(){

        long totalOrders =  invoiceRepository.findAll().size();
        long totalProducts =  productRepository.findAll().size();
        long totalProductInStock = productRepository.findByInStock("1").size();
        long totalActiveCustomers =  userRepository.findByRoleAndIsActive(UserConstants.CustomerRole,"1").size();
        long totalCustomers=userRepository.findByRole(UserConstants.CustomerRole).size();
        long totalActiveEmployees = userRepository.findByRoleAndIsActive(UserConstants.EditorRole,"1").size();
        long totalEmployees =userRepository.findByRole(UserConstants.EditorRole).size();
        long totalDrafts =draftRepository.findAll().size();
        List<GoldRate> goldRate = goldrateRepository.findAll();
        String rateFor22K= goldRate.get(0).getRateFor22K();
        String rateFor18K= goldRate.get(0).getRateFor18K();

        Dashboard dashboard = Dashboard.builder()
                .totalProducts(totalProducts)
                .totalProductsInStock(totalProductInStock)
                .totalEmployees(totalEmployees)
                .totalActiveEmployees(totalActiveEmployees)
                .totalCustomers(totalCustomers)
                .totalActiveCustomers(totalActiveCustomers)
                .totalOrders(totalOrders)
                .totalDrafts(totalDrafts)
                .rateFor22K(rateFor22K)
                .rateFor18K(rateFor18K)
                .build();

        return  dashboard;
    }
}
