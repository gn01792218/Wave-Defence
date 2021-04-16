package gameobj;

import controllers.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;

public class Tank2 extends Actor{

    public Tank2(float x, float y,boolean isenemy){
        super(x,y,60,72,x,y,75,90);
        this.image= ImageController.getInstance().tryGet("/tank2.png");
        hpLimit=400;//血量上限
        hp=400; //血量
        atk=150; //攻擊力
        atkSpeed = new Delay(60);      //攻速
        speed=3.3f; //移動速度
        def=0.45; //防禦力
        atkdis=150; //攻擊距離
        cost=300; //一隻300
        this.isEnemy=isenemy; //敵我單位
    }

    @Override
    public Global.ActorType getType() {
        return Global.ActorType.TANK2;
    }

    public void paintComponent(Graphics g){
        g.drawImage(image,(int)this.painter().left(),(int)this.painter().top(),(int)this.painter().right(),(int)this.painter().bottom(),
                this.cannonDirection.getValue()/3*75,0,
                this.cannonDirection.getValue()/3*75+75,90,null);

        for(int i=0;i<this.bullets.size();i++){
            this.bullets.get(i).paint(g);
        }
    }
}
