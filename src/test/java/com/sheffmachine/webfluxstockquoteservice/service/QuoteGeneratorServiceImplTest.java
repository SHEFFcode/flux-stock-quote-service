package com.sheffmachine.webfluxstockquoteservice.service;

import com.sheffmachine.webfluxstockquoteservice.model.Quote;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class QuoteGeneratorServiceImplTest {
    QuoteGeneratorServiceImpl quoteGeneratorService = new QuoteGeneratorServiceImpl();

    @Test
    public void fetchQuoteStream() throws Exception {
        Flux<Quote>  quoteFlux = quoteGeneratorService.fetchQuoteStream(Duration.ofMillis(1L));
        quoteFlux.take(22000)
                .subscribe(System.out::println);
    }

    @Test
    public void fetchQuoteStreamCountDown() throws Exception {
        Flux<Quote>  quoteFlux = quoteGeneratorService.fetchQuoteStream(Duration.ofMillis(100L));

        Consumer<Quote> println = System.out::println;
        Consumer<Throwable> errorHandler = e -> System.out.println("Some error occurred");

        CountDownLatch countDownLatch = new CountDownLatch(1);

        Runnable allDone = () -> countDownLatch.countDown();

        quoteFlux.take(10)
                .subscribe(println, errorHandler, allDone);


        countDownLatch.await();
    }
}
