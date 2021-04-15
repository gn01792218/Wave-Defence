package scene;

import controllers.AudioResourceController;

import controllers.ImageController;

import controllers.SceneController;
import gameobj.Actor;

import gameobj.Enemy1;
import gameobj.Tank1;
import gameobj.Tank2;

import utils.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
//前10秒可以操控物件往目標方向移動
//當鼠標位置進入物件的範圍，目標就會變成 isClick(只有我軍有效)
//更新時，只有isInClik可以移動到旗子座標點
//問題:倒數10秒的動畫要重弄
//每回合3波，完後 delay5秒換場
//imageController有問題
//判斷打輸的條件是全滅，但假如沒$$買軍隊時，直接進場，就會直接走失敗畫面然後+$$-->變成洗錢的Bug
public class GameScene extends Scene {
    //場地左上角X Y(380,180)；場地右下角xy (1060,700) 。
    private BufferedImage image; //背景圖
    private BufferedImage image2; //失敗的圖片
    private BufferedImage image3;//倒數10秒圖片
    private ArrayList<Actor> alliance; //角色陣列
    private ArrayList<Actor> enemys; //敵軍
    private static Flag flag; //指揮旗
    private Delay delayEnemyBron; //目前用來控制敵人重新生成的間隔時間
    private Delay delayRound;//回合前20秒的delay
    private Delay delayCount;//10秒後倒數10秒的週期播放
    private int countNum; //倒數的播放號碼
    private int changePic = 50; //倒數動畫
    private boolean enemysMove; //敵軍是否可以移動
    private Actor allianceControl;//受旗子控制的我軍
    private int count = 0;//共三波(SceneBegin+2)

    @Override
    public void sceneBegin() {
        image = ImageController.getInstance().tryGet("/m2.png"); //場景圖
        image3 = ImageController.getInstance().tryGet("/count.png"); //倒數的圖片
        delayEnemyBron = new Delay(240);//目前用來控制敵人重新生成的時間
        delayRound = new Delay((600)); //開場前delay前20秒
        delayRound.play();//開場就倒數
        delayCount = new Delay(60);
        delayCount.loop();//倒數計時每1秒觸發一次換圖片
        AudioResourceController.getInstance().play("/boomy-sizzling.wav");
        alliance = new ArrayList<>();
        System.out.println("我軍初始數量" + alliance.size()); //測試用

        for (int i = 0; i < Global.getActorButtons().size(); i++) { //從Global中的角色按鈕取得選單下的訂單
            System.out.println("現在是" + Global.getActorButtons().get(i).getActorType());
            for (int j = 0; j < Global.getActorButtons().get(i).getNumber(); j++) { //跑某個角色的數量次
                System.out.println("做出第" + j + "隻");
                switch (Global.getActorButtons().get(i).getActorType()) { //依據該型號做出該數量的戰隊
                    case TANK1: //畫j才不會疊在一起!!!
                        alliance.add(new Tank1(Global.BOUNDARY_X1 + j * 100, Global.BOUNDARY_Y2, false));
                        System.out.println("產生" + "Tank1");
                        break;
                    case TANK2:
                        alliance.add(new Tank2(Global.BOUNDARY_X1 + j * 100, Global.BOUNDARY_Y2, false));
                        System.out.println("產生" + "Tank2");
                        break;
                }
            }
        }
        System.out.println("我軍數量" + alliance.size());
        enemys = new ArrayList<>();
        for (int i = 0; i < Global.random(5, 10); i++) {  //第一波敵人5-10隻
            enemys.add(new Enemy1(Global.random(400, 1000), Global.random(200, 350), true));
        }
        enemysMove = false; //剛開始敵軍不能移動
        flag = new Flag(1, 1, 50, 50);
    }

