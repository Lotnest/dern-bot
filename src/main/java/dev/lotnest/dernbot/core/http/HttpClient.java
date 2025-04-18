package dev.lotnest.dernbot.core.http;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class HttpClient {
    private static final int[] OK_STATUS_CODES = {200, 201, 202, 203, 204, 205, 206, 207, 208, 226};
    private static final int POOL_SIZE = 10;
    private static final ConcurrentMap<java.net.http.HttpClient, Boolean> httpClientPool = Maps.newConcurrentMap();

    private final RestTemplate restTemplate;

    protected HttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        createClientPool();
    }

    private void createClientPool() {
        for (int i = 0; i < POOL_SIZE; i++) {
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            httpClientPool.put(client, false);
        }
    }

    private boolean isOkStatusCode(int statusCode) {
        for (int okStatusCode : OK_STATUS_CODES) {
            if (statusCode == okStatusCode) {
                return true;
            }
        }
        return false;
    }

    private java.net.http.HttpClient getAvailableClient() {
        for (Map.Entry<java.net.http.HttpClient, Boolean> entry : httpClientPool.entrySet()) {
            java.net.http.HttpClient client = entry.getKey();
            if (Boolean.FALSE.equals(entry.getValue())) {
                httpClientPool.put(client, true);
                return client;
            }
        }
        java.net.http.HttpClient newClient = java.net.http.HttpClient.newHttpClient();
        httpClientPool.put(newClient, true);
        return newClient;
    }

    /**
     * Sends a synchronous request using the provided HttpRequest.
     *
     * @param request the HTTP request to send
     * @param context a short text describing the request context for error logging
     * @return the HTTP response, or null if an error occurred
     */
    private HttpResponse<String> sendSyncRequest(HttpRequest request, String context) {
        java.net.http.HttpClient client = getAvailableClient();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            log.error("Thread interrupted while {}", context, exception);
            return null;
        } catch (Exception exception) {
            log.error("Failed {}", context, exception);
            return null;
        } finally {
            httpClientPool.put(client, false);
        }
    }

    /**
     * Sends an asynchronous request using the provided HttpRequest.
     *
     * @param request the HTTP request to send
     * @param context a short text describing the request context for error logging
     * @return a CompletableFuture with the HTTP response, or null if an error occurred
     */
    private CompletableFuture<HttpResponse<String>> sendAsyncRequest(
            HttpRequest request, String context) {
        try {
            java.net.http.HttpClient client = getAvailableClient();
            CompletableFuture<HttpResponse<String>> future =
                    client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            future.thenRun(() -> httpClientPool.put(client, false));
            return future;
        } catch (Exception exception) {
            log.error("Failed {}", context, exception);
            return null;
        }
    }

    public HttpResponse<String> get(String url) {
        return sendSyncRequest(HttpUtils.newGetRequest(url), "fetching response");
    }

    public CompletableFuture<HttpResponse<String>> getAsync(String url) {
        return sendAsyncRequest(HttpUtils.newGetRequest(url), "fetching async response");
    }

    public HttpResponse<String> post(String url, String body) {
        return sendSyncRequest(HttpUtils.newPostRequest(url, body), "posting response");
    }

    public CompletableFuture<HttpResponse<String>> postAsync(String url, String body) {
        return sendAsyncRequest(HttpUtils.newPostRequest(url, body), "posting async response");
    }

    public <T> T getJson(String url, Class<T> responseType) {
        return getJson(url, responseType, (Object) null);
    }

    public <T> T getJson(String url, Class<T> responseType, Object... uriVariables) {
        try {
            return restTemplate.getForObject(url, responseType, uriVariables);
        } catch (Exception exception) {
            log.error("Failed to fetch JSON from {}: {}", url, exception.getMessage());
            return null;
        }
    }

    public <T> CompletableFuture<T> getJsonAsync(String url, Class<T> responseType) {
        return getJsonAsync(url, responseType, (Object) null);
    }

    public <T> CompletableFuture<T> getJsonAsync(String url, Class<T> responseType, Object... uriVariables) {
        return getAsync(url).thenApply(response -> {
            if (response != null) {
                log.info("ASYNC {} {}: {}", response.statusCode(), url, response.body());
                if (response.body() != null && isOkStatusCode(response.statusCode())) {
                    return restTemplate.getForObject(url, responseType, uriVariables);
                }
            }
            return null;
        });
    }
}
