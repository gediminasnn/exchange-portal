import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrencyHistoryPageComponent } from './currency-history-page.component';

describe('CurrencyHistoryPageComponent', () => {
  let component: CurrencyHistoryPageComponent;
  let fixture: ComponentFixture<CurrencyHistoryPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CurrencyHistoryPageComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CurrencyHistoryPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
