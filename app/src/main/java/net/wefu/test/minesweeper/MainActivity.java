package net.wefu.test.minesweeper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private GridView mGridView;
    private BombAdapter mAdapter;
    private Minesweeper mMinesweeper;
    private ArrayList<Cell> cells;

    private ImageButton mBtnFlag;
    private ImageButton mBtnStart;
    private TextView mTvFlagCount;

    private boolean stateFlag = false;
    private boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }

        mGridView = (GridView) findViewById(R.id.grid);
        mGridView.setNumColumns(Minesweeper.ARRAY_SIZE);

        mTvFlagCount = (TextView) findViewById(R.id.tv_flag_count);
        mBtnStart = (ImageButton) findViewById(R.id.btn_start);
        mBtnStart.setOnClickListener(this);
        mBtnFlag = (ImageButton) findViewById(R.id.btn_flag);
        mBtnFlag.setOnClickListener(this);
        findViewById(R.id.btn_result).setOnClickListener(this);

        cells = new ArrayList<>();

        mAdapter = new BombAdapter(this, R.layout.adapter_cell, cells);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new ItemClickListener());

        mMinesweeper = new Minesweeper();
        refreshMinesweeper();

    }

    private void refreshMinesweeper() {
        gameOver = false;
        cells.clear();
        mBtnStart.setImageResource(R.drawable.ic_smail);
        mMinesweeper.bombArrange();

        mTvFlagCount.setText(String.valueOf(Minesweeper.ARRAY_SIZE - mMinesweeper.getFlagCount()));

        for (int i = 0; i < Minesweeper.ARRAY_SIZE * Minesweeper.ARRAY_SIZE; i++) {
            boolean isBomb = mMinesweeper.isBomb(i);
            int bombCount = 0;
            if (!isBomb) {
                bombCount = mMinesweeper.getBombCount(i);
            }

            cells.add(new Cell(false, isBomb, bombCount));
        }

        mAdapter.notifyDataSetChanged();
    }

    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(message);
        builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                refreshMinesweeper();
                break;
            case R.id.btn_result:
                for (int i = 0; i < mAdapter.getCount(); i++) {
                    Cell item = mAdapter.getItem(i);
                    item.setSelected(true);
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_flag:
                stateFlag = !stateFlag;
                mBtnFlag.setImageResource(stateFlag ? R.drawable.ic_flag : R.drawable.ic_bomb);
                break;
        }
    }

    class ItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cell item = mAdapter.getItem(position);

            if (stateFlag) {
                if (item.isSelected() || gameOver) {
                    return;
                }

                if (item.isFlag()) {
                    mMinesweeper.removeFlagIndex(position);
                    mMinesweeper.setSelectedCount(mMinesweeper.getSelectedCount()-1);
                    item.setFlag(false);
                } else {
                    mMinesweeper.addFlagIndex(position);
                    mMinesweeper.setSelectedCount(mMinesweeper.getSelectedCount()+1);
                    item.setFlag(true);
                }

                mAdapter.notifyDataSetChanged();
                mTvFlagCount.setText(String.valueOf(Minesweeper.BOMB_SIZE - mMinesweeper.getFlagCount()));
                checkSuccess();
            } else {
                if (item.isFlag() || item.isSelected() || gameOver) {
                    return;
                }

                if (item.isBomb()) {
                    gameOver = true;
                    mBtnStart.setImageResource(R.drawable.ic_bad);
                    item.setSelected(true);
                    mAdapter.notifyDataSetChanged();
                    showAlertDialog("fail");
                    return;
                }

                ArrayList<Integer> openCellList = mMinesweeper.getOpenCellIndexList(position);
                for (Integer index : openCellList) {
                    Cell openItem = mAdapter.getItem(index);
                    if (!openItem.isSelected()) {
                        mMinesweeper.setSelectedCount(mMinesweeper.getSelectedCount()+1);
                    }
                    openItem.setSelected(true);
                }
                mAdapter.notifyDataSetChanged();
                checkSuccess();
            }
        }
    }

    private void checkSuccess() {
        if (mMinesweeper.getSelectedCount() >= mAdapter.getCount()) {
            int successCount = 0;
            for (int i = 0; i < mAdapter.getCount(); i++) {
                Cell item = mAdapter.getItem(i);
                if (item.isBomb()) {
                    if (item.isFlag()) {
                        successCount++;
                    }
                }
            }

            if (successCount == Minesweeper.BOMB_SIZE && mMinesweeper.getFlagCount() == Minesweeper.BOMB_SIZE) {
                gameOver = true;
                showAlertDialog("success");
            }
        }
    }
}
