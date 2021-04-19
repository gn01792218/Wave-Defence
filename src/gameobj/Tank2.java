package gameobj;

import controllers.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;

public class Tank2 extends Actor{
    public Tank2(float x, float y,boolean isenemy){
        super(x,y,60,72,x,y,75,90);
        this.image= ImageController.getInstance().tryGet("/AATank2.png");
        this.image2=ImageController.getInstance().tryGet("/AATank2B.png");
        this.image_S1=ImageController.getInstance().tryGet("/AATank2_S1.png");
        this.image_S2=ImageController.getInstance().tryGet("/AATank2_S2.png");
        this.image_S3=ImageController.getInstance().tryGet("/AATank2_S3.png");
        this.image_S4=ImageController.getInstance().tryGet("/AATank2_S4.png");
        hpLimit=500;//血量上限
        hp=hpLimit; //初始血量
        atk=150; //攻擊力
        atkSpeed = new Delay(120);      //攻速
        speed=2.5f; //移動速度
        def=0.5; //防禦力
        atkdis=80; //攻擊距離
        this.isEnemy=isenemy; //敵我單位
    }

    @Override
    public Global.ActorType getType() {
        return Global.ActorType.TANK2;
    }

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
//                    case REINFORCEMENTS:
//                        g.drawImage(image_S4, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
//                                this.cannonDirection.getValue() % 3 * 75, cannonDirection.getValue() / 3 * 90,
//                                this.cannonDirection.getValue() % 3 * 75 + 75, cannonDirection.getValue() / 3 * 90 + 90, null);
//                        break;
                    }
                }

            }else { //平常時候
                g.drawImage(image, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                        this.cannonDirection.getValue() % 3 * 75, cannonDirection.getValue() / 3 * 90,
                        this.cannonDirection.getValue() % 3 * 75 + 75, cannonDirection.getValue() / 3 * 90 + 90, null);
            }
        }
        g.drawImage(image_hp,(int)this.painter().left(),(int)this.painter().bottom(),(int)(this.painter().left()+(6.5F*(this.hp*10/hpLimit))),(int)this.painter().bottom()+13,
                65,13,0,0,null);
        for (int i = 0; i < this.bullets.size(); i++) { //畫子彈
            this.bullets.get(i).paint(g);
        }
    }
}
