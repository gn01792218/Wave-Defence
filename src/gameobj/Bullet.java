package gameobj;
import controllers.ImageController;
import controllers.ImageOperate;
import utils.Delay;
import utils.GameKernel;
import utils.Global;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
public class Bullet extends GameObject implements GameKernel.UpdateInterface,GameKernel.PaintInterface {
    private Image image1;
    private Image image2;
    private Delay delay;
    private boolean isExplored;
    private boolean isTime;
    public Global.Trigonometric trigonometric;
    public Bullet(float x, float y,float targetX,float targetY) {
        super(x,y,1,1,x,y,35,70);
        trigonometric = new Global.Trigonometric(x,y,targetX,targetY);
//        this.image1 = ImageController.getInstance().tryGet("/bullet.png");
//        this.image1 = ImageOperate.rotateImage(ImageController.getInstance().tryGet("/Teacher.png"),trigonometric.getDegree());
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
            if(trigonometric.getyVector()<0){
                g.drawImage(image2,(int)(this.painter().centerX()),(int)(this.painter().centerY())-45,null);
            }else {
                g.drawImage(image2,(int)(this.painter().centerX()),(int)(this.painter().centerY()),null);
            }
        }else{
//            Graphics2D g2 = (Graphics2D)g;
//            AffineTransform t = g2.getTransform();
//            g2.rotate(Math.toRadians(trigonometric.getDegree()),700,400);
//            g2.drawImage(image1,(int)(this.painter().centerX()),(int)(this.painter().centerY()),null);
//            g2.setTransform(t);
            g.drawImage(image1,(int)(this.painter().centerX()),(int)(this.painter().centerY()),null);
        }
        if (Global.IS_DEBUG) {
            g.setColor(Color.RED);
            g.drawRect((int)this.painter().left(), (int)this.painter().top(), (int)this.painter().width(), (int)this.painter().height());
            g.setColor(Color.BLUE);
            g.drawRect((int)this.collider().left(), (int)this.collider().top(), (int)this.collider().width(), (int)this.collider().height());
            g.setColor(Color.BLACK);
        }

    }
    @Override
    public void update() {
        if(!isExplored) {
            this.translate(Global.BULLET_SPEED * this.trigonometric.getxVector(),
                    Global.BULLET_SPEED * this.trigonometric.getyVector());
        }else if(delay.isPause()){
            delay.play();
        }else if(delay.count()){
            isTime = true;
        }

    }
}
