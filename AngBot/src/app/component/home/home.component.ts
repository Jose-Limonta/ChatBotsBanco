import { DialogService } from './../../service/dialog.service';
import { Observable ,  BehaviorSubject } from 'rxjs';
import { ApiService } from './../../service/api.service';
import { Component, OnInit } from '@angular/core';
import { Message } from './../../models/mensaje';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  messages: Observable<Message[]>;
  formValue = '';

  constructor(private api: ApiService, private chat: DialogService) { }

  ngOnInit() {
    this.messages = this.chat.conversation.asObservable();
                    // .map((acc, val) => acc.concat(val) );
                    // TODO check map
  }

  sendMessage() {

    if (this.formValue !== '') {
      this.chat.converse(this.formValue);
    }
    this.formValue = '';
  }

}
