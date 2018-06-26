package autotest.pages;

import autotest.utils.ConfigurationVariables;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;

import static com.codeborne.selenide.Condition.disappear;
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

    @Step("Отметим чек-бокс '+/-3 дня'")
    public void selectPlusMinus3Days(boolean isNeedCheck){
        if(isNeedCheck) $x(".//*[contains(text(),'+/-3')]").shouldBe(visible, enabled).click();
    }

    @Step("Выберем класс '{classType}'")
    public void selectClass(String classType){
        String xPath = String.format(".//label[contains(text(),'%s')]", classType);
        $(By.xpath(xPath)).shouldBe(visible, enabled).click();
    }

    @Step("Выберем место вылета {city}")
    public void setDepartureCity(String city){
        fromField.shouldBe(enabled).setValue(city);
        sleep(200);
    }

    @Step("Выберем место вылета обратно {city}")
    public void setArrivalCity(String city){
        toField.shouldBe(enabled).setValue(city);
        sleep(200);
    }


    @Step("Выберем города вылета/назначения для сложного маршрута")
    public void setDifficultRouteCities(String departure, String arrival, String departure2, String arrival2){
        $(By.xpath("(.//*[@name='difficult_departure'])[1]")).shouldBe(visible, enabled).setValue(departure);
        $(By.xpath("(.//*[@name='difficult_arrival'])[1]")).shouldBe(visible, enabled).setValue(arrival);
        sleep(200);
        $(By.xpath("(.//*[@name='difficult_departure'])[2]")).shouldBe(visible, enabled).setValue(departure2);
        $(By.xpath("(.//*[@name='difficult_arrival'])[2]")).shouldBe(visible, enabled).setValue(arrival2);
        sleep(200);
    }


    @Step("Выберем города вылета/назначения для сложного маршрута")
    public void setDifficultRouteCities(String departure, String arrival, String departure2, String arrival2,
                                        String departure3, String arrival3, String departure4, String arrival4){
        $(By.xpath("(.//*[@name='difficult_departure'])[1]")).shouldBe(visible, enabled).setValue(departure);
        $(By.xpath("(.//*[@name='difficult_arrival'])[1]")).shouldBe(visible, enabled).setValue(arrival);
        sleep(200);
        $(By.xpath("(.//*[@name='difficult_departure'])[2]")).shouldBe(visible, enabled).setValue(departure2);
        $(By.xpath("(.//*[@name='difficult_arrival'])[2]")).shouldBe(visible, enabled).setValue(arrival2);
        sleep(200);

        $x(".//*[text()='Добавить рейс']/parent::button").shouldBe(visible, enabled).click();
        $(By.xpath("(.//*[@name='difficult_departure'])[3]")).shouldBe(visible, enabled).setValue(departure3);
        $(By.xpath("(.//*[@name='difficult_arrival'])[3]")).shouldBe(visible, enabled).setValue(arrival3);
        sleep(200);

        $x(".//*[text()='Добавить рейс']/parent::button").shouldBe(visible, enabled).click();
        $(By.xpath("(.//*[@name='difficult_departure'])[4]")).shouldBe(visible, enabled).setValue(departure4);
        $(By.xpath("(.//*[@name='difficult_arrival'])[4]")).shouldBe(visible, enabled).setValue(arrival4);
        sleep(200);
    }

    @Step("Удалим последний добавленный маршрут и проверим, что строка поиска пропала")
    public void removeLastDifficultRoute(){
        $x(".//*[contains(@class,'difficult-flight__remove')]").shouldBe(visible, enabled).click();
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

    @Step("Установим даты вылета для сложного маршрута")
    public void setDatesForDifficultRoute(int daysFromToday, int daysFromToday2, int daysFromToday3){
        String jsCode = String.format("angular.element(document.getElementsByName('difficult_date')[0]).scope()" +
                ".flight.date=new Date().setDate(new Date().getDate() + %s);", daysFromToday);
        executeJavaScript(jsCode);

        jsCode = String.format("angular.element(document.getElementsByName('difficult_date')[1]).scope()" +
                ".flight.date=new Date().setDate(new Date().getDate() + %s);", daysFromToday2);
        executeJavaScript(jsCode);

        jsCode = String.format("angular.element(document.getElementsByName('difficult_date')[2]).scope()" +
                ".flight.date=new Date().setDate(new Date().getDate() + %s);", daysFromToday3);
        executeJavaScript(jsCode);
    }


    @Step("Установим количество пассажиров")
    public void setPassengersCount(int adult, int child, int infant){
        int totalCount = adult + child + infant;

        $x(".//*[@data-pc-models='vm.models.passengers']").shouldBe(visible).click();

        setAdultsCount(adult);
        setChildrenCount(child);
        setInfantCount(infant);
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