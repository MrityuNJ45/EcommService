package service

import (
	"context"
	"errors"
	"fmt"
	pr "inventory-service/models"
	pb "inventory-service/proto/product"

	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	"gorm.io/gorm"
)

type ProductService struct {
	db *gorm.DB
	pb.UnimplementedProductServiceServer
}

func NewProductService(db *gorm.DB) *ProductService {
	return &ProductService{
		db:                                db,
		UnimplementedProductServiceServer: pb.UnimplementedProductServiceServer{},
	}
}

func (c *ProductService) CreateProduct(ctx context.Context, req *pb.CreateProductRequest) (*pb.CreateProductResponse, error) {

	fmt.Println("Received request name : " + req.Name)
	product := pr.Product{
		Name:     req.Name,
		Price:    req.Price,
		Quantity: int(req.Quantity),
	}

	if result := c.db.Create(&product); result.Error != nil {
		fmt.Println("Some error occured ", result.Error)
		return nil, result.Error
	}

	return &pb.CreateProductResponse{
		Id:       product.ID,
		Name:     product.Name,
		Price:    product.Price,
		Quantity: int32(product.Quantity),
	}, nil

}

func (c *ProductService) GetProduct(ctx context.Context, req *pb.GetProductRequest) (*pb.GetProductResponse, error) {
	fmt.Println("Received request for product ID:", req.Id)

	var product pr.Product
	if result := c.db.First(&product, req.Id); result.Error != nil {
		if errors.Is(result.Error, gorm.ErrRecordNotFound) {
			return nil, status.Errorf(codes.NotFound, "Product not found")
		} else {
			fmt.Println("Some error occurred:", result.Error)
			return nil, status.Errorf(codes.Internal, "Internal server error")
		}
	}

	response := &pb.GetProductResponse{
		Id:       product.ID,
		Name:     product.Name,
		Price:    product.Price,
		Quantity: int32(product.Quantity),
	}

	return response, nil
}

func (c *ProductService) GetAllProducts(ctx context.Context, req *pb.GetAllProductsRequest) (*pb.GetAllProductsResponse, error) {
	fmt.Println("Received request for all products")

	var products []pr.Product
	if result := c.db.Find(&products); result.Error != nil {
		fmt.Println("Some error occurred:", result.Error)
		return nil, status.Errorf(codes.Internal, "Internal server error")
	}

	response := &pb.GetAllProductsResponse{}
	for _, product := range products {
		pbProduct := &pb.Product{
			Id:       product.ID,
			Name:     product.Name,
			Price:    product.Price,
			Quantity: int32(product.Quantity),
		}
		response.Products = append(response.Products, pbProduct)
	}

	return response, nil
}

func (c *ProductService) UpdateQuantity(ctx context.Context, req *pb.UpdateQuantityRequest) (*pb.UpdateQuantityResponse, error) {

	var product pr.Product
	if result := c.db.First(&product, req.Id); result.Error != nil {
		fmt.Println("Failed to retrieve product from the database:", result.Error)
		return nil, result.Error
	}

	product.Quantity += int(req.Quantity)

	if result := c.db.Save(&product); result.Error != nil {
		fmt.Println("Failed to update product quantity:", result.Error)
		return nil, result.Error
	}

	return &pb.UpdateQuantityResponse{
		Product: &pb.Product{
			Id:       product.ID,
			Name:     product.Name,
			Price:    product.Price,
			Quantity: int32(product.Quantity)},
	}, nil
}

func (c *ProductService) DecreaseQuantity(ctx context.Context, req *pb.DecreaseQuantityRequest) (*pb.DecreaseQuantityResponse, error) {

	var product pr.Product
	if result := c.db.First(&product, req.Id); result.Error != nil {
		fmt.Println("Failed to retrieve product from the database:", result.Error)
		return nil, result.Error
	}

	product.Quantity -= 1

	if result := c.db.Save(&product); result.Error != nil {
		fmt.Println("Failed to decrease product quantity:", result.Error)
		return nil, result.Error
	}

	return &pb.DecreaseQuantityResponse{
		
	}, nil
}
