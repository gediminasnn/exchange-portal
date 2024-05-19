import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExchangeRatesPageComponent } from './exchange-rates-page.component';

describe('ExchangeRatesPageComponent', () => {
  let component: ExchangeRatesPageComponent;
  let fixture: ComponentFixture<ExchangeRatesPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExchangeRatesPageComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ExchangeRatesPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
