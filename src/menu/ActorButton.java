package menu;

import utils.Global;

import java.awt.*;

//帶有角色種類的Button
public class ActorButton extends Button{  //可以改成單例模式!!!!!!!!!!!!!!!!!!!!!!

    private Global.ActorType actorType;
    private int number;//存放這個角色的數量
    private Label numberLabel;// 目前數量顯示器
    private Label costLabel;//需要花費的標籤
    private int costMoney;//這種角色要花多少錢

    public ActorButton(int x, int y, Style style,Global.ActorType actorType,int cost) { //輸入的xy是左上角
        super(x, y, style);
        this.actorType=actorType;
        numberLabel=new Label(this.right(),this.bottom()-100,new Style.StyleRect(0,0,true,null));//畫在這個按鈕的右下方；不要設寬高，就可以精準畫在要的位置
        this.costMoney=cost;
        costLabel=new Label(this.right(),this.bottom()-200,new Style.StyleRect(0,0,true,null).setText("花費: "+costMoney));
    }

    //方法

    public int getCostMoney() {
        return costMoney;
    }

    public Global.ActorType getActorType(){
        return this.actorType;
    }
    public int getNumber() {
        return number;
    }
    public void offSetNumber(int number){
        this.number+=number;
    }
    //offsetNum
    public void offSetNum(int x){  //記得在場景中有我軍死掉被移除的話要--
        this.number+=x;
    }
    public Label getNumberInfo() {
        return numberLabel;
    }

    @Override
    public void paint(Graphics g){
        if (super.getPaintStyle() != null) {
            super.getPaintStyle().paintComponent(g, super.getX(), super.getY());
        }
        if(numberLabel!=null){
            numberLabel.getPaintStyle().setText("目前數量"+this.number+"");  //一定要用Style設置，才會顯示文字唷!!
            numberLabel.paint(g);
        }
        if(costLabel!=null) {
            costLabel.paint(g);
        }
    }
}
