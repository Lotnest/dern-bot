package dev.lotnest.dernbot.core.http.clients;

import dev.lotnest.dernbot.core.http.HttpClient;
import dev.lotnest.dernbot.core.http.ratelimiter.RateLimiters;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Service
public class MojangApiHttpClient extends HttpClient {
    public MojangApiHttpClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public HttpResponse<String> get(String url) {
        RateLimiters.MOJANG_API.acquire();
        try {
            return super.get(url);
        } finally {
            RateLimiters.MOJANG_API.release();
        }
    }

    @Override
    public CompletableFuture<HttpResponse<String>> getAsync(String url) {
        return CompletableFuture.runAsync(RateLimiters.MOJANG_API::acquire)
                .thenCompose(_ -> super.getAsync(url))
                .whenComplete((_, _) -> RateLimiters.MOJANG_API.release());
    }

    @Override
    public HttpResponse<String> post(String url, String body) {
        RateLimiters.MOJANG_API.acquire();
        try {
            return super.post(url, body);
        } finally {
            RateLimiters.MOJANG_API.release();
        }
    }

    @Override
    public CompletableFuture<HttpResponse<String>> postAsync(String url, String body) {
        return CompletableFuture.runAsync(RateLimiters.MOJANG_API::acquire)
                .thenCompose(_ -> super.postAsync(url, body))
                .whenComplete((_, _) -> RateLimiters.MOJANG_API.release());
    }
}
