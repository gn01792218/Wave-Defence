package gameobj;

import utils.Global;

import java.util.ArrayList;

public class AttackUp extends Skill{

    @Override
    public void skillExection(ArrayList<Actor> actors) {
        for(int i=0;i<actors.size();i++){
            actors.get(i).offsetAtk(actors.get(i).getAtk()*1.5); //本身攻擊力+攻擊力*1.5
        }
    }
    @Override
    public Global.SkillName getSkillName() {
        return Global.SkillName.ATTACKUP;
    }
}
