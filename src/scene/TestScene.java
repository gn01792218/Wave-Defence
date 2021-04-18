package scene;

import controllers.ImageController;
import gameobj.*;
import utils.CommandSolver;
import utils.Flag;
import utils.Global;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EmptyStackException;

public class TestScene extends Scene{
    private BufferedImage image; //背景圖
    private ArrayList<Actor> alliance; //角色陣列
    private ArrayList<Actor> enemys; //敵軍
    private ArrayList<Bullet> bullets;
    private Actor allianceControl;//受旗子控制的我軍
    private boolean isFlagUsable;
    private int egetx;
    private int egety;
    @Override
    public void sceneBegin() {
        image = ImageController.getInstance().tryGet("/m2.png"); //場景圖
        alliance = new ArrayList();
        enemys = new ArrayList();
        bullets = new ArrayList();
        isFlagUsable = true;
    }
    @Override
    public void sceneEnd() {
    }
    @Override
    public CommandSolver.MouseListener mouseListener() {
        return new CommandSolver.MouseListener() {

            @Override
            public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
                if(state!=null){
                    switch (state){
                        case CLICKED:
                            if(e.getButton() == e.BUTTON1){
                                if (isFlagUsable) {  //當旗子還可以使用的時候
                                    for (int i = 0; i < alliance.size(); i++) { //控制權現在在誰身上
                                        if (alliance.get(i).isTouch(e.getX(), e.getY())) { //假如被點到了
                                            allianceControl = alliance.get(i);  //被點到的人會變被控制者
                                            allianceControl.setControl(true); //設成被控制中
                                            System.out.println("第"+i+"台被點到"+"種類是"+allianceControl.getType());  //很不好點
                                        }else{
                                            alliance.get(i).setControl(false);
                                        }
                                    }
                                }
                            }else if(e.getButton() == e.BUTTON2){

                            }else if(e.getButton() == e.BUTTON3){
                                if(isFlagUsable && allianceControl!=null){
                                    allianceControl.getFlag().setCenter(e.getX(),e.getY());
                                }
                            }
                            break;
                        case MOVED:
                            egetx = e.getX();
                            egety = e.getY();


                    }
                }
            }
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                switch (commandCode) {
                    case 5:
                        alliance.add(new Tank1(egetx,egety, false));
                        break;
                    case 6:
                        enemys.add(new Enemy1(Global.random(Global.BOUNDARY_X1,Global.BOUNDARY_X2),200,true));
                        break;
                }
            }


            @Override
            public void keyReleased(int commandCode, long trigTime) {

            }

            @Override
            public void keyTyped(char c, long trigTime) {

            }
        };
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, -150, null);
        for (int i = 0; i < alliance.size(); i++) {
            alliance.get(i).paint(g);
        }
        for (int i = 0; i < enemys.size(); i++) {
            enemys.get(i).paint(g);
        }
        for(int i=0;i<bullets.size();i++){
            bullets.get(i).paint(g);
        }
        if(allianceControl!=null){
            allianceControl.getFlag().paint(g); //旗子可以使用的時候才畫出來
        }

    }

    @Override
    public void update() {
        //我軍的update
        for (int i = 0; i < alliance.size(); i++) {
            alliance.get(i).autoAttack(enemys,alliance);
            alliance.get(i).bulletsUpdate(enemys);
            if (!alliance.get(i).isAlive()) {
                alliance.remove(i);
                i--;
                break;
            }
        }
        //敵軍update
        for (int i = 0; i < enemys.size(); i++) {
            enemys.get(i).autoAttack(alliance,enemys);
            enemys.get(i).bulletsUpdate(alliance);
            if (!enemys.get(i).isAlive()) {
                enemys.remove(i);
                i--;
                break;
            }
        }
        for (int i=0;i<bullets.size();i++){
            bullets.get(i).update();
        }
    }
}
