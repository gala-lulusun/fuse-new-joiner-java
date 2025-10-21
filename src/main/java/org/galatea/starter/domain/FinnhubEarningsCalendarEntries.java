package org.galatea.starter.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FinnhubEarningsCalendarEntries {

  @JsonProperty("earningsCalendar")
  public List<FinnhubEarningsEntry> finnhubEntries;

  @Data
  @Builder
  public static class FinnhubEarningsEntry {
    private String date;
    private Integer quarter;
    private Long revenueActual;
    private Long revenueEstimate;
    private String Symbol;
    private Integer year;
  }
}
