/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internet.server;

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class ServerTest {

    public static void main(String[] args) {
        Server.instance().create(12345);//new出Server實體,於建構時給埠號(PORT)，再以start()方法啟動
        Server.instance().start();
        System.out.println("主機IP：" + Server.instance().getLocalAddress()[0] + "\n主機PORT：" + Server.instance().getLocalAddress()[1]);
        Server.instance().close();
        Server.instance().create(12345);
        Server.instance().start();
        System.out.println("主機IP：" + Server.instance().getLocalAddress()[0] + "\n主機PORT：" + Server.instance().getLocalAddress()[1]);
    }
}
