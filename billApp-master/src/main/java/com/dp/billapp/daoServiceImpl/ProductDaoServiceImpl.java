package com.dp.billapp.daoServiceImpl;

import com.dp.billapp.daoService.ProductDaoService;
import com.dp.billapp.model.Product;
import com.dp.billapp.repository.ProductRepository;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductDaoServiceImpl implements ProductDaoService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Option<Product> findBySerialNo(String serialNo) {
        return productRepository.findBySerialNo(serialNo);
    }

    @Override
    public Option<Product> findById(long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public String deleteProduct(long id) {
        productRepository.deleteById(id);
        return "Product has been deleted";
    }

    @Override
    public List<Product> allProducts() {
        return productRepository.findAll();
    }
}
