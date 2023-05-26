package main

import (
	"log"
	"net/http"

	"apigateway/controller"
	"apigateway/proto/product" 
	"github.com/gorilla/mux"
	"google.golang.org/grpc"
)

func main() {
	
	conn, err := grpc.Dial("localhost:8080", grpc.WithInsecure())
	if err != nil {
		log.Fatalf("Failed to connect to gRPC server: %v", err)
	}
	defer conn.Close()

	
	productService := product.NewProductServiceClient(conn)

	
	productController := &controllers.ProductController{
		ProductService: productService,
	}

	
	router := mux.NewRouter()

	
	router.HandleFunc("/products", productController.CreateProduct).Methods(http.MethodPost)
	router.HandleFunc("/products/{id}", productController.GetProduct).Methods(http.MethodGet)
	router.HandleFunc("/products", productController.GetAllProducts).Methods(http.MethodGet)

	
	log.Println("Server started on port 8082")
	log.Fatal(http.ListenAndServe(":8082", router))
}
