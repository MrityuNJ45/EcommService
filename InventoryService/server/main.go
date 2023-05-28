package main

import (
	
	

	pb "inventory-service/proto/product"
	repo "inventory-service/repository"
	srv "inventory-service/service"

	"fmt"
	"log"
	"net"

	"google.golang.org/grpc"
	
)





func main() {

	fmt.Println("Starting server on port :8888")

	lis, err := net.Listen("tcp", ":8888")
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
