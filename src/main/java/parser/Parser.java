package parser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;

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
            Thread.sleep(1000);
        } catch (Exception exp) {
            exp.printStackTrace();
        }/* finally {
            browserDriver.quit();
        }*/
    }
}
