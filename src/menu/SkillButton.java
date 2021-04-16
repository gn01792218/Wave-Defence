package menu;

import controllers.ImageController;
import gameobj.Actor;
import utils.Delay;
import utils.Global;

import java.awt.*;
import java.util.ArrayList;

public abstract class SkillButton extends Button { //目前只有UserScene場景中會用到，所以放在UserScene就好

    protected Button info;//技能資訊欄位。在User監聽滑鼠移動後，將角色按鈕設置成顯示Info true，否則為false；資訊欄都固定畫在左側。
    protected Label skillInfo;// 技能介紹。
    protected boolean infoVisable; //是否顯示資訊欄
    protected int cost;//技能所需榮譽點數
    protected boolean isSelect;// 被選中否
    protected Delay buffTime;//技能持續時間
    protected boolean isUsed; //是否被施放過了

    //在User場景中new就好了
    private Global.SkillName skillName;


    public SkillButton(int x, int y, Style style, Global.SkillName skillName, int cost) { //專門給指揮所使用
        super(x, y, style);
        this.skillName = skillName;
        this.cost=cost;
        this.isSelect=false;
        this.isUsed=false; //還沒被施放
        info=new Button(this.left(),this.top()-50,new Style.StyleRect(339,57,
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/350.png"))).setTextFont(new Font("標楷體",Font.ITALIC,22)));
    }
    public void setSelect(boolean isSelect){
        this.isSelect=isSelect;
    }
    public void setUsed(boolean used){this.isUsed=used;}

    public boolean isUsed() {
        return isUsed;
    }

    public boolean getIsSelect(){
        return this.isSelect;
    }
    public Global.SkillName getSkillName() {
        return skillName;
    }

    public Delay getBuffTime() {
        return buffTime;
    }
    //放技能的方式

    public abstract void skillExection(ArrayList<Actor> actors);
    public abstract void skillReset(ArrayList<Actor> actors);
    public void skillBufftimePlay(){
        this.buffTime.play();//開啟技能播放
    }
    public int getCost(){return this.cost;}
    public  void setInfo(Button info){this.info=info;};
    public void setInfoVisable(boolean visable){
        this.infoVisable=visable;
    }
    public  void setSkillInfo(Label skillInfo){this.skillInfo=skillInfo;};


    @Override
    public void paint(Graphics g){
        if (super.getPaintStyle() != null) {
            super.getPaintStyle().paintComponent(g, super.getX(), super.getY());
        }
        if(info!=null && infoVisable){info.paint(g);}
        if(skillInfo!=null){skillInfo.paint(g);}
    }

}
