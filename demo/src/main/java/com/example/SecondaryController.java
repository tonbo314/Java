package com.example;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ResourceBundle;
import java.util.concurrent.PriorityBlockingQueue;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.*;
import javafx.beans.value.*;


public class SecondaryController implements Initializable{
    @FXML
    private ImageView albamView;

    @FXML
    private Button secondaryButton;

    @FXML
    private Slider slider;

    @FXML
    private TextArea text;

    @FXML
    private Slider vslider;

    @FXML
    private TextArea vtext;

    int number = PrimaryController.jsonnumber;
    //jsonnumberで何番めの曲のタイトルかを指定して、.mp4をくっつける
    Media r = new Media(new File("RADWIMPS/" + App.root.get("RADWIMPS").get(number).get("title") + ".m4a").toURI().toString());
    MediaPlayer m = new MediaPlayer(r);
    Image image;

    //親クラスのメソッドを継承している、スペルミスを防ぐためのOverride
    //initializeメソッドはcontrollerを初期化するために呼ばれるメソッドで、sceneをstageに追加する前に呼ばれるもの
    //音量のテキストをbindで連動させる
    //albamViewの画像を設定する
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){ 
        this.vtext.textProperty().bind(this.m.volumeProperty().asString("%.2f"));
    }

    //音楽再生用
    @FXML
    private void musicToplay() throws IOException{
        //スライダーに始めと終わりのタイムを設定
        slider.setMin( m.getStartTime().toSeconds() );
        slider.setMax( m.getStopTime().toSeconds() );
        slider.setSnapToTicks( true );
        //ov(observableValue自身),変更前(old)後(current)が引数として渡される
        ChangeListener<? super Duration> playListener = ( ov , old , current ) ->
        {
            // 現在時間とトータルの時間の情報をラベル出力
            String infoStr = format(m.getCurrentTime()) + "/" + format(m.getTotalDuration());
            text.setText(infoStr);
            // スライダを移動
            //スライダーが触られている状態であればスライダーの位置が変わらないようにする
            if(!seekSliderIsChanging()){
                slider.setValue( m.getCurrentTime().toSeconds() );
            }
        };
        //mの現在時間をプロパティとして、それに変化が起こったらplayListenerが呼び出されるように設定
        m.currentTimeProperty().addListener(playListener);
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

    //曲の再生時間が2000ミリ秒(1秒)より大きいなら初めから再生
    //2秒以下なら前の曲に戻る
    //msが邪魔なのでreplaceで""に置き換え
    @FXML
    private void backMusic() throws IOException{
        if(Double.parseDouble(m.getCurrentTime().toString().replace("m", "").replace("s", "").replace(" ", "")) > 2000.0){
            m.stop();
        }else{
            //1曲目まで戻った時用
            if(number!=0){
                number--;
            }
            m.stop();
            r = new Media(new File("RADWIMPS/" + App.root.get("RADWIMPS").get(number).get("title") + ".m4a").toURI().toString());
            m = new MediaPlayer(r);
        }
    }

    //次の曲に行くためのやつ
    @FXML
    private void nextMusic() throws IOException{
        number++;
        m.stop();
        r = new Media(new File("RADWIMPS/" + App.root.get("RADWIMPS").get(number).get("title") + ".m4a").toURI().toString());
        m = new MediaPlayer(r);
    }

    //スライダーを操作されるとシークする
    @FXML
    private void sliderChenger() throws IOException{
        //seekで再生位置の変更
        m.seek(javafx.util.Duration.seconds(slider.getValue()));
    }

    //スライダーを操作されると音量を変化させる
    @FXML
    private void vsliderChenger() throws IOException{
        this.m.volumeProperty().bind(this.vslider.valueProperty());
    }

    //スライダーをスライドさせている最中orスライダーをクリックしている場合はtrueを返す
    private boolean seekSliderIsChanging() {
        return this.slider.isValueChanging() || this.slider.isPressed();
    }

    //現在、合計時間の表示フォーマット変更
    private String format(Duration duration) {
        long millis = (long) duration.toMillis();
        long minutes = (millis / 60_000) % 60;
        long seconds = (millis / 1_000) % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }
}
