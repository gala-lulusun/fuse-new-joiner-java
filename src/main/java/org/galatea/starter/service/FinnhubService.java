package org.galatea.starter.service;


import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.domain.FinnhubEarningsCalendarEntries.FinnhubEarningsEntry;
import org.galatea.starter.domain.FinnhubLastTradedPrice;
import org.galatea.starter.domain.FinnhubSymbol;
import org.springframework.stereotype.Service;

/**
 * A layer for transformation, aggregation, and business required when retrieving data from IEX.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FinnhubService {

  @NonNull
  private FinnhubClient finnhubClient;

  @NonNull
  private FinnhubDBProvider finnhubDBProvider;

  /**
   * Get all stock symbols from Finnhub for the exchange code given
   * Other optional parameters can be used to filter results by mic, figi, currency.
   * @return a list of all Stock Symbols from Finnhub.
   */

  public List<FinnhubSymbol> getAllSymbols(final String exchange, String mic, String figi, String currency) {
    log.info("Retrieving symbols using FinnhubService.getAllSymbols endpoint taking in the following parameters: mic={}, figi={}, currency={}", mic, figi, currency);
    List<FinnhubSymbol> symbols = finnhubClient.getAllSymbols(exchange, mic, figi, currency);
    log.info("Retrieved symbols using FinnhubService.getAllSymbols endpoint: symbols={}", symbols);
    return symbols;
  }

  /**
   * Get the last traded price for a symbol that is passed in.
   *
   * @param symbol stock symbol to get real-time quote data.
   * @return a singleton list of price object returned by API.
   */
  public List<FinnhubLastTradedPrice> getLastTradedPriceForSymbols(final String symbol) {
    log.info("Retrieving price info using FinnhubService.getLastTradedPriceForSymbols endpoint taking in the following parameter: symbol={}", symbol);
    List<FinnhubLastTradedPrice> lastTradedPrices =  Collections.singletonList(finnhubClient.getLastTradedPriceForSymbols(symbol));
    log.info("Retrieved price info using FinnhubService.getLastTradedPriceForSymbols endpoint : lastTradedPrices={}", lastTradedPrices);
    return lastTradedPrices;
  }

  /**
   * Get historical and coming earnings release. See https://finnhub.io/docs/api/earnings-calendar.
   *
   * Optional parameters for filtering: date range (to, from) & symbol.
   * @return a list of the earnings calendar entries for the params passed in.
   */
  public List<FinnhubEarningsEntry> getEarningsCalendar(String fromDate, String toDate, String symbol) {
    log.info("Retrieving earnings calendar taking the parameters: fromDate={}, toDate={}, symbol={}", fromDate, toDate, symbol);

    // TODO: Refactor this!!!!
//    List<FinnhubEarningsEntry> earningsCalendars = finnhubDBProvider.getEarningsCalendar(fromDate, toDate, symbol).finnhubEntries;

    List<FinnhubEarningsEntry> earningsCalendars = finnhubDBProvider.handleQuery(fromDate, toDate, symbol);

    log.info("Retrieved earnings calendar: earningsCalendars={}", earningsCalendars);
    return earningsCalendars;
  }
}
