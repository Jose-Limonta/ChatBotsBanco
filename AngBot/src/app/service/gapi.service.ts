import { QueryInput, QueryParams } from './../models/googleData';
import { ApiService } from './api.service';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { catchError } from 'rxjs/operators/catchError';
import { GoogleData } from '../models/googleData';
import { ErrorObservable } from 'rxjs/observable/ErrorObservable';

const httpOptions = {
  headers: new HttpHeaders({
    // tslint:disable-next-line:max-line-length
    'Authorization': 'Bearer ya29.c.ElrMBZNyfBNI8PkWdw5k-8mJnfnGda2zuCCIAlKAB16RPP2oL2Mj-1L3hIG_eG942OxOQLyeMS2NKjNIuAC5gk5HPmp1Qw23rd5Dz8A6tV65aV0lf8O0yLjLHTA',
    'Content-Type': 'application/json'
  })
};

@Injectable()
export class GapiService{

  private actionUrl: string;

  constructor(private http: HttpClient) {
    // tslint:disable-next-line:max-line-length
    this.actionUrl = 'https://dialogflow.googleapis.com/v2/projects/angbot-ab821/agent/sessions/955cf259-d479-453e-a50d-ac96f9bc00cf:detectIntent';
  }

  textRequest(msg: string) {
    const req: GoogleData = {queryInput: {text: { text: '', languageCode: ''}}, queryParams:{timeZone: ''} };
    req.queryInput.text.text = msg;
    req.queryInput.text.languageCode = navigator.language;
    req.queryParams.timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
    console.log(req);
    return this.http.post<GoogleData>(this.actionUrl, req, httpOptions)
                .pipe(
                  catchError( this.handleError  )
                );
  }

  public handleError(error: HttpErrorResponse){
    if (error.error instanceof ErrorEvent) {
      console.error('An error occurred:', error.error.message);
    } else {
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
    }
    return new ErrorObservable(
      'Something bad happened; please try again later.');
  }

}
