package autotest;


import autotest.entity.SearchData;
import autotest.entity.TicketData;
import autotest.pages.*;
import autotest.utils.ConfigurationVariables;
import autotest.utils.Utils;
import lombok.extern.log4j.Log4j;

import java.util.List;

import static com.codeborne.selenide.Condition.appear;

@Log4j
class TestSuite {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();

    void front_14514(SearchData search, TicketData ticket){

        MainPage mainPage = new MainPage();
        SearchPage searchPage = new SearchPage();
        SearchResultsPage searchResultsPage = new SearchResultsPage();
        TicketInfoPage ticketInfoPage = new TicketInfoPage();
        CustomerContactDataPage customerContactDataPage = new CustomerContactDataPage();
        PassengersDataPage passengersDataPage = new PassengersDataPage();
        ArchivePage archivePage = new ArchivePage();

        mainPage.openMainPage()
                .openSearchPageViaChannel("Внешний Сайт")
                .submitOpenFrame();


        Utils.switchFrame();

        searchPage.selectWaysForTicket(search.getWaysType());
        searchPage.selectClass(search.getClassType());
        searchPage.setDepartureCity(search.getDepartureCity());
        searchPage.setArrivalCity(search.getArrivalCity());
        searchPage.setFirstDate(180);
        searchPage.setSecondDate(185);
        searchPage.setPassengersCount(1, 0);

        searchPage.submitSearch();
        searchPage.preloader.should(appear);

        Utils.waitUntilPreloaderRemove(searchPage.preloader, 180);
        //Получим id-шнки блоков с результатами поиска
        List<String> ids = searchResultsPage.getIdOfSearchResults();
        String id = ids.get(0);

        searchResultsPage.checkCompanyPresence(id, 2);

        searchResultsPage.checkDepartureCityNameForward(id, search.getDepartureCity());
        searchResultsPage.checkArrivalCityNameForward(id, search.getArrivalCity());
        searchResultsPage.checkDepartureCityNameBackward(id, search.getArrivalCity());
        searchResultsPage.checkArrivalCityNameBackward(id, search.getDepartureCity());

        searchResultsPage.checkDepartureAitportNameForward(id);
        searchResultsPage.checkArrivalAitportNameForward(id);
        searchResultsPage.checkDepartureAitportNameBackward(id);
        searchResultsPage.checkArrivalAitportNameBackward(id);

        searchResultsPage.checkDepartureDateForward(id, Utils.dateForFlightSearchResults(180));
        searchResultsPage.checkArrivalDateForward(id, Utils.dateForFlightSearchResults(180), Utils.dateForFlightSearchResults(180+1));
        searchResultsPage.checkDepartureDateBackward(id, Utils.dateForFlightSearchResults(185));
        searchResultsPage.checkArrivalDateBackward(id, Utils.dateForFlightSearchResults(185), Utils.dateForFlightSearchResults(185+1));

        String regex = "[0-9]{1,2}:[0-9]{2}";
        searchResultsPage.checkPresenceOfDepartureTimeForward(id, regex);
        searchResultsPage.checkPresenceOfArrivalTimeForward(id, regex);
        searchResultsPage.checkPresenceOfDepartureTimeBackward(id, regex);
        searchResultsPage.checkPresenceOfArrivalTimeBackward(id, regex);

        regex = "[0-9ч]{2,3}[0-9м\\s]{3,4}";
        searchResultsPage.checkPresenceOfFlyingTimeForward(id, regex);
        searchResultsPage.checkPresenceOfFlyingTimeBackward(id, regex);

        String price = searchResultsPage.checkPresenceOfTicketsCost(id);
        ticket.setPrice(price);

        searchResultsPage.pressSelectButton(id);

        ticketInfoPage.waitForTicketRulesBtn();
        ticketInfoPage.checkTicketForwardDetails();
        ticketInfoPage.checkTicketBackwardDetails();

        customerContactDataPage.checkPresenceOfContactDataBlock();
        customerContactDataPage.enterUserData();

        passengersDataPage.checkAvaliabilityOfCustomersDataFields();
        passengersDataPage.checkPresenceOfTextElements(1, "Взрослый");
        passengersDataPage.checkPresenceAndAvaliabilityOfButtons();

        passengersDataPage.fillCustomersData(1, CV.lastName, CV.firstName, CV.birthDate);
        passengersDataPage.fillCitizenship(1, CV.citizenship);
        passengersDataPage.setSex(1, "M");
        passengersDataPage.fillDocData(1, CV.docSN, CV.docExpDate);
        passengersDataPage.fillEmail("pedroDelgardo@mail.com");
        passengersDataPage.bookTicket();

        passengersDataPage.checkBookedTicketMessage(ticket.getPrice());
        String bookingCode = passengersDataPage.getBookingCode();
        ticket.setBookingId(bookingCode);

        passengersDataPage.openArchive();
        archivePage.checkTicketStatus(bookingCode, "Забронирован, не оплачен");
    }



