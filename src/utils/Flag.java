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

    public Flag(int x, int y, int width, int height,
                int x2, int y2, int width2, int height2){
        collider = Rect.genWithCenter(x, y, width, height);
        painter = Rect.genWithCenter(x2, y2, width2, height2);
        this.image = ImageController.getInstance().tryGet("/Flag.png");
    }

    public Rect getCollider(){
        return this.collider;
    }

    public Rect getPainter(){
        return this.painter;
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


    @Override
    public void paint(Graphics g) {
        g.drawImage(image,(int)painter.centerX(),(int)painter.centerY(),null);
    }

    @Override
    public void update() {

    }
}