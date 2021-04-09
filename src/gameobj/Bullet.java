package gameobj;

import controllers.ImageController;
import controllers.ImageOperate;
import utils.Delay;
import utils.GameKernel;
import utils.Global;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject implements GameKernel.UpdateInterface,GameKernel.PaintInterface {

    private BufferedImage image1;
    private BufferedImage image2;
    private Delay delay;
    private boolean isExplored;
    public Global.Trigonometric trigonometric;

    public Bullet(int x, int y,int targetX,int targetY) {
        super(x,y,35,70);
        trigonometric = new Global.Trigonometric(x,y,targetX,targetY);
        this.image1 = ImageOperate.rotateImage(ImageController.getInstance().tryGet("/bullet.png"),trigonometric.getDegree());
        this.image2 = ImageController.getInstance().tryGet("/boomb.png");
        delay = new Delay(120);
        isExplored = false;
    }

    public void explored(){
        this.isExplored = true;
    }

    @Override
    public void paint(Graphics g) {
        if(isExplored){
            g.drawImage(image2,(int)(this.painter().centerX()),(int)(this.painter().centerY()),null);
        }else{
            g.drawImage(image1,(int)(this.painter().centerX()),(int)(this.painter().centerY()),null);
        }
    }

    @Override
    public void update() {
        this.painter().translate(Global.BULLET_SPEED * this.trigonometric.getxVector(),
                Global.BULLET_SPEED * this.trigonometric.getyVector());
    }
}
