import { Component, Input, SimpleChanges } from '@angular/core';
import ExchangeRates from '../../../../core/models/exchange-rates.model';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import Currency from '../../../../core/models/currency.model';

@Component({
  selector: 'app-calculator-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './calculator-form.component.html',
  styleUrls: ['./calculator-form.component.scss']
})
export class CalculatorFormComponent {
  @Input() exchangeRates: ExchangeRates[] = [];
  calculatorForm: FormGroup;
  calculatedAmount: string = '0.00';
  calculatedRate: string = '0.00';

  constructor(private fb: FormBuilder) {
    this.calculatorForm = this.fb.group({
      fromCurrency: [''],
      amount: [''],
      toCurrency: ['']
    });
  }

  ngOnInit(): void {
    this.calculatorForm.valueChanges.subscribe(() => this.onCalculate());
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['exchangeRates'] && changes['exchangeRates'].currentValue.length > 0) {
      this.setDefaultValues();
      this.onCalculate();
    }
  }

  setDefaultValues() {
    this.calculatorForm.setValue({
      fromCurrency: this.exchangeRates.find(rate => rate.currency.code === 'USD')?.currency.code || '',
      toCurrency: this.exchangeRates.find(rate => rate.currency.code === 'CHF')?.currency.code || '',
      amount: 100000
    });
  }

  onCalculate() {
    const fromCurrency = this.calculatorForm.value.fromCurrency;
    const toCurrency = this.calculatorForm.value.toCurrency;
    const amount = this.calculatorForm.value.amount;

    const fromRate = this.exchangeRates.find(rate => rate.currency.code === fromCurrency)?.rate || 1;
    const toRate = this.exchangeRates.find(rate => rate.currency.code === toCurrency)?.rate || 1;

    this.calculatedRate = (toRate / fromRate).toFixed(2);
    this.calculatedAmount = (amount * (toRate / fromRate)).toFixed(2);
  }
}
