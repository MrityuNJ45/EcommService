package clients

import (
	pb "apigateway/proto/product"
	
	"log"

	"google.golang.org/grpc"
)

var (
	ProductClient pb.ProductServiceClient
)

func InitialiseProductServiceClient() {

	conn, err := grpc.Dial("localhost:8080", grpc.WithInsecure(), grpc.WithBlock())
	if err != nil {
	 log.Fatalf("Failed to dial: %v", err)
	 return
	}

	defer conn.Close()
	ProductClient = pb.NewProductServiceClient(conn)

}



