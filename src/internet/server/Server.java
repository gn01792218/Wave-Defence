package internet.server;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Server extends Thread {

    private static Server server;

    public static Server instance() {
        if (server == null) {
            server = new Server();
        }
        return server;
    }

    public class Client {

        String IP;
        int UDP_PORT;
        int id;

        public Client(String ipAddr, int UDP_PORT, int id) {
            this.IP = ipAddr;
            this.UDP_PORT = UDP_PORT;
            this.id = id;
        }
    }

    // 定義伺服器介面ServerSocket  
    public static int ID = 100;//給Client端的ID初始值
    public int TCP_PORT;//Server的TCP連接埠號
    public static final int UDP_PORT = 55556;//轉發Client端數據的UDP埠號
    public static final int DISCONNECT_PORT = 55557;//接收取消連線的UDP_PORT
    private ArrayList<Client> clients;//Client端ArrayList
    private DatagramSocket disConnectDatagramSocket;
    private DatagramSocket sendds;
    private ServerSocket ss;
    private boolean isStop;

    // 定義一個伺服器，定義埠 
    private Server() {
    }

    public void create(int port) {
        isStop = false;
        new Thread(new UDPThread()).start();//建立一個執行緒來收發訊息
        TCP_PORT = port;
        try {
            ss = new ServerSocket(TCP_PORT);//建立一個埠號為TCP_PORT的Socket的伺服器端
            disConnectDatagramSocket = new DatagramSocket(DISCONNECT_PORT);
            System.out.println("Server started...");
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new DisconnectThread()).start();
        clients = new ArrayList<>();
    }

    public void close() {
        if (ss == null) {
            return;
        }
        isStop = true;
        clients = null;
        try {
            ss.close();
            sendds.close();
            disConnectDatagramSocket.close();
            ID = 100;
            server = null;
        } catch (IOException ex) {

        }
    }

    public String[] getLocalAddress() {
        String strs[] = new String[2];
        try {
            strs[0] = InetAddress.getLocalHost().getHostAddress();
            strs[1] = "" + ss.getLocalPort();
        } catch (UnknownHostException ex) {
        }
        return strs;
    }

    @Override
    public void run() {
        while (!isStop) {
            Socket s = null;
            try {
                s = ss.accept();//取得和Client端連線的Socket物件
                System.out.println("A client has connected...");
                DataInputStream dis = new DataInputStream(s.getInputStream());//接收Client端資訊
                int udpport = dis.readInt();//讀取接收到的資訊，最先為Client端的UDP埠號
                Client client = new Client(s.getInetAddress().getHostAddress(), udpport, ID);//NEW一個Client物件(IP,UDP埠號,ID)
                clients.add(client);//將剛剛NEW出來的Client加入Client的ArrayList中
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeInt(ID++);//向Client端分配id
                dos.writeInt(this.UDP_PORT);//告訴Client端Server端的UDP埠號
                dos.writeInt(this.DISCONNECT_PORT);//告訴Client端取消連線的UDP埠號
            } catch (IOException e) {
                break;
            } finally {
                try {
                    if (s != null) {//若Client端斷線就關閉連接這個Client端的socket
                        s.close();
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }
    }

    private class DisconnectThread implements Runnable {

        byte[] buf = new byte[1024];

        @Override
        public void run() {
            while (null != disConnectDatagramSocket && !isStop) {//當收到的斷線資訊不為null時執行
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                ByteArrayInputStream bais = null;
                DataInputStream dis = null;
                try {
                    disConnectDatagramSocket.receive(dp);
                    bais = new ByteArrayInputStream(buf, 0, dp.getLength());
                    dis = new DataInputStream(bais);
                    String str = dis.readUTF();
                    String strarr[] = str.split(",");
                    if (clients != null) {
                        for (int i = 0; i < clients.size(); i++) {
                            Client c = clients.get(i);
                            if (Integer.parseInt(strarr[0]) == c.id) {//從連線中的client們找出此和斷線訊息相同id的client
                                System.out.println("ID：" + c.id + "disconnect");
                                clients.remove(c);//移除此client
                                break;
                            }
                        }
                        for (int i = 0; i < clients.size(); i++) {//向連線中的client們傳送斷線訊息
                            dp.setSocketAddress(new InetSocketAddress(clients.get(i).IP, clients.get(i).UDP_PORT));
                            disConnectDatagramSocket.send(dp);
                        }
                    }

                } catch (IOException e) {
                    break;
                } finally {
                    if (null != dis) {
                        try {
                            dis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != bais) {
                        try {
                            bais.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }

    private class UDPThread implements Runnable {

        byte[] buf = new byte[1024];

        @Override
        public void run() {
            try {
                sendds = new DatagramSocket(UDP_PORT);//NEW一個埠號為UDP_PORT代表UDP協議的Socket

            } catch (SocketException e) {
                e.printStackTrace();
            }

            while (null != sendds && !isStop) {
                DatagramPacket dp = new DatagramPacket(buf, buf.length);//new出用來接收長度為 buf.length 的數據包
                try {
                    sendds.receive(dp);//以DatagramPacket為載體接收訊息
                    for (int i = 0; i < clients.size(); i++) {
                        dp.setSocketAddress(new InetSocketAddress(clients.get(i).IP, clients.get(i).UDP_PORT));
                        sendds.send(dp);
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }
    }
}
