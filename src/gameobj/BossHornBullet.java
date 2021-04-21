package gameobj;

import controllers.ImageController;
import controllers.ImageOperate;
import utils.Global;

import java.awt.*;

public class BossHornBullet extends Bullet{
    public BossHornBullet(float x, float y, float targetX, float targetY) {
        super(x, y, targetX, targetY);
        this.image1 = ImageOperate.rotateImage(ImageController.getInstance().tryGet("/BossHorn.png"),trigonometric.getDegree());
        this.image2 = ImageController.getInstance().tryGet("/Boss_boom.png");
    }
    public void paintComponent(Graphics g){
        if(isExplored){
            if(trigonometric.getyVector()<0){
                g.drawImage(image2,(int)(this.painter().centerX()),(int)(this.painter().centerY()-50)-45,null);
            }else {
                g.drawImage(image2,(int)(this.painter().centerX()),(int)(this.painter().centerY()-50),null);
            }
        }else{
            g.drawImage(image1,(int)(this.painter().centerX()),(int)(this.painter().centerY()),null);
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
