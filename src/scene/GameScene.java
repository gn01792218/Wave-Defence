package scene;

import controllers.AudioResourceController;
import controllers.ImageController;

import controllers.ImageOperate;
import gameobj.Actor;
import gameobj.Bullet;
import gameobj.Enemy1;
import gameobj.Tank1;
import utils.CommandSolver;
import utils.Delay;
import utils.Flag;
import utils.Global;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class GameScene extends Scene {
    //場地左上角X Y(380,180)；場地右下角xy (1060,700) 。
    private BufferedImage image; //背景圖
    private BufferedImage image2; //失敗的圖片
    private ArrayList<Actor> alliance; //角色陣列
    private ArrayList<Actor> enemys; //敵軍
    private static Flag flag = new Flag(1,1,50,50,1,1,50,50);
    private Delay delay; //目前用來控制敵人重新生成的時間


    @Override
    public void sceneBegin() {
        image=ImageController.getInstance().tryGet("/m2.png");
        delay=new Delay(240);//目前用來控制敵人重新生成的時間
        alliance=new ArrayList<>();
        for(int i=0;i<10;i++){
            alliance.add(new Tank1(400+i*50, 650, false));}
        enemys=new ArrayList<>();
        for(int i=0;i<10;i++) {
            enemys.add(new Enemy1(Global.random(400, 1000), Global.random(200, 650), true));
        }

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
                                flag.getPainter().setCenter(e.getX(),e.getY());
                            }
                        case MOVED:
                    }
                }
            }
        };
    }
    @Override
    public void paint(Graphics g) {
        g.drawImage(image,0,-150,null);
        for(int i=0;i<alliance.size();i++){
            alliance.get(i).paint(g);
        }
        for(int i=0;i<enemys.size();i++) {
            enemys.get(i).paint(g);
        }
        flag.paint(g);
        if(alliance.size()<=0){
            image2=ImageController.getInstance().tryGet("/fail2.png");
            g.drawImage(image2,350,250,null);
        }
    }
    @Override
    public void update() {
        //我軍的update
        for(int i=0;i<alliance.size();i++){
            alliance.get(i).autoAttack(enemys); //到時候回合開始後30秒才要自動攻擊
            alliance.get(i).update();
            if(!alliance.get(i).isAlive()){
                alliance.remove(i);
                i--;
                break;
            }
        }
        //敵軍的update
        for(int i=0;i<enemys.size();i++){
            enemys.get(i).autoAttack(alliance);
            enemys.get(i).update();
            if(!enemys.get(i).isAlive()){
                enemys.remove(i);
                i--;
                break;
            }
        }
        //測試用:假如敵軍全消滅，再生成敵軍出來
        if(enemys.size()==0) {
            delay.play();
            if(delay.count()) {
                for (int i = 0; i < 10; i++) {
                    enemys.add(new Enemy1(Global.random(400, 1000), Global.random(200, 650), true));
                }
            }
        }
    }
}
