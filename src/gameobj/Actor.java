package gameobj;

import utils.Flag;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Actor extends GameObject{
    //設基本屬性的g s 並且offset
    protected BufferedImage image;
    protected double hp;
    protected static double atk;
    protected static double atkSpeed;
    protected static double speed;
    protected static double def;
    private static double atkdis;
    protected int dirX; //0左 1右
    protected int dirY; //0上 1下

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
    public Actor(int x, int y, int width, int height){
        super(x,y,width,height);
        this.dirX=0;
        this.dirY=0;
    }

    //基本方法區 set get
    public void setHp(double hp) {
        this.hp = hp;
    }
    public static void setAtk(double atk) {
        Actor.atk = atk;
    }
    public static void setAtkSpeed(double atkSpeed) {
        Actor.atkSpeed = atkSpeed;
    }
    public static void setSpeed(double speed) {
        Actor.speed = speed;
    }
    public static void setDef(double def) {
        Actor.def = def;
    }
    public static void setAtkdis(double atkdis) {
        Actor.atkdis = atkdis;
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
    public void changeDir(int x,int y){  //依據敵人的座標  或是 Flag的座標改變方向
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
        int x=Math.abs(painter().centerX()- enemy.painter().centerX());//x座標差值
        int y=Math.abs(painter().centerY()-enemy.painter().centerY()); //y座標差值
    }
    //朝著Flag移動
    public void moveToFlag(Flag flag){
        changeDir(flag.getPainter().centerX(),flag.getPainter().centerY()); //依據flage的位置改變方向
        //角色的tranlate根據x/y的斜率來走
            moveWhere(flag.getPainter().centerX(),flag.getPainter().centerY());
    }
    //判斷敵方位置的象限而移動
    public void moveWhere(int x,int y){
        int x1=Math.abs(painter().centerX()- x);//x座標差值
        int y1=Math.abs(painter().centerY()-y); //y座標差值
        if(y1!=0) {
            if (dirX == 1 && dirY == 1) {
                this.painter().translate(x1/y1, x1/y1);//敵人在第四象限 (右下)-->xy方向為 11
            } else if (dirX == 0 && dirY == 0) {
                this.painter().translate(-x1/y1, -x1/y1); //敵人在第二象限(左上)-->xy方向為 00
            } else if (dirX == 1 && dirY == 0) {
                this.painter().translate(x1/y1, -x1/y1); //敵人在第一象限(右上)-->xy方向為 10
            } else {
                this.painter().translate(-x1/y1, x1/y1); //敵人在第三象限(左下)-->xy方向為 01
            }
        }
    }
    //核心方法區-->子類實現
    public abstract void paint(Graphics g);
    public abstract void update();
}
