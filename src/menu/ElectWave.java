package menu;

import controllers.ImageController;
import gameobj.Actor;
import utils.Delay;
import utils.Global;

import java.awt.*;
import java.util.ArrayList;

public class ElectWave extends SkillButton{
    private Label lockLabel;// 鎖住的圖片
    private int bufftime=240; //敵軍全體暫停4秒
    private Label label;

    public ElectWave(int x, int y, Style style, Global.SkillName skillName, int cost){
        super(x,y,style,skillName,cost);
        this.buffTime=new Delay(bufftime);//增基攻擊力時間持續5秒
        info.getPaintStyle().setText("使敵軍全體當機"+bufftime/60+"秒");
        this.lockLabel=new Label(this.getCenterX()-64,this.getCenterY()-64,new Style.StyleRect(64,64,true,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/SLock2.png"))));
        this.unLockCost=650;//花500可以解鎖
        this.label=new Label(this.getCenterX(),this.bottom(),new Style.StyleRect(10,10,true,null));
        infoVisable=false; //一開始不顯現
        this.isUnLocked=false;//需要解鎖

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
            actors.get(i).setOnDebuff(false);
        }
        setUsed(true); //被施放過了
    }
    @Override
    public void paint(Graphics g){
        if (super.getPaintStyle() != null) {
            super.getPaintStyle().paintComponent(g, super.getX(), super.getY());
        }
        if(info!=null && infoVisable){
            info.paint(g);}

        if(!isUnLocked && lockLabel!=null){
            lockLabel.paint(g);
            label.getPaintStyle().setText("解鎖花費:"+this.unLockCost+"榮譽").setTextFont(new Font("標楷體",Font.ITALIC,22));//顯示解鎖的畫面
        }else{
            label.getPaintStyle().setText("花費:"+this.cost+"榮譽").setTextFont(new Font("標楷體",Font.ITALIC,22));//顯示解鎖的畫面
        }
        if(label!=null  && infoVisable){
            label.paint(g);
        }
    }
}
