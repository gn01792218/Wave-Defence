package gameobj;
import utils.GameKernel;
import utils.Global;
import java.awt.*;
import java.util.ArrayList;
//管理遊戲物件的抽象父類
public abstract class GameObject implements GameKernel.UpdateInterface,GameKernel.PaintInterface{
    private final Rect collider;
    private final Rect painter;
    public GameObject(float x, float y, float width, float height) {
        this(x, y, width, height, x, y, width, height);
    }
    public GameObject(Rect rect) {
        collider = new Rect(rect);
        painter = new Rect(rect);
    }
    public GameObject(float x, float y, float width, float height,
                      float x2, float y2, float width2, float height2) {
        collider = Rect.genWithCenter(x, y, width, height);
        painter = Rect.genWithCenter(x2, y2, width2, height2);
    }

    public GameObject(Rect rect, Rect rect2) {
        collider = new Rect(rect);
        painter = new Rect(rect2);
    }

    public boolean outOfScreen() {
        if (painter.bottom() <= 0) {
            return true;
        }
        if (painter.right() <= 0) {
            return true;
        }
        if (painter.left() >= Global.SCREEN_X) {
            return true;
        }
        return painter.top() >= Global.SCREEN_Y;
    }
    //是否碰到場地邊界
    public boolean isTouchBattleEdge(){
        if(this.painter.centerX()<=Global.BOUNDARY_X1){return true;}
        if(this.painter.centerX()>=Global.BOUNDARY_X2){return true;}
        if(this.painter.centerY()<=Global.BOUNDARY_Y1){return true;}
        if(this.painter.centerY()<=Global.BOUNDARY_Y2){return true;}
        return false;
    }

    //四個方向的碰撞
    public boolean touchTop() {
        return collider.top() <= 0;
    }
    public boolean touchLeft() {
        return collider.left() <= 0;
    }
    public boolean touchRight() {
        return collider.right() >= Global.SCREEN_X;
    }
    public boolean touchBottom() {
        return collider.bottom() >= Global.SCREEN_Y;
    }

    public boolean isCollision(GameObject obj) {
        return collider.overlap(obj.collider);
    }
    //移動
    public final void translate(int x, int y) {
        collider.translate(x, y);
        painter.translate(x, y);
    }
    public final void translateX(int x) {
        collider.translateX(x);
        painter.translateX(x);
    }
    public final void translateY(int y) {
        collider.translateY(y);
        painter.translateY(y);
    }
    //碰撞
    public final Rect collider() {
        return collider;
    }
    public final Rect painter() {
        return painter;
    }

}
