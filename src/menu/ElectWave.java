package menu;

import controllers.ImageController;
import gameobj.Actor;
import utils.Delay;
import utils.Global;
import utils.Player;

import java.awt.*;
import java.util.ArrayList;

public class ElectWave extends SkillButton{
    private Label lockLabel;// 鎖住的圖片
    private int bufftime=240; //敵軍全體暫停4秒
    private Label label;
    private Label selectedLabel;//被選中後的按鈕

    public ElectWave(int x, int y, Style style, Global.SkillName skillName, int cost){
        super(x,y,style,skillName,cost);
        this.buffTime=new Delay(bufftime);//增基攻擊力時間持續5秒
        info.getPaintStyle().setText("使敵軍全體當機"+bufftime/60+"秒");
        this.lockLabel=new Label(getX(),getY(),new Style.StyleRect(128,128,true,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/SLock2.png"))));
        this.unLockCost=250;//花500可以解鎖
        this.label=new Label(this.getCenterX()+20,this.top()-20,new Style.StyleRect(10,10,true,null));
        infoVisable=false; //一開始不顯現
        this.isUnLocked=false;//需要解鎖
        selectedLabel=new Label(getX(),getY(),new Style.StyleRect(64,64,true,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/SB-elecWave.png"))));

    }

    @Override
    public void skillExection(ArrayList<Actor> actors) { //放敵人進來
        for(int i=0;i<actors.size();i++){
            actors.get(i).setSkillName(this.getSkillName());
            actors.get(i).setOnDebuff(true);
            System.out.println(actors.get(i).getType()+"停止移動");
        }
        this.setCanUsed(false);
    }

    @Override
    public void skillReset(ArrayList<Actor> actors) {
        for(int i=0;i<actors.size();i++){
            if(actors.get(i).isOnDebuff()) {
                for(int j=0;j<actors.get(i).getSkillNames().size();j++) {
                    if (actors.get(i).getSkillNames().get(j) == this.getSkillName()) {
                        actors.get(i).setOnDebuff(false);
                        System.out.println(actors.get(i).getType() + "恢復移動");
                        actors.get(i).getSkillNames().remove(j);
                        j--;
                    }
                }
            }
        }
        setUsed(true); //被施放過了
        if (Player.getInstance().getHonor() >= this.getCost()) {
            this.setCanUsed(true);
        }
    }
    @Override
    public void paint(Graphics g){
        if(info!=null && infoVisable){info.paint(g);}
        if ((Player.getInstance().getHonor()<this.getCost()) || isSelect || !canUsed) {
            if(isInGameScene()){
                if (super.getPaintStyle() != null) { //畫原本的圖
                    super.getPaintStyle().paintComponent(g, super.getX(), super.getY());
                }

            }else{
                selectedLabel.paint(g);} //被選中後畫灰色圖
        }else{
            if (super.getPaintStyle() != null) { //畫原本的圖
                super.getPaintStyle().paintComponent(g, super.getX(), super.getY());
            }
        }
        if(!isUnLocked && lockLabel!=null){
            lockLabel.paint(g);
            label.getPaintStyle().setText("解鎖花費:"+this.unLockCost+"榮譽").setTextFont(new Font("標楷體",Font.ITALIC,22));//顯示解鎖的畫面
            label.getPaintStyle().setTextColor(Color.RED);
        }else{
            label.getPaintStyle().setText("花費:"+this.cost+"榮譽").setTextFont(new Font("標楷體",Font.ITALIC,22));//顯示解鎖的畫面
            label.getPaintStyle().setTextColor(Color.WHITE);
        }
        if(label!=null  && infoVisable && !isInGameScene){
            label.paint(g);
        }
        if(isInSkillScene){
            info.getPaintStyle().setHeight(200);
        }
    }
    @Override
    public void update() {
//        if (Player.getInstance().getHonor() >= this.getCost()) {  //玩家錢小於這個技能的時候也不能使用
//            System.out.println(Player.getInstance().getHonor());
//            setCanUsed(true);
//            setUsed(false);
//        }
    }
}
