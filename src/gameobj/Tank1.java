package gameobj;

import controllers.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;

public class Tank1 extends Actor{

    public Tank1(float x, float y,boolean isenemy){
        super(x,y,50,50,x,y,75,90);
        this.image= ImageController.getInstance().tryGet("/tank1.png");
        hpLimit=300;//血量上限
        hp=300; //目前血量
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

    public void paintComponent(Graphics g){
        g.drawImage(image,(int)this.painter().left(),(int)this.painter().top(),(int)this.painter().right(),(int)this.painter().bottom(),
                this.cannonDirection.getValue()%3*75,cannonDirection.getValue()/3*90,
                this.cannonDirection.getValue()%3*75+75,cannonDirection.getValue()/3*90+90,null);

        for(int i=0;i<this.bullets.size();i++){
            this.bullets.get(i).paint(g);
        }
    }

}
