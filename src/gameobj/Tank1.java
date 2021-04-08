package gameobj;

import controllers.ImageController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.awt.*;

public class Tank1 extends Actor{

    public Tank1(int x, int y){
        super(x,y,75,90);
        this.image= ImageController.getInstance().tryGet("/tank1.png");
        this.flag=null;
        this.dir=1;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image,this.painter().centerX(),this.painter().centerY(),null);
    }

    @Override
    public void update() {

    }


}
