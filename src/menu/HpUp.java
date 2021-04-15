package menu;

import gameobj.Actor;
import utils.Delay;
import utils.Global;

import java.util.ArrayList;

public class HpUp extends SkillButton{
    public HpUp(int x, int y, Style style, Global.SkillName skillName, int cost){
        super(x,y,style,skillName,cost);
        this.buffTime=new Delay(0); //就算不用記時也要設置否則會nullPoint
    }

    @Override
    public void skillExection(ArrayList<Actor> actors) {
        for(int i=0;i<actors.size();i++){
            System.out.println("坦克血"+actors.get(i).getHp());
            actors.get(i).setHp(actors.get(i).getHpLimit()); //血量全滿(直接設成血量上限)
            System.out.println("坦克血加滿~~~"+actors.get(i).getHp());
        }
    }

    @Override
    public void skillReset(ArrayList<Actor> actors) {
        setUsed(true); //被施放過了
        System.out.println("技能: "+this.getSkillName()+"施放結束");
    }
}
