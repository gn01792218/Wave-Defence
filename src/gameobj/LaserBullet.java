package gameobj;

import controllers.ImageController;
import controllers.ImageOperate;

public class LaserBullet extends Bullet{
    public LaserBullet(float x, float y, float targetX, float targetY) {
        super(x,y,1,1,targetX,targetY);
        this.image1 = ImageOperate.rotateImage(ImageController.getInstance().tryGet("/LaserBullet.png"),trigonometric.getDegree());
    }
}
