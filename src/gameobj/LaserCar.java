package gameobj;

import controllers.AudioResourceController;
import controllers.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LaserCar extends Actor{
    protected BufferedImage image_S1;//技能1圖片
    protected BufferedImage image_S2;// 技能2圖片
    protected BufferedImage image_S3;//技能3圖片
    protected BufferedImage image_S4;//技能4圖片
    protected BufferedImage image_S5;//技能5圖片
    protected BufferedImage image_S7;//技能7圖片
    public LaserCar(float x,float y,boolean isEnemy){
        super(x,y,50,50,x,y,75,90);
        this.image= ImageController.getInstance().tryGet("/AALaserCar.png");
        this.image2=ImageController.getInstance().tryGet("/AALaserCarB.png");
        this.image_S1=ImageController.getInstance().tryGet("/AALaserCar_S1.png");
        this.image_S2=ImageController.getInstance().tryGet("/AALaserCar_S2.png");
        this.image_S3=ImageController.getInstance().tryGet("/AALaserCar_S3.png");
        this.image_S4=ImageController.getInstance().tryGet("/AALaserCar_S4.png");
        this.image_S5=ImageController.getInstance().tryGet("/AALaserCar_S5.png");
        this.image_S7=ImageController.getInstance().tryGet("/AALaserCar_S7.png");
        hpLimit=250;//血量上限
        hp=hpLimit; //初始血量
        atk=150; //攻擊力
        atkSpeed = new Delay(40);      //攻速
        atkSpeed.loop();
        this.atkSpeedLimit=atkSpeed.getCountLimit();
        speed=3f; //移動速度
        def=0.2; //防禦力
        atkdis=300; //攻擊距離
        this.isEnemy=isEnemy; //敵我單位
        atkSpeed.loop();
    }
    @Override
    public void fire (float x,float y){
        Bullet bullet = new LaserBullet(this.painter().centerX(), this.painter().centerY(), x, y);
//        根據角度變換砲管方向的狀態
        int atkAngle = bullet.trigonometric.getDegree() + 270;
        if (atkAngle < 60) {
            this.cannonDirection = CANNON_DIRECTION.FrontLeft;
        } else if (atkAngle <= 120) {
            this.cannonDirection = CANNON_DIRECTION.FrontMiddle;
        } else if (atkAngle <= 180) {
            this.cannonDirection = CANNON_DIRECTION.FrontRight;
        } else if (atkAngle < 240) {
            this.cannonDirection = CANNON_DIRECTION.BackRight;
        } else if (atkAngle <= 300) {
            this.cannonDirection = CANNON_DIRECTION.BackMiddle;
        } else if (atkAngle < 360) {
            this.cannonDirection = CANNON_DIRECTION.BackLeft;
        }

        if (atkSpeed.count()) {
            AudioResourceController.getInstance().shot("/LaserSound.wav");
            bullets.add(bullet);
        }
    }
    @Override
    public void bulletsUpdate (ArrayList< Actor > actors) {
        ////飛彈爆炸後一定時間後消失
        for (int i = 0; i < this.bullets.size(); i++) {
            this.bullets.get(i).update(); //子彈移動
            if (bullets.get(i).isExplored()) {
                if (bullets.get(i).isTime()) {
                    bullets.remove(i);
                    i--;
                }
            } else if (isTouchBattleEdge(bullets.get(i).collider().centerX(), bullets.get(i).collider().centerY())) {
                bullets.get(i).explored();
                AudioResourceController.getInstance().shot("/explosion.wav");   //音效改核彈音效
            }else {
                for(int j=0;j<actors.size();j++) {   //攻擊敵機
                    if (bullets.get(i).isCollision(actors.get(j))) {
                        bullets.get(i).explored();
                        AudioResourceController.getInstance().shot("/explosion.wav");  //炮炸改爆炸音效
                    }
                    if(bullets.get(i).isExplored()){  //造成範圍攻擊
                        for(int k=0;k<actors.size();k++){
                            if(Math.abs(actors.get(k).painter().centerX()-bullets.get(i).painter().centerX())<=150
                                    && Math.abs(actors.get(k).painter().centerY()-bullets.get(i).painter().centerY())<=200)  //子彈左右100  下上80的距離 敵軍都會被扣血
                                actors.get(k).offsetHp(-this.atk);
                        }
                    }
                }
            }
        }
    }

    @Override
    public Global.ActorType getType() {
        return Global.ActorType.LASERCAR;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (isInControl) { //被控制時要畫藍色的圖
            g.drawImage(image2, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                    this.cannonDirection.getValue() % 3 * 75, cannonDirection.getValue() / 3 * 90,
                    this.cannonDirection.getValue() % 3 * 75 + 75, cannonDirection.getValue() / 3 * 90 + 90, null);
        } else {
            if(isOnBuff()){ //buff狀態時候的畫圖
                if(this.skillName!=null) {
                    switch (skillName) {
                        case ATTACKUP:
                            g.drawImage(image_S1, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                                    this.cannonDirection.getValue() % 3 * 75, cannonDirection.getValue() / 3 * 90,
                                    this.cannonDirection.getValue() % 3 * 75 + 75, cannonDirection.getValue() / 3 * 90 + 90, null);
                            break;
                        case DEFUP:
                            g.drawImage(image_S2, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                                    this.cannonDirection.getValue() % 3 * 75, cannonDirection.getValue() / 3 * 90,
                                    this.cannonDirection.getValue() % 3 * 75 + 75, cannonDirection.getValue() / 3 * 90 + 90, null);
                            break;
                        case MOVESPEEDUP:
                            g.drawImage(image_S3, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                                    this.cannonDirection.getValue() % 3 * 75, cannonDirection.getValue() / 3 * 90,
                                    this.cannonDirection.getValue() % 3 * 75 + 75, cannonDirection.getValue() / 3 * 90 + 90, null);
                            break;
                        case HPUP:
                            g.drawImage(image_S4, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                                    this.cannonDirection.getValue() % 3 * 75, cannonDirection.getValue() / 3 * 90,
                                    this.cannonDirection.getValue() % 3 * 75 + 75, cannonDirection.getValue() / 3 * 90 + 90, null);
                            break;
                        case ATKSPEEDUP:
                            g.drawImage(image_S7, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                                    this.cannonDirection.getValue() % 3 * 75, cannonDirection.getValue() / 3 * 90,
                                    this.cannonDirection.getValue() % 3 * 75 + 75, cannonDirection.getValue() / 3 * 90 + 90, null);
                            break;
                    }
                }
            }else if(isReinforcement){
                g.drawImage(image_S5, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                        this.cannonDirection.getValue() % 3 * 75, cannonDirection.getValue() / 3 * 90,
                        this.cannonDirection.getValue() % 3 * 75 + 75, cannonDirection.getValue() / 3 * 90 + 90, null);
            }
            else { //平常時候
                g.drawImage(image, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                        this.cannonDirection.getValue() % 3 * 75, cannonDirection.getValue() / 3 * 90,
                        this.cannonDirection.getValue() % 3 * 75 + 75, cannonDirection.getValue() / 3 * 90 + 90, null);
            }
        }
        g.drawImage(image_hp,(int)this.painter().left(),(int)this.painter().bottom(),(int)(this.painter().left()+(7.5F*(this.hp*10/hpLimit))),(int)this.painter().bottom()+11,
                0,0,75,11,null);
        for(int i=0;i<this.bullets.size();i++){
            this.bullets.get(i).paint(g);
        }
    }
}
