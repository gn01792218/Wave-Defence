package menu;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import scene.Scene;
import utils.CommandSolver;
//彈跳視窗
public abstract class PopupWindow extends Scene {

    private boolean isShow;
    private boolean isCancelable;

    private int x;
    private int y;
    private int width;
    private int height;

    public PopupWindow(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isShow = false;
        this.isCancelable = false;
    }

    public void setCancelable() {
        isCancelable = true;
    }

    public void show() {
        isShow = true;
    }

    public void hide() {
        isShow = false;
    }

    public boolean isShow() {
        return this.isShow;
    }

    @Override
    public abstract void sceneBegin();

    @Override
    public abstract void sceneEnd();

    public abstract void paintWindow(Graphics g);

    @Override
    public final void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform tf = g2d.getTransform();
        g2d.translate(x, y);
        paintWindow(g);
        g2d.setTransform(tf);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public abstract void update();

    @Override
    public final CommandSolver.MouseListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime) -> {
            if (e == null) {
                return;
            }
            if (isCancelable) { //滑鼠點外面他會hide()
                isCancelableHide(e, state, trigTime);
            }
            e.translatePoint(-x, -y);
            mouseTrig(e, state, trigTime);
            if (isShow == false) {
                sceneEnd();
            }
        };
    }

    public void isCancelableHide(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
        if (state == CommandSolver.MouseState.PRESSED) {
            if (e.getX() < x || e.getX() > x + width || e.getY() < y || e.getY() > y + height) {
                hide();
            }
        }
    }

    protected abstract void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime);

}
