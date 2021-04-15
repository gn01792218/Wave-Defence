package gameobj;

import controllers.AudioResourceController;
import utils.Delay;
import utils.Global;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Actor extends GameObject {
    //設基本屬性的g s 並且offset
    protected BufferedImage image;
    protected BufferedImage image2;// 變色圖片
    protected double hpLimit;
    protected double hp;
    protected double atk;
    protected Delay atkSpeed;

    protected float speed;
    protected double def;
    protected double atkdis;
    protected boolean isEnemy; //標示此單位是敵是我
    protected boolean isAlive; //標示是否死亡
    protected boolean isInControl; //是否 被點選
    protected int cost; //每隻要花多少錢

    protected ArrayList<Bullet> bullets; //每個角色都有彈藥
    protected float strategyX;//我的戰略座標
    protected float strategyY;
    protected CANNON_DIRECTION cannonDirection;

    protected enum CANNON_DIRECTION {
        FrontLeft(3),
        FrontMiddle(4),
        FrontRight(5),
        BackLeft(0),
        BackMiddle(1),
        BackRight(2);
        private int value;

        private CANNON_DIRECTION(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public Actor(float x, float y, float width, float height) {
        super(x, y, width, height);
        bullets = new ArrayList<>();
        isAlive = true;
        strategyX = this.collider().centerX();
        strategyY = this.collider().centerY();
        //剛開始是起始位置，之後在場景中可以set成旗幟位置
        this.cannonDirection = CANNON_DIRECTION.FrontMiddle;
        isInControl = false;
        this.strategyX = x; //剛開始是起始位置，之後在場景中可以set成旗幟位置
        this.strategyY = y; //剛開始是起始位置，之後在場景中可以set成旗幟位置
    }


    //基本方法區 get
    public double getHpLimit() {
        return hpLimit;
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

    //set方法
    public void setFlagXY(float x, float y) {//傳入座標點(flag旗幟，或是出生的起始位置等等來設定要他固守的位置)
        strategyX = x;
        strategyY = y;
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

    //朝目標移動
    public void move(float x ,float y, ArrayList<Actor> actors) {
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
            if (d > this.getAtkdis() - (this.getAtkdis() * 0.5)) {  //大於0會精準回到原點，且所有人會重疊，亦可能顫抖  ；大於自己的攻擊距離會回到原點+攻擊距離的位置。值不能大於所有角色中射程最短的角色(否則他會無法發射子彈)
                float xM = (float) a / d * speed; //x向量
                float yM = (float) b / d * speed; //y向量

                boolean xArisIsNoTouch = true;
                boolean yArisIsNoTouch = true;

                //檢查跟友軍是否碰撞
                for (int i = 0; i < actors.size(); i++) {
                    if (this.collider().centerX() == actors.get(i).collider().centerX() &&
                            this.collider().centerY() == actors.get(i).collider().centerY()) {
                        continue;
                    }
                    if (this.leftIsCollision(actors.get(i)) || this.rightIsCollision(actors.get(i))) {
                        xArisIsNoTouch = false;
                    }
                    if (this.topIsCollision(actors.get(i)) || this.bottomIsCollision(actors.get(i))) {
                        yArisIsNoTouch = false;
                    }
                }
                if (xArisIsNoTouch) {
                    if (painter().centerX() > x) {
                        this.offSet((int) -xM, 0);
                    } else {
                        this.offSet((int) xM, 0);
                    }
                }
                if (yArisIsNoTouch) {
                    if (painter().centerY() > y) {
                        this.offSet(0, (int) -yM);
                    } else {
                        this.offSet(0, (int) yM);
                    }
                }
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
                }
            }
        }
        //選最短距離者追蹤並攻擊，敵方死亡後回到原位
        public void autoAttack (ArrayList < Actor > actors, ArrayList < Actor > alliance){ //傳敵軍陣列近來
            if (atkSpeed.isPause()) {
                atkSpeed.loop();
            }
            if (actors.size() > 0) {
                //先一一算出最短距離，存進數字陣列中找出最近的敵人 = target
                float a = 0;
                float b = 0;
                float d = 0;
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
                if (collider().centerX() == strategyX && collider().centerY() == strategyY) {
                    if (atkSpeed.getCount() < 119) {
                        atkSpeed.count();
                    }
                } else {
                    move(strategyX,strategyY, actors);
                }
                this.cannonDirection = CANNON_DIRECTION.FrontMiddle;
//                    System.out.println("我的座標" + this.painter().centerX() + " " + this.painter().centerX() + //測試用輸出
//                            " 戰略座標" + this.strategyXY[0] + " " + this.strategyXY[1]);
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
                AudioResourceController.getInstance().play("/T.wav");
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
                        continue;
                    }
                }else if(bullets.get(i).isTouchBattleEdge()){
                        bullets.get(i).explored();
                }else {
                    //攻擊敵機並扣血
                    for (int j = 0; j < actors.size(); j++) {
                        if (bullets.get(i).isCollision(actors.get(j))) {
                            bullets.get(i).explored();
                            actors.get(j).offsetHp(-(this.atk) * (1 - actors.get(j).def));
                        }
                    }
                }
            }
        }
        //判斷是否死亡
        public boolean isAlive () {
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
        public abstract void paint (Graphics g);
        public void update () {

        }
    }
