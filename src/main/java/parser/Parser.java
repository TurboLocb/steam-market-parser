package parser;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class Parser {
    //ссылка на sih
    private static ChromeOptions chromeOptions = new ChromeOptions().addExtensions(new File("src/main/resources/sih.crx"));

    //добавляем расширение sih (steam inventory helper)
    private static DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();

    private static WebDriver browserDriver;

    public static void parseByName(String itemName) {
        //создаем расширение
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        //добавляем расширение в драйвер
        browserDriver = new ChromeDriver(desiredCapabilities);

        //переходим по ссылке
        //browserDriver.get("https://steamcommunity.com/market/");

        //ссылка на стим маркет
        browserDriver.get("https://steamcommunity.com/market/search?appid=730");
        try {
            //ищем кнопку cs go
            //WebElement element = browserDriver.findElement(By.xpath("//span[@class='game_button_contents']//span[contains(text(),'Counter-Strike: Global Offensive')]"));

            //input который мы заполняем текстом
            WebElement element = browserDriver.findElement(By.xpath("//input[@id='findItemsSearchBox']"));
            //input который мы сабмитим
            WebElement elementSubmit = browserDriver.findElement(By.xpath("//input[@id='findItemsSearchSubmit']"));
            //передаем текст в поле ввода
            element.sendKeys(itemName);
            //нажимаем поиск
            elementSubmit.submit();

            parseTable();

            //Thread.sleep(2000);

        } catch (Exception exp) {
            exp.printStackTrace();
        }/* finally {
            browserDriver.quit();
        }*/
    }

    private static void parseTable() {
        List<WebElement> elements;
        try {
            //вытигиваем все элементы списка
            elements = browserDriver.findElements(By.xpath("//span[@id='searchResults_links']/*"));

            int pageCounter = 1;

            //если дочерние элементы есть, то продолжаем парсинг
            if (elements.size() != 0) {
                pageCounter = Integer.parseInt(elements.get(elements.size() - 1).getText());
            }

            //проходимся по всем страницам запроса
            for (int i = 0; i < pageCounter; i++) {

                //получаем список позиций
                List<WebElement> list = browserDriver.findElements(By.xpath("//div[@id='searchResultsRows']/a"));
                List<String> positionsOfRequest = new ArrayList<>();

                for (WebElement element : list) {
                    positionsOfRequest.add(element.getAttribute("href"));
                }

                //positionsOfRequest.forEach(position -> System.out.println(position.getAttribute("href")));

                //проходимся по каждой позиции
                positionsOfRequest.forEach(Parser::parseElementTable);

                //получаем кнопку "далее"
                List<WebElement> nextButton = browserDriver.findElements(By.xpath("//span[@id='searchResults_btn_next']"));

                //Если кнопка "далее" существует, то тыкаем по ней
                if (!nextButton.isEmpty()) {
                    //проверяем кликабельна ли кнопка
                    if (nextButton.get(0).isEnabled() && nextButton.get(0).isDisplayed()){
                        nextButton.get(0).click();
                    }
                }
            }

        } catch (NoSuchElementException nse) {
            nse.printStackTrace();
        }
    }

    private static void parseElementTable(String link) {

        //открывает новую вкладку
        /*JavascriptExecutor js = (JavascriptExecutor) browserDriver;
        js.executeScript("window.open()");*/

        /*ArrayList<String> tabs = new ArrayList<>(browserDriver.getWindowHandles());
        browserDriver.switchTo().window(tabs.get(1));
        browserDriver.get(link);*/

        //browserDriver.findElement(By.xpath("//body")).sendKeys(Keys.CONTROL + "t");

        //открывает в этой вкладке ссылку
        browserDriver.get(link);

        //получаем кол-во вкладок таблицы текущего элемента
        int pageCounter = 1;

        //если вкладок больше 1, то узнаем их
        //создаем объект 'pages' для поиска наличия вкладок
        List<WebElement> pages;
        try{
            pages = browserDriver.findElements(By.xpath("//span[@id='searchResults_links']"));

            //вытягиваем количество вкладок по полученному лоту
            if (!pages.isEmpty() && pages.get(0).isDisplayed()) {
                pageCounter = Integer.parseInt(browserDriver
                        .findElement(By.xpath("//span[@id='searchResults_links']/span[last()]"))
                        .getText());
            }
        }catch (NoSuchElementException nse){
            nse.printStackTrace();
        }

        //проходимся циклом по полученным элементам
        for (int i = 0; i < pageCounter; i++) {
            FluentWait<WebDriver> wait = new WebDriverWait(browserDriver, 20)
                    .ignoring(StaleElementReferenceException.class, ElementNotVisibleException.class);

            //кликам "получить все float"
            //WebElement getAllFloatButton = browserDriver.findElement(By.xpath("//a[@id='allfloatbutton']"));

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='allfloatbutton']")));

            browserDriver.findElement(By.xpath("//a[@id='allfloatbutton']")).click();

            List<WebElement> dataList = browserDriver.findElements(By.xpath("//div[@class='float_data']/div[@class='itemseed']/span[@class='value']"));

            System.out.println("Элементов на странице " + dataList.size());

            wait.until(ExpectedConditions.visibilityOfAllElements(dataList));

            dataList.forEach(data -> System.out.println(data.getText()));

            try{
                //получаем кнопку "далее"
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@id='searchResults_btn_next']")));
                List<WebElement> nextButton = browserDriver.findElements(By.xpath("//span[@id='searchResults_btn_next']"));

                //Если кнопка "далее" существует, то тыкаем по ней
                if (!nextButton.isEmpty()) {
                    //проверяем кликабельна ли кнопка
                    if (nextButton.get(0).isEnabled() && nextButton.get(0).isDisplayed()){
                        nextButton.get(0).click();
                    }
                }
            }catch (NoSuchElementException nse){
                nse.printStackTrace();
            }
        }
    }
}
