import { Component, EventEmitter, Output, inject } from '@angular/core';
import { NgbCalendar, NgbDate, NgbDateParserFormatter, NgbDateStruct, NgbDatepickerModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { JsonPipe } from '@angular/common';
import { DateHelper } from '../../core/helpers/DateHelper';

@Component({
  selector: 'ngbd-datepicker-range-popup',
  standalone: true,
  imports: [NgbDatepickerModule, FormsModule, JsonPipe],
  templateUrl: './ngbd-datepicker-range-popup.component.html',
  styleUrl: './ngbd-datepicker-range-popup.component.scss'
})
export class NgbdDatepickerRangePopup {
  @Output() toDateChange = new EventEmitter<string>();
  @Output() fromDateChange = new EventEmitter<string>();

  displayMonthsCount = 2;

  calendar = inject(NgbCalendar);
  formatter = inject(NgbDateParserFormatter);

  hoveredDate: NgbDate | null = null;
  fromDate: NgbDate | null = this.calendar.getPrev(this.calendar.getToday(), 'm', 1);
  toDate: NgbDate | null = this.calendar.getToday();

  maxDate: NgbDateStruct = this.calendar.getToday();

  onDateSelection(date: NgbDate) {
    if (!this.fromDate && !this.toDate) {
      this.fromDate = date;
      this.fromDateChange.emit(DateHelper.formatNgbDate(this.fromDate));
    } else if (this.fromDate && !this.toDate && date && date.after(this.fromDate)) {
      this.toDate = date;
      this.toDateChange.emit(DateHelper.formatNgbDate(date));
    } else {
      this.toDate = null;
      this.fromDate = date;
      this.fromDateChange.emit(DateHelper.formatNgbDate(this.fromDate));
    }
  }

  isHovered(date: NgbDate) {
    return (
      this.fromDate && !this.toDate && this.hoveredDate && date.after(this.fromDate) && date.before(this.hoveredDate)
    );
  }

  isInside(date: NgbDate) {
    return this.toDate && date.after(this.fromDate) && date.before(this.toDate);
  }

  isRange(date: NgbDate) {
    return (
      date.equals(this.fromDate) ||
      (this.toDate && date.equals(this.toDate)) ||
      this.isInside(date) ||
      this.isHovered(date)
    );
  }

  validateInput(currentValue: NgbDate | null, input: string): NgbDate | null {
    const parsed = this.formatter.parse(input);
    return parsed && this.calendar.isValid(NgbDate.from(parsed)) ? NgbDate.from(parsed) : currentValue;
  }
}
