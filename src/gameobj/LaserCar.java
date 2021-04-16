package gameobj;

import controllers.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;
public class LaserCar extends Actor{
    public LaserCar(float x,float y,boolean isEnemy){
        super(x,y,50,50,x,y,75,90);
        this.image= ImageController.getInstance().tryGet("/AALaserCar.png");
//        this.image2=ImageController.getInstance().tryGet("/AAtank1B.png");
        hpLimit=250;//血量上限
        hp=hpLimit; //初始血量
        atk=250; //攻擊力
        atkSpeed = new Delay(30);      //攻速
        speed=5f; //移動速度
        def=0.3; //防禦力
        atkdis=300; //攻擊距離
        this.isEnemy=isEnemy; //敵我單位

    }
    @Override
    public Global.ActorType getType() {
        return Global.ActorType.LASERCAR;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(image,(int)this.painter().left(),(int)this.painter().top(),(int)this.painter().right(),(int)this.painter().bottom(),
                this.cannonDirection.getValue()%3*75,cannonDirection.getValue()/3*90,
                this.cannonDirection.getValue()%3*75+75,cannonDirection.getValue()/3*90+90,null);
        for(int i=0;i<this.bullets.size();i++){
            this.bullets.get(i).paint(g);
        }

    }
}
