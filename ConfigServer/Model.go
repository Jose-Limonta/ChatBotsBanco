package dialogApi


type Message struct {
	Text   string `json:"Text"`
	Date   string `json:"Date"`
	Origin string `json:"Origin"`
	Intent string `json:"Intent"`
}
