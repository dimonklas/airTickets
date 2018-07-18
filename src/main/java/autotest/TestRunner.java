package autotest;

import autotest.dto.custData.ClientDataItem;
import autotest.entity.BookedTickets;
import autotest.entity.SearchData;
import autotest.entity.TicketData;
import autotest.utils.ConfigurationVariables;
import autotest.utils.Utils;
import autotest.utils.listeners.AllureOnFailListener;
import autotest.utils.listeners.RunTestAgainIfFailed;
import io.qameta.allure.Epic;
import io.qameta.allure.Link;
import lombok.extern.log4j.Log4j;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;


@Log4j
@Epic("Сайт покупки авиабилетов (регрессионное тестирование крит. функционала)")
@Listeners({AllureOnFailListener.class})
public class TestRunner extends SetUpAndTearDown {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();
    private TestSuite testSuite = new TestSuite();

    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-14514: Бронировка авиабилета для одного взрослого (Внешний Сайт)",
            groups = {"Покупка билетов"},
            priority = 10)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-14514")
    public void a1_front_14514(){
        log.info(">>>> a1_front_14514() is running...");
        SearchData searchData = new SearchData(s -> {
            s.setChannel("Внешний Сайт");
            s.setWaysType("Туда и обратно");
            s.setClassType("Эконом");
            s.setDepartureCity("Краков");
            s.setArrivalCity("Варшава");
            s.setDaysFwd(180);
            s.setDaysBckwd(185);
            s.setDepartureDateForward(Utils.dateForFlightSearchResults(180));
            s.setDepartureDateBackward(Utils.dateForFlightSearchResults(185));
            s.setPassengersCount(1);
        });

        ClientDataItem cl = CV.clientData.get(Utils.randomCl());

        TicketData ticketData = new TicketData(t -> {
            t.setOwnerFIO(cl.getLastName().toUpperCase() + " " + cl.getFirstName().toUpperCase());
            t.setClientDataItem(cl);
        });

        testSuite.searchTickets(searchData);
        testSuite.bookTickets(searchData, cl, ticketData);
    }


    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-12552:Покупка авиабилета для взрослого и ребенка (внешний сайт)",
            groups = {"Покупка билетов"},
            priority = 20)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-12552")
    public void a2_front_12552(){
        log.info(">>>> a2_front_12552() is running...");
        SearchData searchData = new SearchData(s -> {
            s.setChannel("Внешний Сайт");
            s.setWaysType("Туда и обратно");
            s.setClassType("Эконом");
            s.setDepartureCity("Краков");
            s.setArrivalCity("Варшава");
            s.setDaysFwd(180);
            s.setDaysBckwd(185);
            s.setDepartureDateForward(Utils.dateForFlightSearchResults(180));
            s.setDepartureDateBackward(Utils.dateForFlightSearchResults(185));
            s.setPassengersCount(2);
            s.setChildCount(1);
        });

        ClientDataItem cl = CV.clientData.get(Utils.randomCl());

        TicketData ticketData = new TicketData(t -> {
            t.setOwnerFIO(cl.getLastName().toUpperCase() + " " + cl.getFirstName().toUpperCase());
            t.setClientDataItem(cl);
        });

        testSuite.searchTickets(searchData);
        testSuite.front_12552(searchData, cl, ticketData);
    }


    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-15024:Покупка авиабилета для одного взрослого сложный маршрут ( внешний сайт )",
            groups = {"Покупка билетов"},
            priority = 30)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-15024")
    public void a3_front_15024(){
        log.info(">>>> a3_front_15024() is running...");
        SearchData searchData = new SearchData(s -> {
            s.setChannel("Внешний Сайт");
            s.setWaysType("Сложный маршрут");
            s.setClassType("Эконом");
            s.setDepartureCity("Москва");
            s.setArrivalCity("Минск");
            s.setDepartureCity_2("Киев");
            s.setArrivalCity_2("Мюнхен");
            s.setDaysForDifficult_1(180);
            s.setDaysForDifficult_2(210);
            s.setDepartureDateForward(Utils.dateForFlightSearchResults(180));
            s.setDepartureDateBackward(Utils.dateForFlightSearchResults(210));
            s.setPassengersCount(1);
        });

        ClientDataItem cl = CV.clientData.get(Utils.randomCl());

        TicketData ticketData = new TicketData(t -> {
            t.setOwnerFIO(cl.getLastName().toUpperCase() + " " + cl.getFirstName().toUpperCase());
            t.setClientDataItem(cl);
        });

        testSuite.searchTickets(searchData);
        testSuite.front_15024(searchData, cl, ticketData);
    }


    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-15091:Добавление перелета при сложном маршруте (внешний сайт)",
            groups = {"Покупка билетов"},
            priority = 40)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-15091")
    public void a4_front_15091(){
        log.info(">>>> a4_front_15091() is running...");
        SearchData searchData = new SearchData(s -> {
            s.setChannel("Внешний Сайт");
            s.setWaysType("Сложный маршрут");
            s.setClassType("Эконом");
            s.setDepartureCity("Москва");
            s.setArrivalCity("Минск");
            s.setDepartureCity_2("Киев");
            s.setArrivalCity_2("Мюнхен");
            s.setDepartureCity_3("Стамбул");
            s.setArrivalCity_3("Киев");
            s.setDepartureCity_4("Киев");
            s.setArrivalCity_4("Мадрид");
            s.setNeedRemoveLastRoute(true);
            s.setDaysForDifficult_1(180);
            s.setDaysForDifficult_2(190);
            s.setDaysForDifficult_3(210);
            s.setPassengersCount(1);
        });

        testSuite.searchTickets(searchData);
        testSuite.front_15091();
    }


    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-15848:Покупка билета для одного взрослого +- 3 дня (внешний сайт)",
            groups = {"Покупка билетов"},
            priority = 50)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-15848")
    public void a5_front_15848(){
        log.info(">>>> a5_front_15848() is running...");
        SearchData searchData = new SearchData(s -> {
            s.setChannel("Внешний Сайт");
            s.setWaysType("Туда и обратно");
            s.setPlusMinus3days(true);
            s.setClassType("Эконом");
            s.setDepartureCity("Краков");
            s.setArrivalCity("Варшава");
            s.setDepartureDateForward(Utils.dateForFlightSearchResults(180));
            s.setDepartureDateBackward(Utils.dateForFlightSearchResults(180));
            s.setDaysFwd(180);
            s.setDaysBckwd(180);
            s.setPassengersCount(1);
        });

        ClientDataItem cl = CV.clientData.get(Utils.randomCl());

        TicketData ticketData = new TicketData(t -> {
            t.setOwnerFIO(cl.getLastName().toUpperCase() + " " + cl.getFirstName().toUpperCase());
            t.setClientDataItem(cl);
        });

        testSuite.searchTickets(searchData);
        testSuite.front_15848(searchData, cl, ticketData);
    }


    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-14517:Бронировка билета для взрослого и младенца (внешний сайт)",
            groups = {"Покупка билетов"},
            priority = 60)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-14517")
    public void a6_front_14517(){
        log.info(">>>> a6_front_14517() is running...");
        SearchData searchData = new SearchData(s -> {
            s.setChannel("Внешний Сайт");
            s.setWaysType("Туда и обратно");
            s.setClassType("Эконом");
            s.setDepartureCity("Краков");
            s.setArrivalCity("Варшава");
            s.setDaysFwd(180);
            s.setDaysBckwd(185);
            s.setDepartureDateForward(Utils.dateForFlightSearchResults(180));
            s.setDepartureDateBackward(Utils.dateForFlightSearchResults(185));
            s.setPassengersCount(2);
            s.setInfantCount(1);
        });

        ClientDataItem cl = CV.clientData.get(Utils.randomCl());

        TicketData ticketData = new TicketData(t -> {
            t.setOwnerFIO(cl.getLastName().toUpperCase() + " " + cl.getFirstName().toUpperCase());
            t.setClientDataItem(cl);
        });

        testSuite.searchTickets(searchData);
        testSuite.bookTickets(searchData, cl, ticketData);
    }

    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-15720:Покупка авиабилета для одного пассажира с фейковыми документами с не заполнением данных(внешний сайт)",
            groups = {"Покупка билетов"},
            priority = 70)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-15720")
    public void a7_front_15720(){
        log.info(">>>> a7_front_15720() is running...");
        SearchData searchData = new SearchData(s -> {
            s.setChannel("Внешний Сайт");
            s.setWaysType("Туда и обратно");
            s.setClassType("Эконом");
            s.setDepartureCity("Краков");
            s.setArrivalCity("Варшава");
            s.setDaysFwd(183);
            s.setDaysBckwd(188);
            s.setDepartureDateForward(Utils.dateForFlightSearchResults(183));
            s.setDepartureDateBackward(Utils.dateForFlightSearchResults(188));
            s.setFakeDoc(true);
        });

        ClientDataItem cl = CV.clientData.get(Utils.randomCl());

        TicketData ticketData = new TicketData(t -> {
            t.setOwnerFIO(cl.getLastName().toUpperCase() + " " + cl.getFirstName().toUpperCase());
            t.setClientDataItem(cl);
        });

        testSuite.searchTickets(searchData);
        testSuite.bookTickets(searchData, cl, ticketData);
    }


    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-14506:Оплата бронировки авиабилета в архиве",
            groups = {"Архив билетов"},
            priority = 110)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-14506")
    public void b1_front_14506(){
        log.info(">>>> b1_front_14506() is running...");

        if(BookedTickets.getTicketsList().size() < 1) {
            a1_front_14514();
        }

        testSuite.front_14506(BookedTickets.getTicketsList().get(0));
    }


    @Test(  enabled = false,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-14928:Скачивание правил билета после покупки билета в архиве (Архив)",
            groups = {"Архив билетов"},
            priority = 130)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-14928")
    public void b2_front_14928(){
        log.info(">>>> b2_front_14928() is running...");

        if(BookedTickets.getTicketsList().size() < 1) {
            a1_front_14514();
        }

//        testSuite.front_14928(BookedTickets.getTicketsList().get(0));
    }


    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-17753:Заказ багажа в архиве",
            groups = {"Архив билетов"},
            priority = 130)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-17753")
    public void b3_front_17753(){
        log.info(">>>> b3_front_17753() is running...");

        if(BookedTickets.getTicketsList().size() < 1) {
            a1_front_14514();
        }

        testSuite.front_17753(BookedTickets.getTicketsList().get(0));
    }


    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-17763:Заказ перевозки животных в архиве (Архив)",
            groups = {"Архив билетов"},
            priority = 140)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-17763")
    public void b4_front_17763(){
        log.info(">>>> b4_front_17763() is running...");

        if(BookedTickets.getTicketsList().size() < 1) {
            a1_front_14514();
        }

        testSuite.front_17763(BookedTickets.getTicketsList().get(0));
    }

    @Test(  enabled = false,
            description = "тестовый тест",
            groups = {"тест билетов"},
            priority = 7000)
    public void testEmail() throws FileNotFoundException {
        log.info(">>>> Запуск тестового теста...");
//        Utils.pdfToString("");
    }

}