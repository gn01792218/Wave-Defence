package menu;

import gameobj.Actor;
import gameobj.LaserCar;
import gameobj.Tank1;
import gameobj.Tank2;
import utils.Delay;
import utils.Global;

import java.awt.*;
import java.util.ArrayList;
//召喚援軍的技能
//可以呼叫援軍-->隨機指派兩隻進場
//回合結束就離開戰場
public class Reinforcements extends SkillButton{
    private Label label;
    private final int reinforcementsNum=2; //召喚兩隻援軍
    private final int bufftime=600; //持續X/60秒

    public int getReinforcementsNum() {
        return reinforcementsNum;
    }

    public Reinforcements(int x, int y, Style style, Global.SkillName skillName, int cost){
        super(x,y,style,skillName,cost);
        buffTime=new Delay(bufftime); //30秒後離開
        info.getPaintStyle().setText("隨機呼叫"+reinforcementsNum+"個援軍"+bufftime/60+"秒後離開").
                setTextFont(new Font("標楷體",Font.ITALIC,22));
        infoVisable=false; //一開始不顯現
        this.label=new Label(this.getCenterX(),this.bottom(),new Style.StyleRect(10,10,true,null).setText("花費"+this.cost).setTextFont(new Font("標楷體",Font.ITALIC,22)));
    }
    @Override
    public void skillExection(ArrayList<Actor> actors) {
        ArrayList<Actor> temp=Global.getAllianceActors();
        //隨機增加兩隻，使用switch來做
        for(int i=0;i<reinforcementsNum;i++) {
            switch (temp.get(Global.random(0, temp.size() - 1)).getType()) {
                case TANK1:
                    Actor actor1=new Tank1(400+i*100,800,false);
//                    actor1.setOnBuff(true); //設成是onBuff狀態-->才可以畫出特效圖
                    actor1.setReinforcement(true);  //記得將ReinforceMent設成true
                    actors.get(i).setSkillName(this.getSkillName()); //將該角色身上的當前招式名稱更改
                    System.out.println(this.getSkillName());
                    actors.add(actor1);
                    System.out.println("呼叫"+actor1.getType());
                    break;
                case TANK2:
                    Actor actor2=new Tank2(400+i*100,800,false);
//                    actor2.setOnBuff(true); //設成是onBuff狀態-->才可以畫出特效圖
                    actor2.setReinforcement(true);  //記得將ReinforceMent設成true
                    actors.get(i).setSkillName(this.getSkillName()); //將該角色身上的當前招式名稱更改
                    System.out.println(this.getSkillName());
                    actors.add(actor2);
                    System.out.println("呼叫"+actor2.getType());
                    break;
                case LASERCAR:
                    Actor actor3= new LaserCar(400, 800, false);
//                    actor3.setOnBuff(true); //設成是onBuff狀態-->才可以畫出特效圖
                    actor3.setReinforcement(true);  //記得將ReinforceMent設成true
                    actors.get(i).setSkillName(this.getSkillName()); //將該角色身上的當前招式名稱更改
                    System.out.println(this.getSkillName());
                    actors.add(actor3);
                    System.out.println("呼叫"+actor3.getType());
                    break;
            }
        }
    }

    @Override
    public void skillReset(ArrayList<Actor> actors) {
        //換場了，援軍還沒死的話，會影響到Global中的actortButton數量-->解方，遊戲場景中，按下這個技能，就直接扣2
        //從陣列中刪除增援的部隊
        for(int i=0;i<actors.size();i++){
            if(actors.get(i).isReinforcement()){
                actors.get(i).moveToTarget(500,1000); //從場地邊緣離開
                actors.remove(i);
                i--;
                System.out.println("刪除了第"+i+1+"隻援軍");
            }

            }

    }
    @Override
    public void paint(Graphics g){
        if (super.getPaintStyle() != null) {
            super.getPaintStyle().paintComponent(g, super.getX(), super.getY());
        }
        if(info!=null && infoVisable){ info.paint(g);}
        if(label!=null && infoVisable){
            label.paint(g);
        }
    }
}
