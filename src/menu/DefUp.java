package menu;

import gameobj.Actor;
import utils.Global;

import java.util.ArrayList;

public class DefUp extends SkillButton{

    public DefUp(int x, int y, Style style, Global.SkillName skillName, int cost){
        super(x,y,style,skillName,cost);
    }

    @Override
    public void skillExection(ArrayList<Actor> actors) {
        for(int i=0;i<actors.size();i++){
            actors.get(i).offsetDef(actors.get(i).getDef()*1.5); //本身防禦力+防禦力*1.5
        }
    }

}
