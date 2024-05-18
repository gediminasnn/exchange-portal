import { Component, Input, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { CanvasJSAngularChartsModule } from '@canvasjs/angular-charts';
import Currency from '../../../../../core/models/currency.model';
import { NgbdDatepickerRangePopup } from '../../../../../shared/ngbd-datepicker-range-popup/ngbd-datepicker-range-popup.component';

@Component({
  selector: 'currency-history-chart',
  standalone: true,
  imports: [CommonModule, RouterOutlet, CanvasJSAngularChartsModule, NgbdDatepickerRangePopup],
  templateUrl: './currency-history-chart.component.html',
  styleUrls: ['./currency-history-chart.component.scss']
})
export class CurrencyHistoryChartComponent {
  @Input() currency: Currency | null = null;
  dps: { x: Date, y: number }[] = [];
  chart: any;
  chartTitle: string | null = null;

  chartOptions = {
    exportEnabled: true,
    title: {
      text: this.chartTitle
    },
    data: [{
      type: "line",
      dataPoints: this.dps
    }]
  };

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['currency']) {
      this.updateChartData();
    }
  }

  updateChartData() {
    if (this.currency && this.currency.exchangeRates) {
      this.chartOptions.title.text = `${this.currency.name} Exchange Rate History (Eur)`;
      this.dps = this.currency.exchangeRates.map(rate => ({
        x: new Date(rate.date),
        y: rate.rate
      }));
      this.chartOptions.data[0].dataPoints = this.dps;
      if (this.chart) {
        this.chart.render();
      }
    }
  }

  getChartInstance(chart: object) {
    this.chart = chart;
  }
}
