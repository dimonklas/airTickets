package autotest.pages;


import autotest.utils.Utils;
import autotest.utils.exception.NotClickedException;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class SearchResultsPage {


    private ElementsCollection
        searchResults = $$(By.xpath(".//div[contains(@class,'flights-list__item')]")),
        chooseBtn = $$(By.xpath(".//button[contains(text(),'Выбрать')]"));

    private SelenideElement
        loadMoreResultsBtn = $x(".//button[text()='Загрузить больше результатов']");

    private static int step;

    public List<String> getIdOfSearchResults(){
        searchResults.shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1).because("Не найден ни один варианта перелета"));
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

    @Step("Проверим отображение названия аэропорта отправки для маршрута 'туда' в результатах поиска")
    public void checkDepartureAitportNameForward(String id){
        String xPath = String.format("(.//*[@id='%s']//div[@data-ng-bind='departure.airport.name'])[1]", id);
        Assert.assertTrue(!$(By.xpath(xPath)).shouldBe(visible).getText().isEmpty());
    }

    @Step("Проверим отображение названия аэропорта прибытия для маршрута 'туда' в результатах поиска")
    public void checkArrivalAitportNameForward(String id){
        String xPath = String.format("(.//*[@id='%s']//div[@data-ng-bind='departure.airport.name'])[1]", id);
        Assert.assertTrue(!$(By.xpath(xPath)).shouldBe(visible).getText().isEmpty());
    }

    @Step("Проверим отображение названия аэропорта отправки для маршрута 'обратно' в результатах поиска")
    public void checkDepartureAitportNameBackward(String id){
        String xPath = String.format("(.//*[@id='%s']//div[@data-ng-bind='departure.airport.name'])[2]", id);
        Assert.assertTrue(!$(By.xpath(xPath)).shouldBe(visible).getText().isEmpty());
    }

    @Step("Проверим отображение названия аэропорта прибытия для маршрута 'обратно' в результатах поиска")
    public void checkArrivalAitportNameBackward(String id){
        String xPath = String.format("(.//*[@id='%s']//div[@data-ng-bind='departure.airport.name'])[2]", id);
        Assert.assertTrue(!$(By.xpath(xPath)).shouldBe(visible).getText().isEmpty());
    }


    @Step("Проверим дату вылета для маршрута 'туда' в результатах поиска")
    public void checkDepartureDateForward(String id, int daysFromToday){
        String xPathDate = String.format("(.//*[@id='%s'] //span[@data-ng-bind='departure.flightDate'])[1]", id);
        $(By.xpath(xPathDate)).shouldBe(visible).shouldHave(
                text(Utils.getDateForFlightSearchResults(daysFromToday))
                        .because("Некорректно отображается дата вылета для маршрута 'туда' в результатах поиска"));
    }

    @Step("Проверим дату прилета для маршрута 'туда' в результатах поиска")
    public void checkArrivalDateForward(String id, int daysFromToday){
        String xPathDate = String.format("(.//*[@id='%s'] //span[@data-ng-bind='arrival.flightDate'])[1]", id);
        String actText = $(By.xpath(xPathDate)).getText();
        Assert.assertTrue( Utils.getDateForFlightSearchResults(daysFromToday).equalsIgnoreCase(actText) ||
                           Utils.getDateForFlightSearchResults(daysFromToday + 1).equalsIgnoreCase(actText),
                           "Некорректно отображается дата прилета для маршрута 'туда' в результатах поиска"
        );
    }

    @Step("Проверим дату вылета для маршрута 'обратно' в результатах поиска")
    public void checkDepartureDateBackward(String id, int daysFromToday){
        String xPathDate = String.format("(.//*[@id='%s'] //span[@data-ng-bind='departure.flightDate'])[2]", id);
        $(By.xpath(xPathDate)).shouldBe(visible).shouldHave(
                text(Utils.getDateForFlightSearchResults(daysFromToday))
                        .because("Некорректно отображается дата вылета для маршрута 'обратно' в результатах поиска"));
    }

    //так как дата прилета может быть на след. день после вылета, проверяются 2 даты: вылета и вылета + 1 день
    @Step("Проверим дату прилета для маршрута 'обратно' в результатах поиска")
    public void checkArrivalDateBackward(String id, int daysFromToday){
        String xPathDate = String.format("(.//*[@id='%s'] //span[@data-ng-bind='arrival.flightDate'])[2]", id);
        String actText = $(By.xpath(xPathDate)).shouldBe(visible).getText();
        Assert.assertTrue( Utils.getDateForFlightSearchResults(daysFromToday).equalsIgnoreCase(actText) ||
                           Utils.getDateForFlightSearchResults(daysFromToday + 1).equalsIgnoreCase(actText),
                           "Некорректно отображается дата прилета для маршрута 'обратно' в результатах поиска"
        );
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

    @Step("Проверим отображение времени полета (в пути) для маршрута 'туда' в результатах поиска")
    public void checkPresenceOfFlyingTimeForward(String id, String regex){
        String xPathTime = String.format("(.//*[@id='%s']//span[text()='в пути']/following-sibling::span)[1]", id);
        Assert.assertTrue($(By.xpath(xPathTime)).shouldBe(visible).getText().matches(regex));
    }

    @Step("Проверим отображение времени полета (в пути) для маршрута 'обратно' в результатах поиска")
    public void checkPresenceOfFlyingTimeBackward(String id, String regex){
        String xPathTime = String.format("(.//*[@id='%s']//span[text()='в пути']/following-sibling::span)[2]", id);
        Assert.assertTrue($(By.xpath(xPathTime)).shouldBe(visible).getText().matches(regex));
    }


    @Step("Проверим отображение стоимости билета")
    public String checkPresenceOfTicketsCost(String id){
        String xPath = String.format(".//*[@id='%s']//*[contains(text(),'Стоимость')]", id);
        $(By.xpath(xPath)).shouldBe(visible);
        String xPathPrice = String.format(".//*[@id='%s']//*[@data-ng-bind='ticket.amount.UAHFormat']", id);
        String price = $(By.xpath(xPathPrice)).shouldBe(visible).getText();
        Assert.assertFalse(price.isEmpty(), "Не отобразилась стоимость билета");
        return price.trim();
    }


    @Step("Нажимаем кнопку 'Выбрать'")
    public void pressSelectButton(String id){
        String xPath = String.format(".//*[@id='%s'] //button[contains(text(),'Выбрать')]", id);
        $x(xPath).shouldBe(visible, enabled).click();
        try{
            $x(xPath).waitUntil(not(visible), 5 * 1000);
        } catch (Error er) {
            if ($x(xPath).isDisplayed()) executeJavaScript("arguments[0].click();", $x(xPath));
            $x(xPath).waitUntil(not(visible), 5 * 1000);
            if ($x(xPath).isDisplayed()) throw new NotClickedException("Не нажалась кнопка 'Выбрать'");
        }
    }


    @Step("Проверим наличие таблицы с результатами поиска")
    public void checkMatrixFlightsPresence(){
        $(".flights-table-range").shouldBe(visible).scrollIntoView(true);
    }


    @Step("Проверим, что выбранные даты и цены соответствуют данным в билетах (для всех результатов)")
    public void checkResultsForPlusMinus3Days(int daysFwdSearch, String depCity, String arrCity){
        String xpathBase = ".//*[text()='%s']/following::*[text()='%s']/ancestor::*[@*[starts-with(.,'forwPeriod')]] " +
                "//*[contains(@data-ng-bind,'amount.UAH')][text()!='']/..";

        List<String> days = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        for(int i = daysFwdSearch-3; i <= daysFwdSearch+3; ++i) {
            days.add(Utils.dateFormatted("E", i));
            dates.add(Utils.dateFormatted("dd.MM", i));
        }

        for(int i = 0; i < 7; ++i){
            String day = days.get(i);
            String date = dates.get(i);
            String xPath_column = String.format(xpathBase, day, date);
            int columnSize = $$x(xPath_column).shouldHave(CollectionCondition.sizeGreaterThanOrEqual(0)).size();
            step = 0;
            int dayFwdFlight = daysFwdSearch - 3 + i;
            int dayBcwdFlight = daysFwdSearch - 3 + (7 - columnSize);
            //Перебираем все заполненные клетки матрицы и проверяем отображаемые данные
            $$x(xPath_column).forEach(element -> {
                    //Кликаем по клетке матрицы
                    element.shouldBe(visible).click();
                    element.click();
                    String id = getIdOfSearchResults().get(0);

                    checkDepartureCityNameForward(id, depCity);
                    checkArrivalCityNameForward(id, arrCity);
                    checkDepartureCityNameBackward(id, arrCity);
                    checkArrivalCityNameBackward(id, depCity);
                    checkCompanyPresence(id, 2);

                    checkDepartureAitportNameForward(id);
                    checkArrivalAitportNameForward(id);
                    checkDepartureAitportNameBackward(id);
                    checkArrivalAitportNameBackward(id);

                    checkDepartureDateForward(id, dayFwdFlight);
                    checkArrivalDateForward(id, dayFwdFlight);
                    checkDepartureDateBackward(id, dayBcwdFlight + step);
                    checkArrivalDateBackward(id, dayBcwdFlight + step);
                    ++step;

                    String regex = "[0-9]{1,2}:[0-9]{2}";
                    checkPresenceOfDepartureTimeForward(id, regex);
                    checkPresenceOfArrivalTimeForward(id, regex);
                    checkPresenceOfDepartureTimeBackward(id, regex);
                    checkPresenceOfArrivalTimeBackward(id, regex);

                    regex = "[0-9ч]{2,3}[0-9м\\s]{0,4}";
                    checkPresenceOfFlyingTimeForward(id, regex);
                    checkPresenceOfFlyingTimeBackward(id, regex);

                    String price = $x("//*[text()='Стоимость:']/following-sibling::*").getText().trim();
                    String priceMatrix = element.getText().trim();

                    Assert.assertEquals(price, priceMatrix, "Цена в результирующей таблице поиска '+/-3 дня' и в билете не совпадает");
                    //Проверка доступности кнопки 'Загрузить больше результатов' для +/-3 дня от точной даты поиска
                    if(getIdOfSearchResults().size() < 10) {
                        loadMoreResultsBtn.shouldBe(exist.because("Кнопка 'Загрузить больше результатов' отсутствует в результирующей таблице поиска " +
                                "при количестве рейсов < 10. Выведено рейсов: " + getIdOfSearchResults().size()));
                    }
            });
        }



    }
}
