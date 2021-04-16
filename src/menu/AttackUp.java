package menu;

import gameobj.Actor;
import menu.Button;
import menu.Label;
import menu.SkillButton;
import menu.Style;
import utils.Global;

import java.util.ArrayList;

public class AttackUp extends SkillButton {

    public AttackUp(int x, int y, Style style, Global.SkillName skillName, int cost){
        super(x,y,style,skillName,cost);
    }

    @Override
    public void skillExection(ArrayList<Actor> actors) {  //施放技能，場景中會用到
        for(int i=0;i<actors.size();i++){
            actors.get(i).offsetAtk(actors.get(i).getAtk()*1.5); //本身攻擊力+攻擊力*1.5
        }
    }


}
