package autotest.pages;

import autotest.utils.ConfigurationVariables;
import autotest.utils.exception.CityAutocompleteException;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.By;
import org.testng.Assert;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

@Log4j
public class SearchPage {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();

    public SelenideElement
           preloader = $(By.xpath(".//*[@alt='loader-plane']"));

    private SelenideElement
            fromField = $(By.name("departure")),
            toField = $(By.name("arrival")),
            addRouteBtn = $x(".//*[text()='Добавить рейс']/parent::button"),
            submitSearchBtn = $(By.xpath(".//*[@name='PlaneSearchForm'] //button[contains(text(),'Найти')]"));

    private ElementsCollection
            departureField = $$x(".//*[@name='difficult_departure']"),
            arrivalField = $$x(".//*[@name='difficult_arrival']"),
            citiesListBoxDeparture = $$x(".//*[@name='difficult_departure']/following-sibling::*[contains(@id,'typeahead')]"),
            citiesListBoxArrival = $$x(".//*[@name='difficult_arrival']/following-sibling::*[contains(@id,'typeahead')]");


    @Step("Выберем направление '{waysType}'")
    public void selectWaysForTicket(String waysType){
        $(".search-block").shouldBe(visible).scrollIntoView(true);
        String xPath = String.format(".//label[contains(text(),'%s')]", waysType);
        $(By.xpath(xPath)).shouldBe(visible, enabled).click();
    }

    @Step("Отметим чек-бокс '+/-3 дня'")
    public void selectPlusMinus3Days(boolean isNeedCheck){
        Boolean isChecked = executeJavaScript("return angular.element(document.getElementsByClassName('pb-checkbox'))[0].firstElementChild.checked");
        if(isNeedCheck && !isChecked) {
            $x(".//*[contains(text(),'+/-3')]").shouldBe(visible, enabled).click();
        } else if (!isNeedCheck && isChecked) {
            $x(".//*[contains(text(),'+/-3')]").shouldBe(visible, enabled).click();
        }
    }

    @Step("Выберем класс '{classType}'")
    public void selectClass(String classType){
        String xPath = String.format(".//label[contains(text(),'%s')]", classType);
        $(By.xpath(xPath)).shouldBe(visible, enabled).click();
    }

    @Step("Выберем место вылета {city}")
    public void setDepartureCity(String city){
        if (city.equals("Лондон")) {
            $x("(//*[@class='grid-col-10']//*[text()='Лондон'])[1]").click();
            $x(".//*[@name='departure']/following-sibling::*[contains(@id,'typeahead')]").waitUntil(disappear, 5*1000);

        } else {
            fromField.shouldBe(enabled).setValue(city);
            $x(".//*[@name='departure']/following-sibling::*[contains(@id,'typeahead')]").waitUntil(disappear, 5*1000);
        }
    }

    @Step("Выберем место вылета обратно {city}")
    public void setArrivalCity(String city){
        toField.shouldBe(enabled).setValue(city);
        $x(".//*[@name='arrival']/following-sibling::*[contains(@id,'typeahead')]").waitUntil(disappear, 5*1000);
    }

