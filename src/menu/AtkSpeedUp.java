package menu;

import controllers.ImageController;
import gameobj.Actor;
import utils.Delay;
import utils.Global;

import java.awt.*;
import java.util.ArrayList;

public class AtkSpeedUp extends SkillButton{
    private Label lockLabel;// 鎖住的圖片
    private final int atkSpeedUp=60;//攻擊速度減少1秒
    private final int bufftime=180; //持續X/60秒
    private Label label;
    private Label selectedLabel;//被選中後的按鈕
    public AtkSpeedUp(int x, int y, Style style, Global.SkillName skillName, int cost){
        super(x,y,style,skillName,cost);
        this.buffTime=new Delay(bufftime);//增基攻擊力時間持續5秒
        info.getPaintStyle().setText("全體攻擊速提高，持續"+bufftime/60+"秒");
        this.lockLabel=new Label(this.getCenterX()-64,this.getCenterY()-64,new Style.StyleRect(64,64,true,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/SLock3.png"))));
        this.label=new Label(this.getCenterX(),this.bottom(),new Style.StyleRect(10,10,true,null));
        infoVisable=false; //一開始不顯現
        this.isUnLocked=false;//需要解鎖
        this.unLockCost=250;//花500可以解鎖
        selectedLabel=new Label(this.getCenterX()-64,this.getCenterY()-64,new Style.StyleRect(64,64,true,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/SB-atkSpeedUp2.png"))));
    }
    @Override
    public void skillExection(ArrayList<Actor> actors) {
        for(int i=0;i<actors.size();i++){
            if(actors.get(i).getAtkSpeed().getCountLimit()-atkSpeedUp<=0){
                System.out.println("原本攻擊速度"+actors.get(i).getAtkSpeed().getCountLimit());
                actors.get(i).setSkillName(this.getSkillName()); //將該角色身上的當前招式名稱更改
                actors.get(i).setAtkSpeed(new Delay(6));
                actors.get(i).setOnBuff(true); //標示為Buff狀態
                System.out.println("提升為"+actors.get(i).getAtkSpeed().getCountLimit());
            }else {
                System.out.println("原本攻擊速度" + actors.get(i).getAtkSpeed().getCountLimit());
                actors.get(i).setSkillName(this.getSkillName()); //將該角色身上的當前招式名稱更改
                actors.get(i).setAtkSpeed(new Delay(actors.get(i).getAtkSpeed().getCountLimit() - 60));
                actors.get(i).setOnBuff(true); //標示為Buff狀態
                System.out.println("提升為" + actors.get(i).getAtkSpeed().getCountLimit());
            }

        }
    }

    @Override
    public void skillReset(ArrayList<Actor> actors) {
        for(int i=0;i<actors.size();i++){
            actors.get(i).setAtkSpeed(new Delay(actors.get(i).getAtkSpeedLimit()));
            actors.get(i).setOnBuff(false); //標示為非Buff狀態
            System.out.println("攻擊速度回復為"+actors.get(i).getAtkSpeed().getCountLimit());
        }
        setUsed(true); //被施放過了
        System.out.println("技能: "+this.getSkillName()+"施放結束");

    }
    @Override
    public void paint(Graphics g){
        if (super.getPaintStyle() != null) {
            super.getPaintStyle().paintComponent(g, super.getX(), super.getY());
        }
        if(info!=null && infoVisable){info.paint(g);}

        if(this.isSelect){
            selectedLabel.paint(g); //被選中後畫灰色圖
        }
        if(!isUnLocked && lockLabel!=null){
            lockLabel.paint(g);
            label.getPaintStyle().setText("解鎖花費:"+this.unLockCost+"榮譽").setTextFont(new Font("標楷體",Font.ITALIC,22));//顯示解鎖的畫面
            label.getPaintStyle().setTextColor(Color.RED);
        }else{

            label.getPaintStyle().setText("花費:"+this.cost+"榮譽").setTextFont(new Font("標楷體",Font.ITALIC,22));//顯示解鎖的畫面
            label.getPaintStyle().setTextColor(Color.WHITE);
        }
        if(label!=null  && infoVisable){
            label.paint(g);
        }
    }
}
