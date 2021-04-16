package scene;

import controllers.ImageController;
import controllers.SceneController;
import menu.BackgroundType;
import menu.Button;
import menu.Style;
import utils.CommandSolver;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class OpenScene extends Scene {
    private Button startButton; //開始遊戲的按鈕
    private Button endButton; //結束遊戲的按鈕
    private BufferedImage image;//背景圖

    @Override
    public void paint(Graphics g) {
        g.drawImage(image,350,80,null);
        startButton.paint(g);
        endButton.paint(g);
    }
    @Override
    public void update() {
    }
    @Override
    public void sceneBegin() {
        //背景圖
        image =ImageController.getInstance().tryGet("/Scene1-2.png");
        //Button的第三個參數可newStyle；Style的三個參數為寬 高 背景圖片。
        startButton=new Button(800,500,new Style.StyleRect(250,106,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/B-openStart1.png"))));
        startButton.setStyleHover(new Style.StyleRect(250,106,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/B-openStart2.png"))));
        endButton=new Button(780,600,new Style.StyleRect(300,300,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/end.png"))));
    }

    @Override
    public void sceneEnd() {
    }
    @Override
    public CommandSolver.MouseListener mouseListener() {
        return new CommandSolver.MouseListener() {
            @Override
            public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
                if(state!=null) {
                    switch (state) {
                        case CLICKED:
                            if(e.getButton()==1) {
                                if (startButton.isTouch(e.getX(),e.getY())) { //開始遊戲按鈕
                                    SceneController.getInstance().changeScene(new UserScene());
                                }
                                if(endButton.isTouch(e.getX(),e.getY())){  //離開按鈕
                                    System.exit(0);
                                }
                            }
                        case MOVED:
                                if (startButton.isTouch(e.getX(),e.getY())) { //開始遊戲按鈕的hover
                                    startButton.isHover(true);
                                }else{startButton.isHover(false);}
                    }
                }
            }
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }
}
