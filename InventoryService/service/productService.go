package service

import (
	"context"
	pr "ecommerceApp/inventoryService/models"
	pb "ecommerceApp/inventoryService/proto/product"
	"fmt"

	"gorm.io/gorm"
)

type ProductService struct {
	db *gorm.DB
	pb.UnimplementedProductServiceServer
}


func NewProductService(db *gorm.DB) *ProductService {
	return &ProductService{
		db: db,
		UnimplementedProductServiceServer: pb.UnimplementedProductServiceServer{},
	}
}

func (c *ProductService) CreateProduct(ctx context.Context,req *pb.CreateProductRequest) (*pb.CreateProductResponse, error){
	
    fmt.Println("Received request name : " + req.Name )
	product := pr.Product{
		Name:  req.Name,
		Price: req.Price,
		Quantity: int(req.Quantity),
	}

	

	if result := c.db.Create(&product); result.Error != nil {
		fmt.Println("Some error occured ", result.Error);
		return nil, result.Error
	}
    
	 return &pb.CreateProductResponse{
		Id: product.ID,
	}, nil
	
}