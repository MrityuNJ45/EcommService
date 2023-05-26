package models


type Product struct {
	ID       int32 `gorm:"primaryKey" json:"id"`
	Name     string `gorm:"not null;unique" json:"name"`
	Price    float64 `json:"price"`
	Quantity int    `json:"quantity"`
}

