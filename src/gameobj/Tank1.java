package gameobj;

import controllers.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tank1 extends Actor{
    protected BufferedImage image_S1;//技能1圖片
    protected BufferedImage image_S2;// 技能2圖片
    protected BufferedImage image_S3;//技能3圖片
    protected BufferedImage image_S4;//技能4圖片
    protected BufferedImage image_S5;//技能5圖片
    protected BufferedImage image_S7;//技能7圖片

    public Tank1(float x, float y,boolean isenemy){
        super(x,y,50,50,x,y,75,90);
        this.image= ImageController.getInstance().tryGet("/AAtank1.png");
        this.image2=ImageController.getInstance().tryGet("/AAtank1B.png");
        this.image_S1=ImageController.getInstance().tryGet("/AATank1_S1.png");
        this.image_S2=ImageController.getInstance().tryGet("/AATank1_S2.png");
        this.image_S3=ImageController.getInstance().tryGet("/AATank1_S3.png");
        this.image_S4=ImageController.getInstance().tryGet("/AATank1_S4.png");
        this.image_S5=ImageController.getInstance().tryGet("/AATank1_S5.png");
        this.image_S7=ImageController.getInstance().tryGet("/AATank1_S7.png");
        hpLimit=300;//血量上限
        hp=hpLimit; //初始血量
        atk=250; //攻擊力
        atkSpeed = new Delay(120);      //攻速
        this.atkSpeedLimit=atkSpeed.getCountLimit();
        speed=2.2f; //移動速度
        def=0.5; //防禦力
        atkdis=200; //攻擊距離
        this.isEnemy=isEnemy; //敵我單位
        atkSpeed.loop();
    }

    @Override
    public Global.ActorType getType() {
        return Global.ActorType.TANK1;
    }

    public void paintComponent(Graphics g){
        if(isInControl){ //被控制時要畫藍色的圖
            g.drawImage(image2,(int)this.painter().left(),(int)this.painter().top(),(int)this.painter().right(),(int)this.painter().bottom(),
                    this.cannonDirection.getValue()%3*75,cannonDirection.getValue()/3*90,
                    this.cannonDirection.getValue()%3*75+75,cannonDirection.getValue()/3*90+90,null);
        }else{
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
                            System.out.println("畫技能7");
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
