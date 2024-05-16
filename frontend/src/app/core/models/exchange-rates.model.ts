import Currency from "./currency.model";

export default interface ExchangeRates {
  id: bigint;
  currency: Currency;
  rate: number;
  date: string;
}
