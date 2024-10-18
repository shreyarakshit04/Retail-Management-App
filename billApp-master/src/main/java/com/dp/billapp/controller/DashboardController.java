package com.dp.billapp.controller;

import com.dp.billapp.model.Dashboard;
import com.dp.billapp.serviceImpl.DashboardServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value="/dashboard")
public class DashboardController {
    @Autowired
    DashboardServiceImpl dashboardService;

    @GetMapping("/")
    public ResponseEntity<Dashboard> getDashboard(){
       return  ResponseEntity.ok(dashboardService.getDashboard());
    }
}
