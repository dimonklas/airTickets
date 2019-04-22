package autotest;


import autotest.dto.custData.ClientDataItem;
import autotest.entity.BookedTickets;
import autotest.entity.SearchData;
import autotest.entity.TicketData;
import autotest.entity.forDataproviders.DateTests;
import autotest.pages.*;
import autotest.utils.ConfigurationVariables;
import autotest.utils.Utils;
import com.codeborne.selenide.SelenideElement;
import lombok.extern.log4j.Log4j;
import org.testng.Assert;

import java.util.List;

import static autotest.utils.Utils.closeTabAfterOpenArchivePage;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.testng.Assert.assertEquals;

@Log4j
class TestSuite {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();


    void performSearch(SearchData search) {
        MainPage mainPage = new MainPage();
        SearchPage searchPage = new SearchPage();

        mainPage.openMainPage()
                .openSearchPageViaChannel(search.getChannel())
                .submitOpenFrame();

        Utils.switchFrame();

        searchPage.selectWaysForTicket(search.getWaysType());
        searchPage.selectClass(search.getClassType());
        searchPage.selectPlusMinus3Days(search.isPlusMinus3days());

        if ("Сложный маршрут".equalsIgnoreCase(search.getWaysType())) {
            searchPage.setDifficultRouteCities(search.getDepartureCity(), search.getArrivalCity(),
                    search.getDepartureCity_2(), search.getArrivalCity_2(),
                    search.getDepartureCity_3(), search.getArrivalCity_4(),
                    search.getDepartureCity_3(), search.getArrivalCity_4());
            searchPage.setDatesForDifficultRoute(search.getDaysForDifficult_1(),
                    search.getDaysForDifficult_2(),
                    search.getDaysForDifficult_3(),
                    search.getDaysForDifficult_4());
            if (search.isNeedRemoveLastRoute()) searchPage.removeLastDifficultRoute();
            searchPage.setPassengersCountForDifficultRoute(search.getAdultsCount(), search.getChildCount());
        } else {
            searchPage.setDepartureCity(search.getDepartureCity());
            searchPage.setArrivalCity(search.getArrivalCity());
            searchPage.setFirstDate(search.getDaysFwd());
            searchPage.setSecondDate(search.getDaysBckwd());
            searchPage.setPassengersCount(search.getAdultsCount(), search.getChildCount(), search.getInfantCount());
        }

        searchPage.submitSearch();
        searchPage.preloader.should(appear.because("Прелоадер-самолетик при успешном запуске поиска")).waitUntil(disappears, 180 * 1000);

        if ($x(".//*[contains(text(),'Не найдены варианты перелёта')]").isDisplayed()) {
            sleep(45 * 1000);
            searchPage.submitSearch();
            searchPage.preloader.should(appear.because("Прелоадер-самолетик при успешном запуске поиска")).waitUntil(disappears, 180 * 1000);
        }
    }


