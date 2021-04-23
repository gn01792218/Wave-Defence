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
        arrowR=new Button(1250,380,new Style.StyleRect(150,113,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/arrowR.png"))));
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
        g.setColor(new Color(0x2B2B34, false));
        g.fillRect(super.getX(), super.getY(), super.getWidth(), super.getHeight());
        for(int i=0;i<enemyButtons.size();i++){
            if(enemyButtons.get(i).left()<950 && enemyButtons.get(i).right()>550) {
                enemyButtons.get(i).paint(g);
            }
        }
        if(arrowRUseable){
            arrowR.paint(g);
        }
        if(arrowLUseable){
            arrowL.paint(g);
        }
    }
    @Override
    public void update() {
        if(enemyButtons.get(0).left()<=-350 && enemyButtons.get(0).left()>=-1350){ //當T1在第一張時
            this.arrowLUseable=true;
        }else{this.arrowLUseable=false;}
        if(enemyButtons.get(4).right()>=1550 && enemyButtons.get(4).right()<=2550) {
            this.arrowRUseable = true;
        }else{this.arrowRUseable=false;}
    }
    @Override
    protected void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
        System.out.println("進入敵軍資訊監聽");
        if(state!=null){
            switch (state){
                case MOVED:
                    for (int i = 0; i < enemyButtons.size(); i++) {    //每個按鈕監聽滑鼠移動
                        if (enemyButtons.get(i).isTouch(e.getX(), e.getY())) { //移動到角色上會有訊息欄
                            //座標產生資訊圖片-->把角色圖片資訊設成visabl
                            enemyButtons.get(i).setInfoVisable(true);
                        } else {
                            enemyButtons.get(i).setInfoVisable(false);
                        }
                    }
                case CLICKED: //負責監聽升級和購買-->左鍵購買；右鍵取消
                    if(e.getButton()==1){ //左鍵
                        if(arrowR.isTouch(e.getX(),e.getY())){//右箭頭-->最底是火箭
                            AudioResourceController.getInstance().shot("/skillSound.wav");
                            if(enemyButtons.get(0).left()<550 ) {
                                for (int i = 0; i < enemyButtons.size(); i++) {//全部都不能動
                                    System.out.println("右鍵+++");
                                    enemyButtons.get(i).offSetXY(500, 0);
                                }
                            }
                        }
                        if(arrowL.isTouch(e.getX(),e.getY())){ //左箭頭-->最底是Tank1
                            AudioResourceController.getInstance().shot("/skillSound.wav");
                            if(enemyButtons.get(4).left()>550) {
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




