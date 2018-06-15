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

//
    public SelenideElement
           preloader = $(By.xpath(".//*[@alt='loader-plane']"));
//            oneWayRadioBtn = $(By.xpath(".//label[contains(text(),'Только туда')]")),
//            bothWaysRadioBtn = $(By.xpath(".//label[contains(text(),'Туда и обратно')]")),
//            economyClassRadioBtn = $(By.xpath(".//label[contains(text(),'Эконом')]")),
//            businessClassRadioBtn = $(By.xpath(".//label[contains(text(),'Бизнес')]")),
//            plusMinus3daysChkBox = $(By.xpath(".//label[contains(text(),'+/-3')]"));

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
    }

    @Step("Выберем место вылета обратно {city}")
    public void setArrivalCity(String city){
        toField.shouldBe(enabled).setValue(city);
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

    @Step("Установим количество пассажиров")
    public void setPassengersCount(int adult, int child){
        int totalCount = adult + child;

        $x(".//*[@data-pc-models='vm.models.passengers']").shouldBe(visible).click();
        setAdultsCount(adult);
        setChildrenCount(child);
        Assert.assertTrue($x(".//*[@data-pc-models='vm.models.passengers']//input").getValue().contains(String.valueOf(totalCount)),
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
               for (int i = actCount; i > (actCount - adultsCount); i--) {
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
                for (int i = actCount; i > (actCount - childCount); i--) {
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