package gameobj;

import controllers.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;

public class Castle extends Actor{


    public Castle(float x, float y) {
        super(x, y, 300, 150, x, y, 1, 270);
        hpLimit=1000;//血量上限
        hp=hpLimit; //初始血量
        atk=550; //攻擊力
        atkSpeed = new Delay(240);      //攻速
        this.atkSpeedLimit=atkSpeed.getCountLimit();
        def=0.6f; //防禦力
        atkdis=250; //攻擊距離
        this.isEnemy=false; //敵我單位
        atkSpeed.loop();
        this.image_hp= ImageController.getInstance().tryGet("/bloodB.png"); //血條大家都一樣
    }

    @Override
    public Global.ActorType getType() {
        return null;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(image_hp,(int)this.painter().left(),(int)this.painter().bottom(),(int)(this.painter().left()+(7.5F*(this.hp*10/hpLimit))*4),(int)this.painter().bottom()+25,
                0,0,75,11,null);

        for(int i=0;i<this.bullets.size();i++){
            this.bullets.get(i).paint(g);
        }
    }
}
