package org.galatea.starter.service;

import java.util.List;
import org.galatea.starter.domain.FinnhubLastTradedPrice;
import org.galatea.starter.domain.FinnhubSymbol;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * A Feign Declarative REST Client to access endpoints from the Finnhub API to get market
 * data. See https://finnhub.io/docs/api/introduction
 */
@FeignClient(name = "FINNHUB", url = "${spring.rest.finnhubBasePath}")
public interface FinnhubClient {

  /**
   * Get a list of all stocks supported by Finnhub. See https://finnhub.io/docs/api/stock-symbols.
   * Constant polling is not recommended - use websocket if you need real-time updates.
   *
   * @return a list of all stock symbols supported by Finnhub.
   */
  @GetMapping("/ref-data/symbol")

  List<FinnhubSymbol> getAllSymbols(
      @RequestParam("exchange") String exchange,
      @RequestParam(value = "mic", required = false) String mic,
      @RequestParam(value = "figi", required = false) String figi,
      @RequestParam(value = "currency", required = false) String currency
  );

  /**
   * Get the last traded price for each stock symbol passed in. See https://finnhub.io/docs/api/quote.
   *
   * @param symbols stock symbols to get last traded price for.
   * @return a list of the last traded price for each of the symbols passed in.
   */
  @GetMapping("/tops/last")
  List<FinnhubLastTradedPrice> getLastTradedPriceForSymbols(@RequestParam("symbol") String[] symbols);

}
