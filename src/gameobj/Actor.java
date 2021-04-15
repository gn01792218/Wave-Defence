package gameobj;

import controllers.AudioResourceController;
import utils.Delay;
import utils.Global;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Actor extends GameObject{
    //設基本屬性的g s 並且offset
    protected BufferedImage image;
    protected double hp;
    protected double atk;
    protected Delay atkSpeed;
    protected float speed;
    protected double def;
    protected double  atkdis;
//    protected int dirX; //0左 1右
//    protected int dirY; //0上 1下
    protected boolean isEnemy; //標示此單位是敵是我
    protected boolean isAlive; //標示是否死亡
    protected ArrayList<Bullet> bullets; //每個角色都有彈藥
    protected Rect strategyXY; //我的戰略座標
    protected CANNON_DIRECTION cannonDirection;
    protected enum CANNON_DIRECTION{
        FrontLeft(3),
        FrontMiddle(4),
        FrontRight(5),
        BackLeft(0),
        BackMiddle(1),
        BackRight(2);
        private int value;
        private CANNON_DIRECTION(int value){
            this.value = value;
        }
        public int getValue(){
            return this.value;
        }
    }


    public Actor(float x, float y, float width, float height){
        super(x,y,width,height);
//        this.dirX=0;
//        this.dirY=0;
        bullets=new ArrayList<>();
        isAlive=true;
        strategyXY = this.collider();
        //剛開始是起始位置，之後在場景中可以set成旗幟位置
        this.cannonDirection = CANNON_DIRECTION.FrontMiddle;
    }

    //基本方法區 get
//    public int getDirX() {
//        return dirX;
//    }
//    public int getDirY() {
//        return dirY;
//    }
//    public void setDirX(int dirX) {
//        this.dirX = dirX;
//    }
//    public void setDirY(int dirY) {
//        this.dirY = dirY;
//    }
    public double getHp() {
        return this.hp;
    }
    public double getAtk() {
        return atk;
    }
    public Delay getAtkSpeed() {
        return atkSpeed;
    }
    public double getSpeed() {
        return this.speed;
    }
    public double getDef() {
        return def;
    }
    public double getAtkdis() { return this.atkdis;
    }
    //set方法
    public void setFlagXY(float x,float y){//傳入座標點(flag旗幟，或是出生的起始位置等等來設定要他固守的位置)
        strategyXY.setCenter(x,y);
    }
    //offset方法區
    public void offsetHp(double hp){
        this.hp+=hp;
    }
    public void offsetAtk(double atk){this.atk+=atk;}
    public void offsetAtkSpeed(int atkSpeed){this.atkSpeed = new Delay(atkSpeed);}
    public void offsetSpeed(double speed){this.speed+=speed;}
    public void offsetDef(double def){this.def+=def;}
    public void offsetAtkdis(double atkdis){this.atkdis+=atkdis;}

    //核心方法區-->子類實現
    //改變方向
//    public void changeDir(float x,float y){  //依據敵人的座標  或是 Flag的座標改變方向
//        if(x>this.painter().centerX()){
//            dirX=1; //向右
//        }else {
//            dirX=0;
//        }
//        if(y>this.painter().centerY()){
//            dirY=1; //向下
//        }else{
//            dirY=0;
//        }
//    }
    //朝目標移動
    public void move(Rect rect,ArrayList<Actor> actors){
        //根據目標位置移動
        Global.Trigonometric trigonometric = new Global.Trigonometric(this.collider().centerX(),this.collider().centerY(),
                rect.centerX(), rect.centerY());
        int atkAngle = trigonometric.getDegree()+270;
        if(atkAngle<=180){
            this.cannonDirection = CANNON_DIRECTION.FrontMiddle;
        }else{
            this.cannonDirection = CANNON_DIRECTION.BackMiddle;
        }

        float x = rect.centerX();
        float y = rect.centerY();
        if(targetIsInBattleField(x,y)){
//            changeDir(x,y); //依據flag的位置改變方向
            //角色的translate根據x/y的斜率來走
            float a = Math.abs(painter().centerX() - x);//x座標差值 對邊
            float b = Math.abs(painter().centerY() - y); //y座標差值 臨邊
            float d = (float) Math.sqrt(a * a + b * b); //斜邊
            //當d的距離大於10時才執行，所以會在距離敵軍100的地方停下來
            //但需要解決和我軍重疊的問題
            if(d>this.getAtkdis()-(this.getAtkdis()*0.5)) {  //大於0會精準回到原點，且所有人會重疊，亦可能顫抖  ；大於自己的攻擊距離會回到原點+攻擊距離的位置。值不能大於所有角色中射程最短的角色(否則他會無法發射子彈)
                float xM = (float) a / d * speed; //x向量
                float yM = (float) b / d * speed; //y向量

                boolean xArisIsNoTouch = true;
                boolean yArisIsNoTouch = true;

                //檢查跟友軍是否碰撞
                for(int i=0;i<actors.size();i++){
                    if(this.collider().centerX() == actors.get(i).collider().centerX() &&
                    this.collider().centerY() == actors.get(i).collider().centerY()){
                        continue;
                    }
                    if(this.leftIsCollision(actors.get(i))  || this.rightIsCollision(actors.get(i))){
                        xArisIsNoTouch = false;
                    }
                    if(this.topIsCollision(actors.get(i)) || this.bottomIsCollision(actors.get(i))){
                        yArisIsNoTouch = false;
                    }
                }

                if (xArisIsNoTouch){
                    if(painter().centerX()>x){
                        this.offSet((int) -xM,0);
                    }else{
                        this.offSet((int) xM,0);
                    }
                }

                if(yArisIsNoTouch){
                    if(painter().centerY()>y){
                        this.offSet(0,(int) -yM);
                    }else {
                        this.offSet(0,(int) yM);
                    }
                }
            }
        }
    }
    //選最短距離者追蹤並攻擊，敵方死亡後回到原位
    public void autoAttack(ArrayList<Actor> actors,ArrayList<Actor> alliance){ //傳敵軍陣列近來
        if(atkSpeed.isPause()){
            atkSpeed.loop();
        }

        if(actors.size()>0) {
            //先一一算出最短距離，存進數字陣列中找出最近的敵人 = target
            float a = 0;
            float b = 0;
            float d = 0;
            float mind = Integer.MAX_VALUE;
            Rect target =null;
            for (int i = 0; i < actors.size(); i++) {
                a = Math.abs(this.painter().centerX() - actors.get(i).painter().centerX());
                b = Math.abs(this.painter().centerY() - actors.get(i).painter().centerY());
                d = (float) Math.sqrt(a * a + b * b);
                if (d < mind) { //最短距離者 ，取他的XY值
                    mind = d;
                    target = actors.get(i).collider();
                }
            }
            //移動至攻擊範圍內則開火

            if(isInAtkdis(target)){
                    fire(target, actors);
            }else {
                move(target,alliance);
            }
        }

        if(actors.size()<=0 && !this.isEnemy) {
            //回到自己原本的位置並導正砲管
            if(collider().centerX() == strategyXY.centerX() && collider().centerY() == strategyXY.centerY()){
                if(atkSpeed.getCount()<119){
                    atkSpeed.count();
                }
            }else {
                move(strategyXY,actors);
            }
            this.cannonDirection = CANNON_DIRECTION.FrontMiddle;
//                    System.out.println("我的座標" + this.painter().centerX() + " " + this.painter().centerX() + //測試用輸出
//                            " 戰略座標" + this.strategyXY[0] + " " + this.strategyXY[1]);
        }
    }

    //開火
    public void fire(Rect rect,ArrayList<Actor> actors){
        float x = rect.centerX();
        float y = rect.centerY();
//        if(atkSpeed.isPause()){
//            atkSpeed.loop();
//            return;
//        }
//        atkSpeed.loop();
        Bullet bullet = new Bullet(this.painter().centerX(), this.painter().centerY(), x, y);
//        根據角度變換砲管方向的狀態
        int atkAngle = bullet.trigonometric.getDegree()+270;
        if(atkAngle<60){
            this.cannonDirection = CANNON_DIRECTION.FrontLeft;
        }else if(atkAngle<=120){
            this.cannonDirection = CANNON_DIRECTION.FrontMiddle;
        }else if(atkAngle<=180){
            this.cannonDirection = CANNON_DIRECTION.FrontRight;
        }else if(atkAngle<240){
            this.cannonDirection = CANNON_DIRECTION.BackRight;
        }else if(atkAngle<=300) {
            this.cannonDirection = CANNON_DIRECTION.BackMiddle;
        }else if(atkAngle<360) {
            this.cannonDirection = CANNON_DIRECTION.BackLeft;
        }

        if(atkSpeed.count()){
            bullets.add(bullet);
            AudioResourceController.getInstance().play("/T.wav");
        }

        for(int i=0;i<bullets.size();i++) {
            //攻擊敵機並扣血
            for(int j=0;j<actors.size();j++){
                if(!bullets.get(i).isExplored()){
                    if(bullets.get(i).isCollision(actors.get(j))){
                        bullets.get(i).explored();
                        actors.get(j).offsetHp(-(this.atk)*(1-actors.get(j).def));
                    }
                }
            }
        }
    }
    //子彈更新
    private void bulletsUpdate(){
        ////飛彈爆炸後一定時間後消失
        for(int i=0;i<this.bullets.size();i++){
            if (bullets.get(i).isExplored()) {
                if (bullets.get(i).isTime()) {
                    bullets.remove(i);
                    i--;
                    continue;
                }
            }
            //碰到邊界爆炸
            if (bullets.get(i).isTouchBattleEdge()) {
                bullets.get(i).explored();
            }
            this.bullets.get(i).update(); //子彈移動
        }
    }
    //判斷是否死亡
    public boolean isAlive(){
        return this.getHp()>0;
    }
    //判斷目標在不在場內
    public boolean targetIsInBattleField(float x, float y){
        if(x< Global.BOUNDARY_X1 || x>Global.BOUNDARY_X2){return false;}
        if(y<Global.BOUNDARY_Y1 || y>Global.BOUNDARY_Y2){return false;}
        return true;
    }
    //判斷目標是否在射程內
    public boolean isInAtkdis(Rect rect){
        float x = rect.centerX();
        float y = rect.centerY();
        //判斷目標點是否在攻擊距離內:上下左右框框+atkdis
        if(x>this.painter().right()+getAtkdis()){return false;}
        if(x<this.painter().left()-getAtkdis()){return false;}
        if(y>this.painter().bottom()+getAtkdis()){return false;}
        if(y<this.painter().top()-getAtkdis()){return false;}
        return true;
    }
    public abstract void paint(Graphics g);
    public void update(){
        bulletsUpdate();
    };
}
