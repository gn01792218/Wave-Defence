package menu;

import controllers.ImageController;
import gameobj.Actor;
import utils.Delay;
import utils.Global;

import java.awt.*;
import java.util.ArrayList;

public class DefUp extends SkillButton{

    private final float defEffect=0.2f; //要增加幾成的防禦力
    private final int bufftime=300; //持續X/60秒

    public DefUp(int x, int y, Style style, Global.SkillName skillName, int cost){
        super(x,y,style,skillName,cost);
        this.buffTime=new Delay(bufftime);//增加防禦時間5秒
        info.getPaintStyle().setText("全體防禦力+"+defEffect+"("+defEffect*10+"成)，持續"+bufftime/60+"秒");
        infoVisable=false; //一開始不顯現
    }

    @Override
    public void skillExection(ArrayList<Actor> actors) {
            for (int i = 0; i < actors.size(); i++) {
                System.out.println("第"+(i+1)+"台原始防禦力為"+actors.get(i).getDef());
                actors.get(i).offsetDef(defEffect); //本身+0.2-->提高兩成防禦力 0.5+0.2=0.7
                System.out.println("第"+(i+1)+"台增加防禦力為"+actors.get(i).getDef()); //測試用，外面要記得打印攻擊力測試時間內的攻擊力
            }
    }

    @Override
    public void skillReset(ArrayList<Actor> actors) {
        for(int i=0;i<actors.size();i++){
            actors.get(i).offsetDef(-defEffect);
            System.out.println("防禦力回復原廠設定"+actors.get(i).getDef());
        }
        setUsed(true); //被施放過了
        System.out.println("技能: "+this.getSkillName()+"施放結束");
    }

}
