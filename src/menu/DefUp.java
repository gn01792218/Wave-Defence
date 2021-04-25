package menu;

import controllers.ImageController;
import gameobj.Actor;
import utils.Delay;
import utils.Global;
import utils.Player;

import java.awt.*;
import java.util.ArrayList;

public class DefUp extends SkillButton{
    private Label label;
    private final float defEffect=0.2f; //要增加幾成的防禦力
    private final int bufftime=300; //持續X/60秒
    private Label selectedLabel;//被選中後的按鈕

    public DefUp(int x, int y, Style style, Global.SkillName skillName, int cost){
        super(x,y,style,skillName,cost);
        this.buffTime=new Delay(bufftime);//增加防禦時間5秒
        info.getPaintStyle().setText("全體防禦力+"+defEffect+"("+defEffect*10+"成)，持續"+bufftime/60+"秒");
        this.label=new Label(this.getCenterX()+20,this.top()-20,new Style.StyleRect(10,10,true,null).setText("花費:"+this.cost+"榮譽").setTextFont(new Font("標楷體",Font.ITALIC,22)));
        infoVisable=false; //一開始不顯現
        this.isUnLocked=true;//一開始就是被解鎖的
        selectedLabel=new Label(getX(),getY(),new Style.StyleRect(64,64,true,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/SB-def2.png"))));
    }

    @Override
    public void skillExection(ArrayList<Actor> actors) {
        for (int i = 0; i < actors.size(); i++) {
            if(actors.get(i).isOnBuff()) {
                System.out.println("第" + (i + 1) + "台原始防禦力為" + actors.get(i).getDef());
                actors.get(i).setSkillName(this.getSkillName()); //將該角色身上的當前招式名稱更改
                actors.get(i).offsetDef(defEffect); //本身+0.2-->提高兩成防禦力 0.5+0.2=0.7
                actors.get(i).setOnBuff(true); //標示為Buff狀態
                System.out.println("第" + (i + 1) + "台增加防禦力為" + actors.get(i).getDef()); //測試用，外面要記得打印攻擊力測試時間內的攻擊力
            }
        }
        this.setCanUsed(false);
    }

    @Override
    public void skillReset(ArrayList<Actor> actors) {
        for(int i=0;i<actors.size();i++){
            actors.get(i).offsetDef(-defEffect);
            actors.get(i).setOnBuff(false); //標示為非Buff狀態
            System.out.println("防禦力回復原廠設定"+actors.get(i).getDef());
        }
        setUsed(true); //被施放過了
        if (Player.getInstance().getHonor() >= this.getCost()) {
            this.setCanUsed(true);
        }
        System.out.println("技能: "+this.getSkillName()+"施放結束");
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
//        if (Player.getInstance().getHonor() >= this.getCost()) {  //玩家錢小於這個技能的時候也不能使用
//            System.out.println(Player.getInstance().getHonor());
//            setCanUsed(true);
//            setUsed(false);
//        }
    }
}
