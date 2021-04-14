package scene;

import controllers.ImageController;
import controllers.SceneController;
import menu.*;
import menu.Button;
import menu.Label;
import utils.CommandSolver;
import utils.Global;
import utils.Player;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//角色資訊欄位-->裡面有多個labl(放攻擊 防禦 等等)。
//如何存放進Map? -->把角色按鈕放在Global中(自帶type和數量)，之後遊戲場景中也要有-->角色按鈕圖片要顯示目前數量

//需要角色物件陣列-->以取得角色資訊

public class UserScene extends Scene{
    private ArrayList<ActorButton> actorButtons=Global.getActorButtons(); //得到Global的角色按鈕

    private Map<Global.ActorType,Integer> allianceMap;// 儲存要什麼角色，要幾隻的訊息
    private Button roundStart;// 進入回合的按鈕


    @Override
    public void sceneBegin() {
        //進入回合的按鈕
        roundStart=new Button(900,500,new Style.StyleRect(500,500,new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/start.png"))));
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
                        case MOVED:
                           for(int i=0;i<actorButtons.size();i++){ //每個按鈕監聽滑鼠移動
                               if(actorButtons.get(i).isTouch(e.getX(),e.getY())){ //移動到角色上會有訊息欄
                                   //座標產生資訊圖片-->把角色圖片資訊設成visabl
                               }
                           }
                           break;
                        case CLICKED:
                            if(e.getButton()==1){ //點擊左鍵升級
                                if(roundStart.isTouch(e.getX(),e.getY())){
                                    SceneController.getInstance().changeScene(new GameScene()); //觸發換場的按鈕
                                }
                                for(int i=0;i<actorButtons.size();i++){ //每個按鈕監聽滑鼠移動
                                    if(actorButtons.get(i).isTouch(e.getX(),e.getY())){
                                        //產生確認框
                                        //升級軍隊

                                    }
                                }
                            }
                            if(e.getButton()==3){//點擊右鍵購買軍隊
                                for(int i=0;i<actorButtons.size();i++){ //每個按鈕監聽滑鼠移動
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
                                                    case ENEMY1:
                                                        Player.getInstance().offsetMoney(-actorButtons.get(i).getCostMoney());
                                                        break;
                                                }

                                        }
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
        for(int i=0;i<actorButtons.size();i++){
            actorButtons.get(i).paint(g);
        }
//        for(int i=0;i<actorInfoImage.size();i++){
//          //畫角色訊息圖片，if角色訊息圖是Visebal的時候
//
//        }
    }

    @Override
    public void update() {
        for(int i=0;i<actorButtons.size();i++) {
            System.out.println(Global.getActorButtons().get(i).getNumber()); //測試用
        }

    }
//    private class InfoImage{ //有角色按鈕；和角色資訊圖片
//        private String imageName; //圖片名稱
//        private boolean Visible;//可以看見否
//        private Map<Button,BufferedImage> actors;
//        public InfoImage(String imageName,BufferedImage image){
//            this.imageName=imageName;
//            this.Visible=false;
//        }
//        //設成顯示或不顯示
//        public void setVisible(boolean visible) {
//            Visible = visible;
//        }
//        public boolean isVisible() {
//            return Visible;
//        }
//        //取得圖片名稱
//        public String getImageName() {
//            return imageName;
//        }
//    }

}
