package scene;

import controllers.AudioResourceController;
import controllers.ImageController;
import controllers.SceneController;
import gameobj.*;
import menu.*;
import utils.CommandSolver;
import utils.Delay;
import utils.Global;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ChallengeScene extends Scene{
    private BufferedImage image; //背景圖
    private BufferedImage image1_1;
    private BufferedImage image2; //失敗的圖片
    private BufferedImage image3;//倒數10秒圖片
    private BufferedImage image4;// 挑戰成功的圖片
    private ArrayList<Actor> alliance; //角色陣列
    private ArrayList<Actor> castles; //城堡陣列
    private ArrayList<Actor> enemys; //敵軍
    private ArrayList<SkillButton> skill;//技能陣列
    private boolean isFlagUsable;
    private int step;
    private Delay gameBegin; //過場秒數
    private Delay gameOver; //過場秒數
    private Delay enemyLoop;
    private Delay rewardLoop;

    private Actor allianceControl;//受旗子控制的我軍
    private int count = 0;//回合數
    private float mouseX;
    private float mouseY;
    private int money;
    private int honor;

    @Override
    public void sceneBegin() {
        image = ImageController.getInstance().tryGet("/GameScene1.png"); //場景圖
        image1_1=ImageController.getInstance().tryGet("/GameScene1-1.png");
        image2 = ImageController.getInstance().tryGet("/fail2.png");
        isFlagUsable = true;
        gameBegin=new Delay(300);
        gameOver=new Delay(180);
        enemyLoop=new Delay(1200);
        rewardLoop=new Delay(600);
        gameBegin.play();
        count=1;
        money=600;
        honor=200;
//        作技能
        skill=new ArrayList<>();
        ArrayList<SkillButton> temp=Global.getSkillButtons(); //從世界技能紐中下訂單
        for(int i=0;i<temp.size();i++) {
            skill.add(new AttackUp(500+skill.size()*128,100,temp.get(i).getStyleNormal(),temp.get(i).getSkillName(),temp.get(i).getCost())); //設置在場中的位置
            skill.add(new HpUp(500+skill.size()*128,100,temp.get(i).getStyleNormal(),temp.get(i).getSkillName(),temp.get(i).getCost())); //設置在場中的位置
            skill.add(new DefUp(500+skill.size()*128,100,temp.get(i).getStyleNormal(),temp.get(i).getSkillName(),temp.get(i).getCost())); //設置在場中的位置
            skill.add(new SpeedUp(500+skill.size()*128,100,temp.get(i).getStyleNormal(),temp.get(i).getSkillName(),temp.get(i).getCost())); //設置在場中的位置
            skill.add(new Reinforcements(500+skill.size()*128,100,temp.get(i).getStyleNormal(),temp.get(i).getSkillName(),temp.get(i).getCost())); //設置在場中的位置
            skill.add(new ElectWave(500+ skill.size()*128,100,temp.get(i).getStyleNormal(),temp.get(i).getSkillName(),temp.get(i).getCost()));
            skill.add(new AtkSpeedUp(500+ skill.size()*128,100,temp.get(i).getStyleNormal(),temp.get(i).getSkillName(),temp.get(i).getCost()));
        }


        //做軍隊
        alliance = new ArrayList<>();
//        alliance.add(new Tank1(850,600,false));
//        alliance.add(new Tank1(750,600,false));
//        alliance.add(new Tank1(950,700,false));
//        alliance.add(new Tank1(650,700,false));
        castles = new ArrayList<>();
        castles.add(new Castle(400,730));
        castles.get(0).painter().setLeft(100);
        castles.add(new Castle(1150,730));
        enemys = new ArrayList<>();

    }

    @Override
    public void sceneEnd() {
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {

            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                switch (commandCode){
                    case 11:
                        if(money>250){
                            Actor temp = new Tank1(800,1100,false);
                            alliance.add(temp);
                            temp.setStrategyXY(mouseX,mouseY);
                            money -=250;
                        }
                        break;
                    case 12:
                        if(money>280){
                            Actor temp = new Tank2(800,1100,false);
                            alliance.add(temp);
                            temp.setStrategyXY(mouseX,mouseY);
                            money -=280;
                        }

                        break;
                    case 13:
                        if(money>250){
                            Actor temp = new LaserCar(800,1100,false);
                            alliance.add(temp);
                            temp.setStrategyXY(mouseX,mouseY);
                            money -=300;
                        }
                        break;
                    case 14:
                        if(money>500){
                            Actor temp = new Rocket(800,1100,false);
                            alliance.add(temp);
                            temp.setStrategyXY(mouseX,mouseY);
                            money-=500;
                        }
                        break;
                }
            }

            @Override
            public void keyTyped(char c, long trigTime) {

            }
        };
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
                                        System.out.println("技能已經啟動一次，不會再有下一次~~");
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
                            mouseX = e.getX();
                            mouseY = e.getY();
                    }
                }
            }
        };
    }

    @Override
    public void paint(Graphics g) {

        g.drawImage(image, 0, -150, null);



        for (int i = 0; i < alliance.size(); i++) {
            alliance.get(i).paint(g); //畫我軍
        }

        for (int i = 0; i < enemys.size(); i++) {
            enemys.get(i).paint(g); //畫敵軍
        }
        if(skill.size()>0) {
            for (int i = 0; i < skill.size(); i++) {
                if(!skill.get(i).isUsed()) {
                    skill.get(i).paint(g); //畫技能
                }
            }
        }
        if (isFlagUsable && allianceControl!=null) {
            allianceControl.getFlag().paint(g); //旗子可以使用的時候才畫出來
        }

        g.drawImage(image1_1,0,-150,null);
        for(int i = 0; i< castles.size(); i++){
            castles.get(i).paint(g);
        }
        if(castles.size()<=0){
            g.drawImage(image2, 350, 250, null);
        }
    }
    //當偵測到被點到，開啟可以移動，時才移動，並一直移動到目標點，然後
    @Override
    public void update() {

//        if(skill.size()>0) {
//            for (int i = 0; i < skill.size(); i++) {
//                if (skill.get(i).isUsed()) { //沒有被施放過
//                    if (skill.get(i).getBuffTime().count()) {
//                        if(skill.get(i).getSkillName()==Global.SkillName.ELECTWAVE){//電磁波的~!
//                            skill.get(i).skillReset(enemys);
//                        }else{ skill.get(i).skillReset(alliance);}//時間到全軍恢復原廠設置~!
//                        skill.remove(i); //移除技能~
//                        System.out.println("移除技能");
//                    }
//                }
//            }
//        }
        //我軍的update
        for (int i = 0; i < alliance.size(); i++) {  //我軍自己移動
            if(alliance.get(i).collider().centerY()>Global.BOUNDARY_Y2){
                alliance.get(i).moveToField();
            }else {
                alliance.get(i).standAttack(enemys, alliance);
                alliance.get(i).bulletsUpdate(enemys); //發射子彈
            }

            if (!alliance.get(i).isAlive()) { //沒有活著的時候移除
                alliance.remove(i);
                break;
            }
        }
        for(int i = 0; i< castles.size(); i++){
            //攻擊條件
            castles.get(i).autoAttack(enemys,alliance);
            castles.get(i).bulletsUpdate(enemys); //發射子彈

            if (!castles.get(i).isAlive()) { //沒有活著的時候移除
                castles.remove(i);
                break;
            }
        }
        //敵軍update
        for (int i = 0; i < enemys.size(); i++) {
            if(enemys.get(i).collider().centerY()<100){
                enemys.get(i).moveToField();
            }else {
                enemys.get(i).straightAttack(alliance,castles);
                enemys.get(i).bulletsUpdate(alliance);
                enemys.get(i).bulletsUpdate(castles);
            }

            if (!enemys.get(i).isAlive()) {
                enemys.remove(i);
                money +=100;
                break;
            }
        }


        //REWARD
        if(rewardLoop.count()){
            money+=600;
            honor+=150;
        }

        //第一回合
        if(gameBegin.count()){
            AudioResourceController.getInstance().play("/cinematic-dramatic-brass-hit_G_major");
            for(int i=0;i<(count+1)*2;i++){
                enemys.add(new Enemy1(Global.random(650, 950), -Global.random(0, 50), true));
            }
            for(int i=0;i<enemys.size();i++){
                enemys.get(i).setStrategyXY(Global.random(Global.BOUNDARY_X1,Global.BOUNDARY_X2),250);
            }
            enemyLoop.loop();
            rewardLoop.loop();
        }

        //回合++
        if(enemyLoop.count()){
            AudioResourceController.getInstance().play("/cinematic-dramatic-brass-hit_G_major");
            count++;
            for(int i=0;i<(count+1)*2;i++){
                enemys.add(new Enemy1(Global.random(650, 950), -Global.random(0,150), true));
            }
            if(count>2){
                for(int i=0;i<(count-1)*2;i++){
                    enemys.add(new Enemy2(Global.random(650, 950), -Global.random(50,200), true));
                }
            }
            if(count>4){
                for(int i=0;i<(count-2)*2;i++){
                    enemys.add(new Enemy3(Global.random(650, 950), -Global.random(150,300), true));
                }
            }
            if(count>5){
                for(int i=0;i<count-5;i++){
                    enemys.add(new Enemy3(Global.random(400, 1000), Global.random(-100,50), true));
                }
            }
            //隨機給予敵人攻擊位置
            for(int i=0;i<enemys.size();i++){
                enemys.get(i).setStrategyXY(Global.random(Global.BOUNDARY_X1,Global.BOUNDARY_X2),200);
            }
        }

//
//        if (step == 3 && enemys.size() <= 0) { //挑戰成功條件
//            delay.play();
//            if(delay.count()) {
//                Player.getInstance().offsetHonor(+1000); //榮譽值+1000
//                Player.getInstance().offsetMoney(1000); //金錢+1000
//                SceneController.getInstance().changeScene(new UserScene());
//            }
        if (castles.size() <= 0) { //挑戰失敗
            gameOver.play();
            if(gameOver.count()) {
                SceneController.getInstance().changeScene(new UserScene());
            }
        }

    }
}
