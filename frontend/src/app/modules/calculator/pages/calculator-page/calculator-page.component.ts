import { Component } from '@angular/core';
import ExchangeRates from '../../../../core/models/exchange-rates.model';
import { ExchangeRatesService } from '../../../../core/services/exchange-rates.service';
import { CalculatorFormComponent } from '../../components/calculator-form/calculator-form.component';

@Component({
  selector: 'app-calculator-page',
  standalone: true,
  imports: [CalculatorFormComponent],
  templateUrl: './calculator-page.component.html',
  styleUrl: './calculator-page.component.scss'
})
export class CalculatorPageComponent {
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
