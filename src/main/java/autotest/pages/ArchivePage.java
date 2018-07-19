package autotest.pages;


import autotest.entity.TicketData;
import autotest.utils.ConfigurationVariables;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ArchivePage {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();

    private SelenideElement
            phoneInputField = $(By.name("phone")),
            submitPhoneBtn = $x(".//button[text()='Далее']"),
            otpcodeField = $(By.name("otpcode")),
            submitOtpBtn = $x(".//button[text()='Отправить']");

    private String alertText = "Заявка принята и будет обработана в течение 24 часов. Результат обработки будет направлен на e-mail, " +
            "который Вы указывали при покупке. Если для внесения изменений необходима доплата, ее необходимо произвести в день получения ответа.";

    @Step("Авторизация на форме архива")
    public void auth(){
        if(!$(By.xpath(".//*[text()='Авторизация']")).isDisplayed()) sleep(3000);
        if( $(By.xpath(".//*[text()='Авторизация']")).isDisplayed()) {
            phoneInputField.shouldBe(visible, enabled).setValue(CV.phone);
            submitPhoneBtn.click();
            otpcodeField.shouldBe(visible, enabled).setValue(CV.otp);
            submitOtpBtn.click();
            $(By.xpath(".//div[@class='tickets-filters']")).waitUntil(appear, 30 * 1000);
            $x(".//button[text()='Поиск']").should(appear);
        }
    }


    @Step("Проверим состояние забронированного билета")
    public void checkTicketStatus(String bookingCode, String expectedStatus){
        switchTo().window(1);
        switchTo().defaultContent();

        $$(By.xpath(".//section[contains(@id,'ticket-')]")).shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));
        String xPathStatus = String.format(".//*[text()='%s']/../*[@data-ng-bind='ticket.status_text']", bookingCode);
        String actualStatus = $(By.xpath(xPathStatus)).shouldBe(visible).getText().trim();
        Assert.assertEquals(actualStatus, expectedStatus);
        switchTo().window(1).close();
    }

    public List<String> getTickets_id(){
        $$x(".//*[contains(@id,'ticket-')]").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));
        ElementsCollection bookedTicketsId = $$x(".//*[text()='Забронирован, не оплачен']/ancestor::section");
        List<String> idList = new ArrayList<>();
        if(bookedTicketsId.size() > 0) {
            bookedTicketsId.forEach(element -> {
                String attrValue =  element.getAttribute("id");
                String id = attrValue.substring(attrValue.lastIndexOf("-")+1);
                idList.add(id);
            });
        }
        return idList;
    }


    @Step("Нажмем кнопку 'Подробнее' для бронировки {bookingId}")
    public void pressMoreInfoButton(String bookingId){
        $x(String.format(".//*[text()='%s']/following-sibling::*//button[text()='Подробнее']", bookingId)).shouldBe(visible, enabled).click();
        $x(".//*[text()='Основная информация']").shouldBe(visible);
        $x(String.format(".//*[text()='Ваше бронирование']/following-sibling::*[contains(text(),'%s')]", bookingId)).shouldBe(visible);
    }

    @Step("Проверим наличие кнопок: 'Оплатить', 'Аннулировать', 'Передать бронь', 'Бронировка', 'Правила билета'")
    public void checkTicketMainInfoButtons(){
        $x(".//*[text()='Оплатить']").shouldBe(exist, enabled);
        $x(".//*[text()='Аннулировать']").shouldBe(exist, enabled);
        $x(".//*[text()='Передать бронь']").shouldBe(exist, enabled);
        $x(".//*[text()='Бронировка']").shouldBe(exist, enabled);
        $x(".//*[text()='Правила билета']").shouldBe(exist, enabled);
    }

    @Step("Проверим наличие услуг: 'Багаж', 'Перевозка животных', 'Специальное питание' ")
    public void checkTicketMainInfoServices(){
        $x(".//button[@data-ng-show='vm.permissions.baggage']").shouldBe(exist, enabled.because("Кнопка Услуги/Багаж - Заказать"));
        $x(".//button[@data-ng-show='vm.permissions.pets']").shouldBe(exist, enabled.because("Кнопка Услуги/Перевозка животных - Заказать"));
        $x(".//button[@data-ng-show='vm.permissions.food']").shouldBe(exist, enabled.because("Кнопка Услуги/Специальное питание - Заказать"));
    }

    @Step("Проверим наличие кнопки 'Крестик', которая позволяет вернуться на этап выбора ранее купленных/забронированных локаторов")
    public void checkCloseButton(){
        $x(".//*[@data-ng-click='vm.back()']").shouldBe(exist, enabled.because("Кнопка закрытия формы бронирования"));
    }

    @Step("Кликнем кнопку 'Оплатить'")
    public void clickPayButton(){
        $x(".//*[text()='Оплатить']").click();
    }

    @Step("Нажмем кнопку 'Заказать багаж'")
    public void clickBaggageOrderButton(){
        $x(".//button[@data-ng-show='vm.permissions.baggage']").click();
    }

    @Step("Нажмем кнопку 'Перевозка животных - Заказать'")
    public void clickPetsTransferButton(){
        $x(".//button[@data-ng-show='vm.permissions.pets']").click();
    }


    @Step("Нажмем кнопку 'Специальное питание - Заказать'")
    public void clickFoodOrderButton(){
        $x(".//button[@data-ng-show='vm.permissions.food']").click();
    }


    @Step("Нажмем кнопку 'Аннулировать' ")
    public void clickStornBookingButton(String bookingId){
        $x(".//a[text()='Аннулировать']").click();
        $x(".//*[text()='Подтверждения действия']").shouldBe(visible);
        $x(".//*[text()='Вы уверены, что хотите отменить билет?']").shouldBe(visible);
        $x(".//*[@data-ng-click='modal.close()']").shouldBe(visible, enabled.because("Кнопка (крестик) отмены Аннуляции бронирования"));
        $x(".//*[text()='Подтвердить']").shouldBe(visible.because("Кнопка подтверждения отмены бронирования")).click();

        $x(String.format(".//*[text()='%s']", alertText)).waitUntil(appear, 30*1000);
        $x(".//*[text()='OK']").shouldBe(visible, enabled.because("Кнопка ОК")).click();

        $x(String.format(".//*[text()='Ваше бронирование']/..//*[contains(text(),'%s')]", bookingId)).shouldBe(visible);
        $x(".//a[text()='Аннулировать']").shouldNotBe(visible.because("После аннуляции кнопка должна пропасть"));
    }

    @Step("Закажем багаж для выбранного рейса")
    public void orderBaggage(String bookingId){
        $x(".//*[text()='Шаг 1. Выберите рейс, на который хотите купить багаж']").shouldBe(visible);
        $x(".//*[@for='checkbox-baggage-0']").shouldBe(visible, enabled).click();

        $x(".//*[text()='Шаг 2. Выберите пассажиров']").shouldBe(visible);
        $x(".//*[@for='checkbox-baggage-passenger-0']").shouldBe(visible, enabled);
        String checked = executeJavaScript("return angular.element(document.getElementById('checkbox-baggage-passenger-0'))[0].form[2].checked").toString();
        if (checked.equalsIgnoreCase("false")) {
            $x(".//*[@for='checkbox-baggage-passenger-0']").shouldBe(visible, enabled).click();
        }

        $x(".//*[text()='Шаг 3. Введите комментарий']").shouldBe(visible);
        $(By.name("comment")).shouldBe(visible, enabled).setValue("Пара-тройка небольших клетчатых сумок по 3-4 кг каждая");

        $x(".//*[@ng-message='required']").shouldNotBe(visible.because("Рейс или пассажир не выбран на форме"));
        $x(".//*[text()='Отправить заявку']").shouldBe(visible, enabled).click();

        $x(".//*[text()='Успешно']").waitUntil(exist, 45 * 1000);
        $x(".//*[@data-ng-click='modal.close()']").should(exist.because("Кнопка закрытия модального окна"));
        $x(String.format(".//*[text()='%s']", alertText)).shouldBe(visible);
        $x(".//*[text()='OK']").shouldBe(visible, enabled.because("Кнопка ОК")).click();
        $x(".//*[text()='Основная информация']").waitUntil(visible, 30 * 1000);
        $x(String.format(".//*[text()='Ваше бронирование']/..//*[contains(text(),'%s')]", bookingId)).shouldBe(visible);
    }


    @Step("Закажем багаж для выбранного рейса")
    public void orderPetsTransfer(String pet, String bookingId){
        $x(".//*[text()='Шаг 1. Выберите тип животного']").shouldBe(visible);
        $x(String.format(".//*[text()='%s']", pet)).shouldBe(visible, enabled.because("Radiobtn Кошка/Собака")).click();

        $x(".//*[text()='Шаг 2. Укажите размер клетки']").shouldBe(visible);
        $(By.name("h")).shouldBe(visible, enabled).setValue("20");
        $(By.name("w")).shouldBe(visible, enabled).setValue("30");
        $(By.name("l")).shouldBe(visible, enabled).setValue("40");

        $x(".//*[text()='Шаг 3. Укажите вес питомца вместе с клеткой']").shouldBe(visible);
        $(By.name("weight")).shouldBe(visible, enabled).setValue("5");

        $x(".//*[text()='Шаг 4. На каких рейсах будет лететь питомец?']").shouldBe(visible);
        $x(".//*[@for='checkbox-baggage-0']").shouldBe(visible, enabled);
        String checked = executeJavaScript("return angular.element(document.getElementById('checkbox-baggage-0'))[0].form[6].checked").toString();
        if (checked.equalsIgnoreCase("false")) {
            $x(".//*[@for='checkbox-baggage-0']").click();
        }

        $x(".//*[text()='Шаг 5. Введите комментарий (при необходимости)']").shouldBe(visible);
        $(By.name("comment")).shouldBe(visible, enabled).setValue("Кот-пес");

        $x(".//*[text()='Отправить заявку']").shouldBe(visible, enabled.because("Кнопка 'Отправить заявку'")).click();
        $x(".//*[@data-ng-message='required']").shouldNotBe(visible.because("Размер клетки или вес или рейс не указан на форме"));

        $x(".//*[text()='Успешно']").waitUntil(exist, 45 * 1000);
        $x(".//*[@data-ng-click='modal.close()']").should(exist.because("Кнопка закрытия модального окна"));
        $x(String.format(".//*[text()='%s']", alertText)).shouldBe(visible);
        $x(".//*[text()='OK']").shouldBe(visible, enabled.because("Кнопка ОК")).click();
        $x(".//*[text()='Основная информация']").waitUntil(visible, 30 * 1000);
        $x(String.format(".//*[text()='Ваше бронирование']/..//*[contains(text(),'%s')]", bookingId)).shouldBe(visible);
    }

    @Step("Закажем специальное питание для выбранного рейса")
    public void orderFood(String foodType, String bookingId){
        $x(".//*[text()='Шаг 1. Выберите тип питания']").shouldBe(visible);
        $x(String.format(".//*[text()='%s']", foodType)).shouldBe(visible, enabled.because("Radiobtn Кошерное/Вегетарианское/Детское/Диабетическое")).click();
        $x(".//*[text()='Шаг 2. Введите комментарий (при необходимости)']").shouldBe(visible);
        $(By.name("comment")).shouldBe(visible, enabled).setValue("Кофе без сахара, овощи с говядиной");

        $x(".//*[text()='Отправить заявку']").shouldBe(visible, enabled.because("Кнопка 'Отправить заявку'")).click();
        $x(".//*[@class='text-error']").shouldNotBe(visible.because("Не выбрали тип питания"));

        $x(".//*[text()='Успешно']").waitUntil(exist, 45 * 1000);
        $x(".//*[@data-ng-click='modal.close()']").should(exist.because("Кнопка закрытия модального окна"));
        $x(String.format(".//*[text()='%s']", alertText)).shouldBe(visible);
        $x(".//*[text()='OK']").shouldBe(visible, enabled.because("Кнопка ОК")).click();
        $x(".//*[text()='Основная информация']").waitUntil(visible, 30 * 1000);
        $x(String.format(".//*[text()='Ваше бронирование']/..//*[contains(text(),'%s')]", bookingId)).shouldBe(visible);
    }

    @Step("Проверим отображение данных о пассажирах на форме покупки билета в архиве")
    public void checkPassengersDataOnPaymentForm(TicketData ticket){
        $x(".//*[text()='Данные о пассажирах']").shouldBe(visible);

        Assert.assertTrue($(By.name("lastname")).shouldBe(visible, disabled).getValue().equalsIgnoreCase(ticket.getClientDataItem().getLastName()),
                 "Фамилия клиента в архиве не соответствует введенной при бронировании");
        Assert.assertTrue($(By.name("firstname")).shouldBe(visible, disabled).getValue().equalsIgnoreCase(ticket.getClientDataItem().getFirstName()),
                "Имя клиента в архиве не соответствует введенному при бронировании");
        Assert.assertTrue($(By.name("birthday")).shouldBe(visible, disabled).getValue().equalsIgnoreCase(ticket.getClientDataItem().getBirthDate()),
                "Дата рождения клиента в архиве не соответствует введенной при бронировании");
        Assert.assertTrue($(By.name("citizenship")).shouldBe(visible, disabled).getValue().equalsIgnoreCase("Ukraine"),
                "Гражданство клиента в архиве не соответствует введенному при бронировании");
        Assert.assertTrue($(By.name("docnum")).shouldBe(visible, disabled).getValue().equalsIgnoreCase(ticket.getClientDataItem().getDocSN()),
                "Серия, № документа в архиве не соответствует введенному при бронировании");
        Assert.assertTrue($(By.name("doc_expire_date")).shouldBe(visible, disabled).getValue().equalsIgnoreCase(ticket.getClientDataItem().getDocExpDate()),
                "Срок действия документа в архиве не соответствует введенному при бронировании");
    }

    @Step("Проверим отображение стоимости билета")
    public void checkPresenceOfTotalTicketsCost(){
        $x(".//*[text()='Общая стоимость:']").shouldBe(visible);
        String price = $x(".//*[@data-ng-bind='vm.product.formatTotal']").shouldBe(visible).getText();
        Assert.assertFalse(price.isEmpty(), "Не отобразилась стоимость билета");
    }





}