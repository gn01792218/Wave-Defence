package menu;

import controllers.ImageController;
import gameobj.Actor;
import utils.Global;
import java.awt.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

//帶有角色種類的Button
public class ActorButton extends Button{
    private ArrayList<Actor> alliances=Global.getAllianceActors();//得到Global的角色
    private ArrayList<Actor> enemys=Global.getEnemyActors();//得到Enemy的角色
    private Global.ActorType actorType;
    private int number;//存放這個角色的數量
    private Label name;//角色名稱
//    private Label numberLabel;// 目前數量顯示器
    private Label costLabel;//需要花費的標籤
    private Label costMoneyLabel;//需要花費ㄉ金錢顯示標籤
    private Label hpLimit;//hp上限資訊
    private Label atk;// 攻擊力資訊
    private Label def;//防禦力
    private Label atkdis;//攻擊距離
    private Label atkSpeed;//攻擊速度
    private Label speed;// 移動速度
    private int costMoney;//這種角色要花多少錢-->要改的話去Global改唷~~~~
    private Button info;//角色資訊欄位。在User監聽滑鼠移動後，將角色按鈕設置成顯示Info true，否則為false；資訊欄都固定畫在左側。
    private boolean infoVisable; //是否顯示資訊欄
    private boolean isUnLocked;// 是否被解鎖
    private BufferedImage lockImage;//被鎖住的圖片
    protected BufferedImage plusButton;//+號
    private Label unLockInfo;//解鎖條件的說明
    private boolean isEnemy=false;

