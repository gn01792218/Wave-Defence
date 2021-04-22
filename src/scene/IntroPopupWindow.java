package scene;

import controllers.ImageController;
import controllers.SceneController;
import menu.BackgroundType;
import menu.Button;
import menu.PopupWindow;
import menu.Style;
import utils.CommandSolver;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

//按鈕必須要在外面畫才不會走鐘

public class IntroPopupWindow extends PopupWindow {
    private Button button;//確認按鈕
    private BufferedImage image;//教學圖片
    public IntroPopupWindow(int x, int y, int width, int height){
        super(x,y,width,height);

    }

    @Override
    public void sceneBegin() {
        button=new Button(1200,650,new Style.StyleRect(150,150, //畫出確認按鈕
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/start.png"))));
        image= ImageController.getInstance().tryGet("/intro.png");
    }
    @Override
    public void sceneEnd() {
        button=null;
        image=null;
    }

    public Button getButton() {
        return button;
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

    @Override
    public void paintWindow(Graphics g) {
        g.setColor(new Color(0xDC72696C, true));
        g.fillRect(super.getX(), super.getY(), super.getWidth(), super.getHeight());
        g.drawImage(image,this.getX()+500,this.getY()+300,null);
    }
    @Override
    public void update() {
    }
    @Override
    protected void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
        if(state!=null){
            switch (state){
                case CLICKED:
                    if(e.getButton()==1){
                        if(button.isTouch(e.getX(),e.getY())){
                            SceneController.getInstance().changeScene(new GameScene());
                        }
                    }
            }
        }
    }
}