package menu;

import gameobj.Actor;
import utils.Global;

import java.awt.*;
import java.util.ArrayList;

public abstract class SkillButton extends Button { //目前只有UserScene場景中會用到，所以放在UserScene就好

    private Button info;//技能資訊欄位。在User監聽滑鼠移動後，將角色按鈕設置成顯示Info true，否則為false；資訊欄都固定畫在左側。
    private Label skillInfo;// 技能介紹。
    protected int cost;//技能所需榮譽點數
    protected boolean isSelect;// 被選中否

    //在User場景中new就好了
    private Global.SkillName skillName;
    public SkillButton(int x, int y, Style style, Global.SkillName skillName, int cost) {
        super(x, y, style);
        this.skillName = skillName;
        this.cost=cost;
        this.isSelect=false;
    }
    public void setSelect(boolean isSelect){
        this.isSelect=isSelect;
    }

    public boolean getIsSelect(){
        return this.isSelect;
    }
    public Global.SkillName getSkillName() {
        return skillName;
    }
    //放技能的方式
    public abstract void skillExection(ArrayList<Actor> actors);
    public  void setInfo(Button info){this.info=info;};
    public  void setSkillInfo(Label skillInfo){this.skillInfo=skillInfo;};
    @Override
    public void paint(Graphics g){
        if (super.getPaintStyle() != null) {
            super.getPaintStyle().paintComponent(g, super.getX(), super.getY());
        }
        if(info!=null){info.paint(g);}
        if(skillInfo!=null){skillInfo.paint(g);}
    }
}
