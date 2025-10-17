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
import org.springframework.util.CollectionUtils;
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
   * Other optional parameters can be used, though are not integrated into tests just yet
   *
   * @return a list of all Stock Symbols from Finnhub.
   */

  @GetMapping(value = "${mvc.finnhub.getAllSymbolsPath}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public List<FinnhubSymbol> getAllSymbols(
      @RequestParam String exchange,
      @RequestParam(required = false) String mic,
      @RequestParam(required = false) String figi,
      @RequestParam(required = false) String currency
  ) {
    return finnhubClient.getAllSymbols(exchange, mic, figi, currency);
  }

  /**
   * Get the last traded price for each Symbol that is passed in.
   *
   * @param symbol stock symbol to get real-time quote data.
   * @return a list of last traded price objects for each Symbol that is passed in.
   */
  public List<FinnhubLastTradedPrice> getLastTradedPriceForSymbols(final String symbol) {

      return Collections.singletonList(finnhubClient.getLastTradedPriceForSymbols(symbol));
  }


}
