package org.galatea.starter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.galatea.starter.domain.FinnhubEarningsCalendarEntries;
import org.galatea.starter.domain.FinnhubEarningsEntry;
import org.postgresql.ds.PGSimpleDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor

public class FinnhubDBProvider {
  @NonNull
  private FinnhubClient finnhubClient;
  private static List<LocalDate> databaseDates;

  public List<FinnhubEarningsEntry> handleQuery(String fromDate, String toDate, String symbol)
      throws SQLException {
    /*
    calendar/earnings?from=2025-08-01&to=2025-10-09
     */

    String queryString = createQueryString(fromDate, toDate, symbol);
    boolean query_exists = queryExists(queryString);

    if (query_exists) {
      //TODO get the results!
    }
    else {
      //TODO finnhubClient.getEarningsCalendar --> STORE IN DB!!!
    }

    return null;
  }

  private FinnhubEarningsCalendarEntries getEarningsCalendar(String fromDate, String toDate, String symbol) {

    FinnhubEarningsCalendarEntries earningsCalendars = finnhubClient.getEarningsCalendar(fromDate, toDate, symbol);
    return earningsCalendars;

  }

  private String storeQueryResults(String query, FinnhubEarningsCalendarEntries earningsCalendar) throws SQLException {
    //TODO: Edit this so that it stores exchangeCalendar entries & put ex_calendar_id's into queryhistory table
    log.info("Connecting to the database....");
    DataSource dataSource = createDataSource();
    Connection conn = dataSource.getConnection();
    PreparedStatement stmt = conn.prepareStatement("INSERT INTO finnhub_financialdata.queryhistory (query_string, query_results) VALUES (?, ?)");
    stmt.setString(1, query);
    stmt.setString(2, earningsCalendar.toString());
    int rowsInserted = stmt.executeUpdate();

    log.info("Database connected! Rows inserted: {}", rowsInserted);

    return query;
  }

  private List<Integer> retrieveQueryCalendarIDs(String query) throws SQLException {

    //TODO: There can actually be multiple ex_calendar_id's per query
    //TODO: Not returning void! The goal is to get a List of finnhubEntries using @builder

    log.info("Connecting to the database....");
    DataSource dataSource = createDataSource();
    Connection conn = dataSource.getConnection();
    PreparedStatement stmt = conn.prepareStatement("SELECT ex_calendar_id FROM finnhub_financialdata.queryhistory WHERE query_string = ?");
    stmt.setString(1, query);
    ResultSet rs = stmt.executeQuery();

    log.info("Database connected! Query exists? {}", rs.next());

    List<Integer> ex_calendar_ids = new ArrayList<>();

    while (rs.next()) {
      log.info("ex_calendar_id: {}", rs.getString("ex_calendar_id"));
      ex_calendar_ids.add(rs.getInt("ex_calendar_id"));
    }

    return ex_calendar_ids;
  }

  private List<FinnhubEarningsEntry> combineEarningsEntries(List<Integer> ex_calendar_ids) throws SQLException {
    // Using the list of ex_calendar_ids, retrieve the corresponding FinnhubEarningsEntry objects from the database
    // TODO: Still need to check if this actually works!
    log.info("Connecting to the database....");
    DataSource dataSource = createDataSource();
    Connection conn = dataSource.getConnection();
    PreparedStatement stmt = conn.prepareStatement("SELECT date, quarter, revenue_actual, revenue_estimated, symbol, year FROM finnhub_financialdata.exchangecalendar WHERE exchange_id = ?");

    List<FinnhubEarningsEntry> earningsEntries = new ArrayList<>();

    for (Integer id : ex_calendar_ids) {
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        FinnhubEarningsEntry entry = createEarningsEntry(
            rs.getString("date"),
            rs.getInt("quarter"),
            rs.getLong("revenue_actual"),
            rs.getLong("revenue_estimated"),
            rs.getString("symbol"),
            rs.getInt("year")
        );
        earningsEntries.add(entry);
      }
    }

    log.info("Database connected! Retrieved {} earnings entries.", earningsEntries.size());
    return earningsEntries;
  }

  private FinnhubEarningsEntry createEarningsEntry(String date, Integer quarter, Long revenueActual, Long revenueEstimate, String symbol, Integer year) {
    // Takes in fields to create a FinnhubEarningsEntry object
    FinnhubEarningsEntry earningsEntry = FinnhubEarningsEntry.builder().date(date)
        .quarter(quarter)
        .revenueActual(revenueActual)
        .revenueEstimate(revenueEstimate)
        .Symbol(symbol)
        .year(year)
        .build();

    return earningsEntry;
  }

  private boolean queryExists(String query) throws SQLException {
    // Check if the query is in the database

    log.info("Connecting to the database....");
    DataSource dataSource = createDataSource();
    Connection conn = dataSource.getConnection();
    PreparedStatement stmt = conn.prepareStatement("SELECT query_string FROM finnhub_financialdata.queryhistory WHERE query_string = ?");
    stmt.setString(1, query);
    ResultSet rs = stmt.executeQuery();

    log.info("Database connected! Query exists? {}", rs.next());
    return rs.next();
  }

  private String createQueryString(String fromDate, String toDate, String symbol) {
    // This should be used as the KEY value in the queryhistory table!!!

    String queryString = fromDate + toDate + symbol;
    return queryString;
  }

  private static DataSource createDataSource(){
    // TODO put password as an env variable. add comments too
    final String url = "jdbc:postgresql://localhost:5432/finnhub?user=postgres&password=4589";
    final PGSimpleDataSource dataSource = new PGSimpleDataSource();
    dataSource.setUrl(url);
    return dataSource;
  }
}
