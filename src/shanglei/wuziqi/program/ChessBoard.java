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
    boolean isGamming = true;// 是否正在游戏


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

    private boolean hasChess(int col, int row, Color color) {
        for (int i = 0; i < chessCount; i++) {
            Chess ch = chessList[i];
            if (ch != null && ch.getCol() == col && ch.getRow() == row && ch.getColor() == color) {
                return true;
            }
        }
        return false;
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
//            if (!isGamming) {
//                return;
//            }
//            //讲鼠标点击的坐标位置转换成网络索引
//            int col = (e.getX() - MARGIN + SPAN / 2) / SPAN;
//            int row = (e.getY() - MARGIN + SPAN / 2) / SPAN;
//            // 落在棋盘外不能下
//            if (col < 0 || col > COLS || row < 0 || row > ROWS) {
//                return;
//            }
//            // 如果x，y位置已经有棋子存在，不能下
//            if (hasChess(col, row)) {
//                return;
//            }
//            // 可以进行的处理
//            Chess ch = new Chess(ChessBoard.this, col, row, isBlack ? Color.black : Color.white);
//            chessList[chessCount++] = ch;
//            // 通知系统重绘
//            repaint();
//            if (isWin(col, row)) {
//                String colorName = isBlack ? "黑棋" : "白棋";
//                String msg = String.format("恭喜，%s赢了！", colorName);
//                JOptionPane.showConfirmDialog(ChessBoard.this, msg);
//                isGamming = false;
//            }
//            isBlack = !isBlack;
        }

        @Override
        public void mouseReleased(MouseEvent e) {// 鼠标在组件上按下时调用
            if (!isGamming) {
                return;
            }
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
            if (isWin(col, row)) {
                String colorName = isBlack ? "黑棋" : "白棋";
                String msg = String.format("恭喜，%s赢了！", colorName);
                JOptionPane.showConfirmDialog(ChessBoard.this, msg);
                isGamming = false;
            }
            isBlack = !isBlack;
        }
    }

    private boolean isWin(int col, int row) {
        //连续棋子的个数
        int continueCount = 1;
        Color c = isBlack ? Color.black : Color.white;

        // 横向向左寻找
        for (int x = col - 1; x >= 0; x--) {
            if (hasChess(x, row, c)) {
                continueCount++;
            } else {
                break;
            }
        }

        // 横向向右寻找
        for (int x = col + 1; x <= COLS; x++) {
            if (hasChess(x, row, c)) {
                continueCount++;
            } else {
                break;
            }
        }
        if (continueCount >= 5) {
            return true;
        } else {
            continueCount = 1;
        }
        // 继续另一种搜索
        // 纵向向上搜索
        for (int y = row - 1; y >= 0; y--) {
            if (hasChess(col, y, c)) {
                continueCount++;
            } else {
                break;
            }
        }
        // 纵向向下搜索
        for (int y = row + 1; y <= ROWS; y++) {
            if (hasChess(col, y, c)) {
                continueCount++;
            } else {
                break;
            }
        }
        if (continueCount >= 5) {
            return true;
        } else {
            continueCount = 1;
        }

        // 继续另一种情况的搜索：右上到左下
        // 向右上搜索
        for (int x = col + 1, y = row - 1; y >= 0 && x <= COLS; x++, y--) {
            if (hasChess(x, y, c)) {
                continueCount++;
            } else {
                break;
            }
        }
        // 向左下搜索
        for (int x = col - 1, y = row + 1; y >= 0 && y <= ROWS; x--, y++) {
            if (hasChess(x, y, c)) {
                continueCount++;
            } else {
                break;
            }
        }
        if (continueCount >= 5) {
            return true;
        } else {
            continueCount = 1;
        }


        // 继续另一种情况的搜索：左上到右下
        // 向左上搜索
        for (int x = col - 1, y = row - 1; x >= 0 && y >= 0; x--, y--) {
            if (hasChess(x, y, c)) {
                continueCount++;
            } else {
                break;
            }
        }
        // 向右下搜索
        for (int x = col + 1, y = row + 1; x <= COLS && y <= ROWS; x++, y++) {
            if (hasChess(x, y, c)) {
                continueCount++;
            } else {
                break;
            }
        }
        if (continueCount >= 5) {
            return true;
        } else {
            continueCount = 1;
        }
        if (continueCount >= 5) {
            return true;
        } else {
            return false;
        }
    }
}
