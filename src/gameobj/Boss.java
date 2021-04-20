package gameobj;

import controllers.AudioResourceController;
import controllers.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;
import java.util.ArrayList;

public class Boss extends Actor{
    private float chargeAtk; //衝鋒攻擊傷害
    private float chargeSpeed;//衝鋒速度
    private int skillNum=0; //招式switch
    private Delay delay; //隨機選招的Delay
    private Delay delayCharge;
    public float tempX; //儲存上一動的位置
    public float tempY; //儲存上一動的位置
    private boolean isCharge;//假如使用了衝鋒攻擊後，就馬上改成普通攻擊

    //三角飛彈-->普通攻擊
    public Boss(float x,float y,boolean isEnemy){
        super(x,y,80,150,x,y,150,180);
        this.image= ImageController.getInstance().tryGet("/AE-Boss.png");
        hpLimit=6000;//血量上限
        hp=hpLimit; //血量
        atk=100; //攻擊力
        this.chargeAtk=1;//衝鋒攻擊傷害 每秒扣60
        atkSpeed=new Delay(210); //攻速
        chargeSpeed=6;//衝鋒速度
        speed=1.2f; //移動速度
        def=0.55; //防禦力
        atkdis= 250; //攻擊距離
        this.isEnemy=isEnemy; //敵我單位
        this.skillNum=0; //剛開始使用普通攻擊
        this.delay=new Delay(240);
        this.tempX=this.painter().centerX();
        this.tempY=this.painter().centerY();
        delayCharge=new Delay(30);
    }

