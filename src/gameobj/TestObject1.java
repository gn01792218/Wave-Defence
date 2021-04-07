package gameobj;

import controllers.ImageController;

import java.awt.*;

public  class TestObject1 extends GameObject {
    private Image img;


    public TestObject1(int x, int y) {
        super(x, y, 44, 48);
        this.img = ImageController.getInstance().tryGet("/p1.png");

    }




    @Override
    public void update() {

    }

    @Override
    public void paintComponent(Graphics g) {

    }
}
