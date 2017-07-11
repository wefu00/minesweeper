package net.wefu.test.minesweeper;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by wefu on 2017-07-09.
 */
public class Minesweeper {
    public static final int ARRAY_SIZE = 10;
    public static final int BOMB_SIZE = 10;

    private int[][] bombArrange;
    private int[] bombArray;

    private ArrayList<Integer> flagList = new ArrayList<>();
    private int selectedCount;

    public Minesweeper() {

    }


    public void bombArrange() {

        bombArray = new int[ARRAY_SIZE];
        Random random = new Random();
        flagList.clear();
        selectedCount = 0;

        for (int i = 0; i < bombArray.length; i++) {
            int randValue;

            do {
                randValue = random.nextInt(ARRAY_SIZE * ARRAY_SIZE);
            } while (arrangeCheck(i, randValue));

            bombArray[i] = randValue;
        }

        setBombCount();
    }

    private void setBombCount() {
        bombArrange = new int[ARRAY_SIZE][ARRAY_SIZE];

        for (int i = 0; i < bombArray.length; i++) {
            int row = bombArray[i] / ARRAY_SIZE;
            int col = bombArray[i] % ARRAY_SIZE;


            boolean rowIncrement = row + 1 <= ARRAY_SIZE-1;
            boolean rowDecrement = row - 1 >= 0;
            boolean colIncrement = col + 1 <= ARRAY_SIZE-1;
            boolean colDecrement = col - 1 >= 0;

            if (rowIncrement) {
                bombArrange[row+1][col] ++;

                if (colIncrement) {
                    bombArrange[row+1][col+1] ++;
                }

                if (colDecrement) {
                    bombArrange[row+1][col-1] ++;
                }
            }


            if (rowDecrement) {
                bombArrange[row-1][col] ++;

                if (colIncrement) {
                    bombArrange[row-1][col+1] ++;
                }

                if (colDecrement) {
                    bombArrange[row-1][col-1] ++;
                }
            }

            if (colIncrement) {
                bombArrange[row][col+1] ++;
            }

            if (colDecrement) {
                bombArrange[row][col-1] ++;
            }

            bombArrange[row][col] = ARRAY_SIZE + 1;
        }
    }

    private boolean arrangeCheck(int count, int value) {
        for (int i = 0; i < count; i++) {
            if (bombArray[i] == value) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Integer> getOpenCellIndexList(int index) {
        ArrayList<Integer> cellList = new ArrayList<>();

        int row = index / ARRAY_SIZE;
        int col = index % ARRAY_SIZE;


        if (bombArrange[row][col] > 0) {
            cellList.add(index);
            return cellList;
        }

        setOpenCellIndex(row, col, cellList);

        return cellList;
    }


    public void setOpenCellIndex(int row, int col, ArrayList<Integer> cellList) {
        int index = row*BOMB_SIZE+col;

        if (cellList.contains(index) || flagList.contains(index)) {
            return;
        }

        if (isBomb(row, col))
            return;

        cellList.add(index);

        if (bombArrange[row][col] > 0) {
            return;
        }

        boolean rowIncrement = row + 1 <= ARRAY_SIZE-1;
        boolean rowDecrement = row - 1 >= 0;
        boolean colIncrement = col + 1 <= ARRAY_SIZE-1;
        boolean colDecrement = col - 1 >= 0;

        if (rowIncrement) {
            setOpenCellIndex(row+1, col, cellList);

            if (colIncrement) {
                setOpenCellIndex(row+1, col+1, cellList);
            }

            if (colDecrement) {
                setOpenCellIndex(row+1, col-1, cellList);
            }
        }


        if (rowDecrement) {
            setOpenCellIndex(row-1, col, cellList);

            if (colIncrement) {
                setOpenCellIndex(row-1, col+1, cellList);
            }

            if (colDecrement) {
                setOpenCellIndex(row-1, col-1, cellList);
            }
        }

        if (colIncrement) {
            setOpenCellIndex(row, col+1, cellList);
        }

        if (colDecrement) {
            setOpenCellIndex(row, col-1, cellList);
        }
    }

    public boolean isBomb(int index) {
        int row = index / ARRAY_SIZE;
        int col = index % ARRAY_SIZE;

        return isBomb(row, col);
    }

    public boolean isBomb(int row, int col) {
        if (bombArrange[row][col] > ARRAY_SIZE) {
            return true;
        }

        return false;
    }

    public int getBombCount(int index) {
        int row = index / ARRAY_SIZE;
        int col = index % ARRAY_SIZE;

        return bombArrange[row][col];
    }

    public int getFlagCount() {
        return flagList.size();
    }

    public void addFlagIndex(int index) {
        flagList.add(index);
    }

    public void removeFlagIndex(Integer index) {
        flagList.remove(index);
    }

    public int getSelectedCount() {
        return selectedCount;
    }

    public void setSelectedCount(int selectedCount) {
        this.selectedCount = selectedCount;
    }
}
