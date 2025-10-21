package org.galatea.starter.service;

import java.util.List;
import org.galatea.starter.domain.FinnhubEarningsCalendarEntries;
import org.galatea.starter.domain.FinnhubEarningsCalendarEntries.FinnhubEarningsEntry;
import org.galatea.starter.domain.FinnhubLastTradedPrice;
import org.galatea.starter.domain.FinnhubSymbol;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
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
   * Other optional parameters may be passed in for filtering, though not integrated into tests yet.
   * @return a list of all stock symbols supported by Finnhub.
   */
  @GetMapping(value = "${mvc.finnhub.getAllSymbolsPath}", produces = {MediaType.APPLICATION_JSON_VALUE})
  List<FinnhubSymbol> getAllSymbols(
      @RequestParam("exchange") String exchange,
      @RequestParam(value = "mic", required = false) String mic,
      @RequestParam(value = "figi", required = false) String figi,
      @RequestParam(value = "currency", required = false) String currency
  );

  /**
   * Get the last traded price for each stock symbol passed in. See https://finnhub.io/docs/api/quote.
   *
   * @param symbol stock symbol to get real-time quote data.
   * @return a list of the last traded price for each of the symbols passed in.
   */
  @GetMapping(value = "${mvc.finnhub.getLastTradedPricePath}", produces = {MediaType.APPLICATION_JSON_VALUE})
  FinnhubLastTradedPrice getLastTradedPriceForSymbols(@RequestParam("symbol") String symbol);

  /**
   * Get historical and coming earnings release. See https://finnhub.io/docs/api/earnings-calendar.
   *
   * Optional parameters for filtering: date range (to, from) & symbol.
   * @return a list of the earnings calendar entries for the params passed in.
   */

  @GetMapping(value = "${mvc.finnhub.getEarningsCalendarPath}", produces = {MediaType.APPLICATION_JSON_VALUE})
  FinnhubEarningsCalendarEntries getEarningsCalendar(
      @RequestParam (value = "from", required = false) String fromDate,
      @RequestParam (value = "to", required = false) String toDate,
      @RequestParam (value = "symbol", required = false) String symbol
  );

}
