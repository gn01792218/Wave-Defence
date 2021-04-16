package menu;

import controllers.ImageController;
import gameobj.Actor;
import utils.Global;
import java.awt.*;

import java.util.ArrayList;

//帶有角色種類的Button
//6種labl再加上可以++的標籤，點下去直接+Gloabl的Actor該屬性
public class ActorButton extends Button{
    private ArrayList<Actor> alliances=Global.getAllianceActors();//得到Global的角色
    private ArrayList<Actor> enemys=Global.getEnemyActors();//得到Enemy的角色

    private Global.ActorType actorType;
    private int number;//存放這個角色的數量
    private Label numberLabel;// 目前數量顯示器
    private Label costLabel;//需要花費的標籤
    private Label hpLimit;//hp上限資訊
    private Label atk;// 攻擊力資訊
    private Label def;//防禦力
    private Label atkdis;//攻擊距離
    private Label atkSpeed;//攻擊速度
    private Label speed;// 移動速度
    private int costMoney;//這種角色要花多少錢-->要改的話去Global改唷~~~~
    private Button info;//角色資訊欄位。在User監聽滑鼠移動後，將角色按鈕設置成顯示Info true，否則為false；資訊欄都固定畫在左側。
    private boolean infoVisable; //是否顯示資訊欄

    public ActorButton(int x, int y, Style style,Global.ActorType actorType,int cost) { //輸入的xy是左上角
        super(x, y, style);
        this.actorType=actorType;
        numberLabel=new Label(this.right()-200,this.top(),new Style.StyleRect(0,0,true,null));//畫在這個按鈕的右下方；不要設寬高，就可以精準畫在要的位置
        this.costMoney=cost;
        costLabel=new Label(this.right()-200,this.top()+100,new Style.StyleRect(0,0,true,null));
        info=new Button(this.left(),this.bottom()-200,new Style.StyleRect(300,468
                ,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/InfoB2-300.png"))));
        infoVisable=false;

        for(int i=0;i<this.alliances.size();i++){
            if(alliances.get(i).getType()==this.actorType){
                for(int j=0;j<6;j++) {
                    if(j==0) {
                        Global.getFrmt().setMaximumFractionDigits(0);
                        hpLimit = new Label(this.left() + 60, this.top() + 330 + (j * 338 / 6), new Style.StyleRect(100, 100, true, null).
                                setText("血量上限 :" + Global.getFrmt().format(alliances.get(i).getHpLimit())).setTextFont(new Font("標楷體", Font.ITALIC, 32)));
                    }else if(j==1) {
                        Global.getFrmt().setMaximumFractionDigits(0);
                        atk = new Label(this.left() + 60, this.top() + 330 + (j * 338 / 6), new Style.StyleRect(100, 100, true, null).
                                setText("攻擊力 :" + Global.getFrmt().format(alliances.get(i).getAtk())).setTextFont(new Font("標楷體", Font.ITALIC, 32)));
                    }else if(j==2){
                        def=new Label(this.left() + 60, this.top() + 330 + (j * 338 / 6), new Style.StyleRect(100, 100, true, null).
                                setText("防禦力 :" + alliances.get(i).getDef()).setTextFont(new Font("標楷體", Font.ITALIC, 32)));
                    }else if(j==3){
                        Global.getFrmt().setMaximumFractionDigits(0);
                        atkdis=new Label(this.left() + 60, this.top() + 330 + (j * 338 / 6), new Style.StyleRect(100, 100, true, null).
                                setText("攻擊距離 :" + Global.getFrmt().format(alliances.get(i).getAtkdis())).setTextFont(new Font("標楷體", Font.ITALIC, 32)));
                    }else if(j==4){
                        atkSpeed=new Label(this.left() + 60, this.top() + 330 + (j * 338 / 6), new Style.StyleRect(100, 100, true, null).
                                setText("攻擊速度 :" + 60/alliances.get(i).getAtkSpeed().getCountLimit()).setTextFont(new Font("標楷體", Font.ITALIC, 32)));
                    }else{
                        Global.getFrmt().setMaximumFractionDigits(2);
                        speed=new Label(this.left() + 60, this.top() + 330 + (j * 338 / 6), new Style.StyleRect(100, 100, true, null).
                                setText("移動速度 :"+Global.getFrmt().format(alliances.get(i).getSpeed())).setTextFont(new Font("標楷體", Font.ITALIC, 32)));
                    }
                }


            }
        }
    }

    //方法
    public void setInfoVisable(boolean visable){
        this.infoVisable=visable;
    }
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
            costLabel.getPaintStyle().setText("花費: "+costMoney);
            costLabel.paint(g);
        }
        if(infoVisable){ //是顯示的時候才要畫出來
            info.paint(g);
            //畫出訊息
            hpLimit.paint(g);
            atk.paint(g);
            def.paint(g);
            atkdis.paint(g);
            atkSpeed.paint(g);
            speed.paint(g);
        }
    }
}
