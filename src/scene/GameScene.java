package scene;

import controllers.ImageController;

import gameobj.Actor;

import gameobj.Enemy1;
import gameobj.Tank1;
import gameobj.Tank2;

import utils.CommandSolver;
import utils.Delay;
import utils.Flag;
import utils.Global;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import java.util.ArrayList;

public class GameScene extends Scene {
    //場地左上角X Y(380,180)；場地右下角xy (1060,700) 。
    private BufferedImage image; //背景圖
    private BufferedImage image2; //失敗的圖片
    private BufferedImage image3;//倒數10秒圖片
    private ArrayList<Actor> alliance; //角色陣列
    private ArrayList<Actor> enemys; //敵軍
    private static Flag flag; //指揮旗
    private Delay delayEnemyBron; //目前用來控制敵人重新生成的間隔時間
    private Delay delayRound;//回合前X秒的delay
    private Delay delayCount;//倒數10秒的週期播放
    private int countNum; //倒數的播放號碼
    private boolean enemysMove; //敵軍是否可以移動

    @Override
    public void sceneBegin() {

        image = ImageController.getInstance().tryGet("/m2.png"); //場景圖
        image3 = ImageController.getInstance().tryGet("/count.png"); //倒數的圖片
        delayEnemyBron = new Delay(240);//目前用來控制敵人重新生成的時間
        delayRound = new Delay((600)); //開場前delay前10秒
        delayRound.play();//開場就倒數
        delayCount = new Delay(60);
        delayCount.loop();//倒數計時每1秒觸發一次換圖片
        alliance = new ArrayList<>();
        System.out.println("我軍初始數量"+alliance.size());
        //問題; 做4-6隻 5-8隻 6-8?!  做10-15隻；只做某一種類就對，坦克1的數量不對?!  坦克1有幾隻 坦克2就會多幾隻
        //畫出來位置都一樣
        for (int i = 0; i <Global.getActorButtons().size(); i++) { //從Global中的角色按鈕取得選單下的訂單
            System.out.println("現在是"+Global.getActorButtons().get(i).getActorType());
            for(int j=0;j<Global.getActorButtons().get(i).getNumber();j++) { //跑某個角色的數量次
                System.out.println("做出第"+j+"隻");
                switch (Global.getActorButtons().get(i).getActorType()) { //依據該型號做出該數量的戰隊
                    case TANK1: //畫j才不會疊在一起!!!
                        alliance.add(new Tank1(Global.BOUNDARY_X1 + j * 60, Global.BOUNDARY_Y2, false));
                        System.out.println("產生"+"Tank1");
                        break;
                    case TANK2:
                        alliance.add(new Tank2(Global.BOUNDARY_X1+ j * 60, Global.BOUNDARY_Y2, false));
                        System.out.println("產生"+"Tank2");
                        break;
                }
            }
//            if (i < 5) { //測試用
//                alliance.add(new Tank1(500 + i * 50, 450 + i * 30, false));
//            } else if (i >= 5) {
//                alliance.add(new Tank2(650 + i * 50, 850 - i * 50, false));
//            }
        }
        System.out.println("我軍數量"+alliance.size());
        enemys = new ArrayList<>();
        for (int i = 0; i < 10; i++) {  //第一波敵人
            enemys.add(new Enemy1(Global.random(400, 1000), Global.random(200, 350), true));
        }
        enemysMove=false; //剛開始敵軍不能移動
        flag = new Flag(1, 1, 50, 50, 1, 1, 50, 50);
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
                                System.out.println("左鍵");
                            } else if (e.getButton() == e.BUTTON2) {
                                System.out.println("中鍵");
                            } else if (e.getButton() == 3) {//也可以這樣
                                //旗子在可以使用的狀態才接收座標
                                if (flag.isFlagUsable()) {
                                    flag.getPainter().setCenter(e.getX(), e.getY());
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
        }
        int tx =this.countNum*74;  //0-74 1-74*2 2-74*3
        g.drawImage(image3, 750, 100, 750 + 74, 100 + 90,
                tx,0,tx+74,90, null); //倒數的圖片
        for (int i = 0; i < alliance.size(); i++) {
            System.out.println("畫出我軍"+i+"隻");
            alliance.get(i).paint(g);
        }
        for (int i = 0; i < enemys.size(); i++) {
            if(enemysMove) { //敵軍可以移動時才畫
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

    @Override
    public void update() {
        //我軍的update
        for (int i = 0; i < alliance.size(); i++) {
            if (flag.isFlagUsable()) { //旗子可用時，到旗子的指定位置
                alliance.get(i).moveToTarget(flag.getPainter().centerX(), flag.getPainter().centerY());
                continue; //然後繼續
            }
            alliance.get(i).autoAttack(enemys);
            alliance.get(i).update();
            if (!alliance.get(i).isAlive()) {
                alliance.remove(i);
                i--;
                break;
            }
        }
        if (delayRound.count()) { //開場10秒後
            flag.setFlagUsable(false); //旗子不能用
            enemysMove=true; //10秒後敵軍可以移動
        }
        if(enemysMove) { //敵軍可以移動時
            //敵軍update
            for (int i = 0; i < enemys.size(); i++) {
                enemys.get(i).autoAttack(alliance);
                enemys.get(i).update();
                if (!enemys.get(i).isAlive()) {
                    enemys.remove(i);
                    i--;
                    break;
                }
            }
            //測試用:假如敵軍全消滅，再生成敵軍出來
            if (enemys.size() == 0) {
                delayEnemyBron.play();
                if (delayEnemyBron.count()) {
                    for (int i = 0; i < 10; i++) {
                        enemys.add(new Enemy1(Global.random(400, 1000), Global.random(200, 350), true));
                    }
                }
            }
        }

    }
}