    void bookTickets(SearchData search, ClientDataItem client, TicketData ticket) {
        SearchResultsPage searchResultsPage = new SearchResultsPage();
        TicketInfoPage ticketInfoPage = new TicketInfoPage();
        CustomerContactDataPage customerContactDataPage = new CustomerContactDataPage();
        PassengersDataPage passengersDataPage = new PassengersDataPage();
        ArchivePage archivePage = new ArchivePage();


        //Получим id блока с результатами поиска
        String id = searchResultsPage.getIdOfSearchResults().get(0);

        searchResultsPage.checkCompanyPresence(id, 2);

        searchResultsPage.checkDepartureCityNameForward(id, search.getDepartureCity());
        searchResultsPage.checkArrivalCityNameForward(id, search.getArrivalCity());
        searchResultsPage.checkDepartureCityNameBackward(id, search.getArrivalCity());
        searchResultsPage.checkArrivalCityNameBackward(id, search.getDepartureCity());

        searchResultsPage.checkDepartureAitportNameForward(id);
        searchResultsPage.checkArrivalAitportNameForward(id);
        searchResultsPage.checkDepartureAitportNameBackward(id);
        searchResultsPage.checkArrivalAitportNameBackward(id);

        searchResultsPage.checkDepartureDateForward(id, search.getDaysFwd());
        searchResultsPage.checkArrivalDateForward(id, search.getDaysFwd());
        searchResultsPage.checkDepartureDateBackward(id, search.getDaysBckwd());
        searchResultsPage.checkArrivalDateBackward(id, search.getDaysBckwd());

        String regex = "[0-9]{1,2}:[0-9]{2}";
        searchResultsPage.checkPresenceOfDepartureTimeForward(id, regex);
        searchResultsPage.checkPresenceOfArrivalTimeForward(id, regex);
        searchResultsPage.checkPresenceOfDepartureTimeBackward(id, regex);
        searchResultsPage.checkPresenceOfArrivalTimeBackward(id, regex);

        regex = "[0-9ч]{2,3}[0-9м\\s]{0,4}";
        searchResultsPage.checkPresenceOfFlyingTimeForward(id, regex);
        searchResultsPage.checkPresenceOfFlyingTimeBackward(id, regex);

        String price = searchResultsPage.checkPresenceOfTicketsCost(id);
        ticket.setPrice(price);

        searchResultsPage.pressSelectButton(id);

        ticketInfoPage.waitForTicketRulesBtn();
        ticketInfoPage.checkTicketForwardDetails();
        ticketInfoPage.checkTicketBackwardDetails();

        if (search.getChannel().equalsIgnoreCase("Внешний Сайт")) {
            customerContactDataPage.checkPresenceOfContactDataBlock();
            customerContactDataPage.enterUserData();
        }

        //Индексы полей для xPath
        int indexChild = 2;
        int indexInfant = 2;
        if (search.getInfantCount() == 1 && search.getChildCount() == 1) indexInfant = 3;

        passengersDataPage.checkAvaliabilityOfCustomersDataFields();
        passengersDataPage.checkPresenceOfTextElements(1, "Взрослый");
        if (search.getChildCount() == 1) passengersDataPage.checkPresenceOfTextElements(indexChild, "Ребенок");
        if (search.getInfantCount() == 1) passengersDataPage.checkPresenceOfTextElements(indexInfant, "Младенец");

        passengersDataPage.checkPresenceAndAvaliabilityOfButtons();

        passengersDataPage.fillCustomersData(1, client.getLastName(), client.getFirstName(), client.getBirthDate());
        passengersDataPage.fillCitizenship(1, CV.citizenship);
        passengersDataPage.setSex(1, client.getSex());
        passengersDataPage.fillDocData(1, client.getDocSN(), client.getDocExpDate(), search.isFakeDoc());

        if (search.getChildCount() == 1) {
            passengersDataPage.fillCustomersData(indexChild, client.getLastName(), client.getFirstNameChd(), client.getBirthDateChd());
            passengersDataPage.fillCitizenship(indexChild, CV.citizenship);
            passengersDataPage.setSex(indexChild, client.getSexChd());
            passengersDataPage.fillDocData(indexChild, client.getDocSNChd(), client.getDocExpDateChd(), search.isFakeDocChld());
        }

        if (search.getInfantCount() == 1) {
            passengersDataPage.fillCustomersData(indexInfant, client.getLastName(), client.getFirstNameInf(), client.getBirthDateInf());
            passengersDataPage.fillCitizenship(indexInfant, CV.citizenship);
            passengersDataPage.setSex(indexInfant, client.getSexInf());
            passengersDataPage.fillDocData(indexInfant, client.getDocSNInf(), client.getDocExpDateInf(), search.isFakeDocInf());
        }

        passengersDataPage.fillEmail(CV.email);
        passengersDataPage.bookTicket();

        passengersDataPage.checkBookedTicketMessage(ticket.getPrice());
        String bookingCode = passengersDataPage.getBookingCode();
        ticket.setBookingId(bookingCode);

        BookedTickets.getTicketsList().add(ticket);

        passengersDataPage.openArchive();
        archivePage.checkTicketStatus(bookingCode, "Забронирован, не оплачен");
//        if (search.getChannel().equalsIgnoreCase("Внешний сайт")) closeTabAfterOpenArchivePage();
    }


