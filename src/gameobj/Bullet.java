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

public class Bullet extends GameObject implements GameKernel.UpdateInterface, GameKernel.PaintInterface {
    protected Image image1;
    protected Image image2;
    protected Delay delay;
    protected boolean isExplored;
    protected boolean isTime; //時候到了，子彈藥消失並移除
    public Global.Trigonometric trigonometric;
    public int count; //爆炸切圖的count

    public Bullet(float x, float y, float width, float heigh, float targetX, float targetY) {
        super(x, y, 25, 60, x, y, width, heigh);
        trigonometric = new Global.Trigonometric(x, y, targetX, targetY);
//        this.image1 = ImageController.getInstance().tryGet("/bullet.png");
//        this.image1 = ImageOperate.rotateImage(ImageController.getInstance().tryGet("/Teacher.png"),trigonometric.getDegree());f
        this.image1 = ImageOperate.rotateImage(ImageController.getInstance().tryGet("/bullet.png"), trigonometric.getDegree());
        this.image2 = ImageController.getInstance().tryGet("/boom2.png");
        delay = new Delay(10);
        isExplored = false;
        isTime = false;
        count = 0;
    }

    public void explored() {
        this.isExplored = true;
    }

    public boolean isExplored() {
        return isExplored;
    }

    public boolean isTime() {
        return isTime;
    }

    public void paintComponent(Graphics g) {
        if (isExplored) {
            int tx = count * 65;
            delay.play();
            if(delay.isPlaying()){
                    g.drawImage(image2, (int) (this.painter().left()), (int) (this.painter().top()), (int) (this.painter().right()), (int) (this.painter().bottom()),
                            tx, 0, tx + 65, 75, null);
            }
            if (delay.count()) {
                count++;
            }
            if(this.count>3){
            this.isTime=true;} //假如count到3之後就標示為true
//            if (trigonometric.getyVector() < 0) {
//                g.drawImage(image2, (int) (this.painter().centerX()), (int) (this.painter().centerY()) - 45, null);
//            } else {
//                g.drawImage(image2, (int) (this.painter().centerX()), (int) (this.painter().centerY()), null);
//            }
        } else {
//            Graphics2D g2 = (Graphics2D)g;
//            AffineTransform t = g2.getTransform();
//            g2.rotate(Math.toRadians(trigonometric.getDegree()),700,400);
//            g2.drawImage(image1,(int)(this.painter().centerX()),(int)(this.painter().centerY()),null);
//            g2.setTransform(t);
            g.drawImage(image1, (int) (this.painter().centerX()), (int) (this.painter().centerY()), null);
        }
    }

    @Override
    public void update() {
        if (!isExplored) {
            this.translate(Global.BULLET_SPEED * this.trigonometric.getxVector(),
                    Global.BULLET_SPEED * this.trigonometric.getyVector());
        }
//        else if (delay.isPause()) {
//            delay.play();
//        } else if (delay.count()) {
//            isTime = true;
//        }
    }
}
