package autotest.pages;


import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class SearchResultsPage {


    public ElementsCollection
        searchResults = $$(By.xpath(".//div[contains(@class,'flights-list__item')]")),
        chooseBtn = $$(By.xpath(".//button[contains(text(),'Выбрать')]"));

//    private SelenideElement
//        chooseBtn = $(By.xpath(""));

    public List<String> getIdOfSearchResults(){
        searchResults.shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));
        List<String> ids = new ArrayList<>();
        searchResults.forEach(s -> {
            ids.add(s.getAttribute("id"));
        });
        return ids;
    }

    @Step("Проверим наличие логотипа компании-перевозчика в результатах поиска")
    public void checkCompanyPresence(String id, int expCount){
        String xPath = String.format(".//*[@id='%s'] //img[contains(@title, 'Supplier')]", id);
        $$(By.xpath(xPath)).shouldHaveSize(expCount);
    }

    @Step("Проверим наличие города вылета для маршрута 'туда' в результатах поиска")
    public void checkDepartureCityNameForward(String id, String cityName){
        String xPath = String.format("(.//*[@id='%s'] //div[text()='вылет']/following-sibling::div[contains(@class,'flight__way__city')])[1]", id);
        $(By.xpath(xPath)).shouldBe(visible).shouldHave(text(cityName));
    }

    @Step("Проверим наличие города прилета для маршрута 'туда' в результатах поиска")
    public void checkArrivalCityNameForward(String id, String cityName){
        String xPath = String.format("(.//*[@id='%s'] //div[text()='прилет']/following-sibling::div[contains(@class,'flight__way__city')])[1]", id);
        $(By.xpath(xPath)).shouldBe(visible).shouldHave(text(cityName));
    }

    @Step("Проверим наличие города вылета для маршрута 'обратно' в результатах поиска")
    public void checkDepartureCityNameBackward(String id, String cityName){
        String xPath = String.format("(.//*[@id='%s'] //div[text()='вылет']/following-sibling::div[contains(@class,'flight__way__city')])[2]", id);
        $(By.xpath(xPath)).shouldBe(visible).shouldHave(text(cityName));
    }

    @Step("Проверим наличие города прилета для маршрута 'обратно' в результатах поиска")
    public void checkArrivalCityNameBackward(String id, String cityName){
        String xPath = String.format("(.//*[@id='%s'] //div[text()='прилет']/following-sibling::div[contains(@class,'flight__way__city')])[2]", id);
        $(By.xpath(xPath)).shouldBe(visible).shouldHave(text(cityName));
    }


    @Step("Проверим дату вылета для маршрута 'туда' в результатах поиска")
    public void checkDepartureDateForward(String id, String date){
        String xPathDate = String.format("(.//*[@id='%s'] //span[@data-ng-bind='departure.flightDate'])[1]", id);

        $(By.xpath(xPathDate)).shouldBe(visible).shouldHave(text(date));

    }

    @Step("Проверим дату прилета для маршрута 'туда' в результатах поиска")
    public void checkArrivalDateForward(String id, String date1, String date2){
        String xPathDate = String.format("(.//*[@id='%s'] //span[@data-ng-bind='arrival.flightDate'])[1]", id);
        String actText = $(By.xpath(xPathDate)).getText();
        Assert.assertTrue(date1.equalsIgnoreCase(actText) || date2.equalsIgnoreCase(actText),
                "Некорректно отображается дата прилета");
    }

    @Step("Проверим дату вылета для маршрута 'обратно' в результатах поиска")
    public void checkDepartureDateBackward(String id, String date){
        String xPathDate = String.format("(.//*[@id='%s'] //span[@data-ng-bind='departure.flightDate'])[2]", id);
        $(By.xpath(xPathDate)).shouldBe(visible).shouldHave(text(date));
    }

    @Step("Проверим дату прилета для маршрута 'обратно' в результатах поиска")
    public void checkArrivalDateBackward(String id, String date1, String date2){
        String xPathDate = String.format("(.//*[@id='%s'] //span[@data-ng-bind='arrival.flightDate'])[2]", id);
        String actText = $(By.xpath(xPathDate)).getText();
        Assert.assertTrue(date1.equalsIgnoreCase(actText) || date2.equalsIgnoreCase(actText),
                "Некорректно отображается дата прилета");
    }

    @Step("Проверим отображение времени вылета для маршрута 'туда' в результатах поиска")
    public void checkPresenceOfDepartureTimeForward(String id, String regex){
        String xPathTime = String.format("(.//*[@id='%s'] //div[@data-ng-bind='departure.flightTime'])[1]", id);
        Assert.assertTrue($(By.xpath(xPathTime)).shouldBe(visible).getText().matches(regex));
    }

    @Step("Проверим отображение времени прилета для маршрута 'туда' в результатах поиска")
    public void checkPresenceOfArrivalTimeForward(String id, String regex){
        String xPathTime = String.format("(.//*[@id='%s'] //div[@data-ng-bind='arrival.flightTime'])[1]", id);
        Assert.assertTrue($(By.xpath(xPathTime)).shouldBe(visible).getText().matches(regex));
    }

    @Step("Проверим отображение времени вылета для маршрута 'обратно' в результатах поиска")
    public void checkPresenceOfDepartureTimeBackward(String id, String regex){
        String xPathTime = String.format("(.//*[@id='%s'] //div[@data-ng-bind='departure.flightTime'])[2]", id);
        Assert.assertTrue($(By.xpath(xPathTime)).shouldBe(visible).getText().matches(regex));
    }

    @Step("Проверим отображение времени прилета для маршрута 'обратно' в результатах поиска")
    public void checkPresenceOfArrivalTimeBackward(String id, String regex){
        String xPathTime = String.format("(.//*[@id='%s'] //div[@data-ng-bind='arrival.flightTime'])[2]", id);
        Assert.assertTrue($(By.xpath(xPathTime)).shouldBe(visible).getText().matches(regex));
    }

}
