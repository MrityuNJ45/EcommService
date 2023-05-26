package models

type ProductRequest struct {
	Name     string  `json:"name"`
	Price    float64 `json:"price"`
	Quantity int     `json:"quantity"`
}

type ProductCreateResponse struct {
	ID       int32   `json:"id"`
}
