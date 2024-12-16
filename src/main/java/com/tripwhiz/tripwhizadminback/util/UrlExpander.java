package com.tripwhiz.tripwhizadminback.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlExpander {

    public static String expandShortUrl(String shortUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(shortUrl).openConnection();
        connection.setInstanceFollowRedirects(false); // 리다이렉트를 따르지 않음
        connection.connect();

        // "Location" 헤더에서 리다이렉트된 URL 추출
        String expandedUrl = connection.getHeaderField("Location");
        connection.disconnect();

        if (expandedUrl == null) {
            throw new IOException("Failed to resolve short URL: " + shortUrl);
        }

        return expandedUrl;
    }
}
