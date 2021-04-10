package gameobj;

import controllers.ImageController;
import controllers.ImageOperate;
import utils.Delay;

import java.awt.*;

public class Enemy1 extends Actor{
    public Enemy1(int x,int y,boolean isenemy){
        super(x,y,75,90);
        this.image= ImageController.getInstance().tryGet("/enemy1.png");
        hp=100; //血量
        atk=50; //攻擊力
        atkSpeed=new Delay(60); //功速
        speed=10; //移動速度
        def=50; //防禦力
        atkdis=100; //攻擊距離
        this.isenemy=isenemy; //敵我單位
    }
    @Override
    public void paint(Graphics g) {
        g.drawImage(image,(int)this.painter().left(),(int)this.painter().top(),(int)this.painter().right(),(int)this.painter().bottom(),
                0,0,75,90,null);
        for(int i=0;i<this.bullets.size();i++){ //畫子彈
            this.bullets.get(i).paint(g);
        }

    }

    @Override
    public void update() {
        for(int i=0;i<this.bullets.size();i++){
            this.bullets.get(i).update(); //子彈更新
        }
    }
}
