package autotest;


import autotest.pages.MainPage;
import autotest.pages.SearchPage;
import autotest.pages.SearchResultsPage;
import autotest.utils.ConfigurationVariables;
import autotest.utils.Utils;
import lombok.extern.log4j.Log4j;

import java.util.List;

import static com.codeborne.selenide.Condition.appear;

@Log4j
class TestSuite {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();

    void front_14514(){
        MainPage mainPage = new MainPage();
        SearchPage searchPage = new SearchPage();
        SearchResultsPage searchResultsPage = new SearchResultsPage();

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
        List<String> ids = searchResultsPage.getIdOfSearchResults();
        String id = ids.get(0);

        searchResultsPage.checkCompanyPresence(id, 2);

        searchResultsPage.checkDepartureCityNameForward(id, "Краков");
        searchResultsPage.checkArrivalCityNameForward(id, "Варшава");
        searchResultsPage.checkDepartureCityNameBackward(id, "Варшава");
        searchResultsPage.checkArrivalCityNameBackward(id, "Краков");

        searchResultsPage.checkDepartureDateForward(id, Utils.dateForFlightSearchResults(180));
        searchResultsPage.checkArrivalDateForward(id, Utils.dateForFlightSearchResults(180), Utils.dateForFlightSearchResults(180+1));
        searchResultsPage.checkDepartureDateBackward(id, Utils.dateForFlightSearchResults(185));
        searchResultsPage.checkArrivalDateBackward(id, Utils.dateForFlightSearchResults(185), Utils.dateForFlightSearchResults(185+1));

        searchResultsPage.checkPresenceOfDepartureTimeForward(id, "[0-9]{1,2}:[0-9]{2}");
        searchResultsPage.checkPresenceOfArrivalTimeForward(id, "[0-9]{1,2}:[0-9]{2}");
        searchResultsPage.checkPresenceOfDepartureTimeBackward(id, "[0-9]{1,2}:[0-9]{2}");
        searchResultsPage.checkPresenceOfArrivalTimeBackward(id, "[0-9]{1,2}:[0-9]{2}");
    }

}