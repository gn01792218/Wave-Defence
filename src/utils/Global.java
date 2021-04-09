package utils;

public class Global {

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
        
        public static Direction getDirection(int value){
            for(Direction d : Direction.values()){
                if(d.getValue() == value){
                    return d;
                }
            }
            return null;
        }
    }

    //除法公式取到小數點之後，暫無使用後續可刪除
    public static float division(int a,int b){
        int result =a/b;
        int remainder = a%b;
        int decimalPoint = 5;
        for(int i=0;i<decimalPoint;i++){
            result = result*10 + remainder*10/b;
            remainder = remainder*10%b;
        }
        return (float) (result / Math.pow(10,decimalPoint));
    }

    //三角函數求X與Y的向量
    public static class Trigonometric{
        private float dX;
        private float dY;
        private boolean xIsPositive;
        private boolean yIsPositive;
        private float k;

        private float xVector;
        private float yVector;
        private int degree;

        public Trigonometric(int x,int y,int targetX,int targetY){
            dX = Math.abs(targetX - x);//x座標差值 對邊
            dY = Math.abs(targetY - y);//y座標差值 臨邊
            k = (float) Math.sqrt(dX*dX+dY*dY); //斜邊

            xVector = dX/k;  //x向量
            yVector = dY/k; //y向量
            degree = (int)(Math.toDegrees(angleBetweenTwoPointsWithFixedPoint(targetX,targetY,x,y+1,x,y)));

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
            if(xIsPositive){
                return xVector;
            }
            return -xVector;
        }

        public float getyVector() {
            if(yIsPositive){
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
    // 單位大小
    public static final int UNIT_X = 32;
    public static final int UNIT_Y = 32;
    //飛彈飛行速度
    public static final int BULLET_SPEED = 4;
    //場地邊際
    public static final int BOUNDARY_X1=360;
    public static final int BOUNDARY_X2=1060;
    public static final int BOUNDARY_Y1=180;
    public static final int BOUNDARY_Y2=700;
    // 視窗大小
    public static final int WINDOW_WIDTH = 1600;
    public static final int WINDOW_HEIGHT = 1200;
    public static final int SCREEN_X = WINDOW_WIDTH - 8 - 8;
    public static final int SCREEN_Y = WINDOW_HEIGHT - 31 - 8;
    // 資料刷新時間
    public static final int UPDATE_TIMES_PER_SEC = 60;// 每秒更新60次遊戲邏輯
    public static final int NANOSECOND_PER_UPDATE = 1000000000 / UPDATE_TIMES_PER_SEC;// 每一次要花費的奈秒數
    // 畫面更新時間
    public static final int FRAME_LIMIT = 60;
    public static final int LIMIT_DELTA_TIME = 1000000000 / FRAME_LIMIT;

    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }
    public static boolean random(int rate) {
        return random(1, 100) <= rate;
    }

}
