package scene;

import utils.CommandSolver;

import java.awt.*;
//無盡模式
//敵人波數無限
//設置三種階級頭銜:  抵擋 5波以上->特戰隊長 7波以上->指揮官 9波以上->最高指揮官
//技能只有最初五種:1.不需要榮譽點數購買，每三波會更新一次(兩波內每招只能使用一次)。
//階級將會顯示在玩家的主畫面上

public class EndlessWaveScene extends Scene{
    @Override
    public void sceneBegin() {

    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return null;
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public void update() {

    }
}
