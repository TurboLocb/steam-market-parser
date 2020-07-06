package parser;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;

public class ParserBySelenide {

    public static void parseByName(String itemName) {
        //Открываем браузер через Selenide
        ChromeOptions options = new ChromeOptions();
        //options.addExtensions(new File("src/main/resources/sih.crx"));

        //linux path
        options.addExtensions(new File("/sih.crx"));

        WebDriver webDriver = new ChromeDriver(options);
        setWebDriver(webDriver);

        open("https://steamcommunity.com/market/search?appid=730");

        try {
            //input который мы заполняем текстом
            SelenideElement element = $(byXpath("//input[@id='findItemsSearchBox']"));
            //input который мы сабмитим
            SelenideElement elementSubmit = $(byXpath("//input[@id='findItemsSearchSubmit']"));
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
        ElementsCollection elements;
        try {
            //вытигиваем все элементы списка
            elements = $$(byXpath("//span[@id='searchResults_links']/*"));

            int pageCounter = 1;

            //если дочерние элементы есть, то продолжаем парсинг
            if (elements.size() != 0) {
                pageCounter = Integer.parseInt(elements.get(elements.size() - 1).getText());
            }

            //проходимся по всем страницам запроса
            for (int i = 0; i < pageCounter; i++) {

                //получаем список позиций
                ElementsCollection list = $$(byXpath("//div[@id='searchResultsRows']/a"));
                //ссылки на позиции по запросу
                List<String> positionsOfRequest = new ArrayList<>();

                for (SelenideElement element : list) {
                    positionsOfRequest.add(element.getAttribute("href"));
                }

                //positionsOfRequest.forEach(position -> System.out.println(position.getAttribute("href")));

                //проходимся по каждой позиции
                positionsOfRequest.forEach(link -> parseElementTable(link));

                //получаем кнопку "далее"
                SelenideElement buttonNext = $(byXpath("//span[@id='searchResults_btn_next']"));

                //Если существует "далее", то кликаем по ней
                if (buttonNext.is(Condition.exist) && buttonNext.is(Condition.visible)) {
                    buttonNext.click();
                }
            }

        } catch (NoSuchElementException nse) {
            nse.printStackTrace();
        }
    }

    private static void parseElementTable(String link) {

        //открывает в этой вкладке ссылку
        open(link);

        //получаем кол-во вкладок таблицы текущего элемента
        int pageCounter = 1;

        //если вкладок больше 1, то узнаем их
        //создаем объект 'pages' для поиска наличия вкладок
        ElementsCollection pages;
        try {
            pages = $$(byXpath("//span[@id='searchResults_links']"));

            //вытягиваем количество вкладок по полученному лоту
            if (!pages.isEmpty() && pages.get(0).isDisplayed()) {
                pageCounter = Integer.parseInt($(byXpath("//span[@id='searchResults_links']/span[last()]"))
                        .getText());
            }
        } catch (NoSuchElementException nse) {
            nse.printStackTrace();
        }

        //проходимся циклом по полученным элементам
        for (int i = 0; i < pageCounter; i++) {

            //Тот же функционал, только на Selenide
            //ждем загрузки "получить все float"
            $(byXpath("//a[@id='allfloatbutton']")).should(Condition.exist);
            $(byXpath("//a[@id='allfloatbutton']")).shouldBe(Condition.visible);
            $(byXpath("//a[@id='allfloatbutton']")).click();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //получаем все данные по seed
            ElementsCollection dataList = $$(byXpath("//div[@class='float_data']/div[@class='itemseed']/span[@class='value']"));

            while (true) {
                int downloadCounter = 0;

                //ждем пока все элементы подгрузятся
                for (SelenideElement s : dataList) {
                    if (s.is(Condition.visible)) {
                        downloadCounter++;
                    }
                }
                if (downloadCounter == dataList.size()) {
                    break;
                } else {
                    $(byXpath("//a[@id='allfloatbutton']")).click();
                    dataList = $$(byXpath("//div[@class='float_data']/div[@class='itemseed']/span[@class='value']"));
                }
            }

            System.out.println("Элементов на странице " + dataList.size());
            dataList.forEach(data -> System.out.println(data.getText()));

            try {
                //получаем кнопку "далее"
                SelenideElement buttonNext = $(byXpath("//span[@id='searchResults_btn_next']"));

                //Если кнопка "далее" существует, то тыкаем по ней
                //Если существует "далее", то кликаем по ней
                if (buttonNext.is(Condition.exist) && buttonNext.is(Condition.visible)) {
                    buttonNext.click();
                }
            } catch (NoSuchElementException nse) {
                nse.printStackTrace();
            }
        }
    }
}
