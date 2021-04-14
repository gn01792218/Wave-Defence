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
    private double hpLimit;
    protected double hp;
    protected double atk;
    protected Delay atkSpeed;
    protected float speed;
    protected double def;
    protected double  atkdis;
    protected int dirX; //0左 1右
    protected int dirY; //0上 1下
    protected boolean isenemy; //標示此單位是敵是我
    protected boolean isAlive; //標示是否死亡
    protected ArrayList<Bullet> bullets; //每個角色都有彈藥
    protected float []strategyXY={0,0}; //我的戰略座標


    public Actor(float x, float y, float width, float height){
        super(x,y,width,height);
        this.dirX=0;
        this.dirY=0;
        bullets=new ArrayList<>();
        isAlive=true;
        this.strategyXY[0]=x; //剛開始是起始位置，之後在場景中可以set成旗幟位置
        this.strategyXY[1]=y; //剛開始是起始位置，之後在場景中可以set成旗幟位置
    }

    //基本方法區 get
    public double getHpLimit() {
        return hpLimit;
    }
    public int getDirX() {
        return dirX;
    }
    public int getDirY() {
        return dirY;
    }
    public void setDirX(int dirX) {
        this.dirX = dirX;
    }
    public void setDirY(int dirY) {
        this.dirY = dirY;
    }
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

    public void setHpLimit(double hpLimit) {
        this.hpLimit = hpLimit;
    }
    public void setFlagXY(float x, float y){//傳入座標點(flag旗幟，或是出生的起始位置等等來設定要他固守的位置)
        this.strategyXY[0]=x;
        this.strategyXY[1]=y;
    }
    public void setHp(double hp) {
        this.hp = hp;
    }
    public void setAtk(double atk) {
        this.atk = atk;
    }
    public void setAtkSpeed(Delay atkSpeed) {
        this.atkSpeed = atkSpeed;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    public void setDef(double def) {
        this.def = def;
    }
    public void setAtkdis(double atkdis) {
        this.atkdis = atkdis;
    }
    public void setCenter(float x,float y){
        this.painter().setCenter(x,y);
        this.collider().setCenter(x,y);
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
    //獲得種類的方法，回傳Global的enum
    public abstract Global.ActorType getType();
    //改變方向
    public void changeDir(float x,float y){  //依據敵人的座標  或是 Flag的座標改變方向
        if(x>this.painter().centerX()){
            dirX=1; //向右
        }else {
            dirX=0;
        }
        if(y>this.painter().centerY()){
            dirY=1; //向下
        }else{
            dirY=0;
        }
    }
    //朝目標移動
    public void moveToTarget(float x,float y){
        if(targetIsInBattleField(x,y)){
            changeDir(x,y); //依據flag的位置改變方向
            //角色的translate根據x/y的斜率來走
            float a = Math.abs(painter().centerX() - x);//x座標差值 對邊
            float b = Math.abs(painter().centerY() - y); //y座標差值 臨邊
            float d = (float) Math.sqrt(a * a + b * b); //斜邊
            //當d的距離大於10時才執行，所以會在距離敵軍100的地方停下來
            //但需要解決和我軍重疊的問題
            if(d>this.getAtkdis()-(this.getAtkdis()*0.5)) {  //大於0會精準回到原點，且所有人會重疊，亦可能顫抖  ；大於自己的攻擊距離會回到原點+攻擊距離的位置。值不能大於所有角色中射程最短的角色(否則他會無法發射子彈)
                float xM = (float) a / d * speed;  //x向量
                float yM = (float) b / d * speed; //y向量
                if (painter().centerX() > x) {
                    xM = -xM;
                }
                if (painter().centerY() > y) {
                    yM = -yM;
                }
                this.painter().offSet((int) xM, (int) yM);
            }
        }
    }
    //選最短距離者追蹤並攻擊，敵方死亡後回到原位
    public void autoAttack(ArrayList<Actor> actors){ //傳敵軍陣列近來
            if(actors.size()>0) {
                //先一一算出最短距離，存進數字陣列中
                float a = 0;
                float b = 0;
                float d = 0;
                float mind = Integer.MAX_VALUE;
                float actorX = 0;
                float actorY = 0;
                for (int i = 0; i < actors.size(); i++) {
                    a = Math.abs(this.painter().centerX() - actors.get(i).painter().centerX());
                    b = Math.abs(this.painter().centerY() - actors.get(i).painter().centerY());
                    d = (float) Math.sqrt(a * a + b * b);
                    if (d < mind) { //最短距離者 ，取他的XY值
                        mind = d;
                        actorX = actors.get(i).painter().centerX();
                        actorY = actors.get(i).painter().centerY();
                    }
                }
                //使用座標點版本
                moveToTarget(actorX, actorY);
                fire(actorX, actorY, actors);
                if (atkSpeed.count()) {
                    AudioResourceController.getInstance().play("/T.wav");
                }
            } if(actors.size()<=0 && !this.isenemy) {
            //回到自己原本的位置
                    moveToTarget(this.strategyXY[0], this.strategyXY[1]);
//                    System.out.println("我的座標" + this.painter().centerX() + " " + this.painter().centerX() + //測試用輸出
//                            " 戰略座標" + this.strategyXY[0] + " " + this.strategyXY[1]);
        }
    }
    //開火
    public void fire(float x,float y,ArrayList<Actor> actors){
        if (isInAtkdis(x, y)) { //在攻擊範圍內，就開火
            //在自己的top產生子彈
            if(atkSpeed.isPause()){
                atkSpeed.loop();
                return;
            }
            if(atkSpeed.count()){
                bullets.add(new Bullet(this.painter().centerX() - 30, this.painter().centerY() - 80, x, y));
            }
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
    public boolean isInAtkdis(float x,float y){
        //判斷目標點是否在攻擊距離內:上下左右框框+atkdis
        if(x>this.painter().right()+getAtkdis()){return false;}
        if(x<this.painter().left()-getAtkdis()){return false;}
        if(y>this.painter().bottom()+getAtkdis()){return false;}
        if(y<this.painter().top()-getAtkdis()){return false;}
        return true;
    }
    public abstract void paint(Graphics g);
    public  void update(){
        bulletsUpdate();
    };
}
