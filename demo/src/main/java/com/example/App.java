package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static JsonNode root;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        json();
        stage.setScene(scene);
        stage.show();
    }

    //画面切り替え用メソッド
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    //接尾詞(.fxml)をくっつけるメソッド
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    //jacksonを使ってjsonファイルの読み込み、相対パスでファイル名指定
    static int json() throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        root = mapper.readTree(new File("demo/src/main/resources/com/example/music.json"));
        return 1;
    }

    public static void main(String[] args) {
        launch();
    }
}

