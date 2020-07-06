import com.codeborne.selenide.Configuration;
import parser.ParserBySelenide;

public class Main {
    public static void main(String[] args) {
        //System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");

        //Linux path
        System.setProperty("webdriver.chrome.driver", "/chromedriver-linux");

        //Selenium Web Driver
        //Parser.parseByName("АК-47 | Ягуар");

        //Время на подгрузку элементов Selenide
        Configuration.timeout = 30_000;

        //Selenide
        ParserBySelenide.parseByName("АК-47 | Ягуар");
    }
}
