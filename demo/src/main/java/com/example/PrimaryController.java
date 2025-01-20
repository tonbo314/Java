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

    public static int jsonnumber;

    @FXML
    private ChoiceBox<String> musicchoice;

    @FXML
    private Button primaryButton;

    @FXML
    void switchToSecondary() throws IOException{
        jsonnumber = music();
        //何番めの曲かの番号
        App.setRoot("secondary");
        //どのfxmlをroot(画面に表示するもの)にするかの設定
    }

    //親クラスのメソッドを継承している、スペルミスを防ぐためのOverride
    //initializeメソッドはcontrollerを初期化するために呼ばれるメソッドで、sceneをstageに追加する前に呼ばれるもの
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){ 
        musicchoice.getItems().addAll("01 人生 出会い","02 自暴自棄自己中心的(思春期)自己依存症の少年","03 心臓","04 もしも「みんな一緒に」バージョン","05 さみしい僕","06 コンドーム","07 「ぼく」と「僕」","08 あいまい","09 嫌ん","10 「ずっと大好きだよ」「ほんと?...」","11 愛へ","12 あいラブユー");
    }

    int music() throws IOException{
        for(int i=0;i<13;i++){
            //取得した曲名と同じ曲名かどうかを全探査
            if(musicchoice.getValue().equals(App.root.get("RADWIMPS").get(i).get("title").toString())){
                //一致した数を返す
                return i;
            }
        }
        return 0;
    }
}
