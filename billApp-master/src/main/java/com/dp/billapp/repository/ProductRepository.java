package com.dp.billapp.repository;


import com.dp.billapp.model.Product;
import com.dp.billapp.model.User;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Option<Product> findBySerialNo(String serialNo);
    Option<Product> findById(long id);
    List<Product> findByInStock(String value);

}
