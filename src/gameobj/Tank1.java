package gameobj;

import controllers.ImageController;
import utils.Delay;

import java.awt.*;

public class Tank1 extends Actor{

    public Tank1(float x, float y,boolean isenemy){
        super(x,y,75,90);
        this.image= ImageController.getInstance().tryGet("/tank1.png");
        hp=300; //血量
        atk=250; //攻擊力
        atkSpeed = new Delay(120);      //攻速
        speed=(float)2.2; //移動速度
        def=0.5; //防禦力
        atkdis=300; //攻擊距離
        this.isenemy=isenemy; //敵我單位
    }
    @Override
    public void paint(Graphics g) {
        int moveDir;
        if(!this.isenemy){
            moveDir = 90;
        }else {
            moveDir = 0;
        }

        g.drawImage(image,(int)this.painter().left(),(int)this.painter().top(),(int)this.painter().right(),(int)this.painter().bottom(),
                this.attackDirection.getValue()*75,moveDir,this.attackDirection.getValue()*75+75,moveDir+90,null);

        for(int i=0;i<this.bullets.size();i++){
            this.bullets.get(i).paint(g);
        }
    }
}
