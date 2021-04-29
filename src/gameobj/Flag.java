package gameobj;

import controllers.ImageController;
import gameobj.GameObject;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Flag extends GameObject{
    private final BufferedImage image;
    private boolean flagUsable;

    public Flag(float x, float y){
        super(x,y,50,50);
        this.image = ImageController.getInstance().tryGet("/Flag.png");
        flagUsable=true;
    }

    public boolean isFlagUsable() {
        return flagUsable;
    }

    public void setFlagUsable(boolean flagUsable) {
        this.flagUsable = flagUsable;
    }

    public void setCenter(float x,float y){
        this.collider().setCenter(x,y);
        this.painter().setCenter(x,y);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(image,(int)collider().left(),(int)collider().top(),null);
    }

    @Override
    public void update() {

    }
}