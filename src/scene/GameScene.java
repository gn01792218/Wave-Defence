package scene;

import controllers.AudioResourceController;
import controllers.ImageController;
import controllers.SceneController;
import gameobj.*;

import menu.*;
import menu.Button;
import utils.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
//前10秒可以操控物件往目標方向移動
//當鼠標位置進入物件的範圍，目標就會變成 isClick(只有我軍有效)
//更新時，只有isInClik可以移動到旗子座標點
//問題:倒數10秒的動畫要重弄
//每回合3波，完後 delay5秒換場
//判斷打輸的條件是全滅，但假如沒$$買軍隊時，直接進場，就會直接走失敗畫面然後+$$-->變成洗錢的Bug
//部隊卡住時，會無法移動(敵軍也是)-->解方 控制敵軍出生時候的位置不要距離太近(開啟deBug模式)
public class GameScene extends Scene {
    public static GameScene gameScene;
    //場地左上角X Y(380,180)；場地右下角xy (1060,700) 。
    private BufferedImage image; //背景圖
    private BufferedImage image1_1;
    private BufferedImage image2; //失敗的圖片
    private BufferedImage image3;//倒數10秒圖片
    private BufferedImage image4;// 挑戰成功的圖片
    private BufferedImage laserUnlock;//解鎖雷射車
    private BufferedImage rocketUnlock;//解鎖火箭車
    private Button roundStart;// 進入回合的按鈕
    private Boolean isReady;// 進入回合的按鈕

    private ArrayList<Actor> alliance; //角色陣列
    private ArrayList<Actor> enemys; //敵軍
    private ArrayList<SkillButton> skill;//技能陣列
    private boolean isFlagUsable;
    private boolean gameComplete;
    private int completeStep;
    private int step;

    private Delay starDelayCount;//回合前20秒的delay
    private Delay delayCount;//10秒後倒數10秒的週期播放
    private Delay changePicDelay;//每秒鐘圖片變化
    private Delay delay; //過場秒數

