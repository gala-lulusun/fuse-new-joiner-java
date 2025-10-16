package org.galatea.starter.domain;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinnhubLastTradedPrice {
  private String currency;
  private String description;
  private String displaySymbol;
  private String figi;
  private String mic;
  private String symbol;
  private String type;
}
