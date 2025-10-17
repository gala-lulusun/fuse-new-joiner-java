package org.galatea.starter.domain;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinnhubLastTradedPrice {
  private float c;
  private float d;
  private float dp;
  private float h;
  private float l;
  private float o;
  private float pc;
  private float t;
}