    @Step("Выберем города вылета/назначения для сложного маршрута")
    public void setDifficultRouteCities(String departure, String arrival, String departure2, String arrival2,
                                        String departure3, String arrival3, String departure4, String arrival4){

        while ($$x(".//*[@name='difficult_departure']").size() > 2) {
            $x("(.//*[contains(@class,'difficult-flight__remove')])[last()]").shouldBe(visible, enabled).click();
        }

        departureField.shouldHaveSize(2).get(0).shouldBe(visible, enabled).setValue(departure);
        citiesListBoxDeparture.shouldHaveSize(2).get(0).waitUntil(disappear, 5*1000);
        arrivalField.shouldHaveSize(2).get(0).shouldBe(visible, enabled).setValue(arrival);
        citiesListBoxArrival.shouldHaveSize(2).get(0).waitUntil(disappear, 5*1000);

        departureField.get(1).shouldBe(visible, enabled).setValue(departure2);
        citiesListBoxDeparture.get(1).waitUntil(disappear, 5*1000);
        arrivalField.get(1).shouldBe(visible, enabled).setValue(arrival2);
        citiesListBoxArrival.get(1).waitUntil(disappear, 5*1000);

        if(departure3 != null) {
            addRouteBtn.shouldBe(visible, enabled).click();
            sleep(200);
            if (departureField.size() < 3) addRouteBtn.click();
            departureField.shouldHaveSize(3).get(2).shouldBe(visible, enabled).setValue(departure3);
            citiesListBoxDeparture.shouldHaveSize(3).get(2).waitUntil(disappear, 5*1000);
            arrivalField.shouldHaveSize(3).get(2).shouldBe(visible, enabled).setValue(arrival3);
            citiesListBoxArrival.shouldHaveSize(3).get(2).waitUntil(disappear, 5*1000);
        }

        if(departure4 != null) {
            addRouteBtn.click();
            sleep(200);
            if (departureField.size() < 4) addRouteBtn.click();
            departureField.shouldHaveSize(4).get(3).shouldBe(visible, enabled).setValue(departure4);
            citiesListBoxDeparture.shouldHaveSize(4).get(3).waitUntil(disappear, 5*1000);
            arrivalField.shouldHaveSize(4).get(3).shouldBe(visible, enabled).setValue(arrival4);
            citiesListBoxArrival.shouldHaveSize(4).get(3).waitUntil(disappear, 5*1000);
        }
    }

    @Step("Удалим последний добавленный маршрут и проверим, что строка поиска пропала")
    public void removeLastDifficultRoute(){
        $x("(.//*[contains(@class,'difficult-flight__remove')])[last()]").shouldBe(visible, enabled).click();
        $(By.xpath("(.//*[@name='difficult_departure'])[4]")).should(disappear);
        $(By.xpath("(.//*[@name='difficult_arrival'])[4]")).should(disappear);
    }

    @Step("Установим дату вылета {daysFromToday} дней от сегодня")
    public void setFirstDate(int daysFromToday){
        String jsCode = String.format("angular.element(document.querySelector(\"[ng-model='rangeStart']\")).scope()" +
                ".dates[0]=new Date().setDate(new Date().getDate() + %s);", daysFromToday);
        executeJavaScript(jsCode);
    }

    @Step("Установим дату вылета обратно {daysFromToday} дней от сегодня")
    public void setSecondDate(int daysFromToday){
        if (daysFromToday != -1) {
            String jsCode = String.format("angular.element(document.querySelector(\"[ng-model='rangeStart']\")).scope()" +
                    ".dates[1]=new Date().setDate(new Date().getDate() + %s);", daysFromToday);
            executeJavaScript(jsCode);
        }
    }


    @Step("Установим даты вылета для сложного маршрута")
    public void setDatesForDifficultRoute(int daysFromToday, int daysFromToday2, int daysFromToday3, int daysFromToday4){
        String jsCode = String.format("angular.element(document.getElementsByName('difficult_date')[0]).scope()" +
                ".flight.date=new Date().setDate(new Date().getDate() + %s);", daysFromToday);
        executeJavaScript(jsCode);

        jsCode = String.format("angular.element(document.getElementsByName('difficult_date')[1]).scope()" +
                ".flight.date=new Date().setDate(new Date().getDate() + %s);", daysFromToday2);
        executeJavaScript(jsCode);

        if (daysFromToday3 != -1) {
            jsCode = String.format("angular.element(document.getElementsByName('difficult_date')[2]).scope()" +
                    ".flight.date=new Date().setDate(new Date().getDate() + %s);", daysFromToday3);
            executeJavaScript(jsCode);
        }

        if (daysFromToday4 != -1) {
            jsCode = String.format("angular.element(document.getElementsByName('difficult_date')[3]).scope()" +
                    ".flight.date=new Date().setDate(new Date().getDate() + %s);", daysFromToday4);
            executeJavaScript(jsCode);
        }

    }


