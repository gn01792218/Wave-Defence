package gameobj;

import utils.Flag;
import utils.Global;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Actor extends GameObject{
    //設基本屬性的g s 並且offset
    protected BufferedImage image;
    protected double hp;
    protected double atk;
    protected double atkSpeed;
    protected float speed;
    protected double def;
    protected double  atkdis;
    protected int dirX; //0左 1右
    protected int dirY; //0上 1下
    protected boolean isenemy; //標示此單位是敵是我
    protected boolean isDeaded; //標示是否死亡
    protected ArrayList<Bullet> bullets; //每個角色都有彈藥


    public Actor(int x, int y, int width, int height){
        super(x,y,width,height);
        this.dirX=0;
        this.dirY=0;
        bullets=new ArrayList<>();
    }

    //基本方法區 get
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
        return hp;
    }
    public double getAtk() {
        return atk;
    }
    public double getAtkSpeed() {
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

    //offset方法區
    public void offsetHp(double hp){
        this.hp+=hp;
    }
    public void offsetAtk(double atk){this.atk+=atk;}
    public void offsetAtkSpeed(double atkSpeed){this.atkSpeed+=atkSpeed;}
    public void offsetSpeed(double speed){this.speed+=speed;}
    public void offsetDef(double def){this.def+=def;}
    public void offsetAtkdis(double atkdis){this.atkdis+=atkdis;}

    //核心方法區-->子類實現
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
        if(targetIsInBattleFeild(x,y)){
            changeDir(x,y); //依據flage的位置改變方向
            //角色的tranlate根據x/y的斜率來走
            float a = Math.abs(painter().centerX() - x);//x座標差值 對邊
            float b = Math.abs(painter().centerY() - y); //y座標差值 臨邊
            float d = (float) Math.sqrt(a * a + b * b); //斜邊
            //當d的距離大於10時才執行，所以會在距離敵軍100的地方停下來
            //但需要解決和我軍重疊的問題
            if(d>this.getAtkdis()) {
                float xM = (float) a / d * speed;  //x向量
                float yM = (float) b / d * speed; //y向量
                if (painter().centerX() > x) {
                    xM = -xM;
                }
                if (painter().centerY() > y) {
                    yM = -yM;
                }
                if(a==0){ //表示在同一個直線上 x座標相同

                }
                this.painter().offSet((int) xM, (int) yM);
            }
        }
    }
    //判斷目標在不在場內
    public boolean targetIsInBattleFeild(float x,float y){
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
    //選最短距離者追蹤並攻擊，
    public void autoAttack(ArrayList<Actor> actors){ //傳敵軍陣列近來
        //先一一算出最短距離，存進數字陣列中
        float a=0;
        float b=0;
        float d=0;
        float mind=Integer.MAX_VALUE;
        float actorX=0;
        float actorY=0;
        for(int i=0;i<actors.size();i++){
            a=Math.abs(this.painter().centerX()-actors.get(i).painter().centerX());
            b=Math.abs(this.painter().centerY()-actors.get(i).painter().centerY());
            d=(float)Math.sqrt(a*a+b*b);
            if(d<mind){ //最短距離者 ，取他的XY值
                mind=d;
                actorX=actors.get(i).painter().centerX();
                actorY=actors.get(i).painter().centerY();
            }
        }
        //使用座標點版本
        moveToTarget(actorX,actorY);
        fire(actorX,actorY);
    }
    //開火
    public void fire(float x,float y){
        if (isInAtkdis(x, y)) { //在攻擊範圍內，就開火
            //在自己的top產生子彈
            bullets.add(new Bullet(this.painter().centerX() - 30, this.painter().centerY() - 80, x, y));
        }
    }
    //受到傷害
    public void Damaged(Actor actor){
        offsetHp(-actor.getAtk());
    }
    //是否死亡
    public boolean isDeaded(){
        return this.getHp()<=0;
    }
    public abstract void paint(Graphics g);
    public abstract void update();
}