    private int countNum; //倒數的播放號碼
    private int changePic; //倒數動畫
    private Actor allianceControl;//受旗子控制的我軍
    private int count;//共三波(SceneBegin+2)
    private GameScene(){}
    public static GameScene getInstance(){
        if(gameScene==null){
            gameScene=new GameScene();
        }
        return gameScene;
    }
    @Override
    public void sceneBegin() {

        if(Global.getLevel()==1){
            image = ImageController.getInstance().tryGet("/GameScene1.png"); //場景圖
        }else if(Global.getLevel()==2){
            image = ImageController.getInstance().tryGet("/GameScene2.png");
        }else if(Global.getLevel()==3){
            image = ImageController.getInstance().tryGet("/GameScene3.png");
        }
        image1_1=ImageController.getInstance().tryGet("/GameScene1-1.png");
        image3 = ImageController.getInstance().tryGet("/count.png"); //倒數的圖片

        roundStart=new Button(1450,500,new Style.StyleRect(150,150,
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/start.png"))));
        roundStart.setStyleHover(new Style.StyleRect(150,150,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/start1.png"))));
        isReady = false;

        starDelayCount = new Delay(1200);//倒數前20秒
        delayCount = new Delay(600);//倒數10秒
        changePicDelay = new Delay(60);//每秒鐘圖片變化
        delay=new Delay(240); //過場delay
        starDelayCount.play();

        isFlagUsable = true;
        gameComplete =false;
        completeStep=0;
        step=1;
        count=0;
        //作技能
        skill=new ArrayList<>();
        ArrayList<SkillButton> temp=Global.getSkillButtons(); //從世界技能紐中下訂單
        for(int i=0;i<temp.size();i++){
            if(temp.get(i).getIsSelect()) { //有被選中的才new出來
                switch (temp.get(i).getSkillName()) {
                    case ATTACKUP:
                        temp.get(i).setInfoVisable(false);
                        temp.get(i).setSelect(false); //把Global的技能按鈕設成非選，才不會在下一場又免費出現!!!!!!!
                        skill.add(new AttackUp(1390+skill.size()%4*100+skill.size()/4*100,350+skill.size()/4*100,temp.get(i).getStyleNormal(),temp.get(i).getSkillName(),temp.get(i).getCost())); //設置在場中的位置
                        skill.get(skill.size()-1).setInGameScene(true);
                        break;
                    case HPUP:
                        temp.get(i).setInfoVisable(false);
                        temp.get(i).setSelect(false); //設成非選，才不會在下一場又免費出現!!!!!!!
                        skill.add(new HpUp(1390+skill.size()%4*100+skill.size()/4*100,350+skill.size()/4*100,temp.get(i).getStyleNormal(),temp.get(i).getSkillName(),temp.get(i).getCost())); //設置在場中的位置
                        skill.get(skill.size()-1).setInGameScene(true);
                        break;
                    case DEFUP:
                        temp.get(i).setInfoVisable(false);
                        temp.get(i).setSelect(false); //設成非選，才不會在下一場又免費出現!!!!!!!
                        skill.add(new DefUp(1390+skill.size()%4*100+skill.size()/4*100,350+skill.size()/4*100,temp.get(i).getStyleNormal(),temp.get(i).getSkillName(),temp.get(i).getCost())); //設置在場中的位置
                        skill.get(skill.size()-1).setInGameScene(true);
                        break;
                    case MOVESPEEDUP:
                        temp.get(i).setInfoVisable(false);
                        temp.get(i).setSelect(false); //設成非選，才不會在下一場又免費出現!!!!!!!
                        skill.add(new SpeedUp(1390+skill.size()%4*100+skill.size()/4*100,350+skill.size()/4*100,temp.get(i).getStyleNormal(),temp.get(i).getSkillName(),temp.get(i).getCost())); //設置在場中的位置
                        skill.get(skill.size()-1).setInGameScene(true);
                        break;
                    case REINFORCEMENTS:
                        SkillButton s1=new Reinforcements(1390+skill.size()%4*100+skill.size()/4*100,350+skill.size()/4*100,temp.get(i).getStyleNormal(),temp.get(i).getSkillName(),temp.get(i).getCost());
                        s1.setInfoVisable(false);
                        s1.setUnLocked(true);
                        temp.get(i).setSelect(false); //設成非選，才不會在下一場又免費出現!!!!!!!
                        skill.add(s1); //設置在場中的位置
                        skill.get(skill.size()-1).setInGameScene(true);
                        break;
                    case  ELECTWAVE:
                        SkillButton s2=new ElectWave(1390+skill.size()%4*100+skill.size()/4*100,350+skill.size()/4*100,temp.get(i).getStyleNormal(),temp.get(i).getSkillName(),temp.get(i).getCost());
                        s2.setInfoVisable(false);
                        s2.setUnLocked(true);
                        temp.get(i).setSelect(false); //設成非選，才不會在下一場又免費出現!!!!!!!
                        skill.add(s2);
                        skill.get(skill.size()-1).setInGameScene(true);
                        break;
                    case ATKSPEEDUP:
                        SkillButton s3=new AtkSpeedUp(1390+skill.size()%4*100+skill.size()/4*100,350+skill.size()/4*100,temp.get(i).getStyleNormal(),temp.get(i).getSkillName(),temp.get(i).getCost());
                        s3.setUnLocked(true);
                        s3.setInfoVisable(false);
                        temp.get(i).setSelect(false); //設成非選，才不會在下一場又免費出現!!!!!!!
                        skill.add(s3);
                        skill.get(skill.size()-1).setInGameScene(true);
                        break;
                }
            }
        }
        //做軍隊
        alliance = new ArrayList<>();
        enemys = new ArrayList<>();
        for (int i = 0; i < Global.getActorButtons().size(); i++) { //從Global中的角色按鈕取得選單下的訂單
            for (int j = 0; j < Global.getActorButtons().get(i).getNumber(); j++) { //跑某個角色的數量次
                switch (Global.getActorButtons().get(i).getActorType()) { //依據該型號做出該數量的戰隊
                    case TANK1: //畫j才不會疊在一起!!!
                        alliance.add(new Tank1(Global.BOUNDARY_X1 + j * 100, Global.BOUNDARY_Y2-90*i, false));
                        break;
                    case TANK2:
                        alliance.add(new Tank2(Global.BOUNDARY_X1 + j * 100, Global.BOUNDARY_Y2-90*i, false));
                        break;
                    case LASERCAR:
                        alliance.add(new LaserCar(Global.BOUNDARY_X1+j*100,Global.BOUNDARY_Y2-90*i,false));
                        break;
                    case ROCKET:
                        alliance.add(new Rocket(Global.BOUNDARY_X1+50+j*100,Global.BOUNDARY_Y2-90*i,false));
                }
            }
        }
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
                                if (roundStart.isTouch(e.getX(), e.getY())) {//1.觸發換場的按鈕
                                    roundStart.setStyleHover(new Style.StyleRect(150,150,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/start1.png"))));
                                    if(delayCount.isPlaying() || starDelayCount.isPlaying()){
                                        isReady = true;
                                    }
                                }
                                if(gameComplete){
                                    completeStep++;
                                }
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
                                for(int i=0;i<skill.size();i++){ //監聽玩家是否有點技能按鈕
                                    if(skill.get(i).isTouch(e.getX(),e.getY()) && skill.size()>0 && !skill.get(i).isUsed()){ //按鈕被點時 且 還有按鈕時
                                        AudioResourceController.getInstance().play("/skillSound.wav");// 音效聲音，可以大聲點嗎?
                                        skill.get(i).skillBufftimePlay();// 才啟動技能
                                        if(skill.get(i).getSkillName()== Global.SkillName.ELECTWAVE){
                                            skill.get(i).skillExection(enemys);
                                        }else{
                                            skill.get(i).skillExection(alliance); //執行技能~
                                        }
                                        skill.get(i).setUsed(true);
                                    }
                                }
                                System.out.println("左鍵");
                            } else if (e.getButton() == e.BUTTON2) {
                                System.out.println("中鍵");
                            } else if (e.getButton() == 3) {//也可以這樣
                                //旗子在可以使用的狀態才接收座標
                                System.out.println("右鍵");
                                if (isFlagUsable&&allianceControl!=null) {
                                    allianceControl.getFlag().setCenter(e.getX(), e.getY());
                                }
                            }
                        case MOVED:
                            for(int i=0;i<skill.size();i++){
                                if(skill.get(i).isTouch(e.getX(),e.getY())){
                                    skill.get(i).setInfoVisable(true);
                                }else{ skill.get(i).setInfoVisable(false);}
                            }
                            if(roundStart.isTouch(e.getX(),e.getY())){
                                roundStart.isHover(true);
                            }else{roundStart.isHover(false);}
                            break;
                    }
                }
            }
        };
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, -150, null);

        int tx = this.countNum * 74;  //0-74 1-74*2 2-74*3

        if(delayCount.isPlaying()){
            g.drawImage(image3, 750 - changePic, 100 - changePic, 750 + 74 + changePic, 100 + 90 + changePic,
                    tx, 0, tx + 74, 90, null); //倒數的圖片
        }
        for (int i = 0; i < alliance.size(); i++) {
            alliance.get(i).paint(g); //畫我軍
        }
        for (int i = 0; i < enemys.size(); i++) {
                enemys.get(i).paint(g); //畫敵軍
        }
        if(skill.size()>0) {
            for (int i = 0; i < skill.size(); i++) {
                if(!skill.get(i).isUsed()) {  //沒被使用過的時候，畫技能
                    skill.get(i).paint(g); //畫技能
                }
            }
        }
        if (isFlagUsable && allianceControl!=null) {
            allianceControl.getFlag().paint(g); //旗子可以使用的時候才畫出來
        }
        if(Global.getLevel()==1) { //只有在第一關才畫
            g.drawImage(image1_1, 0, -150, null);
        }
        roundStart.paint(g); //畫出開始回合的按鈕

        //當獲勝的時候
        if(gameComplete){
            if(completeStep==3){
                if(Global.getLevel()<=3) {
                    SceneController.getInstance().changeScene(UserScene.getInstance());
                }else if(Global.getLevel()>3){
                    SceneController.getInstance().changeScene(new EndScene()); //謝幕
                }
            }else if(completeStep==2 && Global.getLevel()<=2){
                rocketUnlock = ImageController.getInstance().tryGet("/AARocket_UnLock.png");
                g.drawImage(rocketUnlock, 550, 250, null);
            }else if(completeStep==1 && Global.getLevel()<=2){
                laserUnlock = ImageController.getInstance().tryGet("/AALaserCar_UnLock.png");
                g.drawImage(laserUnlock, 550, 250, null);
            }else{
                if(Global.getLevel()==2) {
                    image4 = ImageController.getInstance().tryGet("/Victory.png");
                    g.drawImage(image4, 130, 100, null);
                }else if(Global.getLevel()==3){
                    image4 = ImageController.getInstance().tryGet("/Victory2.png");
                    g.drawImage(image4, 130, 100, null);
                }else if(Global.getLevel()>3){
                    image4 = ImageController.getInstance().tryGet("/Victory3.png");
                    g.drawImage(image4, 130, 100, null);
                }
            }
        }
        if (alliance.size() <= 0) { //死光時畫失敗畫面
            if(Global.getLevel()==1){
            image2 = ImageController.getInstance().tryGet("/fail1.png");
            g.drawImage(image2, 350, 250, null);
            }else if(Global.getLevel()==2){
                image2 = ImageController.getInstance().tryGet("/fail2.png");
                g.drawImage(image2, 350, 250, null);
            }
        }
        Player.getInstance().paint(g);

    }
    //當偵測到被點到，開啟可以移動，時才移動，並一直移動到目標點，然後
    @Override
    public void update() {
        if(skill.size()>0) {
            for (int i = 0; i < skill.size(); i++) {
                if (skill.get(i).isUsed()) { //沒有被施放過
                    if (skill.get(i).getBuffTime().count()) {
                        if(skill.get(i).getSkillName()==Global.SkillName.ELECTWAVE){//電磁波的~!
                            skill.get(i).skillReset(enemys);
                        }else{ skill.get(i).skillReset(alliance);}//時間到全軍恢復原廠設置~!
                      //完成後移除技能
                        skill.remove(i); //移除技能~
                    }
                }
            }
        }
        //我軍的update
        for (int i = 0; i < alliance.size(); i++) {  //我軍自己移動
            alliance.get(i).autoAttack(enemys, alliance);
            alliance.get(i).bulletsUpdate(enemys); //發射子彈
                if (!alliance.get(i).isAlive()) { //沒有活著的時候移除
                    for (int j = 0; j < Global.getActorButtons().size(); j++) { //和Glabl的角色類型作比對
                        if (Global.getActorButtons().get(j).getActorType() == alliance.get(i).getType() && !alliance.get(i).isReinforcement() ) { //重要!!!且不是援軍，才-1
                            Global.getActorButtons().get(j).offSetNum(-1); //該類型的角色數量-1
                        }
                    }
                    alliance.remove(i);
                    i--;
                    break;
                }
        }
        //敵軍update
        for (int i = 0; i < enemys.size(); i++) {
                enemys.get(i).autoAttack(alliance, enemys);
            enemys.get(i).bulletsUpdate(alliance);
            if (!enemys.get(i).isAlive()) {
                enemys.remove(i);
                Player.getInstance().offsetMoney(+100); //殺一隻敵軍100元
                Player.getInstance().offsetHonor(+50); //殺一隻敵軍榮譽+100
                break;
            }
        }

        if(starDelayCount.count()){
            delayCount.play();
            changePicDelay.loop();
            starDelayCount.stop();
            changePic=50;
        }

        if(step ==1 && count<3){

            if(delayCount.isPlaying()){
                if (changePicDelay.count()) {  //每1秒播放圖片
                    countNum++;
                    changePic = 50;
                }
                changePic--;
            }



            if (delayCount.count() || isReady) { //開場30秒後
                isFlagUsable = false; //旗子不能用
                count++;
                countNum=0;
                starDelayCount.stop();
                delayCount.stop();
                changePicDelay.stop();
                isReady =false;
                if(allianceControl!=null) {
                    allianceControl.setControl(false);
                }
                step++;
            }
        }
        //產生敵軍
        if (Global.getLevel()==1 && step==2) {//敵軍可以移動時
            if(count==1){
                //做敵軍第一波
                for (int i = 0; i <  Global.random(5, 7); i++) {
                    enemys.add(new Enemy1(Global.random(400, 1000), Global.random(450, 400), true));
                }
            }
            if (count == 2) {
                for (int i = 0; i < 5; i++) {
                    enemys.add(new Enemy1(Global.random(400, 1000), Global.random(400, 450), true));
                }
                for (int i = 0; i < 2; i++) {
                    enemys.add(new Enemy4(Global.random(400, 1000), Global.random(400, 400), true));
                }
            }
            if(count ==3){
                for (int i = 0; i < 5; i++) {
                    enemys.add(new Enemy1(Global.random(400, 1000), Global.random(400, 350), true));
                }
                for (int i = 0; i < 3; i++) {
                    enemys.add(new Enemy4(Global.random(400, 1000), Global.random(400, 350), true));
                }
            }
            step++;
        }

        //第二關
        if (Global.getLevel()==2 && step==2) {//敵軍可 以移動時
            if(count==1){
                //做敵軍第一波
                for (int i = 0; i < 6; i++) {
                    enemys.add(new Enemy1(Global.random(400, 1000), Global.random(400, 350), true));
                }
                for (int i = 0; i < 3; i++) {
                    enemys.add(new Enemy2(Global.random(400, 1000), Global.random(400, 350), true));
                }
                for (int i = 0; i < Global.random(3, 5); i++) {
                    enemys.add(new Enemy3(Global.random(400, 1100), Global.random(400, 450), true));
                }
            }
            if (count == 2) {
                for (int i = 0; i < 8; i++) {
                    enemys.add(new Enemy1(Global.random(400, 1000), Global.random(400, 350), true));
                }
                for (int i = 0; i < 4; i++) {
                    enemys.add(new Enemy2(Global.random(400, 1000), Global.random(400, 350), true));
                }
                for (int i = 0; i < Global.random(5, 7); i++) {
                    enemys.add(new Enemy3(Global.random(400, 1100), Global.random(400, 450), true));
                }
                for (int i = 0; i < Global.random(3, 5); i++) {
                    enemys.add(new Enemy4(Global.random(400, 1100), Global.random(400, 450), true));
                }
            }
            if(count ==3){
                for (int i = 0; i < 12; i++) {
                    enemys.add(new Enemy1(Global.random(400, 1000), Global.random(400, 350), true));
                }
                for (int i = 0; i < 10; i++) {
                    enemys.add(new Enemy2(Global.random(400, 1000), Global.random(400, 350), true));
                }
                for (int i = 0; i < Global.random(7, 9); i++) {
                    enemys.add(new Enemy3(Global.random(400, 1100), Global.random(400, 450), true));
                }
                for (int i = 0; i < Global.random(6, 8); i++) {
                    enemys.add(new Enemy4(Global.random(400, 1100), Global.random(400, 450), true));
                }
            }
            step++;
        }
        //第三關(魔王關)
        //第二關
        if (Global.getLevel()==3 && step==2) {//敵軍可 以移動時
            if(count==1){
                //做敵軍第一波
                for (int i = 0; i < 6; i++) {
                    enemys.add(new Enemy2(Global.random(400, 1000), Global.random(250, 350), true));
                }
                for (int i = 0; i < 3; i++) {
                    enemys.add(new Enemy4(Global.random(400, 1000), Global.random(250, 350), true));
                }
                for (int i = 0; i < Global.random(1, 3); i++) {
                    enemys.add(new Enemy3(Global.random(400, 1100), Global.random(300, 450), true));
                }
            }
            if (count == 2) {
                for (int i = 0; i < 3; i++) {
                    enemys.add(new Enemy1(Global.random(400, 1000), Global.random(250, 350), true));
                }
                for (int i = 0; i < 5; i++) {
                    enemys.add(new Enemy2(Global.random(400, 1000), Global.random(250, 350), true));
                }
                for (int i = 0; i < Global.random(3, 5); i++) {
                    enemys.add(new Enemy3(Global.random(400, 1100), Global.random(300, 450), true));
                }
                for (int i = 0; i < Global.random(3, 5); i++) {
                    enemys.add(new Enemy4(Global.random(400, 1100), Global.random(300, 450), true));
                }
            }
            if(count ==3){
                for (int i = 0; i < 6; i++) {
                    enemys.add(new Enemy2(Global.random(400, 1000), Global.random(250, 350), true));
                }
                for (int i = 0; i < Global.random(2, 3); i++) {
                    enemys.add(new Enemy3(Global.random(400, 1100), Global.random(300, 450), true));
                }
                for (int i = 0; i < Global.random(1, 2); i++) {
                    enemys.add(new Boss(Global.random(400, 1100), Global.random(300, 450), true));
                }
            }
            step++;
        }

        //戰鬥中
        if (step == 3) {
            if(enemys.size()==0){
                step=1;
                delayCount.play();
                changePicDelay.loop();//倒數計時每1秒觸發一次換圖片
                isFlagUsable = true;
            }
        }

        if (count >= 3 && enemys.size() <= 0 &&!gameComplete) { //挑戰成功條件
            Global.getActorButtons().get(2).setUnLocked(true); //解鎖雷射車
            Global.getActorButtons().get(3).setUnLocked(true); //解鎖火箭車
            Player.getInstance().offsetHonor(1000); //榮譽值+1000
            Player.getInstance().offsetMoney(1000); //金錢+1000
            Global.addLevel();
            gameComplete=true;
        }

        if (alliance.size() <= 0) { //挑戰失敗
            delay.play();
            if(delay.count()) {
                Player.getInstance().offsetMoney(1000); //錢值+1000
                Player.getInstance().offsetHonor(250); //榮譽值+50
                SceneController.getInstance().changeScene(UserScene.getInstance());
            }
        }
    }
}

