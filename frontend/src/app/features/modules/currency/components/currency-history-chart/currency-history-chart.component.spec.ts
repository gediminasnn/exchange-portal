import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrencyHistoryChartComponent } from './currency-history-chart.component';

describe('CurrencyHistoryChartComponent', () => {
  let component: CurrencyHistoryChartComponent;
  let fixture: ComponentFixture<CurrencyHistoryChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CurrencyHistoryChartComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CurrencyHistoryChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
