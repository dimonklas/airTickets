package autotest.pages;

import autotest.utils.ConfigurationVariables;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class SearchPage {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();

    public SelenideElement
           preloader = $(By.xpath(".//*[@alt='loader-plane']"));

    private SelenideElement
            fromField = $(By.name("departure")),
            toField = $(By.name("arrival")),
            submitSearchBtn = $(By.xpath(".//*[@name='PlaneSearchForm'] //button[contains(text(),'Найти')]"));

    @Step("Выберем направление '{waysType}'")
    public void selectWaysForTicket(String waysType){
        String xPath = String.format(".//label[contains(text(),'%s')]", waysType);
        $(By.xpath(xPath)).shouldBe(visible, enabled).click();
    }

    @Step("Выберем класс '{classType}'")
    public void selectClass(String classType){
        String xPath = String.format(".//label[contains(text(),'%s')]", classType);
        $(By.xpath(xPath)).shouldBe(visible, enabled).click();
    }

    @Step("Выберем место вылета {city}")
    public void setDepartureCity(String city){
        fromField.shouldBe(enabled).setValue(city);
        sleep(100);
    }

    @Step("Выберем место вылета обратно {city}")
    public void setArrivalCity(String city){
        toField.shouldBe(enabled).setValue(city);
        sleep(100);
    }


    @Step("Выберем города вылета/назначения для сложного маршрута")
    public void setDifficultRouteCities(String departure, String arrival, String departure2, String arrival2){
        $(By.xpath("(.//*[@name='difficult_departure'])[1]")).shouldBe(visible, enabled).setValue(departure);
        $(By.xpath("(.//*[@name='difficult_arrival'])[1]")).shouldBe(visible, enabled).setValue(arrival);
        sleep(100);
        $(By.xpath("(.//*[@name='difficult_departure'])[2]")).shouldBe(visible, enabled).setValue(departure2);
        $(By.xpath("(.//*[@name='difficult_arrival'])[2]")).shouldBe(visible, enabled).setValue(arrival2);
        sleep(100);
    }


    @Step("Установим дату вылета {daysFromToday} дней от сегодня")
    public void setFirstDate(int daysFromToday){
        String jsCode = String.format("angular.element(document.querySelector(\"[ng-model='rangeStart']\")).scope()" +
                ".dates[0]=new Date().setDate(new Date().getDate() + %s);", daysFromToday);
        executeJavaScript(jsCode);
    }

    @Step("Установим дату вылета обратно {daysFromToday} дней от сегодня")
    public void setSecondDate(int daysFromToday){
        String jsCode = String.format("angular.element(document.querySelector(\"[ng-model='rangeStart']\")).scope()" +
                ".dates[1]=new Date().setDate(new Date().getDate() + %s);", daysFromToday);
        executeJavaScript(jsCode);
    }


    @Step("Установим даты вылета для сложного маршрута")
    public void setDatesForDifficultRoute(int daysFromToday, int daysFromToday2){
        String jsCode = String.format("angular.element(document.getElementsByName('difficult_date')[0]).scope()" +
                ".flight.date=new Date().setDate(new Date().getDate() + %s);", daysFromToday);
        executeJavaScript(jsCode);

        jsCode = String.format("angular.element(document.getElementsByName('difficult_date')[1]).scope()" +
                ".flight.date=new Date().setDate(new Date().getDate() + %s);", daysFromToday2);
        executeJavaScript(jsCode);
    }


    @Step("Установим количество пассажиров")
    public void setPassengersCount(int adult, int child){
        int totalCount = adult + child;

        $x(".//*[@data-pc-models='vm.models.passengers']").shouldBe(visible).click();

        setAdultsCount(adult);
        setChildrenCount(child);
        Assert.assertTrue($x(".//*[@data-pc-models='vm.models.passengers']//input").getValue().contains(String.valueOf(totalCount)),
                "Неправильно установили количество пассажиров");
    }


    @Step("Установим количество пассажиров")
    public void setPassengersCountForDifficultRoute(int adult, int child){
        int totalCount = adult + child;

        $x("(.//*[@data-pc-models='vm.models.passengers'])[2]").shouldBe(visible).click();

        setAdultsCount(adult);
        setChildrenCount(child);
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


    @Step("Установим кол-во взрослых: {adultsCount}")
    private void setChildrenCount(int childCount) {
        int actCount = Integer.parseInt($x(".//input[@data-ng-model='models.chd']").shouldBe(visible).getValue());

        if(actCount != childCount) {
            SelenideElement
                    addCount = $x(".//input[@data-ng-model='models.chd']/../div[contains(@data-ng-click,'addPassenger')]").shouldBe(enabled, visible),
                    removeCount = $x(".//input[@data-ng-model='models.chd']/../div[contains(@data-ng-click,'removePassenger')]").shouldBe(enabled, visible);
            if (actCount < childCount) {
                for (int i = 0; i < (childCount - actCount); ++i) {
                    addCount.click();
                    sleep(600);
                }
            } else {
                for (int i = actCount; i >= (actCount - childCount); i--) {
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
    }

}