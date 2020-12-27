package shanglei.wuziqi.program;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author: JeffreyJi
 * @date: 2020/12/27 8:47
 * @version: 1.0
 * @description: TODO
 */
public class ChessBoard extends JPanel {
    // 边距
    public static final int MARGIN = 15;
    // 网格边距
    public static final int SPAN = 20;
    // 棋盘行数
    public static final int ROWS = 14;
    // 棋盘列数
    public static final int COLS = 14;

    private Image img;

    Chess[] chessList = new Chess[(ROWS + 1) * (COLS + 1)];//初始每个数组元素为null
    boolean isBlack = true;//默认开始是黑旗先
    int chessCount = 0;//当前棋盘棋子的个数


    public ChessBoard() {
        img = Toolkit.getDefaultToolkit().getImage("image/board.jpg");
        this.addMouseListener(new MouseMonitor());
    }

    // 话棋盘
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this);
        for (int i = 0; i <= ROWS; i++) {
            g.drawLine(MARGIN, MARGIN + i * SPAN, MARGIN + COLS * SPAN, MARGIN + i * SPAN);
        }
        for (int i = 0; i <= COLS; i++) {
            g.drawLine(MARGIN + i * SPAN, MARGIN, MARGIN + i * SPAN, MARGIN + ROWS * SPAN);
        }
        g.fillRect(MARGIN + 3 * SPAN - 2, MARGIN + 3 * SPAN - 2, 5, 5);
        g.fillRect(MARGIN + COLS / 2 * SPAN - 2, MARGIN + 3 * SPAN - 2, 5, 5);
        g.fillRect(MARGIN + (COLS - 3) * SPAN - 2, MARGIN + 3 * SPAN - 2, 5, 5);

        g.fillRect(MARGIN + 3 * SPAN - 2, MARGIN + ROWS / 2 * SPAN - 2, 5, 5);
        g.fillRect(MARGIN + COLS / 2 * SPAN - 2, MARGIN + ROWS / 2 * SPAN - 2, 5, 5);
        g.fillRect(MARGIN + (COLS - 3) * SPAN - 2, MARGIN + ROWS / 2 * SPAN - 2, 5, 5);

        g.fillRect(MARGIN + 3 * SPAN - 2, MARGIN + (ROWS - 3) * SPAN - 2, 5, 5);
        g.fillRect(MARGIN + COLS / 2 * SPAN - 2, MARGIN + (ROWS - 3) * SPAN - 2, 5, 5);
        g.fillRect(MARGIN + (COLS - 3) * SPAN - 2, MARGIN + (ROWS - 3) * SPAN - 2, 5, 5);

        for (int i = 0; i < chessCount; i++) {
            chessList[i].draw(g);
            if (i == chessCount - 1) {// 如果是最后一个棋子
                // 网格交叉点x，y坐标
                int xPos = chessList[i].getCol() * SPAN + MARGIN;
                int yPos = chessList[i].getRow() * SPAN + MARGIN;
                g.setColor(Color.red);
                g.drawRect(xPos - Chess.DIAMETER / 2, yPos - Chess.DIAMETER / 2, Chess.DIAMETER, Chess.DIAMETER);
            }
        }

        Chess c1 = new Chess(this, 2, 1, Color.BLACK);
        Chess c2 = new Chess(this, 5, 2, Color.WHITE);
        c1.draw(g);
        c2.draw(g);

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(MARGIN * 2 + SPAN * COLS, MARGIN * 2 + SPAN * ROWS);
    }

    private boolean hasChess(int col, int row) {
        for (int i = 0; i < chessCount; i++) {
            Chess ch = chessList[i];
            if (ch != null && ch.getCol() == col && ch.getRow() == row) {
                return true;
            }
        }
        return false;
    }

    class MouseMonitor extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {// 鼠标在组件上按下时调用
            //讲鼠标点击的坐标位置转换成网络索引
            int col = (e.getX() - MARGIN + SPAN / 2) / SPAN;
            int row = (e.getY() - MARGIN + SPAN / 2) / SPAN;
            // 落在棋盘外不能下
            if (col < 0 || col > COLS || row < 0 || row > ROWS) {
                return;
            }
            // 如果x，y位置已经有棋子存在，不能下
            if (hasChess(col, row)) {
                return;
            }
            // 可以进行的处理
            Chess ch = new Chess(ChessBoard.this, col, row, isBlack ? Color.black : Color.white);
            chessList[chessCount++] = ch;
            // 通知系统重绘
            repaint();
            isBlack = !isBlack;
        }
    }
}
