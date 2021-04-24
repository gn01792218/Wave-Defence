package menu;

import utils.*;
import java.awt.*;


import java.awt.event.KeyEvent;

public class EditText extends Label{

    private String defaultText;
    private Color defaultTextColor;
    private boolean isEditable; //是否為可編輯狀態

    //cursor
    private Delay cursorSpeed;
    private Color cursorColor;
    private boolean isCursorLight;
    private int cursorWidth;
    private int cursorHeight;

    //edit
    private int editLimit;
    private String editText;
    public boolean isTouch(float x,float y){
        if(x<=this.left()){return false;}
        if(x>=this.right()){return false;}
        if(y<=this.top()){return false;}
        if(y>=this.bottom()){return false;}
        return true;
    }

    private void init(String hint) {
        isEditable = true;
        this.defaultText = hint;
        defaultTextColor = Color.LIGHT_GRAY;

        //cursor
        cursorWidth = 3;
        cursorHeight = 15;
        cursorColor = Color.BLACK;
        cursorSpeed = new Delay(Global.UPDATE_TIMES_PER_SEC / 2);
        cursorSpeed.loop();
        isCursorLight = false;
        editLimit = -1;
        editText = "";
    }

    public EditText(int x, int y, String hint, Style styleRect) {
        super(x, y, styleRect);
        init(hint);
    }

    public EditText(int x, int y, String hint, Theme theme) {
        super(x, y, theme);
        init(hint);
    }

    public EditText(int x, int y, String hint) {
        super(x, y);
        init(hint);
    }

    public void changeCursorIsLight() {
        isCursorLight = !isCursorLight;
    }

    // 使用者更改游標長寬
    public void setCursorSize(int width) {
        cursorWidth = width;
    }

    // 使用者更改游標閃爍速度
    public void setCursorSpeed(int frame) {
        cursorSpeed.setLimit(frame);
    }

    // 使用者更改游標顏色
    public void setCursorColor(Color cursorColor) {
        this.cursorColor = cursorColor;
    }

    public String getDefaultText() {
        return defaultText;
    }

    // 設定預設提示字串
    public void setDefaultText(String hint) {
        this.defaultText = hint;
    }

    @Override
    public void isFocus() {
        super.isFocus();
    }

    public void unlockEdit() {
        isEditable = true;
    }

    // 關閉可編輯狀態
    public void lockEdit() {
        isEditable = false;
    }

    //設輸入字數限制
    public void setEditLimit(int n) {
        editLimit = n;
    }

    private boolean isOverEditLimit() {
        return !(editLimit == -1 || editText.length() < editLimit);
    }

    private boolean isExcepion(char c) {//例外允許輸入的特殊符號判定
        if (c >= 41 && c <=64
                || c >= 91 && c <= 95
                || c >= 124 && c < 126) {
            return true;
        }
        return false;
    }

    private boolean isLegalRange(char c) {
        if (c >= 65 && c <= 90) {
            return true;
        }
        return false;
    } //輸入英文判定

    private boolean isNumberpad(char c) {
        if (c >= 96 && c <= 111) {
            return true;
        }
        return false;
    } //輸入數字鍵盤判定

    private int keyNumbpadToNum(char c) {
        if (c >= 96 && c <= 105) {
            return c - 48;
        }
        switch (c) {
            case 106:
                return 42;
            case 107:
                return 43;
            case 108:
                return 13;
            case 109:
                return 45;
            case 110:
                return 46;
            case 111:
                return 47;
        }
        return -1;
    } //數字鍵盤對應

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setFont(this.getPaintStyle().getTextFont()); //預設提示文字與輸入文字字型大小一樣
        g.setColor((editText.length() == 0) ? defaultTextColor : this.getPaintStyle().getTextColor());
        g.drawString((editText.length() == 0 && getIsFocus()) ? defaultText : editText, super.getX(), super.getY() + super.height() / 2 + g.getFontMetrics().getDescent());

        if (super.getIsFocus() && isEditable) {
            if (cursorSpeed.count()) {
                changeCursorIsLight();
            }
            if (isCursorLight) {
                g.setColor(cursorColor);
                int stringWidth = g.getFontMetrics().stringWidth(editText);
                cursorHeight = g.getFontMetrics().getAscent();
                g.fillRect(super.getX() + 1 + stringWidth, super.getY() + super.height() / 2 - (cursorHeight + g.getFontMetrics().getDescent()) / 2, cursorWidth, cursorHeight);
            }
        }
    }

    public String getEditText() {
        return this.editText;
    }

    @Override
    public void update() {

    }

    public void keyTyped(char c) {
        System.out.println("char1 " + (int) c);
        if (getIsFocus() && isEditable) {
            if (c == KeyEvent.VK_ENTER) {
                super.unFocus();
            }
            if (!isOverEditLimit() && isExcepion(c)) {
                editText = editText + c;
                System.out.println("char2 " + (int) c);
            } else if (!isOverEditLimit() && isNumberpad(c)) {
                editText = editText + (char) keyNumbpadToNum(c);
            } else if (c == KeyEvent.VK_BACK_SPACE) {
                if (editText.length() > 0) {
                    editText = editText.substring(0, editText.length() - 1);
                }
            } else if (!isOverEditLimit() && isLegalRange(c) && Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
                editText = editText + c;
                System.out.println("char3 " + (int) c);
            } else if (!isOverEditLimit() && c >= 65 && c <= 90) {
                editText = editText + (char) (c + 32);
                System.out.println("char4 " + (int) c);
            }
        }
    }
}
