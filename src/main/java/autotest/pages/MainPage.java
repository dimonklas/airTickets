package autotest.pages;

import autotest.utils.ConfigurationVariables;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.open;

public class MainPage {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();

    public MainPage() {
        baseUrl = CV.urlTest;
    }


    @Step("Откроем главную страницу")
    public void openMainPage(){
        open(baseUrl);
    }

    @Step("Перейдем на страницу поиска билетов через канал {channel}")
    public void openSearchPageViaChannel(String channel){

    }


}
