package org.galatea.starter.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinnhubEarningsEntry {
  private String date;
  private Integer quarter;
  private Long revenueActual;
  private Long revenueEstimate;
  private String Symbol;
  private Integer year;
}


