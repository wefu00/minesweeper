package net.wefu.test.minesweeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

/**
 * Created by wefu on 2017-07-09.
 */
public class Minesweeper {
    public static final int ROW_SIZE = 12;
    public static final int COL_SIZE = 11;
    public static final int BOMB_SIZE = 130;

    private int[][] bombArrange;
    private HashMap<Integer, Integer> bombMap = new HashMap<>();

    private ArrayList<Integer> flagList = new ArrayList<>();
    private int selectedCount;
    private boolean reverse;

    public Minesweeper() {

    }


    public void bombArrange() {

        bombMap.clear();
        Random random = new Random();
        flagList.clear();
        selectedCount = 0;
        reverse = false;

        int bombSize = ((ROW_SIZE * COL_SIZE) / 2) > BOMB_SIZE ? BOMB_SIZE : (ROW_SIZE * COL_SIZE) - BOMB_SIZE;
        reverse = bombSize != BOMB_SIZE;

        for (int i = 0; i < bombSize; i++) {
            int randValue;

            do {
                randValue = random.nextInt(ROW_SIZE * COL_SIZE);
            } while (bombMap.get(randValue) != null);

            bombMap.put(randValue, randValue);
        }

        setBombCount();
    }

    private void setBombCount() {
        bombArrange = new int[ROW_SIZE][COL_SIZE];

        Set<Integer> bombSet = bombMap.keySet();

        for (Integer index : bombSet) {
            int row = index / COL_SIZE;
            int col = index % COL_SIZE;

            for (int j = -1; j <= 1; j++) {
                if (row + j < 0 || row + j > ROW_SIZE-1) {
                    continue;
                }

                for (int k = -1; k <= 1; k++) {
                    if (col + k < 0 || col + k > COL_SIZE-1) {
                        continue;
                    }

                    if (reverse) {
                        int bombIndex = (row + j) * COL_SIZE + (col + k);
                        if (!bombSet.contains(bombIndex)) {
                            bombArrange[row][col]++;
                        }
                    } else {
                        bombArrange[row + j][col + k]++;
                    }
                }
            }
        }
    }

    public ArrayList<Integer> getOpenCellIndexList(int index) {
        ArrayList<Integer> cellList = new ArrayList<>();

        int row = index / COL_SIZE;
        int col = index % COL_SIZE;


        if (bombArrange[row][col] > 0) {
            cellList.add(index);
            return cellList;
        }

        setOpenCellIndex(row, col, cellList);

        return cellList;
    }


    private void setOpenCellIndex(int row, int col, ArrayList<Integer> cellList) {
        int index = row*COL_SIZE+col;

        if (cellList.contains(index) || flagList.contains(index)) {
            return;
        }

        if (isBomb(row, col))
            return;

        cellList.add(index);

        if (bombArrange[row][col] > 0) {
            return;
        }

        for (int j = -1; j <= 1; j++) {
            if (row + j < 0 || row + j > ROW_SIZE-1) {
                continue;
            }

            for (int k = -1; k <= 1; k++) {
                if (col + k < 0 || col + k > COL_SIZE-1) {
                    continue;
                }

                setOpenCellIndex(row+j, col+k, cellList);
            }
        }
    }

    public boolean isBomb(int index) {
        boolean isBomb = true;
        if (bombMap.get(index) == null) {
            isBomb =  false;
        }

        if (reverse) {
            return !isBomb;
        }

        return isBomb;
    }

    public boolean isBomb(int row, int col) {
        return isBomb(row * COL_SIZE + col);
    }

    public int getBombCount(int index) {
        int row = index / COL_SIZE;
        int col = index % COL_SIZE;

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
