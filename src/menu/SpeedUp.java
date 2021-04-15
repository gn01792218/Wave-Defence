package menu;

import gameobj.Actor;
import utils.Delay;
import utils.Global;

import java.util.ArrayList;

public class SpeedUp extends SkillButton{

    public SpeedUp(int x, int y, Style style, Global.SkillName skillName, int cost){
        super(x,y,style,skillName,cost);
        this.buffTime=new Delay(360); //速度增加持續六秒

    }


    @Override
    public void skillExection(ArrayList<Actor> actors) {
            for (int i = 0; i < actors.size(); i++) {
                System.out.println("第"+(i+1)+"台原始移動為"+actors.get(i).getSpeed());
                actors.get(i).offsetSpeed(5); //本身移動速度+5
                System.out.println("第"+(i+1)+"台移動加到"+actors.get(i).getSpeed());
            }
    }

    @Override
    public void skillReset(ArrayList<Actor> actors) {
        for(int i=0;i<actors.size();i++){
            actors.get(i).offsetSpeed(-5); //把atk回復原廠設定
            System.out.println("移動速度回復原廠設定"+actors.get(i).getSpeed());
        }
        setUsed(true); //被施放過了
        System.out.println("技能: "+this.getSkillName()+"施放結束");
    }
}
