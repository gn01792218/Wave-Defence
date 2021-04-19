package gameobj;

import controllers.ImageController;
import controllers.ImageOperate;
import utils.Delay;
import utils.Global;

import java.awt.*;

public class Enemy1 extends Actor{
    public Enemy1(float x,float y,boolean isEnemy){
        super(x,y,60,72,x,y,75,90);
        this.image= ImageController.getInstance().tryGet("/enemy1.png");
        hpLimit=300;//血量上限
        hp=hpLimit; //血量
        atk=100; //攻擊力
        atkSpeed=new Delay(80); //攻速
        speed=3; //移動速度
        def=0.3; //防禦力
        atkdis=80; //攻擊距離
        this.isEnemy=isEnemy; //敵我單位
    }

    @Override
    public Global.ActorType getType() {
        return Global.ActorType.ENEMY1;
    }

    public void paintComponent(Graphics g){
        g.drawImage(image,(int)this.painter().left(),(int)this.painter().top(),(int)this.painter().right(),(int)this.painter().bottom(),
                this.cannonDirection.getValue()/3*75,0,
                this.cannonDirection.getValue()/3*75+75,90,null);
        g.drawImage(image_hp,(int)this.painter().left(),(int)this.painter().bottom(),(int)(this.painter().left()+(6.5F*(this.hp*10/hpLimit))),(int)this.painter().bottom()+13,
                65,13,0,0,null);

        for(int i=0;i<this.bullets.size();i++){
            this.bullets.get(i).paint(g);
        }
    }
}
