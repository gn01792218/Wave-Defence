package scene;

import controllers.ImageController;
import menu.Style;
import utils.CommandSolver;
import utils.Global;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LeaderboardScene extends Scene{
    private BufferedImage backGround;//背景圖場景
    private BufferedImage leaderBoard;//排行榜條
    private ArrayList<menu.Label> labelsList;


    @Override
    public void sceneBegin() {
        backGround = ImageController.getInstance().tryGet("/UserSceneBack.png");
        leaderBoard=ImageController.getInstance().tryGet("/leaderBoardBar.png");
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
    }
    @Override
    public CommandSolver.MouseListener mouseListener() {
        return null;
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(backGround,150,0,null);
        g.drawImage(leaderBoard,550,150,null);
        for(int i=0;i<labelsList.size();i++){
            if(labelsList.get(i)!=null){
                labelsList.get(i).getPaintStyle().setText("玩家: "+ Global.rankList.name.get(i)+" 波數:"+Global.rankList.score.get(i))
                        .setTextFont(new Font("標楷體",Font.ITALIC,42));
                labelsList.get(i).paint(g);
            }
        }
    }

    @Override
    public void update() {

    }
}
