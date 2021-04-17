package gameobj;

import controllers.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;
public class LaserCar extends Actor{
    public LaserCar(float x,float y,boolean isEnemy){
        super(x,y,50,50,x,y,75,90);
        this.image= ImageController.getInstance().tryGet("/AALaserCar.png");
        this.image2=ImageController.getInstance().tryGet("/AALaserCarB.png");
        this.image_S1=ImageController.getInstance().tryGet("/AALaserCar_S1.png");
        this.image_S2=ImageController.getInstance().tryGet("/AALaserCar_S2.png");
        this.image_S3=ImageController.getInstance().tryGet("/AALaserCar_S3.png");
        this.image_S4=ImageController.getInstance().tryGet("/AALaserCar_S4.png");
        hpLimit=250;//血量上限
        hp=hpLimit; //初始血量
        atk=150; //攻擊力
        atkSpeed = new Delay(40);      //攻速
        speed=3f; //移動速度
        def=0.2; //防禦力
        atkdis=300; //攻擊距離
        this.isEnemy=isEnemy; //敵我單位
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
//                    case REINFORCEMENTS:
//                        g.drawImage(image_S5, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
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
        for(int i=0;i<this.bullets.size();i++){
            this.bullets.get(i).paint(g);
        }

    }
}
