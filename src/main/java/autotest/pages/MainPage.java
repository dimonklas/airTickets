package autotest.pages;

import autotest.utils.ConfigurationVariables;
import autotest.utils.Utils;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.*;

@Log4j
public class MainPage {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();

    public MainPage() {
        baseUrl =  String.format("https://%s/frame_noauth/?sid=%s", CV.urlBase, CV.prominSession);
    }

    private SelenideElement
            channelList = $(By.id("field-channel")),
            generateFrameBtn = $(By.xpath(".//button[contains(text(),'Сгенерировать фрейм')]"));


    @Step("Откроем главную страницу")
    public MainPage openMainPage(){
        open(baseUrl);
        return this;
    }


    @Step("Перейдем на страницу поиска билетов через канал {channel}")
    public MainPage openSearchPageViaChannel(String channel){
        String xPath = String.format(".//optgroup[@label='Поиск']/option[contains(text(),'%s')]", channel);
        channelList.shouldBe(visible, enabled).click();
        $(By.xpath(xPath)).shouldBe(visible).click();

        if (!channel.equalsIgnoreCase("Внешний Сайт")) {
            fillEkbIdField(CV.ekbId);
            fillPhoneField(CV.phone.substring(1));
        }
        return this;
    }

    @Step("Откроем архив билетов для {phoneNum}")
    public void openArchivePage(String phoneNum){
        open(Utils.getArchiveUrl(phoneNum));
        switchTo().defaultContent();
        ArchivePage.waitForArchivePageLoad();
    }

    @Step("Перейдем в архив билетов через канал {channel}")
    public MainPage openArchivePageViaChannel(String channel){
        String xPath = String.format(".//optgroup[@label='Архив']/option[contains(text(),'%s')]", channel);
        channelList.shouldBe(visible, enabled).click();
        $(By.xpath(xPath)).shouldBe(visible).click();
        return this;
    }

    @Step("Заполним поле моб. телефона")
    public MainPage fillPhoneField(String number){
        $(By.id("field-phone")).shouldBe(visible, enabled).setValue(number);
        return this;
    }

    @Step("Заполним поле екб id")
    public MainPage fillEkbIdField(String id) {
        $(By.id("field-id")).shouldBe(visible, enabled).setValue(id);
        return this;
    }

    @Step("Нажмем кнопку 'Сгенерировать фрейм'")
    public MainPage submitOpenFrame(){
        generateFrameBtn.shouldBe(enabled).click();
        return this;
    }

    @Step("Вернуть текущий канал")
    public String getCurrentChannel() {
        switchTo().defaultContent();
        String channel = $(By.id("field-channel")).shouldBe(visible).getText();
        $("[name=avia-widget-frame]").shouldBe(Condition.enabled);
        switchTo().frame("avia-widget-frame");
        return channel;
    }
}
