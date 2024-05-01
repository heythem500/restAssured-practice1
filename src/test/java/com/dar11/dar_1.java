package com.dar11;

//import io.restassured.RestAssured; // old "without a shortcut"
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class dar_1 {

    @Test
    public void darTest() {
//        RestAssured.given().when().then(); // old "without a shortcut"
        given().baseUri("https://660c82ed3a0766e85dbe3af2.mockapi.io/api/v1")
                .when().get("/members")
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("[0].name", equalTo("Alan Blanda"))
//                .assertThat().body("[0].name", is(equalTo("Alan Blanda"))); // same thing but is more readable in english
                .assertThat().body("name", hasItem("Dr. Lisa Hartmann MD"))
//                .assertThat().body("name", hasItem("hythhhm")); // a wrong name not in our data will give [JSON path name doesn't match]
                .assertThat().body("[7].city", equalTo("Schaumburg"), "city", hasItem("Palo Alto")) // we can add two together with no repeat
                .assertThat().body("name", hasItems("Rachael Fadel", "Sylvia Kuvalis", "Ralph Johns"))
                .assertThat().body("name", not(hasItem("khaled"))) //check if this name not in our data
//                .assertThat().body("name", contains("Alan Blanda", "Anna Koch")) // will fail of course..must write all names not just 2 + in the right order
//                .assertThat().body("name", containsInAnyOrder("Alan Blanda", "Anna Koch")) // same but order here not important
//                .assertThat().body("name", empty()) //check if there's nothing, if it has data it should fail
                .assertThat().body("name", not(empty()))
//                .assertThat().body("name", is(not(empty()))); // same but more beautiful, I see it ugly though
                .assertThat().body("name", hasSize(28)) // how many names inside your data
//                .assertThat().body("name.size()", equalTo(28)); //no need but give exactly the same thing, I dislike it.
                .assertThat().body("createdAt", everyItem(startsWith("2024-04"))) //the first words[value] of this key: start with [has]
                .assertThat().body("[0]", hasKey("avatar"))
                .assertThat().body("[4]", hasValue("DeKalb"))
                .assertThat().body("[4]", hasEntry("city", "DeKalb"));
    }

    @Test
    public void darTest2() {
        Response res = given().baseUri("https://660c82ed3a0766e85dbe3af2.mockapi.io")
                .when().get("/api/v1/members")
                .then().extract().response();
        String nameFirstMember = res.path("[0].name"); //if we want to get the first name only
        String nameSecondMember = JsonPath.from(res.asString()).getString("[1].name"); // same but alternative way
        System.out.println(res.asString()); //by using asString we convert response to string "all data" to see it on console
        System.out.println(nameFirstMember);
        System.out.println(nameSecondMember);

        //third way to get it directly
        String xxx5 = given().baseUri("https://660c82ed3a0766e85dbe3af2.mockapi.io")
                .when().get("/api/v1/members")
                .then().extract().response().path("[3].name");
        System.out.println("third way..."+ xxx5);

    }

    @Test
    public void darTest3_logs() {
//        given().baseUri("https://660c82ed3a0766e85dbe3af2.mockapi.io/api/v1/members").log().all() //will show shortcut list
//        given().baseUri("https://660c82ed3a0766e85dbe3af2.mockapi.io/api/v1/members").log().headers() //show one the shortcut list only
//        given().baseUri("https://660c82ed3a0766e85dbe3af2.mockapi.io/api/v1/members").log().method()
        given().baseUri("https://660c82ed3a0766e85dbe3af2.mockapi.io/api/v1/members")
//        given().baseUri("https://6.mockapi.io/api/v1/members") // let's test a wrong Url this time

                .when().get("/")
//                .then();
//                .then().log().status();
//                .then().log().body(); //long detials name avatar city etc
//                .then().log().headers(); // with much more detials compared to hearders in baseUrl
//                .then().log().ifError(); //show us the data only "in the console" if there's an error
                .then().log().ifValidationFails()
                .body("[0].name", equalTo("Alan Blanda")); //+we added assertion related to ifValidationFails() .. won't print
//                  .body("[0].name", equalTo("Alan2")); // same but a wrong name if we need to print
    }




}
