package camera;


import gameobj.GameObject;
import utils.CommandSolver.KeyListener;
import utils.Global.Direction;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Camera extends GameObject implements KeyListener{
    
    //地圖資訊由 MapInformation 類別直接呼叫，可以進行將相關資訊設定到Global 其實只需要地圖的寬跟長即可
    //相機設定
    private int cameraMoveSpeed; //鏡頭移動速度
    private double chaseDivisorX; // 追焦時X軸要除的值，越小追越快
    private double chaseDivisorY; // 追焦時Y軸要除的值，越小追越快
    private GameObject obj;  //要跟焦的對象，如果null 就代表不用跟畫面可自由移動
    protected AffineTransform tmpCanvas;// 暫存畫布移動前的位置
    private boolean lockLeft;
    private boolean lockRight;
    private boolean lockUp;
    private boolean lockDown;
    private int cameraWindowX; // 此顆鏡頭實際在畫面的左上角X座標
    private int cameraWindowY; // 此顆鏡頭實際在畫面的左上角Y座標
    
    public static class Builder{
        private Camera tmp;
        
        public Builder(int width, int height){
            tmp = new Camera(width, height);
        }
        public Builder setChaseObj(GameObject obj){
            tmp.setObj(obj);
            return this;
        }
        public Builder setChaseObj(GameObject obj, double chaseDivisorX, double chaseDivisorY){
            tmp.setObj(obj);
            tmp.setChaseDivisorX(chaseDivisorX);
            tmp.setChaseDivisorY(chaseDivisorY);
            return this;
        }
        public Builder setCameraMoveSpeed(int num){
            tmp.setCameraMoveSpeed(num);
            return this;
        }
        public Builder setCameraStartLocation(int left, int top){
            tmp.translate(left - tmp.painter().left(), top - tmp.painter().top());
            return this;
        }
        public Builder setCameraWindowLocation(int left, int top){
            tmp.setCameraWindowX(left);
            tmp.setCameraWindowY(top);
            return this;
        }
        public Builder setCameraLockDirection(boolean left, boolean up, boolean right, boolean down){
            tmp.lockLeft(left);
            tmp.lockUp(up);
            tmp.lockRight(right);
            tmp.lockDown(down);
            return this;
        }
        public Camera gen(){
            return new Camera(tmp);
        }
    }
    
    private Camera(int width, int height){
        super(0, 0, width, height, 0, 0, width, height);
        cameraWindowX = 0;
        cameraWindowY = 0;
        cameraMoveSpeed = 10;
        chaseDivisorX = 20;
        chaseDivisorY = 20;
    }
    protected Camera(Camera tmp){
        super(tmp.collider().centerX(), tmp.collider().centerY(), tmp.collider().width(), tmp.collider().height(),
                tmp.painter().centerX(), tmp.painter().centerY(), tmp.painter().width(), tmp.painter().height());
        setObj(tmp.obj());
        cameraMoveSpeed = tmp.cameraMoveSpeed;
        chaseDivisorX = tmp.chaseDivisorX;
        chaseDivisorY = tmp.chaseDivisorY;
        cameraWindowX = tmp.cameraWindowX;
        cameraWindowY = tmp.cameraWindowY;
        lockLeft = tmp.lockLeft;
        lockRight = tmp.lockRight;
        lockUp = tmp.lockUp;
        lockDown = tmp.lockDown;
    }

    private void resetX(int left, int right){
        collider().setLeft(left);
        painter().setLeft(left);
        collider().setRight(right);
        painter().setRight(right);
    }
    private void resetY(int top, int bottom){
        collider().setTop(top);
        painter().setTop(top);
        collider().setBottom(bottom);
        painter().setBottom(bottom);
    }
    public void setObj(GameObject obj){
        this.obj = obj;
        if (this.obj != null){
            int left = obj.painter().centerX() - painter().width() / 2;
            int right = obj.painter().centerX() + painter().width() / 2;
            int top = obj.painter().centerY() - painter().height() / 2;
            int bottom = obj.painter().centerY() + painter().height() / 2;
            if (touchLeft()){
                left = MapInformation.mapInfo().left();
                right = left + painter().width();
            }
            if (touchRight()){
                right = MapInformation.mapInfo().right();
                left = right - painter().width();
            }
            if (touchTop()){
                top = MapInformation.mapInfo().top();
                bottom = top + painter().height();
            }
            if (touchBottom()){
                bottom = MapInformation.mapInfo().bottom();
                top = bottom - painter().height();
            }
            resetX(left, right);
            resetY(top, bottom);
        }
    }

    private void chaseMove() { //鏡頭追蹤加速度 數字越大追越慢
        double targeX = (obj.painter().centerX() - painter().centerX()) / chaseDivisorX;
        double targeY = (obj.painter().centerY() - painter().centerY()) / chaseDivisorY;
        if (targeX > 0 && !touchRight() && !lockRight){
            translateX((int)targeX);
        }else if (targeX < 0 && !touchLeft() && !lockLeft){
            translateX((int)targeX);
        }
        if (targeY > 0 && !touchBottom() && !lockDown){
            translateY((int)targeY);
        }else if (targeY < 0 && !touchTop() && !lockUp){
            translateY((int)targeY);
        }
    }

    /*使用時，請在場景的paint方法中
    1.camera.start(g) //將畫布移動到您的顯示視窗範圍(0,0)
    2.放入您的物件(請讓每個物件與camera做isCollision碰撞判斷，有重疊到才paint)
    EX: if(camera.isCollision(ac)){
            ac.paint(g);
        }
    3. camera.end(g) 將畫布移回原位
    4. 如果有第二顆camera 再次操作 1 ~ 3。 
    */
    public void start(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        tmpCanvas = g2d.getTransform(); // 暫存畫布
        // 先將畫布初始移到(0,0)（-painter().left()/top()) 然後再到自己想定位的地點(+ cameraWindowX/Y)
        g2d.translate(-painter().left() + cameraWindowX ,  -painter().top() + cameraWindowY ); 
        // 將畫布依照鏡頭大小作裁切
        g.setClip(painter().left(), painter().top(),painter().width(), painter().height()); 
    }
    
    public void end(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        g2d.setTransform(tmpCanvas); // 將畫布移回原位
        g.setClip(null); //把畫布的裁切還原。
    }
    
    @Override
    public void update() {
        if(obj != null){
            chaseMove(); // 追焦功能
        }
    }

    @Override
    public void paintComponent(Graphics g) {
    }
    
    @Override
    public void keyPressed(int commandCode, long trigTime) { 
        //無追焦時啟用自由移動鏡頭
        Direction d = Direction.getDirection(commandCode);
        if (obj == null && d != null){
            switch (d) {
                case UP:
                    if (!touchTop()) {
                        translateY(-cameraMoveSpeed);
                    }
                    break;
                case DOWN:
                    if (!touchBottom()) {
                        translateY(cameraMoveSpeed);
                    }
                    break;
                case LEFT:
                    if (!touchLeft()) {
                        translateX(-cameraMoveSpeed);
                    }
                    break;
                case RIGHT:
                    if (!touchRight()) {
                        translateX(cameraMoveSpeed);
                    }
                    break;
            }
        }
    }
    @Override
    public void keyReleased(int commandCode, long trigTime) {
    }
    @Override
    public void keyTyped(char c, long trigTime) {
    }
    
    public void setCameraMoveSpeed(int num){
        cameraMoveSpeed = num;
    }
    public void setChaseDivisorX(double num){
        chaseDivisorX = num;
    }
    public void setChaseDivisorY(double num){
        chaseDivisorY = num;
    }
    public GameObject obj(){
        return obj;
    }
    public void lockLeft(boolean lock){
        lockLeft = lock;
    }
    public void lockRight(boolean lock){
        lockRight = lock;
    }
    public void lockUp(boolean lock){
        lockUp = lock;
    } 
    public void lockDown(boolean lock){
        lockDown = lock;
    }
    public void setCameraWindowX(int num){
        cameraWindowX = num;
    }
    public int cameraWindowX(){
        return cameraWindowX;
    }
    public void setCameraWindowY(int num){
        cameraWindowY = num;
    }
    public int cameraWindowY(){
        return cameraWindowY;
    }
    
}
