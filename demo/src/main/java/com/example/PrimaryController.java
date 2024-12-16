package com.example;

import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.io.IOException;
import java.net.*;

//implementsは実行という意味、初期化メソッドを実行しますよということ
public class PrimaryController implements Initializable{

    @FXML
    private ChoiceBox<String> musicchoice;

    @FXML
    private Button primaryButton;

    @FXML
    void switchToSecondary() throws IOException{
        App.setRoot("secondary");
        //どのfxmlをroot(画面に表示するもの)にするかの設定
    }

    //親クラスのメソッドを継承している、スペルミスを防ぐためのOverride
    //initializeメソッドはcontrollerを初期化するために呼ばれるメソッドで、sceneをstageに追加する前に呼ばれるもの
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){ 
        musicchoice.getItems().addAll("もしも","人生、出会い");
    }

}