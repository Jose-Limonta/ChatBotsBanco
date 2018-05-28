import { Component, OnInit, Input } from '@angular/core';
import { Message } from '../../models/message';

@Component({
  selector: 'app-message-list',
  templateUrl: './message-list.component.html',
  styleUrls: ['./message-list.component.scss']
})
export class MessageListComponent implements OnInit {

  // tslint:disable-next-line:no-input-rename
  @Input('message')
  private message: Message;

  // tslint:disable-next-line:no-input-rename
  @Input('messages')
  private messages: Message[];

  constructor() { }

  ngOnInit() {
}

}
