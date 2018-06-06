package autotest.pages;


import autotest.utils.ConfigurationVariables;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CustomerContactDataPage {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();

    private SelenideElement
        contDataText = $(By.xpath(".//h2[text()='Контактные данные покупателя']")),
        selectUserText = $(By.xpath(".//*[text()='Выберите пользователя']")),
        filledCustomerBtn = $(By.xpath(String.format(".//*[contains(text(),'%s') and contains(text(),'***')]", CV.firstName))),
        otherCustomerBtn = $(By.xpath(".//*[text()='Другой пользователь']")),
        phoneFieldText1 = $(By.xpath(".//*[text()='Укажите Ваш номер телефона']")),
        phoneFieldText2 = $(By.xpath(".//*[text()='на него будет выслан SMS с кодом']")),
        inputPhoneField = $(By.xpath(".//input[@name='phone']")),
        inputOtpCodeField = $(By.xpath(".//input[@name='otpcode']")),
        sendBtn = $(By.xpath(".//button[text()='Отправить']")),
        backBtn = $(By.xpath(".//button[text()='Назад']")),
        backBtn2 = $(By.xpath("(.//button[text()='Назад'])[2]")),
        nextBtn = $(By.xpath(".//button[text()='Далее']")),
        qrCode = $(By.xpath(".//img[contains(@src,'qrapi.privatbank.ua')]")),
        qrCodeText = $(By.xpath(".//*[text()='QR-код для входа через смартфон']")),
        otpText1 = $(By.xpath(".//*[text()='На этот номер отправлено сообщение с одноразовым паролем']")),
        otpText2 = $(By.xpath(".//*[text()='Не приходит SMS']")),
        otpText3 = $(By.xpath(".//*[text()='Если Вы не получили SMS, авторизуйтесь с помощью звонка из банка']")),
        pnoneNumberText = $(By.xpath(String.format(".//*[text()='%s']", CV.phone))),
        callBackBtn = $(By.linkText("Нажмите тут")),
        callBackBtn2 = $(By.linkText("Позвонить мне"));


    private ElementsCollection
        usersButtons = $$(By.xpath(".//*[contains(@class,'authentication-board__item')]"));


    @Step("Проверим наличие блока 'Контактные данные покупателя' и его основных элементов")
    public void checkPresenceOfContactDataBlock(){
        contDataText.shouldBe(visible).scrollTo();

        if(selectUserText.isDisplayed()) {
            usersButtons.shouldBe(CollectionCondition.sizeGreaterThanOrEqual(2));
            usersButtons.forEach(s -> s.shouldBe(visible, enabled));
            otherCustomerBtn.shouldBe(visible, enabled);
        } else {
            inputPhoneField.shouldBe(visible, enabled);
            phoneFieldText1.shouldBe(visible);
            phoneFieldText2.shouldBe(visible);
            backBtn.shouldBe(visible, enabled);
            nextBtn.shouldBe(visible, enabled);
            qrCode.shouldBe(visible);
            qrCodeText.shouldBe(visible);
        }
    }


    @Step("Заполним клиентские данные")
    public void enterUserData(){
        if(inputPhoneField.isDisplayed()) {
            fillDataForNewCustomer();
        } else {
            selectCustomerDataFilledBefore();
        }
    }

    @Step("Данных о клиенте нет - авторизуемся и введем вручную")
    private void fillDataForNewCustomer(){
        inputPhoneField.setValue(CV.phone);
        nextBtn.click();

        otpText1.shouldBe(visible);
        otpText2.shouldBe(visible);
        pnoneNumberText.shouldBe(visible);
        callBackBtn.shouldBe(visible, enabled);
        inputOtpCodeField.shouldBe(visible, enabled).setValue(CV.otp);
        backBtn2.shouldBe(visible, enabled);
        sendBtn.shouldBe(visible, enabled).click();
    }

    @Step("Клиентские данные уже были введены ранее - заполним их по кнопке")
    private void selectCustomerDataFilledBefore(){
        filledCustomerBtn.click();
    }


}
