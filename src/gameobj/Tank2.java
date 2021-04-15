package gameobj;

import controllers.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;

public class Tank2 extends Actor{

    public Tank2(float x, float y,boolean isenemy){
        super(x,y,75,90);
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

    @Override
    public void paint(Graphics g) {
        g.drawImage(image,(int)this.painter().centerX(),(int)this.painter().centerY(),null); //畫自己
        for(int i=0;i<this.bullets.size();i++){ //畫子彈
            this.bullets.get(i).paint(g);
        }
    }

}