    //Покупка авиабилета для взрослого и ребенка (внешний сайт)
    void front_12552(SearchData search, ClientDataItem client, TicketData ticket) {
        SearchResultsPage searchResultsPage = new SearchResultsPage();
        TicketInfoPage ticketInfoPage = new TicketInfoPage();
        CustomerContactDataPage customerContactDataPage = new CustomerContactDataPage();
        PassengersDataPage passengersDataPage = new PassengersDataPage();
        ArchivePage archivePage = new ArchivePage();

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

        searchResultsPage.checkDepartureDateForward(id, 180);
        searchResultsPage.checkArrivalDateForward(id, 180);
        searchResultsPage.checkDepartureDateBackward(id, 185);
        searchResultsPage.checkArrivalDateBackward(id, 185);

        String regex = "[0-9]{1,2}:[0-9]{2}";
        searchResultsPage.checkPresenceOfDepartureTimeForward(id, regex);
        searchResultsPage.checkPresenceOfArrivalTimeForward(id, regex);
        searchResultsPage.checkPresenceOfDepartureTimeBackward(id, regex);
        searchResultsPage.checkPresenceOfArrivalTimeBackward(id, regex);

        regex = "[0-9ч]{2,3}[0-9м\\s]{0,4}";
        searchResultsPage.checkPresenceOfFlyingTimeForward(id, regex);
        searchResultsPage.checkPresenceOfFlyingTimeBackward(id, regex);

        String price = searchResultsPage.checkPresenceOfTicketsCost(id);
        ticket.setPrice(price);

        searchResultsPage.pressSelectButton(id);

        ticketInfoPage.waitForTicketRulesBtn();
        ticketInfoPage.checkTicketForwardDetails();
        ticketInfoPage.checkTicketBackwardDetails();

        if (search.getChannel().equalsIgnoreCase("Внешний Сайт")) {
            customerContactDataPage.checkPresenceOfContactDataBlock();
            customerContactDataPage.enterUserData();
        }

        passengersDataPage.checkAvaliabilityOfCustomersDataFields();
        passengersDataPage.checkPresenceOfTextElements(1, "Взрослый");
        passengersDataPage.checkPresenceOfTextElements(2, "Ребенок");
        passengersDataPage.checkPresenceAndAvaliabilityOfButtons();

        passengersDataPage.fillCustomersData(1, client.getLastName(), client.getFirstName(), client.getBirthDate());
        passengersDataPage.fillCitizenship(1, CV.citizenship);
        passengersDataPage.setSex(1, client.getSex());
        passengersDataPage.fillDocData(1, client.getDocSN(), client.getDocExpDate(), search.isFakeDoc());

        passengersDataPage.fillCustomersData(2, client.getLastName(), client.getFirstNameChd(), client.getBirthDateChd());
        passengersDataPage.fillCitizenship(2, CV.citizenship);
        passengersDataPage.setSex(2, client.getSexChd());
        passengersDataPage.fillDocData(2, client.getDocSNChd(), client.getDocExpDateChd(), search.isFakeDocChld());

        passengersDataPage.fillEmail(CV.email);
        passengersDataPage.bookTicket();

        passengersDataPage.checkBookedTicketMessage(ticket.getPrice());
        String bookingCode = passengersDataPage.getBookingCode();
        ticket.setBookingId(bookingCode);

        passengersDataPage.openArchive();
        archivePage.checkTicketStatus(bookingCode, "Забронирован, не оплачен");
        closeTabAfterOpenArchivePage();
    }


