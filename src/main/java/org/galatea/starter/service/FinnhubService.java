package org.galatea.starter.service;

import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.domain.FinnhubLastTradedPrice;
import org.galatea.starter.domain.FinnhubSymbol;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * A layer for transformation, aggregation, and business required when retrieving data from IEX.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FinnhubService {

  @NonNull
  private FinnhubClient finnhubClient;


  /**
   * Get all stock symbols from Finnhub for the exchange code given
   * Other optional parameters can be used to filter results by mic, figi, currency.
   * @return a list of all Stock Symbols from Finnhub.
   */

  public List<FinnhubSymbol> getAllSymbols(final String exchange, String mic, String figi, String currency) {
    System.out.printf("mic: %s, figi: %s, currency: %s", mic, figi, currency);
    return finnhubClient.getAllSymbols(exchange, mic, figi, currency);
  }

  /**
   * Get the last traded price for a symbol that is passed in.
   *
   * @param symbol stock symbol to get real-time quote data.
   * @return a singleton list of price object returned by API.
   */
  public List<FinnhubLastTradedPrice> getLastTradedPriceForSymbols(final String symbol) {
    System.out.printf("symbol: %s", symbol);
    return Collections.singletonList(finnhubClient.getLastTradedPriceForSymbols(symbol));
  }


}
