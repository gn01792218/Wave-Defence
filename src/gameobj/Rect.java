package gameobj;

public class Rect {
    private float left;
    private float top;
    private float right;
    private float bottom;

    public Rect(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public Rect(Rect rect) {
        this.left = rect.left;
        this.top = rect.top;
        this.right = rect.right;
        this.bottom = rect.bottom;
    }

    public static Rect genWithCenter(float x, float y, float width, float height) {
        float left = x - width / 2;
        float right = left + width;
        float top = y - height / 2;
        float bottom = top + height;
        return new Rect(left, top, right, bottom);
    }

    public final boolean overlap(float left, float top, float right, float bottom) {
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

    public final boolean overlap(Rect b) {
        return overlap(b.left, b.top, b.right, b.bottom);
    }

    public float centerX() {
        return (left + right) / 2;
    }

    public float centerY() {
        return (top + bottom) / 2;
    }

    public float exactCenterX() {
        return (left + right) / 2f;
    }

    public float exactCenterY() {
        return (top + bottom) / 2f;
    }

    public final void offSet(float x, float y) {
        setCenter(centerX() + x, centerY() + y);
    }

    public final Rect translate(float dx, float dy) {
        this.left += dx;
        this.right += dx;
        this.top += dy;
        this.bottom += dy;
        return this;
    }
        public final Rect translateX( float dx){
            this.left += dx;
            this.right += dx;
            return this;
        }
        public final Rect translateY ( float dy){
            this.top += dy;
            this.bottom += dy;
            return this;
        }

        public float left () {
            return left;
        }

        public void setLeft ( float left){
            this.left = left;
        }

        public float top () {
            return top;
        }

        public void setTop ( float top){
            this.top = top;
        }

        public float right () {
            return right;
        }

        public void setRight ( float right){
            this.right = right;
        }

        public float bottom () {
            return bottom;
        }

        public void setBottom ( float bottom){
            this.bottom = bottom;
        }

        public float width () {
            return this.right - this.left;
        }

        public float height () {
            return this.bottom - this.top;
        }

        public final void setCenter (float x, float y){
            translate(x - centerX(), y - centerY());
        }

    }