    void front_15024(SearchData search, ClientDataItem client, TicketData ticket) {
        SearchResultsPage searchResultsPage = new SearchResultsPage();
        TicketInfoPage ticketInfoPage = new TicketInfoPage();
        CustomerContactDataPage customerContactDataPage = new CustomerContactDataPage();
        PassengersDataPage passengersDataPage = new PassengersDataPage();
        PaymentPage paymentPage = new PaymentPage();
        ArchivePage archivePage = new ArchivePage();

        String id = searchResultsPage.getIdOfSearchResults().get(0);

        String price = searchResultsPage.checkPresenceOfTicketsCost(id);
        ticket.setPrice(price);

        searchResultsPage.pressSelectButton(id);

        ticketInfoPage.waitForTicketRulesBtn();
        ticketInfoPage.checkTicketDetailsForDifficultRoute(2);

        if (search.getChannel().equalsIgnoreCase("Внешний Сайт")) {
            customerContactDataPage.checkPresenceOfContactDataBlock();
            customerContactDataPage.enterUserData();
        }

        passengersDataPage.checkAvaliabilityOfCustomersDataFields();
        passengersDataPage.checkPresenceOfTextElements(1, "Взрослый");
        passengersDataPage.checkPresenceAndAvaliabilityOfButtons();
        passengersDataPage.fillCustomersData(1, client.getLastName(), client.getFirstName(), client.getBirthDate());
        passengersDataPage.fillCitizenship(1, CV.citizenship);
        passengersDataPage.setSex(1, client.getSex());
        passengersDataPage.fillDocData(1, client.getDocSN(), client.getDocExpDate(), search.isFakeDoc());
        passengersDataPage.fillEmail(CV.email);
        passengersDataPage.buyTicket();

        paymentPage.title.shouldBe(visible);

        if (search.getChannel().equalsIgnoreCase("Внешний сайт")) {
            paymentPage.doPaymentByCard(new String[]{"", "", "", ""}, "08/2020", "000");
        } else {
            paymentPage.doPaymentByCard();
            String idTicket = paymentPage.getIdTicketAfterPayment();
            paymentPage.openArchive();
            archivePage.checkTicketStatus(idTicket, "Забронирован, не оплачен");
        }
    }


    //Добавление перелета при сложном маршруте (внешний сайт)
    void front_15091() {
        SearchResultsPage searchResultsPage = new SearchResultsPage();
        TicketInfoPage ticketInfoPage = new TicketInfoPage();

        String id = searchResultsPage.getIdOfSearchResults().get(0);

        searchResultsPage.checkPresenceOfTicketsCost(id);
        searchResultsPage.pressSelectButton(id);

        ticketInfoPage.waitForTicketRulesBtn();
        ticketInfoPage.checkTicketDetailsForDifficultRoute(3);
    }


    //Покупка билета для одного взрослого +- 3 дня (внешний сайт)
    void front_15848(SearchData search, ClientDataItem client, TicketData ticket) {
        SearchResultsPage searchResultsPage = new SearchResultsPage();
        TicketInfoPage ticketInfoPage = new TicketInfoPage();
        CustomerContactDataPage customerContactDataPage = new CustomerContactDataPage();
        PassengersDataPage passengersDataPage = new PassengersDataPage();
        PaymentPage paymentPage = new PaymentPage();
        ArchivePage archivePage = new ArchivePage();

        searchResultsPage.checkMatrixFlightsPresence();
//        searchResultsPage.checkResultsForPlusMinus3Days(search.getDaysFwd(), search.getDepartureCity(), search.getArrivalCity());

        String id = searchResultsPage.getIdOfSearchResults().get(0);
        String price = searchResultsPage.checkPresenceOfTicketsCost(id);
        ticket.setPrice(price);

        searchResultsPage.pressSelectButton(id);

        ticketInfoPage.waitForTicketRulesBtn();
        ticketInfoPage.checkTicketForwardDetails();
        ticketInfoPage.checkTicketBackwardDetails();

        if (search.getChannel().equalsIgnoreCase("Внешний Сайт")) {
            customerContactDataPage.checkPresenceOfContactDataBlock();
            customerContactDataPage.enterUserData();
        }

        passengersDataPage.checkAvaliabilityOfCustomersDataFields();
        passengersDataPage.checkPresenceOfTextElements(1, "Взрослый");
        passengersDataPage.checkPresenceAndAvaliabilityOfButtons();

        passengersDataPage.fillCustomersData(1, client.getLastName(), client.getFirstName(), client.getBirthDate());
        passengersDataPage.fillCitizenship(1, CV.citizenship);
        passengersDataPage.setSex(1, client.getSex());
        passengersDataPage.fillDocData(1, client.getDocSN(), client.getDocExpDate(), search.isFakeDoc());
        passengersDataPage.fillEmail(CV.email);
        passengersDataPage.buyTicket();

        paymentPage.title.shouldBe(visible);
        if (search.getChannel().equalsIgnoreCase("Внешний сайт")) {
            paymentPage.doPaymentByCard(new String[]{"", "", "", ""}, "08/2020", "000");
        } else {
            paymentPage.doPaymentByCard();
            String idTicket = paymentPage.getIdTicketAfterPayment();
            paymentPage.openArchive();
            archivePage.checkTicketStatus(idTicket, "Забронирован, не оплачен");
        }
    }


