package shanglei.wuziqi.program;

import javax.swing.*;
import java.awt.*;

/**
 * @author: JeffreyJi
 * @date: 2020/12/27 8:41
 * @version: 1.0
 * @description: TODO
 */
public class Five extends JFrame {
    private JToolBar toolBar;
    private JButton startButton, backButton, exitButton;
    private ChessBoard boardPanel;

    public Five() {
        super("单机版五子棋-尚磊");
        toolBar = new JToolBar();
        startButton = new JButton("重新开始");
        backButton = new JButton("悔棋");
        exitButton = new JButton("退出");
        toolBar.add(startButton);
        toolBar.add(backButton);
        toolBar.add(exitButton);
        this.add(toolBar, BorderLayout.NORTH);

        boardPanel = new ChessBoard();
        this.add(boardPanel, BorderLayout.CENTER);
        this.setLocation(200, 200);
        this.pack();
        this.setResizable(false);

//        this.setBounds(200,200,300,200);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new Five();
    }
}
