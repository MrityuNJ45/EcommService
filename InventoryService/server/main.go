package main

import (
	"context"
	pb "ecommerceApp/inventoryService/proto/product"
	"fmt"
	"log"
	"net"

	"google.golang.org/grpc"
)

type myGrpcServer struct {
	pb.UnimplementedProductServiceServer
}

func (m *myGrpcServer) CreateProduct(ctx context.Context, req *pb.CreateProductRequest) (*pb.CreateProductResponse, error) {

	fmt.Println("Product Received name : " + req.Name)
	return &pb.CreateProductResponse{
		Id: 1,
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
