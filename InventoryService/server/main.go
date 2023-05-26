package main

import (
	
	

	pb "ecommerceApp/inventoryService/proto/product"
	repo "ecommerceApp/inventoryService/repository"
	srv "ecommerceApp/inventoryService/service"

	"fmt"
	"log"
	"net"

	"google.golang.org/grpc"
	
)





func main() {

	fmt.Println("Starting server on port :8080")

	lis, err := net.Listen("tcp", ":8080")
	if err != nil {
		log.Fatalf("Failed to listen: %v", err)
		return
	}

	db := &repo.Database{};
	db.Connect();
	
    myGrpcServer := srv.NewProductService(db.DB)

	grpcServer := grpc.NewServer()

	pb.RegisterProductServiceServer(grpcServer, myGrpcServer)

	err = grpcServer.Serve(lis)
	if err != nil {
		log.Fatalf("Failed to serve: %v", err)
		return
	}

	defer lis.Close()
	defer grpcServer.Stop()

}
