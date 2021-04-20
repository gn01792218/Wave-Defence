package menu;

import gameobj.Actor;
import utils.Delay;
import utils.Global;

import java.awt.*;
import java.util.ArrayList;

public class ElectWave extends SkillButton{
    private int bufftime=180; //敵軍全體暫停3秒
    private Label label;
    public ElectWave(int x, int y, Style style, Global.SkillName skillName, int cost){
        super(x,y,style,skillName,cost);
        this.buffTime=new Delay(bufftime);//增基攻擊力時間持續5秒
        info.getPaintStyle().setText("使敵軍全體當機"+bufftime/60+"秒");
        this.label=new Label(this.getCenterX(),this.bottom(),new Style.StyleRect(10,10,true,null).setText("花費:"+this.cost).setTextFont(new Font("標楷體",Font.ITALIC,22)));
        infoVisable=false; //一開始不顯現
    }

    @Override
    public void skillExection(ArrayList<Actor> actors) { //放敵人進來
        for(int i=0;i<actors.size();i++){
            actors.get(i).setSkillName(this.getSkillName());
            actors.get(i).setOnDebuff(true);
        }
    }

    @Override
    public void skillReset(ArrayList<Actor> actors) {
        for(int i=0;i<actors.size();i++){
            actors.get(i).setSkillName(null);
            actors.get(i).setOnDebuff(false);
        }
    }
    @Override
    public void paint(Graphics g){
        if (super.getPaintStyle() != null) {
            super.getPaintStyle().paintComponent(g, super.getX(), super.getY());
        }
        if(info!=null && infoVisable){
            info.paint(g);}
        if(label!=null  && infoVisable){
            label.paint(g);
        }
    }
}
