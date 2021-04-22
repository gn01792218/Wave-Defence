package utils;

import controllers.ImageController;
import menu.BackgroundType;
import menu.Label;
import menu.Style;

import java.awt.*;

public class Player implements GameKernel.UpdateInterface,GameKernel.PaintInterface {
    //單例模式
    private  String name;
    private  int money; //金錢
    private  int honor; //榮譽值
    public   Label  moneyLabel;
    public   Label honorLabel;
    private static Player player;

    private Player(){
        money=2000;
        honor=2000;
        moneyLabel=new Label(1230,50,new Style.StyleRect(350,87,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/350.png"))).setTextFont(new Font("標楷體",Font.ITALIC,42)));
        honorLabel=new Label(1230,150,new Style.StyleRect(350,87,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/350.png"))).setTextFont(new Font("標楷體",Font.ITALIC,42)));
    }
    public static Player getInstance(){
        if(player==null){
            player=new Player();
        }
        return player;
    }

    public void setHonor(int honor) {
        this.honor = honor;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public  int getMoney(){return this.money;}
   public  int getHonor(){return this.honor;}
   public  void offsetMoney(int x){this.money+=x;}
   public  void offsetHonor(int x){this.honor+=x;}

    public  Label getMoneyLabel() {
        return this.moneyLabel;
    }
    public  Label getHonorLabel() {
        return this.honorLabel;
    }

    @Override
    public void paint(Graphics g) {
        moneyLabel.getPaintStyle().setText("玩家金錢: "+money);
        honorLabel.getPaintStyle().setText("玩家榮譽: "+honor);
        moneyLabel.paint(g);
        honorLabel.paint(g);
    }

    @Override
    public void update() {

    }
}
