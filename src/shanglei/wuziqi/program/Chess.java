package shanglei.wuziqi.program;

import java.awt.*;
import java.awt.geom.Ellipse2D;

import static shanglei.wuziqi.program.ChessBoard.MARGIN;
import static shanglei.wuziqi.program.ChessBoard.SPAN;


/**
 * @author: JeffreyJi
 * @date: 2020/12/27 9:32
 * @version: 1.0
 * @description: TODO
 */
public class Chess {
    public static final int DIAMETER = SPAN - 2;
    private int col;
    private int row;
    private Color color;
    ChessBoard cb;

    public Chess(ChessBoard cb, int col, int row, Color color) {
        this.cb = cb;
        this.col = col;
        this.row = row;
        this.color = color;
    }

    public void draw(Graphics g) {
        int xPos = col * SPAN + MARGIN;
        int yPos = row * SPAN + MARGIN;

        //Color colorOld = g.getColor();
        //g.setColor(color);//设置颜色

        Graphics2D g2d = (Graphics2D) g;
        RadialGradientPaint paint = null;
        int x = xPos + DIAMETER / 4;
        int y = yPos - DIAMETER / 4;
        float[] f = {0f, 1f};
        Color[] c = {Color.WHITE, Color.BLACK};
        if (color == Color.black) {
            paint = new RadialGradientPaint(x, y, DIAMETER, f, c);
        } else if (color == Color.white) {
            paint = new RadialGradientPaint(x, y, DIAMETER * 2, f, c);

        }
        g2d.setPaint(paint);
        //以下两行使边界更均匀
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);

        Ellipse2D e = new Ellipse2D.Float(xPos - DIAMETER / 2, yPos - DIAMETER / 2, DIAMETER, DIAMETER);
        g2d.fill(e);
        //g.setColor(colorOld);
    }


    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ChessBoard getCb() {
        return cb;
    }

    public void setCb(ChessBoard cb) {
        this.cb = cb;
    }
}
