package autotest.pages;

import autotest.utils.ConfigurationVariables;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class MainPage {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();

    public MainPage() {
        baseUrl = CV.urlTest + CV.prominSession;
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
        return this;
    }

    @Step("Перейдем в архив билетов через канал {channel}")
    public MainPage openArchivePageViaChannel(String channel){
        String xPath = String.format(".//optgroup[@label='Архив']/option[contains(text(),'%s')]", channel);
        channelList.shouldBe(visible, enabled).click();
        $(By.xpath(xPath)).shouldBe(visible).click();
        return this;
    }

    @Step("Нажмем кнопку 'Сгенерировать фрейм'")
    public MainPage submitOpenFrame(){
        generateFrameBtn.shouldBe(enabled).click();
        return this;
    }

}
