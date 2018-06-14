package main

import (
	"encoding/json"
	"log"
	"net/http"

	"fmt"
	"time"

	"github.com/gorilla/mux"
	df "github.com/meinside/dialogflow-go"
)

type Message struct {
	Text   string `json:"Text"`
	Date   string `json:"Date"`
	Origin string `json:"Origin"`
	Intent string `json:"Intent"`
}

// CORSRouterDecorator applies CORS headers to a mux.Router
type CORSRouterDecorator struct {
    R *mux.Router
}

func main() {

	ruta := mux.NewRouter()
	ruta.HandleFunc("/message", MessageHandler)

	http.Handle("/", &CORSRouterDecorator{ruta})
	http.ListenAndServe(":8085", nil)
}

func MessageHandler(w http.ResponseWriter, r *http.Request) {

	decoder := json.NewDecoder(r.Body)
	var message Message
	err := decoder.Decode(&message)
	if err != nil {
		panic(err)
	}
	defer r.Body.Close()
	log.Println(message)

	json.NewEncoder(w).Encode(googleResponse(message.Text))
}

func googleResponse(message string) Message {
	token := "1004f175588041c7ac826aff1bc74f94"
	sessionId := "fbf255c2-16fe-9e7f-123f-addbb1cebab1"

	client := df.NewClient(token)

	if response, err := client.QueryText(df.QueryRequest{
		Query:     []string{message},
		SessionId: sessionId,
		Language:  df.Spanish,
	}); err == nil {
		fmt.Printf(">>> response = %+v\n", response)
		return Message{Text: response.Result.Fulfillment.Speech, Origin: "bot", Date: getDateForm(), Intent: response.Result.Metadata.IntentName}
	} else {
		fmt.Printf("*** error: %s\n", err)
		return Message{Text: "", Origin: "Bot", Date: getDateForm(), Intent: ""}
	}
}

func getDateForm() string {
	t := time.Now()
	return fmt.Sprintf("%d/%d/%d %d:%d:%d", t.Day(), t.Month(), t.Year(), t.Hour(), t.Minute(), t.Second())

}

func (c *CORSRouterDecorator) ServeHTTP(rw http.ResponseWriter, req *http.Request) {
    if origin := req.Header.Get("Origin"); origin != "" {
        rw.Header().Set("Access-Control-Allow-Origin", origin)
        rw.Header().Set("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE")
        rw.Header().Set("Access-Control-Allow-Headers", "Accept, Accept-Language, Content-Type, YourOwnHeader")
    }
    // Stop here if its Preflighted OPTIONS request
    if req.Method == "OPTIONS" {
        return
    }

    c.R.ServeHTTP(rw, req)
}
