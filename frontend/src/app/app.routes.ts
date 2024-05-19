import { Routes } from '@angular/router';
import { CurrencyHistoryPageComponent } from './modules/currency/pages/currency-history-page/currency-history-page.component';
import { ExchangeRatesPageComponent } from './modules/exchange-rate/pages/exchange-rates-page/exchange-rates-page.component';
import { CalculatorPageComponent } from './modules/calculator/pages/calculator-page/calculator-page.component';

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
  },
  {
    path: 'calculator',
    component: CalculatorPageComponent,
    title: 'Currency calculator'
  }
];
