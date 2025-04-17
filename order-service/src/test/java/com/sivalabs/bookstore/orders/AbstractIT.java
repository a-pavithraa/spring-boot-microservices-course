package com.sivalabs.bookstore.orders;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.wiremock.integrations.testcontainers.WireMockContainer;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(ContainersConfig.class)
@AutoConfigureMockMvc
public abstract class AbstractIT {
    static final String CLIENT_ID = "bookstore-webapp";
    static final String CLIENT_SECRET = "P1sibsIrELBhmvK18BOzw1bUl96DcP2z";
    static final String USERNAME = "siva";
    static final String PASSWORD = "siva1234";

    @Autowired
    OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    @LocalServerPort
    int port;

    @Autowired
    protected MockMvc mockMvc;

    static WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:latest");

    @BeforeAll
    static void beforeAll() {
        wiremockServer.start();
        configureFor(wiremockServer.getHost(), wiremockServer.getPort());
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("orders.catalog-service-url", wiremockServer::getBaseUrl);
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected static void mockGetProductByCode(String code, String name, BigDecimal price) {
        stubFor(WireMock.get(urlMatching("/api/products/" + code))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(
                                """
                    {
                        "code": "%s",
                        "name": "%s",
                        "price": %f
                    }
                """
                                        .formatted(code, name, price.doubleValue()))));
    }
}
