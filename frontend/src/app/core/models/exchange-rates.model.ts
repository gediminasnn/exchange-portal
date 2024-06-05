import Currency from "./currency.model";

export default interface ExchangeRates {
  currency: Currency;
  rate: number;
  date: string;
}
