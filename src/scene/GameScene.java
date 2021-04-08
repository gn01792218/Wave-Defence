package scene;

import controllers.ImageController;

import controllers.ImageOperate;
import gameobj.Actor;
import gameobj.Enemy1;
import gameobj.Tank1;
import utils.CommandSolver;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameScene extends Scene {
    private BufferedImage image; //背景圖
    private ArrayList<Actor> alliance; //角色陣列
    private ArrayList<Actor> enemys; //敵軍



    @Override
    public void sceneBegin() {
        image=ImageController.getInstance().tryGet("/m2.png");
        alliance=new ArrayList<>();
        alliance.add(new Tank1(500,350));
        enemys=new ArrayList<>();
        enemys.add(new Enemy1(550,250));
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return null;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image,0,-150,null);
        alliance.get(0).paint(g);
        enemys.get(0).paint(g);
    }

    @Override
    public void update() {

    }
}
