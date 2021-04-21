package gameobj;

import controllers.AudioResourceController;
import controllers.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Boss extends Actor{
    private float chargeAtk; //衝鋒攻擊傷害
    private float chargeSpeed;//衝鋒速度
    private int skillNum=0; //招式switch
    private Delay delay; //隨機選招的Delay
    private Delay delayCharge; //音效的delay
    public float tempX; //儲存上一動的位置
    public float tempY; //儲存上一動的位置
    private boolean isCharge;//假如使用了衝鋒攻擊後，就馬上改成普通攻擊
    private BufferedImage debuff;//被deBuff的圖

    //三角飛彈-->普通攻擊
    public Boss(float x,float y,boolean isEnemy){
        super(x,y,80,150,x,y,150,180);
        this.image= ImageController.getInstance().tryGet("/AE-Boss.png");
        this.image2=ImageController.getInstance().tryGet("/AE-Boss_S1.png");
        this.debuff=ImageController.getInstance().tryGet("/AE-Boss_S6.png");
        hpLimit=6000;//血量上限
        hp=hpLimit; //血量
        atk=100; //攻擊力
        this.chargeAtk=1;//衝鋒攻擊傷害 每秒扣60
        atkSpeed=new Delay(210); //攻速
        this.atkSpeedLimit=atkSpeed.getCountLimit();
        chargeSpeed=6;//衝鋒速度
        speed=1.2f; //移動速度
        def=0.55; //防禦力
        atkdis= 250; //攻擊距離
        this.isEnemy=isEnemy; //敵我單位
        this.skillNum=0; //剛開始使用普通攻擊
        this.delay=new Delay(240);
        this.tempX=this.painter().centerX();
        this.tempY=this.painter().centerY();
        delayCharge=new Delay(60);
        delayCharge.loop();
    }

    //衝撞直線上的敵人，隨機向戰場底部衝鋒，然後回到原位，並且對路徑上碰撞的敵人造成傷害
    //開火
    public void charge (ArrayList<Actor> actors){
        //衝鋒攻擊，並且對路徑上敵軍造成傷害
        for(int i=0;i<actors.size();i++){
            if(actors.get(i).isCollision(this)){
                actors.get(i).offsetHp(-chargeAtk);
            }
        }
    }
    @Override
    //選最短距離者追蹤並攻擊，敵方死亡後回到原位
    public void autoAttack (ArrayList< Actor > actors, ArrayList < Actor > alliance){ //傳敵軍陣列近來
        if (atkSpeed.isPause()) {
            atkSpeed.loop();
        }
         //不是衝鋒攻擊的時候，才要延時，否則就直接換
            delay.play();
            if (delay.count()) {
                skillNum = Global.random(0, 1);
            }

        switch (skillNum){
            case 0:  //普通攻擊
                this.isCharge=false;
                if (actors.size() > 0) {
                    //先一一算出最短距離，存進數字陣列中找出最近的敵人 = target
                    float a ;
                    float b ;
                    float d ;
                    float mind = Integer.MAX_VALUE;
                    Rect target = null;
                    for (int i = 0; i < actors.size(); i++) {
                        a = Math.abs(this.painter().centerX() - actors.get(i).painter().centerX());
                        b = Math.abs(this.painter().centerY() - actors.get(i).painter().centerY());
                        d = (float) Math.sqrt(a * a + b * b);
                        if (d < mind) { //最短距離者 ，取他的XY值
                            mind = d;
                            target = actors.get(i).collider();
                        }
                    }
                    //移動至攻擊範圍內則開火
                    float targetX = target.centerX();
                    float targetY = target.centerY();
                    if (isInAtkdis(targetX,targetY)) {
                        fire(targetX,targetY);
                    }else{
                        move(targetX,targetY,alliance);
                    }
                }
                break;
            case 1: //隨機衝鋒攻擊，要回到原本的位置
                this.isCharge=true;
                if (actors.size() > 0) {
                    //先一一算出最長距離，存進數字陣列中找出最近的敵人 = target
                    float a ;
                    float b ;
                    float d ;
                    float max = Integer.MIN_VALUE;
                    Rect target = null;
                    for (int i = 0; i <alliance.size(); i++) {
                        a = Math.abs(this.painter().centerX() - actors.get(i).painter().centerX());   //這裡會IndexOutOfBounds Index1 size1
                        b = Math.abs(this.painter().centerY() - actors.get(i).painter().centerY());
                        d = (float) Math.sqrt(a * a + b * b);
                        if (d > max) { //最長距離者 ，取他的XY值
                            max = d;
                            target = actors.get(i).collider();
                        }
                    }
                    final float targetX = target.centerX();
                    final float targetY = target.centerY();
                    move(targetX,targetY,alliance);
                    delayCharge.play();
                    charge(actors);
                    if(delayCharge.count()){
                        AudioResourceController.getInstance().shot("/AE-BossSound2.wav");
                    }
                }
                break;
        }
    }
    //開火
    @Override
    public void fire (float x,float y){
        Bullet bullet = new BossHornBullet(this.painter().centerX(), this.painter().centerY(), x, y);
//        根據角度變換砲管方向的狀態
        int atkAngle = bullet.trigonometric.getDegree() + 270;
        if (atkAngle < 60) {
            this.cannonDirection = CANNON_DIRECTION.FrontLeft;
        } else if (atkAngle <= 120) {
            this.cannonDirection = CANNON_DIRECTION.FrontMiddle;
        } else if (atkAngle <= 180) {
            this.cannonDirection = CANNON_DIRECTION.FrontRight;
        } else if (atkAngle < 240) {
            this.cannonDirection = CANNON_DIRECTION.BackRight;
        } else if (atkAngle <= 300) {
            this.cannonDirection = CANNON_DIRECTION.BackMiddle;
        } else if (atkAngle < 360) {
            this.cannonDirection = CANNON_DIRECTION.BackLeft;
        }
    }
    @Override
    public Global.ActorType getType() {
        return Global.ActorType.BOSS;
    }

    @Override
    public void paintComponent(Graphics g) {
        if(isCharge){
            g.drawImage(image2, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                    this.cannonDirection.getValue() / 3 * 150, 0,
                    this.cannonDirection.getValue() / 3 * 150 + 150, 180, null);
        }else  if(isOnDebuff){
            g.drawImage(debuff,(int)this.painter().left(),(int)this.painter().top(),(int)this.painter().right(),(int)this.painter().bottom(),
                    this.cannonDirection.getValue()/3*150,0,
                    this.cannonDirection.getValue()/3*150+150,180,null);
        }else {
            g.drawImage(image, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                    this.cannonDirection.getValue() / 3 * 150, 0,
                    this.cannonDirection.getValue() / 3 * 150 + 150, 180, null);
        }
        //血條
        g.drawImage(image_hp,(int)this.painter().left(),(int)this.painter().bottom(),(int)(this.painter().left()+(7.5F*(this.hp*10/hpLimit))),(int)this.painter().bottom()+11,
                0,0,75,11,null);

        for(int i=0;i<this.bullets.size();i++){
            this.bullets.get(i).paint(g);
        }

    }

}
