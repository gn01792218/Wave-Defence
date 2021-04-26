package menu;

import controllers.ImageController;
import gameobj.*;
import utils.Delay;
import utils.Global;
import utils.Player;

import java.awt.*;
import java.util.ArrayList;

public class Reinforcements extends SkillButton{
    private Label lockLabel;// 鎖住的圖片
    private Label label;
    private final int reinforcementsNum=3; //召喚兩隻援軍
    private final int bufftime=600; //持續X/60秒
    private Label selectedLabel;//被選中後的按鈕
    public int getReinforcementsNum() {
        return reinforcementsNum;
    }
    public Reinforcements(int x, int y, Style style, Global.SkillName skillName, int cost){
        super(x,y,style,skillName,cost);
        buffTime=new Delay(bufftime); //30秒後離開
        info.getPaintStyle().setText("隨機呼叫"+reinforcementsNum+"個援軍"+bufftime/60+"秒後離開").
                setTextFont(new Font("標楷體",Font.ITALIC,22));
        infoVisable=false; //一開始不顯現
        this.lockLabel=new Label(getX(),getY(),new Style.StyleRect(128,128,true,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/SLock1.png"))));
        this.unLockCost=250;//花500可以解鎖
        this.label=new Label(this.getCenterX()+20,this.top()-20,new Style.StyleRect(10,10,true,null));
        this.isUnLocked=false;//需要解鎖
        selectedLabel=new Label(getX(),getY(),new Style.StyleRect(64,64,true,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/SB-reinforcement.png"))));
    }
    @Override
    public void skillExection(ArrayList<Actor> actors) {
        ArrayList<Actor> temp=Global.getAllianceActors();
        //隨機增加兩隻，使用switch來做
        for(int i=0;i<reinforcementsNum;i++) {
            switch (temp.get(Global.random(0, temp.size() - 1)).getType()) {
                case TANK1:
                    Actor actor1=new Tank1(600+i*100,700,false);
//                    actor1.setOnBuff(true); //設成是onBuff狀態-->才可以畫出特效圖
                    //記得將ReinforceMent設成true
                    actor1.setReinforcement(true);
                    actors.add(actor1);
                    actors.get(i).setSkillName(this.getSkillName()); //將該角色身上的當前招式名稱更改
                    System.out.println(this.getSkillName());
                    System.out.println("呼叫"+actor1.getType());
                    break;
                case TANK2:
                    Actor actor2=new Tank2(600+i*100,700,false);
//                    actor2.setOnBuff(true); //設成是onBuff狀態-->才可以畫出特效圖
                    actor2.setReinforcement(true);  //記得將ReinforceMent設成true
                    actors.add(actor2);
                    actors.get(i).setSkillName(this.getSkillName()); //將該角色身上的當前招式名稱更改
                    System.out.println(this.getSkillName());
                    System.out.println("呼叫"+actor2.getType());
                    break;
                case LASERCAR:
                    Actor actor3= new LaserCar(600+i*100, 700, false);
//                    actor3.setOnBuff(true); //設成是onBuff狀態-->才可以畫出特效圖
                    actor3.setReinforcement(true);  //記得將ReinforceMent設成true
                    actors.add(actor3);
                    actors.get(i).setSkillName(this.getSkillName()); //將該角色身上的當前招式名稱更改
                    System.out.println(this.getSkillName());
                    System.out.println("呼叫"+actor3.getType());
                    break;
                case ROCKET:
                    Actor actor4=new Rocket(600+i*100,700,false);
                    actor4.setOnBuff(true);
                    actor4.setReinforcement(true);
                    actors.add(actor4);
                    actors.get(i).setSkillName(this.getSkillName());
                    System.out.println(this.getSkillName());
                    System.out.println("呼叫"+actor4.getType());
            }
        }
        this.setCanUsed(false);

    }

    @Override
    public void skillReset(ArrayList<Actor> actors) {
        //換場了，援軍還沒死的話，會影響到Global中的actortButton數量-->解方，遊戲場景中，按下這個技能，就直接扣2
        //從陣列中刪除增援的部隊
        for(int i=0;i<actors.size();i++){
            if(actors.get(i).isReinforcement() && actors.get(i).isOnBuff()){
                actors.get(i).moveToTarget(500,1080); //從場地邊緣離開
                actors.remove(i);
                i--;
                System.out.println("刪除了第"+(i+1)+"隻援軍");
            }
        }
        if (Player.getInstance().getHonor() >= this.getCost()) {
            this.setCanUsed(true);
        }
    }
    @Override
    public void paint(Graphics g){
        if(info!=null && infoVisable){info.paint(g);}
        if ((Player.getInstance().getHonor()<this.getCost()) || isSelect || !canUsed) {
            if(isInGameScene()){
                if (super.getPaintStyle() != null) { //畫原本的圖
                    super.getPaintStyle().paintComponent(g, super.getX(), super.getY());
                }

            }else{
                selectedLabel.paint(g);} //被選中後畫灰色圖
        }else{
            if (super.getPaintStyle() != null) { //畫原本的圖
                super.getPaintStyle().paintComponent(g, super.getX(), super.getY());
            }
        }
        if(!isUnLocked && lockLabel!=null){
            lockLabel.paint(g);
            label.getPaintStyle().setText("解鎖花費:"+this.unLockCost+"榮譽").setTextFont(new Font("標楷體",Font.ITALIC,22));//顯示解鎖的畫面
            label.getPaintStyle().setTextColor(Color.RED);
        }else{
            label.getPaintStyle().setText("花費:"+this.cost+"榮譽").setTextFont(new Font("標楷體",Font.ITALIC,22));//顯示解鎖的畫面
            label.getPaintStyle().setTextColor(Color.WHITE);
        }
        if(label!=null  && infoVisable && !isInGameScene){
            label.paint(g);
        }
        if(isInSkillScene){
            info.getPaintStyle().setHeight(200);
        }
    }
    @Override
    public void update() {
//        if (Player.getInstance().getHonor() >= this.getCost()) {  //玩家錢小於這個技能的時候也不能使用
//            System.out.println(Player.getInstance().getHonor());
//            setCanUsed(true);
//            setUsed(false);
//        }
    }
}
