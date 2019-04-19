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

@Log4j
@Epic("Сайт покупки авиабилетов (регрессионное тестирование крит. функционала) 24")
@Listeners({AllureOnFailListener.class})
public class TestRunnerP24 extends SetUpAndTearDown {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();
    private TestSuite testSuite = new TestSuite();

    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-14514: Бронировка авиабилета для одного взрослого (П24)",
            groups = {"Покупка билетов"},
            priority = 10)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-14514")
    public void a1_front_14514(){
        log.info(">>>> a1_front_14514() is running...");
        SearchData searchData = new SearchData(s -> {
            s.setChannel("П24");
            s.setWaysType("Туда и обратно");
            s.setClassType("Эконом");
            s.setDepartureCity("Киев");
            s.setArrivalCity("Харьков");
            s.setDaysFwd(180);
            s.setDaysBckwd(185);
            s.setDepartureDateForward(Utils.getDateForFlightSearchResults(180));
            s.setDepartureDateBackward(Utils.getDateForFlightSearchResults(185));
            s.setPassengersCount(1);
        });

        ClientDataItem cl = CV.clientData.get(Utils.randomCl());

        TicketData ticketData = new TicketData(t -> {
            t.setOwnerFIO(cl.getLastName().toUpperCase() + " " + cl.getFirstName().toUpperCase());
            t.setClientDataItem(cl);
        });

        testSuite.performSearch(searchData);
        testSuite.bookTickets(searchData, cl, ticketData);
    }

    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-12552:Покупка авиабилета для взрослого и ребенка (П24)",
            groups = {"Покупка билетов"},
            priority = 20)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-12552")
    public void a2_front_12552(){
        log.info(">>>> a2_front_12552() is running...");
        SearchData searchData = new SearchData(s -> {
            s.setChannel("П24");
            s.setWaysType("Туда и обратно");
            s.setClassType("Эконом");
            s.setDepartureCity("Киев");
            s.setArrivalCity("Харьков");
            s.setDaysFwd(180);
            s.setDaysBckwd(185);
            s.setDepartureDateForward(Utils.getDateForFlightSearchResults(180));
            s.setDepartureDateBackward(Utils.getDateForFlightSearchResults(185));
            s.setPassengersCount(2);
            s.setChildCount(1);
        });

        ClientDataItem cl = CV.clientData.get(Utils.randomCl());
        TicketData ticketData = new TicketData(t -> {
            t.setOwnerFIO(cl.getLastName().toUpperCase() + " " + cl.getFirstName().toUpperCase());
            t.setClientDataItem(cl);
        });

        testSuite.performSearch(searchData);
        testSuite.front_12552(searchData, cl, ticketData);
    }

    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-15024:Покупка авиабилета для одного взрослого сложный маршрут ( П24 )",
            groups = {"Покупка билетов"},
            priority = 30)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-15024")
    public void a3_front_15024(){
        log.info(">>>> a3_front_15024() is running...");
        SearchData searchData = new SearchData(s -> {
            s.setChannel("П24");
            s.setWaysType("Сложный маршрут");
            s.setClassType("Эконом");
            s.setDepartureCity("Москва");
            s.setArrivalCity("Минск");
            s.setDepartureCity_2("Минск");
            s.setArrivalCity_2("Киев");
            s.setDaysForDifficult_1(180);
            s.setDaysForDifficult_2(210);
            s.setDepartureDateForward(Utils.getDateForFlightSearchResults(180));
            s.setDepartureDateBackward(Utils.getDateForFlightSearchResults(210));
            s.setPassengersCount(1);
        });

        ClientDataItem cl = CV.clientData.get(Utils.randomCl());

        TicketData ticketData = new TicketData(t -> {
            t.setOwnerFIO(cl.getLastName().toUpperCase() + " " + cl.getFirstName().toUpperCase());
            t.setClientDataItem(cl);
        });

        testSuite.performSearch(searchData);
        testSuite.front_15024(searchData, cl, ticketData);
    }

    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-15091:Добавление перелета при сложном маршруте (П24)",
            groups = {"Покупка билетов"},
            priority = 40)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-15091")
    public void a4_front_15091(){
        log.info(">>>> a4_front_15091() is running...");
        SearchData searchData = new SearchData(s -> {
            s.setChannel("П24");
            s.setWaysType("Сложный маршрут");
            s.setClassType("Эконом");
            s.setDepartureCity("Москва");
            s.setArrivalCity("Минск");
            s.setDepartureCity_2("Минск");
            s.setArrivalCity_2("Киев");
            s.setDepartureCity_3("Киев");
            s.setArrivalCity_3("Рига");
            s.setDepartureCity_4("Рига");
            s.setArrivalCity_4("Хельсинки");
            s.setNeedRemoveLastRoute(true);
            s.setDaysForDifficult_1(180);
            s.setDaysForDifficult_2(190);
            s.setDaysForDifficult_3(210);
            s.setPassengersCount(1);
        });

        testSuite.performSearch(searchData);
        testSuite.front_15091();
    }

    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-15848:Покупка билета для одного взрослого +- 3 дня (П24)",
            groups = {"Покупка билетов"},
            priority = 50)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-15848")
    public void a5_front_15848(){
        log.info(">>>> a5_front_15848() is running...");
        SearchData searchData = new SearchData(s -> {
            s.setChannel("П24");
            s.setWaysType("Туда и обратно");
            s.setPlusMinus3days(true);
            s.setClassType("Эконом");
            s.setDepartureCity("Киев");
            s.setArrivalCity("Харьков");
            s.setDepartureDateForward(Utils.getDateForFlightSearchResults(180));
            s.setDepartureDateBackward(Utils.getDateForFlightSearchResults(180));
            s.setDaysFwd(180);
            s.setDaysBckwd(180);
            s.setPassengersCount(1);
        });

        ClientDataItem cl = CV.clientData.get(Utils.randomCl());

        TicketData ticketData = new TicketData(t -> {
            t.setOwnerFIO(cl.getLastName().toUpperCase() + " " + cl.getFirstName().toUpperCase());
            t.setClientDataItem(cl);
        });

        testSuite.performSearch(searchData);
        testSuite.front_15848(searchData, cl, ticketData);
    }

    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-14517:Бронировка билета для взрослого и младенца (П24)",
            groups = {"Покупка билетов"},
            priority = 60)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-14517")
    public void a6_front_14517(){
        log.info(">>>> a6_front_14517() is running...");
        SearchData searchData = new SearchData(s -> {
            s.setChannel("П24");
            s.setWaysType("Туда и обратно");
            s.setClassType("Эконом");
            s.setDepartureCity("Киев");
            s.setArrivalCity("Харьков");
            s.setDaysFwd(180);
            s.setDaysBckwd(185);
            s.setDepartureDateForward(Utils.getDateForFlightSearchResults(180));
            s.setDepartureDateBackward(Utils.getDateForFlightSearchResults(185));
            s.setPassengersCount(2);
            s.setInfantCount(1);
        });

        ClientDataItem cl = CV.clientData.get(Utils.randomCl());

        TicketData ticketData = new TicketData(t -> {
            t.setOwnerFIO(cl.getLastName().toUpperCase() + " " + cl.getFirstName().toUpperCase());
            t.setClientDataItem(cl);
        });

        testSuite.performSearch(searchData);
        testSuite.bookTickets(searchData, cl, ticketData);
    }

    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-15720:Покупка авиабилета для одного пассажира с фейковыми документами с не заполнением данных(П24)",
            groups = {"Покупка билетов"},
            priority = 70)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-15720")
    public void a7_front_15720(){
        log.info(">>>> a7_front_15720() is running...");
        SearchData searchData = new SearchData(s -> {
            s.setChannel("П24");
            s.setWaysType("Туда и обратно");
            s.setClassType("Эконом");
            s.setDepartureCity("Киев");
            s.setArrivalCity("Харьков");
            s.setDaysFwd(183);
            s.setDaysBckwd(188);
            s.setDepartureDateForward(Utils.getDateForFlightSearchResults(183));
            s.setDepartureDateBackward(Utils.getDateForFlightSearchResults(188));
            s.setFakeDoc(true);
        });

        ClientDataItem cl = CV.clientData.get(Utils.randomCl());

        TicketData ticketData = new TicketData(t -> {
            t.setOwnerFIO(cl.getLastName().toUpperCase() + " " + cl.getFirstName().toUpperCase());
            t.setClientDataItem(cl);
        });

        testSuite.performSearch(searchData);
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

    @Test(  enabled = true,
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

        testSuite.front_14928(BookedTickets.getTicketsList().get(0));
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

    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-17767:Заказ специального питания в архиве (Архив)",
            groups = {"Архив билетов"},
            priority = 150)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-17767")
    public void b5_front_17767(){
        log.info(">>>> b5_front_17767() is running...");

        if(BookedTickets.getTicketsList().size() < 1) {
            a1_front_14514();
        }

        testSuite.front_17767(BookedTickets.getTicketsList().get(0));
    }

    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-17714:Аннулирование брони авиабилета в архиве",
            groups = {"Архив билетов"},
            priority = 170)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-17714")
    public void b7_front_17714(){
        log.info(">>>> b7_front_17714() is running...");

        if(BookedTickets.getTicketsList().size() < 1) {
            a1_front_14514();
        }
        TicketData ticketData = BookedTickets.getTicketsList().get(0);
        BookedTickets.getTicketsList().remove(0);
        testSuite.front_17714(ticketData);
    }

}
