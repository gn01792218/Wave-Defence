package menu;

import controllers.ImageController;
import gameobj.Actor;

import utils.Delay;
import utils.Global;

import java.awt.*;
import java.util.ArrayList;

public class AttackUp extends SkillButton {
    private float atk;
    private final float atkEffect=1.5f;//要把攻擊力*幾倍
    private final int bufftime=300; //持續X/60秒
    private Label label;
    private Label selectedLabel;//被選中後的按鈕

    //圖畫的怪怪的!!
    public AttackUp(int x, int y, Style style, Global.SkillName skillName, int cost){
        super(x,y,style,skillName,cost);
        this.buffTime=new Delay(bufftime);//增基攻擊力時間持續5秒
        info.getPaintStyle().setText("全體攻擊力*"+atkEffect+"倍，持續"+bufftime/60+"秒");
        this.label=new Label(this.getCenterX(),this.bottom(),new Style.StyleRect(10,10,true,null).setText("花費:"+this.cost+"榮譽").setTextFont(new Font("標楷體",Font.ITALIC,22)));
        infoVisable=false; //一開始不顯現
        this.isUnLocked=true;//一開始就是被解鎖的
        selectedLabel=new Label(this.getCenterX()-64,this.getCenterY()-64,new Style.StyleRect(64,64,true,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/SB-attack.png"))));
    }
    @Override
    public void skillExection(ArrayList<Actor> actors) {  //施放技能，場景中會用到
        for (int i = 0; i < actors.size(); i++) {
            System.out.println("第"+(i+1)+"台原始攻擊力為"+actors.get(i).getAtk());
            this.atk=(float)actors.get(i).getAtk();
            actors.get(i).setSkillName(this.getSkillName()); //將該角色身上的當前招式名稱更改
            actors.get(i).offsetAtk(actors.get(i).getAtk()*atkEffect); //本身攻擊力*0.5  x+x*1.5
            actors.get(i).setOnBuff(true); //標示為Buff狀態
            System.out.println("第"+(i+1)+"台增加攻擊力為"+actors.get(i).getAtk()); //測試用，外面要記得打印攻擊力測試時間內的攻擊力
        }
    }
    @Override
    public void skillReset(ArrayList<Actor> actors) {   //時間到施放完畢要記得reSet技能
        for(int i=0;i<actors.size();i++){
            System.out.println("攻擊力目前為"+actors.get(i).getAtk());
            actors.get(i).offsetAtk(-atk*atkEffect); //把atk回復原廠設定 公式:(625-625*0.4=375)要減去的值
            actors.get(i).setOnBuff(false); //標示為非Buff狀態
            System.out.println("攻擊力回復原廠設定"+actors.get(i).getAtk());
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
        if(label!=null  && infoVisable){
            label.paint(g);
        }
    }
}
