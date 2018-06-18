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
//@Feature("Бронирование билетов")
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
            s.setWaysType("Туда и обратно");
            s.setClassType("Эконом");
            s.setDepartureCity("Краков");
            s.setArrivalCity("Варшава");
            s.setDepartureDateForward(Utils.dateForFlightSearchResults(180));
            s.setDepartureDateBackward(Utils.dateForFlightSearchResults(185));
            s.setPassengersCount(1);
        });

        TicketData ticketData = new TicketData(t -> {
            t.setOwnerFIO(CV.lastName.toUpperCase() + " " + CV.firstName.toUpperCase());
        });

        log.info("Sid = " + CV.prominSession);
        testSuite.front_14514(searchData, ticketData);
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
        });

        TicketData ticketData = new TicketData(t -> {
            t.setOwnerFIO(CV.lastName.toUpperCase() + " " + CV.firstName.toUpperCase());
        });

        testSuite.front_12552(searchData, ticketData);

    }


}

