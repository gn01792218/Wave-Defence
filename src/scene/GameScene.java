package scene;

import controllers.ImageController;

import utils.CommandSolver;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameScene extends Scene {
    private Image image;



    @Override
    public void sceneBegin() {
        image= ImageController.getInstance().tryGet("/map1.png");
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
        g.drawImage(image,-100,-220,null);
    }

    @Override
    public void update() {

    }
}
