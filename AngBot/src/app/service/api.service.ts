import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Login } from '../models/login';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

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

    return this.http.post<Login>(this.actionUrl + 'persona', persona, httpOptions);
  }


  private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error); // log to console instead
      return of(result as T);
    };
  }

  public setActionUrl(url: string) {
    this.actionUrl = url;
  }

}
