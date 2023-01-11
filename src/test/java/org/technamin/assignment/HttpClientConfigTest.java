package org.technamin.assignment;

import org.junit.jupiter.api.Test;
import org.technamin.assignment.config.HttpClientConfig;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpClientConfigTest {
    @Test
    void shouldGetResponse() {
        HttpResponse<String> test = HttpClientConfig.INSTANCE.getResponse();
        assertNotEquals(null, test);
        assertTrue(test.body().contains("doc_id"));
    }
}
