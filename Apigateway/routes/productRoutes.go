package routes

import (
	"apigateway/controller"
	"log"
	"net/http"

	"github.com/gorilla/mux"
)


func InitializeRouter() {
	router := mux.NewRouter()

	// Define routes
	router.HandleFunc("/products", controller.CreateProduct).Methods("POST")
	log.Fatal(http.ListenAndServe(":8082", router))

}