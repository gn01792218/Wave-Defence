package gameobj;

import controllers.ImageController;

import java.awt.*;

public class TestObject2 extends GameObject {

    private Image img;


    public TestObject2(int x, int y) {
        super(x, y, 44, 48);
        this.img = ImageController.getInstance().tryGet("/p2.png");

    }




    @Override
    public void update() {

    }

    @Override
    public void paintComponent(Graphics g) {

    }
}
