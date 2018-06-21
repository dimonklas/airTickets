package autotest.pages;


import autotest.utils.ConfigurationVariables;
import autotest.utils.exception.NotClickedException;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class PassengersDataPage {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();

    private SelenideElement
            passengersDataText = $(By.xpath(".//h2[text()='Данные о пассажирах']")),
            preloader = $(By.xpath(".//*[class='circle-spinner']")),
            preloaderBaggage = $(By.xpath(".//*[contains(text(),'Получение дополнительного багажа')]")),
            bookingMessageText = $(By.xpath(".//*[text()='Вы забронировали авиабилет стоимостью']")),
            errorText = $(By.xpath(".//*[@class='text-error'][text()='Проверьте заполнение пассажирских данных']"));


    private SelenideElement
            backBtn = $(By.xpath(".//*[@id='details'] //*[text()='Назад']/parent::a")),
            bookingBtn = $(By.xpath(".//button[contains(@class,'button-booking')]")),
            buyBtn = $(By.xpath(".//button[contains(@class,'button-buy')]"));



    @Step("Проверим наличие и доступность основных полей ввода данных")
    public void checkAvaliabilityOfCustomersDataFields() {
        passengersDataText.shouldBe(visible).scrollIntoView(true);
        preloader.waitUntil(disappear, 60*1000);

        try {
            preloaderBaggage.shouldBe(visible).waitUntil(disappear, 180 * 1000);
        } catch (com.codeborne.selenide.ex.ElementShould ex) {
            preloaderBaggage.should(disappears);
        }

        $(By.xpath(".//*[@name='lastname']")).shouldBe(visible, enabled);
        $(By.xpath(".//*[@name='firstname']")).shouldBe(visible, enabled);
        $(By.xpath(".//label[@title='Мужской']")).shouldBe(visible, enabled);
        $(By.xpath(".//label[@title='Женский']")).shouldBe(visible, enabled);
        $(By.xpath(".//*[@name='birthday']")).shouldBe(visible, enabled);
        $(By.xpath(".//*[@name='citizenship']")).shouldBe(visible, enabled);
        $(By.xpath(".//label[@for='docs-0']")).shouldBe(visible, enabled);
        $(By.xpath(".//label[@for='withExpireDateLabel-0']")).shouldBe(visible, enabled);
        $(By.xpath(".//label[@for='isBonusCard-0']")).shouldBe(visible, enabled);
//        buyBaggageChkbox.shouldBe(visible, enabled);
        $(By.xpath(".//*[@name='docnum']")).shouldBe(visible, enabled);
        $(By.xpath(".//*[@name='doc_expire_date']")).shouldBe(visible, enabled);
        $(By.xpath("(.//*[@name='bonus_card'])[1]")).shouldBe(visible);
        $(By.xpath(".//*[@name='email']")).shouldBe(visible, enabled);
    }



    @Step("Проверим основные надписи для полей ввода данных")
    public void checkPresenceOfTextElements(int passNo, String passType){
        $(By.xpath(String.format("(.//*[@data-ng-bind='passenger.title'])[%s]", passNo))).shouldBe(visible).should(matchesText(passType));

        $(By.xpath(String.format("(.//label[text()='Фамилия'])[%s]", passNo))).shouldBe(visible);
        $(By.xpath(String.format("(.//label[text()='Имя'])[%s]", passNo))).shouldBe(visible);
        $(By.xpath(String.format("(.//label[text()='Пол'])[%s]", passNo))).shouldBe(visible);
        $(By.xpath(String.format("(.//label[text()='Дата рождения'])[%s]", passNo))).shouldBe(visible);
        $(By.xpath(String.format("(.//label[text()='Гражданство'])[%s]", passNo))).shouldBe(visible);
        $(By.xpath(String.format("(.//label[text()='Серия, № документа'])[%s]", passNo))).shouldBe(visible);
        $(By.xpath(String.format("(.//label[text()='Срок действия'])[%s]", passNo))).shouldBe(visible);
        $(By.xpath(String.format("(.//label[text()='Мильная карта'])[%s]", passNo))).shouldBe(visible);
//        $(By.xpath(String.format("(.//label[text()='Докупить багаж'])[%s]", passNo))).shouldBe(visible);
        $(By.xpath(String.format("(.//*[text()='Фамилия/имя должны совпадать с данными паспорта'])[%s]", passNo))).shouldBe(visible);
        $(By.xpath(String.format("(.//*[text()='Отключите поле, если нет при себе паспорта'])[%s]", passNo))).shouldBe(visible);
        $(By.xpath(String.format("(.//*[text()='Для документов без срока действия отключите поле'])[%s]", passNo))).shouldBe(visible);

        $(By.xpath(".//*[text()='Укажите e-mail']")).shouldBe(visible);
        $(By.xpath(".//*[text()='На него будет выслан электронный билет']")).shouldBe(visible);

    }

    @Step("Проверим наличие кнопок 'Назад', 'Забронировать (бесплатно)', 'Купить' и текста со сроком бронирования")
    public void checkPresenceAndAvaliabilityOfButtons(){
        backBtn.shouldBe(visible, enabled);
        bookingBtn.shouldBe(visible, enabled);
        buyBtn.shouldBe(visible, enabled);
        Assert.assertTrue(
                $(By.xpath(".//*[@data-ng-bind='vm.bookingData.expireTimeBefore']")).shouldBe(visible)
                        .getText().contains("Действует до"));
        $(By.xpath(".//*[text()='Общая стоимость:']")).shouldBe(visible);
        $(By.xpath(".//*[@data-ng-bind='vm.product.formatTotal']")).shouldBe(visible).shouldNotHave(exactText(""));
    }


    @Step("Заполним клиентские данные: ФИО, пол, дата рождения и гражданство")
    public void fillCustomersData(int passNo, String surname, String name, String bDate){
        $(By.xpath(String.format("(.//*[@name='lastname'])[%s]", passNo))).shouldBe(visible).setValue(surname);
        $(By.xpath(String.format("(.//*[@name='firstname'])[%s]", passNo))).setValue(name);
        $(By.xpath(String.format("(.//*[@name='birthday'])[%s]", passNo))).setValue(bDate);
    }

    @Step("Укажем гражданство")
    public void fillCitizenship(int passNo, String citizenship){
        $(By.xpath(String.format("(.//*[@name='citizenship'])[%s]", passNo))).setValue(citizenship);
        $(By.xpath(String.format(".//strong[text()='%s']/ancestor::li", citizenship))).shouldBe(visible).click();
        $(By.xpath(".//*[contains(@ng-messages,'citizenship')] //*[contains(text(),'Заполните поле')]")).shouldNotBe(visible);
    }

    @Step("Установим пол")
    public void setSex(int passNo, String sex){
        if("F".equalsIgnoreCase(sex)) $(By.xpath(String.format("(.//label[@title='Женский'])[%s]", passNo))).click();
        else $(By.xpath(String.format("(.//label[@title='Мужской'])[%s]", passNo))).click();
    }

    @Step("Заполним данные документов")
    public void fillDocData(int passNo, String serNum, String expDate){
        if(serNum != null) {
            String id = String.format("docs-%s", passNo-1);
            if( !$(By.id(id)).isSelected() ) $(By.xpath(String.format(".//label[@for='%s']", id))).shouldBe(visible).click();
            $(By.xpath(String.format("(.//*[@name='docnum'])[%s]", passNo))).shouldBe(enabled).setValue(serNum);
        }

        if(expDate != null) {
            String id = String.format("withExpireDateLabel-%s", passNo-1);
            if( !$(By.id(id)).isSelected() ) $(By.xpath(String.format(".//label[@for='%s']", id))).shouldBe(visible).click();
            $(By.xpath(String.format("(.//*[@name='doc_expire_date'])[%s]", passNo))).shouldBe(enabled).setValue(expDate);
        }
    }

    @Step("Укажем мильную карту")
    public void fillMileCard(int passNo, String mileCardNumber){
        if (mileCardNumber != null) {
            String id = String.format("isBonusCard-%s", passNo-1);
            if(!$(By.id(id)).isSelected()) $(By.xpath(String.format(".//label[@for='%s']", id))).click();
            $(By.xpath(String.format("(.//*[@name='bonus_card'])[%s]", passNo))).shouldBe(enabled).setValue(mileCardNumber);
        }
    }

    @Step("Заполним поле e-mail")
    public void fillEmail(String email){
        $(By.xpath(".//*[@name='email']")).setValue(email);
    }

    @Step("Нажмем кнопку 'Забронировать (бесплатно)'")
    public void bookTicket(){
        bookingBtn.shouldBe(visible).scrollIntoView(true).click();
        sleep(100);
        errorText.shouldNot(appear);
        if ($x(".//*[contains(text(),'произошла ошибка') and contains(text(),'Обновите страницу и попробуйте заново')]").isDisplayed()){
            throw new NotClickedException("Ошибка при бронировании билета");
        }
        bookingMessageText.waitUntil(appear, 120*1000).shouldBe(visible).scrollTo();
    }

    @Step("Нажмем кнопку 'Купить'")
    public void buyTicket(){
        buyBtn.shouldBe(visible).scrollIntoView(true).click();
        sleep(100);
        errorText.shouldNot(appear);
        $x(".//*[text()='Выберите способ оплаты']").shouldBe(visible);
    }


    @Step("Проверим данные после бронирования")
    public void checkBookedTicketMessage(String price){
        String text = "Вы забронировали авиабилет стоимостью " + price + ",00" + " грн";
        $$(By.xpath(".//*[contains(@class,'booking-message__text-header')]/span")).forEach(e -> {
            Assert.assertTrue(text.contains(e.getText()), "В сообщении о бронировании билета некорректно отобразился текст");
        });
    }

    @Step("Проверим отображение кода бронирования билета")
    public String getBookingCode(){
        String code = $(By.xpath(".//*[text()='Код бронирования']/following-sibling::p[@data-ng-bind='locator']")).shouldBe(visible).getText();
        Assert.assertFalse(code.isEmpty(), "В сообщении о бронировании билета не отобразился код бронирования");
        return code;
    }

    @Step("Перейдем в архив билетов")
    public void openArchive(){
        $(By.linkText("Архив билетов")).shouldBe(visible, enabled).click();
//
//        String currUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
//        Assert.assertTrue(currUrl.contains("https://bilet-dev.isto.it.loc/archive/frame/exsite/?csid="), "Не перешли в архив");
    }

}