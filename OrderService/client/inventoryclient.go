package client

import (
	
	"fmt"
	"log"
	"google.golang.org/grpc"
)



func GetInventoryClientConn() (*grpc.ClientConn) {
	fmt.Println("Starting gRPC client application...")

	conn, err := grpc.Dial("localhost:8080", grpc.WithInsecure(), grpc.WithBlock())
	if err != nil {
	 log.Fatalf("Failed to dial: %v", err)
	 return nil
	}
	defer conn.Close()
	return conn
}