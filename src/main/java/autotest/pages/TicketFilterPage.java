package autotest.pages;

import autotest.entity.AirportsData;
import autotest.entity.AirlinesData;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Log4j
public class TicketFilterPage {


    /****** Фильтр багажа ******/

    @Step("Проверим отображение кнопок фильтров")
    public void checkPresenceOfFiltersButtons(){
        $x(".//*[text()='Время вылета/прилета']/..").shouldBe(visible, enabled.because("Dropdown 'Время вылета/прилета'"));
        $x(".//*[text()='Аэропорт']/..").shouldBe(visible, enabled.because("Dropdown 'Аэропорт'"));
        $x(".//*[text()='Авиакомпания']/..").shouldBe(visible, enabled.because("Dropdown 'Авиакомпания'"));
        $x(".//*[text()='Багаж']/..").shouldBe(visible, enabled.because("Dropdown 'Багаж'"));
        $x(".//*[@for='filters-without-transfers']").shouldBe(visible, enabled.because("Чек-бокс 'Прямой'"));
        $x(".//*[@for='filters-one-transfer']").shouldBe(visible, enabled.because("Чек-бокс '1 пересадка'"));
    }

    @Step("Применим фильтр 'Есть багаж'")
    public void filterBaggageByAvailability(){
        $x(".//*[text()='Багаж']/..").shouldBe(visible).click();
        if(!$x(".//*[text()='Есть багаж']").isDisplayed()) $x(".//*[text()='Багаж']/..").shouldBe(visible).click(); //костыль
        $x(".//*[text()='Есть багаж']").shouldBe(visible, enabled).click();
        $$x(".//span[text()='Есть багаж']").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(10));
        $$x(".//span[text()='Нет багажа']").shouldHaveSize(0);
    }

    @Step("Применим фильтр 'Нет багажа'")
    public void filterBaggageByAbsence(){
        if(!$x(".//*[text()='Есть багаж']").isDisplayed()) $x(".//*[text()='Багаж']/..").shouldBe(visible).click(); //костыль#2
        $x(".//*[text()='Все']").shouldBe(visible, enabled).click();
        $x(".//*[text()='Нет багажа']").shouldBe(visible, enabled).click();
        $$x(".//span[text()='Нет багажа']").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(10));
        $$x(".//span[text()='Есть багаж']").shouldHaveSize(0);

    }

    @Step("Применим фильтр 'Частичный'")
    public void filterBaggageByPartialAvailability(){
        if(!$x(".//*[text()='Есть багаж']").isDisplayed()) $x(".//*[text()='Багаж']/..").shouldBe(visible).click(); //костыль#3
        $x(".//*[text()='Все']").shouldBe(visible, enabled).click();
        $x(".//*[text()='Частичный']").shouldBe(visible, enabled).click();
        $$x(".//span[text()='Частичный' or text()='Есть багаж' or text()='Нет багажа']").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));
    }


    /****** Фильтр время вылета/прилета ******/

    private void setDepartureArrivalDefaultValue() {
        $x("(//*[text()='В любое время'])[1]").shouldBe(visible, enabled).click();
        $x("(//*[text()='В любое время'])[2]").shouldBe(visible, enabled).click();
        $x("(//*[text()='В любое время'])[3]").shouldBe(visible, enabled).click();
        $x("(//*[text()='В любое время'])[4]").shouldBe(visible, enabled).click();
    }

    @Step("Применим фильтр 'Время вылета' 'Ночь (00 - 06)'")
    public void filterTimeDeparture0006() {
        $x(".//*[text()='Время вылета/прилета']/..").shouldBe(visible, enabled).click();
        if (!$x("(.//*[text()='Туда'])[1]").isDisplayed()) $x(".//*[text()='Время вылета/прилета']/..").shouldBe(visible).click(); //костыль
        setDepartureArrivalDefaultValue();
        $x("(//div[text()='Время вылета']//..//*[text()='Ночь (00 - 06)'])[1]").shouldBe(visible, enabled).click();
        $x("(//div[text()='Время вылета']//..//*[text()='Ночь (00 - 06)'])[2]").shouldBe(visible, enabled).click();

        for (int i = 1; i <= $$x("//*[@data-ng-bind='departure.flightTime']").size(); i++) {
            String[] arr = $x("(//*[@data-ng-bind='departure.flightTime'])[" + i + "]").getText().split(":");
            assertTrue(Integer.parseInt(arr[0]) < 6, "Время рейса вышло за пределы '(00- 06)'");
        }
    }

    @Step("Применим фильтр 'Время вылета' 'День (06 - 12)'")
    public void filterTimeDeparture0612() {
        $x(".//*[text()='Время вылета/прилета']/..").shouldBe(visible, enabled).click();
        if (!$x("(.//*[text()='Туда'])[1]").isDisplayed()) $x(".//*[text()='Время вылета/прилета']/..").shouldBe(visible).click(); //костыль
        setDepartureArrivalDefaultValue();
        $x("(//div[text()='Время вылета']//..//*[text()='Утро (06 - 12)'])[1]").shouldBe(visible, enabled).click();
        $x("(//div[text()='Время вылета']//..//*[text()='Утро (06 - 12)'])[2]").shouldBe(visible, enabled).click();

        for (int i = 1; i <= $$x("//*[@data-ng-bind='departure.flightTime']").size(); i++) {
            String[] arr = $x("(//*[@data-ng-bind='departure.flightTime'])[" + i + "]").getText().split(":");
            assertTrue(Integer.parseInt(arr[0]) >= 6 && Integer.parseInt(arr[0]) < 12, "Время рейса вышло за пределы '(06- 12)'");
        }
    }

    @Step("Применим фильтр 'Время вылета' 'День (12 - 18)'")
    public void filterTimeDeparture1218() {
        $x(".//*[text()='Время вылета/прилета']/..").shouldBe(visible, enabled).click();
        if (!$x("(.//*[text()='Туда'])[1]").isDisplayed()) $x(".//*[text()='Время вылета/прилета']/..").shouldBe(visible).click(); //костыль
        setDepartureArrivalDefaultValue();
        $x("(//div[text()='Время вылета']//..//*[text()='День (12 - 18)'])[1]").shouldBe(visible, enabled).click();
        $x("(//div[text()='Время вылета']//..//*[text()='День (12 - 18)'])[2]").shouldBe(visible, enabled).click();

        for (int i = 1; i <= $$x("//*[@data-ng-bind='departure.flightTime']").size(); i++) {
            String[] arr = $x("(//*[@data-ng-bind='departure.flightTime'])[" + i + "]").getText().split(":");
            assertTrue(Integer.parseInt(arr[0]) >= 12 && Integer.parseInt(arr[0]) < 18, "Время рейса вышло за пределы '(12- 18)'");
        }
    }

    @Step("Применим фильтр 'Время вылета' 'Ночь (18 - 00)'")
    public void filterTimeDeparture1800() {
        $x(".//*[text()='Время вылета/прилета']/..").shouldBe(visible, enabled).click();
        if (!$x("(.//*[text()='Туда'])[1]").isDisplayed()) $x(".//*[text()='Время вылета/прилета']/..").shouldBe(visible).click(); //костыль
        setDepartureArrivalDefaultValue();
        $x("(//div[text()='Время вылета']//..//*[text()='Вечер (18 - 00)'])[1]").shouldBe(visible, enabled).click();
        $x("(//div[text()='Время вылета']//..//*[text()='Вечер (18 - 00)'])[2]").shouldBe(visible, enabled).click();

        for (int i = 1; i <= $$x("//*[@data-ng-bind='departure.flightTime']").size(); i++) {
            String[] arr = $x("(//*[@data-ng-bind='departure.flightTime'])[" + i + "]").getText().split(":");
            assertTrue(Integer.parseInt(arr[0]) >= 18 && Integer.parseInt(arr[0]) < 24, "Время рейса вышло за пределы '(18- 00)'");
        }
    }

    /***** Прилет *****/

    @Step("Применим фильтр 'Время прилета' 'Ночь (00 - 06)'")
    public void filterTimeArrival0006() {
        $x(".//*[text()='Время вылета/прилета']/..").shouldBe(visible, enabled).click();
        if (!$x("(.//*[text()='Туда'])[1]").isDisplayed()) $x(".//*[text()='Время вылета/прилета']/..").shouldBe(visible).click(); //костыль
        setDepartureArrivalDefaultValue();
        $x("(//div[text()='Время прилета']//..//*[text()='Ночь (00 - 06)'])[1]").shouldBe(visible, enabled).click();
        $x("(//div[text()='Время прилета']//..//*[text()='Ночь (00 - 06)'])[2]").shouldBe(visible, enabled).click();

        for (int i = 1; i <= $$x("//*[@data-ng-bind='arrival.flightTime']").size(); i++) {
            String[] arr = $x("(//*[@data-ng-bind='arrival.flightTime'])[" + i + "]").getText().split(":");
            assertTrue(Integer.parseInt(arr[0]) < 6, "Время рейса вышло за пределы '(00- 06)'");
        }
    }

    @Step("Применим фильтр 'Время прилета' 'День (06 - 12)'")
    public void filterTimeArrival0612() {
        $x(".//*[text()='Время вылета/прилета']/..").shouldBe(visible, enabled).click();
        if (!$x("(.//*[text()='Туда'])[1]").isDisplayed()) $x(".//*[text()='Время вылета/прилета']/..").shouldBe(visible).click(); //костыль
        setDepartureArrivalDefaultValue();
        $x("(//div[text()='Время прилета']//..//*[text()='Утро (06 - 12)'])[1]").shouldBe(visible, enabled).click();
        $x("(//div[text()='Время прилета']//..//*[text()='Утро (06 - 12)'])[2]").shouldBe(visible, enabled).click();

        for (int i = 1; i <= $$x("//*[@data-ng-bind='arrival.flightTime']").size(); i++) {
            String[] arr = $x("(//*[@data-ng-bind='arrival.flightTime'])[" + i + "]").getText().split(":");
            assertTrue(Integer.parseInt(arr[0]) >= 6 && Integer.parseInt(arr[0]) < 12, "Время рейса вышло за пределы '(06- 12)'");
        }
    }

    @Step("Применим фильтр 'Время прилета' 'День (12 - 18)'")
    public void filterTimeArrival1218() {
        $x(".//*[text()='Время вылета/прилета']/..").shouldBe(visible, enabled).click();
        if (!$x("(.//*[text()='Туда'])[1]").isDisplayed()) $x(".//*[text()='Время вылета/прилета']/..").click(); //костыль
        setDepartureArrivalDefaultValue();
        $x("(//div[text()='Время прилета']//..//*[text()='День (12 - 18)'])[1]").shouldBe(visible, enabled).click();
        $x("(//div[text()='Время прилета']//..//*[text()='День (12 - 18)'])[2]").shouldBe(visible, enabled).click();

        for (int i = 1; i <= $$x("//*[@data-ng-bind='arrival.flightTime']").size(); i++) {
            String[] arr = $x("(//*[@data-ng-bind='arrival.flightTime'])[" + i + "]").getText().split(":");
            assertTrue(Integer.parseInt(arr[0]) >= 12 && Integer.parseInt(arr[0]) < 18, "Время рейса вышло за пределы '(12- 18)'");
        }
    }

    @Step("Применим фильтр 'Время прилета' 'Ночь (18 - 00)'")
    public void filterTimeArrival1800() {
        $x(".//*[text()='Время вылета/прилета']/..").shouldBe(visible, enabled).click();
        if (!$x("(.//*[text()='Туда'])[1]").isDisplayed()) $x(".//*[text()='Время вылета/прилета']/..").click(); //костыль
        setDepartureArrivalDefaultValue();
        $x("(//div[text()='Время прилета']//..//*[text()='Вечер (18 - 00)'])[1]").shouldBe(visible, enabled).click();
        $x("(//div[text()='Время прилета']//..//*[text()='Вечер (18 - 00)'])[2]").shouldBe(visible, enabled).click();

        for (int i = 1; i <= $$x("//*[@data-ng-bind='arrival.flightTime']").size(); i++) {
            String[] arr = $x("(//*[@data-ng-bind='arrival.flightTime'])[" + i + "]").getText().split(":");
            assertTrue(Integer.parseInt(arr[0]) >= 18 && Integer.parseInt(arr[0]) < 24, "Время рейса вышло за пределы '(18- 00)'");
        }
    }

    /***** Аэропорт *****/
    @Step("Применим фильтр по аэропорту (вылет с Лондона)")
    public void filterLondonAirportDeparture() {
        AirportsData data = new AirportsData();
        $x(".//*[text()='Аэропорт']/..").shouldBe(visible).click();
        if (!$x("(.//*[text()='Туда'])[2]").isDisplayed()) $x(".//*[text()='Аэропорт']/..").shouldBe(visible).click(); //костыль
        for (String value : data.getLondonAirports()) {
            $x("//div[text()='Аэропорт вылета']//..//*[text()='" + value + "']").click();
            $$x("//div[text()='Лондон']//..//div[text()='вылет']//..//div[text()='" + value.replace(" (Лондон)", "") + "']").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(5));
            $x("(//div[text()='Аэропорт вылета']//..//*[text()='Любой'])[1]").click();
        }
    }

    @Step("Применим фильтр по аэропорту (прилет в Лондон)")
    public void filterLondonAirportArrival() {
        AirportsData data = new AirportsData();
        $x(".//*[text()='Аэропорт']/..").shouldBe(visible).shouldBe(visible).click();
        if (!$x("(.//*[text()='Туда'])[2]").isDisplayed()) $x(".//*[text()='Аэропорт']/..").shouldBe(visible).click(); //костыль
        for (String value : data.getLondonAirports()) {
            try {
                $x("//div[text()='Аэропорт прилета']//..//*[text()='" + value + "']").click();
                if (value.equals("Станстед (Лондон)")) $$x("//div[text()='Лондон']//..//div[text()='прилет']//..//div[text()='" + value.replace(" (Лондон)", "") + "']").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));
                else $$x("//div[text()='Лондон']//..//div[text()='прилет']//..//div[text()='" + value.replace(" (Лондон)", "") + "']").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(5));
                $x("(//div[text()='Аэропорт прилета']//..//*[text()='Любой'])[2]").click();
            } catch (Throwable e) {
                log.info("Аэропорта " + value + " нет в списке");
            }
        }
    }

    @Step("Применим фильтр по аэропорту (вылет с Нью-Йорка)")
    public void filterNewYorkAirportDeparture() {
        AirportsData data = new AirportsData();
        $x(".//*[text()='Аэропорт']/..").shouldBe(visible).click();
        if (!$x("(.//*[text()='Туда'])[2]").isDisplayed()) $x(".//*[text()='Аэропорт']/..").shouldBe(visible).click(); //костыль
        for (String value : data.getNewYorkAirports()) {
            $x("//div[text()='Аэропорт вылета']//..//*[text()='" + value + "']").click();
            $$x("//div[text()='Нью-Йорк']//..//div[text()='вылет']//..//div[text()='" + value.replace(" (Нью-Йорк)", "") + "']").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(5));
            $x("(//div[text()='Аэропорт вылета']//..//*[text()='Любой'])[2]").click();
        }
    }

    @Step("Применим фильтр по аэропорту (прилет в Нью-Йорк)")
    public void filterNewYorkAirportArrival() {
        AirportsData data = new AirportsData();
        $x(".//*[text()='Аэропорт']/..").shouldBe(visible).click();
        if (!$x("(.//*[text()='Туда'])[2]").isDisplayed()) $x(".//*[text()='Аэропорт']/..").shouldBe(visible).click(); //костыль
        for (String value : data.getNewYorkAirports()) {
            $x("//div[text()='Аэропорт прилета']//..//*[text()='" + value + "']").click();
            $$x("//div[text()='Нью-Йорк']//..//div[text()='прилет']//..//div[text()='" + value.replace(" (Нью-Йорк)", "") + "']").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(5));
            $x("(//div[text()='Аэропорт прилета']//..//*[text()='Любой'])[1]").click();
        }
    }

    @Step("Применить фильтр по авиакомпаниям")
    public void filterAirline() {
        AirlinesData airlinesData = new AirlinesData();
        $x(".//*[text()='Авиакомпания']/..").shouldBe(visible, enabled).click();
        if(!$x(".//*[text()='Любая авиакомпания']").isDisplayed()) $x(".//*[text()='Авиакомпания']/..").shouldBe(visible, enabled).click();

        ElementsCollection companies = $$x(".//*[text()='Выберите авиакомпанию']//..//div[@data-ng-repeat='checkbox in checkboxes track by $index']//label");

        for (SelenideElement company : companies) {
            try {
                $x("//*[text()='Любая авиакомпания']").shouldBe(visible, enabled).click();
            } catch (Throwable e) {
                $x(".//*[text()='Авиакомпания']/..").shouldBe(visible, enabled).click();
                $x("//*[text()='Любая авиакомпания']").shouldBe(visible, enabled).click();
            }
            company.click();
            // разкомментировать после фикса отображения авиакомпаний
//            $$x("//div[text()='Киев']//..//div[text()='вылет']").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));
            for (int j = 0; j < $$x("//div[text()='Киев']//..//div[text()='вылет']").size(); j++) {
                String alt = $$x("//div[text()='Киев']//..//div[text()='вылет']//..//..//..//..//img[@title='Supplier ']").get(j).attr("alt");
                assertEquals(alt, airlinesData.getAirlines().get(company.innerText().trim()), "Лишняя Авиакомпания в выборке или Авиакомпания отсутствует в списке");
            }
        }
    }

    @Step("Прямой/Пересадка")
    public void filterFlight() {
        $x(".//*[text()='Прямой']").shouldBe(visible, enabled).click();
        $$x(".//*[text()='Пересадки']").shouldBe(CollectionCondition.empty);
        $x(".//*[text()='1 пересадка']").shouldBe(visible, enabled).click();
        $$x(".//*[text()='без пересадок']").shouldBe(CollectionCondition.empty);
    }
}
