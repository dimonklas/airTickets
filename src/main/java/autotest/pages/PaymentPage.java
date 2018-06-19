package autotest.pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class PaymentPage {

    SelenideElement
            title = $(By.xpath(".//*[text()='Выберите способ оплаты']"));

}
