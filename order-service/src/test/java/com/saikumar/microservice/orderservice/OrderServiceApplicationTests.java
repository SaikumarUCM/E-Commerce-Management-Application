package com.saikumar.microservice.orderservice;

import com.saikumar.microservice.orderservice.client.InventoryClient;
import com.saikumar.microservice.orderservice.stubs.InventoryClientStub;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;
import org.hamcrest.Matchers;


@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock
class OrderServiceApplicationTests {

    @ServiceConnection
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.34");

    @LocalServerPort
    private Integer port;


    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }



    @Test
    void shouldSubmitOrder() {
        String requestBody = """
                {
                    "skuCode":"iPhone_15",
                    "quantity": 1,
                    "price":1000
                }
                """;

        InventoryClientStub.stubInventoryCall("iPhone_15", 1);

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/order")
                .then()
                .statusCode(201)
                .body("skuCode", Matchers.equalTo("iPhone_15"))
                .body("quantity", Matchers.equalTo(1))
                .body("price", Matchers.equalTo(1000));
    }

}
