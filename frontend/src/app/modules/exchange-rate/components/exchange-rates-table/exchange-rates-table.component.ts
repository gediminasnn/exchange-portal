import { Component, Input } from '@angular/core';
import { ExchangeRatesService } from '../../../../core/services/exchange-rates.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import ExchangeRates from '../../../../core/models/exchange-rates.model';

@Component({
  selector: 'app-exchange-rates-table',
  standalone: true,
  templateUrl: './exchange-rates-table.component.html',
  styleUrl: './exchange-rates-table.component.scss',
  providers: [ExchangeRatesService],
  imports: [CommonModule, RouterModule]
})
export class ExchangeRatesTableComponent {
  @Input() exchangeRates: ExchangeRates[] = [];
}
