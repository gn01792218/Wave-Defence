package gameobj;

import controllers.ImageController;
import utils.Delay;

import java.awt.*;
import java.util.ArrayList;

public class Tank1 extends Actor{


    public Tank1(int x, int y,boolean isenemy){
        super(x,y,75,90);
        this.image= ImageController.getInstance().tryGet("/tank1.png");
        hp=200; //血量
        atk=100; //攻擊力
        atkSpeed = new Delay(60); //攻速
        speed=3; //移動速度
        def=100; //防禦力
        atkdis=150; //攻擊距離
        this.isenemy=isenemy; //敵我單位
    }

    @Override
    public void paint(Graphics g) { //5*2
        if(this.isenemy==false){
        g.drawImage(image,(int)this.painter().left(),(int)this.painter().top(),(int)this.painter().right(),(int)this.painter().bottom(),
                160,90,160+75,180,null);
        }else if(this.isenemy==true){
            g.drawImage(image,(int)this.painter().left(),(int)this.painter().top(),(int)this.painter().right(),(int)this.painter().bottom(),
                    160,0,160+75,90,null);
        }
        for(int i=0;i<this.bullets.size();i++){
            this.bullets.get(i).paint(g);
        }
    }

    @Override
    public void update() {
        for(int i=0;i<this.bullets.size();i++){
            this.bullets.get(i).update();
        }

    }


}
