import { Component, OnInit, Input } from '@angular/core';
import { Message } from '../../models/message';

@Component({
  selector: 'app-message-form',
  templateUrl: './message-form.component.html',
  styleUrls: ['./message-form.component.scss']
})
export class MessageFormComponent implements OnInit {

  // tslint:disable-next-line:no-input-rename
  @Input('message')
  private message: Message;

  // tslint:disable-next-line:no-input-rename
  @Input('messages')
  private messages: Message[];

  constructor() { }

  ngOnInit() {
  }

  public sendMessage(): void {
    this.message.timestamp = new Date();
    this.messages.push(this.message);

    this.message = new Message('', 'assets/images/user.png');
}

}
