package menu.impl;
//選滑鼠滑進去、點擊功能擇一

import menu.*;
import java.awt.event.MouseEvent;
import utils.CommandSolver;

//外面輸入xy(滑鼠該有反應的位置)
//使用者可以改變滑進去、點擊呈現出的style自訂
public class MouseTriggerImpl {

    private static boolean ovalOverlap(Label obj, int eX, int eY) {
        int r = (int) (Math.sqrt(Math.pow(obj.width() / 2, 2) - Math.pow(obj.height() / 2, 2)));
        int r1X = obj.getX() + obj.width() / 2 - r;
        int r2X = obj.getX() + obj.width() / 2 + r;
        int rY = obj.getY() + obj.height() / 2;
        int threePointDistance = (int) (Math.sqrt(Math.pow(r1X - eX, 2) + Math.pow(rY - eY, 2)) + Math.sqrt(Math.pow(r2X - eX, 2) + Math.pow(rY - eY, 2)));
        return threePointDistance <= obj.width();
    }

    private static boolean rectOverlap(Label obj, int eX, int eY) {
        return eX <= obj.right() && eX >= obj.left() && eY >= obj.top() && eY <= obj.bottom();
    }

    public static void mouseTrig(Label obj, MouseEvent e, CommandSolver.MouseState state) {
        boolean isOval = (obj.getPaintStyle() instanceof Style.StyleOval);
        if (state == CommandSolver.MouseState.RELEASED && (obj instanceof Button)) {
            obj.unFocus();
        }
        if (state == CommandSolver.MouseState.MOVED) {
            if (isOval) {
                if (ovalOverlap(obj, e.getX(), e.getY())) {
                    obj.isHover(true);
                } else {
                    obj.isHover(false);
                }
            } else {
                if (rectOverlap(obj, e.getX(), e.getY())) {
                    obj.isHover(true);
                } else {
                    obj.isHover(false);
                }
            }
        }

        if (state == CommandSolver.MouseState.PRESSED) {
            if (isOval) {
                if (ovalOverlap(obj, e.getX(), e.getY())) {
                    obj.isFocus();
                    if (obj.getClickedAction() != null) {
                        obj.clickedActionPerformed();
                    }
                } else {
                    obj.unFocus();
                }
            } else {
                if (rectOverlap(obj, e.getX(), e.getY())) {
                    obj.isFocus();
                    if (obj.getClickedAction() != null) {
                        obj.clickedActionPerformed();
                    }
                } else {
                    obj.unFocus();
                }
            }
        }
    }
}
