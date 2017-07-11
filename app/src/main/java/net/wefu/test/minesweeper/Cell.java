package net.wefu.test.minesweeper;

/**
 * Created by wefu on 2017-07-09.
 */
public class Cell {

    private boolean selected;
    private boolean flag;
    private boolean bomb;
    private int bombCount;


    public Cell(boolean selected, boolean bomb, int bombCount) {
        this.selected = selected;
        this.bomb = bomb;
        this.bombCount = bombCount;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getBombCount() {
        return bombCount;
    }

    public void setBombCount(int bombCount) {
        this.bombCount = bombCount;
    }

    public boolean isBomb() {
        return bomb;
    }

    public void setBomb(boolean bomb) {
        this.bomb = bomb;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
