package com.dp.billapp.controller;

import com.dp.billapp.config.JwtResponse;
import com.dp.billapp.model.*;
import com.dp.billapp.repository.ProductRepository;
import com.dp.billapp.repository.UserRepository;
import com.dp.billapp.service.ProductService;
import com.dp.billapp.service.UserService;
import io.vavr.control.Option;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping(value="/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @PostMapping("/save")
    public ResponseEntity<?> saveProduct(@RequestBody ProductRequest productRequest, HttpServletRequest request){
        if(request.getContentLength()==0)
            return  new ResponseEntity<>("Token Not Found!!!",HttpStatus.NOT_FOUND);
        String userContact= userService.getContact(request);
        Option<User> userOptional = userService.findByContact(userContact);

        Option<Product> productOption = productService.findBySerialNo(productRequest.getSerialNo());
        if(!productOption.isEmpty())
            return new ResponseEntity<>("Product already exists!", HttpStatus.BAD_REQUEST);

        Date date = new Date();
        String strDateFormat = "dd/MM/yyyy/hhmmssa";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        String formattedDate = dateFormat.format(date);

        Product product = Product.builder().createdBy(userOptional.get().getId()).updatedBy(userOptional.get().getId()).purity(productRequest.getPurity())
                .grossWeight(productRequest.getGrossWeight()).netWeight(productRequest.getNetWeight())
                .name(productRequest.getName()).serialNo(productRequest.getSerialNo()).createdAt(formattedDate)
                .updatedAt(formattedDate)
                .inStock("1").build();

        return ResponseEntity.ok(productService.saveProduct(product));
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllProduct(){
        List<Product> products =productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search/{serialNo}")
    public ResponseEntity<?> findProductBySerialNo(@PathVariable String serialNo){
        Option<Product> productOption = productService.findBySerialNo(serialNo);
        if(productOption.isEmpty())
            return new ResponseEntity<>("Product Not exists!", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(productOption.get());
    }

    @GetMapping("/search/id/{id}")
    public ResponseEntity<?> findProductBySerialNo(@PathVariable long id){
        Option<Product> productOption = productService.findById(id);
        if(productOption.isEmpty())
            return new ResponseEntity<>("Product Not exists!", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(productOption.get());
    }

    @GetMapping("/allSerialNumber")
    public ResponseEntity<?> allSerialNumbers(){
        List<String> serialNumberList = productService.getallSerialNo();
        return ResponseEntity.ok(serialNumberList);

    }

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/all/status/{value}")
    public ResponseEntity<?> findProductByStock(@PathVariable String value){
        System.out.println(value);
        if(value.equals("2")){
            return  ResponseEntity.ok(productService.getAllProducts());
        }
        List<Product> productListByStockValue = productRepository.findByInStock(value);
        if(productListByStockValue.size()>0){
            return ResponseEntity.ok(productListByStockValue);
        }
        return ResponseEntity.ok(new ArrayList<>());

    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestBody ProductUpdateRequest productUpdateRequest, HttpServletRequest request){

        if(request.getContentLength()==0)
            return  new ResponseEntity<>("Token Not Found!!!",HttpStatus.NOT_FOUND);
        String userContact= userService.getContact(request);
        Option<User> userOptional = userService.findByContact(userContact);

        Date date = new Date();
        String strDateFormat = "dd/MM/yyyy/hhmmssa";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        String formattedDate = dateFormat.format(date);

        Option<Product> productOption = productService.findById(productUpdateRequest.getId());
        if(productOption.isEmpty())
            return new ResponseEntity<>("Product doesn't exists,can't be updated!!!!",HttpStatus.NOT_FOUND);

        Product product = Product.builder()
                .id(productOption.get().getId())
                .createdBy(productOption.get().getCreatedBy())
                .updatedBy(userOptional.get().getId())
                .purity(productUpdateRequest.getPurity())
                .grossWeight(productUpdateRequest.getGrossWeight())
                .netWeight(productUpdateRequest.getNetWeight())
                .name(productUpdateRequest.getName())
                .serialNo(productUpdateRequest.getSerialNo())
                .createdAt(productOption.get().getCreatedAt())
                .inStock(productUpdateRequest.getInStock())
                .updatedAt(formattedDate).build();

        return ResponseEntity.ok(productService.updateProduct(product));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable long id){
        Option<Product> productOption = productService.findById(id);
        if(productOption.isEmpty())
            return new ResponseEntity<>("Product doesn't exists,can't be deleted!!!!",HttpStatus.NOT_FOUND);

        return  ResponseEntity.ok(productService.deleteProduct(id));
    }


    @PostMapping("/status/{id}")
    public ResponseEntity<Product> productStatus(@PathVariable long id) {
        Option<Product> product = productService.findById(id);
        if(!product.isEmpty()){
            if(product.get().getInStock().equals("0"))product.get().setInStock("1");
            else product.get().setInStock("0");
        }
        return  ResponseEntity.ok(productService.saveProduct(product.get()));
    }


    @Data
    static class ProductRequest{

        private String serialNo;

        private String name;

        private String purity;

        private String grossWeight;

        private String netWeight;

    }

    @Data
    static class ProductUpdateRequest{

        private long Id;

        private String serialNo;

        private String name;

        private String purity;

        private String grossWeight;

        private String netWeight;

        private String inStock;

    }
}
