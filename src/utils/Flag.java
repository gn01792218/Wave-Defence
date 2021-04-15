package utils;

import controllers.ImageController;
import gameobj.GameObject;
import gameobj.Rect;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Flag implements GameKernel.PaintInterface,GameKernel.UpdateInterface{
    private final Rect collider;
    private final Rect painter;
    private BufferedImage image;
    private boolean flagUsable;

    public Flag(int x, int y, int width, int height){
        collider = Rect.genWithCenter(x, y, width, height);
        painter = Rect.genWithCenter(x, y, width, height);
        this.image = ImageController.getInstance().tryGet("/Flag.png");
        flagUsable=true;
    }

    public Rect getCollider(){
        return this.collider;
    }

    public Rect getPainter(){
        return this.painter;
    }

    public boolean isFlagUsable() {
        return flagUsable;
    }

    public void setFlagUsable(boolean flagUsable) {
        this.flagUsable = flagUsable;
    }
    //    public final void setCenter (float x, float y){
//        float newX = x - painter.centerX();
//        float newY = y - painter.centerY();
//        if(newX<Global.BOUNDARY_X1){
//            newX = Global.BOUNDARY_X1;
//        }else if(newX>Global.BOUNDARY_X2){
//            newX = Global.BOUNDARY_X2;
//        }
//        if(newY<Global.BOUNDARY_Y1){
//            newY = Global.BOUNDARY_Y1;
//        }else if(newY > Global.BOUNDARY_Y2){
//            newY = Global.BOUNDARY_Y2;
//        }
//        this.painter.translate(newX,newY);
//    }

    public void setCenter(float x,float y){
        this.collider.setCenter(x,y);
        this.painter.setCenter(x,y);
    }


    @Override
    public void paint(Graphics g) {
        g.drawImage(image,(int)painter.centerX(),(int)painter.centerY(),null);
    }

    @Override
    public void update() {

    }
}