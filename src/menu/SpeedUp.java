package menu;

import gameobj.Actor;
import utils.Global;

import java.util.ArrayList;

public class SpeedUp extends SkillButton{

    public SpeedUp(int x, int y, Style style, Global.SkillName skillName, int cost){
        super(x,y,style,skillName,cost);
    }


    @Override
    public void skillExection(ArrayList<Actor> actors) {
        for(int i=0;i<actors.size();i++){
            actors.get(i).offsetSpeed(actors.get(i).getSpeed()*1.5); //本身移動速度+移動速度*1.5
        }
    }
}
