package gameobj;

import controllers.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;
import java.util.ArrayList;

//會專門攻擊遠距離單位!!!
public class Enemy2 extends Actor{
    public Enemy2(float x,float y,boolean isEnemy){
        super(x,y,60,72,x,y,75,90);
        this.image= ImageController.getInstance().tryGet("/AE-Enemy2.png");
        this.image2=ImageController.getInstance().tryGet("/AE-Enemy2_S6.png");
        this.image_hp= ImageController.getInstance().tryGet("/Blood3.png"); //血條大家都一樣
        hpLimit=450;//血量上限
        hp=hpLimit; //血量
        atk=100; //攻擊力
        atkDefault=atk;
        atkSpeed=new Delay(45); //攻速
        atkSpeed.loop();
        this.atkSpeedLimit=atkSpeed.getCountLimit();
        speed=4.5f; //移動速度
        def=0.4f; //防禦力
        atkdis= 150; //攻擊距離
        this.isEnemy=isEnemy; //敵我單位
        atkSpeed.loop();
    }
    @Override
    public void autoAttack (ArrayList< Actor > actors, ArrayList < Actor > alliance){ //傳敵軍陣列近來
        if (atkSpeed.isPause()) {
            atkSpeed.loop();
        }
        if (actors.size() > 0) {
            //先一一算出最短距離，存進數字陣列中找出最近的敵人 = target
            float a ;
            float b ;
            float d ;
            float max = Integer.MIN_VALUE;
            Rect target = null;
            for (int i = 0; i < actors.size(); i++) {
                a = Math.abs(this.painter().centerX() - actors.get(i).painter().centerX());
                b = Math.abs(this.painter().centerY() - actors.get(i).painter().centerY());
                d = (float) Math.sqrt(a * a + b * b);
                if (d > max) { //最短距離者 ，取他的XY值
                    max = d;
                    target = actors.get(i).collider();
                }
            }
            //移動至攻擊範圍內則開火
            float targetX = target.centerX();
            float targetY = target.centerY();

            if (isInAtkdis(targetX,targetY)) {
                fire(targetX,targetY);
            } else {
                move(targetX,targetY,alliance);
            }
        }
        if (actors.size() <= 0 && !this.isEnemy) {
            //回到自己原本的位置並導正砲管
            if (collider().centerX() == flag.collider().centerX() && collider().centerY() == flag.collider().centerY()) {
                if (atkSpeed.getCount() < 119) {
                    atkSpeed.count();
                }
            } else {
                move(flag.collider().centerX(),flag.collider().centerY(), alliance);
            }
//                this.cannonDirection = CANNON_DIRECTION.FrontMiddle;
        }
    }
    @Override
    public Global.ActorType getType() {
        return Global.ActorType.ENEMY2;
    }
    @Override
    public void paintComponent(Graphics g) {
        if(isOnDebuff){
            g.drawImage(image2,(int)this.painter().left(),(int)this.painter().top(),(int)this.painter().right(),(int)this.painter().bottom(),
                    this.cannonDirection.getValue()/3*75,0,
                    this.cannonDirection.getValue()/3*75+75,90,null);
        }else {
            g.drawImage(image, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                    this.cannonDirection.getValue() / 3 * 75, 0,
                    this.cannonDirection.getValue() / 3 * 75 + 75, 90, null);
        }
        g.drawImage(image_hp,(int)this.painter().left(),(int)this.painter().bottom(),(int)(this.painter().left()+(7.5F*(this.hp*10/hpLimit))),(int)this.painter().bottom()+11,
                0,0,75,11,null);

        for(int i=0;i<this.bullets.size();i++){
            this.bullets.get(i).paint(g);
        }
    }
}
