package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class DemowebshopTests {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://demowebshop.tricentis.com";
        Configuration.startMaximized = true;
        Configuration.baseUrl = "http://demowebshop.tricentis.com";
    }

    @Test
    void addItemToCartAsNewUserTest() {
        Response response = given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body("product_attribute_72_5_18=53&product_attribute_72_6_19=54&product_attribute_72_3_20=57&addtocart_72.EnteredQuantity=1")
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .statusCode(200)
                .log().body()
                .body("success", is(true))
                .body("updatetopcartsectionhtml", is("(1)"))
                .extract().response();

        String cartSizeString = response.jsonPath().get("updatetopcartsectionhtml");
        int cartSize = Integer.parseInt(cartSizeString.substring(1, cartSizeString.length() - 1));
    }

    @Test
    void addItemToCartAsExistUserTest() {
        Response response = given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body("product_attribute_72_5_18=53&product_attribute_72_6_19=54&product_attribute_72_3_20=57&addtocart_72.EnteredQuantity=1")
                .cookie("__utmz=78382081.1619120185.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); Nop.customer=c14e82b1-dc94-4970-8ae1-002c0ecc621e; ARRAffinity=06e3c6706bb7098b5c9133287f2a8d510a64170f97e4ff5fa919999d67a34a46; __utmc=78382081; __utma=78382081.1370846760.1619120185.1621451734.1621455673.9; __utmt=1; NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=72&RecentlyViewedProductIds=16; __atuvc=3%7C20; __atuvs=60a5734a3a3dff83001; __utmb=78382081.6.10.1621455673")
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .statusCode(200)
                .log().body()
                .body("success", is(true))
                .extract().response();

        String cartSizeString = response.jsonPath().get("updatetopcartsectionhtml");
        int cartSize = Integer.parseInt(cartSizeString.substring(1, cartSizeString.length() - 1));

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body("product_attribute_72_5_18=53&product_attribute_72_6_19=54&product_attribute_72_3_20=57&addtocart_72.EnteredQuantity=1")
                .cookie("__utmz=78382081.1619120185.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); Nop.customer=c14e82b1-dc94-4970-8ae1-002c0ecc621e; ARRAffinity=06e3c6706bb7098b5c9133287f2a8d510a64170f97e4ff5fa919999d67a34a46; __utmc=78382081; __utma=78382081.1370846760.1619120185.1621451734.1621455673.9; __utmt=1; NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=72&RecentlyViewedProductIds=16; __atuvc=3%7C20; __atuvs=60a5734a3a3dff83001; __utmb=78382081.6.10.1621455673")
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .statusCode(200)
                .log().body()
                .body("success", is(true))
                .body("updatetopcartsectionhtml", is("(" + (cartSize + 1) + ")"));
    }


    @Test
    void addItemToCartAsExistUserTestAndUiCheck() {
        open("/build-your-cheap-own-computer");
        $("input[type=\"button\"]#add-to-cart-button-72").click();
        $("#topcartlink a[href='/cart']").shouldHave(Condition.text("(1)"));

        String nopCustomerCookie = WebDriverRunner.getWebDriver().manage().getCookieNamed("Nop.customer").getValue();
        System.out.println("Nop.customer = " + nopCustomerCookie);

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body("product_attribute_72_5_18=53&product_attribute_72_6_19=54&product_attribute_72_3_20=57&addtocart_72.EnteredQuantity=1")
                .cookie("Nop.customer", nopCustomerCookie)
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .statusCode(200)
                .log().body()
                .body("success", is(true))
                .body("updatetopcartsectionhtml", is("(2)"));

    }

}
