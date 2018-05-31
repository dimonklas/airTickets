package autotest;


import autotest.pages.MainPage;
import autotest.pages.SearchPage;
import autotest.utils.ConfigurationVariables;
import autotest.utils.Utils;
import lombok.extern.log4j.Log4j;

import static com.codeborne.selenide.Condition.appear;

@Log4j
class TestSuite {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();

    void front_14514(){
        MainPage mainPage = new MainPage();
        SearchPage searchPage = new SearchPage();

        mainPage.openMainPage()
                .openSearchPageViaChannel("Внешний Сайт")
                .submitOpenFrame();


        Utils.switchFrame();

        searchPage.selectWaysForTicket("Туда и обратно");
        searchPage.selectClass("Эконом");
        searchPage.setDepartureCity("Краков");
        searchPage.setArrivalCity("Варшава");
        searchPage.setFirstDate(180);
        searchPage.setSecondDate(185);
        searchPage.submitSearch();
        searchPage.preloader.should(appear);

        Utils.waitUntilPreloaderRemove(searchPage.preloader, 180);
        log.info("Дождались что прелоадер пропал");
    }

}