package shanglei.saolei.program;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MinePanel extends JPanel {
    public final int GRID_WIDTH = 20;  //方格的宽度
    public final int GRID_HEIGHT = 20; //方格的高度
    private MineFrame mf;
    private int cols;   //雷区的列数
    private int rows;  //雷区的行数
    private int mines;  //雷区的雷数
    private int remainedMines;  //未标记的雷数
    private int openedBlocks;   //已经翻开的方块数
    private Block[][] blocks;    //方块数组
    UpdateTimeTask utt;

    public MinePanel(MineFrame mf, int cols, int rows, int mines) {
        super();
        this.mf = mf;
        initMinePanel(rows, cols, mines);
        this.addMouseListener(new MouseMonitor());
        this.setBackground(new Color(210, 210, 210));
    }

    /**
     * 初始化参数
     */
    public void initMinePanel(int rows, int cols, int mines) {
        this.cols = cols;
        this.rows = rows;
        this.mines = mines;
        remainedMines = mines;
        openedBlocks = 0;
        createBlocks();
        layMines();
        repaint();
    }

    /**
     * 创建小方块
     */
    private void createBlocks() {
        blocks = new Block[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                blocks[i][j] = new Block(this, i, j, BlockType.ZERO, BlockState.ORIGINAL);
            }
        }
    }

    /**
     * 随机布雷
     */
    private void layMines() {
        int r;
        int c;
        // 初始化雷区
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                blocks[i][j].setType(BlockType.ZERO);
                blocks[i][j].setState(BlockState.ORIGINAL);
            }
        }
        // 随机布雷
        int m = 0;
        while (m < mines) {
            r = (int) (Math.random() * rows);
            c = (int) (Math.random() * cols);
            if (blocks[r][c].getType() != BlockType.MINE) {
                blocks[r][c].setType(BlockType.MINE);
                m++;
            }
        }
        // 计算每个block周围的雷数
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (blocks[i][j].getType() != BlockType.MINE) {
                    countMines(i, j);
                }
            }
        }
    }

    /**
     * 计算指定方块周围的雷数
     */

    private void countMines(int row, int col) {
        int mineNumber = 0;// blocks[i][j]周围的雷数
        for (int i = row - 1; i <= row + 1; i++) {
            if ((i >= 0) && (i < rows)) {// 在block的周围搜索
                for (int j = col - 1; j <= col + 1; j++) {
                    // 判断没有越界
                    if ((j >= 0) && (j < cols)) {
                        if (blocks[i][j].getType() == BlockType.MINE) {// 是雷
                            mineNumber++;
                        }
                    }
                }
            }
        }
        blocks[row][col].setType(mineNumber);
    }

    /**
     * 画出雷区
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                blocks[i][j].draw(g);
            }
        }
    }

    public void open(int row, int col) {
        if (blocks[row][col].getState() == BlockState.ORIGINAL) {
            if (blocks[row][col].open()) {
                openedBlocks++;
                if (openedBlocks == rows * cols - mines) {
                    wins();
                }
                if (blocks[row][col].getType() == BlockType.ZERO) {
                    search(row, col);
                }
            } else {
                lose(row, col);
            }
        }
    }

    public void wins() {
        utt.cancel();
        mf.setGamming(false);
        mf.setStoped(true);
        JOptionPane.showMessageDialog(this, "恭喜，扫雷成功");
    }

    public void lose(int row, int col) {
        int i, j;
        utt.cancel();
        for (i = 0; i < rows; i++) {
            for (j = 0; j < cols; j++) {
                if (blocks[i][j].getType() == BlockType.MINE && blocks[i][j].getState() != BlockState.MINE_FLAG) {
                    blocks[i][j].setState(BlockState.OPEN);
                    blocks[i][j].draw(MinePanel.this.getGraphics());
                }
            }
        }
        blocks[row][col].setState(BlockState.EXPLODED);
        blocks[row][col].draw(MinePanel.this.getGraphics());
        mf.setGamming(false);
        mf.setStoped(true);
        JOptionPane.showMessageDialog(this, "扫雷失败,继续努力");
    }

    public void search(int row, int col) {
        int i, j;
        for (i = row - 1; i < row + 1; i++) {
            if ((i < 0) || (i >= rows)) {
                continue;
            }
            for (j = col - 1; j <= col + 1; j++) {
                if ((j < 0) || (j >= cols)) {
                    continue;
                }
                if (blocks[i][j].getState() == BlockState.ORIGINAL) {
                    blocks[i][j].open();
                    openedBlocks++;
                    if (openedBlocks == rows * cols - mines) {
                        wins();
                    }
                    if (blocks[i][j].getType() == BlockType.ZERO) {
                        search(i, j);
                    }
                }
            }
        }
    }

    class MouseMonitor extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent event) {
            int col = event.getX() / GRID_WIDTH;
            int row = event.getY() / GRID_HEIGHT;
            if (mf.isStoped()) {
                return;
            }

            if (!mf.isGamming()) {
                mf.setGamming(true);
                utt = new UpdateTimeTask(mf);
                Timer timer = new Timer();
                timer.schedule(utt, 1000, 1000);
            }
            // 如果是左键，翻开小方块
            if (event.getButton() == MouseEvent.BUTTON1) {
                open(row, col);
            } else if (event.getButton() == MouseEvent.BUTTON3) {
                if (blocks[row][col].getState() == BlockState.ORIGINAL) {
                    blocks[row][col].setState(BlockState.MINE_FLAG);
                    remainedMines--;
                    mf.setMinesRemaind(remainedMines);
                    blocks[row][col].draw(MinePanel.this.getGraphics());
                } else if (blocks[row][col].getState() == BlockState.MINE_FLAG) {
                    blocks[row][col].setState(BlockState.QUESTION_FLAG);
                    remainedMines++;
                    mf.setMinesRemaind(remainedMines);
                    blocks[row][col].draw(MinePanel.this.getGraphics());
                } else if (blocks[row][col].getState() == BlockState.QUESTION_FLAG) {
                    blocks[row][col].setState(BlockState.ORIGINAL);
                    blocks[row][col].draw(MinePanel.this.getGraphics());
                } else if (blocks[row][col].getState() == BlockState.OPEN) {
                    int flagNumber = 0;
                    for (int i = row - 1; i <= row + 1; i++) {
                        for (int j = col - 1; j <= col + 1; j++) {
                            if ((i >= 0) && (i < rows) && (j >= 0) && (j < cols)) {
                                if (blocks[i][j].getState() == BlockState.MINE_FLAG) {
                                    flagNumber++;
                                }
                            }
                        }
                    }

                    if (flagNumber == blocks[row][col].getType()) {
                        for (int i = row - 1; i <= row + 1; i++) {
                            for (int j = col - 1; j <= col + 1; j++) {
                                if ((i >= 0) && (i < rows) && (j >= 0) && (j < cols)) {
                                    open(i, j);
                                }
                            }
                        }
                    }
                }
            }

        }

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(cols * GRID_WIDTH, rows * GRID_HEIGHT);
    }

}
