package utils;

import java.awt.*;
import java.awt.image.BufferStrategy;
//遊戲主核心，用來處理遊戲邏輯運算和繪圖
//內含兩個介面:繪圖介面和新介面
public class GameKernel extends Canvas {
    // 畫面更新時間
    private final long frameDeltaTime; //每次畫圖要幾奈秒
    private final long nsPerUpdates;// 每次運算要幾奈秒
    private UpdateInterface updateInterface;
    private PaintInterface pi;
    private CommandSolver cs;

    private GameKernel(final UpdateInterface ui, final PaintInterface pi, final CommandSolver.BuildStream buildStream, final int updatesPerSec, final int framePerSec) {
        this.updateInterface = ui;
        this.pi = pi;
        this.nsPerUpdates = 1000000000 / updatesPerSec; // 每秒等於10億奈秒/更新60次= 每一次更新要花幾奈秒
        this.frameDeltaTime = 1000000000 / framePerSec;  //10億奈秒/更新60禎= 每畫一禎需要幾奈秒
        this.cs = buildStream.bind(this, updatesPerSec);
    }

    private void paint() { //設置成私有，只有run可以用
        // 當沒有緩衝機制時我們便調用方法創建
        final BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        final Graphics g = bs.getDrawGraphics();// 從BufferStrategy中取出Graphics 緩衝機制會自行判斷並進行Cache處理
        g.fillRect(0, 0, this.getWidth(), this.getHeight());// 先畫一個跟畫布一樣大小的區塊
        // 利用 Graphics 進行繪圖
        if (this.pi != null) {
            this.pi.paint(g);
        }
        // end
        g.dispose();// 畫完之後釋放掉相關資源
        bs.show();// 畫出畫面
    }

    public void run() {
        this.cs.start();
        final long startTime = System.nanoTime();
        long passedUpdated = 0;
        long lastRepaintTime = System.nanoTime();
        int paintTimes = 0;
        long timer = System.nanoTime();

        while (true) {
            final long currentTime = System.nanoTime();// 系統當前時間
            final long totalTime = currentTime - startTime;// 從開始到現在經過的時間
            final long targetTotalUpdated = totalTime / this.nsPerUpdates;// 開始到現在應該更新的次數
            while (passedUpdated < targetTotalUpdated) {// 2.如果當前經過的次數小於實際應該要更新的次數
                //update 更新追上當前次數
                if (this.cs != null) {
                    this.cs.update();
                }
                if (this.updateInterface != null) {
                    this.updateInterface.update();
                }
                passedUpdated++;
            }
            if (currentTime - timer >= 1000000000) {  //3.印出FPS
                System.out.println("FPS: " + paintTimes);
                paintTimes = 0;
                timer = currentTime;
            }
            if (this.frameDeltaTime <= currentTime - lastRepaintTime) { //1.設定畫畫時機
                lastRepaintTime = currentTime;
                paint();
                paintTimes++;
            }
        }
    }

    @FunctionalInterface
    public interface PaintInterface {
        void paint(Graphics g);
    }

    @FunctionalInterface
    public interface UpdateInterface {
        void update();
    }

    public static class Builder {  //建造者模式
        private PaintInterface paintInterface;
        private UpdateInterface updateInterface;
        private CommandSolver.BuildStream buildStream;
        private int framePerSec;
        private int updatesPerSec;

        public Builder() {
            this.paintInterface = null;
            this.updateInterface = null;
            this.buildStream = null;
            this.framePerSec = this.updatesPerSec = 60;
        }

        public Builder paint(final PaintInterface paintInterface) {
            this.paintInterface = paintInterface;
            return this;
        }

        public Builder update(final UpdateInterface updateInterface) {
            this.updateInterface = updateInterface;
            return this;
        }

        public Builder fps(final int framePerSec) {
            this.framePerSec = framePerSec;
            return this;
        }

        public Builder ups(final int updatesPerSec) {
            this.updateInterface = this.updateInterface;
            return this;
        }

        public Builder input(final CommandSolver.BuildStream buildStream) {
            this.buildStream = buildStream;
            return this;
        }

        public GameKernel gen() {
            return new GameKernel(this.updateInterface
                    , this.paintInterface
                    , this.buildStream
                    , this.updatesPerSec
                    , this.framePerSec);
        }
    }
}

