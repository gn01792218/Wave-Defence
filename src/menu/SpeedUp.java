package menu;

import controllers.ImageController;
import gameobj.Actor;
import utils.Delay;
import utils.Global;

import java.awt.*;
import java.util.ArrayList;

public class SpeedUp extends SkillButton{
    private final float speedEffect=5;
    private final int bufftime=360; //持續X/60秒
    public SpeedUp(int x, int y, Style style, Global.SkillName skillName, int cost){
        super(x,y,style,skillName,cost);
        this.buffTime=new Delay(bufftime); //速度增加持續六秒
        info.getPaintStyle().setText("全體移動+"+speedEffect+"持續"+bufftime/60+"秒").
                setTextFont(new Font("標楷體",Font.ITALIC,22));
        infoVisable=false; //一開始不顯現

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
            actors.get(i).offsetSpeed(-speedEffect); //把atk回復原廠設定
            System.out.println("移動速度回復原廠設定"+actors.get(i).getSpeed());
        }
        setUsed(true); //被施放過了
        System.out.println("技能: "+this.getSkillName()+"施放結束");
    }
}
