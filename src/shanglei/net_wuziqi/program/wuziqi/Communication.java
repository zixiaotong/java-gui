package shanglei.net_wuziqi.program.wuziqi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Communication {
    FiveClient fc;
    Socket s;
    private DataInputStream dis;
    private DataOutputStream dos;

    public Communication(FiveClient fc) {
        this.fc = fc;
    }

    public void connect(String IP, int port) {
        try {
            s = new Socket(IP, port);
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            new ReceiveThread(s).start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ReceiveThread extends Thread {
        Socket s;
        private DataInputStream dis;
        private DataOutputStream dos;
        String msg;

        public ReceiveThread(Socket s) {
            this.s = s;
        }

        public void run() {
            while (true) {
                try {
                    dis = new DataInputStream(s.getInputStream());
                    dos = new DataOutputStream(s.getOutputStream());
                    msg = dis.readUTF();
                    String[] words = msg.split(":");
                    if (words[0].equals(Command.TELLNAME)) {
                        fc.myname = words[1];
                        fc.userList.userList.add(fc.myname + ":ready");
                        fc.timing.setMyName(fc.myname);
                        fc.message.mesageArea.append("My Name:" + fc.myname + "\n");
                    } else if (words[0].equals(Command.ADD)) {
                        fc.userList.userList.add(words[1] + ":" + words[2]);
                        fc.message.mesageArea.append(words[1] + ":" + words[2] + "\n");
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        }
    }
}
