package gameobj;

import controllers.ImageController;

import java.awt.*;

public class TestObject3 extends GameObject {
    private Image img;


    public TestObject3(int x, int y) {
        super(x, y, 44, 48);
        this.img = ImageController.getInstance().tryGet("/p3.png");

    }



    @Override
    public void paintComponent(Graphics g) {

    }

    @Override
    public void update() {

    }

}
