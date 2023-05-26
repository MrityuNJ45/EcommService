package controllers

import (
	"encoding/json"
	"net/http"
	"strconv"

	// "github.com/gorilla/mux"
	"apigateway/models"
	pb "apigateway/proto/product" 

	"github.com/gorilla/mux"
)


type ProductController struct {
    ProductService pb.ProductServiceClient 
}


func (pc *ProductController) CreateProduct(w http.ResponseWriter, r *http.Request) {
    
    var request models.ProductRequest
    err := json.NewDecoder(r.Body).Decode(&request)
    if err != nil {
        http.Error(w, "Invalid request body", http.StatusBadRequest)
        return
    }

	proReq := pb.CreateProductRequest{
		Name: request.Name,
		Price: request.Price,
		Quantity: int32(request.Quantity),
	}
    
    response, err := pc.ProductService.CreateProduct(r.Context(), &proReq)
    if err != nil {
        http.Error(w, "Failed to create product", http.StatusInternalServerError)
        return
    }

    
    jsonResponse, err := json.Marshal(response)
    if err != nil {
        http.Error(w, "Failed to marshal response", http.StatusInternalServerError)
        return
    }

   
    w.Header().Set("Content-Type", "application/json")
    w.WriteHeader(http.StatusOK)
    w.Write(jsonResponse)
}


func (pc *ProductController) GetProduct(w http.ResponseWriter, r *http.Request) {
    
    vars := mux.Vars(r)
    id := vars["id"]

	idn, err := strconv.Atoi(id)
   
    request := &pb.GetProductRequest{
        Id: int32(idn),
    }

   
    response, err := pc.ProductService.GetProduct(r.Context(), request)
    if err != nil {
        http.Error(w, "Failed to get product", http.StatusInternalServerError)
        return
    }

    
    jsonResponse, err := json.Marshal(response)
    if err != nil {
        http.Error(w, "Failed to marshal response", http.StatusInternalServerError)
        return
    }

   
    w.Header().Set("Content-Type", "application/json")
    w.WriteHeader(http.StatusOK)
    w.Write(jsonResponse)
}


func (pc *ProductController) GetAllProducts(w http.ResponseWriter, r *http.Request) {
  
    request := &pb.GetAllProductsRequest{}

   
    response, err := pc.ProductService.GetAllProducts(r.Context(), request)
    if err != nil {
        http.Error(w, "Failed to get products", http.StatusInternalServerError)
        return
    }

    
    jsonResponse, err := json.Marshal(response)
    if err != nil {
        http.Error(w, "Failed to marshal response", http.StatusInternalServerError)
        return
    }

    
    w.Header().Set("Content-Type", "application/json")
    w.WriteHeader(http.StatusOK)
    w.Write(jsonResponse)
}
