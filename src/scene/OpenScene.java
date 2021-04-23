package scene;

import controllers.ImageController;
import controllers.SceneController;
import menu.BackgroundType;
import menu.Button;
import menu.Style;
import utils.CommandSolver;
import utils.Global;
import utils.RankList;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class OpenScene extends Scene {
    private Button startButton; //開始遊戲的按鈕
    private Button endlessModeButton;//無盡模式按鈕
    private Button leaderBoard;//排行榜按鈕
    private Button endButton; //結束遊戲的按鈕
    private BufferedImage image;//背景圖


    @Override
    public void paint(Graphics g) {
        g.drawImage(image,150,0,null);
        startButton.paint(g);
        endlessModeButton.paint(g);
        endButton.paint(g);
        leaderBoard.paint(g);
    }
    @Override
    public void update() {
    }
    @Override
    public void sceneBegin() {
        //背景圖
        image =ImageController.getInstance().tryGet("/OpenScene.png");
        //Button的第三個參數可newStyle；Style的三個參數為寬 高 背景圖片。
        startButton=new Button(650,520,new Style.StyleRect(250,106,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/B-openStart1.png"))));
        startButton.setStyleHover(new Style.StyleRect(250,106,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/B-openStart2.png"))));
        endlessModeButton=new Button(1000,520,new Style.StyleRect(250,106,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/EndlessModeB.png"))));
        endlessModeButton.setStyleHover(new Style.StyleRect(250,106,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/EndlessMode.png"))));
        endButton=new Button(780,750,new Style.StyleRect(300,300,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/end.png"))));
        leaderBoard=new Button(825,650,new Style.StyleRect(250,106,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/LeaderBoardB.png"))));
        leaderBoard.setStyleHover(new Style.StyleRect(250,106,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/LeaderBoard.png"))));
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
                                    SceneController.getInstance().changeScene(UserScene.getInstance());
                                }
                                if(endlessModeButton.isTouch(e.getX(),e.getY())){ //切換無盡模式
                                    SceneController.getInstance().changeScene(new ChallengeScene());
                                }
                                if(endButton.isTouch(e.getX(),e.getY())){  //離開按鈕
                                    System.exit(0);
                                }
                                if(leaderBoard.isTouch(e.getX(),e.getY())){
                                    SceneController.getInstance().changeScene(new LeaderboardScene());
                                }
                            }
                        case MOVED:
                                if (startButton.isTouch(e.getX(),e.getY())) { //開始遊戲按鈕的hover
                                    startButton.isHover(true);
                                }else{startButton.isHover(false);}
                                if(endlessModeButton.isTouch(e.getX(),e.getY())){
                                    endlessModeButton.isHover(true);
                                }else{endlessModeButton.isHover(false);}
                                if(leaderBoard.isTouch(e.getX(),e.getY())){
                                    leaderBoard.isHover(true);
                                }else{leaderBoard.isHover(false);}
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
