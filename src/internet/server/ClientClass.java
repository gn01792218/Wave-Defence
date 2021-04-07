package internet.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientClass {

    private int UDP_PORT;//此Client的UDP Socket的埠號
    private String serverIP;//Setvet端的IP地址
    private int serverUDPPort;//Setvet端轉發客戶端UDP數據包的UDP埠號
    private int serverTCPPort;//server端埠號
    private int disconnectPort;//負責監聽玩家斷線的port
    private DatagramSocket ds = null;//Client端的UDP協議的Socket
    private int ID;//此Client的ID
    private Socket socket;
    private DataInputStream dataInputStream;//資料輸入流
    private Map<Integer, List<Command>> commandMap;
    private static ClientClass client;

    private ClientClass() {
        try {
            this.UDP_PORT = getRandomUDPPort();//隨機創造Client端UDP Socket的埠號
        } catch (Exception e) {
            System.exit(0);//如果重複就退出再重新隨機一次
        }
        commandMap = Collections.synchronizedMap(new HashMap<>());
    }

    public static ClientClass getInstance() {
        if (client == null) {
            client = new ClientClass();
        }
        return client;
    }

    private static class Command {

        private int num;
        private ArrayList<String> strs;

        public Command(int num, ArrayList<String> strs) {
            this.num = num;
            this.strs = strs;
        }

        @Override
        public String toString() {
            String tmp = "";
            tmp += num + ",";
            for (int i = 0; i < strs.size(); i++) {
                tmp += strs.get(i) + ",";
            }
            return tmp;
        }
    }

    public class UDPThread implements Runnable {

        byte[] buf = new byte[1024];

        @Override
        public void run() {
            while (null != ds) {
                DatagramPacket dp = new DatagramPacket(buf, buf.length);//創建出用來接收長度為 buf.length 的數據包
                try {
                    ds.receive(dp);//以DatagramPacket為載體接收ds內訊息
                    parse(dp);//解析訊息
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void parse(DatagramPacket dp) {
            ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0, dp.getLength());
            DataInputStream dis = new DataInputStream(bais);

            try {
                String str = dis.readUTF();
                String strarr[] = str.split(",");
//                if (IS_DEBUG) {
//                    System.out.println(str);
//                }
                int serialNum = Integer.parseInt(strarr[0]);//id

                int commandCode = Integer.parseInt(strarr[1]);//協議的編號

                ArrayList<String> content = new ArrayList<>();//參數
                for (int i = 2; i < strarr.length; i++) {
                    content.add(strarr[i]);
                }
                Command c = new Command(commandCode, content);
                if (commandMap.containsKey(serialNum)) {
                    commandMap.get(serialNum).add(c);//存到緩衝區
                } else {
                    List<Command> ll = Collections.synchronizedList(new LinkedList<>());
                    synchronized (ll) {
                        ll.add(c);
                        commandMap.put(serialNum, ll);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getID() {
        return ID;
    }

    public void connect(String host, int port) throws IOException {
        serverIP = host;
        serverTCPPort = port;
        socket = null;
        ds = new DatagramSocket(UDP_PORT);//創建埠號為UDP_PORT代表UDP協議的Socket
        socket = new Socket(serverIP, serverTCPPort);//創建TCP Socket 連接ip地址為serverIP 埠號為serverTCPPort
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());//創建資料輸入流
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());//創建資料輸出流
        dataOutputStream.writeInt(UDP_PORT);//向Server端發送此Client端的UDP埠號
        dataInputStream = new DataInputStream(socket.getInputStream());//跟120行重複
        int id = dataInputStream.readInt();//從Server端得到id
        this.serverUDPPort = dataInputStream.readInt();//從Server端得到該Server轉發客户端訊息的UDP埠號
        this.disconnectPort = dataInputStream.readInt();//從Server端得到該Sever轉發Client端訊息的UDPPort
        ID = id;//設定id
        System.out.println("connect to server successfully...");

        if (dataOutputStream != null) {
            dataOutputStream.close();//若資料輸出流不為空，關閉
        }

        new Thread(new UDPThread()).start();//開啟Client端UDP執行緒，用來向Server端發送/接收遊戲數據
    }

    public void disConnect() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeUTF(ID + ",-1");
            byte[] buf = baos.toByteArray();
            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(serverIP, disconnectPort));
            if (ds != null) {
                ds.send(dp);
            }
            if (socket != null) {
                socket.close();
            }
            ds = null;
            socket = null;
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != dos) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != baos) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void consume(CommandReceiver cr) {//消化緩衝區裡的指令
        if (commandMap != null) {
            for (int key : commandMap.keySet()) {
                List<Command> ll = commandMap.get(key);
                synchronized (ll) {
                    for (Command command : ll) {
                        cr.receive(key, command.num, command.strs);//執行指令
                    }
                    ll.clear();//執行完刪除指令
                }
            }
        }
    }

    private int getRandomUDPPort() {//隨機產生Client端UDP連接埠號
        return 55558 + (int) (Math.random() * 9000);
    }

    public void sent(int commandCode, ArrayList<String> strs) {
        if (socket != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(100);//指定大小, 避免字節太大
            DataOutputStream dos = new DataOutputStream(baos);

            try {
                dos.writeUTF(ID + "," + new Command(commandCode, strs).toString());//將Command及id組成資料包
            } catch (IOException ex) {
                Logger.getLogger(ClientClass.class.getName()).log(Level.SEVERE, null, ex);
            }

            byte[] buf = baos.toByteArray();
            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(serverIP, serverUDPPort));//設定數據包傳送大小，位置
            try {
                ds.send(dp);//發送數據包
            } catch (IOException ex) {
                Logger.getLogger(ClientClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