    //Оплата бронировки авиабилета в архиве
    void front_14506(TicketData ticket) {
        MainPage mainPage = new MainPage();
        ArchivePage archivePage = new ArchivePage();
        PaymentPage paymentPage = new PaymentPage();

        String currentChannel = mainPage.getCurrentChannel();
        if (currentChannel.equalsIgnoreCase("Внешний сайт")) {
            mainPage.openArchivePage(CV.phone);
        }

        archivePage.pressMoreInfoButton(ticket.getBookingId());
        archivePage.checkTicketMainInfoButtons();
        archivePage.checkTicketMainInfoServices();
        archivePage.checkCloseButton();

        archivePage.clickPayButton();
        archivePage.checkPassengersDataOnPaymentForm(ticket);
        archivePage.checkPresenceOfTotalTicketsCost();

        if (currentChannel.equalsIgnoreCase("Внешний сайт")) {
            paymentPage.doPaymentByCardFromArchive(null, null, null);
        } else {
            paymentPage.doPaymentByCard();
            assertEquals(ticket.getBookingId(), paymentPage.getIdTicketAfterPayment(), "Номера билетов не совпадают!");
        }
    }


    //Скачивание правил билета после покупки билета в архиве
    void front_14928(TicketData ticket) {
        MainPage mainPage = new MainPage();
        ArchivePage archivePage = new ArchivePage();

        String currentChannel = mainPage.getCurrentChannel();
        if (currentChannel.equalsIgnoreCase("Внешний сайт")) {
            mainPage.openArchivePage(CV.phone);
        }

        archivePage.pressMoreInfoButton(ticket.getBookingId());
        archivePage.checkTicketMainInfoButtons();
        archivePage.checkCloseButton();

        archivePage.downloadTicketRulesFile();
        Utils.waitUntilFileDownload("fare_conditions.pdf");
        String rules = Utils.pdfToString("fare_conditions.pdf");

        Assert.assertTrue(rules.contains("Условия возврата"), "Файл не содержит текст 'Условия возврата'");
        String fromCityToCity = String.format("%s - %s", CV.defaultDepartureCity, CV.defaultArrivalCity);
        Assert.assertTrue(rules.contains(fromCityToCity), "Файл 'Условия возврата' не содержит текст '" + fromCityToCity + "'");

        archivePage.downloadBookingDocument();
        Utils.waitUntilFileDownload("booking.doc");
        String bookingDoc = Utils.docToString("booking.doc");
        Assert.assertTrue(bookingDoc.contains("PASSENGER ITINERARY RECEIPT"), "Файл не содержит текст 'PASSENGER ITINERARY RECEIPT'");
        Assert.assertTrue(bookingDoc.contains("NAME: " + ticket.getOwnerFIO()), "Файл не содержит ФИО " + ticket.getOwnerFIO());
    }

    //Заказ доп. багажа
    void front_17753(TicketData ticket) {
        MainPage mainPage = new MainPage();
        ArchivePage archivePage = new ArchivePage();

        String currentChannel = mainPage.getCurrentChannel();
        if (currentChannel.equalsIgnoreCase("Внешний сайт")) {
            mainPage.openArchivePage(CV.phone);
        }

        archivePage.pressMoreInfoButton(ticket.getBookingId());
        archivePage.checkTicketMainInfoButtons();
        archivePage.checkTicketMainInfoServices();
        archivePage.checkCloseButton();

        archivePage.clickBaggageOrderButton();
        archivePage.orderBaggage(ticket.getBookingId());
    }

