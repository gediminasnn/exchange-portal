import { Component } from '@angular/core';
import { ExchangeRatesTableComponent } from '../../components/exchange-rates-table/exchange-rates-table.component';
import ExchangeRates from '../../../../core/models/exchange-rates.model';
import { ExchangeRatesService } from '../../../../core/services/exchange-rates.service';

@Component({
  selector: 'app-exchange-rates-page',
  standalone: true,
  imports: [ExchangeRatesTableComponent],
  templateUrl: './exchange-rates-page.component.html',
  styleUrl: './exchange-rates-page.component.scss'
})
export class ExchangeRatesPageComponent {
  exchangeRates: ExchangeRates[] = [];

  constructor(private exchangeRatesService: ExchangeRatesService) { }

  ngOnInit() {
    this.fetchExchangeRates();
  }

  fetchExchangeRates() {
    this.exchangeRatesService.fetchAll().subscribe(exchangeRates => {
      this.exchangeRates = exchangeRates;
    })
  }
}
