package scene;

import controllers.AudioResourceController;
import controllers.ImageController;
import controllers.SceneController;
import menu.*;
import menu.Button;
import menu.Label;
import utils.CommandSolver;
import utils.Global;
import utils.Player;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;
//角色資訊欄位-->裡面有多個labl(放攻擊 防禦 等等)。
//需要技能顯示資訊
//做確認訊息視窗
//買技能要扣榮譽值

public class UserScene extends Scene{ //改成單例模式!!!
    public static UserScene userScene;
    private BufferedImage backGround;//背景圖
    private BufferedImage backCover;//背景透明板
    private BufferedImage barImage;//
    private BufferedImage woman;//秘書
    private BufferedImage intro;//秘書說
    private ArrayList<ActorButton> actorButtons;
    private Button roundStart;// 進入回合的按鈕
    private Button previous;//上一頁
    private Button secrt;//機密檔案(敵軍資料)按鈕
    private Button arrowR;
    private Button arrowL;
    private Button music;//
    private boolean arrowRUseable;
    private boolean arrowLUseable;
    private Label armyLabel; //購買軍隊的標籤
    private Label enemyLabel; //敵軍機密的標籤
    private Label playerLevel;//目前在第幾關
    private Label playerMoney;//玩家的錢
    private Label playerHorn;//玩家榮譽
    private PopWindowScene popupWindow;//敵方資訊場景
    private IntroPopupWindow introPopupWindow;
    private boolean isBackToOpenscene;//是否有切回首頁
    private int count=0; //控制撥放音樂的focous
    private boolean sceneEnd;//是否結束場警
    //關於彈跳視窗內的按鈕位置問題:1.就算按鈕做在PopWindow內部，也必須要在外面的場景中paint出來(做出getButton方法)，否則按鈕位置會有誤差。
    private UserScene(){}
    public static UserScene getInstance(){
        if(userScene==null){
            userScene=new UserScene();
        }
        return userScene;
    }
    private BufferedImage imageTank1;
    private BufferedImage imageTank2;
    private BufferedImage imageLaserCar;
    private BufferedImage imageRocket;
    private int armyCount;

