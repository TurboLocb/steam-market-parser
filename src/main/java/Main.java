import parser.Parser;

public class Main {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        Parser.parseByName("M4A4");
    }
}
