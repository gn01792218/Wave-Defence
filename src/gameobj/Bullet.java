package gameobj;
import controllers.ImageController;
import controllers.ImageOperate;
import utils.Delay;
import utils.GameKernel;
import utils.Global;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
public class Bullet extends GameObject implements GameKernel.UpdateInterface,GameKernel.PaintInterface {
    private Image image1;
    private Image image2;
    private Delay delay;
    private boolean isExplored;
    private boolean isTime;
    public Global.Trigonometric trigonometric;
    public Bullet(float x, float y,float targetX,float targetY) {
        super(x,y,35,70);
        trigonometric = new Global.Trigonometric(x,y,targetX,targetY);
        this.image1 = ImageOperate.rotateImage(ImageController.getInstance().tryGet("/bullet.png"),trigonometric.getDegree());
        this.image2 = ImageController.getInstance().tryGet("/boomb.png");
        delay = new Delay(30);
        isExplored = false;
        isTime = false;
    }
    public void explored(){
        this.isExplored = true;
    }
    public boolean isExplored() {
        return isExplored;
    }
    public boolean isTime(){
        return isTime;
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
        if(!isExplored) {
            this.painter().translate(Global.BULLET_SPEED * this.trigonometric.getxVector(),
                    Global.BULLET_SPEED * this.trigonometric.getyVector());
        }else if(delay.isPause()){
            delay.play();
        }else if(delay.count()){
            isTime = true;
        }
    }
}
