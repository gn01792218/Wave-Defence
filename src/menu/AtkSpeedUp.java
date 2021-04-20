package menu;

import gameobj.Actor;
import utils.Delay;
import utils.Global;

import java.awt.*;
import java.util.ArrayList;

public class AtkSpeedUp extends SkillButton{
    private final int atkSpeedUp=-60;//攻擊速度減少1秒
    private final int bufftime=180; //持續X/60秒
    private Label label;
    public AtkSpeedUp(int x, int y, Style style, Global.SkillName skillName, int cost){
        super(x,y,style,skillName,cost);
        this.buffTime=new Delay(bufftime);//增基攻擊力時間持續5秒
        info.getPaintStyle().setText("全體攻擊速提高，持續"+bufftime/60+"秒");
        this.label=new Label(this.getCenterX(),this.bottom(),new Style.StyleRect(10,10,true,null).setText("花費:"+this.cost).setTextFont(new Font("標楷體",Font.ITALIC,22)));
        infoVisable=false; //一開始不顯現
    }
    @Override
    public void skillExection(ArrayList<Actor> actors) {
        for(int i=0;i<actors.size();i++){
            if(actors.get(i).getAtkSpeed().getCountLimit()<=60){
                System.out.println("原本攻擊速度"+actors.get(i).getAtkSpeed().getCountLimit());
                actors.get(i).setAtkSpeed(new Delay(6));
                System.out.println("提升為"+actors.get(i).getAtkSpeed().getCountLimit());
            }else {
                System.out.println("原本攻擊速度" + actors.get(i).getAtkSpeed().getCountLimit());
                actors.get(i).setAtkSpeed(new Delay(actors.get(i).getAtkSpeed().getCountLimit() - 60));
                System.out.println("提升為" + actors.get(i).getAtkSpeed().getCountLimit());
            }

        }
    }

    @Override
    public void skillReset(ArrayList<Actor> actors) {
        for(int i=0;i<actors.size();i++){
            actors.get(i).setAtkSpeed(new Delay(actors.get(i).getAtkSpeedLimit()));
            System.out.println("攻擊速度回復為"+actors.get(i).getAtkSpeed().getCountLimit());
        }

    }
    @Override
    public void paint(Graphics g){
        if (super.getPaintStyle() != null) {
            super.getPaintStyle().paintComponent(g, super.getX(), super.getY());
        }
        if(info!=null && infoVisable){info.paint(g);}
        if(label!=null  && infoVisable){
            label.paint(g);
        }
    }
}
