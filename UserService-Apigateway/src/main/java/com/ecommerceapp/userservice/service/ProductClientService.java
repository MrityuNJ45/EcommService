package com.ecommerceapp.userservice.service;


import com.ecommerceapp.ProductOuterClass;
import com.ecommerceapp.ProductServiceGrpc;
import com.ecommerceapp.userservice.models.Product;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductClientService {

    @GrpcClient("grpc-product-service")
    private ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;

    public Product createProduct(Product product) {

        ProductOuterClass.CreateProductRequest createProductRequest = ProductOuterClass.CreateProductRequest.newBuilder()
                .setName(product.getName())
                .setPrice(product.getPrice())
                .setQuantity(product.getQuantity())
                .build();
        ProductOuterClass.CreateProductResponse createProductResponse = productServiceBlockingStub.createProduct(createProductRequest);
        return new Product(createProductResponse.getId(), createProductResponse.getName(), createProductResponse.getPrice(), createProductResponse.getQuantity());
    }

    public Product getProduct(Integer id) {
        ProductOuterClass.GetProductRequest getProductRequest = ProductOuterClass.GetProductRequest.newBuilder()
                .setId(id).build();

        ProductOuterClass.GetProductResponse getProductResponse = productServiceBlockingStub.getProduct(getProductRequest);
        Product modelResponse = new Product(getProductResponse.getId(), getProductResponse.getName(), getProductResponse.getPrice(), getProductResponse.getQuantity());
        return modelResponse;
    }

    public List<Product> getAllProducts() {

        ProductOuterClass.GetAllProductsRequest getAllProductsRequest = ProductOuterClass.GetAllProductsRequest.newBuilder()
                .build();
        ProductOuterClass.GetAllProductsResponse getAllProductsResponse = productServiceBlockingStub.getAllProducts(getAllProductsRequest);

        List<Product> responseProducts = new ArrayList<>();
        for (ProductOuterClass.Product product : getAllProductsResponse.getProductsList()) {
            Product modelProduct = new Product(product.getId(), product.getName(), product.getPrice(), product.getQuantity());
            responseProducts.add(modelProduct);
        }
        return responseProducts;

    }

    public Product updateQuantity(Integer id, Integer quantity) {

        ProductOuterClass.UpdateQuantityRequest updateQuantityRequest = ProductOuterClass.UpdateQuantityRequest.newBuilder().setId(id).setQuantity(quantity).build();
        ProductOuterClass.UpdateQuantityResponse updateQuantityResponse = productServiceBlockingStub.updateQuantity(updateQuantityRequest);

        ProductOuterClass.Product product = updateQuantityResponse.getProduct();
        return new Product(product.getId(), product.getName(), product.getPrice(), product.getQuantity());

    }

    public void decreaseQuantity(Integer id) {
        ProductOuterClass.DecreaseQuantityRequest decreaseQuantityRequest = ProductOuterClass.DecreaseQuantityRequest.newBuilder().setId(id).build();
         productServiceBlockingStub.decreaseQuantity(decreaseQuantityRequest);

    }



}
