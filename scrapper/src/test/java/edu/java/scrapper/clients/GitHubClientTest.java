package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.ScrapperApplication;
import edu.java.clients.GitHubClient;
import edu.java.dto.Repository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.server.ResponseStatusException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ScrapperApplication.class})
@WireMockTest
class GitHubClientTest {
    static final String BODY_REQUEST = "{\"name\": \"kishfish\"}";
    static final String URL = "/repos/ahaha/ohoho";
    static final String ERROR_404 = "404 NOT_FOUND \"Link is not valid\"";
    static final String ERROR_500 = "500 INTERNAL_SERVER_ERROR \"Internal Server Error\"";

    @Autowired
    GitHubClient gitHubClient;
    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().dynamicPort()).build();

    @DynamicPropertySource
    public static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("app.base-url.git-hub-base-url", wireMockExtension::baseUrl);
    }

    @Test
    @DisplayName("regular work")
    public void testRegWork() {
        wireMockExtension.stubFor(WireMock.get(WireMock.urlEqualTo(
            URL)
        ).willReturn(aResponse()
            .withBody(BODY_REQUEST)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        ));
        Repository rep = gitHubClient.getRep("ahaha", "ohoho");
        assertEquals("kishfish", rep.name());
    }

    @Test
    @DisplayName("link error")
    public void testLinkError() {
        wireMockExtension.stubFor(WireMock.get(WireMock.urlEqualTo(
            URL)
        ).willReturn(aResponse()
            .withBody(BODY_REQUEST)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        ));

        String message = "";
        try {
            gitHubClient.getRep("nothehe", "ohoho");
        } catch (ResponseStatusException e) {
            message = e.getMessage();
        }
        assertEquals(ERROR_404, message);
    }

    @Test
    @DisplayName("server error")
    public void testServerError() {
        wireMockExtension.stubFor(WireMock.get(WireMock.urlEqualTo(
            URL)
        ).willReturn(aResponse()
            .withBody(BODY_REQUEST).withStatus(500)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        ));

        String message = "";
        try {
            gitHubClient.getRep("ahaha", "ohoho");
        } catch (ResponseStatusException e) {
            message = e.getMessage();
        }
        assertEquals(ERROR_500, message);
    }
}
