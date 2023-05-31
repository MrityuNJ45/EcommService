package repository

import (
	product "inventory-service/models"
	"errors"
	"fmt"

	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

type Database struct {
	DB *gorm.DB
}

func (d *Database)Connect() (error) {
	var err error
	dsn := "host=localhost user=username dbname=database password=dbpassword port=5432 sslmode=disable"
	d.DB, err = gorm.Open(postgres.Open(dsn), &gorm.Config{})
	
	if err != nil {
		return errors.New("Failed to connect to DB")
	}
	
	err = d.DB.AutoMigrate(&product.Product{})
	

	fmt.Println("Database connected");
	return nil
}

