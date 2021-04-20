package menu;

import gameobj.Actor;
import utils.Delay;
import utils.Global;

import java.awt.*;
import java.util.ArrayList;

public class HpUp extends SkillButton{
    private Label label;
    public HpUp(int x, int y, Style style, Global.SkillName skillName, int cost){
        super(x,y,style,skillName,cost);
        this.buffTime=new Delay(60); //就算不用記時也要設置否則會nullPoint；且可以標示Buff圖示
        info.getPaintStyle().setText("全體回復血量到最大上限");
        infoVisable=false; //一開始不顯現
        this.label=new Label(this.getCenterX(),this.bottom(),new Style.StyleRect(10,10,true,null).setText("花費:"+this.cost+"榮譽").setTextFont(new Font("標楷體",Font.ITALIC,22)));
        this.isUnLocked=true;//一開始就是被解鎖的
    }
    @Override
    public void skillExection(ArrayList<Actor> actors) {
        for(int i=0;i<actors.size();i++){
            System.out.println("坦克血"+actors.get(i).getHp());
            actors.get(i).setSkillName(this.getSkillName()); //將該角色身上的當前招式名稱更改
            actors.get(i).setHp(actors.get(i).getHpLimit()); //血量全滿(直接設成血量上限)
            actors.get(i).setOnBuff(true); //標示為Buff狀態
            System.out.println("坦克血加滿~~~"+actors.get(i).getHp());
        }
    }
    @Override
    public void skillReset(ArrayList<Actor> actors) {
        setUsed(true); //被施放過了
        for(int i=0;i<actors.size();i++){
            actors.get(i).setOnBuff(false); //標示為非Buff狀態
        }
        System.out.println("技能: "+this.getSkillName()+"施放結束");
    }
    @Override
    public void paint(Graphics g){
        if (super.getPaintStyle() != null) {
            super.getPaintStyle().paintComponent(g, super.getX(), super.getY());
        }
        if(info!=null && infoVisable){
            info.paint(g);}
        if(label!=null  && infoVisable){
            label.paint(g);
        }
    }
}
