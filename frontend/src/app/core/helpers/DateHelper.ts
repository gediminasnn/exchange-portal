import { NgbDate } from "@ng-bootstrap/ng-bootstrap";

export class DateHelper {
  static formatNgbDate(date: NgbDate): string {
    const year = date.year.toString();
    const month = date.month < 10 ? `0${date.month}` : date.month.toString();
    const day = date.day < 10 ? `0${date.day}` : date.day.toString();
    return `${year}-${month}-${day}`;
  }
}
