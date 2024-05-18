import { Routes } from '@angular/router';
import { CurrencyHistoryPageComponent } from './features/modules/currency/pages/currency-history-page/currency-history-page.component';
import { ExchangeRatesPageComponent } from './features/modules/exchange-rate/pages/exchange-rates-page/exchange-rates-page.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'exchange-rates',
    pathMatch: 'full'
  },
  {
    path: 'exchange-rates',
    component: ExchangeRatesPageComponent,
    title: 'Exchange rates'
  },
  {
    path: 'currency/:id',
    component: CurrencyHistoryPageComponent,
    title: 'Currency history chart'
  }
];
