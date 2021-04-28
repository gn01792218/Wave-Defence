package menu;

import controllers.ImageController;
import gameobj.Actor;
import utils.Delay;
import utils.Global;
import utils.Player;

import java.awt.*;
import java.util.ArrayList;

public class SpeedUp extends SkillButton{
    private Label label;
    private final float speedEffect=8;
    private final int bufftime=480; //持續X/60秒
    private Label selectedLabel;//被選中後的按鈕
    public SpeedUp(int x, int y, Style style, Global.SkillName skillName, int cost){
        super(x,y,style,skillName,cost);
        this.buffTime=new Delay(bufftime); //速度增加持續六秒
        info.getPaintStyle().setText("全體移動+"+speedEffect+"持續"+bufftime/60+"秒").
                setTextFont(new Font("標楷體",Font.ITALIC,22));
        infoVisable=false; //一開始不顯現
        this.label=new Label(this.getCenterX()+20,this.top()-20,new Style.StyleRect(10,10,true,null).setText("花費:"+this.cost+"榮譽").setTextFont(new Font("標楷體",Font.ITALIC,22)));
        this.isUnLocked=true;//一開始就是被解鎖的
        selectedLabel=new Label(getX(),getY(),new Style.StyleRect(64,64,true,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/SB-speed2.png"))));
        this.isInGameScene=false;
    }
    @Override
    public void skillExection(ArrayList<Actor> actors) {
        for (int i = 0; i < actors.size(); i++) {
            System.out.println("第"+(i+1)+"台原始移動為"+actors.get(i).getSpeed());
            actors.get(i).setSkillName(this.getSkillName()); //將該角色身上的當前招式名稱更改
            actors.get(i).offsetSpeed(speedEffect); //本身移動速度+5
            actors.get(i).setOnBuff(true); //標示為Buff狀態
            System.out.println("第"+(i+1)+"台移動加到"+actors.get(i).getSpeed());
        }
        this.setCanUsed(false);
    }

    @Override
    public void skillReset(ArrayList<Actor> actors) {
        for(int i=0;i<actors.size();i++){
            for(int j=0;j<actors.get(i).getSkillNames().size();j++) {
                if (actors.get(i).getSkillNames().get(j) == this.getSkillName()) {
                    actors.get(i).offsetSpeed(-speedEffect); //把atk回復原廠設定
                    actors.get(i).setOnBuff(false); //標示為非Buff狀態
                    System.out.println("移動速度回復原廠設定" + actors.get(i).getSpeed());
                    actors.get(i).getSkillNames().remove(j);
                    j--;
                }
            }

        }
        setUsed(true); //被施放過了
        System.out.println("技能: "+this.getSkillName()+"施放結束");
            this.setCanUsed(true);
    }
    @Override
    public void paint(Graphics g){
        if(info!=null && infoVisable){info.paint(g);}  //招式資訊
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
        if(label!=null  && infoVisable && !isInGameScene){ //花費顯示
            label.paint(g);
        }
        if(isInSkillScene){
            info.getPaintStyle().setHeight(200);
        }
    }
    @Override
    public void update() {
    }


}
