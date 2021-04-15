package menu;

import java.awt.Graphics;

public class Button extends Label {
    //也可以設置文字囉!

    public Button(int x, int y, Style style) {
        super(x, y, style);
    } //輸入的xy是左上角
    public Button(int x, int y, Theme theme) {
        super(x, y, theme);
    }
    public boolean isTouch(float x,float y){
        if(x<=this.left()){return false;}
        if(x>=this.right()){return false;}
        if(y<=this.top()){return false;}
        if(y>=this.bottom()){return false;}
        return true;
    }
    public Button(int x, int y) {
        super(x, y);
    }
    @Override
    public void paint(Graphics g) {
        if (super.getPaintStyle() != null) {
            super.getPaintStyle().paintComponent(g, super.getX(), super.getY()); //會畫在中心點
        }
    }
    @Override
    public void update() {

    }

}
