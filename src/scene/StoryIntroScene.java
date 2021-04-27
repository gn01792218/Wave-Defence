package scene;

import controllers.AudioResourceController;
import controllers.ImageController;
import controllers.SceneController;
import menu.BackgroundType;
import menu.Button;
import menu.Style;
import utils.CommandSolver;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class StoryIntroScene extends Scene{
    private BufferedImage backGround;
    private Button roundStart;// 下一頁按鈕
    private BufferedImage say;//對話框
    private BufferedImage introImage;//對話內容
    private BufferedImage woman;//秘書
    private BufferedImage enemy1;//
    private BufferedImage enemy2;//
    private BufferedImage enemy3;//

    @Override
    public void sceneBegin() {
        AudioResourceController.getInstance().play("/Radio.wav"); //在skillScene End切掉
        backGround= ImageController.getInstance().tryGet("/UserSceneBack.png");
        enemy1=ImageController.getInstance().tryGet("/storyIntro1.png");
        enemy2=ImageController.getInstance().tryGet("/storyIntro2.png");
        enemy3=ImageController.getInstance().tryGet("/storyIntro3.png");
        woman=ImageController.getInstance().tryGet("/commander.png");
        say=ImageController.getInstance().tryGet("/say.png");
        introImage=ImageController.getInstance().tryGet("/introImage.png");
        roundStart=new Button(1350,850,new Style.StyleRect(225,151,
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/next.png"))));
        roundStart.setStyleHover(new Style.StyleRect(225,159,
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/next1.png"))));

    }

    @Override
    public void sceneEnd() {
        AudioResourceController.getInstance().stop("/Radio.wav"); //在skillScene End切掉
        backGround=null;
        roundStart=null;
        enemy1=null;
        enemy2=null;
        enemy3=null;
        woman=null;
        say=null;
        introImage=null;

    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return new CommandSolver.MouseListener() {
            @Override
            public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
                if (state != null) {
                    switch (state) {
                        case MOVED: //負責監聽浮現的資訊欄
                            if (roundStart.isTouch(e.getX(), e.getY())) {
                                roundStart.isHover(true);
                            } else {
                                roundStart.isHover(false);
                            }
                            break;
                        case CLICKED: //負責監聽升級和購買-->左鍵購買；右鍵取消
                            if (e.getButton() == 1) { //左鍵
                                if (roundStart.isTouch(e.getX(), e.getY())) {//1.觸發換場的按鈕
                                    AudioResourceController.getInstance().shot("/skillSound.wav");
                                    SceneController.getInstance().changeScene(UserScene.getInstance());
                                    break;
                                }
                            }
                    }
                }
            }
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(backGround,160,0,null);
        g.drawImage(enemy1,160,550,null);
        g.drawImage(enemy2,710,550,null);
        g.drawImage(enemy3,1260,550,null);
        g.drawImage(say,560,50,null);
        g.drawImage(woman,560,50,null);
        g.drawImage(introImage,680,130,null);
        roundStart.paint(g); //畫出開始回合的按鈕

    }

    @Override
    public void update() {

    }
}
