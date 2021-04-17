package scene;

import controllers.ImageController;
import controllers.SceneController;
import menu.*;
import menu.Button;
import utils.CommandSolver;
import utils.Global;
import utils.Player;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;
//角色資訊欄位-->裡面有多個labl(放攻擊 防禦 等等)。
//需要技能顯示資訊
//做確認訊息視窗
//買技能要扣榮譽值

public class UserScene extends Scene{
    private ArrayList<ActorButton> actorButtons;
    private ArrayList<SkillButton> skillButtons;
    private Button roundStart;// 進入回合的按鈕
    private Button secrt;//機密檔案(敵軍資料)按鈕

    @Override
    public void sceneBegin() {
        //進入回合的按鈕
        roundStart=new Button(1350,400,new Style.StyleRect(150,150,
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/start.png"))));
        secrt=new Button(1350, 600, new Style.StyleRect(500,308,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/secret-1.png"))));
        secrt.setStyleHover(new Style.StyleRect(548,356,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/secret-2.png"))));
            actorButtons=Global.getActorButtons();//得到Global的角色按鈕
            skillButtons=Global.getSkillButtons();//得到Global的技能按鈕
    }
    @Override
    public void sceneEnd() {
    }
    @Override
    public CommandSolver.MouseListener mouseListener() {
        return new CommandSolver.MouseListener() {
            @Override
            public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
                if(state!=null){
                    switch (state){
                        case MOVED: //負責監聽浮現的資訊欄
                           for(int i=0;i<actorButtons.size();i++){ //每個按鈕監聽滑鼠移動
                               if(actorButtons.get(i).isTouch(e.getX(),e.getY())){ //移動到角色上會有訊息欄
                                   //座標產生資訊圖片-->把角色圖片資訊設成visabl
                                   actorButtons.get(i).setInfoVisable(true);
                               }else{actorButtons.get(i).setInfoVisable(false);}
                           }
                           for(int i=0;i<skillButtons.size();i++){
                               if(skillButtons.get(i).isTouch(e.getX(),e.getY())){
                                   skillButtons.get(i).setInfoVisable(true);
                               }else{ skillButtons.get(i).setInfoVisable(false);}
                           }
                           if(secrt.isTouch(e.getX(),e.getY())){ //機密文件
                               secrt.isHover(true);
                           }else{secrt.isHover(false);}
                           break;
                        case CLICKED: //負責監聽升級和購買-->左鍵購買；右鍵升級
                            if(e.getButton()==1){ //左鍵
                                if(roundStart.isTouch(e.getX(),e.getY())){//1.觸發換場的按鈕
                                    SceneController.getInstance().changeScene(new GameScene());
                                }
                                for(int i=0;i<actorButtons.size();i++){ //2.角色購買
                                    if(actorButtons.get(i).isTouch(e.getX(),e.getY())){
                                        //產生確認框
                                        //購買軍隊
                                        if(Player.getInstance().getMoney()>0 && Player.getInstance().getMoney()>=actorButtons.get(i).getCostMoney()) { //金錢大於0才可以買唷!-->問題:必須要現有的錢>要買的單位的錢
                                            actorButtons.get(i).offSetNumber(1); //點一下增加一單位
                                            switch (actorButtons.get(i).getActorType()){
                                                case TANK1:
                                                    Player.getInstance().offsetMoney(-actorButtons.get(i).getCostMoney());
                                                    break;
                                                case TANK2:
                                                    Player.getInstance().offsetMoney(-actorButtons.get(i).getCostMoney());
                                                    break;
                                                case LASERCAR:
                                                    Player.getInstance().offsetMoney(-actorButtons.get(i).getCostMoney());
                                                    break;
                                                case ENEMY1:
                                                    Player.getInstance().offsetMoney(-actorButtons.get(i).getCostMoney());
                                                    break;
                                            }
                                        }
                                    }
                                }
                                for(int i=0;i<skillButtons.size();i++){ //3.技能購買
                                    if(skillButtons.get(i).isTouch(e.getX(),e.getY()) && Player.getInstance().getHonor()>=skillButtons.get(i).getCost() &&
                                            !skillButtons.get(i).getIsSelect()){ //被點中 且有榮譽職 且還沒被點過時
                                        skillButtons.get(i).setSelect(true); //設定為被選中的，在場景中只要new出被選中的技能即可
                                        Player.getInstance().offsetHonor(-skillButtons.get(i).getCost()); //扣榮譽值!
                                        System.out.println("我被選中了");
                                    }
                                    //若榮譽值夠，要可以重複買同樣技能，開啟此
//                                    if(skillButtons.get(i).isTouch(e.getX(),e.getY()) && Player.getInstance().getHonor()>=skillButtons.get(i).getCost()){ //被點中 且有榮譽職 且還沒被點過時
//                                        skillButtons.get(i).setSelect(true); //設定為被選中的，在場景中只要new出被選中的技能即可
//                                        Player.getInstance().offsetHonor(-skillButtons.get(i).getCost()); //扣榮譽值!
//                                        System.out.println("我被選中了");
//                                    }
                                }
                            }
                            if(e.getButton()==3){//點擊右鍵
                                for(int i=0;i<actorButtons.size();i++){ //1.角色升級
                                    if(actorButtons.get(i).isTouch(e.getX(),e.getY())){
                                        //產生確認框
                                        //升級軍隊
                                    }
                                }
                            }
                            break;
                    }
                }
            }
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

    @Override
    public void paint(Graphics g) {
        Player.getInstance().paint(g); //畫出 玩家金錢和榮譽
        roundStart.paint(g); //畫出開始回合的按鈕
        secrt.paint(g);//化機密檔案
        for(int i=0;i<skillButtons.size();i++){
            skillButtons.get(i).paint(g);
        }
        for(int i=0;i<actorButtons.size();i++){
            actorButtons.get(i).paint(g);
        }

    }
    @Override
    public void update() {

    }

}
