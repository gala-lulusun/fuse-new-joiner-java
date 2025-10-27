package org.galatea.starter.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FinnhubEarningsCalendarEntries {

  @JsonProperty("earningsCalendar")
  public List<FinnhubEarningsEntry> finnhubEntries;

}
