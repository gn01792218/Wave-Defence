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

    public static final boolean IS_DEBUG = true;

    public static void log(String str) {
        if (IS_DEBUG) {
            System.out.println(str);
        }
    }
    // 單位大小
    public static final int UNIT_X = 32;
    public static final int UNIT_Y = 32;
    // 視窗大小
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
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
