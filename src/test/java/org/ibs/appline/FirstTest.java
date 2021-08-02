package org.ibs.appline;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.WatchEvent;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FirstTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void before() {
//      Для Windows
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        wait = new WebDriverWait(driver, 15, 1000);

        String baseUrl = "https://www.rgs.ru";
        driver.get(baseUrl);
    }
    @Test
    public void test() {

        closeFrame();

        //принимаем куки
        String cookie = "//div[@tabindex='1']";
        WebElement btnCookie = driver.findElement(By.xpath(cookie));
        waitUtilElementToBeClickable(btnCookie);
        btnCookie.click();

        //нажимаем на Меню
        String menu = "//li[contains(@class, 'dropdown adv-analytics')]/a[contains(text(), 'Меню')]";
        WebElement btnMenu = driver.findElement(By.xpath(menu));
        waitUtilElementToBeClickable(btnMenu);
        btnMenu.click();

        //выбираем подпункт Компаниям
        WebElement company = btnMenu.findElement(By.xpath("./..//a[contains(text(), 'Компаниям')]"));
        waitUtilElementToBeClickable(company);
        company.click();

        //переходим на страхование здоровья
        String health = "//a[contains(text(), 'Страхование здоровья')]";
        WebElement btnHealth = driver.findElement(By.xpath(health));
        waitUtilElementToBeClickable(btnHealth);
        btnHealth.click();

        //переходим на новую вкладку
        List<String> vkladki = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(vkladki.get(1));

        //выбираем Добровольное медицинское страхование
        String healthInsure = "//div[@class='list-group list-group-rgs-menu collapse']//a[contains(text(), 'Добровольное медицинское страхование')]";
        WebElement btnHealthInsure = driver.findElement(By.xpath(healthInsure));
        waitUtilElementToBeClickable(btnHealthInsure);
        btnHealthInsure.click();

        //проверка открытия страницы "Добровольное медицинское страхование"
        waitUtilElementToBeVisible(By.xpath("//h1"));
        Assert.assertEquals("заголовк отсутствует, не соответствует требуемому",
                "Добровольное медицинское страхование", driver.findElement(By.xpath("//h1")).getText());

        //нажимаем на "отправить заявку"
        String bid = "//a[contains(text(), 'Отправить заявку' )]";
        WebElement bthBid = driver.findElement(By.xpath(bid));
        waitUtilElementToBeClickable(bthBid);
        bthBid.click();

//        System.out.println(driver.findElement(By.xpath("//b[contains(text(),'Заявка на добровольное медицинское страхование')]")).getText());

        //проверка открытия окна "Заявка на добровольное медицинское страхование"
        waitUtilElementToBeVisible(By.xpath("//b[contains(text(),'Заявка на добровольное медицинское страхование')]"));
        Assert.assertEquals("заголовк отсутствует, не соответствует требуемому",
                "Заявка на добровольное медицинское страхование",
                driver.findElement(By.xpath("//b[contains(text(),'Заявка на добровольное медицинское страхование')]")).getAttribute("outerText"));

        closeDinamicWindows();

        //--------------------------------------------------------------
        String fieldXPath = "//input[@name='%s']";
        //заполняем поле Фамилия
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "LastName"))), "Ильин");
        //заполняем поле Имя
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "FirstName"))), "Дмитрий");
        //заполняем поле Отчество
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "MiddleName"))), "Евгеньевич");

        //заполняем поле регион
        Select select = new Select(driver.findElement(By.xpath("//select[@name = 'Region']")));
        select.selectByVisibleText("Москва");

        //заполняем поле телефон
        fillInputPhone(By.xpath("//input[contains(@data-bind, 'Phone')]"), "+7 (927) 000-00-00");
        //заполняем поле Эл.почта
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "Email"))), "qwertyqwerty");
        //заполняем поле дата контакта
        fillInputData(By.xpath(String.format(fieldXPath, "ContactDate")), "03.08.2021");
        //заполняем поле Комментарий
        fillInputField(driver.findElement(By.xpath("//textarea[@name = 'Comment']")), "Я согласен на обработку\n");



        //ставим галочку согласия
        WebElement btnCheckMark = driver.findElement(By.xpath("//input[@class = 'checkbox']"));
        btnCheckMark.click();

        //нажимаем отправить
        WebElement btnSend = driver.findElement(By.xpath("//button[@id = 'button-m']"));
        btnSend.click();

        //Проверка ошибки email
        Assert.assertEquals("Проверка ошибки email ",
                 "Введите адрес электронной почты",
                driver.findElement(By.xpath("//span[@class='validation-error-text' and text()='Введите адрес электронной почты']"))
        .getAttribute("innerText"));




    }

    @After
    public void after(){
        driver.quit();
    }

    /**
     * Явное ожидание того что элемент станет кликабельный
     *
     * @param element - веб элемент до которого нужно проскролить
     */
    private void waitUtilElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Явное ожидание того что элемент станет видемым
     *
     * @param locator - локатор до веб элемент который мы ожидаем найти и который виден на странице
     */
    private void waitUtilElementToBeVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Заполнение полей определённым значений
     *
     * @param element - веб элемент (поле какое-то) которое планируем заполнить)
     * @param value - значение которы мы заполняем веб элемент (поле какое-то)
     */

    private void fillInputField(WebElement element, String value){
        element.click();
        element.clear();
        element.sendKeys(value);
        boolean checkFlag = wait.until(ExpectedConditions.attributeContains(element, "value", value));
        Assert.assertTrue("Поле было заполнено некорректно", checkFlag);
    }

    /**
     * Закрываем всплывающие окна (close Pop Up)
     */
    public void closeDinamicWindows(){
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        try {
            System.out.println("Поиск фрейма");
            WebElement element = driver.findElement(By.id("fl-501173"));
            driver.switchTo().frame("fl-501173");
            WebElement frameBtnClose= driver.findElement(By.xpath("//div[@data-fl-track='click-close-login']"));
            waitUtilElementToBeClickable(frameBtnClose);
            frameBtnClose.click();
            driver.switchTo().defaultContent();
            System.out.println("Нашли фрейм");
        }
        catch(NoSuchElementException ignore){
            System.out.println("---");
        } finally {
            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        }
    }

    public void closeFrame(){
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        try{
            System.out.println("Поиск фрейма");
            WebElement element = driver.findElement(By.id("fl-498072"));
            System.out.println("Нашли фрейм");
            driver.switchTo().frame("fl-498072");
            WebElement frameBtnClose = driver.findElement(By.xpath("//div[@class='Ribbon-close']"));
            waitUtilElementToBeClickable(frameBtnClose);
            frameBtnClose.click();
            driver.switchTo().defaultContent();
        } catch (NoSuchElementException ignore){

        }finally {
            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        }


    }

    private void fillInputPhone(By by, String value){
        WebElement element = driver.findElement(by);
        element.click();
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].value = arguments[1]", element, value);
        wait.ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .withMessage("Ошибка ожидания поля телефон");
        wait.until(ExpectedConditions.attributeContains(element, "value", value));
    }

    private void fillInputData(By by, String value){
        WebElement element = driver.findElement(by);
        element.click();
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].value = arguments[1]", element, value);
        wait.ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .withMessage("Ошибка ожидания поля дата");
        wait.until(ExpectedConditions.attributeContains(element, "value", value));
    }


}
