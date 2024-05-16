import { Component } from '@angular/core';
import { ExchangeRatesService } from '../../core/services/exchange-rates.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import ExchangeRates from '../../core/models/exchange-rates.model';

@Component({
  selector: 'app-exchange-rates-table',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './exchange-rates-table.component.html',
  styleUrl: './exchange-rates-table.component.scss',
  providers: [ExchangeRatesService]
})
export class ExchangeRatesTableComponent {
  exchangeRates: ExchangeRates[] = [];

  constructor(private exchangeRatesService: ExchangeRatesService) { }

  ngOnInit() {
    this.fetchExchangeRates();
  }

  fetchExchangeRates(): void {
    this.exchangeRatesService.fetchAll()
      .subscribe(exchangeRates => (this.exchangeRates = exchangeRates));
  }
}
