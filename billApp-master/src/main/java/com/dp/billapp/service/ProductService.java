package com.dp.billapp.service;

import com.dp.billapp.model.Product;
import io.vavr.control.Option;

import java.util.List;

public interface ProductService {
    Product saveProduct(Product product);
    Option<Product> findBySerialNo(String serialNo);
    Option<Product> findById(long id);
    Product updateProduct(Product product);
    String deleteProduct(long id);
    List<String> getallSerialNo();
    List<Product> getAllProducts();
}
