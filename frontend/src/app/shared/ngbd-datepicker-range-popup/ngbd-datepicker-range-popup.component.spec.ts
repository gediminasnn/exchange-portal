import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NgbdDatepickerRangePopup } from './ngbd-datepicker-range-popup.component';

describe('NgbdDatepickerRangePopup', () => {
  let component: NgbdDatepickerRangePopup;
  let fixture: ComponentFixture<NgbdDatepickerRangePopup>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NgbdDatepickerRangePopup]
    })
      .compileComponents();

    fixture = TestBed.createComponent(NgbdDatepickerRangePopup);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