    void front_12552(SearchData search, TicketData ticket){
        MainPage mainPage = new MainPage();
        SearchPage searchPage = new SearchPage();
        SearchResultsPage searchResultsPage = new SearchResultsPage();
        TicketInfoPage ticketInfoPage = new TicketInfoPage();
        CustomerContactDataPage customerContactDataPage = new CustomerContactDataPage();
        PassengersDataPage passengersDataPage = new PassengersDataPage();
        ArchivePage archivePage = new ArchivePage();

        mainPage.openMainPage()
                .openSearchPageViaChannel("Внешний Сайт")
                .submitOpenFrame();


        Utils.switchFrame();

        searchPage.selectWaysForTicket(search.getWaysType());
        searchPage.selectClass(search.getClassType());
        searchPage.setDepartureCity(search.getDepartureCity());
        searchPage.setArrivalCity(search.getArrivalCity());
        searchPage.setFirstDate(180);
        searchPage.setSecondDate(185);
        searchPage.setPassengersCount(1, 1);

        searchPage.submitSearch();
        searchPage.preloader.should(appear);

        Utils.waitUntilPreloaderRemove(searchPage.preloader, 180);
        //Получим id-шнки блоков с результатами поиска
        List<String> ids = searchResultsPage.getIdOfSearchResults();
        String id = ids.get(0);

        searchResultsPage.checkCompanyPresence(id, 2);

        searchResultsPage.checkDepartureCityNameForward(id, search.getDepartureCity());
        searchResultsPage.checkArrivalCityNameForward(id, search.getArrivalCity());
        searchResultsPage.checkDepartureCityNameBackward(id, search.getArrivalCity());
        searchResultsPage.checkArrivalCityNameBackward(id, search.getDepartureCity());

        searchResultsPage.checkDepartureAitportNameForward(id);
        searchResultsPage.checkArrivalAitportNameForward(id);
        searchResultsPage.checkDepartureAitportNameBackward(id);
        searchResultsPage.checkArrivalAitportNameBackward(id);

        searchResultsPage.checkDepartureDateForward(id, Utils.dateForFlightSearchResults(180));
        searchResultsPage.checkArrivalDateForward(id, Utils.dateForFlightSearchResults(180), Utils.dateForFlightSearchResults(180+1));
        searchResultsPage.checkDepartureDateBackward(id, Utils.dateForFlightSearchResults(185));
        searchResultsPage.checkArrivalDateBackward(id, Utils.dateForFlightSearchResults(185), Utils.dateForFlightSearchResults(185+1));

        String regex = "[0-9]{1,2}:[0-9]{2}";
        searchResultsPage.checkPresenceOfDepartureTimeForward(id, regex);
        searchResultsPage.checkPresenceOfArrivalTimeForward(id, regex);
        searchResultsPage.checkPresenceOfDepartureTimeBackward(id, regex);
        searchResultsPage.checkPresenceOfArrivalTimeBackward(id, regex);

        regex = "[0-9ч]{2,3}[0-9м\\s]{3,4}";
        searchResultsPage.checkPresenceOfFlyingTimeForward(id, regex);
        searchResultsPage.checkPresenceOfFlyingTimeBackward(id, regex);

        String price = searchResultsPage.checkPresenceOfTicketsCost(id);
        ticket.setPrice(price);

        searchResultsPage.pressSelectButton(id);

        ticketInfoPage.waitForTicketRulesBtn();
        ticketInfoPage.checkTicketForwardDetails();
        ticketInfoPage.checkTicketBackwardDetails();

        customerContactDataPage.checkPresenceOfContactDataBlock();
        customerContactDataPage.enterUserData();

        passengersDataPage.checkAvaliabilityOfCustomersDataFields();
        passengersDataPage.checkPresenceOfTextElements(1, "Взрослый");
        passengersDataPage.checkPresenceOfTextElements(2, "Ребенок");
        passengersDataPage.checkPresenceAndAvaliabilityOfButtons();



        passengersDataPage.fillCustomersData(1, CV.lastName, CV.firstName, CV.birthDate);
        passengersDataPage.fillCitizenship(1, CV.citizenship);
        passengersDataPage.setSex(1, "M");
        passengersDataPage.fillDocData(1, CV.docSN, CV.docExpDate);

        passengersDataPage.fillCustomersData(2, CV.lastNameChd, CV.firstNameChd, CV.birthDateChd);
        passengersDataPage.fillCitizenship(2, CV.citizenshipChd);
        passengersDataPage.setSex(2, "F");
        passengersDataPage.fillDocData(2, CV.docSNChd, CV.docExpDateChd);

        passengersDataPage.fillEmail("pedroDelgardo@mail.com");
        passengersDataPage.bookTicket();

        passengersDataPage.checkBookedTicketMessage(ticket.getPrice());
        String bookingCode = passengersDataPage.getBookingCode();
        ticket.setBookingId(bookingCode);

        passengersDataPage.openArchive();
        archivePage.checkTicketStatus(bookingCode, "Забронирован, не оплачен");
    }
}