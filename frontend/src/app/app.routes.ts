import { Routes } from '@angular/router';
import { ExchangeRatesTableComponent } from './features/exchange-rates-table/exchange-rates-table.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'exchange-rates',
    pathMatch: 'full'
  },
  {
    path: 'exchange-rates',
    component: ExchangeRatesTableComponent,
    title: 'Exchange rates'
  }
];
