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


public class SecondaryController {

    Media r1 = new Media(new File("RADWIMPS/01 人生 出会い.m4a").toURI().toString());
    MediaPlayer zinnseideai = new MediaPlayer(r1);

    @FXML
    private Button secondaryButton;

    //音楽再生用
    @FXML
    private void musicToplay() throws IOException{
        zinnseideai.play();
    }

    //音楽一時停止用
    @FXML
    private void musicTopause() throws IOException{
        zinnseideai.pause();
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}