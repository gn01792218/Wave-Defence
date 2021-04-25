package scene;

import com.sun.scenario.effect.Color4f;
import controllers.AudioResourceController;
import controllers.ImageController;
import controllers.SceneController;
import menu.BackgroundType;
import menu.Button;
import menu.Label;
import menu.SkillButton;
import menu.Style;
import utils.CommandSolver;
import utils.Global;
import utils.Player;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SkillScene extends Scene {
    private BufferedImage backGround;//背景圖
    private ArrayList<SkillButton> skillButtons;
    private Button roundStart;// 進入回合的按鈕
    private Button previous;//上一頁
    private IntroPopupWindow introPopupWindow;//進入回合前的教學視窗
    private Label skillLabel; //購買技能的標籤
    private BufferedImage barImage;//
    private Label playerLevel;//目前在第幾關
    private Label playerMoney;//玩家的錢
    private Label playerHorn;//玩家榮譽


    @Override
    public void sceneBegin() {
        backGround=ImageController.getInstance().tryGet("/UserSceneBack.png");
        barImage=ImageController.getInstance().tryGet("/bar.png");
        playerLevel=new Label(1120,915,new Style.StyleRect(100,100,true,null));
        playerMoney=new Label(1320,915,new Style.StyleRect(100,100,true,null));
        playerHorn=new Label(1550,915,new Style.StyleRect(100,100,true,null));
        skillButtons = Global.getSkillButtons();//得到Global的技能按鈕
        roundStart=new Button(1350,650,new Style.StyleRect(150,150,
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/start.png"))));
        roundStart.setStyleHover(new Style.StyleRect(150,150,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/start1.png"))));
        previous=new Button(50,750,new Style.StyleRect(225,150,
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/previous.png"))));
        previous.setStyleHover(new Style.StyleRect(225,150,
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/previous1.png"))));
        introPopupWindow=Global.getIntroPopupWindow();
        introPopupWindow.setCancelable();
        introPopupWindow.hide();
        skillLabel=new Label(490,110,new Style.StyleRect(959,119,true,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/skill.png"))));
    }

    @Override
    public void sceneEnd() {
        for(int i=0;i<skillButtons.size();i++){
            skillButtons.get(i).getPaintStyle().setWidth(64);
            skillButtons.get(i).getPaintStyle().setHeight(64);
        }
    }
    @Override
    public CommandSolver.MouseListener mouseListener() {
        return new CommandSolver.MouseListener() {
            @Override
            public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
                if (state != null) {
                    if(introPopupWindow.isShow()){
                        introPopupWindow.mouseListener().mouseTrig(e, state, trigTime); //將Popwindow的監聽丟給本地監聽
                    }else {
                        switch (state) {
                            case MOVED: //負責監聽浮現的資訊欄
                                for (int i = 0; i < skillButtons.size(); i++) {
                                        skillButtons.get(i).setInfoVisable(true);
                                }
                                if(roundStart.isTouch(e.getX(),e.getY())){
                                    roundStart.isHover(true);
                                }else{roundStart.isHover(false);}
                                if(previous.isTouch(e.getX(),e.getY())){
                                    AudioResourceController.getInstance().play("/hover.wav");
                                    previous.isHover(true);
                                }else{previous.isHover(false);}
                                break;
                            case CLICKED: //負責監聽升級和購買-->左鍵購買；右鍵取消
                                if (e.getButton() == 1) { //左鍵
                                    if (roundStart.isTouch(e.getX(), e.getY()) && !introPopupWindow.isPassed()) {//1.觸發換場的按鈕
                                        AudioResourceController.getInstance().shot("/skillSound.wav");
                                        introPopupWindow.sceneBegin();//記得初始化場景，否則會nullPoint!
                                        introPopupWindow.show();
                                    } else if (roundStart.isTouch(e.getX(), e.getY()) && introPopupWindow.isPassed()) { //第二次之後 直接換場
                                        SceneController.getInstance().changeScene(GameScene.getInstance());
                                    }
                                    if(previous.isTouch(e.getX(),e.getY())){
                                        AudioResourceController.getInstance().shot("/buttonSound2.wav");
                                        SceneController.getInstance().changeScene(UserScene.getInstance());
                                    }
                                    for (int i = 0; i < skillButtons.size(); i++) {  //3.技能購買
                                        if (skillButtons.get(i).isTouch(e.getX(), e.getY()) &&
                                                !skillButtons.get(i).getIsSelect()) { //被點中 且還沒被點過時
                                            if (skillButtons.get(i).isUnLocked() && Player.getInstance().getHonor() >= skillButtons.get(i).getCost()) { //且已經解鎖了  且有榮譽職
                                                AudioResourceController.getInstance().shot("/skillSound.wav");
                                                skillButtons.get(i).setSelect(true); //設定為被選中的，在場景中只要new出被選中的技能即可
                                                Player.getInstance().offsetHonor(-skillButtons.get(i).getCost()); //扣榮譽值!
                                            } else if (!skillButtons.get(i).isUnLocked() && Player.getInstance().getHonor() >= skillButtons.get(i).getUnLockCost()) {
                                                //還沒解鎖 且 榮譽值大於等於解鎖的錢時
                                                AudioResourceController.getInstance().shot("/unLock.wav");
                                                skillButtons.get(i).setUnLocked(true); //設定成已經解鎖
                                                Player.getInstance().offsetHonor(-skillButtons.get(i).getUnLockCost()); //扣榮譽值!
                                            }
                                        }
                                    }
                                }
                                if (e.getButton() == 3) {//點擊右鍵 取消
                                    for (int i = 0; i < skillButtons.size(); i++) {  //2.技能取消購買  (解鎖無法取消)
                                        if (skillButtons.get(i).isTouch(e.getX(), e.getY()) && skillButtons.get(i).getIsSelect()) { //已經被選過的
                                            skillButtons.get(i).setSelect(false); //設成未被選中
                                            Player.getInstance().offsetHonor(+skillButtons.get(i).getCost()); //把錢+回來
                                        }
                                    }
                                }
                                break;
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
        for(int i=0;i<skillButtons.size();i++){
            skillButtons.get(i).setInSkillScene(true);
            skillButtons.get(i).getPaintStyle().setWidth(128);
            skillButtons.get(i).getPaintStyle().setHeight(128);
            skillButtons.get(i).paint(g);
        }
        roundStart.paint(g); //畫出開始回合的按鈕
        previous.paint(g);
        skillLabel.paint(g);
        if(introPopupWindow.isShow() && !introPopupWindow.isPassed()){
            introPopupWindow.paint(g);
            if(introPopupWindow.getButton()!=null) {
                introPopupWindow.getButton().paint(g);
            }
        }
        g.drawImage(barImage,200,850,null);
        //畫玩家金錢和榮譽
        playerLevel.getPaintStyle().setText(Global.getLevel()+"").setTextFont(new Font("標楷體",Font.BOLD,42)).setTextColor(new Color(0xFC246CC6, true));
        playerLevel.paint(g);
        playerMoney.getPaintStyle().setText(Player.getInstance().getMoney()+"").setTextFont(new Font("標楷體",Font.BOLD,42)).setTextColor(new Color(0xFC246CC6, true));
        playerMoney.paint(g);
        playerHorn.getPaintStyle().setText(Player.getInstance().getHonor()+"").setTextFont(new Font("標楷體",Font.BOLD,42)).setTextColor(new Color(0xFC246CC6, true));
        playerHorn.paint(g);

    }
    @Override
    public void update() { //第二次近來沒有進update
        for(int i=0;i<Global.getActorButtons().size();i++){
            if(Global.getActorButtons().get(i).getNumber()>0){
                System.out.println(Global.getActorButtons().get(0).getNumber());
                introPopupWindow.setArmyIsReady(true);
                System.out.println(introPopupWindow.isArmyIsReady());
            }
        }

    }
}
