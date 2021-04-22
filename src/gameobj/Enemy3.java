package gameobj;

import controllers.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;

public class Enemy3 extends Actor{
    public Enemy3(float x,float y,boolean isEnemy){
        super(x,y,60,72,x,y,75,90);
        this.image= ImageController.getInstance().tryGet("/AE-Enemy3.png");
        this.image2=ImageController.getInstance().tryGet("/AE-Enemy3_S6.png");
        hpLimit=600;//血量上限
        hp=hpLimit; //血量
        atk=500; //攻擊力
        atkSpeed=new Delay(150); //攻速  2.5秒
        this.atkSpeedLimit=atkSpeed.getCountLimit();
        speed=1.5f; //移動速度
        def=0.5; //防禦力
        atkdis= 250; //攻擊距離
        this.isEnemy=isEnemy; //敵我單位
        atkSpeed.loop();
    }
    @Override
    public Global.ActorType getType() {
        return Global.ActorType.ENEMY3;
    }

    @Override
    public void paintComponent(Graphics g) {
        if(isOnDebuff){
            g.drawImage(image2,(int)this.painter().left(),(int)this.painter().top(),(int)this.painter().right(),(int)this.painter().bottom(),
                    this.cannonDirection.getValue()/3*85,0,
                    this.cannonDirection.getValue()/3*85+85,100,null);
        }else {
            g.drawImage(image, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                    this.cannonDirection.getValue() / 3 * 85, 0,
                    this.cannonDirection.getValue() / 3 * 85 + 85, 100, null);
        }
        g.drawImage(image_hp,(int)this.painter().left(),(int)this.painter().bottom(),(int)(this.painter().left()+(7.5F*(this.hp*10/hpLimit))),(int)this.painter().bottom()+11,
                0,0,75,11,null);
        for(int i=0;i<this.bullets.size();i++){
            this.bullets.get(i).paint(g);
        }

    }
}
