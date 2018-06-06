package main

import (
	"encoding/json"
	"fmt"
	"net/http"
	"os/exec"
	"github.com/gorilla/mux"
)

type API struct {
	Key string `json:"key"`
}

func main()  {

	ruta := mux.NewRouter()
	ruta.HandleFunc("/key", GetKey).Methods("GET")

	http.Handle("/", ruta)
	http.ListenAndServe(":8085", nil)
}

func GetKey(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	enableCors(&w)
	cmd := "gcloud auth print-access-token"
	out, err := exec.Command("sh","-c",cmd).Output()

	if err != nil {
		fmt.Println("Error")
	} else {
		str := fmt.Sprintf("%s", out[:len(out) - 1])
		fmt.Print(str)
		message :=API{ Key : str } 
		json.NewEncoder(w).Encode(message)
	}

}

func enableCors(w *http.ResponseWriter) {
	(*w).Header().Set("Access-Control-Allow-Origin", "*")
}