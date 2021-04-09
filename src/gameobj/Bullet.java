package gameobj;

import controllers.ImageController;
import controllers.ImageOperate;
import utils.Delay;
import utils.GameKernel;
import utils.Global;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bullet implements GameKernel.UpdateInterface,GameKernel.PaintInterface {

    public class FloatRect {
        private float left;
        private float top;
        private float right;
        private float bottom;

        public FloatRect(float left, float top, float right, float bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public FloatRect(FloatRect floatRect){
            this.left = floatRect.left;
            this.top = floatRect.top;
            this.right = floatRect.right;
            this.bottom = floatRect.bottom;
        }

        public FloatRect genWithCenter(float x, float y, float width, float height){
            float left = x - width / 2;
            float right = left + width;
            float top = y - height / 2;
            float bottom = top + height;
            return new FloatRect(left, top, right, bottom);
        }

        public final boolean overlap(float left, float top, float right, float bottom){
            if (this.left() > right) {
                return false;
            }
            if (this.right() < left) {
                return false;
            }
            if (this.top() > bottom) {
                return false;
            }
            if (this.bottom() < top) {
                return false;
            }
            return true;
        }

        public final boolean overlap(FloatRect b){
            return overlap(b.left, b.top, b.right, b.bottom);
        }

        public float centerX(){
            return (left + right) / 2;
        }
        public float centerY(){
            return (top + bottom) / 2;
        }
        public float exactCenterX(){
            return (left + right) / 2f;
        }
        public float exactCenterY(){
            return (top + bottom) / 2f;
        }

        public final FloatRect translate(float dx, float dy){
            this.left += dx;
            this.right += dx;
            this.top += dy;
            this.bottom += dy;
            return this;
        }

        public final FloatRect translateX(float dx){
            this.left += dx;
            this.right += dx;
            return this;
        }
        public final FloatRect translateY(float dy){
            this.top += dy;
            this.bottom += dy;
            return this;
        }

        public float left() {
            return left;
        }

        public void setLeft(float left) {
            this.left = left;
        }

        public float top() {
            return top;
        }

        public void setTop(float top) {
            this.top = top;
        }

        public float right() {
            return right;
        }

        public void setRight(float right) {
            this.right = right;
        }

        public float bottom() {
            return bottom;
        }

        public void setBottom(float bottom) {
            this.bottom = bottom;
        }

        public float width(){
            return this.right - this.left;
        }

        public float height(){
            return this.bottom - this.top;
        }

        public final void setCenter(float x, float y){
            translate(x - centerX(), y - centerY());
        }
    }
    
    private BufferedImage image1;
    private BufferedImage image2;
    private Delay delay;
    private boolean isExplored;
    private FloatRect floatRect;
    public Global.Trigonometric trigonometric;

    public Bullet(int x, int y,int targetX,int targetY) {
        floatRect = new FloatRect(x, y, x+35, y+70);
        trigonometric = new Global.Trigonometric(x,y,targetX,targetY);
        this.image1 = ImageOperate.rotateImage(ImageController.getInstance().tryGet("/bullet.png"),trigonometric.getDegree());

        this.image2 = ImageController.getInstance().tryGet("/boomb.png");
        delay = new Delay(120);
        isExplored = false;
    }

    public void explored(){
        this.isExplored = true;
    }

    @Override
    public void paint(Graphics g) {
        if(isExplored){
            g.drawImage(image2,(int)(this.floatRect.centerX()),(int)(this.floatRect.centerY()),null);
        }else{
            g.drawImage(image1,(int)(this.floatRect.centerX()),(int)(this.floatRect.centerY()),null);
        }
    }

    @Override
    public void update() {
        this.floatRect.translate(Global.BULLET_SPEED * this.trigonometric.getxVector(),
                Global.BULLET_SPEED * this.trigonometric.getyVector());
    }
}
