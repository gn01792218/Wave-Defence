/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internet.server;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public interface CommandReceiver {
    public void receive(int serialNum, int commandCode, ArrayList<String> strs);
}
