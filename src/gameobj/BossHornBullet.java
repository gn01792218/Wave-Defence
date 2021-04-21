package gameobj;

import controllers.ImageController;
import controllers.ImageOperate;
import utils.Global;

import java.awt.*;

public class BossHornBullet extends Bullet{
    public BossHornBullet(float x, float y, float targetX, float targetY) {
        super(x, y, targetX, targetY);
        this.image1 = ImageOperate.rotateImage(ImageController.getInstance().tryGet("/BossHorn.png"),trigonometric.getDegree());
    }
}
