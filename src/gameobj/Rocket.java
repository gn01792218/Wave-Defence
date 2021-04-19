package gameobj;

import controllers.AudioResourceController;
import controllers.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;
import java.util.ArrayList;

public class Rocket extends Actor{

    public Rocket(float x,float y,boolean isEnemy){
        super(x,y,50,50,x,y,75,90);
        this.image= ImageController.getInstance().tryGet("/AARocket.png");
        this.image2=ImageController.getInstance().tryGet("/AARocketB.png");
        this.image_S1=ImageController.getInstance().tryGet("/AARocket_S1.png");
        this.image_S2=ImageController.getInstance().tryGet("/AARocket_S2.png");
        this.image_S3=ImageController.getInstance().tryGet("/AARocket_S3.png");
        this.image_S4=ImageController.getInstance().tryGet("/AARocket_S4.png");
        this.image_S5=ImageController.getInstance().tryGet("/AARocket_S5.png");
        hpLimit=250;//血量上限
        hp=hpLimit; //初始血量
        atk=300; //攻擊力-->全域傷害~!!!!!
        atkSpeed = new Delay(240); //4秒一發
        speed=1.2f; //移動速度
        def=0.1; //防禦力
        atkdis=500; //攻擊距離
        this.isEnemy=isEnemy; //敵我單位
    }
    //開火
    public void fire (float x,float y){
        Bullet bullet = new RocketBullet(this.painter().centerX(), this.painter().centerY(), x, y);
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
            bullets.add(bullet);
            AudioResourceController.getInstance().shot("/T.wav");
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
                AudioResourceController.getInstance().shot("/boomSound_Rocket.wav");   //音效改核彈音效
            }else {
                for(int j=0;j<actors.size();j++) {   //攻擊敵機並扣血
                    if (bullets.get(i).isCollision(actors.get(j))) {
                        bullets.get(i).explored();
                        AudioResourceController.getInstance().shot("/boomSound_Rocket.wav");  //炮炸改爆炸音效
                        for(int k=0;k<actors.size();k++){
                            actors.get(k).offsetHp(-this.atk);
                        }
                    }


                }
            }
        }
    }
    @Override
    public Global.ActorType getType() {
        return Global.ActorType.ROCKET;
    }

    @Override
    public void paintComponent(Graphics g) {
        if(isInControl){ //被控制時要畫藍色的圖
            g.drawImage(image2, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                    0, 0,
                    140, 180, null);
        }else{
            if(isOnBuff()){ //buff狀態時候的畫圖
                if(this.skillName!=null) {
                    switch (skillName) {
                        case ATTACKUP:
                            g.drawImage(image_S1, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                                    0, 0,
                                    140, 180, null);
                            break;
                        case DEFUP:
                            g.drawImage(image_S2, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                                    0, 0,
                                    140, 180, null);
                            break;
                        case MOVESPEEDUP:
                            g.drawImage(image_S3, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                                    0, 0,
                                    140, 180, null);
                            break;
                        case HPUP:
                            g.drawImage(image_S4, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                                    0, 0,
                                    140, 180, null);
                            break;
                    }
                }

            }else if(isReinforcement){
                g.drawImage(image_S5, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                        0, 0,
                        140, 180, null);
            }
            else { //平常時候
                g.drawImage(image, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                        0, 0,
                        140, 180, null);
            }
        }
        g.drawImage(image_hp,(int)this.painter().left(),(int)this.painter().bottom(),(int)(this.painter().left()+(7.5F*(this.hp*10/hpLimit))),(int)this.painter().bottom()+11,
                0,0,75,11,null);
        for(int i=0;i<this.bullets.size();i++){
            this.bullets.get(i).paint(g);
        }
    }

    }

