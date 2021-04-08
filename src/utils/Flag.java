package utils;

import controllers.ImageController;

import java.awt.*;

//建立旗幟屬性儲存X與Y座標，用Setter封裝其屬性
public class Flag implements GameKernel.PaintInterface,GameKernel.UpdateInterface{
    private int x;
    private int y;
    private final Image image;

    public Flag(){
        this.image = ImageController.getInstance().tryGet("/Flag.png");
        this.x =0;
        this.y =0;
    }

    public void setFlag(int x,int y){
        setX(x);
        setY(y);
    }

    public void setX(int x) {
        if(x<Global.BOUNDARY_X1){
            this.x = 360;
        }else if(x>Global.BOUNDARY_X2){
            this.x = 1060;
        }else {
            this.x = x;
        }
    }

    public void setY(int y) {
        if(y<Global.BOUNDARY_Y1){
            this.y = 180;
        }else if(y>Global.BOUNDARY_Y2){
            this.y = 700;
        }else {
            this.y = y;
        }
    }

    public int getFlagX(){
        return this.x;
    }

    public int getFlagY(){
        return y;
    }

    public void printFlag(){
        System.out.println("Flag("+getFlagX()+","+getFlagY()+")");
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image,getFlagX(),getFlagY(),null);
    }

    @Override
    public void update() {

    }
}