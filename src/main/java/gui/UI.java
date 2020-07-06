package gui;

import javafx.application.Application;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;


public class UI extends Application {

    public static void main(String[] args) {
        launch(args);
        /*IntegerProperty integerProperty = new SimpleIntegerProperty(4);
        IntegerProperty integerProperty2 = new SimpleIntegerProperty(4);

        IntegerBinding integerBinding = new IntegerBinding() {
            {
                super.bind(integerProperty, integerProperty2);
            }
            @Override
            protected int computeValue() {
                return integerProperty.get() + integerProperty2.get();
            }
        };
        System.out.println(integerBinding.get());*/
    }

    @Override
    public void start(Stage primaryStage) {
        //BorderPane представляет собой BorderLayout (аналог в swing)
        //остальные лейауты смотреть тут https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/steam-parser-ui.fxml"));
            Scene scene = new Scene(root,800,600);
            //BorderPane pane = new BorderPane();
            //pane.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
            //pane.setCenter(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    private GridPane addMainPane(){
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(0,10, 0, 10));

        //Название поля для поиска нужного оружия
        //во 2-ой столбец, 1-ую строку
        Text weaponLabel = new Text("Название оружия");
        weaponLabel.setFont(Font.font("Ubuntu", FontWeight.NORMAL, 12));
        gridPane.add(weaponLabel, 1, 0);

        //Название поля для искомого номера паттена
        //во 4-ый столбец, 1-ую строку
        Text patternLabel = new Text("Paint Seed");
        patternLabel.setFont(Font.font("Ubuntu", FontWeight.NORMAL, 12));
        gridPane.add(patternLabel, 3, 0);

        //Поле для поиска оружия 2, 2
        TextField weaponField = new TextField();
        gridPane.add(weaponField, 1, 1, 2, 1);

        //Поле для поиска paint seed 4, 2
        TextField paintSeedField = new TextField();
        gridPane.add(paintSeedField,3, 1);

        //Кнопка для запуска поиска
        Button searchButton = new Button("find");
        gridPane.add(searchButton, 5, 1);

        return gridPane;
    }
}

class User{
    StringProperty stringProperty = new SimpleStringProperty("Dima");

    public String getStringProperty() {
        return stringProperty.get();
    }

    public StringProperty stringPropertyProperty() {
        return stringProperty;
    }

    public void setStringProperty(String stringProperty) {
        this.stringProperty.set(stringProperty);
    }
}
