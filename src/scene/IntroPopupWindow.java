package scene;

import controllers.AudioResourceController;
import controllers.ImageController;
import controllers.SceneController;
import menu.BackgroundType;
import menu.Button;
import menu.PopupWindow;
import menu.Style;
import utils.CommandSolver;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

//按鈕必須要在外面畫才不會走鐘

public class IntroPopupWindow extends PopupWindow {

    private Button button;//確認按鈕
    private BufferedImage image1;//教學圖片1
    private BufferedImage image2;//教學圖片
    private BufferedImage warningimage;//警告標語
    private boolean isPassed; //已經被看過了
    private boolean armyIsReady;//已經購買>0數量的軍隊

    public IntroPopupWindow(int x, int y, int width, int height){
        super(x,y,width,height);

    }
    @Override
    public void sceneBegin() {
        button=new Button(800,650,new Style.StyleRect(150,150, //畫出確認按鈕
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/start.png"))));
        button.setStyleHover(new Style.StyleRect(150,150,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/start1.png"))));
        image1=ImageController.getInstance().tryGet("/intro1.png");
        image2= ImageController.getInstance().tryGet("/intro2.png");
        armyIsReady=false;
    }
    @Override
    public void sceneEnd() {
        button=null;
        image1=null;
        image2=null;
    }

    public Button getButton() {
        return button;
    }

    public boolean isArmyIsReady() {
        return armyIsReady;
    }

    public void setArmyIsReady(boolean armyIsReady) {
        this.armyIsReady = armyIsReady;
    }

    public void setPassed(boolean passed) {
        isPassed = passed;
    }

    public boolean isPassed() {
        return isPassed;
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

    @Override
    public void paintWindow(Graphics g) {
        if(armyIsReady) {  //準備好的時候畫教學提醒
            g.setColor(new Color(0xDC72696C, true));
            g.fillRect(super.getX(), super.getY(), super.getWidth(), super.getHeight());
            g.drawImage(image1, this.getX() + 100, this.getY() + 300, null);
            g.drawImage(image2, this.getX() + 800, this.getY() + 300, null);
        }else{ //否則跳出警告視窗
            if(warningimage==null){
                warningimage=ImageController.getInstance().tryGet("/warning.png");
            }
            g.drawImage(warningimage,getX()+350,getY(),null);
        }
    }
    @Override
    public void update() {
    }
    @Override
    protected void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
        if(state!=null){
            switch (state){
                case MOVED:
                    if(button.isTouch(e.getX(),e.getY())){
                        button.isHover(true);
                    }else{button.isHover(false);}
                case CLICKED:
                    if(e.getButton()==1){
                        if(button.isTouch(e.getX(),e.getY())){
                            isPassed=true; //點擊到就變成true
                            AudioResourceController.getInstance().shot("/skillSound.wav");
                            SceneController.getInstance().changeScene(GameScene.getInstance());
                        }
                    }
            }
        }
    }
}
