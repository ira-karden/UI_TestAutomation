import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;
import org.testng.annotations.*;


import static com.codeborne.selenide.Selenide.*;

public class SelenideTests {

    @BeforeMethod
    public void setUp(){
        open ("https://makeup.com.ua/");
    }

    @AfterGroups("addingItemToTheCart")
    public void afterGroup(){
        $(By.xpath("//span[text()='Корзина']")).click();
        $(" .product-list__item:nth-child(1) .product__button-remove").click();
        $(By.xpath("//div[@class='popup__window'] // div[@class='popup-close close-icon']")).click();
        $(By.xpath("// div[@class='product__header'][contains(text(), 'Givenchy')]")).shouldBe(Condition.disappear);
    }

    @Test(priority = 1, groups = {"addingItemToTheCart"})
    public void addingItemToTheCartFromListing(){ //Проверка добавления продукта в корзину из выдачи
        $(By.xpath("//input[@itemprop='query-input']")).click();
        $(By.xpath("//input[@itemprop='query-input']")).setValue("Givenchy").pressEnter();
        $(".catalog-products").scrollTo();
        $(".catalog-products > .simple-slider-list > li:first-child").hover();
        $(".catalog-products > .simple-slider-list > li:first-child .button ").should(Condition.visible);
        $(".catalog-products > .simple-slider-list > li:first-child .button ").click();
        $(By.xpath("// div[@class='page-header'][text()='Корзина']")).shouldBe(Condition.appear);
        $(By.xpath("// div[@class='product__header'][contains(text(), 'Givenchy')]")).should(Condition.enabled);
        $(By.xpath("//div[@class='popup__window'] // div[@class='popup-close close-icon']")).click();
        $(By.xpath("// div[@class='product__header'][contains(text(), 'Givenchy')]")).shouldBe(Condition.disappear);
    }

    @Test(priority = 2, groups ={"addingItemToTheCart"})
    public void removingProductFromTheCart(){ //Проверка удаления продукта из корзины
        $(By.xpath("//input[@itemprop='query-input']")).click();
        $(By.xpath("//input[@itemprop='query-input']")).setValue("Givenchy").pressEnter();
        $(".catalog-products").scrollTo();
        $(".catalog-products > .simple-slider-list > li:first-child").hover();
        $(".catalog-products > .simple-slider-list > li:first-child .button ").should(Condition.visible);
        $(".catalog-products > .simple-slider-list > li:first-child .button ").click();
        $(By.xpath("//div[@class='popup__window'] // div[@class='popup-close close-icon']")).click();
        $(By.xpath("// div[@class='product__header'][contains(text(), 'Givenchy')]")).shouldBe(Condition.disappear);
        $(".catalog-products > .simple-slider-list > li:nth-child(2)").hover();
        $(".catalog-products > .simple-slider-list > li:nth-child(2) .button ").should(Condition.visible);
        $(".catalog-products > .simple-slider-list > li:nth-child(2) .button ").click();
        $(By.xpath("// div[@class='page-header'][text()='Корзина']")).shouldBe(Condition.appear);
        $(By.xpath("// div[@class='product__header'][contains(text(), 'Givenchy')]")).should(Condition.enabled);
        $$(".product-list.scrolling.compact:not(.additional)> .product-list__item").shouldHave(CollectionCondition.size(2));
        $(" .product-list__item:nth-child(2) .product__button-remove").click();
        $$(".product-list.scrolling.compact:not(.additional)> .product-list__item").shouldHave(CollectionCondition.size(1));
        $(By.xpath("//div[@class='popup__window'] // div[@class='popup-close close-icon']")).click();
        $(By.xpath("// div[@class='product__header'][contains(text(), 'Givenchy')]")).shouldBe(Condition.disappear);
    }

    @Test (priority = 3)
    public void availabilityNotificationButton() { //Кнопка "Сообщить о наличии" для товара, которого нет в наличии
        $(By.xpath("//input[@itemprop='query-input']")).click();
        $(By.xpath("//input[@itemprop='query-input']")).setValue("Jovial Luxe").pressEnter();
        $(By.xpath("//li[contains(@class,'out-of-stock')][last()]")).scrollTo();
        $(By.xpath("//li[contains(@class,'out-of-stock')][last()]//span[text()='Нет в наличии']")).isEnabled();
        $(By.xpath("//li[contains(@class,'out-of-stock')][last()]")).hover();
        $(".simple-slider-list__item.active").shouldBe(Condition.appear);
        $(By.xpath("//li[contains(@class,'out-of-stock')][last()]//div[text()='Сообщить о появлении']")).shouldBe(Condition.visible);
        $(By.xpath("//li[contains(@class,'out-of-stock')][last()]//div[text()='Сообщить о появлении']")).click();
        $(By.xpath("//form[@data-popup='auth']")).shouldBe(Condition.appear);
    }

    @Test (priority = 4, dataProvider = "DataForCertificate")
    public void creatingCertificatePurchase(String price, String receiverName, String clientName, String clientLastName, String email, String phone, String receiverQuantity){ //Оформление покупки сертификата

        $(By.xpath("//a[text()='Подарки']")).hover();
        $(By.xpath("//a[text()='Сертификаты']")).click();
        $(".catalog-products  li:first-child").click();
        $(By.xpath("//span [contains (@style, 'pxhob9twymua')]")).click();
        $(By.xpath("//textarea [@name='text-congratulation']")).setValue("Happy Birthday, my friend!");
        $(".sum-custom").click();
        $(By.xpath("//input[@name='custom_price']")).setValue(price).click();
        $(By.xpath("//input[@name='receiver_name_print']")).setValue(receiverName);
        $(By.xpath("//input[@name='client_name']")).setValue(clientName);
        $(By.xpath("//input[@name='client_last_name']")).setValue(clientLastName);
        $(By.xpath("//input[@name='client_email']")).setValue(email);
        $(By.xpath("//input[@name='client_phone']")).setValue(phone);
        $(By.xpath("//input[@name='receiver_quantity'][not(@autocomplete='off')]")).setValue(receiverQuantity);
        $(By.xpath("//div[@class='custom-select__value'][text()='Оплата банковской картой']")).should(Condition.enabled);
        $("button.order-cert").scrollTo();
        $("button.order-cert").click();
        $(".privat__pay").shouldBe(Condition.appear);
    }

    @Test (priority = 5) @Ignore
    public void labelDealIsDisplaying(){ //Отображение лейбы Deal для акционного товара
        open ("https://makeup.com.ua/");
        $(By.xpath("//li[@class='header-top-list__item promoted']")).click();
        $(By.xpath("//div[@class='actions-list']/child::div[1]")).click();
        $(".catalog-products").scrollTo();
        $(".catalog-products  li:first-child .action").should(Condition.visible);
    }

    @DataProvider
    public Object [][] DataForCertificate(){
        return new Object[][] {
                {"250", "Kateryna", "Iryna", "Karden", "Test@test.com", "931111111", "1"},
                {"8000", "Anna-Maria", "Ed", "Ivanov", "Test@ng.com", "930001111", "10"}
        };
    }


}