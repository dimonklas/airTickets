package autotest.pages;

import autotest.entity.AuthData;
import autotest.utils.ConfigurationVariables;
import autotest.utils.Utils;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@Log4j
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

    @Step("Откроем архив билетов")
    public void openArchivePage(){
        String archiveUrl = String.format("https://bilet-dev.isto.it.loc/archive/?csid=%s", AuthData.getAuth_key());
        log.info("Url = " + archiveUrl);
        open(archiveUrl);
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

    @Step("Нажмем кнопку 'Сгенерировать фрейм'")
    public MainPage submitOpenFrame(){
        generateFrameBtn.shouldBe(enabled).click();
        Utils.setCookieData();
        return this;
    }



}