    public ActorButton(int x, int y, Style style,Global.ActorType actorType,int cost,boolean isEnemy,String name) { //輸入的xy是左上角
        super(x, y, style);
        this.actorType=actorType;
        if(cost>0) { //不是敵人才畫花費
//            numberLabel = new Label(1050, 200, new Style.StyleRect(0, 0, true, null));//畫在這個按鈕的右下方；不要設寬高，就可以精準畫在要的位置
            this.costMoney = cost;
            costLabel = new Label(1050, 200, new Style.StyleRect(45, 45, true, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/money.png"))));
            costMoneyLabel=new Label(1070,230,new Style.StyleRect(0,0,null));
        }
        if(!isEnemy) {
            info = new Button(480, 157, new Style.StyleRect(300, 468
                    , new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/InfoB2-300.png"))));
        }else {
            info = new Button(350, 127, new Style.StyleRect(300, 468
                    , new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/InfoB2-300.png"))));
        }
        unLockInfo=new Label(680,630,new Style.StyleRect(0,0,true,null).setText("解鎖條件:完成第一關挑戰").setTextColor(Color.RED).setTextFont(new Font("標楷體",Font.ITALIC,22)));
        infoVisable=false;
        if(!isEnemy) {
            for (int i = 0; i < this.alliances.size(); i++) {
                if (alliances.get(i).getType() == this.actorType) {
                    this.name=new Label(580,150,new Style.StyleRect(100,100,true,null).setText(name).setTextFont(new Font("標楷體", Font.BOLD, 32)).setTextColor(Color.orange));
                    for (int j = 0; j < 6; j++) {
                        if (j == 0) {
                            Global.getFrmt().setMaximumFractionDigits(0);
                            hpLimit = new Label(510 + 60, 230 + (j * 338 / 6), new Style.StyleRect(100, 100, true, null).
                                    setText("血量上限 :" + Global.getFrmt().format(alliances.get(i).getHpLimit())).setTextFont(new Font("標楷體", Font.ITALIC, 32)));
                        } else if (j == 1) {
                            Global.getFrmt().setMaximumFractionDigits(0);
                            atk = new Label(510 + 60, 230 + (j * 338 / 6), new Style.StyleRect(100, 100, true, null).
                                    setText("攻擊力 :" + Global.getFrmt().format(alliances.get(i).getAtk())).setTextFont(new Font("標楷體", Font.ITALIC, 32)));
                        } else if (j == 2) {
                            Global.getFrmt().setMaximumFractionDigits(1);
                            def = new Label(510 + 60, 230 + (j * 338 / 6), new Style.StyleRect(100, 100, true, null).
                                    setText("防禦力 :" + Global.getFrmt().format(alliances.get(i).getDef())).setTextFont(new Font("標楷體", Font.ITALIC, 32)));
                        } else if (j == 3) {
                            Global.getFrmt().setMaximumFractionDigits(0);
                            atkdis = new Label(510 + 60, 230 + (j * 338 / 6), new Style.StyleRect(100, 100, true, null).
                                    setText("攻擊距離 :" + Global.getFrmt().format(alliances.get(i).getAtkdis())).setTextFont(new Font("標楷體", Font.ITALIC, 32)));
                        } else if (j == 4) {
                            Global.getFrmt().setMaximumFractionDigits(1);
                            atkSpeed = new Label(510 + 60, 230 + (j * 338 / 6), new Style.StyleRect(100, 100, true, null).
                                    setText("攻擊速度 :" + Global.getFrmt().format(60 / alliances.get(i).getAtkSpeed().getCountLimit())).setTextFont(new Font("標楷體", Font.ITALIC, 32)));
                        } else {
                            Global.getFrmt().setMaximumFractionDigits(1);
                            speed = new Label(510 + 60, 230 + (j * 338 / 6), new Style.StyleRect(100, 100, true, null).
                                    setText("移動速度 :" + Global.getFrmt().format(alliances.get(i).getSpeed())).setTextFont(new Font("標楷體", Font.ITALIC, 32)));
                        }
                    }
                }
            }
        }else {
            for (int i = 0; i < this.enemys.size(); i++) {
                if (enemys.get(i).getType() == this.actorType) {
                    this.name=new Label(450,120,new Style.StyleRect(100,100,true,null).setText(name).setTextFont(new Font("標楷體", Font.BOLD, 32)).setTextColor(Color.GREEN));
                    for (int j = 0; j < 6; j++) {
                        if (j == 0) {
                            Global.getFrmt().setMaximumFractionDigits(0);
                            hpLimit = new Label(380 + 60, 200 + (j * 338 / 6), new Style.StyleRect(100, 100, true, null).
                                    setText("血量上限 :" + Global.getFrmt().format(enemys.get(i).getHpLimit())).setTextFont(new Font("標楷體", Font.ITALIC, 32)));
                        } else if (j == 1) {
                            Global.getFrmt().setMaximumFractionDigits(0);
                            atk = new Label(380 + 60, 200 + (j * 338 / 6), new Style.StyleRect(100, 100, true, null).
                                    setText("攻擊力 :" + Global.getFrmt().format(enemys.get(i).getAtk())).setTextFont(new Font("標楷體", Font.ITALIC, 32)));
                        } else if (j == 2) {
                            Global.getFrmt().setMaximumFractionDigits(1);
                            def = new Label(380 + 60, 200 + (j * 338 / 6), new Style.StyleRect(100, 100, true, null).
                                    setText("防禦力 :" + Global.getFrmt().format(enemys.get(i).getDef())).setTextFont(new Font("標楷體", Font.ITALIC, 32)));
                        } else if (j == 3) {
                            Global.getFrmt().setMaximumFractionDigits(0);
                            atkdis = new Label(380 + 60, 200 + (j * 338 / 6), new Style.StyleRect(100, 100, true, null).
                                    setText("攻擊距離 :" + Global.getFrmt().format(enemys.get(i).getAtkdis())).setTextFont(new Font("標楷體", Font.ITALIC, 32)));
                        } else if (j == 4) {
                            Global.getFrmt().setMaximumFractionDigits(1);
                            atkSpeed = new Label(380+ 60, 200 + (j * 338 / 6), new Style.StyleRect(100, 100, true, null).
                                    setText("攻速/秒 :" + Global.getFrmt().format(60 / enemys.get(i).getAtkSpeed().getCountLimit())).setTextFont(new Font("標楷體", Font.ITALIC, 32)));
                        } else {
                            Global.getFrmt().setMaximumFractionDigits(1);
                            speed = new Label(380 + 60, 200 + (j * 338 / 6), new Style.StyleRect(100, 100, true, null).
                                    setText("移動速度 :" + Global.getFrmt().format(enemys.get(i).getSpeed())).setTextFont(new Font("標楷體", Font.ITALIC, 32)));
                        }
                    }
                }
            }
        }
    }

    //方法
    public void setInfoVisable(boolean visable){
        this.infoVisable=visable;
    }
    public int getCostMoney() {
        return costMoney;
    }

//    public void setNumberLabel(Label numberLabel) {
//        this.numberLabel = numberLabel;
//    }

    public void setCostLabel(Label costLabel) {
        this.costLabel = costLabel;
    }

    public boolean isUnLocked() {
        return isUnLocked;
    }

    public Global.ActorType getActorType(){
        return this.actorType;
    }
    public int getNumber() {
        return number;
    }
    public void offSetNumber(int number){
        this.number+=number;
    }

    public void setUnLocked(boolean unLocked) {
        isUnLocked = unLocked;
    }
    public void setLockImage(BufferedImage image){
        this.lockImage=image;
    }

    //offsetNum
    public void offSetNum(int x){  //記得在場景中有我軍死掉被移除的話要--
        this.number+=x;
    }
//    public Label getNumberInfo() {
//        return numberLabel;
//    }

    @Override
    public void paint(Graphics g){
        if(!isUnLocked && this.lockImage!=null){ //被鎖住時 且 有圖片時
            g.drawImage(lockImage,this.left()-20,this.top()+75,500,500,null); //就畫LockImage
            unLockInfo.paint(g);
        }else{
            if (super.getPaintStyle() != null) {
                super.getPaintStyle().paintComponent(g, super.getX(), super.getY());
            }
//            if(numberLabel!=null){
//                numberLabel.getPaintStyle().setText("目前數量"+this.number+"").setTextFont(new Font("標楷體",Font.ITALIC,42));  //一定要用Style設置，才會顯示文字唷!!
//                numberLabel.paint(g);
//            }
            if(costLabel!=null) {
                costLabel.paint(g);
            }
            if(costMoneyLabel!=null){
                costMoneyLabel.getPaintStyle().setText("       "+costMoney).setTextFont(new Font("標楷體",Font.ITALIC,38)).setTextColor(new Color(0xC4C4C7));
                costMoneyLabel.paint(g);
            }
                info.paint(g);
                //畫出訊息
                name.paint(g);
                hpLimit.paint(g);
                atk.paint(g);
                def.paint(g);
                atkdis.paint(g);
                atkSpeed.paint(g);
                speed.paint(g);
        }
    }
}
