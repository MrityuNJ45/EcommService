package repository

import (
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
	product "ecommerceApp/inventoryService/models"
)



func SetupDB() (*gorm.DB, error) {
	dsn := "host=localhost user=mrityunjay.s_ftc dbname=morty port=5432 sslmode=disable"
	db, err := gorm.Open(postgres.Open(dsn), &gorm.Config{})
	if err != nil {
		return nil, err
	}

	
	err = db.AutoMigrate(&product.Product{})
	if err != nil {
		return nil, err
	}

	return db, nil
}