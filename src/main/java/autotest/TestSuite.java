package autotest;


import autotest.entity.SearchData;
import autotest.entity.TicketData;
import autotest.pages.*;
import autotest.utils.ConfigurationVariables;
import autotest.utils.Utils;
import lombok.extern.log4j.Log4j;

import java.util.List;

import static com.codeborne.selenide.Condition.and;
import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

@Log4j
class TestSuite {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();

    void bookTickets(SearchData search, TicketData ticket){

        MainPage mainPage = new MainPage();
        SearchPage searchPage = new SearchPage();
        SearchResultsPage searchResultsPage = new SearchResultsPage();
        TicketInfoPage ticketInfoPage = new TicketInfoPage();
        CustomerContactDataPage customerContactDataPage = new CustomerContactDataPage();
        PassengersDataPage passengersDataPage = new PassengersDataPage();
        ArchivePage archivePage = new ArchivePage();

        mainPage.openMainPage()
                .openSearchPageViaChannel(search.getChannel())
                .submitOpenFrame();


        Utils.switchFrame();

        searchPage.selectWaysForTicket(search.getWaysType());
        searchPage.selectClass(search.getClassType());
        searchPage.selectPlusMinus3Days(search.isPlusMinus3days());
        searchPage.setDepartureCity(search.getDepartureCity());
        searchPage.setArrivalCity(search.getArrivalCity());
        searchPage.setFirstDate(search.getDaysFwd());
        searchPage.setSecondDate(search.getDaysBckwd());
        searchPage.setPassengersCount(search.getAdultsCount(), search.getChildCount(), search.getInfantCount());

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

        searchResultsPage.checkDepartureDateForward(id, Utils.dateForFlightSearchResults(search.getDaysFwd()));
        searchResultsPage.checkArrivalDateForward(id, Utils.dateForFlightSearchResults(search.getDaysFwd()), Utils.dateForFlightSearchResults(search.getDaysFwd()+1));
        searchResultsPage.checkDepartureDateBackward(id, Utils.dateForFlightSearchResults(search.getDaysBckwd()));
        searchResultsPage.checkArrivalDateBackward(id, Utils.dateForFlightSearchResults(search.getDaysBckwd()), Utils.dateForFlightSearchResults(search.getDaysBckwd()+1));

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

        int indexChild = 2;
        int indexInfant = 2;
        if(search.getInfantCount() == 1 && search.getChildCount() == 1) indexInfant = 3;

        passengersDataPage.checkAvaliabilityOfCustomersDataFields();
        passengersDataPage.checkPresenceOfTextElements(1, "Взрослый");
        if(search.getChildCount() == 1) passengersDataPage.checkPresenceOfTextElements(indexChild, "Ребенок");
        if(search.getInfantCount() == 1) passengersDataPage.checkPresenceOfTextElements(indexInfant, "Младенец");

        passengersDataPage.checkPresenceAndAvaliabilityOfButtons();

        passengersDataPage.fillCustomersData(1, CV.lastName, CV.firstName, CV.birthDate);
        passengersDataPage.fillCitizenship(1, CV.citizenship);
        passengersDataPage.setSex(1, CV.sex);
        passengersDataPage.fillDocData(1, CV.docSN, CV.docExpDate, search.isFakeDoc());

        if (search.getChildCount() == 1) {
            passengersDataPage.fillCustomersData(indexChild, CV.lastNameChd, CV.firstNameChd, CV.birthDateChd);
            passengersDataPage.fillCitizenship(indexChild, CV.citizenshipChd);
            passengersDataPage.setSex(indexChild, CV.sexChd);
            passengersDataPage.fillDocData(indexChild, CV.docSNChd, CV.docExpDateChd, search.isFakeDocChld());
        }

        if (search.getInfantCount() == 1) {
            passengersDataPage.fillCustomersData(indexInfant, CV.lastNameInf, CV.firstNameInf, CV.birthDateInf);
            passengersDataPage.fillCitizenship(indexInfant, CV.citizenshipInf);
            passengersDataPage.setSex(indexInfant, CV.sexInf);
            passengersDataPage.fillDocData(indexInfant, CV.docSNInf, CV.docExpDateInf, search.isFakeDocInf());
        }

        passengersDataPage.fillEmail("kedroDelgardo@mail.com");
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
        searchPage.selectPlusMinus3Days(search.isPlusMinus3days());
        searchPage.setDepartureCity(search.getDepartureCity());
        searchPage.setArrivalCity(search.getArrivalCity());
        searchPage.setFirstDate(180);
        searchPage.setSecondDate(185);
        searchPage.setPassengersCount(search.getAdultsCount(), search.getChildCount(), search.getInfantCount());

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
        passengersDataPage.fillDocData(1, CV.docSN, CV.docExpDate, search.isFakeDoc());

        passengersDataPage.fillCustomersData(2, CV.lastNameChd, CV.firstNameChd, CV.birthDateChd);
        passengersDataPage.fillCitizenship(2, CV.citizenshipChd);
        passengersDataPage.setSex(2, "F");
        passengersDataPage.fillDocData(2, CV.docSNChd, CV.docExpDateChd, search.isFakeDocChld());

        passengersDataPage.fillEmail("kedroDelgardo@mail.com");
        passengersDataPage.bookTicket();

        passengersDataPage.checkBookedTicketMessage(ticket.getPrice());
        String bookingCode = passengersDataPage.getBookingCode();
        ticket.setBookingId(bookingCode);

        passengersDataPage.openArchive();
        archivePage.checkTicketStatus(bookingCode, "Забронирован, не оплачен");
    }


