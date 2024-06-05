import ExchangeRates from "./exchange-rates.model";

export default interface Currency {
  id: bigint;
  code: string;
  name: string;
  exchangeRates?: ExchangeRates[];
}
