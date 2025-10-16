package org.galatea.starter.domain;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinnhubSymbol {

  private String currency;
  private String description;
  private String displaySymbol;
  private String figi;
  private String mic;
  private String symbol;
  private String type;

}
