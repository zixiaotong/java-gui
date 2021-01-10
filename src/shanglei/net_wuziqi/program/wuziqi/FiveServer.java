package shanglei.net_wuziqi.program.wuziqi;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class FiveServer extends Frame implements ActionListener {
    Label lStatus = new Label("当前连接数:", Label.LEFT);
    TextArea taMesage = new TextArea("", 22, 50, TextArea.SCROLLBARS_VERTICAL_ONLY);
    Button btServerClose = new Button("关闭服务器");

    ServerSocket ss = null;
    public static final int TCP_PORT = 4801;
    static int clientNum = 0;
    static int clientNameNum = 0;
    ArrayList<Client> clients = new ArrayList<>();

    public static void main(String[] args) {
        FiveServer fs = new FiveServer();
        fs.startServer();
    }

    public FiveServer() {
        super("Java五子棋服务器");

        btServerClose.addActionListener(this);

        add(lStatus, BorderLayout.NORTH);
        add(taMesage, BorderLayout.CENTER);
        add(btServerClose, BorderLayout.SOUTH);

        setLocation(400, 100);
        pack();
        setVisible(true);
        setResizable(false);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btServerClose) {
            System.exit(0);
        }
    }

    class Client {
        String name;
        Socket s;
        String state;
        Client opponent;

        public Client(String name, Socket s) {
            this.name = name;
            this.s = s;
            this.state = "ready";
            this.opponent = null;
        }
    }

    public void startServer() {
        try {
            ss = new ServerSocket(TCP_PORT);
            while (true) {
                Socket s = ss.accept();
                clientNum++;
                clientNameNum++;
                Client c = new Client("Player" + clientNameNum, s);
                clients.add(c);
                lStatus.setText("连接数" + clientNum);
                String msg = s.getInetAddress().getHostAddress() + "  Player" + clientNameNum + "" + "\n";
                taMesage.append(msg);
                tellName(c);
                addAllUserToMe(c);
                addMeToAllUser(c);
                new ClientThread(c).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tellName(Client c) {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(c.s.getOutputStream());
            dos.writeUTF("tellName" + ":" + c.name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addAllUserToMe(Client c) {
        DataOutputStream dos = null;
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i) != c) {
                try {
                    dos = new DataOutputStream(c.s.getOutputStream());
                    dos.writeUTF(Command.ADD + ":" + clients.get(i).name + ":" + clients.get(i).state);
                } catch (IOException e) {
                    // TODO: handle exception
                }
            }
        }
    }

    private void addMeToAllUser(Client c) {
        DataOutputStream dos = null;
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i) != c) {
                try {
                    dos = new DataOutputStream(clients.get(i).s.getOutputStream());
                    dos.writeUTF(Command.ADD + ":" + c.name + ":ready");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class ClientThread extends Thread {
        private Client c;
        private DataInputStream dis;
        private DataOutputStream dos;

        ClientThread(Client c) {
            this.c = c;
        }

        public void run() {
            while (true) {
                try {
                    dis = new DataInputStream(c.s.getInputStream());
                    String msg = dis.readUTF();
                    String[] words = msg.split(":");
                    if (words[0].equals(Command.JOIN)) {
                        String opponentName = words[1];
                        for (int i = 0; i < clients.size(); i++) {
                            if (clients.get(i).name.equals(opponentName)) {
                                dos = new DataOutputStream(clients.get(i).s.getOutputStream());
                                dos.writeUTF(Command.JOIN + ":" + c.name);
                                break;
                            }
                        }
                    } else if (words[0].equals(Command.REFUSE)) {
                        String opponentName = words[1];
                        for (int i = 0; i < clients.size(); i++) {
                            if (clients.get(i).name.equals(opponentName)) {
                                dos = new DataOutputStream(clients.get(i).s.getOutputStream());
                                dos.writeUTF(Command.REFUSE + ":" + c.name);
                                break;
                            }
                        }
                    } else if (words[0].equals(Command.AGREE)) {
                        c.state = "playing";
                        String opponentName = words[1];
                        int opponentIndex;
                        for (int i = 0; i < clients.size(); i++) {
                            if (clients.get(i).name.equals(opponentName)) {
                                clients.get(i).state = "playing";
                                clients.get(i).opponent = c;
                                c.opponent = clients.get(i);
                                break;
                            }
                        }

                        for (int i = 0; i < clients.size(); i++) {
                            dos = new DataOutputStream(clients.get(i).s.getOutputStream());
                            dos.writeUTF(Command.CHANGE + ":" + c.name + ":playing");
                            dos.writeUTF(Command.CHANGE + ":" + opponentName + ":playing");
                        }

                        int r = (int) (Math.random() * 2);
                        if (r == 0) {
                            dos = new DataOutputStream(c.s.getOutputStream());
                            dos.writeUTF(Command.GUESSCOLOR + ":black" + opponentName);
                            dos = new DataOutputStream(c.opponent.s.getOutputStream());
                            dos.writeUTF(Command.GUESSCOLOR + ":white" + c.name);
                        } else {
                            dos = new DataOutputStream(c.s.getOutputStream());
                            dos.writeUTF(Command.GUESSCOLOR + ":white" + opponentName);
                            dos = new DataOutputStream(c.opponent.s.getOutputStream());
                            dos.writeUTF(Command.GUESSCOLOR + ":black" + c.name);
                        }
                        taMesage.append(c.name + "playing\n");
                        taMesage.append(opponentName + "playing\n");

                    }
                } catch (IOException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
}