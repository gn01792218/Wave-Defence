package scene;

import controllers.AudioResourceController;
import controllers.ImageController;
import controllers.SceneController;
import menu.Label;
import menu.Style;
import utils.CommandSolver;
import utils.Global;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LeaderboardScene extends Scene{
    private BufferedImage backGround;//背景圖場景
    private BufferedImage leaderBoard;//排行榜條
    private ArrayList<menu.Label> labelsList;
    private Label title;//標題


    @Override
    public void sceneBegin() {
        backGround = ImageController.getInstance().tryGet("/UserSceneBack.png");
        leaderBoard=ImageController.getInstance().tryGet("/leaderBoardBar.png");
        title=new Label(950,230,new Style.StyleRect(0,0,true,null).setText("玩家 / 波數").setTextFont(new Font("標楷體",Font.BOLD,42)).setTextColor(new Color(0xE5BF06)));
        labelsList=new ArrayList<>();
        for(int i=0;i<5;i++){
            if (i == 0) {
                menu.Label label = new menu.Label(950, 370, new Style.StyleRect(0, 0, true, null));
                labelsList.add(label);
            }else if(i==1){
                menu.Label label = new menu.Label(950, 465, new Style.StyleRect(0, 0, true, null));
                labelsList.add(label);
            }else {
                menu.Label label = new menu.Label(950, 350 + (100 * i), new Style.StyleRect(0, 0, true, null));
                labelsList.add(label);
            }
        }
    }
    @Override
    public void sceneEnd() {
        backGround=null;
        leaderBoard=null;
        labelsList=null;
        title=null;
        AudioResourceController.getInstance().stop("/boomy-sizzling.wav");
    }
    @Override
    public CommandSolver.MouseListener mouseListener() {
        return new CommandSolver.MouseListener() {
            @Override
            public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
               if(state!=null){
                   switch (state){
                       case CLICKED:
                           if (e.getButton() == e.BUTTON1) {
                               SceneController.getInstance().changeScene(new OpenScene());
                           }
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
        g.drawImage(backGround,150,0,null);
        g.drawImage(leaderBoard,550,150,null);
        title.paint(g);
        for(int i=0;i<labelsList.size();i++){
            if(labelsList.get(i)!=null){
                //如果大於0才畫出
                if(Global.rankList.score.get(i)>0){
                    if(i==0){
                        labelsList.get(i).getPaintStyle().setText(Global.rankList.name.get(i) + " / " + Global.rankList.score.get(i))
                                .setTextFont(new Font("標楷體", Font.BOLD, 42));
                        labelsList.get(i).paint(g);
                    }else {
                        labelsList.get(i).getPaintStyle().setText(Global.rankList.name.get(i) + " / " + Global.rankList.score.get(i))
                                .setTextFont(new Font("標楷體", Font.ITALIC, 42));
                        labelsList.get(i).paint(g);
                    }
                }
            }
        }
    }

    @Override
    public void update() {

    }
}
