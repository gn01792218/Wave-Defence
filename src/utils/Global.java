package utils;

import controllers.ImageController;
import gameobj.*;
import menu.*;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Global {
    private static ArrayList<ActorButton> actorButtons;//角色按鈕，可以儲存玩家擁有的角色種類和數量；在選單中畫出來，在遊戲場景不畫出來，只是存放屬性。-->多個Scene會需要，故放此
    private static ArrayList<SkillButton> skillButtons=null;// 技能按鈕
    private static ArrayList<Actor> allianceActors;//我軍角色陣列，提供選單的角色資訊用
    private static ArrayList<Actor> enemyActors;//敵軍角色陣列，提供選單角色資訊用
    private static DecimalFormat frmt; //用來格式化浮點數輸出的工具

    public enum SkillName {
        ATTACKUP,
        DEFUP,
        MOVESPEEDUP,
        HPUP,
    }

    public enum ActorType {
        TANK1,
        TANK2,
        ENEMY1,
    }

    public static ArrayList<Actor> getAllianceActors() {
        allianceActors = new ArrayList<>(); //每次需要的時候new新的以更新資訊
        allianceActors.add(new Tank1(0, 0, false));
        allianceActors.add(new Tank2(0, 0, false));
        return allianceActors;
    }

    //做取得技能按鈕陣列的實體。
    public static ArrayList<SkillButton> getSkillButtons() {
        final int skillButtonUnit = 64;
        final int SkillButtonDis = 128;
        final int skillButtonHeight = 700;
        if (skillButtons == null) {
            System.out.println("888");
            //技能按鈕
            skillButtons = new ArrayList<>();
            SkillButton attackSkillButton = new AttackUp(100, skillButtonHeight, new Style.StyleRect(skillButtonUnit, skillButtonUnit, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/SB-attack.gif"))), Global.SkillName.ATTACKUP, 250);
            SkillButton defSkillButton = new DefUp(100 + SkillButtonDis, skillButtonHeight, new Style.StyleRect(skillButtonUnit, skillButtonUnit, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/SB-def.gif"))), Global.SkillName.DEFUP, 250);
            SkillButton speedButton = new SpeedUp(100 + SkillButtonDis * 2, skillButtonHeight, new Style.StyleRect(skillButtonUnit, skillButtonUnit, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/SB-speed.gif"))), Global.SkillName.MOVESPEEDUP, 250);
            SkillButton hpButton = new HpUp(100 + SkillButtonDis * 3, skillButtonHeight, new Style.StyleRect(skillButtonUnit, skillButtonUnit, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/S-hp.gif"))), Global.SkillName.HPUP, 250);
            skillButtons.add(attackSkillButton);
            skillButtons.add(defSkillButton);
            skillButtons.add(speedButton);
            skillButtons.add(hpButton);
        }
        return skillButtons;
    }

    public static ArrayList<Actor> getEnemyActors() {
        enemyActors = new ArrayList<>(); //每次需要的時候new新的以更新資訊
        enemyActors.add(new Enemy1(0, 0, false));
        return allianceActors;
    }

    public static ArrayList<ActorButton> getActorButtons() {
        if (actorButtons == null) { //假如沒有 才做角色按鈕
            actorButtons = new ArrayList<>();//角色參數: 座標 Style Type Cost
            ActorButton tank1 = new ActorButton(600, 100, new Style.StyleRect(500, 500, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/AB-Tank1.png"))),
                    Global.ActorType.TANK1, 250);
            ActorButton tank2 = new ActorButton(200, 100, new Style.StyleRect(500, 500, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/AB-Tank2.png"))),
                    Global.ActorType.TANK2, 350);
            actorButtons.add(tank1);
            actorButtons.add(tank2);
        }
        return actorButtons;
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
            degree = (int) (Math.toDegrees(angleBetweenTwoPointsWithFixedPoint(targetX, targetY, x, y + 1, x, y)));
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

    public static final boolean IS_DEBUG = true;

    public static void log(String str) {
        if (IS_DEBUG) {
            System.out.println(str);
        }
    }

    //飛彈飛行速度
    public static final int BULLET_SPEED = 4;
    //場地邊際
    public static final int BOUNDARY_X1 = 390;
    public static final int BOUNDARY_X2 = 1150;
    public static final int BOUNDARY_Y1 = 180;
    public static final int BOUNDARY_Y2 = 750;
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
