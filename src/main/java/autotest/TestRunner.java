package autotest;

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

        TicketData ticketData = new TicketData(t -> {
            t.setOwnerFIO(CV.lastName.toUpperCase() + " " + CV.firstName.toUpperCase());
        });

        log.info("Sid = " + CV.prominSession);
        testSuite.bookTickets(searchData, ticketData);
    }


    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-12552:Покупка авиабилета для взрослого и ребенка (внешний сайт)",
            groups = {"Покупка билетов"},
            priority = 20)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-12552")
    public void a2_front_12552(){

        SearchData searchData = new SearchData(s -> {
            s.setWaysType("Туда и обратно");
            s.setClassType("Эконом");
            s.setDepartureCity("Краков");
            s.setArrivalCity("Варшава");
            s.setDepartureDateForward(Utils.dateForFlightSearchResults(180));
            s.setDepartureDateBackward(Utils.dateForFlightSearchResults(185));
            s.setPassengersCount(2);
            s.setChildCount(1);
        });

        TicketData ticketData = new TicketData(t -> {
            t.setOwnerFIO(CV.lastName.toUpperCase() + " " + CV.firstName.toUpperCase());
        });

        testSuite.front_12552(searchData, ticketData);
    }


    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-15024:Покупка авиабилета для одного взрослого сложный маршрут ( внешний сайт )",
            groups = {"Покупка билетов"},
            priority = 30)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-15024")
    public void a3_front_15024(){

        SearchData searchData = new SearchData(s -> {
            s.setWaysType("Сложный маршрут");
            s.setClassType("Эконом");
            s.setDepartureCity("Москва");
            s.setArrivalCity("Минск");
            s.setDepartureCity_2("Киев");
            s.setArrivalCity_2("Мюнхен");
            s.setDepartureDateForward(Utils.dateForFlightSearchResults(180));
            s.setDepartureDateBackward(Utils.dateForFlightSearchResults(210));
            s.setPassengersCount(1);
        });

        TicketData ticketData = new TicketData(t -> {
            t.setOwnerFIO(CV.lastName.toUpperCase() + " " + CV.firstName.toUpperCase());
        });

        testSuite.front_15024(searchData, ticketData);
    }


    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-15091:Добавление перелета при сложном маршруте (внешний сайт)",
            groups = {"Покупка билетов"},
            priority = 40)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-15091")
    public void a4_front_15091(){
        SearchData searchData = new SearchData(s -> {
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
            s.setPassengersCount(1);
        });

        testSuite.front_15091(searchData);
    }


    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-15848:Покупка билета для одного взрослого +- 3 дня (внешний сайт)",
            groups = {"Покупка билетов"},
            priority = 50)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-15848")
    public void a5_front_15848(){
        SearchData searchData = new SearchData(s -> {
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

        TicketData ticketData = new TicketData(t -> {
            t.setOwnerFIO(CV.lastName.toUpperCase() + " " + CV.firstName.toUpperCase());
        });

        testSuite.front_15848(searchData, ticketData);
    }


    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-14517:Бронировка билета для взрослого и младенца (внешний сайт)",
            groups = {"Покупка билетов"},
            priority = 60)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-14517")
    public void a6_front_14517(){
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

        TicketData ticketData = new TicketData(t -> {
            t.setOwnerFIO(CV.lastName.toUpperCase() + " " + CV.firstName.toUpperCase());
        });

        testSuite.bookTickets(searchData, ticketData);
    }

    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-15720:Покупка авиабилета для одного пассажира с фейковыми документами с не заполнением данных(внешний сайт)",
            groups = {"Покупка билетов"},
            priority = 70)
    @Link(name = "Ссылка на ТК", url = "https://testlink.privatbank.ua/linkto.php?tprojectPrefix=front&item=testcase&id=front-15720")
    public void a7_front_15720(){
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
//            s.setPassengersCount(1);
            s.setFakeDoc(true);
        });

        TicketData ticketData = new TicketData(t -> {
            t.setOwnerFIO(CV.lastName.toUpperCase() + " " + CV.firstName.toUpperCase());
        });

        testSuite.bookTickets(searchData, ticketData);
    }

}