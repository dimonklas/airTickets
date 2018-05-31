package autotest.pages;

import autotest.utils.ConfigurationVariables;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.sleep;

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

    @Step("Подтвердим поиск")
    public void submitSearch(){
        sleep(500);
        submitSearchBtn.shouldBe(visible, enabled).click();
    }

}