    @Override
    public void sceneBegin() {
        AudioResourceController.getInstance().loop("/Radio2.wav",99);
        sceneEnd=false;
        isBackToOpenscene=false; //剛開使沒有切回首頁
        //加入軍隊數量
        imageTank1= ImageController.getInstance().tryGet("/AB-Tank1-Small.png");
        imageTank2= ImageController.getInstance().tryGet("/AB-Tank2-Small.png");
        imageLaserCar= ImageController.getInstance().tryGet("/AB-LaserCar-Small.png");
        imageRocket= ImageController.getInstance().tryGet("/AB-Rocket-Small.png");
        woman=ImageController.getInstance().tryGet("/commander2.png");
        intro=ImageController.getInstance().tryGet("/UserIntro.png");
//        armyCount =0;
        //進入回合的按鈕
        backGround=ImageController.getInstance().tryGet("/UserSceneBack.png");
        backCover=ImageController.getInstance().tryGet("/UserBackCoverB.png");
        roundStart=new Button(1400,750,new Style.StyleRect(225,151,
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/next.png"))));
        roundStart.setStyleHover(new Style.StyleRect(225,159,
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/next1.png"))));
        previous=new Button(300,20,new Style.StyleRect(225,150,
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/previous.png"))));
        previous.setStyleHover(new Style.StyleRect(225,150,
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/previous1.png"))));
        secrt=new Button(1450, 250, new Style.StyleRect(236,205,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/secret-1.png"))));
        secrt.setStyleHover(new Style.StyleRect(236,205,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/secret-2.png"))));
            actorButtons=Global.getActorButtons();//得到Global的角色按鈕
        arrowR=new Button(1370,380,new Style.StyleRect(187,189,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/arrowR.png"))));
        arrowR.setStyleHover(new Style.StyleRect(187,187,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/arrowRH.png"))));
        arrowL=new Button(280,380,new Style.StyleRect(182,189,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/arrowL.png"))));
        arrowL.setStyleHover(new Style.StyleRect(187,187,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/arrowLH.png"))));
        armyLabel=new Label(665,30,new Style.StyleRect(600,87,true,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/army.png"))));
        enemyLabel=new Label(1450,160,new Style.StyleRect(214,58,true,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/enemy.png"))));
        playerLevel=new Label(1120,915,new Style.StyleRect(100,100,true,null));
        playerMoney=new Label(1320,915,new Style.StyleRect(100,100,true,null));
        playerHorn=new Label(1550,915,new Style.StyleRect(100,100,true,null));
        barImage=ImageController.getInstance().tryGet("/bar.png");
        popupWindow=new PopWindowScene(130,50,1300,600);
        popupWindow.setCancelable();
        popupWindow.hide();
        introPopupWindow=Global.getIntroPopupWindow();
        introPopupWindow.setCancelable();
        introPopupWindow.hide();
        music=new Button(1500,50,new Style.StyleRect(100,40,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/music.png"))));
        music.setStyleFocus(new Style.StyleRect(100,40,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/musicB.png"))));
    }
    @Override
    public void sceneEnd() {
        sceneEnd=true;
        backGround=null;
        backCover=null;
        barImage=null;
        woman=null;
        intro=null;
        introPopupWindow=null;
        popupWindow=null;
        armyLabel=null;
        enemyLabel=null;
        playerLevel=null;
        playerHorn=null;
        playerMoney=null;
        imageLaserCar=null;
        imageRocket=null;
        imageTank1=null;
        imageTank2=null;
        if(isBackToOpenscene){ //從這裡直接回首頁的話  要關掉音樂
            AudioResourceController.getInstance().stop("/Radio2.wav"); //在skillScene End切掉
        }
//        if(!music.getIsFocus()){
//            AudioResourceController.getInstance().stop("/Radio2.wav");
//        }

    }
    @Override
    public CommandSolver.MouseListener mouseListener() {
        return new CommandSolver.MouseListener() {
            @Override
            public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
                if (state != null) {
                    if (popupWindow.isShow()) {
                        popupWindow.mouseListener().mouseTrig(e, state, trigTime);
                    }else if(introPopupWindow.isShow()){
                        introPopupWindow.mouseListener().mouseTrig(e, state, trigTime); //將Popwindow的監聽丟給本地監聽
                    }else {
                        switch (state) {
                            case MOVED: //負責監聽浮現的資訊欄
                                for (int i = 0; i < actorButtons.size(); i++) {    //每個按鈕監聽滑鼠移動
                                    if (actorButtons.get(i).isTouch(e.getX(), e.getY())) { //移動到角色上會有訊息欄
                                        //座標產生資訊圖片-->把角色圖片資訊設成visabl
                                        actorButtons.get(i).setInfoVisable(true);
                                    } else {
                                        actorButtons.get(i).setInfoVisable(false);
                                    }
                                }
                                if (secrt.isTouch(e.getX(), e.getY())) { //機密文件
                                    secrt.isHover(true);
                                } else {
                                    secrt.isHover(false);
                                }
                                if(arrowR.isTouch(e.getX(),e.getY())){
                                    arrowR.isHover(true);
                                }else{
                                    arrowR.isHover(false);}
                                if(arrowL.isTouch(e.getX(),e.getY())){
                                    arrowL.isHover(true);
                                }else{
                                    arrowL.isHover(false);}
                                if(roundStart.isTouch(e.getX(),e.getY())){
                                    if(!introPopupWindow.isArmyIsReady()) {
//                                        AudioResourceController.getInstance().play("/hover2.wav");
                                    }
                                    roundStart.isHover(true);
                                }else{
                                    if(!sceneEnd)
                                    roundStart.isHover(false);}
                                if(previous.isTouch(e.getX(),e.getY())){
                                    previous.isHover(true);
                                }else{ if(!sceneEnd)
                                    previous.isHover(false);}
                                break;
                            case CLICKED: //負責監聽升級和購買-->左鍵購買；右鍵取消
                                if (e.getButton() == 1) { //左鍵
                                    if (roundStart.isTouch(e.getX(), e.getY())) {//1.觸發換場的按鈕
                                        AudioResourceController.getInstance().shot("/skillSound.wav");
                                        if(introPopupWindow.isArmyIsReady()){ //準備好才換場
                                            SceneController.getInstance().changeScene(new SkillScene());
                                        }else { //否則叫他買軍隊
                                            introPopupWindow.sceneBegin();//記得初始化場景，否則會nullPoint!
                                            introPopupWindow.show();
                                        }
                                    }
                                    if(previous.isTouch(e.getX(),e.getY())){
                                        isBackToOpenscene=true;//設定回到首頁，以關掉音樂
                                        AudioResourceController.getInstance().shot("/buttonSound2.wav");
                                        SceneController.getInstance().changeScene(new OpenScene());
                                    }
                                    for (int i = 0; i < actorButtons.size(); i++) { //2.角色購買
                                        if (actorButtons.get(i).isTouch(e.getX(), e.getY())) {
                                            //購買軍隊
                                            if (Player.getInstance().getMoney() >= actorButtons.get(i).getCostMoney()
                                                    && actorButtons.get(i).left() >= 830 && actorButtons.get(i).right() <= 1330 && actorButtons.get(i).isUnLocked()) { //金錢大於0才可以
                                                actorButtons.get(i).offSetNumber(1); //點一下增加一單位
                                                AudioResourceController.getInstance().shot("/skillSound.wav");
                                                Player.getInstance().offsetMoney(-actorButtons.get(i).getCostMoney()); //扣錢
                                            }
                                        }
                                    }
                                    if (arrowR.isTouch(e.getX(), e.getY())) {//右箭頭-->最底是火箭
                                        AudioResourceController.getInstance().shot("/skillSound.wav");
                                        if (actorButtons.get(0).left() < 730) {
                                            for (int i = 0; i < actorButtons.size(); i++) {//全部都不能動
                                                actorButtons.get(i).offSetXY(500, 0);
                                            }
                                        }
                                    }
                                    if (arrowL.isTouch(e.getX(), e.getY())) { //左箭頭-->最底是Tank1
                                        AudioResourceController.getInstance().shot("/skillSound.wav");
                                        if (actorButtons.get(3).left() > 830) {
                                            for (int i = 0; i < actorButtons.size(); i++) {
                                                actorButtons.get(i).offSetXY(-500, 0);
                                            }
                                        }
                                    }
                                    if (secrt.isTouch(e.getX(), e.getY())) {
                                            popupWindow.sceneBegin();
                                            popupWindow.show();

                                    }else {
                                        if(!sceneEnd)
                                        popupWindow.hide();
                                    }
                                    if(music.isTouch(e.getX(),e.getY())){
                                        count++;
                                        if(count%2==0){
                                            music.isFocus();
                                            AudioResourceController.getInstance().stop("/Radio2.wav"); //在skillScene End切掉
                                        }else{
                                                music.unFocus();
                                                AudioResourceController.getInstance().play("/Radio2.wav"); //在skillScene End切掉
                                        }
                                    }
                                }
                                if (e.getButton() == 3) {//點擊右鍵 取消
                                    for (int i = 0; i < actorButtons.size(); i++) { //1.角色取消購買
                                        if (actorButtons.get(i).isTouch(e.getX(), e.getY()) && actorButtons.get(i).getNumber() > 0) { //觸碰到 且數量大於0時，才可以取消
                                            AudioResourceController.getInstance().shot("/buttonSound2.wav");
                                            actorButtons.get(i).offSetNumber(-1); //點一下增加一單位
                                            Player.getInstance().offsetMoney(+actorButtons.get(i).getCostMoney());  //把錢+回來
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
        g.drawImage(backCover,830,180,null);
        roundStart.paint(g); //畫出開始回合的按鈕
        previous.paint(g);
        secrt.paint(g);//化機密檔案
        armyLabel.paint(g);
        enemyLabel.paint(g);
        for(int i=0;i<actorButtons.size();i++){
            if(actorButtons.get(i).left()<1230 && actorButtons.get(i).right()>830) {
                actorButtons.get(i).paint(g);
            }
        }
        if(arrowRUseable){
            arrowR.paint(g);
        }
        if(arrowLUseable){
            arrowL.paint(g);
        }
        g.drawImage(woman,1100,620,null);
        g.drawImage(intro,1110,520,null);
        g.drawImage(barImage,200,850,null);
        if(popupWindow.isShow()) {
            popupWindow.paint(g);
            popupWindow.getArrowR().paint(g);
            popupWindow.getArrowL().paint(g);
        }

//        畫菜籃
        armyCount =0;
        if(actorButtons.get(0).getNumber()>0){
            g.drawImage(imageTank1,350+armyCount*200,620,null);
            g.drawString(actorButtons.get(0).getNumber()+"",350+armyCount*200,700);
            armyCount++;
        }
        if(actorButtons.get(1).getNumber()>0){
            g.drawImage(imageTank2,350+armyCount*200,620,null);
            g.drawString(actorButtons.get(1).getNumber()+"",350+armyCount*200,700);
            armyCount++;
        }
        if(actorButtons.get(2).getNumber()>0){
            g.drawImage(imageLaserCar,350+armyCount*200,600,null);
            g.drawString(actorButtons.get(2).getNumber()+"",350+armyCount*200,700);
            armyCount++;
        }
        if(actorButtons.get(3).getNumber()>0){
            g.drawImage(imageRocket,350+armyCount*200,620,null);
            g.drawString(actorButtons.get(3).getNumber()+"",350+armyCount*200,700);
            armyCount++;
        }
        //畫玩家金錢和榮譽
        playerLevel.getPaintStyle().setText(Global.getLevel()+"").setTextFont(new Font("標楷體",Font.BOLD,42)).setTextColor(new Color(0xFFFF6333, true));
        playerLevel.paint(g);
        playerMoney.getPaintStyle().setText(Player.getInstance().getMoney()+"").setTextFont(new Font("標楷體",Font.BOLD,42)).setTextColor(new Color(0xFFFF6333, true));
        playerMoney.paint(g);
        playerHorn.getPaintStyle().setText(Player.getInstance().getHonor()+"").setTextFont(new Font("標楷體",Font.BOLD,42)).setTextColor(new Color(0xFFFF6333, true));
        playerHorn.paint(g);
        //警告標語
        if(introPopupWindow.isShow()){
            introPopupWindow.paint(g);
        }
        music.paint(g);
    }
    @Override
    public void update() {
        if(actorButtons.get(0).left()<=830 && actorButtons.get(0).left()>=-170){ //當T1在第一張時
            arrowLUseable=true;
        }else{arrowLUseable=false;}
        if(actorButtons.get(3).right()>=1330 && actorButtons.get(3).right()<=2330) {
            arrowRUseable = true;
        }else{arrowRUseable=false;}
        int count=0;
        for(int i=0;i<Global.getActorButtons().size();i++){
            if(Global.getActorButtons().get(i).getNumber()>0){
                introPopupWindow.setArmyIsReady(true);
                break;
            }
            count++;
        }
        if(count>=Global.getActorButtons().size()){ //全部小於0時就是沒準備好
            introPopupWindow.setArmyIsReady(false);
        }
    }
}
