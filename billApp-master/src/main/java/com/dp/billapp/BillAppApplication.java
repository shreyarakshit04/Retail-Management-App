package com.dp.billapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class BillAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillAppApplication.class, args);
	}

}
