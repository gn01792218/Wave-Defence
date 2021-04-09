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


    @Override
    public void paint(Graphics g) {
        g.drawImage(image,(int)painter.centerX(),(int)painter.centerY(),null);
    }

    @Override
    public void update() {

    }
}