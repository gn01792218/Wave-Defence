
import controllers.ImageController;
import controllers.SceneController;
import scene.*;
import utils.CommandSolver;
import utils.GameKernel;
import javax.swing.*;
import java.awt.event.KeyEvent;

public class Main {
    //遊戲建置步驟
    //1.導入遊戲素材包和CommandSolver-->Resource包和src平行的資料夾，並標記為Resource；CommandSolver在src之中
    //2.建置遊戲視窗-->用JFrame類建立
    //3.設置遊戲核心-->GameKernel繼承畫布Canvas專門處理paint和run機制
        //基礎屬性: 兩個介面的屬性  CommandSolver  畫一次圖/更新一次幾奈秒
        //3-1 建立paint和update介面  //3-2 處理繪畫雙緩衝機制 //3-3 處理run的遊戲迴圈，並確保遊戲邏輯運算不會落後於畫面
    //4.建立遊戲物件類-->GameObj類，是飛機 敵機 炸彈 等等類的抽象父類->因為要實現畫和update，但要推遲給子類去實現
        //子類自己有的屬性:例如 速度 方向 圖片屬性
    //5.建立場景類-->
       //5-1 Scene類是抽象父類(必要方法有繪畫和update以及開場和閉場)  //5-2 SceneController控制換場，實現繪畫及更新，及滑鼠鍵盤的介面 //5-3 GameScene繼承Scene，作為遊戲主場
    public static void main(String[] args) {
        JFrame jframe=new JFrame();
        SceneController sc=SceneController.getInstance(); //取得單例模式的控場實體
        sc.changeScene(new OpenScene()); //一開始使用開場畫面
        GameKernel gameKernel = new GameKernel.Builder().input(  //創建遊戲核心
                new CommandSolver.BuildStream().mouseTrack().subscribe(sc).keyboardTrack()
                        .add(KeyEvent.VK_ENTER,1) //設置ENTER按鍵為 1
                        .add(KeyEvent.VK_LEFT,2)//設置左箭頭為2
                        .add(KeyEvent.VK_RIGHT,3) //設置右箭頭為3
                        .add(KeyEvent.VK_X,4)
                        .add(KeyEvent.VK_A,5)
                        .add(KeyEvent.VK_E,6)
                        .add(KeyEvent.VK_R,7)
                        .add(KeyEvent.VK_Q,8)
                        .add(KeyEvent.VK_W,9)
                        .add(KeyEvent.VK_S,15)
                        .add(KeyEvent.VK_D,16)
                        .add(KeyEvent.VK_1,11)
                        .add(KeyEvent.VK_2,12)
                        .add(KeyEvent.VK_3,13)
                        .add(KeyEvent.VK_4,14)
                        .next().trackChar().keyTypedMode().subscribe(sc)
        ).paint(sc).update(sc).gen();
        jframe.setSize(1920,1080);
        jframe.setTitle("WaveDefence");
        jframe.setIconImage(ImageController.getInstance().tryGet("/Victory3.png"));
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //設置關閉時結束程式
        jframe.add(gameKernel);
        jframe.setVisible(true);
        gameKernel.run();
    }
}
