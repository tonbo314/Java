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

    //選択されたアルバム名
    //楽する用に定義しておく
    String al = PrimaryController.albam;
    
    @FXML
    private ImageView view1;

    @FXML
    private ImageView view2;

    @FXML
    private ImageView view3;

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

    //ジャケ写の種類別配列
    String image[] = {"view1","view2","view3"};
    int number = PrimaryController.jsonnumber;
    //jsonnumberで何番めの曲のタイトルかを指定して、.mp4をくっつける
    Media r = new Media(new File(al + "/" + App.root.get(al).get(number).get("title") + ".m4a").toURI().toString());
    MediaPlayer m = new MediaPlayer(r);
    //親クラスのメソッドを継承している、スペルミスを防ぐためのOverride
    //initializeメソッドはcontrollerを初期化するために呼ばれるメソッドで、sceneをstageに追加する前に呼ばれるもの
    //音量のテキストをbindで連動させる
    //音量スライダーの初期値を右端に
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){ 
        this.vtext.textProperty().bind(this.m.volumeProperty().asString("%.2f"));
        this.vslider.setValue(1.0);
        view1.setVisible(false);
        view2.setVisible(false);
        setView();
    }

    //音楽再生用
    @FXML
    private void musicToplay() throws IOException{
        setView();
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
        //なぜかtry,catchじゃないとエラー出る
        //連続再生の処理部分
        this.m.setOnEndOfMedia(() -> {
            try {
                number++;
                m.stop();
                r = new Media(new File(al + "/" + App.root.get(al).get(number).get("title") + ".m4a").toURI().toString());
                m = new MediaPlayer(r);
                m.stop();
                musicToplay();
            } catch (Exception e) { 
                System.out.println(e);
            }
        });
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
        //どのfxmlをroot(画面に表示するもの)にするかの設定
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
            r = new Media(new File(al + "/" + App.root.get(al).get(number).get("title") + ".m4a").toURI().toString());
            m = new MediaPlayer(r);
        }
    }

    //次の曲に行くためのやつ
    @FXML
    private void nextMusic() throws IOException{
        number++;
        m.stop();
        r = new Media(new File(al + "/" + App.root.get(al).get(number).get("title") + ".m4a").toURI().toString());
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

    //ジャケ写用
    private int setView(){
        view1.setVisible(false);
        view2.setVisible(false);
        for(int i=0;i<image.length;i++){
            //取得したジャケ写と同じものかどうかを全探査(""があるといけないので消す)
            if(App.root.get(al).get(number).get("image").toString().replace("\"","").equals(image[i])){
                //一致したものを表示
                switch(i){
                    case 0:
                        view1.setVisible(true);
                        break;
                    case 1:
                        view2.setVisible(true);
                        break;
                    default:
                        break;
                }
            }
        }
        return 1;
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
