package shanglei.net_wuziqi.program.wuziqi;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FiveClient extends Frame {
    PanelBoard board;
    PanelUserList userList;
    PanelMessage message;
    PanelTiming timing;
    PanelControl control;
    String myname;
    String opname;
    public boolean isConnected = false;
    Communication c;

    public static void main(String[] args) {
        FiveClient fc = new FiveClient();
    }

    public FiveClient() {
        super("五子棋客户端");
        board = new PanelBoard(this);
        this.add(board, BorderLayout.CENTER);

        timing = new PanelTiming();
        userList = new PanelUserList();
        //userList.setSize(200,200);
        message = new PanelMessage();
        Panel east = new Panel();
        east.setLayout(new BorderLayout());
        east.add(userList, BorderLayout.CENTER);
        east.add(message, BorderLayout.SOUTH);
        east.add(timing, BorderLayout.NORTH);
        this.add(east, BorderLayout.EAST);

        control = new PanelControl();
        this.add(control, BorderLayout.SOUTH);

        ActionMonitor monitor = new ActionMonitor();
        control.exitGameButton.addActionListener(monitor);
        control.connectButton.addActionListener(monitor);
        control.joinGameButton.addActionListener(monitor);
        control.cancelGameButton.addActionListener(monitor);

        this.setLocation(300, 100);
        pack();
        this.setResizable(false);
        this.setVisible(true);
    }

    class ActionMonitor implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == control.exitGameButton) {
                System.exit(0);
            } else if (e.getSource() == control.connectButton) {
                connect();
            } else if (e.getSource() == control.cancelGameButton) {

            } else if (e.getSource() == control.joinGameButton) {
                String select = userList.userList.getSelectedItem();
                System.out.println(select);
                if (select == null) {
                    message.mesageArea.append("请选择一个对手" + "\n");
                    return;
                }

                if (!select.endsWith("ready")) {
                    message.mesageArea.append("请选择一个已经Ready的对手" + "\n");
                    return;
                }

                if (select.startsWith(myname)) {
                    message.mesageArea.append("不能选自己作为对手" + "\n");
                    return;
                }

                int index = select.lastIndexOf(":");
                String name = select.substring(0, index);
                join(name);
            }
        }

    }

    public void join(String opponentName) {
        c.join(opponentName);
    }

    public void connect() {
        c = new Communication(this);
        String ip = control.inputIP.getText();
        c.connect(ip, FiveServer.TCP_PORT);
        message.mesageArea.append("已连接" + "\n");
        isConnected = true;
        control.exitGameButton.setEnabled(true);
        control.connectButton.setEnabled(false);
        control.joinGameButton.setEnabled(true);
        control.cancelGameButton.setEnabled(false);
    }
}
