package gameobj;

import controllers.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;

public class Tank1 extends Actor{


    public Tank1(float x, float y,boolean isenemy){
        super(x,y,75,90);
        this.image= ImageController.getInstance().tryGet("/AAtank1.png");
        this.image2=ImageController.getInstance().tryGet("/AAtank1B.png");
        hpLimit=300;//血量上限
        hp=hpLimit; //初始血量
        atk=250; //攻擊力
        atkSpeed = new Delay(120);      //攻速
        speed=2.2f; //移動速度
        def=0.5; //防禦力
        atkdis=300; //攻擊距離
        this.isEnemy=isEnemy; //敵我單位
        cost=250; //一隻250元
    }

    @Override
    public Global.ActorType getType() {
        return Global.ActorType.TANK1;
    }

    @Override
    public void paint(Graphics g) {
        if(isInControl){ //被控制時要畫藍色的圖
            g.drawImage(image2,(int)this.painter().left(),(int)this.painter().top(),(int)this.painter().right(),(int)this.painter().bottom(),
                    this.cannonDirection.getValue()%3*75,cannonDirection.getValue()/3*90,
                    this.cannonDirection.getValue()%3*75+75,cannonDirection.getValue()/3*90+90,null);
        }
    else{
        g.drawImage(image,(int)this.painter().left(),(int)this.painter().top(),(int)this.painter().right(),(int)this.painter().bottom(),
                this.cannonDirection.getValue()%3*75,cannonDirection.getValue()/3*90,
                this.cannonDirection.getValue()%3*75+75,cannonDirection.getValue()/3*90+90,null);
        }

        for(int i=0;i<this.bullets.size();i++){
            this.bullets.get(i).paint(g);
        }
    }


}
