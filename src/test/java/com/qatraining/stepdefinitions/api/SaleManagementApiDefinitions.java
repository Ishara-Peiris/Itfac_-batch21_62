package com.qatraining.stepdefinitions.api;

import com.qatraining.abilities.CallTheApi;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.interactions.*;
import net.serenitybdd.screenplay.rest.questions.LastResponse;
import net.serenitybdd.screenplay.Actor;

import java.util.*;

import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SaleManagementApiDefinitions {

    Actor admin = Actor.named("Admin");
    Actor user = Actor.named("User");

    @Given("Admin user is authenticated")
    public void admin_authenticated() {

        admin.can(CallTheApi.at("http://localhost:8080"));

        admin.attemptsTo(
                Post.to("/api/auth/login")
                        .with(req -> req
                                .contentType("application/json")
                                .body("""
                        {
                          "username": "admin",
                          "password": "admin123"
                        }
                        """))
        );

        String token = SerenityRest.lastResponse().jsonPath().getString("token");

        admin.remember("authToken", token);
    }

    @When("Admin sells plant with id {int} quantity {int}")
    public void admin_sells_plant(int plantId, int quantity) {

        String token = admin.recall("authToken");

        admin.attemptsTo(
                Post.to("/api/sales/plant/{plantId}")
                        .with(request -> request
                                .header("Authorization", "Bearer " + token)
                                .pathParam("plantId", plantId)
                                .queryParam("quantity", quantity)
                        )
        );

        SerenityRest.lastResponse().prettyPrint();


    }

    @Then("Sale should be successful")
    public void verify_sale_success() {

        admin.should(
                seeThatResponse("Sale completed",
                        response -> response.statusCode(anyOf(is(200), is(201)))
                )
        );
    }

    @Then("Sale should fail due to insufficient stock")
    public void verify_sale_failed() {

        admin.should(
                seeThatResponse("Should return 400 error",
                        response -> response.statusCode(400)
                )
        );

        admin.should(
                seeThatResponse("Stock error message",
                        response -> response.body("message",
                                anyOf(
                                        containsString("only"),
                                        containsString("stock"),
                                        containsString("available")
                                )
                        )
                )
        );

    }

    @Then("Sale should fail because plant does not exist")
    public void verify_plant_not_found() {

        admin.should(
                seeThatResponse("Should return 404 Not Found",
                        response -> response.statusCode(404)
                )
        );

        admin.should(
                seeThatResponse("Plant not found message",
                        response -> response.body("message",
                                anyOf(
                                        containsString("not"),
                                        containsString("found"),
                                        containsString("Plant")
                                )
                        )
                )
        );
    }

    @Given("Non-admin user is authenticated")
    public void non_admin_authenticated() {



        user.can(CallTheApi.at("http://localhost:8080"));

        user.attemptsTo(
                Post.to("/api/auth/login")
                        .with(req -> req
                                .contentType("application/json")
                                .body("""
                    {
                      "username": "testuser",
                      "password": "test123"
                    }
                    """))
        );

        String token = LastResponse.received().answeredBy(user)
                .jsonPath().getString("token");

        user.remember("authToken", token);
    }

    @When("Non-admin tries to sell plant with id {int} quantity {int}")
    public void non_admin_sell_attempt(int plantId, int quantity) {


        String token = user.recall("authToken");

        user.attemptsTo(
                Post.to("/api/sales/plant/{plantId}")
                        .with(req -> req
                                .header("Authorization", "Bearer " + token)
                                .pathParam("plantId", plantId)
                                .queryParam("quantity", quantity)
                        )
        );

        SerenityRest.lastResponse().prettyPrint();
    }
    @Then("Sale should be forbidden")
    public void verify_forbidden() {

        user.should(
                seeThatResponse("Access denied",
                        response -> response.statusCode(403)
                )
        );
    }

    @When("Admin requests all sales")
    public void get_all_sales() {

        String token = admin.recall("authToken");

        admin.attemptsTo(
                Get.resource("/api/sales")
                        .with(req -> req.header("Authorization", "Bearer " + token))
        );

        SerenityRest.lastResponse().prettyPrint(); // debug
    }

    @Then("All sales should be returned")
    public void verify_all_sales_returned() {

        admin.should(
                seeThatResponse("Sales list returned",
                        response -> response.statusCode(200)
                )
        );

        admin.should(
                seeThatResponse("Response is a list",
                        response -> response.body("$", not(empty()))
                )
        );
    }

    @Given("Admin creates a sale with plant id {int} quantity {int}")
    public void create_sale(int plantId, int quantity) {

        String token = admin.recall("authToken");

        admin.attemptsTo(
                Post.to("/api/sales/plant/{plantId}")
                        .with(req -> req
                                .header("Authorization", "Bearer " + token)
                                .pathParam("plantId", plantId)
                                .queryParam("quantity", quantity)
                        )
        );

        Response response = SerenityRest.lastResponse();
        response.prettyPrint();

        // 🚨 CRITICAL CHECK
        response.then().statusCode(anyOf(is(200), is(201)));

        Integer saleId = response.jsonPath().getInt("id");
        admin.remember("saleId", saleId);
    }



    @When("Admin retrieves the created sale")
    public void admin_gets_created_sale() {

        String token = admin.recall("authToken");
        int saleId = admin.recall("saleId");

        admin.attemptsTo(
                Get.resource("/api/sales/{id}")
                        .with(req -> req
                                .header("Authorization", "Bearer " + token)
                                .pathParam("id", saleId)
                        )
        );

        SerenityRest.lastResponse().prettyPrint();
    }
    @Then("Correct sale details should be returned")
    public void verify_sale_details() {

        int saleId = admin.recall("saleId");

        admin.should(
                seeThatResponse("Status is 200",
                        res -> res.statusCode(200))
        );

        admin.should(
                seeThatResponse("Correct sale returned",
                        res -> res.body("id", equalTo(saleId)))
        );

        admin.should(
                seeThatResponse("Quantity exists",
                        res -> res.body("quantity", greaterThan(0)))
        );
    }
    @When("Admin retrieves sale with id {int}")
    public void admin_gets_sale_with_invalid_id(int saleId) {

        String token = admin.recall("authToken");

        admin.attemptsTo(
                Get.resource("/api/sales/{id}")
                        .with(req -> req
                                .header("Authorization", "Bearer " + token)
                                .pathParam("id", saleId)
                        )
        );

        SerenityRest.lastResponse().prettyPrint();
    }
    @Then("Sale should not be found")
    public void verify_sale_not_found() {

        admin.should(
                seeThatResponse("Status should be 404",
                        res -> res.statusCode(404))
        );

        admin.should(
                seeThatResponse("Error message present",
                        res -> res.body("message", containsString("not found")))
        );
    }
//delete sale succesfully
@When("Admin deletes the created sale")
public void admin_deletes_sale() {

    String token = admin.recall("authToken");
    int saleId = admin.recall("saleId");

    admin.attemptsTo(
            Delete.from("/api/sales/{id}")
                    .with(req -> req
                            .header("Authorization", "Bearer " + token)
                            .pathParam("id", saleId)
                    )
    );

    SerenityRest.lastResponse().prettyPrint();
}
    @Then("Sale should be deleted successfully")
    public void verify_sale_deleted() {

        admin.should(
                seeThatResponse("Status should be 204",
                        res -> res.statusCode(204))
        );

    }

    //try to delete Invalid sale
    @When("Admin deletes a sale with invalid ID")
    public void admin_deletes_sale_with_invalid_id() {

        String token = admin.recall("authToken");

        // Use a sale ID that does not exist (e.g., 999999)
        int invalidSaleId = 999999;

        admin.attemptsTo(
                Delete.from("/api/sales/{id}")
                        .with(req -> req
                                .header("Authorization", "Bearer " + token)
                                .pathParam("id", invalidSaleId)
                        )
        );

        // Optional: print response for debug
        SerenityRest.lastResponse().prettyPrint();
    }

    @Then("API should return 404 Not Found")
    public void sale_not_found() {

        admin.should(
                seeThatResponse("Sale not found",
                        res -> res.statusCode(404))
        );
    }

    @When("Admin retrieves sales page {int} size {int} sorted by id descending")
    public void get_sales_page(int page, int size) {

        String token = admin.recall("authToken");

        admin.attemptsTo(
                Get.resource("/api/sales/page")
                        .with(req -> req
                                .header("Authorization", "Bearer " + token)
                                .queryParam("page", page)
                                .queryParam("size", size)
                                .queryParam("sort", "id,desc")
                        )
        );

        SerenityRest.lastResponse().prettyPrint();
    }




    @Then("Paginated sales should be returned correctly")
    public void verify_sales_page() {

        admin.should(
                seeThatResponse("Status is 200",
                        res -> res.statusCode(200))
        );

        admin.should(
                seeThatResponse("Page number correct",
                        res -> res.body("number", equalTo(0)))
        );

        admin.should(
                seeThatResponse("Page size correct",
                        res -> res.body("size", equalTo(5)))
        );

        admin.should(
                seeThatResponse("Content exists",
                        res -> res.body("content", not(empty())))
        );

        // Verify descending order
        admin.should(
                seeThatResponse("Sorted by id desc",
                        res -> {
                            List<Integer> ids = res.extract().jsonPath().getList("content.id");
                            if (ids.size() > 1) {
                                assertTrue(ids.get(0) >= ids.get(1));
                            }
                        })
        );

    }

}