    //Заказ перевозки животных
    void front_17763(TicketData ticket) {
        MainPage mainPage = new MainPage();
        ArchivePage archivePage = new ArchivePage();

        String currentChannel = mainPage.getCurrentChannel();
        if (currentChannel.equalsIgnoreCase("Внешний сайт")) {
            mainPage.openArchivePage(CV.phone);
        }

        archivePage.pressMoreInfoButton(ticket.getBookingId());
        archivePage.checkTicketMainInfoButtons();
        archivePage.checkTicketMainInfoServices();
        archivePage.checkCloseButton();

        archivePage.clickPetsTransferButton();
        archivePage.orderPetsTransfer("Кошка", ticket.getBookingId());   //Собака или Кошка
    }

    //Заказ специального питания
    void front_17767(TicketData ticket) {
        MainPage mainPage = new MainPage();
        ArchivePage archivePage = new ArchivePage();

        String currentChannel = mainPage.getCurrentChannel();
        if (currentChannel.equalsIgnoreCase("Внешний сайт")) {
            mainPage.openArchivePage(CV.phone);
        }

        archivePage.pressMoreInfoButton(ticket.getBookingId());
        archivePage.checkTicketMainInfoButtons();
        archivePage.checkTicketMainInfoServices();
        archivePage.checkCloseButton();

        archivePage.clickFoodOrderButton();
        archivePage.orderFood("Диабетическое", ticket.getBookingId());  // Диабетическое или Вегетарианское
    }


    //Аннулиррование бронировки в архиве
    void front_17714(TicketData ticket) {
        MainPage mainPage = new MainPage();
        ArchivePage archivePage = new ArchivePage();

        String currentChannel = mainPage.getCurrentChannel();
        if (currentChannel.equalsIgnoreCase("Внешний сайт")) {
            mainPage.openArchivePage(CV.phone);
        }

        archivePage.pressMoreInfoButton(ticket.getBookingId());
        archivePage.checkTicketMainInfoButtons();
        archivePage.checkTicketMainInfoServices();
        archivePage.checkCloseButton();

        archivePage.clickStornBookingButton(ticket.getBookingId());
        archivePage.closeMainInfoBlock();

        if (currentChannel.equalsIgnoreCase("Внешний сайт")) {
            Utils.waitAndCheckForBookingStatusChanged(ticket.getBookingId(), "Отменён");
        } else {
            archivePage.checkTicketStatus(ticket.getBookingId(), "Отменён");
        }
    }


    /* Передача брони в архиве.
     *  После первой передачи - проверка удвления подвязанного номера.
     *  После второй передачи - идем в архив под новым номером и сторнируем бронировку.
     * */

    void front_19457(TicketData ticket) {
        MainPage mainPage = new MainPage();
        ArchivePage archivePage = new ArchivePage();

        mainPage.openArchivePage(CV.phone);

        archivePage.pressMoreInfoButton(ticket.getBookingId());
        archivePage.checkTicketMainInfoButtons();
        archivePage.checkTicketMainInfoServices();
        archivePage.checkCloseButton();

        archivePage.clickTransferBookingButton();
        archivePage.transferBooking(ticket.getBookingId(), CV.phone2);
        archivePage.deleteTransferedBooking();

        archivePage.clickTransferBookingButton();
        archivePage.transferBooking(ticket.getBookingId(), CV.phone2);

        mainPage.openArchivePage(CV.phone2.substring(1));
        archivePage.pressMoreInfoButton(ticket.getBookingId());
        archivePage.checkPassengersData(ticket);
        archivePage.checkTicketMainInfoButtonsForTransferedBooking();
        archivePage.checkTicketMainInfoServices();
        archivePage.checkCloseButton();

        archivePage.clickStornBookingButton(ticket.getBookingId());
        archivePage.closeMainInfoBlock();

        Utils.waitAndCheckForBookingStatusChanged(ticket.getBookingId(), "Отменён");

        mainPage.openArchivePage(CV.phone);
        Utils.waitAndCheckForBookingStatusChanged(ticket.getBookingId(), "Отменён");
    }


