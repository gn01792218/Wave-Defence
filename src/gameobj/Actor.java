package gameobj;

import controllers.AudioResourceController;
import controllers.ImageController;
import utils.Delay;
import utils.Flag;
import utils.Global;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Actor extends GameObject {
    //設基本屬性的g s 並且offset
    protected BufferedImage image;
    protected BufferedImage image2;// 變色圖片
    protected BufferedImage image_hp;// 血條
    protected double hpLimit;
    protected double hp;
    protected double atk;
    protected Delay atkSpeed;
    protected float atkSpeedLimit;

    protected float speed;
    protected double def;
    protected double atkdis;
    protected boolean isEnemy; //標示此單位是敵是我
    protected boolean isAlive; //標示是否死亡
    protected boolean isInControl; //是否 被點選
    protected boolean isOnBuff;//是否是Buff狀態
    protected boolean isOnDebuff;//是否是減益狀態
    protected Flag flag;


    protected ArrayList<Bullet> bullets; //每個角色都有彈藥
    protected CANNON_DIRECTION cannonDirection;

    protected enum CANNON_DIRECTION {
        FrontLeft(3),
        FrontMiddle(4),
        FrontRight(5),
        BackLeft(0),
        BackMiddle(1),
        BackRight(2);
        private final int value;

        CANNON_DIRECTION(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    protected boolean isReinforcement;//是否是增援部隊
    protected Global.SkillName skillName;// 會顯示最後施放的招式特效

    public Actor(float x1, float y1, float width1, float height1,float x2, float y2, float width2, float height2) {
        super(x1, y1, width1, height1,x2, y2, width2, height2);
        bullets = new ArrayList<>();
        isAlive = true;
        flag = new Flag(x1,y1);
        //剛開始是起始位置，之後在場景中可以set成旗幟位置
        this.cannonDirection = CANNON_DIRECTION.FrontMiddle;
        isInControl = false;
        isReinforcement=false;
        this.isOnDebuff=false;
        this.image_hp= ImageController.getInstance().tryGet("/Blood3.png"); //血條大家都一樣
        flag=new Flag(x1,y1);
    }


    //基本方法區 get
    public double getHpLimit() {
        return hpLimit;
    }

    public float getAtkSpeedLimit() {
        return atkSpeedLimit;
    }

    public boolean isOnDebuff() {
        return isOnDebuff;
    }

    public double getHp() {
        return this.hp;
    }

    public double getAtk() {
        return atk;
    }

    public Delay getAtkSpeed() {
        return atkSpeed;
    }

    public double getSpeed() {
        return this.speed;
    }

    public double getDef() {
        return def;
    }

    public double getAtkdis() {
        return this.atkdis;
    }

    public boolean getIsInControl() {
        return isInControl;
    }

    public Flag getFlag() {
        return flag;
    }

    public boolean isReinforcement() {
        return isReinforcement;
    }

    public boolean isOnBuff() {
        return isOnBuff;
    }

    //set方法

    public void setAtkSpeedLimit(float atkSpeedLimit) {
        this.atkSpeedLimit = atkSpeedLimit;
    }

    public void setStrategyXY(float x, float y) {//傳入座標點(flag旗幟，或是出生的起始位置等等來設定要他固守的位置)
        flag.setCenter(x,y);
    }

    public void setOnDebuff(boolean onDebuff) {
        isOnDebuff = onDebuff;
    }

    public void setHpLimit(float hpLimit) {
        this.hpLimit = hpLimit;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public void setAtk(double atk) {
        this.atk = atk;
    }

    public void setAtkSpeed(Delay atkSpeed) {
        this.atkSpeed = atkSpeed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setDef(double def) {
        this.def = def;
    }

    public void setAtkdis(double atkdis) {
        this.atkdis = atkdis;
    }

    public void setCenter(float x, float y) {
        this.painter().setCenter(x, y);
        this.collider().setCenter(x, y);
    }

    public void setControl(boolean cliced) {
        isInControl = cliced;
    }

    public void setReinforcement(boolean reinforcement) {
        isReinforcement = reinforcement;
    }

    public void setSkillName(Global.SkillName skillName) {
        this.skillName = skillName;
    }

    public void setOnBuff(boolean onBuff) {
        isOnBuff = onBuff;
    }

    //offset方法區
    public void offsetHp(double hp) {
        this.hp += hp;
    }

    public void offsetAtk(double atk) {
        this.atk += atk;
    }

    public void offsetAtkSpeed(int atkSpeed) {
        this.atkSpeed = new Delay(atkSpeed);
    }

    public void offsetSpeed(double speed) {
        this.speed += speed;
    }

    public void offsetDef(double def) {
        this.def += def;
    }

    public void offsetAtkdis(double atkdis) {
        this.atkdis += atkdis;
    }

    //核心方法區-->子類實現
    //是否被點擊到
    public boolean isTouch(float x, float y) {
        if (x <= painter().left()) {
            return false;
        }
        if (x >= painter().right()) {
            return false;
        }
        if (y <= painter().top()) {
            return false;
        }
        if (y >= painter().bottom()) {
            return false;
        }
        //否則就是被點到了
        return true;
    }

    //獲得種類的方法，回傳Global的enum
    public abstract Global.ActorType getType();

    public void move(float x ,float y, ArrayList<Actor> alliance) {
        //根據目標位置移動
        Global.Trigonometric trigonometric = new Global.Trigonometric(this.collider().centerX(), this.collider().centerY(),
                x, y);
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
            if ((this.collider().centerX() < x - speed || this.collider().centerX() > x + speed) ||
                    (this.collider().centerY() < y - speed || this.collider().centerY() > y + speed)) {  //大於0會精準回到原點，且所有人會重疊，亦可能顫抖  ；大於自己的攻擊距離會回到原點+攻擊距離的位置。值不能大於所有角色中射程最短的角色(否則他會無法發射子彈)
                float xM = a / d * speed; //x向量
                float yM = b / d * speed; //y向量

                if (x < this.collider().centerX()) {
                    xM =-xM;
                }
                if (y < this.collider().centerY()) {
                    yM =-yM;
                }
                boolean leftIsTouch = false;
                boolean rightIsTouch = false;
                boolean topIsTouch = false;
                boolean bottomIsTouch = false;
                Rect left = null;
                Rect right = null;
                Rect top = null;
                Rect bottom = null;

                for(int i=0;i<alliance.size();i++) {
                    if (this.collider().centerX() == alliance.get(i).collider().centerX() &&
                            this.collider().centerY() == alliance.get(i).collider().centerY()) {
                        continue;
                    } else if (this.offsetRect(this.speed, 0, alliance.get(i).collider())) {
                        rightIsTouch = true;
                        right = new Rect(alliance.get(i).collider());
                    } else if (this.offsetRect(-this.speed, 0, alliance.get(i).collider())) {
                        leftIsTouch = true;
                        left = new Rect(alliance.get(i).collider());
                    } else if (this.offsetRect(0, this.speed, alliance.get(i).collider())) {
                        bottomIsTouch = true;
                        bottom = new Rect(alliance.get(i).collider());
                    } else if (this.offsetRect(0, -this.speed, alliance.get(i).collider())){
                        topIsTouch = true;
                        top = new Rect(alliance.get(i).collider());
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
                    if(xM>0){
                        if(y>right.centerY()){
                            yM=speed;
                        }else {
                            yM=-speed;
                        }
                    }else{
                        if(y>left.centerY()){
                            yM=speed;
                        }else {
                            yM=-speed;
                        }
                    }
                    xM=0;
                    System.out.println("eftIsTouch && rightIsTouch");
                }else if(topIsTouch && rightIsTouch){
                    if(xM<0){
                        xM=-speed;
                        yM=0;
                    }else{
                        xM=0;
                        yM=-speed;
                    }
                    System.out.println("topIsTouch && rightIsTouch");
                }else if(topIsTouch && leftIsTouch){
                    if(xM>0){
                        xM=speed;
                        yM=0;
                    }else{
                        xM=0;
                        yM=-speed;
                    }
                    System.out.println("leftIsTouch && topIsTouch");
                }else if(topIsTouch && bottomIsTouch){
                    if(xM>0){
                        xM=speed;
                    }else {
                        xM=-speed;
                    }
                    yM=0;
                    System.out.println("rightIsTouch && topIsTouch");
                }else if(leftIsTouch && bottomIsTouch){
                    if(xM>0){
                        if(yM>0){
                            xM=speed;
                            yM=0;
                        }
                    }else{
                        xM=0;
                        yM=-speed;
                    }

                }else if(rightIsTouch && bottomIsTouch){
                    if(xM<0){
                        if(yM>0){
                            xM=-speed;
                            yM=0;
                        }
                    }else{
                        xM=0;
                        yM=speed;
                    }

                }else if(topIsTouch){
                    if(x>top.centerX()){
                        xM=speed;
                    }else {
                        xM=-speed;
                    }
                    yM=0;

                }else if(bottomIsTouch){
                    if(x>bottom.centerX()){
                        xM=speed;
                    }else {
                        xM=-speed;
                    }
                    yM=0;

                }else if(leftIsTouch){
                    if(y>left.centerY()){
                        yM=speed;
                    }else {
                        yM=-speed;
                    }
                    xM=0;
                }else if(rightIsTouch){
                    if(y>right.centerY()){
                        yM=speed;
                    }else {
                        yM=-speed;
                    }
                    xM=0;
                }
                if(!leftIsTouch &&!rightIsTouch &&!topIsTouch &&!bottomIsTouch){

                }
                this.offSet(xM,yM);
            }
        }
    }
        //朝目標移動
        public void moveToTarget ( float x, float y){
            if (targetIsInBattleField(x, y)) {
                //角色的translate根據x/y的斜率來走
                float a = Math.abs(painter().centerX() - x);//x座標差值 對邊
                float b = Math.abs(painter().centerY() - y); //y座標差值 臨邊
                float d = (float) Math.sqrt(a * a + b * b); //斜邊
                //當d的距離大於10時才執行，所以會在距離敵軍100的地方停下來
                //但需要解決和我軍重疊的問題
                if (d > this.getAtkdis() - (this.getAtkdis() * 0.5)) {  //大於0會精準回到原點，且所有人會重疊，亦可能顫抖  ；大於自己的攻擊距離會回到原點+攻擊距離的位置。值不能大於所有角色中射程最短的角色(否則他會無法發射子彈)
                    float xM = (float) a / d * speed;  //x向量
                    float yM = (float) b / d * speed; //y向量
                    if (painter().centerX() > x) {
                        xM = -xM;
                    }
                    if (painter().centerY() > y) {
                        yM = -yM;
                    }
                    this.painter().offSet((int) xM, (int) yM);
                    this.collider().offSet((int) xM, (int) yM);
                }
            }
        }
        //選最短距離者追蹤並攻擊，敵方死亡後回到原位
        public void autoAttack (ArrayList < Actor > actors, ArrayList < Actor > alliance){ //傳敵軍陣列近來
        if(this.isOnDebuff && this.skillName== Global.SkillName.ELECTWAVE){
            return; //中deBuff，且是ELECTWAVE時，就直接停止移動和攻擊
        }
            if (atkSpeed.isPause()) {
                atkSpeed.loop();
            }
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
        //開火
        public void fire (float x,float y){
            Bullet bullet = new Bullet(this.painter().centerX(), this.painter().centerY(), x, y);
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
            if (atkSpeed.count()) {
                bullets.add(bullet);
                AudioResourceController.getInstance().shot("/T.wav");
            }
        }
        //子彈更新
        public void bulletsUpdate (ArrayList < Actor > actors) {
            ////飛彈爆炸後一定時間後消失
            for (int i = 0; i < this.bullets.size(); i++) {
                this.bullets.get(i).update(); //子彈移動
                if (bullets.get(i).isExplored()) {
                    if (bullets.get(i).isTime()) {
                        bullets.remove(i);
                        i--;
                    }
                }else if(isTouchBattleEdge(bullets.get(i).collider().centerX(),bullets.get(i).collider().centerY())){
                        bullets.get(i).explored();
                        AudioResourceController.getInstance().shot("/explosion.wav");
                }else {
                    //攻擊敵機並扣血
                    for (int j = 0; j < actors.size(); j++) {
                        if (bullets.get(i).isCollision(actors.get(j))) {
                            bullets.get(i).explored();
                            AudioResourceController.getInstance().shot("/explosion.wav");
                            actors.get(j).offsetHp(-(this.atk) * (1 - actors.get(j).def));
                        }
                    }
                }
            }
        }

    //是否碰到場地邊界
//    public boolean isTouchBattleEdge(){
//        if(this.painter.centerX()<=Global.BOUNDARY_X1){return true;}
//        if(this.painter.centerX()>=Global.BOUNDARY_X2){return true;}
//        if(this.painter.centerY()<=Global.BOUNDARY_Y1){return true;}
//        if(this.painter.centerY()>=Global.BOUNDARY_Y2){return true;}
//        return false;
//    }

    public boolean isTouchBattleEdge(float x,float y){
        if(Global.isTouchX1(x,y)){return true;}
        if(Global.isTouchX2(x,y)){return true;}
        if(Global.isTouchY1(x,y)){return true;}
        if(Global.isTouchY2(x,y)){return true;}
        return false;
    }

        //判斷是否死亡
        public boolean isAlive() {
            return this.getHp() > 0;
        }
        //判斷目標在不在場內
        public boolean targetIsInBattleField ( float x, float y){
            if (x < Global.BOUNDARY_X1 || x > Global.BOUNDARY_X2) {
                return false;
            }
            if (y < Global.BOUNDARY_Y1 || y > Global.BOUNDARY_Y2) {
                return false;
            }
            return true;
        }
        //判斷目標是否在射程內
        public boolean isInAtkdis ( float x, float y){
            //判斷目標點是否在攻擊距離內:上下左右框框+atkdis
            if (x > this.painter().right() + getAtkdis()) {
                return false;
            }
            if (x < this.painter().left() - getAtkdis()) {
                return false;
            }
            if (y > this.painter().bottom() + getAtkdis()) {
                return false;
            }
            if (y < this.painter().top() - getAtkdis()) {
                return false;
            }
            return true;
        }
        public void update(){
        }
    }
