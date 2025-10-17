package org.galatea.starter.entrypoint;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.aspect4log.Log;
import net.sf.aspect4log.Log.Level;
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
   * @param symbols list of symbols to get last traded price for.
   * @return a List of IexLastTradedPrice objects for the given symbols.
   */
  @GetMapping(value = "${mvc.finnhub.getLastTradedPricePath}", produces = {
      MediaType.APPLICATION_JSON_VALUE})
  public List<FinnhubLastTradedPrice> getLastTradedPrice(
      @RequestParam(value = "symbol") final List<String> symbols) {
    return finnhubService.getLastTradedPriceForSymbols(symbols);
  }

}
