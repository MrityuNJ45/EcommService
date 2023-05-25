package service

import (
	pr "ecommerceApp/inventoryService/models"
	
	"fmt"

	"gorm.io/gorm"
)

type ProductService struct {
	db *gorm.DB
}


func NewProductService(db *gorm.DB) *ProductService {
	return &ProductService{
		db: db,
	}
}

func (c *ProductService) CreateProduct(productReq pr.Product) (identity int32){
	

	if result := c.db.Create(&productReq); result.Error != nil {
		fmt.Println("Some error occured ", result.Error.Error());
		return 0;
	}
    
	return productReq.ID;
	
}