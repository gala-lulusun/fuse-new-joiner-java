package org.galatea.starter.entrypoint;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.aspect4log.Log;
import net.sf.aspect4log.Log.Level;
import org.galatea.starter.domain.FinnhubEarningsCalendarEntries.FinnhubEarningsEntry;
import org.galatea.starter.domain.FinnhubLastTradedPrice;
import org.galatea.starter.domain.FinnhubSymbol;
import org.galatea.starter.service.FinnhubService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Log(enterLevel = Level.INFO, exitLevel = Level.INFO)
@Validated
@RestController
@RequiredArgsConstructor
public class FinnhubRestController {

  @NonNull
  private FinnhubService finnhubService;

  /**
   * Exposes an endpoint to get all symbols on Finnhub.
   *
   * @return a list of all Finnhub Symbols.
   */
  @GetMapping(value = "${mvc.finnhub.getAllSymbolsPath}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public List<FinnhubSymbol> getAllStockSymbols(
      @RequestParam String exchange,
      @RequestParam(required = false) String mic,
      @RequestParam(required = false) String figi,
      @RequestParam(required = false) String currency
  ) {
    return finnhubService.getAllSymbols(exchange, mic, figi, currency);
  }

  /**
   * Get the last traded price for each of the symbols passed in.
   *
   * @param symbol stock symbol to get real-time quote data.
   * @return .
   */
  @GetMapping(value = "${mvc.finnhub.getLastTradedPricePath}", produces = {
      MediaType.APPLICATION_JSON_VALUE})
  public List<FinnhubLastTradedPrice> getLastTradedPrice(
      @RequestParam(value = "symbol") final String symbol) {
    return finnhubService.getLastTradedPriceForSymbols(symbol);
  }

  /**
   * Get historical and coming earnings release. See https://finnhub.io/docs/api/earnings-calendar.
   *
   * Optional parameters for filtering: date range (to, from) & symbol.
   * @return a list of the earnings calendar entries for the params passed in.
   */

  @GetMapping(value = "${mvc.finnhub.getEarningsCalendarPath}", produces = {
      MediaType.APPLICATION_JSON_VALUE})
  public List<FinnhubEarningsEntry> getEarningsCalendar(
      @RequestParam (value = "from", required = false) String fromDate,
      @RequestParam (value = "to", required = false) String toDate,
      @RequestParam (value = "symbol", required = false) String symbol
  ) {
    return finnhubService.getEarningsCalendar(fromDate, toDate, symbol);

  }
}
