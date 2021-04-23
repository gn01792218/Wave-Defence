package scene;

import controllers.AudioResourceController;
import controllers.ImageController;
import menu.*;
import menu.Button;
import menu.impl.MouseTriggerImpl;
import utils.CommandSolver;
import utils.Global;
import utils.Player;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class PopWindowScene extends PopupWindow {
    private Button arrowR;
    private Button arrowL;
    private boolean arrowRUseable;
    private boolean arrowLUseable;
    private ArrayList<ActorButton> enemyButtons;

    public PopWindowScene(int x, int y, int width, int height){
        super(x,y,width,height);
    }

    @Override
    public void sceneBegin() {

        enemyButtons= Global.getEnemyButtons();//得到Global的角色按紐
        arrowR=new Button(1000,380,new Style.StyleRect(150,113,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/arrowR.png"))));
        arrowL=new Button(300,380,new Style.StyleRect(150,113,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/arrowL.png"))));

    }

    @Override
    public void sceneEnd() {

    }

    public boolean isArrowRUseable() {
        return arrowRUseable;
    }

    public boolean isArrowLUseable() {
        return arrowLUseable;
    }

    public Button getArrowR() {
        return arrowR;
    }

    public Button getArrowL() {
        return arrowL;
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

    @Override
    public void paintWindow(Graphics g) {
        g.setColor(new Color(0x7A2B2B34, true));
        g.fillRect(super.getX(), super.getY(), super.getWidth(), super.getHeight());
        for(int i=0;i<enemyButtons.size();i++){
            if(enemyButtons.get(i).left()<900 && enemyButtons.get(i).right()>500) {
                enemyButtons.get(i).paint(g);
            }
        }
    }
    @Override
    public void update() {
        if(enemyButtons.get(0).left()<=0 && enemyButtons.get(0).left()>=-1000){ //當T1在第一張時
            this.arrowLUseable=true;
        }else{this.arrowLUseable=false;}
        if(enemyButtons.get(4).right()>=1500 && enemyButtons.get(4).right()<=2500) {
            this.arrowRUseable = true;
        }else{this.arrowRUseable=false;}

    }
    @Override
    protected void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
        System.out.println("進入敵軍資訊監聽");
        if(state!=null){
            switch (state){
                case CLICKED: //負責監聽升級和購買-->左鍵購買；右鍵取消
                    if(e.getButton()==1){ //左鍵
                        if(arrowR.isTouch(e.getX(),e.getY())){//右箭頭-->最底是火箭
                            AudioResourceController.getInstance().shot("/skillSound.wav");
                            if(enemyButtons.get(0).left()<500 ) {
                                for (int i = 0; i < enemyButtons.size(); i++) {//全部都不能動
                                    System.out.println("右鍵+++");
                                    enemyButtons.get(i).offSetXY(500, 0);
                                }
                            }
                        }
                        if(arrowL.isTouch(e.getX(),e.getY())){ //左箭頭-->最底是Tank1
                            AudioResourceController.getInstance().shot("/skillSound.wav");
                            if(enemyButtons.get(4).left()>500) {
                                for (int i = 0; i < enemyButtons.size(); i++) {
                                    System.out.println("左鍵+++++");
                                   enemyButtons.get(i).offSetXY(-500, 0);
                                }
                            }
                        }
                    }
                    break;
            }
        }
    }
}




