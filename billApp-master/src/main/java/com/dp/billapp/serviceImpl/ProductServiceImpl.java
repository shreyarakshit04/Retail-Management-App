package com.dp.billapp.serviceImpl;

import com.dp.billapp.daoService.ProductDaoService;
import com.dp.billapp.model.Product;
import com.dp.billapp.repository.ProductRepository;
import com.dp.billapp.service.ProductService;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private final ProductDaoService productDaoService;

    @Override
    public Product saveProduct(Product product) {
        return productDaoService.saveProduct(product);
    }

    @Override
    public Option<Product> findBySerialNo(String serialNo) {
        return productDaoService.findBySerialNo(serialNo);
    }

    @Override
    public Option<Product> findById(long id) {
        return productDaoService.findById(id);
    }

    @Override
    public Product updateProduct(Product product) {
        return productDaoService.updateProduct(product);
    }

    @Override
    public String deleteProduct(long id) {
        productDaoService.deleteProduct(id);
        return "Product has been deleted";
    }

    @Override
    public List<String> getallSerialNo() {
        ArrayList<String> serialNumberList = new ArrayList<>();
        List<Product> allProducts = productDaoService.allProducts();
        for(Product p: allProducts){
            serialNumberList.add(p.getSerialNo());
        }
        return serialNumberList;
    }

    @Override
    public List<Product> getAllProducts() {
        return productDaoService.allProducts();
    }
}
