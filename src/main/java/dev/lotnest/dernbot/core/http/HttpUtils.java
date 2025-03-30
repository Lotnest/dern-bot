package dev.lotnest.dernbot.core.http;

import com.google.common.net.MediaType;

import java.net.URI;
import java.net.http.HttpRequest;
import java.time.Duration;

public final class HttpUtils {
    public static final Duration TIMEOUT_DURATION = Duration.ofSeconds(30);

    private HttpUtils() {
    }

    public static HttpRequest newGetRequest(String url) {
        return newGetRequest(url, TIMEOUT_DURATION);
    }

    public static HttpRequest newGetRequest(String url, Duration timeoutDuration) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeoutDuration)
                .GET()
                .build();
    }

    public static HttpRequest newPostRequest(String url, String body) {
        return newPostRequest(url, body, TIMEOUT_DURATION);
    }

    public static HttpRequest newPostRequest(String url, String body, Duration timeoutDuration) {
        return HttpRequest.newBuilder()
                .header("Content-Type", MediaType.JSON_UTF_8.type())
                .uri(URI.create(url))
                .timeout(timeoutDuration)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
    }
}