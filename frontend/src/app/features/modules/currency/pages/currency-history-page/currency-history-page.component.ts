import { Component } from '@angular/core';
import { CurrencyHistoryChartComponent } from '../../components/currency-history-chart/currency-history-chart.component';
import { CanvasJSAngularChartsModule } from '@canvasjs/angular-charts';
import { Subscription } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { CurrencyService } from '../../../../../core/services/currency.service';
import Currency from '../../../../../core/models/currency.model';
import { NgbdDatepickerRangePopup } from '../../../../../shared/ngbd-datepicker-range-popup/ngbd-datepicker-range-popup.component';

@Component({
  selector: 'app-currency-history-page',
  standalone: true,
  imports: [CurrencyHistoryChartComponent, CanvasJSAngularChartsModule, NgbdDatepickerRangePopup],
  templateUrl: './currency-history-page.component.html',
  styleUrl: './currency-history-page.component.scss'
})
export class CurrencyHistoryPageComponent {
  currencyId: number;
  currency: Currency | null = null;
  fromDate: String | null = null;
  toDate: String | null = null;

  constructor(
    private route: ActivatedRoute,
    private currencyService: CurrencyService
  ) {
    this.currencyId = Number(this.route.snapshot.params['id']);
  }

  ngOnInit() {
    this.fetchCurrencyWithExchangeRateHistory();
  }

  fetchCurrencyWithExchangeRateHistory() {
    this.currencyService
      .fetchCurrencyWithExchangeRateHistory(this.currencyId, this.fromDate, this.toDate)
      .subscribe(currency => {
        this.currency = currency;
      });
  }

  onFromDateChange(newFromDate: string) {
    this.fromDate = newFromDate;
  }

  onToDateChange(newToDate: string) {
    this.toDate = newToDate;
    this.fetchCurrencyWithExchangeRateHistory();
  }
}
