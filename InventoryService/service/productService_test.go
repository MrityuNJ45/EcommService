package service

import (
	"context"
	pr "ecommerceApp/inventoryService/models"

	"testing"

	pb "ecommerceApp/inventoryService/proto/product"

	"github.com/stretchr/testify/assert"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"

	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
)


func TestNewProductService(t *testing.T) {
	db, err := gorm.Open(sqlite.Open(":memory:"), &gorm.Config{})
	if err != nil {
		t.Fatalf("Failed to open database connection: %v", err)
	}

	t.Run("NewProductService should return a valid ProductService instance", func(t *testing.T) {
		productService := NewProductService(db)

		assert.NotNil(t, productService, "ProductService instance is nil")
		assert.Equal(t, db, productService.db, "Unexpected database connection")
		assert.NotNil(t, productService.UnimplementedProductServiceServer, "UnimplementedProductServiceServer is nil")
	})

	
}


func TestCreateProduct(t *testing.T) {
	db, err := gorm.Open(sqlite.Open(":memory:"), &gorm.Config{})
	if err != nil {
		t.Fatalf("Failed to open database connection: %v", err)
	}
	 db.AutoMigrate(&pr.Product{})

	productService := &ProductService{
		db:                                db,
		UnimplementedProductServiceServer: pb.UnimplementedProductServiceServer{},
	}

	t.Run("CreateProduct method should successfully create a product", func(t *testing.T) {
		request := &pb.CreateProductRequest{
			Name:     "Test Product",
			Price:    9.99,
			Quantity: 10,
		}

		response, err := productService.CreateProduct(context.Background(), request)

		assert.NoError(t, err, "Unexpected error")
		assert.NotNil(t, response, "Response is nil")
		assert.NotEqual(t, int32(0), response.Id, "Product ID should not be zero")
	})

	

}


func TestGetProduct(t *testing.T) {
	db, err := gorm.Open(sqlite.Open(":memory:"), &gorm.Config{})
	if err != nil {
		t.Fatalf("Failed to open database connection: %v", err)
	}
	db.AutoMigrate(&pr.Product{})

	productService := &ProductService{
		db: db,
		UnimplementedProductServiceServer: pb.UnimplementedProductServiceServer{},
	}

	t.Run("GetProduct method should return the product details for a valid product ID", func(t *testing.T) {
		
		product := &pr.Product{
			Name:     "Test Product",
			Price:    9.99,
			Quantity: 10,
		}
		db.Create(product)

		request := &pb.GetProductRequest{
			Id: product.ID,
		}

		response, err := productService.GetProduct(context.Background(), request)

		assert.NoError(t, err, "Unexpected error")
		assert.NotNil(t, response, "Response is nil")
		assert.Equal(t, "Test Product", response.Name, "Unexpected product name")
		assert.Equal(t, float64(9.99), response.Price, "Unexpected product price")
		assert.Equal(t, int32(10), response.Quantity, "Unexpected product quantity")
	})

	t.Run("GetProduct method should return NotFound error for an invalid product ID", func(t *testing.T) {
		request := &pb.GetProductRequest{
			Id: 9999, // Non-existent product ID
		}

		response, err := productService.GetProduct(context.Background(), request)

		assert.Error(t, err, "Expected error")
		assert.Nil(t, response, "Response should be nil")
		st, _ := status.FromError(err)
		assert.Equal(t, codes.NotFound, st.Code(), "Unexpected error code")
		assert.Equal(t, "Product not found", st.Message(), "Unexpected error message")
	})

	
}


func TestGetAllProducts(t *testing.T) {
	db, err := gorm.Open(sqlite.Open(":memory:"), &gorm.Config{})
	if err != nil {
		t.Fatalf("Failed to open database connection: %v", err)
	}
	db.AutoMigrate(&pr.Product{})

	productService := &ProductService{
		db:                                db,
		UnimplementedProductServiceServer: pb.UnimplementedProductServiceServer{},
	}

	t.Run("GetAllProducts method should return all products", func(t *testing.T) {
		
		products := []pr.Product{
			{ID: 1, Name: "Product 1", Price: 9.99, Quantity: 10},
			{ID: 2, Name: "Product 2", Price: 19.99, Quantity: 5},
			{ID: 3, Name: "Product 3", Price: 29.99, Quantity: 3},
		}
		for _, product := range products {
			db.Create(&product)
		}

		
		request := &pb.GetAllProductsRequest{}
		response, err := productService.GetAllProducts(context.Background(), request)

		assert.NoError(t, err, "Unexpected error")
		assert.NotNil(t, response, "Response is nil")
		assert.Equal(t, 3, len(response.Products), "Incorrect number of products returned")

		
		expectedProducts := []*pb.Product{
			{Id: 1, Name: "Product 1", Price: 9.99, Quantity: 10},
			{Id: 2, Name: "Product 2", Price: 19.99, Quantity: 5},
			{Id: 3, Name: "Product 3", Price: 29.99, Quantity: 3},
		}
		for i, expectedProduct := range expectedProducts {
			actualProduct := response.Products[i]
			assert.Equal(t, expectedProduct, actualProduct, "Incorrect product data")
		}
	})
}


