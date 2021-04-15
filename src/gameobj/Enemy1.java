package gameobj;

import controllers.ImageController;
import controllers.ImageOperate;
import utils.Delay;

import java.awt.*;

public class Enemy1 extends Actor{
    public Enemy1(float x,float y,boolean isEnemy){
        super(x,y,75,90);
        this.image= ImageController.getInstance().tryGet("/enemy1.png");
        hp=250; //血量
        atk=70; //攻擊力
        atkSpeed=new Delay(120); //攻速
        speed=3; //移動速度
        def=0.3; //防禦力
        atkdis= 100; //攻擊距離
        this.isEnemy=isEnemy; //敵我單位
    }
    @Override
    public void paint(Graphics g) {
        g.drawImage(image,(int)this.collider().left(),(int)this.collider().top(),(int)this.collider().right(),(int)this.collider().bottom(),
                0,0,75,90,null);
        for(int i=0;i<this.bullets.size();i++){ //畫子彈
            this.bullets.get(i).paint(g);
        }
    }
}
