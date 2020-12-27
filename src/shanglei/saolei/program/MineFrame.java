package shanglei.saolei.program;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MineFrame extends JFrame {
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem[] menuItems;
    String[] menuItemNames = {"初级", "中级", "高级", "自定义", "排行榜", "退出"};

    JTextField minesRemained;
    JButton reStart;
    JTextField timeUsed;
    Icon face;
    JPanel upPanel;

    MinePanel minePanel;
    private int rows;
    private int cols;
    private int mines;
    // 如果stoped为false 安吉可将gamming设置为true
    private boolean gamming;
    private boolean stoped;
    private int grade;

    public MineFrame() {
        createMenu();
        createUpPanel();
        initParameter(10, 10, 10);
        minePanel = new MinePanel(this, cols, rows, mines);
        Container c = this.getContentPane();
        c.add(upPanel, BorderLayout.NORTH);
        c.add(minePanel, BorderLayout.CENTER);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        // this.setSize(300,200);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

    public boolean isGamming() {
        return gamming;
    }

    public void setGamming(boolean gamming) {
        this.gamming = gamming;
    }

    public boolean isStoped() {
        return stoped;
    }

    public void setStoped(boolean stoped) {
        this.stoped = stoped;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    private void initParameter(int rows, int cols, int mines) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        stoped = false;
        gamming = false;
        setTimeUsed(0);
        setMinesRemaind(mines);
    }

    private void createUpPanel() {
        minesRemained = new JTextField("0000");
        minesRemained.setEditable(false);
        timeUsed = new JTextField("0000");
        timeUsed.setEditable(false);
        face = new ImageIcon("image/smile.jpg");
        reStart = new JButton(face);
        reStart.addActionListener(new ButtonMonitor());
        JPanel center = new JPanel();
        JPanel right = new JPanel();
        JPanel left = new JPanel();
        center.add(reStart);
        left.add(minesRemained);
        right.add(timeUsed);
        upPanel = new JPanel(new BorderLayout());
        upPanel.add(left, BorderLayout.WEST);
        upPanel.add(center, BorderLayout.CENTER);
        upPanel.add(right, BorderLayout.EAST);
    }

    public void setMinesRemaind(int mines) {
        String strMines;
        if (mines > 9999) {
            strMines = "9999";
        } else if (mines / 10 == 0) {
            strMines = "000" + mines;
        } else if (mines / 100 == 0) {
            strMines = "00" + mines;
        } else if (mines / 1000 == 0) {
            strMines = "0" + mines;
        } else {
            strMines = "" + mines;
        }
        minesRemained.setText(strMines);
    }

    public void setTimeUsed(int second) {
        String strSecond;
        if (second > 9999) {
            strSecond = "9999";
        } else if (second / 10 == 0) {
            strSecond = "000" + second;
        } else if (second / 100 == 0) {
            strSecond = "00" + second;
        } else if (second / 1000 == 0) {
            strSecond = "0" + second;
        } else {
            strSecond = "" + second;
        }
        timeUsed.setText(strSecond);
    }

    private void createMenu() {
        menuBar = new JMenuBar();
        menu = new JMenu("游戏");
        menuItems = new JMenuItem[menuItemNames.length];
        for (int i = 0; i < menuItemNames.length; i++) {
            menuItems[i] = new JMenuItem(menuItemNames[i]);
            menu.add(menuItems[i]);
        }
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
        MenuMonitor mm = new MenuMonitor();
        for (int i = 0; i < menuItems.length; i++) {
            menuItems[i].addActionListener(mm);
        }
    }

    class ButtonMonitor implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (minePanel.utt != null) {
                minePanel.utt.cancel();
            }
            initParameter(rows, cols, mines);
            minePanel.initMinePanel(rows, cols, mines);
            minePanel.repaint();
            stoped = false;
            gamming = false;
        }

    }

    class MenuMonitor implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem mi = (JMenuItem) e.getSource();
            if (mi.equals(menuItems[5])) {
                System.exit(0);
            } else {
                if (minePanel.utt != null) {
                    minePanel.utt.cancel();
                }
                if (mi.equals(menuItems[0])) {
                    grade = Grade.LOWER;
                    initParameter(10, 10, 10);
                } else if (mi.equals(menuItems[1])) {
                    grade = Grade.MEDIAL;
                    initParameter(16, 16, 40);
                } else if (mi.equals(menuItems[2])) {
                    grade = Grade.HIGHER;
                    initParameter(16, 30, 99);
                }
                minePanel.initMinePanel(rows, cols, mines);
                pack();
                MineFrame.this.setLocationRelativeTo(null);
            }
        }
    }
}
