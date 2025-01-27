package com.example;

import java.util.Random;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.*;

//implementsは実行という意味、初期化メソッドを実行しますよということ
public class PrimaryController implements Initializable{

    //曲の番号名
    public static int jsonnumber;
    //アルバム名
    public static String albam;
    //musicchoice用
    public static int albamnumber;
    //アルバム名配列
    public static String[] albams = {"RADWIMPS","RADWIMPS2","narimi"};
    //ランダム用
    public static Random rand = new Random();
    //ランダム判定用
    public static boolean hantei;

    @FXML
    private Label choose;

    @FXML
    private Label or;

    @FXML
    private Label title;

    @FXML
    private Button albambutton;

    @FXML
    private Button randombutton;

    @FXML
    private ChoiceBox<String> albamchoice;

    @FXML
    private ChoiceBox<String> musicchoiceRAD;  

    @FXML
    private ChoiceBox<String> musicchoiceRAD2;

    @FXML
    private ChoiceBox<String> musicchoicenarimi;

    @FXML
    private Button primaryButton;

    //親クラスのメソッドを継承している、スペルミスを防ぐためのOverride
    //initializeメソッドはcontrollerを初期化するために呼ばれるメソッドで、sceneをstageに追加する前に呼ばれるもの
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){ 
        //アルバム用
        albamchoice.getItems().addAll("RADWIMPS","RADWIMPS2","narimi");
        //楽曲選択用
        musicchoiceRAD.getItems().addAll("01 人生 出会い","02 自暴自棄自己中心的(思春期)自己依存症の少年","03 心臓","04 もしも「みんな一緒に」バージョン","05 さみしい僕","06 コンドーム","07 青い春","08 「ぼく」と「僕」","09 あいまい","10 嫌ん","11 「ずっと大好きだよ」「ほんと?…」","12 愛へ","13 あいラブユー");
        musicchoiceRAD2.getItems().addAll("01 愛し short","02 なんちって","03 そりゃ君が好きだから","04 夢見月に何思う","05 ノットビコーズ","06 愛し","07 ういんぷす学園休み時間","08 ヒキコモリロリン","09 着席","10 俺色スカイ","11 音の葉","12 シリメツレツ","13 祈跡","14 ララバイ");
        musicchoicenarimi.getItems().addAll("01 アフターアワー","02 元彼氏として","03 ドラマみたいだ","04 白熱灯、焼ける朝","05 彼氏として","06 夜行バス","07 マイハッピーウェディング","08 友達になりたい","09 教室とさよなら","10 ふたり","11 18歳よ","12 優しさの行方");
        //始めは見えないようにしておく
        musicchoiceRAD.setVisible(false);
        musicchoiceRAD2.setVisible(false);
        musicchoicenarimi.setVisible(false);
        primaryButton.setVisible(false);
        choose.setVisible(false);
    }

    @FXML
    void switchToSecondary() throws IOException{
        //何番めの曲かの番号
        if(!hantei){
            //Secondaryに一回行ってから戻ってきた時のためにここからal,numberを設定できるようにする
            SecondaryController.al = albam;
            jsonnumber = music();
            SecondaryController.number = jsonnumber;
        }
        //どのfxmlをroot(画面に表示するもの)にするかの設定
        App.setRoot("secondary");
    }

    //アルバム選択終わった後のメソッド
    @FXML
    void albambutton() throws IOException{ 
        //指示ラベルと画面移動ボタンを見えるように
        primaryButton.setVisible(true);
        choose.setVisible(true);
        //アルバム系を見えなくする
        albamchoice.setVisible(false);
        albambutton.setVisible(false);
        randombutton.setVisible(false);
        or.setVisible(false);
        //アルバム名と一致するやつを探して、その曲リストを表示する
        for(int i=0;i<albams.length;i++){
            if(albamchoice.getValue().equals(albams[i])){
                albamnumber = i;
                albam = albams[i];
                switch(i){
                    //RADWIMPS
                    case 0:
                        musicchoiceRAD.setVisible(false);
                        musicchoiceRAD2.setVisible(false);
                        musicchoicenarimi.setVisible(false);
                        musicchoiceRAD.setVisible(true);
                        break;
                    //REDWIMPS2
                    case 1:
                        musicchoiceRAD.setVisible(false);
                        musicchoiceRAD2.setVisible(false);
                        musicchoicenarimi.setVisible(false);
                        musicchoiceRAD2.setVisible(true);
                        break;
                    //narimi
                    case 2:
                        musicchoiceRAD.setVisible(false);
                        musicchoiceRAD2.setVisible(false);
                        musicchoicenarimi.setVisible(false);
                        musicchoicenarimi.setVisible(true);
                        break;
                }
            }
        }
    }

    //ランダム用
    //nextInt()で指定した値未満の整数をもらう(アルバムの数未満)
    @FXML
    public void randombutton() throws IOException{
        hantei = true;
        //画面移動ボタン以外を見えなくする
        musicchoiceRAD.setVisible(false);
        musicchoiceRAD2.setVisible(false);
        musicchoicenarimi.setVisible(false);
        albamchoice.setVisible(false);
        albambutton.setVisible(false);
        randombutton.setVisible(false);
        or.setVisible(false);
        choose.setVisible(false);
        primaryButton.setVisible(true);
        random();
    }

    //一致する曲のjsonでの番号を得る
    //iの範囲はそのアルバムの曲名
    int music() throws IOException{
        switch(albamnumber){
            case 0://RADWIMPS
                for(int i=0;i<13;i++){
                    //取得した曲名と同じ曲名かどうかを全探査(""があるといけないので消す)
                    if(musicchoiceRAD.getValue().equals(App.root.get(albam).get(i).get("title").toString().replace("\"",""))){
                        //一致した数を返す
                        return i;
                    }
                }
                break;
            case 1://RADWIMPS2
                for(int i=0;i<14;i++){
                    //取得した曲名と同じ曲名かどうかを全探査(""があるといけないので消す)
                    if(musicchoiceRAD2.getValue().equals(App.root.get(albam).get(i).get("title").toString().replace("\"",""))){
                        //一致した数を返す
                        return i;
                    }
                }
                break;
            case 2://narimi
                for(int i=0;i<12;i++){
                    //取得した曲名と同じ曲名かどうかを全探査(""があるといけないので消す)
                    if(musicchoicenarimi.getValue().equals(App.root.get(albam).get(i).get("title").toString().replace("\"",""))){
                        //一致した数を返す
                        return i;
                    }
                }
                break;
            default:
                break;
        }
        return 0;
    }

    //ランダム再生のやつ
    public static void random(){
        //乱数でアルバムを選択してalに入れておく
        albamnumber = rand.nextInt(albams.length);
        albam = albams[albamnumber];
        SecondaryController.al = albam;
        //曲数をnextInt()の中に入れて、それ未満の値でランダム
        //その値をnumberに入れる
        switch(albamnumber){
            //RADWIMPS
            case 0:
                jsonnumber = rand.nextInt(13);
                SecondaryController.number = jsonnumber;
                break;
            //REDWIMPS2
            case 1:
                jsonnumber = rand.nextInt(14);
                SecondaryController.number = jsonnumber;
                break;
            //narimi
            case 2:
                jsonnumber = rand.nextInt(12);
                SecondaryController.number = jsonnumber;
                break;
            default:
                break;
        }
    }
}