    // Фильтр багажа
    void front_19452() {
        TicketFilterPage ticketFilterPage = new TicketFilterPage();
        ticketFilterPage.checkPresenceOfFiltersButtons();
        ticketFilterPage.filterBaggageByAvailability();
        ticketFilterPage.filterBaggageByAbsence();
        ticketFilterPage.filterBaggageByPartialAvailability();
    }

    // Фильтр времени вылета/прилета
    void front_19453() {
        TicketFilterPage ticketFilterPage = new TicketFilterPage();
        ticketFilterPage.checkPresenceOfFiltersButtons();
        ticketFilterPage.filterTimeDeparture0006();
        ticketFilterPage.filterTimeDeparture0612();
        ticketFilterPage.filterTimeDeparture1218();
        ticketFilterPage.filterTimeDeparture1800();

        ticketFilterPage.filterTimeArrival0006();
        ticketFilterPage.filterTimeArrival0612();
        ticketFilterPage.filterTimeArrival1218();
        ticketFilterPage.filterTimeArrival1800();
    }

    // Фильтр по аєропортам
    void front_19454() {
        TicketFilterPage ticketFilterPage = new TicketFilterPage();
        ticketFilterPage.checkPresenceOfFiltersButtons();
        ticketFilterPage.filterLondonAirportDeparture();
        ticketFilterPage.filterLondonAirportArrival();
        ticketFilterPage.filterNewYorkAirportDeparture();
        ticketFilterPage.filterNewYorkAirportArrival();
    }

    //  Фильтр по авиакомпаниям
    void front_19455() {
        TicketFilterPage ticketFilterPage = new TicketFilterPage();
        ticketFilterPage.checkPresenceOfFiltersButtons();
        ticketFilterPage.filterAirline();
    }

    void front_19579() {
        TicketFilterPage ticketFilterPage = new TicketFilterPage();
        ticketFilterPage.checkPresenceOfFiltersButtons();
        ticketFilterPage.filterFlight();
    }


    void negativeSearchDeparture(String searchValue) {
        SelenideElement errorMsg = $x(".//*[@data-ng-messages='PlaneSearchForm.departure.$error']").shouldBe(visible);
        if ("".equalsIgnoreCase(searchValue)) {
            assertEquals(errorMsg.innerText().trim(), "Заполните поле");
        } else {
            assertEquals(errorMsg.innerText().trim(), "Выберите город из выпадающего списка");
        }
    }

    void negativeSearchArrival(String searchValue) {
        SelenideElement errorMsg = $x(".//*[@data-ng-messages='PlaneSearchForm.arrival.$error']").shouldBe(visible);
        if ("".equalsIgnoreCase(searchValue)) {
            assertEquals(errorMsg.innerText().trim(), "Заполните поле");
        } else {
            assertEquals(errorMsg.innerText().trim(), "Выберите город из выпадающего списка");
        }
    }

    boolean negativeAddMorePassengersThanItAllowed(SearchData search) {
        MainPage mainPage = new MainPage();
        SearchPage searchPage = new SearchPage();

        mainPage.openMainPage()
                .openSearchPageViaChannel(search.getChannel())
                .submitOpenFrame();

        Utils.switchFrame();

        try {
            searchPage.setPassengersCount(search.getAdultsCount(), search.getChildCount(), search.getInfantCount());
        } catch (AssertionError e) {
            log.info("Сработала проверка на неправильно установленное кол-во пассажиров");
            assertEquals(e.getMessage(), "Неправильно установили количество пассажиров expected [true] but found [false]");
            return true;
        }

        Assert.fail("Установилось количество пассажиров больше, чем положено");
        return false;
    }


    void negativeIncorrectBirthDate(DateTests dateTests) {
        SearchResultsPage searchResultsPage = new SearchResultsPage();
        TicketInfoPage ticketInfoPage = new TicketInfoPage();
        CustomerContactDataPage customerContactDataPage = new CustomerContactDataPage();
        PassengersDataPage passengersDataPage = new PassengersDataPage();

        String id = searchResultsPage.getIdOfSearchResults().get(0);
        searchResultsPage.pressSelectButton(id);
        ticketInfoPage.waitForTicketRulesBtn();
        customerContactDataPage.checkPresenceOfContactDataBlock();
        customerContactDataPage.enterUserData();

        passengersDataPage.checkErrorForBirthdayField(dateTests.getDateValue(), dateTests.getErrorText());
    }

