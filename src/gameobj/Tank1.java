package gameobj;

import controllers.ImageController;

import java.awt.*;
import java.util.ArrayList;

public class Tank1 extends Actor{

    public Tank1(int x, int y){
        super(x,y,75,90);
        this.image= ImageController.getInstance().tryGet("/tank1.png");
        this.flag=null;
        this.dir=1;
    }

    public GameObject getTarget(ArrayList<GameObject> enemys){
        GameObject target = null;
        int min = 999999999;
        for(int i=0;i<enemys.size();i++){
            int xRange = enemys.get(i).collider().centerX() - this.collider().centerX();
            int yRange = enemys.get(i).collider().centerY() - this.collider().centerY();
            int c = xRange*xRange+yRange*yRange;
            //如果C邊
            if(c<min){
                target = enemys.get(i);
                min = c;
            }
        }
        return target;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image,this.painter().centerX(),this.painter().centerY(),null);
    }

    @Override
    public void update() {

    }


}
