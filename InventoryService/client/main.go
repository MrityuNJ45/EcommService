package main

import (
	"context"
	pb "ecommerceApp/inventoryService/proto/product"
	"fmt"
	"log"

	"google.golang.org/grpc"
)

func main() {
	fmt.Println("Starting gRPC client application...")

	conn, err := grpc.Dial("localhost:8080", grpc.WithInsecure(), grpc.WithBlock())
	if err != nil {
	 log.Fatalf("Failed to dial: %v", err)
	 return
	}
	defer conn.Close()
	client := pb.NewProductServiceClient(conn)

	request := &pb.GetProductRequest{
		Id: 1,
	}

	response, err := client.GetProduct(context.Background(), request)

	if err != nil {
		log.Fatalf("Failed to greet: %v", err)
		return
	   }
	  
	   fmt.Println("Response from server: " + response.String())

}