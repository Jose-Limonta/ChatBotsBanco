import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpErrorResponse, HttpRequest, HttpHeaders } from '@angular/common/http';
import { ErrorObservable } from 'rxjs/observable/ErrorObservable';
import { Login } from '../models/login';
import { Observable } from 'rxjs/Observable';
import { catchError } from 'rxjs/operators/catchError';

const httpOptions = {
  headers: new HttpHeaders({
    'Authorization': 'Basic cGVwZTpwZXBl',
    'Content-Type': 'application/json'
  })
};

@Injectable()
export class ApiService {

  private actionUrl: string;

  constructor(private http: HttpClient) {
    this.actionUrl = 'http://localhost:8081/v1/';
  }

  getResource (uri: string) {
    return this.http.get(this.actionUrl + uri, httpOptions);
  }

  postPersona (persona: Login): Observable<Login>  {

    return this.http.post<Login>(this.actionUrl + 'persona', persona, httpOptions)
                .pipe(
                  catchError( this.handleError  )
                );
  }


  public handleError(error: HttpErrorResponse) {
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