    void front_15024(SearchData search, TicketData ticket){
        MainPage mainPage = new MainPage();
        SearchPage searchPage = new SearchPage();
        SearchResultsPage searchResultsPage = new SearchResultsPage();
        TicketInfoPage ticketInfoPage = new TicketInfoPage();
        CustomerContactDataPage customerContactDataPage = new CustomerContactDataPage();
        PassengersDataPage passengersDataPage = new PassengersDataPage();
        PaymentPage paymentPage = new PaymentPage();

        mainPage.openMainPage()
                .openSearchPageViaChannel("Внешний Сайт")
                .submitOpenFrame();

        Utils.switchFrame();

        searchPage.selectWaysForTicket(search.getWaysType());
        searchPage.selectClass(search.getClassType());
        searchPage.selectPlusMinus3Days(search.isPlusMinus3days());
        searchPage.setDifficultRouteCities(search.getDepartureCity(), search.getArrivalCity(),
                                           search.getDepartureCity_2(), search.getArrivalCity_2());
        searchPage.setDatesForDifficultRoute(180, 210);
        searchPage.setPassengersCountForDifficultRoute(search.getAdultsCount(), search.getChildCount());
        searchPage.submitSearch();
        searchPage.preloader.should(appear);

        Utils.waitUntilPreloaderRemove(searchPage.preloader, 180);
        //Получим id-шнки блоков с результатами поиска
        List<String> ids = searchResultsPage.getIdOfSearchResults();
        String id = ids.get(0);

        String price = searchResultsPage.checkPresenceOfTicketsCost(id);
        ticket.setPrice(price);

        searchResultsPage.pressSelectButton(id);

        ticketInfoPage.waitForTicketRulesBtn();
        ticketInfoPage.checkTicketDifficultDetails(2);

        customerContactDataPage.checkPresenceOfContactDataBlock();
        customerContactDataPage.enterUserData();

        passengersDataPage.checkAvaliabilityOfCustomersDataFields();
        passengersDataPage.checkPresenceOfTextElements(1, "Взрослый");
        passengersDataPage.checkPresenceAndAvaliabilityOfButtons();
        passengersDataPage.fillCustomersData(1, CV.lastName, CV.firstName, CV.birthDate);
        passengersDataPage.fillCitizenship(1, CV.citizenship);
        passengersDataPage.setSex(1, "M");
        passengersDataPage.fillDocData(1, CV.docSN, CV.docExpDate, search.isFakeDoc());
        passengersDataPage.fillEmail("kedroDelgardo@mail.com");
        passengersDataPage.buyTicket();

        paymentPage.title.shouldBe(visible);
        paymentPage.doPaymentByCard(null, null, null);
    }


