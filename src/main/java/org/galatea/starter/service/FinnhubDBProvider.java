package org.galatea.starter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.galatea.starter.domain.FinnhubEarningsCalendarEntries;
import org.galatea.starter.domain.FinnhubEarningsCalendarEntries.FinnhubEarningsEntry;
import org.postgresql.ds.PGSimpleDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor

public class FinnhubDBProvider {
  @NonNull
  private FinnhubClient finnhubClient;
  private static Set<String> databaseDates;

  public List<FinnhubEarningsEntry> handleQuery(String fromDate, String toDate, String symbol) {
    /*
    calendar/earnings?from=2025-08-01&to=2025-10-09
     */
    List<String> datesList = getDatesFromRange(fromDate, toDate);
    log.info("Generated list of dates from {} to {}: {}", fromDate, toDate, datesList);

    List<String> datesNotInDatabase = datesNotInDatabase(datesList);
    // TODO: Try/except?

    return null;
  }

  private FinnhubEarningsCalendarEntries getEarningsCalendar(String fromDate, String toDate, String symbol) {

    FinnhubEarningsCalendarEntries earningsCalendars = finnhubClient.getEarningsCalendar(fromDate, toDate, symbol);
    return earningsCalendars;

  }

  private boolean updateDatesFromDB() throws SQLException {
    /*
    Connects to the database and fetches all dates from the exchangecalendar table.
    Updates the databaseDates set with the fetched dates.
    Returns true if the update is successful.
     */

    log.info("Connecting to the database....");
    DataSource dataSource = createDataSource();
    Connection conn = dataSource.getConnection();
    PreparedStatement stmt = conn.prepareStatement("SELECT date FROM finnhub_financialdata.exchangecalendar");
    ResultSet rs = stmt.executeQuery();

    Set<String> databaseFetchedDates = new HashSet<>();
    log.info("Database connected! Fetching dates...");

    while (rs.next()) {
      String date = rs.getString("date");
      databaseFetchedDates.add(date);
    }

    log.info("Fetched {} dates from database", databaseFetchedDates.size());
    return updateDatabaseDates(databaseFetchedDates);
  }

  private boolean updateDatabaseDates(Set<String> databaseFetchedDates) {
    databaseDates = databaseFetchedDates;
    return true;
  }

  private List<String> datesNotInDatabase(List<String> listOfDates) {
    // Takes list of dates and checks if each one is in the database
    List<String> datesNotInDatabase = new ArrayList<>();

    for (String date : listOfDates) {
      if (!databaseDates.contains(date)) {
        datesNotInDatabase.add(date);
      }
    }

    return datesNotInDatabase;
  }

  private List<String> getDatesFromRange(String fromDate, String toDate) {
    // Takes two dates in "YYYY-MM-DD" format and returns a list of LocalDate objects representing each date in the range (inclusive).

    LocalDate fromDateObj = LocalDate.parse(fromDate);
    LocalDate toDateObj = LocalDate.parse(toDate);

    List<LocalDate> datesListObjs = fromDateObj.datesUntil(toDateObj.plusDays(1)).collect(Collectors.toList());

    // However I do NOT want the LocalDate objects, strings are easier to work with
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    List<String> datesList = datesListObjs.stream()
        .map(date -> date.format(formatter))
        .collect(Collectors.toList());

    return datesList;
  }


  private boolean addToDatabase(List<FinnhubEarningsEntry> earningsEntries) {
    // TODO: Add earnings entries to database
    log.info("Added {} rows to database", earningsEntries.size());
    return true;
  }


  private static DataSource createDataSource(){
    // TODO put password as an env variable. add comments too
    final String url = "jdbc:postgresql://localhost:5432/finnhub?user=postgres&password=4589";
    final PGSimpleDataSource dataSource = new PGSimpleDataSource();
    dataSource.setUrl(url);
    return dataSource;
  }
}
