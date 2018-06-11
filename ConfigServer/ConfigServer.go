package main

import (
	"encoding/json"
	"fmt"
	"net/http"
	"os/exec"
	"github.com/gorilla/mux"
	"github.com/patrickmn/go-cache"
	"time"
)

type API struct {
	Key string `json:"key"`
}

var c = cache.New(5*time.Minute, 10*time.Minute)

func main()  {
	
	ruta := mux.NewRouter()
	ruta.HandleFunc("/key", GetKey).Methods("GET")

	http.Handle("/", ruta)
	http.ListenAndServe(":8085", nil)
}

func GetKey(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	enableCors(&w)

	message :=API{ Key : generateKey() }
	json.NewEncoder(w).Encode(message)

}

func enableCors(w *http.ResponseWriter) {
	(*w).Header().Set("Access-Control-Allow-Origin", "*")
}

func generateKey() string {

	foo, found := c.Get("data")
	if found {
		fmt.Println("-----Found in Cache-----")
		fmt.Println(foo)
		fmt.Println("--------------")
		return foo.(string)
	}else {
		cmd := "gcloud auth print-access-token"
		out, err := exec.Command("sh","-c",cmd).Output()

		if err != nil {
			fmt.Println("Error")
		} else {
			str := fmt.Sprintf("%s", out[:len(out) - 1])
			c.Set("data", str, cache.DefaultExpiration)
			return str
		}
		return ""
	}

}