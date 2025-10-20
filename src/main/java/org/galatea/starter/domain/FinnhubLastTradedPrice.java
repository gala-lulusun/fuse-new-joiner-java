package org.galatea.starter.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class FinnhubLastTradedPrice {
  @JsonProperty("c")
  private float currentPrice;
  @JsonProperty("d")
  private float change;
  @JsonProperty("dp")
  private float percentChange;
  @JsonProperty("h")
  private float highPriceOfDay;
  @JsonProperty("l")
  private float lowPriceOfDay;
  @JsonProperty("o")
  private float openPriceOfDay;
  @JsonProperty("pc")
  private float previousClosePrice;
  @JsonProperty("t")
  private float timestamp;
}
