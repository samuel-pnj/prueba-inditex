package com.example.similarProducts.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {
    // config/AppConfig.java
    @Bean
    public WebClient webClient(WebClient.Builder builder,
                               @Value("${external.product-service.base-url:http://localhost:3001}") String baseUrl,
                               @Value("${client.max-connections:200}") int maxConnections,
                               @Value("${client.connect-timeout-ms:2000}") int connectTimeoutMs,
                               @Value("${client.read-timeout-ms:10000}") int readTimeoutMs) {

        ConnectionProvider provider = ConnectionProvider.builder("fixed")
                .maxConnections(maxConnections)
                .pendingAcquireMaxCount(-1) // allow queueing
                .build();

        HttpClient httpClient = HttpClient.create(provider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Math.max(1000, connectTimeoutMs))
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(Math.max(1, readTimeoutMs), TimeUnit.MILLISECONDS)));

        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        return builder
                .clientConnector(connector)
                .baseUrl(baseUrl)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(c -> c.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)).build())
                .build();
    }


}
