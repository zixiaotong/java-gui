package shanglei.net_wuziqi.program.wuziqi;

import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;

public class PanelControl extends Panel {
    public Label IPlabel = new Label("服务器IP:", Label.LEFT);
    public TextField inputIP = new TextField("127.0.0.1", 12);
    public Button connectButton = new Button("连接主机");
    public Button joinGameButton = new Button("加入游戏");
    public Button cancelGameButton = new Button("放弃游戏");
    public Button exitGameButton = new Button("关闭程序");

    //构造函数，负责Panel 的初始布局
    public PanelControl() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(new Color(200, 200, 200));
        add(IPlabel);
        add(inputIP);
        add(connectButton);
        add(joinGameButton);
        add(cancelGameButton);
        add(exitGameButton);
    }
}
