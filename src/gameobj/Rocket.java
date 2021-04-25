package gameobj;

import controllers.AudioResourceController;
import controllers.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Rocket extends Actor{
    private BufferedImage emptyBullet;
    private boolean isempty;
    private Delay reloaddelay;

    private BufferedImage empty_ES1;
    private BufferedImage empty_ES2;
    private BufferedImage empty_ES3;
    private BufferedImage empty_ES4;
    private BufferedImage empty_ES5;
    private BufferedImage empty_EB;
    protected BufferedImage image_S1;//技能1圖片
    protected BufferedImage image_S2;// 技能2圖片
    protected BufferedImage image_S3;//技能3圖片
    protected BufferedImage image_S4;//技能4圖片
    protected BufferedImage image_S5;//技能5圖片
    protected BufferedImage image_S7;//技能7圖片

    public Rocket(float x,float y,boolean isEnemy){
        super(x,y,50,50,x,y,75,90);
        this.image= ImageController.getInstance().tryGet("/AARocket.png");
        this.emptyBullet=ImageController.getInstance().tryGet("/AARocket_emptyBullet.png");
        this.image2=ImageController.getInstance().tryGet("/AARocketB.png");
        this.image_S1=ImageController.getInstance().tryGet("/AARocket_S1.png");
        this.image_S2=ImageController.getInstance().tryGet("/AARocket_S2.png");
        this.image_S3=ImageController.getInstance().tryGet("/AARocket_S3.png");
        this.image_S4=ImageController.getInstance().tryGet("/AARocket_S4.png");
        this.image_S5=ImageController.getInstance().tryGet("/AARocket_S5.png");
        this.empty_ES1=ImageController.getInstance().tryGet("/AARocket_ES1.png");
        this.empty_ES2=ImageController.getInstance().tryGet("/AARocket_ES2.png");
        this.empty_ES3=ImageController.getInstance().tryGet("/AARocket_ES3.png");
        this.empty_ES4=ImageController.getInstance().tryGet("/AARocket_ES4.png");
        this.empty_ES5=ImageController.getInstance().tryGet("/AARocket_ES5.png");
        this.empty_EB=ImageController.getInstance().tryGet("/AARocket_EB.png");
        this.image_S7=ImageController.getInstance().tryGet("/AARocket_S7.png");
        reloaddelay=new Delay(120);
        hpLimit=250;//血量上限
        hp=hpLimit; //初始血量
        atk=150; //攻擊力-->全域傷害~!!!!!
        atkSpeed = new Delay(240); //4秒一發
        atkSpeed.loop();
        this.atkSpeedLimit=atkSpeed.getCountLimit();
        speed=1.2f; //移動速度
        def=0.1; //防禦力
        atkdis=500; //攻擊距離
        this.isEnemy=isEnemy; //敵我單位
        this.isempty=false;
        atkSpeed.loop();
    }

    public boolean isIsempty() {
        return isempty;
    }

    public void setIsempty(boolean isempty) {
        this.isempty = isempty;
    }
    @Override
    public void autoAttack (ArrayList < Actor > actors, ArrayList < Actor > alliance){ //傳敵軍陣列近來
        Rect target = null;

        if (actors.size() > 0) {
            //先一一算出最短距離，存進數字陣列中找出最近的敵人 = target
            float a;
            float b;
            float d;
            float mind = Integer.MAX_VALUE;

            for (int i = 0; i < actors.size(); i++) {
                a = Math.abs(this.painter().centerX() - actors.get(i).painter().centerX());
                b = Math.abs(this.painter().centerY() - actors.get(i).painter().centerY());
                d = (float) Math.sqrt(a * a + b * b);
                if (d < mind) { //最短距離者 ，取他的XY值
                    if (targetIsInBattleField(actors.get(i).collider().centerX(), actors.get(i).collider().centerY())) {
                        mind = d;
                        target = actors.get(i).collider();
                    }
                }
            }
        }
            if (target != null) {
                System.out.println("000");
                //移動至攻擊範圍內則開火
                float targetX = target.centerX();
                float targetY = target.centerY();

                if (isInAtkdis(targetX, targetY)) {
                    fire(targetX, targetY);
                } else {
                    move(targetX, targetY, alliance);
                }
            } else {
                //回到自己原本的位置並導正砲管
                if (collider().centerX() == flag.collider().centerX() && collider().centerY() == flag.collider().centerY()) {
                    System.out.println("111");
                    if (atkSpeed.getCount() < 119) {
                        atkSpeed.count();
                    }
                } else {
                    System.out.println(222);
                    move(flag.collider().centerX(), flag.collider().centerY(), alliance);
                }
//                this.cannonDirection = CANNON_DIRECTION.FrontMiddle;
            }
    }
    //開火
    @Override
    public void fire (float x,float y){
        Bullet bullet = new RocketBullet(this.painter().centerX(), this.painter().centerY(), x, y);
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
        if(!isempty){
            if (atkSpeed.count()) {
                setIsempty(true);
                reloaddelay.play();
                bullets.add(bullet);
                AudioResourceController.getInstance().shot("/T.wav");
            }
        }else{
            if(reloaddelay.count())
            setIsempty(false);
        }
    }
    @Override
    public void bulletsUpdate (ArrayList< Actor > actors) {
        ////飛彈爆炸後一定時間後消失
        for (int i = 0; i < this.bullets.size(); i++) {
            this.bullets.get(i).update(); //子彈移動
            if (bullets.get(i).isExplored()) {
                if (bullets.get(i).isTime()) {
                    bullets.remove(i);
                    i--;
                }
            } else if (isTouchBattleEdge(bullets.get(i).collider().centerX(), bullets.get(i).collider().centerY())) {
                bullets.get(i).explored();
                AudioResourceController.getInstance().shot("/boomSound_Rocket.wav");   //音效改核彈音效
            }else {
                for(int j=0;j<actors.size();j++) {   //攻擊敵機
                    if (bullets.get(i).isCollision(actors.get(j))) {
                        bullets.get(i).explored();
                        AudioResourceController.getInstance().shot("/boomSound_Rocket.wav");  //炮炸改爆炸音效
                    }
                    if(bullets.get(i).isExplored()){  //造成範圍攻擊
                        for(int k=0;k<actors.size();k++){
                            if(Math.abs(actors.get(k).painter().centerX()-bullets.get(i).painter().centerX())<=150
                                    && Math.abs(actors.get(k).painter().centerY()-bullets.get(i).painter().centerY())<=200)  //子彈左右100  下上80的距離 敵軍都會被扣血
                                actors.get(k).offsetHp(-this.atk);
                        }
                    }
                }
            }
        }
    }
    @Override
    public Global.ActorType getType() {
        return Global.ActorType.ROCKET;
    }

    @Override
    public void paintComponent(Graphics g) {
        if(isInControl){ //被控制時要畫藍色的圖
            if(isempty){
                g.drawImage(empty_EB,(int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                        0, 0,
                        140, 180, null);
            }else{
                g.drawImage(image2, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                        0, 0,
                        140, 180, null);
            }
        }else{
            if(isOnBuff()){ //buff狀態時候的畫圖
                if(this.skillName!=null) {
                    switch (skillName) {
                        case ATTACKUP:
                            if(isempty){ //要換圖，下面都要
                                g.drawImage(empty_ES1,(int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                                        0, 0,
                                        140, 180, null);
                            }else{
                                g.drawImage(image_S1, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                                        0, 0,
                                        140, 180, null);
                            }
                            break;
                        case DEFUP:
                            if(isempty){
                                g.drawImage(empty_ES2,(int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                                        0, 0,
                                        140, 180, null);
                            }else{
                                g.drawImage(image_S2, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                                        0, 0,
                                        140, 180, null);
                            }
                            break;
                        case MOVESPEEDUP:
                            if(isempty){
                                g.drawImage(empty_ES3,(int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                                        0, 0,
                                        140, 180, null);
                            }else{
                                g.drawImage(image_S3, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                                        0, 0,
                                        140, 180, null);
                            }
                            break;
                        case HPUP:
                            if(isempty){
                                g.drawImage(empty_ES4,(int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                                        0, 0,
                                        140, 180, null);
                            }else{
                                g.drawImage(image_S4, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                                        0, 0,
                                        140, 180, null);
                            }
                            break;
                        case ATKSPEEDUP:
                            g.drawImage(image_S7, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                                    0, 0,
                                    140, 180, null);
                            break;
                    }
                }
            }else if(isReinforcement){ //要換圖
                if(isempty){
                    g.drawImage(empty_ES5,(int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                            0, 0,
                            140, 180, null);
                }else{
                    g.drawImage(image_S5, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                            0, 0,
                            140, 180, null);
                }
            }
            else { //平常時候
            }
                if(isempty){
                    g.drawImage(emptyBullet,(int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                            0, 0,
                            140, 180, null);
            }else{
                    g.drawImage(image, (int) this.painter().left(), (int) this.painter().top(), (int) this.painter().right(), (int) this.painter().bottom(),
                            0, 0,
                            140, 180, null);
                }
        }
        g.drawImage(image_hp,(int)this.painter().left(),(int)this.painter().bottom(),(int)(this.painter().left()+(7.5F*(this.hp*10/hpLimit))),(int)this.painter().bottom()+11,
                0,0,75,11,null);
        for(int i=0;i<this.bullets.size();i++){
            this.bullets.get(i).paint(g);
        }
    }
    }

