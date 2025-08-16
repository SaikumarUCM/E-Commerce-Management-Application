package com.saikumar.microservice.product;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

import static org.hamcrest.Matchers.equalTo;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

    // - Declares and starts a **MongoDB Testcontainer** running the official MongoDB Docker image (`7.0.5`).
    // - `@ServiceConnection` tells Spring Boot 3.1+ to automatically **bind the container's URI** to `spring.data.mongodb.uri`.

    @LocalServerPort
    private Integer port;

    @ServiceConnection
    static MongoDBContainer mongoDBContainer= new MongoDBContainer("mongo:7.0.5");


    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        mongoDBContainer.start();
    }

    @Test
    void shouldCreateProduct() {
        String requestBody = """
                {
                    "name": "iPhone 15",
                    "description": "This is a Apple Product",
                    "price": 1000
                }
                """;
        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/products")
                .then()
                .statusCode(201)
                .body("name", Matchers.equalTo("iPhone 15"))
                .body("description", Matchers.equalTo("This is a Apple Product"))
                .body("price", equalTo(1000));
    }

    /* 🔄 Flow of Execution
    Spring Boot app starts on random port

    MongoDB container is started

    Spring connects to MongoDB container using spring.data.mongodb.uri

    POST /api/products request is made from the test using RestAssured

    Request hits your controller → service → repository → MongoDB

    The response is returned and assertions are made

    ✅ What This Confirms
    This integration test validates that:

    Your controller accepts valid JSON input

    Your service processes and stores the product correctly

    Your repository successfully interacts with MongoDB

    The final response has the correct data and status code
    This ensures that the entire flow from HTTP request to database interaction works as expected.
     */

}
