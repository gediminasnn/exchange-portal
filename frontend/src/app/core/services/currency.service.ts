import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import Currency from '../models/currency.model';

@Injectable({
  providedIn: 'root'
})
export class CurrencyService {

  private apiUrl = 'http://localhost:8080/api/currencies';

  constructor(private http: HttpClient) { }

  fetchCurrencyWithExchangeRateHistory(id: number, fromDate: String | null, toDate: String | null): Observable<Currency> {
    return this.http.get<Currency>(`${this.apiUrl}/${id}/exchange-rates${fromDate !== null && toDate !== null ? `?fromDate=${fromDate}&toDate=${toDate}` : ''}`);
  }
}
