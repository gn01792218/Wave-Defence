package gameobj;

import utils.Flag;
import utils.Global;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Actor extends GameObject{
    //設基本屬性的g s 並且offset
    protected BufferedImage image;
    protected double hp;
    protected static double atk;
    protected static double atkSpeed;
    protected static float speed;
    protected static double def;
    protected static double atkdis;
    protected int dirX; //0左 1右
    protected int dirY; //0上 1下
    protected boolean isenemy; //標示此單位是敵是我

    public Actor(int x, int y, int width, int height){
        super(x,y,width,height);
        this.dirX=0;
        this.dirY=0;
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
    public static double getAtk() {
        return atk;
    }
    public static double getAtkSpeed() {
        return atkSpeed;
    }
    public static double getSpeed() {
        return speed;
    }
    public static double getDef() {
        return def;
    }
    public static double getAtkdis() {
        return atkdis;
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
    //角色移動，translate的斜率即為兩邊差值相除
    //朝著敵人移動-->尚未完成
    public void moveToEnemy(Actor enemy){ //和下面一樣，到時候傳入敵人座標即可
        changeDir(enemy.painter().centerX(),enemy.painter().centerY()); //依據flage的位置改變方向
        float x=Math.abs(painter().centerX()- enemy.painter().centerX());//x座標差值
        float y=Math.abs(painter().centerY()-enemy.painter().centerY()); //y座標差值
    }
    //朝著Flag移動
    public void moveToTarget(float x,float y){
        if(targetIsInBattleFeild(x,y)){
            changeDir(x,y); //依據flage的位置改變方向
            //角色的tranlate根據x/y的斜率來走
            float a = Math.abs(painter().centerX() - x);//x座標差值 對邊
            float b = Math.abs(painter().centerY() - y); //y座標差值 臨邊
            float d = (float) Math.sqrt(a * a + b * b); //斜邊

             //當d的距離大於1時才執行
            if(d>5) {
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

            System.out.println("xy座標"+x+" "+y+"x y距離:"+a+" "+b+" 我的座標點: "+painter().centerX()+" "+painter().centerY()+ " 斜率"+(b/a));
            System.out.println("x方向"+dirX+"y方向"+dirY);
        }

    }
    //判斷目標在不在場內
    public boolean targetIsInBattleFeild(float x,float y){
        if(x< Global.BOUNDARY_X1 || x>Global.BOUNDARY_X2){return false;}
        if(y<Global.BOUNDARY_Y1 || y>Global.BOUNDARY_Y2){return false;}
        return true;
    }
    //核心方法區-->子類實現
    public abstract void paint(Graphics g);
    public abstract void update();
}
