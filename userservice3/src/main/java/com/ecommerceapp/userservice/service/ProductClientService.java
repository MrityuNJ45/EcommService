package com.ecommerceapp.userservice.service;

import com.ecommerceapp.ProductServiceGrpc;
import com.ecommerceapp.userservice.models.Product;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductClientService {

    @GrpcClient("grpc-product-service")
    private ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;

    public Product createProduct(Product product) {
        com.ecommerceapp.Product.CreateProductRequest createProductRequest = com.ecommerceapp.Product.CreateProductRequest.newBuilder()
                .setName(product.getName())
                .setPrice(product.getPrice())
                .setQuantity(product.getQuantity())
                .build();
       com.ecommerceapp.Product.CreateProductResponse createProductResponse = productServiceBlockingStub.createProduct(createProductRequest);
       Product modelResponse = new Product(createProductResponse.getId(), createProductResponse.getName(), createProductResponse.getPrice(), createProductResponse.getQuantity());
       return modelResponse;
    }

    public Product getProduct(Integer id) {
        com.ecommerceapp.Product.GetProductRequest getProductRequest = com.ecommerceapp.Product.GetProductRequest.newBuilder()
                .setId(id).build();
        com.ecommerceapp.Product.GetProductResponse getProductResponse = productServiceBlockingStub.getProduct(getProductRequest);
        Product modelResponse = new Product(getProductResponse.getId(), getProductResponse.getName(), getProductResponse.getPrice(), getProductResponse.getQuantity());
        return modelResponse;
    }


}
