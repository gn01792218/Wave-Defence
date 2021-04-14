package gameobj;

import controllers.ImageController;
import controllers.ImageOperate;
import utils.Delay;
import utils.Global;

import java.awt.*;

public class Enemy1 extends Actor{
    public Enemy1(float x,float y,boolean isenemy){
        super(x,y,75,90);
        this.image= ImageController.getInstance().tryGet("/enemy1.png");
        hp=250; //血量
        atk=70; //攻擊力
        atkSpeed=new Delay(100); //功速
        speed=2.5f; //移動速度
        def=0.3; //防禦力
        atkdis=300; //攻擊距離
        this.isenemy=isenemy; //敵我單位
    }

    @Override
    public Global.ActorType getType() {
        return Global.ActorType.ENEMY1;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image,(int)this.painter().left(),(int)this.painter().top(),(int)this.painter().right(),(int)this.painter().bottom(),
                0,0,75,90,null);
        for(int i=0;i<this.bullets.size();i++){ //畫子彈
            this.bullets.get(i).paint(g);
        }
    }
}
