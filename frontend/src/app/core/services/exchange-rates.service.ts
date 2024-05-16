import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import ExchangeRates from '../models/exchange-rates.model';

@Injectable({
  providedIn: 'root'
})
export class ExchangeRatesService {

  private apiUrl = 'http://localhost:8080/api/exchange-rates';

  constructor(private http: HttpClient) { }

  fetchAll(): Observable<ExchangeRates[]> {
    const rates = this.http.get<ExchangeRates[]>(this.apiUrl);
    console.log(rates);
    return rates;
  }
}
