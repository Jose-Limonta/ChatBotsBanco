import { Observable } from 'rxjs';
import { QueryInput, QueryParams, Key, GoogleData } from './../models/googleData';
import { ApiService } from './api.service';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { GoogleResponse } from '../models/googleResponse';

@Injectable()
export class GapiService {

  private actionUrl: string;
  private httpOptions: {};

  constructor(private http: HttpClient) {
    // tslint:disable-next-line:max-line-length
    this.actionUrl = 'https://dialogflow.googleapis.com/v2/projects/angbot-ab821/agent/sessions/fbf255c2-16fe-9e7f-123f-addbb1cebab1:detectIntent';

    this.getGcloudKey().subscribe(
      data => {
        console.log('Llave : ' + data);
        this.httpOptions = {
          headers: new HttpHeaders({
            'Authorization': 'Bearer ' + data.key,
            'Content-Type': 'application/json'
          })
        };
      },
      error => {
        console.log('------ERROR---------');
        console.log(error);
      }
    );
  }

  textRequest(msg: string): Observable<GoogleResponse> {

    const req: GoogleData = {
      queryInput: { text: { text: msg, languageCode: navigator.language } },
      queryParams: { timeZone: Intl.DateTimeFormat().resolvedOptions().timeZone }
    };
    // console.log('---------Google Request Data--------');
    // console.log(req);
    // console.log(this.httpOptions.headers);
    return this.http.post<GoogleResponse>(this.actionUrl, req, this.httpOptions);

  }

  private getGcloudKey(): Observable<Key> {
    return this.http.get<Key>('http://localhost:8085/key');
  }
}
