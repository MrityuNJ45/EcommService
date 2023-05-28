package controllers

import (
	"apigateway/models"
	pb "apigateway/proto/product"
	"apigateway/proto/product/mocks"
	"bytes"
	"encoding/json"
	"github.com/go-playground/assert/v2"
	"github.com/stretchr/testify/mock"
	"net/http"
	"net/http/httptest"
	"testing"
)

func TestCreateProduct(t *testing.T) {

	productService := &mocks.ProductServiceClient{}

	productController := &ProductController{
		ProductService: productService,
	}

	requestBody := models.ProductRequest{
		Name:     "Test Product",
		Price:    9.99,
		Quantity: 10,
	}
	requestBodyJSON, _ := json.Marshal(requestBody)
	request, _ := http.NewRequest("POST", "/products", bytes.NewBuffer(requestBodyJSON))

	responseRecorder := httptest.NewRecorder()

	// Mock the CreateProduct method
	expectedResponse := &pb.CreateProductResponse{
		Id: 1,
	}
	productService.On("CreateProduct", mock.Anything, mock.AnythingOfType("*pb.CreateProductRequest")).Return(expectedResponse, nil)

	productController.CreateProduct(responseRecorder, request)

	assert.Equal(t, http.StatusOK, responseRecorder.Code)

	expectedResponseBody := `{"id":1}`
	assert.Equal(t, expectedResponseBody, responseRecorder.Body.String())

	productService.AssertCalled(t, "CreateProduct", mock.Anything, mock.AnythingOfType("*pb.CreateProductRequest"))
}
