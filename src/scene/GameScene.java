package scene;

import controllers.ImageController;

import controllers.ImageOperate;
import gameobj.Actor;
import gameobj.Bullet;
import gameobj.Enemy1;
import gameobj.Tank1;
import utils.CommandSolver;
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
    private ArrayList<Actor> alliance; //角色陣列
    private ArrayList<Actor> enemys; //敵軍
    private static Flag flag = new Flag(1,1,50,50,1,1,50,50);

    @Override
    public void sceneBegin() {
        image=ImageController.getInstance().tryGet("/m2.png");
        alliance=new ArrayList<>();
        alliance.add(new Tank1(700,700,false));
        enemys=new ArrayList<>();
        enemys.add(new Enemy1(1000,150,true));
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
        alliance.get(0).paint(g);
        enemys.get(0).paint(g);
        flag.paint(g);
    }
    @Override
    public void update() {
        enemys.get(0).moveToTarget((int)flag.getPainter().centerX(),(int)flag.getPainter().centerY());
        for(int i=0;i<alliance.size();i++){
            alliance.get(i).update();
            alliance.get(i).attack(enemys.get(0).painter().centerX(),enemys.get(0).painter().centerY());
        }
        for(int i=0;i<enemys.size();i++){
            enemys.get(i).update();
            enemys.get(i).attack(alliance.get(0).painter().centerX(),alliance.get(0).painter().centerY());
        }
    }
}
