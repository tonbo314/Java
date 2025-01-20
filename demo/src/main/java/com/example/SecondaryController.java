package com.example;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ResourceBundle;
import java.util.concurrent.PriorityBlockingQueue;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.media.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;


public class SecondaryController implements Initializable{
    int number = PrimaryController.jsonnumber;
    //jsonnumberで何番めの曲のタイトルかを指定して、.mp4をくっつける
    Media r = new Media(new File("RADWIMPS/" + App.root.get("RADWIMPS").get(number).get("title") + ".m4a").toURI().toString());
    MediaPlayer m = new MediaPlayer(r);
    
    @FXML
    private ImageView albamView;

    @FXML
    private Button secondaryButton;

    //音楽再生用
    @FXML
    private void musicToplay() throws IOException{
        m.play();
    }

    //音楽一時停止用
    @FXML
    private void musicTopause() throws IOException{
        m.pause();
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    //曲の再生時間が100ミリ秒より大きいなら初めから再生
    //1秒以下なら前の曲に戻る
    @FXML
    private void backMusic() throws IOException{
        if(Long.parseLong(m.getCurrentTime().toString()) > 100.0){
            m.stop();
            m.play();
        }else{
            number--;
            r = new Media(new File("RADWIMPS/" + App.root.get("RADWIMPS").get(number).get("title") + ".m4a").toURI().toString());
            m = new MediaPlayer(r);
        }
    }

    @FXML
    private void nextMusic() throws IOException{
        number++;
        r = new Media(new File("RADWIMPS/" + App.root.get("RADWIMPS").get(number).get("title") + ".m4a").toURI().toString());
        m = new MediaPlayer(r);
        musicToplay();
    }

    //親クラスのメソッドを継承している、スペルミスを防ぐためのOverride
    //initializeメソッドはcontrollerを初期化するために呼ばれるメソッドで、sceneをstageに追加する前に呼ばれるもの
    //albamViewの画像を設定する
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){ 
        
    }
}