    //衝撞直線上的敵人，隨機向戰場底部衝鋒，然後回到原位，並且對路徑上碰撞的敵人造成傷害
    //開火
    public void charge (ArrayList<Actor> actors){
        //衝鋒攻擊最遠單位，並且對路徑上敵軍造成傷害
        for(int i=0;i<actors.size();i++){
            if(actors.get(i).isCollision(this)){
                actors.get(i).offsetHp(-chargeAtk);
            }
        }
    }
    @Override
    //朝目標移動
    public void move(float x ,float y, ArrayList<Actor> alliance) {
        //根據目標位置移動
        Global.Trigonometric trigonometric = new Global.Trigonometric(this.collider().centerX(), this.collider().centerY(),
                x,y);
        int atkAngle = trigonometric.getDegree() + 270;
        if (atkAngle <= 180) {
            this.cannonDirection = CANNON_DIRECTION.FrontMiddle;
        } else {
            this.cannonDirection = CANNON_DIRECTION.BackMiddle;
        }
        if (targetIsInBattleField(x, y)) {
            //角色的translate根據x/y的斜率來走
            float a = Math.abs(painter().centerX() - x);//x座標差值 對邊
            float b = Math.abs(painter().centerY() - y); //y座標差值 臨邊
            float d = (float) Math.sqrt(a * a + b * b); //斜邊
            //當d的距離大於10時才執行，所以會在距離敵軍100的地方停下來
            //但需要解決和我軍重疊的問題
            if ((this.collider().centerX()<x-speed || this.collider().centerX()>x+speed) ||
                    (this.collider().centerY()<y-speed || this.collider().centerY()>y+speed)) {  //大於0會精準回到原點，且所有人會重疊，亦可能顫抖  ；大於自己的攻擊距離會回到原點+攻擊距離的位置。值不能大於所有角色中射程最短的角色(否則他會無法發射子彈)
                float xM = a / d * speed; //x向量
                float yM = b / d * speed; //y向量

                boolean leftIsTouch = false;
                boolean rightIsTouch = false;
                boolean topIsTouch = false;
                boolean bottomIsTouch = false;

                //檢查跟友軍是否碰撞
                for (int i = 0; i < alliance.size(); i++) {
                    //排除自己
                    if (this.collider().centerX() == alliance.get(i).collider().centerX() &&
                            this.collider().centerY() == alliance.get(i).collider().centerY()) {
                        continue;
                    }

                    if (this.leftIsCollision(alliance.get(i))){
                        leftIsTouch = true;
                    }
                    if (this.rightIsCollision(alliance.get(i))){
                        rightIsTouch = true;
                    }
                    if (this.bottomIsCollision(alliance.get(i))){
                        bottomIsTouch = true;
                    }
                    if (this.topIsCollision(alliance.get(i))) {
                        topIsTouch = true;
                    }
                }
                //假設只有一個方面沒有碰撞
                if(leftIsTouch && rightIsTouch && topIsTouch && bottomIsTouch){
                    offSet(0,0);
                    return;
                }else if(leftIsTouch && rightIsTouch && !topIsTouch && bottomIsTouch){
                    offSet(0,-speed);
                    return;
                }else if(leftIsTouch && !rightIsTouch && topIsTouch && bottomIsTouch){
                    offSet(speed,0);
                    return;
                }else if(!leftIsTouch && rightIsTouch && topIsTouch && bottomIsTouch){
                    offSet(-speed,0);
                    return;
                }else if(leftIsTouch && rightIsTouch && topIsTouch && !bottomIsTouch){
                    offSet(0,speed);
                    return;
                }

                if(leftIsTouch && rightIsTouch){
                    xM=0;
                    yM=speed;
                }else if(topIsTouch && rightIsTouch){
                    xM=speed;
                    yM=0;
                }else if(leftIsTouch && topIsTouch){
                    xM=0;
                    yM=speed;
                }else if(rightIsTouch && topIsTouch){
                    xM=0;
                    yM=speed;
                }else if(leftIsTouch && bottomIsTouch){
                    xM=speed;
                    yM=0;
                }else if(rightIsTouch && bottomIsTouch){
                    xM=speed;
                    yM=0;
                }else if(topIsTouch && y<this.collider().centerY()){
                    xM=speed;
                    yM=0;
                }else if(bottomIsTouch && y>this.collider().centerY()){
                    xM=speed;
                    y=0;
                }else if(leftIsTouch && x<this.collider().centerX()){
                    xM=0;
                    y=speed;
                }else if(rightIsTouch && x>this.collider().centerX()){
                    xM=0;
                    y=speed;
                }

                if(x<this.collider().centerX()){
                    xM = -xM;
                }
                if(y<this.collider().centerY()){
                    yM = -yM;
                }
                this.offSet(xM,yM);
            }
        }
    }
    @Override
    //朝目標移動
    public void moveToTarget ( float x, float y){
        if (targetIsInBattleField(x, y)) {
            //角色的translate根據x/y的斜率來走
            float a = Math.abs(painter().centerX() - x);//x座標差值 對邊
            float b = Math.abs(painter().centerY() - y); //y座標差值 臨邊
            float d = (float) Math.sqrt(a * a + b * b); //斜邊
            //當d的距離大於10時才執行，所以會在距離敵軍100的地方停下來
            //但需要解決和我軍重疊的問題
            if (d > 60) {  //大於0會精準回到原點，且所有人會重疊，亦可能顫抖  ；大於自己的攻擊距離會回到原點+攻擊距離的位置。值不能大於所有角色中射程最短的角色(否則他會無法發射子彈)
                float xM = (float) a / d * chargeSpeed;  //x向量
                float yM = (float) b / d * chargeSpeed; //y向量
                if (painter().centerX() > x) {
                    xM = -xM;
                }
                if (painter().centerY() > y) {
                    yM = -yM;
                }
                this.painter().offSet((int) xM, (int) yM);
                this.collider().offSet((int)xM,(int)yM);
            }
        }
    }
    @Override
    //選最短距離者追蹤並攻擊，敵方死亡後回到原位
    public void autoAttack (ArrayList< Actor > actors, ArrayList < Actor > alliance){ //傳敵軍陣列近來
        float tempx=this.painter().exactCenterX(); //儲存上一動的動作
        float tempy=this.painter().exactCenterY(); //儲存上一棟的動作
        System.out.println("起始位置"+tempx+" "+tempy);
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
                        tempx=this.painter().centerX();
                        tempy=this.painter().centerY();
                        System.out.println("上一棟座標"+tempx+" "+tempy);
                    }else{
                        move(targetX,targetY,alliance);
                    }
                }
                System.out.println("使用普通攻擊");
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
                        a = Math.abs(this.painter().centerX() - actors.get(i).painter().centerX());
                        b = Math.abs(this.painter().centerY() - actors.get(i).painter().centerY());
                        d = (float) Math.sqrt(a * a + b * b);
                        if (d > max) { //最長距離者 ，取他的XY值
                            max = d;
                            target = actors.get(i).collider();
                        }
                    }
                    final float targetX = target.centerX();
                    final float targetY = target.centerY();
                    moveToTarget(targetX,targetY);
                    System.out.println("移動到"+targetX+" "+targetY);
                    delayCharge.play();
                    charge(actors);
                    moveToTarget(tempx,tempy);
//                    if(delayCharge.count()) {
//                        moveToTarget(tempx,tempy);
//                        this.isCharge=false;
//                        System.out.println("返回位置" + tempx + " " + tempy);
//                    }
                }
                System.out.println("使用衝鋒攻擊");
                break;
        }
    }

    @Override
    public Global.ActorType getType() {
        return Global.ActorType.BOSS;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(image,(int)this.painter().left(),(int)this.painter().top(),(int)this.painter().right(),(int)this.painter().bottom(),
                this.cannonDirection.getValue()/3*150,0,
                this.cannonDirection.getValue()/3*150+150,180,null);
        //血條
        g.drawImage(image_hp,(int)this.painter().left(),(int)this.painter().bottom(),(int)(this.painter().left()+(7.5F*(this.hp*10/hpLimit))),(int)this.painter().bottom()+11,
                0,0,75,11,null);

        for(int i=0;i<this.bullets.size();i++){
            this.bullets.get(i).paint(g);
        }

    }

}
