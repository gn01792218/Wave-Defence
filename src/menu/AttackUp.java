package menu;

import gameobj.Actor;

import utils.Delay;
import utils.Global;

import java.util.ArrayList;

public class AttackUp extends SkillButton {
    private float atk;


    public AttackUp(int x, int y, Style style, Global.SkillName skillName, int cost){
        super(x,y,style,skillName,cost);
        this.buffTime=new Delay(180);//增基攻擊力時間持續3秒
    }
    @Override
    public void skillExection(ArrayList<Actor> actors) {  //施放技能，場景中會用到
        for (int i = 0; i < actors.size(); i++) {
            System.out.println("第"+(i+1)+"台原始攻擊力為"+actors.get(i).getAtk());
            this.atk=(float)actors.get(i).getAtk();
            actors.get(i).offsetAtk(actors.get(i).getAtk()*1.5); //本身攻擊力*0.5  x+x*1.5
            System.out.println("第"+(i+1)+"台增加攻擊力為"+actors.get(i).getAtk()); //測試用，外面要記得打印攻擊力測試時間內的攻擊力
        }
    }

    @Override
    public void skillReset(ArrayList<Actor> actors) {   //時間到施放完畢要記得reSet技能
        for(int i=0;i<actors.size();i++){
            System.out.println("攻擊力目前為"+actors.get(i).getAtk());

            actors.get(i).offsetAtk(-atk*1.5); //把atk回復原廠設定 公式:(625-625*0.4=375)要減去的值
            System.out.println("攻擊力回復原廠設定"+actors.get(i).getAtk());
        }
        setUsed(true); //被施放過了
        System.out.println("技能: "+this.getSkillName()+"施放結束");
    }


}
