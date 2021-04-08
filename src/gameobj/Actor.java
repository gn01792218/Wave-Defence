package gameobj;

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
    protected int dir;

    public Actor(int x,int y,int width,int height){
        super(x,y,width,height);
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
    public void setDir(int dir) {
        this.dir = dir;
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
    public int getDir() {
        return dir;
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
    //角色移動
    public void move(){
        //整個移動範圍在x360-1060；y在180~700

    }
    
    //核心方法區-->子類實現
    public abstract void paint(Graphics g);
    public abstract void update();
}
