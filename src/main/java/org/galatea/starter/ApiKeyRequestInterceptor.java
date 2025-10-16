package org.galatea.starter;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;


@Slf4j
public class ApiKeyRequestInterceptor implements RequestInterceptor {

  public final String apiKey;
  public ApiKeyRequestInterceptor(String apiKey) {
    this.apiKey = apiKey;
  }

  @Override
  public void apply(RequestTemplate template) {
    // Instead of injecting API key into the header, inject into the query
    // Finnhub API format is ?token=<API_KEY>
    template.query("token", apiKey);
  }

}