    void front_15091(SearchData search){
        MainPage mainPage = new MainPage();
        SearchPage searchPage = new SearchPage();
        SearchResultsPage searchResultsPage = new SearchResultsPage();
        TicketInfoPage ticketInfoPage = new TicketInfoPage();

        mainPage.openMainPage()
                .openSearchPageViaChannel("Внешний Сайт")
                .submitOpenFrame();

        Utils.switchFrame();

        searchPage.selectWaysForTicket(search.getWaysType());
        searchPage.selectClass(search.getClassType());
        searchPage.selectPlusMinus3Days(search.isPlusMinus3days());
        searchPage.setDifficultRouteCities(search.getDepartureCity(), search.getArrivalCity(),
                                           search.getDepartureCity_2(), search.getArrivalCity_2(),
                                           search.getDepartureCity_3(), search.getArrivalCity_3(),
                                           search.getDepartureCity_4(), search.getArrivalCity_4());
        searchPage.removeLastDifficultRoute();
        searchPage.setDatesForDifficultRoute(180, 190, 210);
        searchPage.setPassengersCountForDifficultRoute(search.getAdultsCount(), search.getChildCount());
        searchPage.submitSearch();
        searchPage.preloader.should(appear);

        Utils.waitUntilPreloaderRemove(searchPage.preloader, 180);
        //Получим id-шнки блоков с результатами поиска
        List<String> ids = searchResultsPage.getIdOfSearchResults();
        String id = ids.get(0);

        searchResultsPage.checkPresenceOfTicketsCost(id);
        searchResultsPage.pressSelectButton(id);

        ticketInfoPage.waitForTicketRulesBtn();
        ticketInfoPage.checkTicketDifficultDetails(3);
    }


    void front_15848(SearchData search, TicketData ticket){
        MainPage mainPage = new MainPage();
        SearchPage searchPage = new SearchPage();
        SearchResultsPage searchResultsPage = new SearchResultsPage();
        TicketInfoPage ticketInfoPage = new TicketInfoPage();
        CustomerContactDataPage customerContactDataPage = new CustomerContactDataPage();
        PassengersDataPage passengersDataPage = new PassengersDataPage();
        PaymentPage paymentPage = new PaymentPage();

        mainPage.openMainPage()
                .openSearchPageViaChannel("Внешний Сайт")
                .submitOpenFrame();


        Utils.switchFrame();

        searchPage.selectWaysForTicket(search.getWaysType());
        searchPage.selectClass(search.getClassType());
        searchPage.selectPlusMinus3Days(search.isPlusMinus3days());
        searchPage.setDepartureCity(search.getDepartureCity());
        searchPage.setArrivalCity(search.getArrivalCity());
        searchPage.setFirstDate(search.getDaysFwd());
        searchPage.setSecondDate(search.getDaysBckwd());
        searchPage.setPassengersCount(search.getAdultsCount(), search.getChildCount(), search.getInfantCount());

        searchPage.submitSearch();
        searchPage.preloader.should(appear);
        Utils.waitUntilPreloaderRemove(searchPage.preloader, 180);

        searchResultsPage.checkMatrixFlightsPresence();
        searchResultsPage.checkResultsForPlusMinus3Days(search.getDaysFwd(), search.getDepartureCity(), search.getArrivalCity());

        String id = searchResultsPage.getIdOfSearchResults().get(0);
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
        passengersDataPage.fillDocData(1, CV.docSN, CV.docExpDate, search.isFakeDoc());
        passengersDataPage.fillEmail("kedroDelgardo@mail.com");
        passengersDataPage.buyTicket();

        paymentPage.title.shouldBe(visible);
        paymentPage.doPaymentByCard(null, null, null);
    }


    void stornBookings() {
        MainPage mainPage = new MainPage();
        ArchivePage archivePage = new ArchivePage();

        mainPage.openArchivePage();
        switchTo().defaultContent();
        $x(".//*[text()='Поиск']").shouldBe(and("Кнопка поиск в фильтрах Архива билетов", visible));
        Utils.setCookieData();

        List<String> tickets_ids = archivePage.getTickets_id();
        if(tickets_ids.size() > 0) {
            tickets_ids.forEach(Utils::stornBookedTicket);
            sleep(30 * 1000);
            refresh();
            tickets_ids = archivePage.getTickets_id();
            if(tickets_ids.size() > 0) tickets_ids.forEach(Utils::stornBookedTicket);
        } else log.info("Забронированных билетов не найдено");

    }

}