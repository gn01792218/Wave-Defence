package gameobj;

import controllers.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;

public class Enemy4 extends Actor{
    public Enemy4(float x,float y,boolean isEnemy){
        super(x,y,60,72,x,y,75,90);
        this.image= ImageController.getInstance().tryGet("/AE-Enemy4.png");
        this.image2=ImageController.getInstance().tryGet("/AE-Enemy4_S6.png");
        this.image_hp= ImageController.getInstance().tryGet("/Blood3.png"); //血條大家都一樣
        hpLimit=300;//血量上限
        hp=hpLimit; //血量
        atk=150; //攻擊力
        atkDefault=atk;
        atkSpeed=new Delay(45); //攻速 1.5秒
        atkSpeed.loop();
        this.atkSpeedLimit=atkSpeed.getCountLimit();
        speed=3f; //移動速度
        def=0.3f; //防禦力
        atkdis= 200; //攻擊距離
        this.isEnemy=isEnemy; //敵我單位
        atkSpeed.loop();
    }
    @Override
    public Global.ActorType getType() {
        return Global.ActorType.ENEMY4;
    }

    @Override
    public void paintComponent(Graphics g) {
        if(isOnDebuff){
            g.drawImage(image2,(int)this.painter().left(),(int)this.painter().top(),(int)this.painter().right(),(int)this.painter().bottom(),
                    this.cannonDirection.getValue()/3*85,0,
                    this.cannonDirection.getValue()/3*85+85,100,null);
        }else {
            g.drawImage(image, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                    this.cannonDirection.getValue() / 3 * 75, 0,
                    this.cannonDirection.getValue() / 3 * 75 + 75, 100, null);
        }
        g.drawImage(image_hp,(int)this.painter().left(),(int)this.painter().bottom(),(int)(this.painter().left()+(7.5F*(this.hp*10/hpLimit))),(int)this.painter().bottom()+11,
                0,0,75,11,null);
        for(int i=0;i<this.bullets.size();i++){
            this.bullets.get(i).paint(g);
        }

    }
}
