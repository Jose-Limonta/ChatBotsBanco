import { ApiService } from './api.service';
import { GapiService } from './gapi.service';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

import { ApiAiClient } from 'api-ai-javascript';

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Message } from './../models/mensaje';
import 'rxjs/add/operator/scan';

@Injectable()
export class DialogService {

  conversation = new BehaviorSubject<Message[]>([]);

  constructor (private client: GapiService, private openPay: ApiService) {}

  // Sends and receives messages via DialogFlow
  converse(msg: string) {
    const userMessage = new Message(msg, 'user', new  Date().toLocaleString(), '');
    this.update(userMessage);

    return this.client.textRequest(msg)
                      .subscribe(
                        // tslint:disable-next-line:no-shadowed-variable
                        data => {
                          const text = data.queryResult.fulfillmentMessages[0].text.text[0];
                          const intent = data.queryResult.intent.displayName;
                          const botMessage = new Message(text, 'bot', new  Date().toLocaleString(), intent);
                          console.log(botMessage);
                          this.bankAction(intent);
                          this.update(botMessage);
                        },
                        error => console.log('Se presentó un error', error)
                      );
  }

  // Adds message to source
  update(msg: Message) {
    this.conversation.next([msg]);
  }

  bankAction (intent: string){
    if (intent === 'RevisarCuenta'){
      this.openPay.getResource('cards/a5g8bb6ev5bzva6jw1fu')
                  .subscribe(
                    data => {
                      const text = JSON.stringify(data);
                      const botMessage = new Message(text, 'bot', new  Date().toLocaleString(), '');
                      this.update(botMessage);
                    },
                    error => console.log('Se presentó un error', error)
                  );
    } else {
      console.log('Intent erroneo : '+ intent);
    }
  }

}
