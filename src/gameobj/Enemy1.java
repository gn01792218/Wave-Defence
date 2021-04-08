package gameobj;

import controllers.ImageController;
import controllers.ImageOperate;

import java.awt.*;

public class Enemy1 extends Actor{
    public Enemy1(int x,int y){
        super(x,y,75,90);
        this.image= ImageController.getInstance().tryGet("/enemy1.png");
        this.setSpeed(3);
    }
    @Override
    public void paint(Graphics g) {
        g.drawImage(image,this.painter().centerX(),this.painter().centerY(),null);
    }

    @Override
    public void update() {


    }
}