    void negativeIncorrectCardData(SearchData search, ClientDataItem client) {
        SearchResultsPage searchResultsPage = new SearchResultsPage();
        TicketInfoPage ticketInfoPage = new TicketInfoPage();
        CustomerContactDataPage customerContactDataPage = new CustomerContactDataPage();
        PassengersDataPage passengersDataPage = new PassengersDataPage();
        PaymentPage paymentPage = new PaymentPage();

        String id = searchResultsPage.getIdOfSearchResults().get(0);

        searchResultsPage.pressSelectButton(id);

        ticketInfoPage.waitForTicketRulesBtn();

        customerContactDataPage.checkPresenceOfContactDataBlock();
        customerContactDataPage.enterUserData();

        passengersDataPage.checkAvaliabilityOfCustomersDataFields();
        passengersDataPage.checkPresenceOfTextElements(1, "Взрослый");
        passengersDataPage.checkPresenceAndAvaliabilityOfButtons();
        passengersDataPage.fillCustomersData(1, client.getLastName(), client.getFirstName(), client.getBirthDate());
        passengersDataPage.fillCitizenship(1, CV.citizenship);
        passengersDataPage.setSex(1, client.getSex());
        passengersDataPage.fillDocData(1, client.getDocSN(), client.getDocExpDate(), search.isFakeDoc());
        passengersDataPage.fillEmail(CV.email);
        passengersDataPage.buyTicket();

        paymentPage.title.shouldBe(visible);
        paymentPage.doPaymentByCard(new String[]{"", "", "", ""}, "08/2020", "000");
        paymentPage.checkEmptyPanFieldMessage();

        paymentPage.doPaymentByCard(new String[]{"1111", "1111", "1111", "111"}, "08/2020", "000");
        paymentPage.checkPartialFilledPanFieldMessage();

        paymentPage.doPaymentByCard(new String[]{"1111", "1111", "1111", "111d"}, "08/2020", "000");
        paymentPage.checkWrongFilledPanFieldMessage();

        paymentPage.doPaymentByCard(new String[]{"1111", "1111", "1111", "1111"}, "08/2020", "");
        paymentPage.checkEmptyCVV2FieldMessage();

        paymentPage.doPaymentByCard(new String[]{"1111", "1111", "1111", "1111"}, "08/2020", "00");
        paymentPage.checkPartialFilledCVV2FieldMessage();

        paymentPage.doPaymentByCard(new String[]{"1111", "1111", "1111", "1111"}, "08/2020", "sss");
        paymentPage.checkWrongFilledCVV2FieldMessage();

        paymentPage.doPaymentByCard(new String[]{"1111", "1111", "1111", "1111"}, "08/2020", "000");
        paymentPage.chechNotAgreedOfertaMessage();

        paymentPage.acceptOfertaRules();
        paymentPage.checkNoErrorMessagesPresent();
    }


    void negativeIncorrectPhoneNumber(TicketData ticket) {
        MainPage mainPage = new MainPage();
        ArchivePage archivePage = new ArchivePage();

        mainPage.openArchivePage(CV.phone);

        archivePage.pressMoreInfoButton(ticket.getBookingId());
        archivePage.checkTicketMainInfoButtons();
        archivePage.checkTicketMainInfoServices();
        archivePage.checkCloseButton();

        archivePage.clickTransferBookingButton();
        archivePage.transferBooking(ticket.getBookingId(), CV.phone2);
    }

    void stornBookings() {
        MainPage mainPage = new MainPage();
        ArchivePage archivePage = new ArchivePage();

        mainPage.openArchivePage(CV.phone);
        switchTo().defaultContent();
        $x(".//*[text()='Поиск']").waitUntil(visible.because("Кнопка 'Поиск' на главной странице архива билетов"), 30 * 1000);
        Utils.setCookieData();

        List<String> tickets_ids = archivePage.getTickets_id();
        if (tickets_ids.size() > 0) {
            tickets_ids.forEach(Utils::stornBookedTicket);
        } else log.info("Забронированных билетов не найдено");
    }

}