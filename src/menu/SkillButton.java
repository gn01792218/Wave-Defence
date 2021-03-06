package menu;

import gameobj.Actor;
import utils.Delay;
import utils.Global;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class SkillButton extends Button { //目前只有UserScene場景中會用到，所以放在UserScene就好
    protected Button info;//技能資訊欄位。在User監聽滑鼠移動後，將角色按鈕設置成顯示Info true，否則為false；資訊欄都固定畫在左側。
    protected boolean infoVisable; //是否顯示資訊欄
    protected int cost;//技能所需榮譽點數
    protected boolean isSelect;// 被選中否
    protected Delay buffTime;//技能持續時間
    protected boolean isUsed; //是否被施放過了
    protected boolean isUnLocked;// 是否被解鎖了-->需要再弄
    protected int unLockCost;//解鎖的錢-->需要再弄
    protected boolean canUsed;//是否可以使用
    protected boolean isInGameScene;//是否是在故事模式
    protected boolean isInSkillScene;//是否在購買技能區



    //在User場景中new就好了
    private Global.SkillName skillName;


    public SkillButton(int x, int y, Style style, Global.SkillName skillName, int cost) { //專門給指揮所使用
        super(x, y, style);
        this.skillName = skillName;
        this.cost=cost;
        this.isSelect=false;
        this.isUsed=false; //還沒被施放
        isInSkillScene=false;
        this.canUsed=true;
        if(isInSkillScene){
            info=new Button(this.left()+10,this.bottom()+50,new Style.StyleRect(100,60,
                    null).setTextFont(new Font("標楷體",Font.ITALIC,22)));

        }else {
                info=new Button(this.left()+10,this.bottom(),new Style.StyleRect(100,60,
                        null).setTextFont(new Font("標楷體",Font.ITALIC,22)));}


    }

    public boolean getCanUsed() {
        return canUsed;
    }

    public boolean isInGameScene() {
        return isInGameScene;
    }

    public void setInSkillScene(boolean inSkillScene) {
        isInSkillScene = inSkillScene;
    }

    public void setInGameScene(boolean inGameScene) {
        isInGameScene = inGameScene;
    }

    public void setCanUsed(boolean canUsed) {
        this.canUsed = canUsed;
    }

    public boolean isUnLocked() {
        return isUnLocked;
    }

    public int getUnLockCost() {
        return unLockCost;
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

    public void setUnLocked(boolean unLocked) {
        isUnLocked = unLocked;
    }


    public void setUnLockCost(int unLockCost) {
        this.unLockCost = unLockCost;
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


    @Override
    public void paint(Graphics g){

    }
}
