package utils;

import controllers.ImageController;
import gameobj.*;
import menu.*;
import scene.IntroPopupWindow;

import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Global {

    private static IntroPopupWindow introPopupWindow;
    private static ArrayList<ActorButton> actorButtons;//角色按鈕，可以儲存玩家擁有的角色種類和數量；在選單中畫出來，在遊戲場景不畫出來，只是存放屬性。-->多個Scene會需要，故放此
    private static ArrayList<ActorButton> enemyButtons;//
    private static ArrayList<SkillButton> skillButtons=null;// 技能按鈕
    private static ArrayList<Actor> allianceActors;//我軍角色陣列，提供選單的角色資訊用
    private static ArrayList<Actor> enemyActors;//敵軍角色陣列，提供選單角色資訊用
    private static DecimalFormat frmt; //用來格式化浮點數輸出的工具
    private static int level=1;
    public static int getLevel() {
        return level;
    }
    public static void addLevel(){
        level++;
    }
    public static void rsetLevel() {
        Global.level = 1;
    }
    public static RankList rankList = new RankList();
    public enum SkillName {
        ATTACKUP,
        DEFUP,
        MOVESPEEDUP,
        HPUP,
        REINFORCEMENTS,
        ELECTWAVE,
        ATKSPEEDUP,
    }
    public enum ActorType {
        TANK1,
        TANK2,
        LASERCAR,
        ROCKET,
        ENEMY1,
        ENEMY2,
        ENEMY3,
        ENEMY4,
        BOSS,

    }
    public static IntroPopupWindow getIntroPopupWindow(){
        if(introPopupWindow==null) {
            introPopupWindow = new IntroPopupWindow(100,0,1500,1000);
        }
        return introPopupWindow;
    }
    public static ArrayList<Actor> getAllianceActors() {
        allianceActors = new ArrayList<>(); //每次需要的時候new新的以更新資訊
        allianceActors.add(new Tank1(0, 0, false));
        allianceActors.add(new Tank2(0, 0, false));
        allianceActors.add(new LaserCar(0,0,false));
        allianceActors.add(new Rocket(0,0,false));
        return allianceActors;
    }
    //做取得技能按鈕陣列的實體。
    public static ArrayList<SkillButton> getSkillButtons() {  //在此調整按鈕顯示位置
         int skillButtonUnit = 64;
         int SkillButtonDis = 320;
         int skillButtonHeight = 355;
         int skillButtonLeft=420;
        if(skillButtons==null) {
            //技能按鈕
            skillButtons = new ArrayList<>();
            SkillButton attackSkillButton = new AttackUp(skillButtonLeft, skillButtonHeight, new Style.StyleRect(skillButtonUnit, skillButtonUnit, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/SB-attack.gif"))), Global.SkillName.ATTACKUP, 300);
            SkillButton defSkillButton = new DefUp(skillButtonLeft + SkillButtonDis, skillButtonHeight, new Style.StyleRect(skillButtonUnit, skillButtonUnit, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/SB-def.gif"))), Global.SkillName.DEFUP, 250);
            SkillButton speedButton = new SpeedUp(skillButtonLeft + SkillButtonDis * 2, skillButtonHeight, new Style.StyleRect(skillButtonUnit, skillButtonUnit, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/SB-speed.gif"))), Global.SkillName.MOVESPEEDUP, 200);
            SkillButton hpButton = new HpUp(skillButtonLeft + SkillButtonDis * 3, skillButtonHeight, new Style.StyleRect(skillButtonUnit, skillButtonUnit, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/SB-hp.gif"))), Global.SkillName.HPUP, 300);
            SkillButton reinforcement = new Reinforcements(skillButtonLeft, skillButtonHeight + SkillButtonDis, new Style.StyleRect(skillButtonUnit, skillButtonUnit, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/SB-reinforcement.gif"))), Global.SkillName.REINFORCEMENTS, 350);
            SkillButton electWave = new ElectWave(skillButtonLeft + SkillButtonDis, skillButtonHeight + SkillButtonDis, new Style.StyleRect(skillButtonUnit, skillButtonUnit, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/SB-electWave.gif"))), Global.SkillName.ELECTWAVE, 300);
            SkillButton atkSpeedUp = new AtkSpeedUp(skillButtonLeft + SkillButtonDis * 2, skillButtonHeight + SkillButtonDis, new Style.StyleRect(skillButtonUnit, skillButtonUnit, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/SB-atkSpeedUp.gif"))), Global.SkillName.ATKSPEEDUP, 350);
            skillButtons.add(attackSkillButton);
            skillButtons.add(defSkillButton);
            skillButtons.add(speedButton);
            skillButtons.add(hpButton);
            skillButtons.add(reinforcement);
            skillButtons.add(electWave);
            skillButtons.add(atkSpeedUp);
        }

        return skillButtons;
    }

    public static ArrayList<Actor> getEnemyActors() {
        enemyActors = new ArrayList<>(); //每次需要的時候new新的以更新資訊
        enemyActors.add(new Enemy1(0, 0, true));
        enemyActors.add(new Enemy2(0,0,true));
        enemyActors.add(new Enemy3(0,0,true));
        enemyActors.add(new Enemy4(0,0,true));
        enemyActors.add(new Boss(0,0,true));
        return enemyActors;
    }

    public static ArrayList<ActorButton> getActorButtons() {
        if (actorButtons == null) { //假如沒有 才做角色按鈕
            actorButtons = new ArrayList<>();//角色參數: 座標 Style Type Cost
            ActorButton tank1 = new ActorButton(830, 100, new Style.StyleRect(500, 500, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/AB-Tank1.png"))),
                    Global.ActorType.TANK1, 250,false,"TSZ-1號坦克");
            ActorButton tank2 = new ActorButton(1330, 100, new Style.StyleRect(500, 500, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/AB-Tank2.png"))),
                    Global.ActorType.TANK2, 300,false,"AG-2號坦克");
            ActorButton laserCar=new ActorButton(1830,100,new Style.StyleRect(500,500,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/AB-LaserCar.png"))),
                    Global.ActorType.LASERCAR,350,false,"ES雷射砲車");
            ActorButton rocket=new ActorButton(2330,100,new Style.StyleRect(500,500,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/AB-Rocket.png"))),
                    ActorType.ROCKET,1000,false,"VI火箭車");
            tank1.setUnLocked(true); //坦1
            tank2.setUnLocked(true); //坦2 都是被解鎖的
//            laserCar.setUnLocked(true); //測試用
//            rocket.setUnLocked(true);
            laserCar.setLockImage(ImageController.getInstance().tryGet("/AALaserCar_Lock.png"));
            rocket.setLockImage(ImageController.getInstance().tryGet("/AARocket_Lock.png"));
            actorButtons.add(tank1);
            actorButtons.add(tank2);
            actorButtons.add(laserCar);
            actorButtons.add(rocket);
        }
        return actorButtons;
    }
    public static ArrayList<ActorButton> getEnemyButtons(){
        if (enemyButtons == null) { //假如沒有 才做角色按鈕
            enemyButtons= new ArrayList<>();//角色參數: 座標 Style Type Cost
            ActorButton enemy1 = new ActorButton(650, 100, new Style.StyleRect(500, 500, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/EB-Enemy1.png"))),
                    ActorType.ENEMY1, 0,true,"戰鬥機甲兵");
            ActorButton enemy2 = new ActorButton(1150, 100, new Style.StyleRect(500, 500, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/EB-Enemy2.png"))),
                    ActorType.ENEMY2, 0,true,"特工機甲兵");
            ActorButton enemy3=new ActorButton(1650,100,new Style.StyleRect(500,500,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/EB-Enemy3.png"))),
                    ActorType.ENEMY3,0,true,"巨砲甲蟲");
            ActorButton enemy4=new ActorButton(2150,100,new Style.StyleRect(500,500,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/EB-Enemy4.png"))),
                    ActorType.ENEMY4,0,true,"三管砲車");
            ActorButton boss=new ActorButton(2650,100,new Style.StyleRect(500,500,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/EB-Boss.png"))),
                    ActorType.BOSS,0,true,"衝鋒機甲龍");
            enemy1.setUnLocked(true);
            enemy4.setUnLocked(true);
            enemy3.setUnLocked(true);
            enemy4.setUnLocked(true);
            boss.setUnLocked(false);
//            enemy3.setLockImage(ImageController.getInstance().tryGet("/AALaserCar_Lock.png"));
//            enemy2.setLockImage(ImageController.getInstance().tryGet("/AARocket_Lock.png"));
//            boss.setLockImage(ImageController.getInstance().tryGet("/AARocket_Lock.png"));
            for(int i=0;i<enemyButtons.size();i++){
                enemyButtons.get(i).setCostLabel(null);
            }
            enemyButtons.add(enemy1);
            enemyButtons.add(enemy2);
            enemyButtons.add(enemy3);
            enemyButtons.add(enemy4);
            enemyButtons.add(boss);
        }
        return enemyButtons;
    }

    public static DecimalFormat getFrmt() {  //單例化
        if (frmt == null) {
            frmt = new DecimalFormat();
        }
        return frmt;
    }

    public enum Direction {
        UP(3),
        DOWN(0),
        LEFT(1),
        RIGHT(2);
        private int value;

        Direction(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Direction getDirection(int value) {
            for (Direction d : Direction.values()) {
                if (d.getValue() == value) {
                    return d;
                }
            }
            return null;
        }
    }

    //除法公式取到小數點之後，暫無使用後續可刪除
    public static float division(int a, int b) {
        int result = a / b;
        int remainder = a % b;
        int decimalPoint = 5;
        for (int i = 0; i < decimalPoint; i++) {
            result = result * 10 + remainder * 10 / b;
            remainder = remainder * 10 % b;
        }
        return (float) (result / Math.pow(10, decimalPoint));
    }

    //三角函數求X與Y的向量
    public static class Trigonometric {
        private float dX;
        private float dY;
        private boolean xIsPositive;
        private boolean yIsPositive;
        private float k;
        private float xVector;
        private float yVector;
        private int degree;

        public Trigonometric(float x, float y, float targetX, float targetY) {
            dX = Math.abs(targetX - x);//x座標差值 對邊
            dY = Math.abs(targetY - y);//y座標差值 臨邊
            k = (float) Math.sqrt(dX * dX + dY * dY); //斜邊
            xVector = dX / k;  //x向量
            yVector = dY / k; //y向量
            degree = (int) (Math.toDegrees(angleBetweenTwoPointsWithFixedPoint(targetX, targetY, x, y +1, x, y)));
            xIsPositive = targetX - x > 0;
            yIsPositive = targetY - y > 0;
        }

        //求角度用的tan反三角函數
        public double angleBetweenTwoPointsWithFixedPoint(double point1X, double point1Y,
                                                          double point2X, double point2Y,
                                                          double fixedX, double fixedY) {
            double angle1 = Math.atan2(point1Y - fixedY, point1X - fixedX);
            double angle2 = Math.atan2(point2Y - fixedY, point2X - fixedX);

            return angle1 - angle2;
        }

        public float getxVector() {
            if (xIsPositive) {
                return xVector;
            }
            return -xVector;
        }

        public float getyVector() {
            if (yIsPositive) {
                return yVector;
            }
            return -yVector;
        }

        public int getDegree() {
            return degree;
        }
    }



    public static final boolean IS_DEBUG = false;

    public static void log(String str) {
        if (IS_DEBUG) {
            System.out.println(str);
        }
    }

    //飛彈飛行速度
    public static final int BULLET_SPEED = 4;
    //場地邊際
    public static final int BOUNDARY_X1=390;
    public static final int BOUNDARY_X2=1200;
    public static final int BOUNDARY_Y1=180;
    public static final int BOUNDARY_Y2=750;
    public static final boolean isTouchY1(float x,float y){
        Boolean result=false;
        result = (y<=200);
//        if(Global.getLevel()==1){
//            result = (y<=200);
//        }else if(Global.getLevel()==2){
//            result= (y<=308);
//        }
        return result;
    }
    public static final boolean isTouchY2(float x,float y){
        Boolean result=false;
        result = (y>=790);
//        if(Global.getLevel()==1){
//            result = (y>=790);
//        }else if(Global.getLevel()==2){
//            result= (103*y>=x+73474);
//        }
        return result;
    }
    public static final boolean isTouchX1(float x,float y){
        Boolean result=false;
        result= (19*y <=52530-112*x);
//        if(Global.getLevel()==1){
//            result= (19*y <=52530-112*x);
//        }else if(Global.getLevel()==2){
//            result= (73*y >=204*x-42376);
//        }
        return result;
    }
    public static final boolean isTouchX2(float x,float y){
        Boolean result=false;
        result= (21*y <= 112*x-124530);
//        if(Global.getLevel()==1){
//            result= (21*y <= 112*x-124530);
//        }else if(Global.getLevel()==2){
//            result= (163*y>=597120-446*x);
//        }
        return result;
    }
    // 視窗大小
    public static final int WINDOW_WIDTH = 1600;
    public static final int WINDOW_HEIGHT = 1200;
    public static final int SCREEN_X = WINDOW_WIDTH - 8 - 8;
    public static final int SCREEN_Y = WINDOW_HEIGHT - 31 - 8;
    // 資料刷新時間
    public static final int UPDATE_TIMES_PER_SEC = 30;// 每秒更新60次遊戲邏輯
    public static final int NANOSECOND_PER_UPDATE = 1000000000 / UPDATE_TIMES_PER_SEC;// 每一次要花費的奈秒數
    // 畫面更新時間
    public static final int FRAME_LIMIT = 30;
    public static final int LIMIT_DELTA_TIME = 1000000000 / FRAME_LIMIT;

    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }
    public static boolean random(int rate) {
        return random(1, 100) <= rate;
    }

}