    @Step("Установим количество пассажиров")
    public void setPassengersCount(int adult, int child, int infant){
        int totalCount = adult + child + infant;

        $x(".//*[@data-pc-models='vm.models.passengers']").shouldBe(visible).click();

        setAdultsCount(adult);
        setChildrenCount(child);
        setInfantCount(infant);
        // костыль для ожидания исчезновения надиси под полем (чтобы работал для позитивного и негативного сценариев)
        try {
            $x("//*[@id='search-point-arrival']//..//..//*[contains(text(),'выпадающего списка')]").waitUntil(disappear, 6 * 1000);
        } catch (Throwable e) {
            log.info("Выберите город из выпадающего списка");
        }
        Assert.assertTrue($x(".//*[@data-pc-models='vm.models.passengers']//input").getValue().contains(String.valueOf(totalCount)),
                "Неправильно установили количество пассажиров");
    }


    @Step("Установим количество пассажиров")
    public void setPassengersCountForDifficultRoute(int adult, int child, int infant){
        int totalCount = adult + child + infant;

        $x("(.//*[@data-pc-models='vm.models.passengers'])[2]").shouldBe(visible).click();

        setAdultsCount(adult);
        setChildrenCount(child);
        setInfantCount(infant);
        Assert.assertTrue(
                $x("(.//*[@data-pc-models='vm.models.passengers']//input)[2]")
                        .getValue()
                        .contains(String.valueOf(totalCount)),
                "Неправильно установили количество пассажиров");
    }

    @Step("Установим кол-во взрослых: {adultsCount}")
    private void setAdultsCount(int adultsCount) {
       int actCount = Integer.parseInt($x(".//input[@data-ng-model='models.adt']").shouldBe(visible).getValue());

       if(actCount != adultsCount) {
           SelenideElement
           addCount = $x(".//input[@data-ng-model='models.adt']/../div[contains(@data-ng-click,'addPassenger')]").shouldBe(enabled, visible),
           removeCount = $x(".//input[@data-ng-model='models.adt']/../div[contains(@data-ng-click,'removePassenger')]").shouldBe(enabled, visible);
           if (actCount < adultsCount) {
               for (int i = 0; i < (adultsCount - actCount); ++i) {
               addCount.click();
               sleep(500);
               }
           } else {
               for (int i = actCount; i >= (actCount - adultsCount); i--) {
                   removeCount.click();
                   sleep(500);
               }
           }
       }
    }


    @Step("Установим кол-во детей: {childCount}")
    private void setChildrenCount(int childCount) {
        String xPathBase = ".//input[@data-ng-model='models.chd']";
        int actCount = Integer.parseInt($x(xPathBase).shouldBe(visible).getValue());
        setPassCount(actCount, childCount, xPathBase);
    }

    @Step("Установим кол-во младенцев: {infantCount}")
    private void setInfantCount(int infantCount){
        String xPathBase = ".//input[@data-ng-model='models.inf']";
        int actCount = Integer.parseInt($x(xPathBase).shouldBe(visible).getValue());
        setPassCount(actCount, infantCount, xPathBase);
    }

    private void setPassCount(int actCount, int childCount, String xPathBase){
        if(actCount != childCount) {
            SelenideElement
                    addCount = $x(xPathBase + "/../div[contains(@data-ng-click,'addPassenger')]").shouldBe(enabled, visible),
                    removeCount = $x(xPathBase + "/../div[contains(@data-ng-click,'removePassenger')]").shouldBe(enabled, visible);
            if (actCount < childCount) {
                for (int i = 0; i < (childCount - actCount); ++i) {
                    addCount.click();
                    sleep(600);
                }
            } else {
                int j = actCount - childCount;
                for (int i = 0; i <= j; i++) {
                    removeCount.click();
                    sleep(600);
                }
            }
        }
    }

    @Step("Подтвердим поиск")
    public void submitSearch(){
        sleep(500);
        submitSearchBtn.shouldBe(visible, enabled).click();
        if (isInputDataErrorPresent()) throw new CityAutocompleteException("Не подтянулся город из выпадающего списка");
    }

    private Boolean isInputDataErrorPresent() {
        return $$x(".//*[@data-ng-message='required' or @data-ng-message='is_object']").size() > 0;
    }

}