    @Override
    public void sceneEnd() {
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return new CommandSolver.MouseListener() {
            @Override
            public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
                if (state != null) {
                    switch (state) {
                        case CLICKED:
                            if (e.getButton() == e.BUTTON1) {
                                if (flag.isFlagUsable()) {  //當旗子還可以使用的時候
                                    for (int i = 0; i < alliance.size(); i++) { //控制權現在在誰身上
                                        if (alliance.get(i).isTouch(e.getX(), e.getY())) { //假如被點到了
                                            allianceControl = alliance.get(i);  //被點到的人會變被控制者
                                            System.out.println("點到了!!!!!!!!!!!!!!!!");  //不好點到
                                        }
                                    }
                                }
                                System.out.println("左鍵");
                            } else if (e.getButton() == e.BUTTON2) {
                                System.out.println("中鍵");
                            } else if (e.getButton() == 3) {//也可以這樣
                                //旗子在可以使用的狀態才接收座標
                                System.out.println("右鍵");
                                if (flag.isFlagUsable()) {
                                    flag.setCenter(e.getX(), e.getY());
                                }
                            }
                        case MOVED:
                    }
                }
            }
        };
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, -150, null);
        if (delayCount.count()) {  //每1秒播放圖片
            countNum++;
            changePic = 50;
        }
        changePic--;
        //最後10秒畫
        if (delayCount.count()) {  //每1秒播放圖片
            countNum++;
        }
        int tx = this.countNum * 74;  //0-74 1-74*2 2-74*3
        g.drawImage(image3, 750 - changePic, 100 - changePic, 750 + 74 + changePic, 100 + 90 + changePic,
                tx, 0, tx + 74, 90, null); //倒數的圖片
        for (int i = 0; i < alliance.size(); i++) {
            alliance.get(i).paint(g);
        }
        for (int i = 0; i < enemys.size(); i++) {
            if (enemysMove) { //敵軍可以移動時才畫
                enemys.get(i).paint(g);
            }
        }
        if (flag.isFlagUsable()) {
            flag.paint(g); //旗子可以使用的時候才畫出來
        }
        if (alliance.size() <= 0) { //死光時畫失敗畫面
            image2 = ImageController.getInstance().tryGet("/fail2.png");
            g.drawImage(image2, 350, 250, null);
        }
    }

    //當偵測到被點到，開啟可以移動，時才移動，並一直移動到目標點，然後
    @Override
    public void update() {
        //我軍的update
        if(flag.isFlagUsable() && allianceControl!=null){
            allianceControl.move(flag.getPainter().centerX(), flag.getPainter().centerY(),alliance);
        }
        for (int i = 0; i < alliance.size(); i++) {
            if (!flag.isFlagUsable()) {
                alliance.get(i).autoAttack(enemys, alliance);
                alliance.get(i).update();
                if (!alliance.get(i).isAlive()) {
                    alliance.remove(i);
                    break;
                } else {
                    alliance.get(i).autoAttack(enemys, alliance);
                    alliance.get(i).bulletsUpdate(enemys); //發射子彈
                    if (!alliance.get(i).isAlive()) { //沒有活著的時候移除
                        for (int j = 0; j < Global.getActorButtons().size(); j++) { //和Glabl的角色類型作比對
                            if (Global.getActorButtons().get(j).getActorType() == alliance.get(i).getType()) {
                                Global.getActorButtons().get(j).offSetNum(-1); //該類型的角色數量-1
                            }
                        }
                        alliance.remove(i);
                        break;
                    }
                }
            }
        }
            if (delayRound.count()) { //開場20秒後
                flag.setFlagUsable(false); //旗子不能用
                enemysMove = true; //10秒後敵軍可以移動
            }
            if (enemysMove) { //敵軍可以移動時
                //敵軍update
                for (int i = 0; i < enemys.size(); i++) {
                    enemys.get(i).autoAttack(alliance, enemys);
                    enemys.get(i).bulletsUpdate(alliance);
                    if (!enemys.get(i).isAlive()) {
                        enemys.remove(i);
                        Player.getInstance().offsetMoney(+200); //殺一隻敵軍200元
                        break;
                    }
                }
                //測試用:假如敵軍全消滅，再生成敵軍出來
                if (count <= 2) {  //
                    if (enemys.size() == 0) { //當敵軍
                        delayEnemyBron.play();
                        if (delayEnemyBron.count()) {
                            for (int i = 0; i < 10; i++) {
                                enemys.add(new Enemy1(Global.random(400, 1000), Global.random(200, 350), true));
                            }
                            count++; //底類完才觸發++
                        }
                    }
                }
                if (count >= 2 && enemys.size() <= 0) { //挑戰成功條件
                    SceneController.getInstance().changeScene(new UserScene());
                    Player.getInstance().offsetHonor(+300); //榮譽值+300
                    Player.getInstance().offsetMoney(1000); //金錢+1000
                } else if (alliance.size() <= 0) {
                    delayEnemyBron.play();
                    if (delayEnemyBron.count()) { //等4秒後換場
                        SceneController.getInstance().changeScene(new UserScene());
                        Player.getInstance().offsetMoney(250); //錢值+250
                        Player.getInstance().offsetHonor(50); //榮譽值+50
                    }
                }
            }
        }
    }

