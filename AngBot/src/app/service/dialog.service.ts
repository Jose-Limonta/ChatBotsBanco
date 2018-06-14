import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Message } from '../models/mensaje';
import { of, Observable } from 'rxjs';
import { BehaviorSubject} from 'rxjs-compat';


const httpOptions = {
  headers: new HttpHeaders({
    'Access-Control-Allow-Origin': '*',
    'Content-Type': 'application/json'
  })
};

@Injectable({
  providedIn: 'root'
})
export class DialogService {
  conversation = new BehaviorSubject<Message[]>([]);

  constructor(private http: HttpClient) {}

  update(msg: Message) {
    this.conversation.next([msg]);
  }


  converse(msg: string) {
    const userMessage = new Message(msg, 'user', new  Date().toLocaleString(), 'tbd');
    this.update(userMessage);

    this.postMessage(userMessage).subscribe(
      data => {
        console.log(data);
        const botMessage = new Message(data.Text, data.Origin, data.Date , data.Intent);
        this.update(botMessage);
      },
      error => console.log('Se ha presentado un error inesperado')
    );
  }

  postMessage (msg: Message): Observable<Message> {
    const actionUrl = 'http://localhost:8085/message';
    return this.http.post<Message>(actionUrl, msg);
  }

}
