package main

import (
	"context"
	pr "ecommerceApp/inventoryService/models"

	
	pb "ecommerceApp/inventoryService/proto/product"
	srv "ecommerceApp/inventoryService/service"
	repo "ecommerceApp/inventoryService/repository"
	"fmt"
	"log"
	"net"

	"google.golang.org/grpc"
)

type myGrpcServer struct {
	pb.UnimplementedProductServiceServer
	
}

func (m *myGrpcServer) CreateProduct(ctx context.Context, req *pb.CreateProductRequest) (*pb.CreateProductResponse, error) {

	db, _ := repo.SetupDB()
	productService := srv.NewProductService(db);
	product := pr.Product{
		Name:  req.Name,
		Price: req.Price,
		Quantity: int(req.Quanity),
	}

	id := productService.CreateProduct(product)
	fmt.Println("Product Received name : " + req.Name)
	return &pb.CreateProductResponse{
		Id: id,
	}, nil

}

func main() {

	fmt.Println("Starting server on port :8080")

	lis, err := net.Listen("tcp", ":8080")
	if err != nil {
		log.Fatalf("Failed to listen: %v", err)
		return
	}

	grpcServer := grpc.NewServer()

	pb.RegisterProductServiceServer(grpcServer, &myGrpcServer{})

	err = grpcServer.Serve(lis)
	if err != nil {
		log.Fatalf("Failed to serve: %v", err)
		return
	}

	defer lis.Close()
	defer grpcServer.Stop()

}
