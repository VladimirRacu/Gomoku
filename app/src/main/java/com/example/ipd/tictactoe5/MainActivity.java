package com.example.ipd.tictactoe5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

enum CellState {Empty, X, O}

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_SETTINGS = 1;

    static int rows = 10, cols = 10;
    Button[][] buttonBoard = new Button[rows][cols];
    CellState[][] board = new CellState[10][10];
    ArrayOfStates[][] arrayOfStates = new ArrayOfStates[10][10];
    Random random = new Random();
    boolean firstPlayerMove;                            // true - means first player move, false - it's time of second player
    boolean gameEnd = false;
    int winner = 0;
    boolean oPCplayer = false, xPCplayer = false;
    int[][] array = new int[][]{
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
    int[] rangeOfWeights = new int[]{1, 2, 3, 4, 5};
    EditText etFirstGamerName;
    EditText etSecondGamerName;
    EditText lbl_left_player;
    EditText lbl_right_player;
    Button leftPCplayer;
    Button rightPCplayer;

    public void leftPCplayerFunction() {
        if (!xPCplayer) {
            if (oPCplayer) {
                etFirstGamerName.setText("");
                leftPCplayer.setBackgroundColor(Color.LTGRAY);
                //leftPCplayer.setTextColor(Color.BLACK);
                oPCplayer = false;
            } else {
                etFirstGamerName.setText("O - PC player");
                leftPCplayer.setBackgroundColor(Color.YELLOW);
                //leftPCplayer.setTextColor(Color.GREEN);
                oPCplayer = true;
            }
        }
    }

    public void rightPCplayerFunction() {
        if (!oPCplayer) {
            if (xPCplayer) {
                etSecondGamerName.setText("");
                rightPCplayer.setBackgroundColor(Color.LTGRAY);
                //rightPCplayer.setTextColor(Color.BLACK);
                xPCplayer = false;
            } else {
                etSecondGamerName.setText("X - PC player");
                rightPCplayer.setBackgroundColor(Color.YELLOW);
                //rightPCplayer.setTextColor(Color.BLUE);
                xPCplayer = true;
            }
        }
    }

    int turnCount;

    public void resetGame() {
        turnCount = 0;
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                board[row][col] = CellState.Empty;
                arrayOfStates[row][col] = new ArrayOfStates();
                buttonBoard[row][col].setText("");//findViewById (R.id.leftPCplayer);
                String buttonID = "Bt" + row + col;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                Button buttonNumber = (Button) this.findViewById(resID);
                buttonNumber.setEnabled(true);
            }
        }
    }

    public void clicked(int row, int col) {
        if (firstPlayerMove) {
            array[row][col] = 1;    // firstPlayerMove ==> 1 as first Player
            board[row][col] = CellState.O;
            addWeightsByPosition(row, col, 1);
            addWeightsByStrategy();
            //testShowFullPotentialWeight();
            //atent
            buttonBoard[row][col].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            buttonBoard[row][col].setText("O");
            arrayOfStates[row][col].setStateOfCell(1);
            buttonBoard[row][col].setTextColor(Color.RED);
            leftPCplayer.setBackgroundColor(Color.LTGRAY);
            rightPCplayer.setBackgroundColor(Color.YELLOW);
            //System.Reflection.AssemblyKeyNameAttribute buttonNumber = this.GetType().GetField("variable" + row.ToString() + col.ToString());
            //ProgressBar buttonNumber = (ProgressBar)this.Controls["Bt" + row.ToString() + col.ToString()];
            String buttonID = "Bt" + row + col;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            Button buttonNumber = (Button) this.findViewById(resID);
            buttonNumber.setEnabled(false); //arrayButtons[row,col].disabled=true;
            firstPlayerMove = false;
            checkWin();
            if (xPCplayer && !gameEnd) {
                if (!firstPlayerMove) {
                    checkState(10);
                }
            }
        } else {
            array[row][col] = 10;    // !firstPlayerMove ==> 10 as second Player
            board[row][col] = CellState.X;
            addWeightsByPosition(row, col, 2);
            addWeightsByStrategy();
            //testShowFullPotentialWeight();
            //atent
            buttonBoard[row][col].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            buttonBoard[row][col].setText("X");
            arrayOfStates[row][col].setStateOfCell(2);
            buttonBoard[row][col].setTextColor(Color.GREEN);
            leftPCplayer.setBackgroundColor(Color.YELLOW);
            rightPCplayer.setBackgroundColor(Color.LTGRAY);
            //ProgressBar bar = (ProgressBar)this.Controls["Bt" + row.ToString() + col.ToString()];
            String buttonID = "Bt" + row + col;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            Button buttonNumber = (Button) this.findViewById(resID);
            buttonNumber.setEnabled(false); //arrayButtons[row,col].disabled=true;
            firstPlayerMove = true;
            checkWin();
            if (oPCplayer && !gameEnd) {
                if (firstPlayerMove) {
                    checkState(1);
                }
            }
        }
    }

    public void checkWin() {
        int check = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (array[i][j] == 0) {
                    check++;
                }
            }
        }
        ////////////////////////////////////////////////////////////////

        // check if winner exists and background images for winning line are changed accordingly
        if (check != 0) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if(array[i][j] == 1) {
                        if (checkWinByPosition(i, j, 0)) {
                            winner = 1;
                            gameEnd = true;
                        }
                    }
                    if((!gameEnd)&&(array[i][j] == 10)) {
                        if (checkWinByPosition(i, j, 1)) {
                            winner = 2;
                            gameEnd = true;
                        }
                    }
                    if(gameEnd) {break;}
                    }
                }
            ////////////////////////////////////////////////////////////

            // if game ends:
            if (gameEnd) {
                // disable all not pushed cells
                finish();
                if (winner == 1) {
                    dialogBox("Player " + etFirstGamerName.getText() + " won! Do you wont to repeat?");
                    //MessageBox.Show("Player " + firstGamerName.Text + " won!");
                } else {
                    dialogBox("Player " + etSecondGamerName.getText() + " won! Do you wont to repeat?");
                    //MessageBox.Show("Player " + secondGamerName.Text + " won!");
                }
            }///////////////////////////////////////////////////////////
        } else {
            //dialogBox("Game over with draw! Do you wont to repeat?");
            ExitRerunNewGameDialogFragment("Game over with draw!");
            // display message if is draw result
            //MessageBox.Show("Game over with draw!");
        }///////////////////////////////////////////////////////////////
    }

    protected boolean checkWinByPosition(int i, int j, int player){
        if(j + 4 < rows){
            if(checkWinRightHorizontally(i, j, player)) return true;
            if(i + 4 < cols){
                if(checkWinRightDownDiagonally(i, j, player)) return true;
            }
        }
        if(i + 4 < cols){
            if(checkWinDownVertically(i, j, player)) return true;
            if(j - 4 > 0 ){
                if(checkWinLeftDownDiagonally(i, j, player)) return true;
            }
        }
        return false;
    }

    protected boolean checkWinRightHorizontally(int i, int j, int player){
        int value = (int) Math.round(Math.pow(10,player)); // for winners = 0 the value = 1, for winners = 1 the value = 10
        if(array[i][j] + array[i][j+1] + array[i][j+2] + array[i][j+3] + array[i][j+4] == value * 5) {
            return true;
        }
        return false;
    }

    protected boolean checkWinRightDownDiagonally(int i, int j, int player){
        int value = (int) Math.round(Math.pow(10,player)); // for winners = 0 the value = 1, for winners = 1 the value = 10
        if(array[i][j] + array[i+1][j+1] + array[i+2][j+2] + array[i+3][j+3] + array[i+4][j+4] == value * 5) {
            return true;
        }
        return false;
    }

    protected boolean checkWinDownVertically(int i, int j, int player){
        int value = (int) Math.round(Math.pow(10,player)); // for winners = 0 the value = 1, for winners = 1 the value = 10
        if(array[i][j] + array[i+1][j] + array[i+2][j] + array[i+3][j] + array[i+4][j] == value * 5) {
            return true;
        }
        return false;
    }

    protected boolean checkWinLeftDownDiagonally(int i, int j, int player){
        int value = (int) Math.round(Math.pow(10,player)); // for winners = 0 the value = 1, for winners = 1 the value = 10
        if(array[i][j] + array[i+1][j-1] + array[i+2][j-2] + array[i+3][j-3] + array[i+4][j-4] == value * 5) {
            return true;
        }
        return false;
    }

    protected boolean addWeightsByPosition(int i, int j, int player){
        if(player == 1){
            int temp_value = arrayOfStates[i][j].getLeftPlayerWeight();
            arrayOfStates[i][j].setLeftPlayerWeight(temp_value + rangeOfWeights[4]);
            //  direction Up
            int step = 1;
            while((i - step >= 0) && (step < 5)){
                if(arrayOfStates[i - step][j].getStateOfCell() == 0) {
                    temp_value = arrayOfStates[i - step][j].getLeftPlayerWeight();
                    arrayOfStates[i - step][j].setLeftPlayerWeight(temp_value + rangeOfWeights[4 - step]);
                    //atent
//                    buttonBoard[i - step][j].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
//                    buttonBoard[i - step][j].setText(String.valueOf(arrayOfStates[i - step][j].getFullPotential()));
                }
                step++;
            }
            //  direction Up and Right
            step = 1;
            while((i - step >= 0) && (j + step < cols) && (step < 5)){
                if(arrayOfStates[i - step][j + step].getStateOfCell() == 0) {
                    temp_value = arrayOfStates[i - step][j + step].getLeftPlayerWeight();
                    arrayOfStates[i - step][j + step].setLeftPlayerWeight(temp_value + rangeOfWeights[4 - step]);
                    //atent
//                    buttonBoard[i - step][j + step].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
//                    buttonBoard[i - step][j + step].setText("" + arrayOfStates[i - step][j + step].getFullPotential());
                }
                step++;
            }
            //  direction Right
            step = 1;
            while((j + step < cols) && (step < 5)){
                if(arrayOfStates[i][j + step].getStateOfCell() == 0) {
                    temp_value = arrayOfStates[i][j + step].getLeftPlayerWeight();
                    arrayOfStates[i][j + step].setLeftPlayerWeight(temp_value + rangeOfWeights[4 - step]);
                    //atent
//                    buttonBoard[i][j + step].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
//                    buttonBoard[i][j + step].setText("" + arrayOfStates[i][j + step].getFullPotential());
                }
                step++;
            }
            //  direction Down and Right
            step = 1;
            while((i + step < rows) && (j + step < cols) && (step < 5)){
                if(arrayOfStates[i + step][j + step].getStateOfCell() == 0) {
                    temp_value = arrayOfStates[i + step][j + step].getLeftPlayerWeight();
                    arrayOfStates[i + step][j + step].setLeftPlayerWeight(temp_value + rangeOfWeights[4 - step]);
                    //atent
//                    buttonBoard[i + step][j + step].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
//                    buttonBoard[i + step][j + step].setText("" + arrayOfStates[i + step][j + step].getFullPotential());
                }
                step++;
            }
            //  direction Down
            step = 1;
            while((i + step < rows) && (step < 5)){
                if(arrayOfStates[i + step][j].getStateOfCell() == 0) {
                    temp_value = arrayOfStates[i + step][j].getLeftPlayerWeight();
                    arrayOfStates[i + step][j].setLeftPlayerWeight(temp_value + rangeOfWeights[4 - step]);
                    //atent
//                    buttonBoard[i + step][j].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
//                    buttonBoard[i + step][j].setText("" + arrayOfStates[i + step][j].getFullPotential());
                }
                step++;
            }
            //  direction Down and Left
            step = 1;
            while((i + step < rows) && (j - step >= 0) && (step < 5)){
                if(arrayOfStates[i + step][j - step].getStateOfCell() == 0) {
                    temp_value = arrayOfStates[i + step][j - step].getLeftPlayerWeight();
                    arrayOfStates[i + step][j - step].setLeftPlayerWeight(temp_value + rangeOfWeights[4 - step]);
                    //atent
//                    buttonBoard[i + step][j - step].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
//                    buttonBoard[i + step][j - step].setText("" + arrayOfStates[i + step][j - step].getFullPotential());
                }
                step++;
            }
            //  direction Left
            step = 1;
            while((j - step >= 0) && (step < 5)){
                if(arrayOfStates[i][j - step].getStateOfCell() == 0) {
                    temp_value = arrayOfStates[i][j - step].getLeftPlayerWeight();
                    arrayOfStates[i][j - step].setLeftPlayerWeight(temp_value + rangeOfWeights[4 - step]);
                    //atent
//                    buttonBoard[i][j - step].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
//                    buttonBoard[i][j - step].setText("" + arrayOfStates[i][j - step].getFullPotential());
                }
                step++;
            }
            //  direction Up and Left
            step = 1;
            while((i - step >= 0) && (j - step >= 0) && (step < 5)){
                if(arrayOfStates[i - step][j - step].getStateOfCell() == 0) {
                    temp_value = arrayOfStates[i - step][j - step].getLeftPlayerWeight();
                    arrayOfStates[i - step][j - step].setLeftPlayerWeight(temp_value + rangeOfWeights[4 - step]);
                    //atent
//                    buttonBoard[i - step][j - step].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
//                    buttonBoard[i - step][j - step].setText("" + arrayOfStates[i - step][j - step].getFullPotential());
                }
                step++;
            }
        }
        if(player == 2){
            int temp_value = arrayOfStates[i][j].getRightPlayerWeight();
            arrayOfStates[i][j].setRightPlayerWeight(temp_value + rangeOfWeights[4]);
            //  direction Up
            int step = 1;
            while((i - step >= 0) && (step < 5)){
                if(arrayOfStates[i - step][j].getStateOfCell() == 0) {
                    temp_value = arrayOfStates[i - step][j].getRightPlayerWeight();
                    arrayOfStates[i - step][j].setRightPlayerWeight(temp_value + rangeOfWeights[4 - step]);
                    //atent
//                    buttonBoard[i - step][j].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
//                    buttonBoard[i - step][j].setText("" + arrayOfStates[i - step][j].getFullPotential());
                }
                step++;
            }
            //  direction Up and Right
            step = 1;
            while((i - step >= 0) && (j + step < cols) && (step < 5)){
                if(arrayOfStates[i - step][j + step].getStateOfCell() == 0) {
                    temp_value = arrayOfStates[i - step][j + step].getRightPlayerWeight();
                    arrayOfStates[i - step][j + step].setRightPlayerWeight(temp_value + rangeOfWeights[4 - step]);
                    //atent
//                    buttonBoard[i - step][j + step].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
//                    buttonBoard[i - step][j + step].setText("" + arrayOfStates[i - step][j + step].getFullPotential());
                }
                step++;
            }
            //  direction Right
            step = 1;
            while((j + step < cols) && (step < 5)){
                if(arrayOfStates[i][j + step].getStateOfCell() == 0) {
                    temp_value = arrayOfStates[i][j + step].getRightPlayerWeight();
                    arrayOfStates[i][j + step].setRightPlayerWeight(temp_value + rangeOfWeights[4 - step]);
                    //atent
//                    buttonBoard[i][j + step].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
//                    buttonBoard[i][j + step].setText("" + arrayOfStates[i][j + step].getFullPotential());
                }
                step++;
            }
            //  direction Down and Right
            step = 1;
            while((i + step < rows) && (j + step < cols) && (step < 5)){
                if(arrayOfStates[i + step][j + step].getStateOfCell() == 0) {
                    temp_value = arrayOfStates[i + step][j + step].getRightPlayerWeight();
                    arrayOfStates[i + step][j + step].setRightPlayerWeight(temp_value + rangeOfWeights[4 - step]);
                    //atent
//                    buttonBoard[i + step][j + step].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
//                    buttonBoard[i + step][j + step].setText("" + arrayOfStates[i + step][j + step].getFullPotential());
                }
                step++;
            }
            //  direction Down
            step = 1;
            while((i + step < rows) && (step < 5)){
                if(arrayOfStates[i + step][j].getStateOfCell() == 0) {
                    temp_value = arrayOfStates[i + step][j].getRightPlayerWeight();
                    arrayOfStates[i + step][j].setRightPlayerWeight(temp_value + rangeOfWeights[4 - step]);
                    //atent
//                    buttonBoard[i + step][j].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
//                    buttonBoard[i + step][j].setText("" + arrayOfStates[i + step][j].getFullPotential());
                }
                step++;
            }
            //  direction Down and Left
            step = 1;
            while((i + step < rows) && (j - step >= 0) && (step < 5)){
                if(arrayOfStates[i + step][j - step].getStateOfCell() == 0) {
                    temp_value = arrayOfStates[i + step][j - step].getRightPlayerWeight();
                    arrayOfStates[i + step][j - step].setRightPlayerWeight(temp_value + rangeOfWeights[4 - step]);
                    //atent
//                    buttonBoard[i + step][j - step].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
//                    buttonBoard[i + step][j - step].setText("" + arrayOfStates[i + step][j - step].getFullPotential());
                }
                step++;
            }
            //  direction Left
            step = 1;
            while((j - step >= 0) && (step < 5)){
                if(arrayOfStates[i][j - step].getStateOfCell() == 0) {
                    temp_value = arrayOfStates[i][j - step].getRightPlayerWeight();
                    arrayOfStates[i][j - step].setRightPlayerWeight(temp_value + rangeOfWeights[4 - step]);
                    //atent
//                    buttonBoard[i][j - step].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
//                    buttonBoard[i][j - step].setText("" + arrayOfStates[i][j - step].getFullPotential());
                }
                step++;
            }
            //  direction Up and Left
            step = 1;
            while((i - step >= 0) && (j - step >= 0) && (step < 5)){
                if(arrayOfStates[i - step][j - step].getStateOfCell() == 0) {
                    temp_value = arrayOfStates[i - step][j - step].getRightPlayerWeight();
                    arrayOfStates[i - step][j - step].setRightPlayerWeight(temp_value + rangeOfWeights[4 - step]);
                    //atent
//                    buttonBoard[i - step][j - step].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
//                    buttonBoard[i - step][j - step].setText("" + arrayOfStates[i - step][j - step].getFullPotential());
                }
                step++;
            }
        }
        return false;
    }

    protected void clearAllWeightsForStrategy() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                arrayOfStates[i][j].setStrategyLeftPotential(0);
                arrayOfStates[i][j].setStrategyRightPotential(0);
            }
        }
    }

    protected void addWeightsByStrategy() {
        clearAllWeightsForStrategy();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // for player == 1

                if (array[i][j] == 1) {
                    int step = 1;
                    int weight = 3;
                    int multiplier = 3;
                    //  check right horizontally
                    if (j + 1 < cols) {
                        if (array[i][j + step] == 1) {  //  weight increase for stack with at least two elements
                            if (j + 4 < cols){          //  the check if the stack could be long enough for win
                                weight = 10;            //  (it is better to check also the states == 0 for all free places, but maybe later)
                            }
                            step = 2;
                            while ((step < 5) && (j + step < cols)) {
                                if (array[i][j + step] == 1) {  //  weight is multiplied again for stack with more than three elements
                                    multiplier *= multiplier;
                                }
                                if (array[i][j + step] != 10) {
                                    int temp_value = arrayOfStates[i][j + step].getStrategyLeftPotential();
                                    if(j + step + 1 < cols){
                                        //  this check should to put the different weight for extremities of stack.
                                        //  The goal is tested situation like _OOO__. The weight at right edge should be greater.
                                        if ((array[i][j + step + 1] == 0) && (multiplier > 3)) {
                                            temp_value += 2 * weight;
                                        }
                                    }
                                    arrayOfStates[i][j + step].setStrategyLeftPotential(temp_value + multiplier * weight + 2 * (5 - step));
                                } else {
                                    break;
                                }
                                step++;
                            }
                        }
                        else {
                            if (j + 2 < cols) {
                                if ((array[i][j + 1] == 0) && (array[i][j + 2] == 1)) {
                                    weight = 10;
                                    int temp_value = arrayOfStates[i][j + 1].getStrategyLeftPotential();
                                    arrayOfStates[i][j + 1].setStrategyLeftPotential(temp_value + 4 * weight);
                                }
                            }
                        }
                    }
                    //  check right down diagonally
                    if ((i + 1 < rows) && (j + 1 < cols)) {
                        if (array[i + 1][j + 1] == 1) {
                            weight = 3;
                            multiplier = 3;
                            if ((i + 4 < rows) && (j + 4 < cols)) {
                                weight = 10;
                            }
                            step = 2;
                            while ((step < 5) && (i + step < rows) && (j + step < cols)) {
                                if (array[i + step][j + step] == 1) {
                                    multiplier *= multiplier;
                                }
                                if (array[i + step][j + step] != 10) {
                                    int temp_value = arrayOfStates[i + step][j + step].getStrategyLeftPotential();
                                    if((i + step + 1 < rows) && (j + step + 1 < cols)){
                                        //  this check should to put the different weight for extremities of stack.
                                        //  The goal is tested situation like _OOO__. The weight at right edge should be greater.
                                        if ((array[i + step + 1][j + step + 1] == 0) && (multiplier > 3)) {
                                            temp_value += 2 * weight;
                                        }
                                    }
                                    arrayOfStates[i + step][j + step].setStrategyLeftPotential(temp_value + multiplier * weight + 2 * (5 - step));
                                } else {
                                    break;
                                }
                                step++;
                            }
                        }
                        else {
                            if ((i + 2 < rows) && (j + 2 < cols)) {
                                if ((array[i + 1][j + 1] == 0) && (array[i + 2][j + 2] == 1)) {
                                    weight = 10;
                                    int temp_value = arrayOfStates[i + 1][j + 1].getStrategyLeftPotential();
                                    arrayOfStates[i + 1][j + 1].setStrategyLeftPotential(temp_value + 4 * weight);
                                }
                            }
                        }
                    }
                    //  check down vertically
                    if (i + 1 < rows) {
                        if (array[i + 1][j] == 1) {
                            multiplier = 3;
                            weight = 3;
                            if (i + 4 < rows) {
                                weight = 10;
                            }
                            step = 2;
                            while ((step < 5) && (i + step < rows)) {
                                if (array[i + step][j] == 1) {
                                    multiplier *= multiplier;
                                }
                                if (array[i + step][j] != 10) {
                                    int temp_value = arrayOfStates[i + step][j].getStrategyLeftPotential();
                                    if(i + step + 1 < rows){
                                        //  this check should to put the different weight for extremities of stack.
                                        //  The goal is tested situation like _OOO__. The weight at right edge should be greater.
                                        if ((array[i + step + 1][j] == 0) && (multiplier > 3)) {
                                            temp_value += 2 * weight;
                                        }
                                    }
                                    arrayOfStates[i + step][j].setStrategyLeftPotential(temp_value + multiplier * weight + 2 * (5 - step));
                                } else {
                                    break;
                                }
                                step++;
                            }
                        }
                        else {
                            if (i + 2 < rows) {
                                if ((array[i + 1][j] == 0) && (array[i + 2][j] == 1)) {
                                    weight = 10;
                                    int temp_value = arrayOfStates[i + 1][j].getStrategyLeftPotential();
                                    arrayOfStates[i + 1][j].setStrategyLeftPotential(temp_value + 4 * weight);
                                }
                            }
                        }
                    }
                    //  check left down diagonally
                    if ((i + 1 < rows) && (j - 1 >= 0)) {
                        if (array[i + 1][j - 1] == 1) {
                            weight = 3;
                            multiplier = 3;
                            if ((i + 4 < rows) && (j - 4 >= 0)) {
                                weight = 10;
                            }
                            step = 2;
                            while ((step < 5) && (i + step < rows) && (j - step >= 0)) {
                                if (array[i + step][j - step] == 1) {
                                    multiplier *= multiplier;
                                }
                                if (array[i + step][j - step] != 10) {
                                    int temp_value = arrayOfStates[i + step][j - step].getStrategyLeftPotential();
                                    if((i + step + 1 < rows) && (j - step - 1 >= 0)){
                                        //  this check should to put the different weight for extremities of stack.
                                        //  The goal is tested situation like _OOO__. The weight at right edge should be greater.
                                        if ((array[i + step + 1][j - step - 1] == 0) && (multiplier > 3)) {
                                            temp_value += 2 * weight;
                                        }
                                    }
                                    arrayOfStates[i + step][j - step].setStrategyLeftPotential(temp_value + multiplier * weight + 2 * (5 - step));
                                } else {
                                    break;
                                }
                                step++;
                            }
                        }
                        else {
                            if ((i + 2 < rows) && (j - 2 >= 0)) {
                                if ((array[i + 1][j - 1] == 0) && (array[i + 2][j - 2] == 1)) {
                                    weight = 10;
                                    int temp_value = arrayOfStates[i + 1][j - 1].getStrategyLeftPotential();
                                    arrayOfStates[i + 1][j - 1].setStrategyLeftPotential(temp_value + 4 * weight);
                                }
                            }
                        }
                    }
                    //  check left horizontally
                    if (j - 1 >= 0) {
                        if (array[i][j - 1] == 1) {
                            weight = 3;
                            multiplier = 3;
                            if (j - 4 >= 0) {
                                weight = 10;
                            }
                            step = 2;
                            while ((step < 5) && (j - step >= 0)) {
                                if (array[i][j - step] == 1) {
                                    multiplier *= multiplier;
                                }
                                if (array[i][j - step] != 10) {
                                    int temp_value = arrayOfStates[i][j - step].getStrategyLeftPotential();
                                    if (j - step - 1 >= 0){
                                        //  this check should to put the different weight for extremities of stack.
                                        //  The goal is tested situation like _OOO__. The weight at right edge should be greater.
                                        if ((array[i][j - step - 1] == 0) && (multiplier > 3)) {
                                            temp_value += 2 * weight;
                                        }
                                    }
                                    arrayOfStates[i][j - step].setStrategyLeftPotential(temp_value + multiplier * weight + 2 * (5 - step));
                                } else {
                                    break;
                                }
                                step++;
                            }
                        }
                        else {
                            if (j - 2 >= 0) {
                                if ((array[i][j - 1] == 0) && (array[i][j - 2] == 1)) {
                                    weight = 10;
                                    int temp_value = arrayOfStates[i][j - 1].getStrategyLeftPotential();
                                    arrayOfStates[i][j - 1].setStrategyLeftPotential(temp_value + 4 * weight);
                                }
                            }
                        }
                    }
                    //  check left up diagonally
                    if ((i - 1 >= 0) && (j - 1 >= 0)) {
                        if (array[i - 1][j - 1] == 1) {
                            weight = 3;
                            multiplier = 3;
                            if ((i - 4 >= 0) && (j - 4 >= 0)) {
                                weight = 10;
                            }
                            step = 2;
                            while ((step < 5) && (i - step >= 0) && (j - step >= 0)) {
                                if (array[i - step][j - step] == 1) {
                                    multiplier *= multiplier;
                                }
                                if (array[i - step][j - step] != 10) {
                                    int temp_value = arrayOfStates[i - step][j - step].getStrategyLeftPotential();
                                    if((i - step - 1 >= 0) && (j - step - 1 >= 0)){
                                        //  this check should to put the different weight for extremities of stack.
                                        //  The goal is tested situation like _OOO__. The weight at right edge should be greater.
                                        if ((array[i - step - 1][j - step - 1] == 0) && (multiplier > 3)) {
                                            temp_value += 2 * weight;
                                        }
                                    }
                                    arrayOfStates[i - step][j - step].setStrategyLeftPotential(temp_value + multiplier * weight + 2 * (5 - step));
                                } else {
                                    break;
                                }
                                step++;
                            }
                        }
                        else {
                            if ((i - 2 >= 0) && (j - 2 >= 0)) {
                                if ((array[i - 1][j - 1] == 0) && (array[i - 2][j - 2] == 1)) {
                                    weight = 10;
                                    int temp_value = arrayOfStates[i - 1][j - 1].getStrategyLeftPotential();
                                    arrayOfStates[i - 1][j - 1].setStrategyLeftPotential(temp_value + 4 * weight);
                                }
                            }
                        }
                    }
                    //  check up vertically
                    if (i - 1 >= 0) {
                        if (array[i - 1][j] == 1) {
                            weight = 3;
                            multiplier = 3;
                            if (i - 4 >= 0) {
                                weight = 10;
                            }
                            step = 2;
                            while ((step < 5) && (i - step >= 0)) {
                                if (array[i - step][j] == 1) {
                                    multiplier *= multiplier;
                                }
                                if (array[i - step][j] != 10) {
                                    int temp_value = arrayOfStates[i - step][j].getStrategyLeftPotential();
                                    if(i - step - 1 >= 0){
                                        //  this check should to put the different weight for extremities of stack.
                                        //  The goal is tested situation like _OOO__. The weight at right edge should be greater.
                                        if ((array[i - step - 1][j] == 0) && (multiplier > 3)) {
                                            temp_value += 2 * weight;
                                        }
                                    }
                                    arrayOfStates[i - step][j].setStrategyLeftPotential(temp_value + multiplier * weight + 2 * (5 - step));
                                } else {
                                    break;
                                }
                                step++;
                            }
                        }
                        else {
                            if (i - 2 >= 0) {
                                if ((array[i - 1][j] == 0) && (array[i - 2][j] == 1)) {
                                    weight = 10;
                                    int temp_value = arrayOfStates[i - 1][j].getStrategyLeftPotential();
                                    arrayOfStates[i - 1][j].setStrategyLeftPotential(temp_value + 4 * weight);
                                }
                            }
                        }
                    }
                    //  check right up diagonally
                    if ((i - 1 >= 0) && (j + 1 < cols)) {
                        if (array[i - 1][j + 1] == 1) {
                            weight = 3;
                            multiplier = 3;
                            if ((i - 4 >= 0) && (j + 4 < cols)) {
                                weight = 10;
                            }
                            step = 2;
                            while ((step < 5) && (i - step >= 0) && (j + step < cols)) {
                                if (array[i - step][j + step] == 1) {
                                    multiplier *= multiplier;
                                }
                                if (array[i - step][j + step] != 10) {
                                    int temp_value = arrayOfStates[i - step][j + step].getStrategyLeftPotential();
                                    if((i - step - 1 >= 0) && (j + step + 1 < cols)){
                                        //  this check should to put the different weight for extremities of stack.
                                        //  The goal is tested situation like _OOO__. The weight at right edge should be greater.
                                        if ((array[i - step - 1][j + step + 1] == 0) && (multiplier > 3)) {
                                            temp_value += 2 * weight;
                                        }
                                    }
                                    arrayOfStates[i - step][j + step].setStrategyLeftPotential(temp_value + multiplier * weight + 2 * (5 - step));
                                } else {
                                    break;
                                }
                                step++;
                            }
                        }
                        else {
                            if ((i - 2 >= 0) && (j + 2 < cols)) {
                                if ((array[i - 1][j + 1] == 0) && (array[i - 2][j + 2] == 1)) {
                                    weight = 10;
                                    int temp_value = arrayOfStates[i - 1][j + 1].getStrategyLeftPotential();
                                    arrayOfStates[i - 1][j + 1].setStrategyLeftPotential(temp_value + 4 * weight);
                                }
                            }
                        }
                    }
                }
                // for player == 2
                //  check right horizontally
                if (array[i][j] == 10) {
                    int step = 1;
                    int weight = 3;
                    int multiplier = 3;
                    //  check right horizontally
                    if (j + 1 < cols) {
                        if (array[i][j + step] == 10) {
                            if (j + 4 < cols){
                                weight = 10;
                            }
                            step = 2;
                            while ((step < 5) && (j + step < cols)) {
                                if (array[i][j + step] == 10) {
                                    multiplier *= multiplier;
                                }
                                if (array[i][j + step] != 1) {
                                    int temp_value = arrayOfStates[i][j + step].getStrategyRightPotential();
                                    if(j + step + 1 < cols){
                                        //  this check should to put the different weight for extremities of stack.
                                        //  The goal is tested situation like _XXX__. The weight at right edge should be greater.
                                        if ((array[i][j + step + 1] == 0) && (multiplier > 3)) {
                                            temp_value += 2 * weight;
                                        }
                                    }
                                    arrayOfStates[i][j + step].setStrategyRightPotential(temp_value + multiplier * weight + 2 * (5 - step));
                                } else {
                                    break;
                                }
                                step++;
                            }
                        }
                        else {
                            if (j + 2 < cols) {
                                if ((array[i][j + 1] == 0) && (array[i][j + 2] == 10)) {
                                    weight = 10;
                                    int temp_value = arrayOfStates[i][j + 1].getStrategyRightPotential();
                                    arrayOfStates[i][j + 1].setStrategyRightPotential(temp_value + 4 * weight);
                                }
                            }
                        }
                    }
                    //  check right down diagonally
                    if ((i + 1 < rows) && (j + 1 < cols)) {
                        if (array[i + 1][j + 1] == 10) {
                            weight = 3;
                            multiplier = 3;
                            if ((i + 4 < rows) && (j + 4 < cols)) {
                                weight = 10;
                            }
                            step = 2;
                            while ((step < 5) && (i + step < rows) && (j + step < cols)) {
                                if (array[i + step][j + step] == 10) {
                                    multiplier *= multiplier;
                                }
                                if (array[i + step][j + step] != 1) {
                                    int temp_value = arrayOfStates[i + step][j + step].getStrategyRightPotential();
                                    if((i + step + 1 < rows) && (j + step + 1 < cols)){
                                        //  this check should to put the different weight for extremities of stack.
                                        //  The goal is tested situation like _XXX__. The weight at right edge should be greater.
                                        if ((array[i + step + 1][j + step + 1] == 0) && (multiplier > 3)) {
                                            temp_value += 2 * weight;
                                        }
                                    }
                                    arrayOfStates[i + step][j + step].setStrategyRightPotential(temp_value + multiplier * weight + 2 * (5 - step));
                                } else {
                                    break;
                                }
                                step++;
                            }
                        }
                        else {
                            if ((i + 2 < rows) && (j + 2 < cols)) {
                                if ((array[i + 1][j + 1] == 0) && (array[i + 2][j + 2] == 10)) {
                                    weight = 10;
                                    int temp_value = arrayOfStates[i + 1][j + 1].getStrategyRightPotential();
                                    arrayOfStates[i + 1][j + 1].setStrategyRightPotential(temp_value + 4 * weight);
                                }
                            }
                        }
                    }
                    //  check down vertically
                    if (i + 1 < rows) {
                        if (array[i + 1][j] == 10) {
                            weight = 3;
                            multiplier = 3;
                            if (i + 4 < rows) {
                                weight = 10;
                            }
                            step = 2;
                            while ((step < 5) && (i + step < rows)) {
                                if (array[i + step][j] == 10) {
                                    multiplier *= multiplier;
                                }
                                if (array[i + step][j] != 1) {
                                    int temp_value = arrayOfStates[i + step][j].getStrategyRightPotential();
                                    if(i + step + 1 < rows){
                                        //  this check should to put the different weight for extremities of stack.
                                        //  The goal is tested situation like _XXX__. The weight at right edge should be greater.
                                        if ((array[i + step + 1][j] == 0) && (multiplier > 3)) {
                                            temp_value += 2 * weight;
                                        }
                                    }
                                    arrayOfStates[i + step][j].setStrategyRightPotential(temp_value + multiplier * weight + 2 * (5 - step));
                                } else {
                                    break;
                                }
                                step++;
                            }
                        }
                        else {
                            if (i + 2 < rows) {
                                if ((array[i + 1][j] == 0) && (array[i + 2][j] == 10)) {
                                    weight = 10;
                                    int temp_value = arrayOfStates[i + 1][j].getStrategyRightPotential();
                                    arrayOfStates[i + 1][j].setStrategyRightPotential(temp_value + 4 * weight);
                                }
                            }
                        }
                    }
                    //  check left down diagonally
                    if ((i + 1 < rows) && (j - 1 >= 0)) {
                        if (array[i + 1][j - 1] == 10) {
                            weight = 3;
                            multiplier = 3;
                            if ((i + 4 < rows) && (j - 4 >= 0)) {
                                weight = 10;
                            }
                            step = 2;
                            while ((step < 5) && (i + step < rows) && (j - step >= 0)) {
                                if (array[i + step][j - step] == 10) {
                                    multiplier *= multiplier;
                                }
                                if (array[i + step][j - step] != 1) {
                                    int temp_value = arrayOfStates[i + step][j - step].getStrategyRightPotential();
                                    if((i + step + 1 < rows) && (j - step - 1 >= 0)){
                                        //  this check should to put the different weight for extremities of stack.
                                        //  The goal is tested situation like _XXX__. The weight at right edge should be greater.
                                        if ((array[i + step + 1][j - step - 1] == 0) && (multiplier > 3)) {
                                            temp_value += 2 * weight;
                                        }
                                    }
                                    arrayOfStates[i + step][j - step].setStrategyRightPotential(temp_value + multiplier * weight + 2 * (5 - step));
                                } else {
                                    break;
                                }
                                step++;
                            }
                        }
                        else {
                            if ((i + 2 < rows) && (j - 2 >= 0)) {
                                if ((array[i + 1][j - 1] == 0) && (array[i + 2][j - 2] == 10)) {
                                    weight = 10;
                                    int temp_value = arrayOfStates[i + 1][j - 1].getStrategyRightPotential();
                                    arrayOfStates[i + 1][j - 1].setStrategyRightPotential(temp_value + 4 * weight);
                                }
                            }
                        }
                    }
                    //  check left horizontally
                    if (j - 1 >= 0) {
                        if (array[i][j - 1] == 10) {
                            weight = 3;
                            multiplier = 3;
                            if (j - 4 >= 0) {
                                weight = 10;
                            }
                            step = 2;
                            while ((step < 5) && (j - step >= 0)) {
                                if (array[i][j - step] == 10) {
                                    multiplier *= multiplier;
                                }
                                if (array[i][j - step] != 1) {
                                    int temp_value = arrayOfStates[i][j - step].getStrategyRightPotential();
                                    arrayOfStates[i][j - step].setStrategyRightPotential(temp_value + multiplier * weight + 2 * (5 - step));
                                } else {
                                    break;
                                }
                                step++;
                            }
                        }
                        else {
                            if (j - 2 >= 0) {
                                if ((array[i][j - 1] == 0) && (array[i][j - 2] == 10)) {
                                    weight = 10;
                                    int temp_value = arrayOfStates[i][j - 1].getStrategyRightPotential();
                                    arrayOfStates[i][j - 1].setStrategyRightPotential(temp_value + 4 * weight);
                                }
                            }
                        }
                    }
                    //  check left up diagonally
                    if ((i - 1 >= 0) && (j - 1 >= 0)) {
                        if (array[i - 1][j - 1] == 10) {
                            weight = 3;
                            multiplier = 3;
                            if ((i - 4 >= 0) && (j - 4 >= 0)) {
                                weight = 10;
                            }
                            step = 2;
                            while ((step < 5) && (i - step >= 0) && (j - step >= 0)) {
                                if (array[i - step][j - step] == 10) {
                                    multiplier *= multiplier;
                                }
                                if (array[i - step][j - step] != 1) {
                                    int temp_value = arrayOfStates[i - step][j - step].getStrategyRightPotential();
                                    if((i - step - 1 >= 0) && (j - step - 1 >= 0)){
                                        //  this check should to put the different weight for extremities of stack.
                                        //  The goal is tested situation like _XXX__. The weight at right edge should be greater.
                                        if ((array[i - step - 1][j - step - 1] == 0) && (multiplier > 3)) {
                                            temp_value += 2 * weight;
                                        }
                                    }
                                    arrayOfStates[i - step][j - step].setStrategyRightPotential(temp_value + multiplier * weight + 2 * (5 - step));
                                } else {
                                    break;
                                }
                                step++;
                            }
                        }
                        else {
                            if ((i - 2 >= 0) && (j - 2 >= 0)) {
                                if ((array[i - 1][j - 1] == 0) && (array[i - 2][j - 2] == 10)) {
                                    weight = 10;
                                    int temp_value = arrayOfStates[i - 1][j - 1].getStrategyRightPotential();
                                    arrayOfStates[i - 1][j - 1].setStrategyRightPotential(temp_value + 4 * weight);
                                }
                            }
                        }
                    }
                    //  check up vertically
                    if (i - 1 >= 0) {
                        if (array[i - 1][j] == 10) {
                            weight = 3;
                            multiplier = 3;
                            if (i - 4 >= 0) {
                                weight = 10;
                            }
                            step = 2;
                            while ((step < 5) && (i - step >= 0)) {
                                if (array[i - step][j] == 10) {
                                    multiplier *= multiplier;
                                }
                                if (array[i - step][j] != 1) {
                                    int temp_value = arrayOfStates[i - step][j].getStrategyRightPotential();
                                    if(i - step - 1 >= 0){
                                        //  this check should to put the different weight for extremities of stack.
                                        //  The goal is tested situation like _XXX__. The weight at right edge should be greater.
                                        if ((array[i - step - 1][j] == 0) && (multiplier > 3)) {
                                            temp_value += 2 * weight;
                                        }
                                    }
                                    arrayOfStates[i - step][j].setStrategyRightPotential(temp_value + multiplier * weight + 2 * (5 - step));
                                } else {
                                    break;
                                }
                                step++;
                            }
                        }
                        else {
                            if (i - 2 >= 0) {
                                if ((array[i - 1][j] == 0) && (array[i - 2][j] == 10)) {
                                    weight = 10;
                                    int temp_value = arrayOfStates[i - 1][j].getStrategyRightPotential();
                                    arrayOfStates[i - 1][j].setStrategyRightPotential(temp_value + 4 * weight);
                                }
                            }
                        }
                    }
                    //  check right up diagonally
                    if ((i - 1 >= 0) && (j + 1 < cols)) {
                        if (array[i - 1][j + 1] == 10) {
                            weight = 3;
                            multiplier = 3;
                            if ((i - 4 >= 0) && (j + 4 < cols)) {
                                weight = 10;
                            }
                            step = 2;
                            while ((step < 5) && (i - step >= 0) && (j + step < cols)) {
                                if (array[i - step][j + step] == 10) {
                                    multiplier *= multiplier;
                                }
                                if (array[i - step][j + step] != 1) {
                                    int temp_value = arrayOfStates[i - step][j + step].getStrategyRightPotential();
                                    if((i - step - 1 >= 0) && (j + step + 1 < cols)){
                                        //  this check should to put the different weight for extremities of stack.
                                        //  The goal is tested situation like _XXX__. The weight at right edge should be greater.
                                        if ((array[i - step - 1][j + step + 1] == 0) && (multiplier > 3)) {
                                            temp_value += 2 * weight;
                                        }
                                    }
                                    arrayOfStates[i - step][j + step].setStrategyRightPotential(temp_value + multiplier * weight + 2 * (5 - step));
                                } else {
                                    break;
                                }
                                step++;
                            }
                        }
                        else {
                            if ((i - 2 >= 0) && (j + 2 < cols)) {
                                if ((array[i - 1][j + 1] == 0) && (array[i - 2][j + 2] == 10)) {
                                    weight = 10;
                                    int temp_value = arrayOfStates[i - 1][j + 1].getStrategyRightPotential();
                                    arrayOfStates[i - 1][j + 1].setStrategyRightPotential(temp_value + 4 * weight);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected void testShowPotentialWeight(){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (array[i][j] == 0) {
                    buttonBoard[i][j].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                    buttonBoard[i][j].setText(String.valueOf(arrayOfStates[i][j].getFullPotential()));
                }
            }
        }
    }
    
    protected void testShowFullPotentialWeight(){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (array[i][j] == 0) {
                    buttonBoard[i][j].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                    buttonBoard[i][j].setText(String.valueOf(arrayOfStates[i][j].getFullPotential()));
                }
            }
        }
    }

    public void ExitRerunNewGameDialogFragment(String textMessage) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Game over!!!");

        // Setting Dialog Message
        alertDialog.setMessage(textMessage);

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.game_over);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
                System.exit(0);
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("Restart game", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // User pressed No button. Write Logic Here
                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
            }
        });
        // Setting Neutral "Cancel" Button
        alertDialog.setNeutralButton("New game", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                prepareBoardToStart();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void checkStateOld(int num) {
        // if game just began PC push central button = [1,1]
        if (array[1][1] == 0) {
            clicked(1, 1);
        } else {
            // check if two opposite sides only were pushed by another player and select another one side
            // part I (horizontally)
            if ((((array[1][0] + array[1][2]) == 2)
                    || ((array[1][0] + array[1][2]) == 20))
                    && ((array[0][0] + array[0][1] + array[0][2] + array[2][0] + array[2][1] + array[2][2]) == 0)) {
                int side = 1 + random.nextInt(1);
                switch (side) {
                    case 1:
                        if (array[0][1] == 0) {
                            clicked(0, 1);
                        }
                        ;
                        break;
                    case 2:
                        if (array[2][1] == 0) {
                            clicked(2, 1);
                        }
                        ;
                        break;
                }

            } else {
                // check if two opposite sides only were pushed by another player and select another one side
                // part II (vertically)
                if ((((array[0][1] + array[2][1]) == 2)
                        || ((array[0][1] + array[2][1]) == 20))
                        && ((array[0][0] + array[0][2] + array[1][0] + array[1][2] + array[2][0] + array[2][2]) == 0)) {
                    int side = 1 + random.nextInt(1);
                    switch (side) {
                        case 1:
                            if (array[1][0] == 0) {
                                clicked(1, 0);
                            }
                            ;
                            break;
                        case 2:
                            if (array[1][2] == 0) {
                                clicked(1, 2);
                            }
                            ;
                            break;
                    }
                } else {
                    // check if three sides only were pushed by another player and select a corner near fourth side (selected by us)
                    // part I (horizontally top)
                    if (((((array[1][0] + array[1][2] + array[2][1]) == 3) && ( array[0][1] == 10))
                            || (((array[1][0] + array[1][2] + array[2][1]) == 30) && ( array[0][1] == 1)))
                            && ((array[0][0] + array[0][2] + array[2][0] + array[2][2]) == 0)) {
                        int side = 1 + random.nextInt(1);
                        switch (side) {
                            case 1:
                                if (array[0][0] == 0) {
                                    clicked(0, 0);
                                }
                                ;
                                break;
                            case 2:
                                if (array[0][2] == 0) {
                                    clicked(0, 2);
                                }
                                ;
                                break;
                        }
                    } else {
                        // check if three sides only were pushed by another player and select a corner near fourth side (selected by us)
                        // part II (horizontally bottom)
                        if (((((array[1][0] + array[1][2] + array[0][1]) == 3) && ( array[2][1] == 10))
                                || (((array[1][0] + array[1][2] + array[0][1]) == 30) && ( array[2][1] == 1)))
                                && ((array[0][0] + array[0][2] + array[2][0] + array[2][2]) == 0)) {
                            int side = 1 + random.nextInt(1);
                            switch (side) {
                                case 1:
                                    if (array[2][0] == 0) {
                                        clicked(2, 0);
                                    }
                                    ;
                                    break;
                                case 2:
                                    if (array[2][2] == 0) {
                                        clicked(2, 2);
                                    }
                                    ;
                                    break;
                            }
                        } else {
                            // check if three sides only were pushed by another player and select a corner near fourth side (selected by us)
                            // part III (vertically left)
                            if (((((array[0][1] + array[2][1] + array[1][2]) == 3) && (array[1][0] == 10))
                                    || (((array[0][1] + array[2][1] + array[1][2]) == 30) && (array[1][0] == 1)))
                                    && ((array[0][0] + array[0][2] + array[2][0] + array[2][2]) == 0)) {
                                int side = 1 + random.nextInt(1);
                                switch (side) {
                                    case 1:
                                        if (array[0][0] == 0) {
                                            clicked(0, 0);
                                        }
                                        ;
                                        break;
                                    case 2:
                                        if (array[2][0] == 0) {
                                            clicked(2, 0);
                                        }
                                        ;
                                        break;
                                }
                            } else {
                                // check if three sides only were pushed by another player and select a corner near fourth side (selected by us)
                                // part IV (vertically right)
                                if (((((array[0][1] + array[2][1] + array[1][0]) == 3) && (array[1][2] == 10))
                                        || (((array[0][1] + array[2][1] + array[1][0]) == 30) && (array[1][2] == 1)))
                                        && ((array[0][0] + array[0][2] + array[2][0] + array[2][2]) == 0)) {
                                    int side = 1 + random.nextInt(1);
                                    switch (side) {
                                        case 1:
                                            if (array[0][2] == 0) {
                                                clicked(0, 2);
                                            }
                                            ;
                                            break;
                                        case 2:
                                            if (array[2][2] == 0) {
                                                clicked(2, 2);
                                            }
                                            ;
                                            break;
                                    }
                                } else {
                                    // if just central button was pushed, PC select one from all free corners
                                    if (((array[0][0] + array[0][2] + array[2][0] + array[2][2]) == 0)
                                            &&
                                            (
                                                    ((array[0][1] + array[1][0] + array[1][2] + array[2][1]) == 0)
                                                            ||
                                                            (// if central button was pushed by PC and another player selected two opposite sides, PC select one from all free corners
                                                                    (((array[0][1] + array[2][1] == 2) || (array[1][0] + array[1][2] == 2))
                                                                            && (array[1][1] == 10)
                                                                            && ((array[0][1] + array[1][2] + array[2][1] + array[1][0]) == 2))
                                                                            ||
                                                                            (((array[0][1] + array[2][1] == 20) || (array[1][0] + array[1][2] == 20))
                                                                                    && (array[1][1] == 1)
                                                                                    && ((array[0][1] + array[1][2] + array[2][1] + array[1][0]) == 20))
                                                            )
                                            )
                                            ) {
                                        int corner = 1 + random.nextInt(3);
                                        switch (corner) {
                                            case 1:
                                                if (array[0][0] == 0) {
                                                    clicked(0, 0);
                                                }
                                                ;
                                                break;
                                            case 2:
                                                if (array[0][2] == 0) {
                                                    clicked(0, 2);
                                                }
                                                ;
                                                break;
                                            case 3:
                                                if (array[2][2] == 0) {
                                                    clicked(2, 2);
                                                }
                                                ;
                                                break;
                                            case 4:
                                                if (array[2][0] == 0) {
                                                    clicked(2, 0);
                                                }
                                                ;
                                                break;
                                        }
                                    } else {
                                        if (// check if one side was pushed by another player and select one corner
                                                (((array[0][1] + array[1][2] + array[2][1] + array[1][0]) == 1)
                                                        || ((array[0][1] + array[1][2] + array[2][1] + array[1][0]) == 10))
                                                        && ((array[0][0] + array[0][2] + array[2][0] + array[2][2]) == 0)
                                                ) {
                                            if ((array[1][0] + array[0][1] != 0) && (array[2][2] == 0)) {
                                                clicked(2, 2);
                                            } else if ((array[0][1] + array[1][2] != 0) && (array[2][0] == 0)) {
                                                clicked(2, 0);
                                            } else if ((array[2][1] + array[1][2] != 0) && (array[0][0] == 0)) {
                                                clicked(0, 0);
                                            } else if ((array[2][1] + array[1][0] != 0) && (array[0][2] == 0)) {
                                                clicked(0, 2);
                                            }
                                        } else {
                                            if (
                                                    (// check if only one corner was pushed and select another one
                                                            (((array[0][0] + array[0][2] + array[2][0] + array[2][2]) == 1)
                                                                    || ((array[0][0] + array[0][2] + array[2][0] + array[2][2]) == 10))
                                                                    && ((array[0][1] + array[1][2] + array[2][1] + array[1][0]) == 0)
                                                    )
                                                            ||
                                                            (// check if one side and one opposite corner were pushed by another player and select another one
                                                                    (
                                                                            (((array[0][0] == 1) && (array[1][2] + array[2][1] == 1)) ||
                                                                                    ((array[0][2] == 1) && (array[1][0] + array[2][1] == 1)) ||
                                                                                    ((array[2][2] == 1) && (array[1][0] + array[0][1] == 1)) ||
                                                                                    ((array[2][0] == 1) && (array[0][1] + array[1][2] == 1)))
                                                                                    && (array[1][1] == 10)
                                                                                    && ((array[0][0] + array[0][2] + array[2][0] + array[2][2]) == 1)
                                                                    )
                                                                            ||
                                                                            (
                                                                                    (((array[0][0] == 10) && (array[1][2] + array[2][1] == 10)) ||
                                                                                            ((array[0][2] == 10) && (array[1][0] + array[2][1] == 10)) ||
                                                                                            ((array[2][2] == 10) && (array[1][0] + array[0][1] == 10)) ||
                                                                                            ((array[2][0] == 10) && (array[0][1] + array[1][2] == 10)))
                                                                                            && (array[1][1] == 1)
                                                                                            && ((array[0][0] + array[0][2] + array[2][0] + array[2][2]) == 10)
                                                                            )
                                                            )
                                                    ) {
                                                if ((array[0][0] != 0) && (array[2][2] == 0)) {
                                                    clicked(2, 2);
                                                } else if ((array[0][2] != 0) && (array[2][0] == 0)) {
                                                    clicked(2, 0);
                                                } else if ((array[2][0] != 0) && (array[0][2] == 0)) {
                                                    clicked(0, 2);
                                                } else if ((array[2][2] != 0) && (array[0][0] == 0)) {
                                                    clicked(0, 0);
                                                }
                                            } else {
                                                // check if two nearest sides were pushed by another player and select corner between them
                                                if ((((array[0][1] + array[1][2] + array[2][1] + array[1][0]) == 2)
                                                        || ((array[0][1] + array[1][2] + array[2][1] + array[1][0]) == 20))
                                                        && ((array[0][0] + array[0][2] + array[2][0] + array[2][2]) == 0)) {
                                                    if ((array[1][0] != 0) && (array[0][1] != 0) && (array[0][0] == 0)) {
                                                        clicked(0, 0);
                                                    } else if ((array[0][1] != 0) && (array[1][2] != 0) && (array[0][2] == 0)) {
                                                        clicked(0, 2);
                                                    } else if ((array[2][1] != 0) && (array[1][2] != 0) && (array[2][2] == 0)) {
                                                        clicked(2, 2);
                                                    } else if ((array[2][1] != 0) && (array[1][0] != 0) && (array[2][0] == 0)) {
                                                        clicked(2, 0);
                                                    }
                                                } else {
                                                    // check if two nearest corners and one opposite side were pushed by another player and select side near one of selected corners
                                                    // part I (horizontally)
                                                    if (((((array[0][0] + array[0][2]) == 2) || ((array[0][0] + array[0][2]) == 20))
                                                            && ((array[0][1] + array[2][1]) == 11)
                                                            && ((array[1][0] + array[1][2] + array[2][0] + array[2][2]) == 0))
                                                            || ((((array[2][0] + array[2][2]) == 2) || ((array[2][0] + array[2][2]) == 20))
                                                            && ((array[0][1] + array[2][1]) == 11)
                                                            && ((array[0][0] + array[0][2] + array[1][0] + array[1][2]) == 0))) {
                                                        int side = 1 + random.nextInt(1);
                                                        switch (side) {
                                                            case 1:
                                                                if (array[1][0] == 0) {
                                                                    clicked(1, 0);
                                                                }
                                                                ;
                                                                break;
                                                            case 2:
                                                                if (array[1][2] == 0) {
                                                                    clicked(1, 2);
                                                                }
                                                                ;
                                                                break;
                                                        }
                                                    } else {
                                                        // check if two nearest corners and one opposite side were pushed by another player and select side near one of selected corners
                                                        // part II (vertically)
                                                        if (((((array[0][0] + array[2][0]) == 2) || ((array[0][0] + array[2][0]) == 20))
                                                                && ((array[1][0] + array[1][2]) == 11)
                                                                && ((array[0][1] + array[2][1] + array[0][2] + array[2][2]) == 0))
                                                                || ((((array[0][2] + array[2][2]) == 2) || ((array[0][2] + array[2][2]) == 20))
                                                                && ((array[1][0] + array[1][2]) == 11)
                                                                && ((array[0][0] + array[2][0] + array[0][1] + array[2][1]) == 0))) {
                                                            int side = 1 + random.nextInt(1);
                                                            switch (side) {
                                                                case 1:
                                                                    if (array[0][1] == 0) {
                                                                        clicked(0, 1);
                                                                    }
                                                                    ;
                                                                    break;
                                                                case 2:
                                                                    if (array[2][1] == 0) {
                                                                        clicked(2, 1);
                                                                    }
                                                                    ;
                                                                    break;
                                                            }
                                                        } else {
                                                            if (// check if two opposite corner were pushed by another player and choose one cell on side part near any selected corner
                                                                // note that selection of new corner lead to loose
                                                                    (
                                                                            (((array[0][0] == 1) && (array[2][2] == 1)) || ((array[2][0] == 1) && (array[0][2] == 1)))
                                                                                    && (array[1][1] == 10)
                                                                                    && ((array[0][1] + array[1][2] + array[2][1] + array[1][0]) == 0)
                                                                    )
                                                                            ||
                                                                            (
                                                                                    (((array[0][0] == 10) && (array[2][2] == 10)) || ((array[2][0] == 10) && (array[0][2] == 10)))
                                                                                            && (array[1][1] == 1)
                                                                                            && ((array[0][1] + array[1][2] + array[2][1] + array[1][0]) == 0)
                                                                            )
                                                                    ) {
                                                                int side = 1 + random.nextInt(3);
                                                                switch (side) {
                                                                    case 1:
                                                                        if (array[0][1] == 0) {
                                                                            clicked(0, 1);
                                                                        }
                                                                        ;
                                                                        break;
                                                                    case 2:
                                                                        if (array[1][0] == 0) {
                                                                            clicked(1, 0);
                                                                        }
                                                                        ;
                                                                        break;
                                                                    case 3:
                                                                        if (array[1][2] == 0) {
                                                                            clicked(1, 2);
                                                                        }
                                                                        ;
                                                                        break;
                                                                    case 4:
                                                                        if (array[2][1] == 0) {
                                                                            clicked(2, 1);
                                                                        }
                                                                        ;
                                                                        break;
                                                                }
                                                            } else {
                                                                // check all states when 2 cells pushed by non-PC player could lead to win and block them
                                                                if (checkPCTwoCells(num)) {
                                                                    return;
                                                                } else {
                                                                    // check all states when 2 cells pushed by PC could lead to win
                                                                    if (checkNonPCTwoCells(num)) {
                                                                        return;
                                                                    } else {
                                                                        // check if 2 (O + O) or (X + X) or (O + X) corners were pushed and select another one
                                                                        if (((array[0][0] + array[0][2] + array[2][0] + array[2][2]) == 2)
                                                                                || ((array[0][0] + array[0][2] + array[2][0] + array[2][2]) == 20)
                                                                                || ((array[0][0] + array[0][2] + array[2][0] + array[2][2]) == 11)) {
                                                                            int goal = random.nextInt(1);
                                                                            if (goal == 1) {
                                                                                if (array[0][0] == 0) {
                                                                                    clicked(0, 0);
                                                                                } else if (array[0][2] == 0) {
                                                                                    clicked(0, 2);
                                                                                } else if (array[2][0] == 0) {
                                                                                    clicked(2, 0);
                                                                                } else if (array[2][2] == 0) {
                                                                                    clicked(2, 2);
                                                                                }
                                                                            } else {// available variants: could be free only ( 0x0 and 2x2 ) or ( 0x2 and 2x0 )
                                                                                if (array[0][0] == 0) {
                                                                                    if (array[2][2] == 0) {
                                                                                        clicked(2, 2);
                                                                                    }
                                                                                } else if (array[0][2] == 0) {
                                                                                    if (array[2][0] == 0) {
                                                                                        clicked(2, 0);
                                                                                    }
                                                                                }
                                                                            }
                                                                        } else {
                                                                            // check if 3 (O + O + X)or(O + X + X) corners were pushed and select another one
                                                                            if (((array[0][0] + array[0][2] + array[2][0] + array[2][2]) == 12)
                                                                                    || ((array[0][0] + array[0][2] + array[2][0] + array[2][2]) == 21)) {
                                                                                if (array[0][0] == 0) {
                                                                                    clicked(0, 0);
                                                                                } else if (array[0][2] == 0) {
                                                                                    clicked(0, 2);
                                                                                } else if (array[2][0] == 0) {
                                                                                    clicked(2, 0);
                                                                                } else if (array[2][2] == 0) {
                                                                                    clicked(2, 2);
                                                                                }
                                                                            } else {
                                                                                int check = 0, row_temp = 0, col_temp = 0;
                                                                                for (int i = 0; i < rows; i++) {
                                                                                    for (int j = 0; j < cols; j++) {
                                                                                        if (array[i][j] == 0) {
                                                                                            check++;
                                                                                            row_temp = i;
                                                                                            col_temp = j;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if (check == 2) // if two cells are free and no one strategy were applied, any free cell is selected by PC
                                                                                {
                                                                                    if (array[row_temp][col_temp] == 0) {
                                                                                        clicked(row_temp, col_temp);
                                                                                    }
                                                                                }
                                                                                if (check == 1) // if only one cells is not pushed, it is selected by PC
                                                                                {
                                                                                    if (array[row_temp][col_temp] == 0) {
                                                                                        clicked(row_temp, col_temp);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public  int selectPlayerWeight(int player, int row, int col){
        if (player == 1 ) {
            return arrayOfStates[row][col].getLeftPlayerWeight();    //  player == 1 is mean left player
        }
        else {
            return arrayOfStates[row][col].getRightPlayerWeight();    //  player == 10 is mean right player
        }
    }

    public void checkState(int num) {
        TreeMap<Integer, Integer> availableMoves = new TreeMap<Integer, Integer>();
        int maxElem = arrayOfStates[0][0].getFullPotential();
        int maxElemForPlayersWeight = selectPlayerWeight(num, 0, 0);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(arrayOfStates[i][j].getStateOfCell() == 0) {
                    int tempElem = arrayOfStates[i][j].getFullPotential();
                    int tempElem_2 = selectPlayerWeight(num, i, j);
                    if (tempElem > maxElem) {
                        maxElem = tempElem;
                        maxElemForPlayersWeight = tempElem_2;
                    }
                }
            }
        }
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if ((arrayOfStates[i][j].getStateOfCell() == 0) && (arrayOfStates[i][j].getFullPotential() == maxElem)){
                    int tempElem = selectPlayerWeight(num, i, j);
                    if(tempElem >= maxElemForPlayersWeight ) {
                        maxElemForPlayersWeight = tempElem;
                        availableMoves.put(count, i * rows + j);
                        count++;
                    }
                }
            }
        }
        int temp_count = count;
        for (int i = 0; i < count; i++) {
            int row = (int)Math.floor(availableMoves.ceilingEntry(i).getValue()/10);
            int col = (int)availableMoves.ceilingEntry(i).getValue() - row * 10;
            int tempElem = selectPlayerWeight(num, row, col);
            if(tempElem < maxElemForPlayersWeight ) {
                availableMoves.remove(i);
                temp_count--;
            }
        }
        int goal = random.nextInt(temp_count);
        int row = (int)Math.floor(availableMoves.ceilingEntry(goal).getValue()/10);
        int col = (int)availableMoves.ceilingEntry(goal).getValue() - row * 10;
        if (arrayOfStates[row][col].getStateOfCell() == 0) {
            clicked(row, col);
        }
    }

    // check all states when 2 cells pushed by PC could lead to win
    public boolean checkPCTwoCells(int num) {
        if ((array[0][0] == array[0][1]) && (array[0][1] == num) && (array[0][2] == 0)) {
            clicked(0, 2);
            return true;
        }
        if ((array[0][0] == array[0][2]) && (array[0][2] == num) && (array[0][1] == 0)) {
            clicked(0, 1);
            return true;
        }
        if ((array[0][0] == array[1][0]) && (array[1][0] == num) && (array[2][0] == 0)) {
            clicked(2, 0);
            return true;
        }
        if ((array[0][0] == array[1][1]) && (array[1][1] == num) && (array[2][2] == 0)) {
            clicked(2, 2);
            return true;
        }
        if ((array[0][0] == array[2][0]) && (array[2][0] == num) && (array[1][0] == 0)) {
            clicked(1, 0);
            return true;
        }
        if ((array[0][0] == array[2][2]) && (array[2][2] == num) && (array[1][1] == 0)) {
            clicked(1, 1);
            return true;
        }
        if ((array[0][1] == array[0][2]) && (array[0][2] == num) && (array[0][0] == 0)) {
            clicked(0, 0);
            return true;
        }
        if ((array[0][1] == array[1][1]) && (array[1][1] == num) && (array[2][1] == 0)) {
            clicked(2, 1);
            return true;
        }
        if ((array[0][1] == array[2][1]) && (array[2][1] == num) && (array[1][1] == 0)) {
            clicked(1, 1);
            return true;
        }
        if ((array[0][2] == array[1][1]) && (array[1][1] == num) && (array[2][0] == 0)) {
            clicked(2, 0);
            return true;
        }
        if ((array[0][2] == array[1][2]) && (array[1][2] == num) && (array[2][2] == 0)) {
            clicked(2, 2);
            return true;
        }
        if ((array[0][2] == array[2][0]) && (array[2][0] == num) && (array[1][1] == 0)) {
            clicked(1, 1);
            return true;
        }
        if ((array[0][2] == array[2][2]) && (array[2][2] == num) && (array[1][2] == 0)) {
            clicked(1, 2);
            return true;
        }
        if ((array[1][0] == array[1][1]) && (array[1][1] == num) && (array[1][2] == 0)) {
            clicked(1, 2);
            return true;
        }
        if ((array[1][0] == array[1][2]) && (array[1][2] == num) && (array[1][1] == 0)) {
            clicked(1, 1);
            return true;
        }
        if ((array[1][0] == array[2][0]) && (array[2][0] == num) && (array[0][0] == 0)) {
            clicked(0, 0);
            return true;
        }
        if ((array[1][1] == array[1][2]) && (array[1][2] == num) && (array[1][0] == 0)) {
            clicked(1, 0);
            return true;
        }
        if ((array[1][1] == array[2][0]) && (array[2][0] == num) && (array[0][2] == 0)) {
            clicked(0, 2);
            return true;
        }
        if ((array[1][1] == array[2][1]) && (array[2][1] == num) && (array[0][1] == 0)) {
            clicked(0, 1);
            return true;
        }
        if ((array[1][1] == array[2][2]) && (array[2][2] == num) && (array[0][0] == 0)) {
            clicked(0, 0);
            return true;
        }
        if ((array[1][2] == array[2][2]) && (array[2][2] == num) && (array[0][2] == 0)) {
            clicked(0, 2);
            return true;
        }
        if ((array[2][0] == array[2][1]) && (array[2][1] == num) && (array[2][2] == 0)) {
            clicked(2, 2);
            return true;
        }
        if ((array[2][0] == array[2][2]) && (array[2][2] == num) && (array[2][1] == 0)) {
            clicked(2, 1);
            return true;
        }
        if ((array[2][1] == array[2][2]) && (array[2][2] == num) && (array[2][0] == 0)) {
            clicked(2, 0);
            return true;
        }
        return false;
    }
    ////////////////////////////////////////////////////////////////////

    // check all states when 2 cells pushed by non-PC player could lead to win
    public boolean checkNonPCTwoCells(int num) {
        int tempNum = 10 / num;
        if ((array[0][0] == array[0][1]) && (array[0][1] == tempNum) && (array[0][2] == 0)) {
            clicked(0, 2);
            return true;
        }
        if ((array[0][0] == array[0][2]) && (array[0][2] == tempNum) && (array[0][1] == 0)) {
            clicked(0, 1);
            return true;
        }
        if ((array[0][0] == array[1][0]) && (array[1][0] == tempNum) && (array[2][0] == 0)) {
            clicked(2, 0);
            return true;
        }
        if ((array[0][0] == array[1][1]) && (array[1][1] == tempNum) && (array[2][2] == 0)) {
            clicked(2, 2);
            return true;
        }
        if ((array[0][0] == array[2][0]) && (array[2][0] == tempNum) && (array[1][0] == 0)) {
            clicked(1, 0);
            return true;
        }
        if ((array[0][0] == array[2][2]) && (array[2][2] == tempNum) && (array[1][1] == 0)) {
            clicked(1, 1);
            return true;
        }
        if ((array[0][1] == array[0][2]) && (array[0][2] == tempNum) && (array[0][0] == 0)) {
            clicked(0, 0);
            return true;
        }
        if ((array[0][1] == array[1][1]) && (array[1][1] == tempNum) && (array[2][1] == 0)) {
            clicked(2, 1);
            return true;
        }
        if ((array[0][1] == array[2][1]) && (array[2][1] == tempNum) && (array[1][1] == 0)) {
            clicked(1, 1);
            return true;
        }
        if ((array[0][2] == array[1][1]) && (array[1][1] == tempNum) && (array[2][0] == 0)) {
            clicked(2, 0);
            return true;
        }
        if ((array[0][2] == array[1][2]) && (array[1][2] == tempNum) && (array[2][2] == 0)) {
            clicked(2, 2);
            return true;
        }
        if ((array[0][2] == array[2][0]) && (array[2][0] == tempNum) && (array[1][1] == 0)) {
            clicked(1, 1);
            return true;
        }
        if ((array[0][2] == array[2][2]) && (array[2][2] == tempNum) && (array[1][2] == 0)) {
            clicked(1, 2);
            return true;
        }
        if ((array[1][0] == array[1][1]) && (array[1][1] == tempNum) && (array[1][2] == 0)) {
            clicked(1, 2);
            return true;
        }
        if ((array[1][0] == array[1][2]) && (array[1][2] == tempNum) && (array[1][1] == 0)) {
            clicked(1, 1);
            return true;
        }
        if ((array[1][0] == array[2][0]) && (array[2][0] == tempNum) && (array[0][0] == 0)) {
            clicked(0, 0);
            return true;
        }
        if ((array[1][1] == array[1][2]) && (array[1][2] == tempNum) && (array[1][0] == 0)) {
            clicked(1, 0);
            return true;
        }
        if ((array[1][1] == array[2][0]) && (array[2][0] == tempNum) && (array[0][2] == 0)) {
            clicked(0, 2);
            return true;
        }
        if ((array[1][1] == array[2][1]) && (array[2][1] == tempNum) && (array[0][1] == 0)) {
            clicked(0, 1);
            return true;
        }
        if ((array[1][1] == array[2][2]) && (array[2][2] == tempNum) && (array[0][0] == 0)) {
            clicked(0, 0);
            return true;
        }
        if ((array[1][2] == array[2][2]) && (array[2][2] == tempNum) && (array[0][2] == 0)) {
            clicked(0, 2);
            return true;
        }
        if ((array[2][0] == array[2][1]) && (array[2][1] == tempNum) && (array[2][2] == 0)) {
            clicked(2, 2);
            return true;
        }
        if ((array[2][0] == array[2][2]) && (array[2][2] == tempNum) && (array[2][1] == 0)) {
            clicked(2, 1);
            return true;
        }
        if ((array[2][1] == array[2][2]) && (array[2][2] == tempNum) && (array[2][0] == 0)) {
            clicked(2, 0);
            return true;
        }
        return false;
    }
    ////////////////////////////////////////////////////////////////////

    public void finish() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (array[i][j] == 0) {
                    //ProgressBar bar = (ProgressBar)this.Controls["Bt" + i.ToString() + j.ToString()];
                    String buttonID = "Bt" + i + j;
                    int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                    Button buttonNumber = (Button) this.findViewById(resID);
                    buttonNumber.setEnabled(false); //arrayButtons[i,j].disabled=true;
                }
            }
        }
    }

    // Dialog box
    public void dialogBox(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        prepareBoardToStart();
                    }
                });

        alertDialogBuilder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        System.exit(0);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void prepareBoardToStart() {
        firstPlayerMove = true;
        lbl_left_player.setBackgroundColor(Color.RED);
        lbl_right_player.setBackgroundColor(Color.GREEN);
        leftPCplayer.setBackgroundColor(Color.LTGRAY);
        rightPCplayer.setBackgroundColor(Color.LTGRAY);
        gameEnd = false;
        winner = 0;
        etFirstGamerName.setText("");
        etSecondGamerName.setText("");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                array[i][j] = 0;
                board[i][j] = CellState.Empty;
                arrayOfStates[i][j] = new ArrayOfStates();
                buttonBoard[i][j].setText("");
                buttonBoard[i][j].setTextColor(Color.DKGRAY);
                String buttonID = "Bt" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                Button buttonNumber = (Button) this.findViewById(resID);
                buttonNumber.setEnabled(false);
            }
        }
        for (int i = 0; i < rows/2; i++) {
            for (int j = i; j < cols - i; j++) {
                arrayOfStates[i][j].setWeightOfCell(i+1);
                arrayOfStates[j][i].setWeightOfCell(i+1);
                arrayOfStates[rows - i - 1][j].setWeightOfCell(i+1);
                arrayOfStates[j][rows - i - 1].setWeightOfCell(i+1);
            }
        }
        leftPCplayer.setEnabled(true);
        rightPCplayer.setEnabled(true);
        oPCplayer = false;
        xPCplayer = false;
    }

    public void Bt00_Click(View v) {
        clicked(0, 0);
    }

    public void Bt01_Click(View v) {
        clicked(0, 1);
    }

    public void Bt02_Click(View v) {
        clicked(0, 2);
    }

    public void Bt03_Click(View v) {
        clicked(0, 3);
    }

    public void Bt04_Click(View v) {
        clicked(0, 4);
    }

    public void Bt05_Click(View v) {
        clicked(0, 5);
    }

    public void Bt06_Click(View v) {
        clicked(0, 6);
    }

    public void Bt07_Click(View v) {
        clicked(0, 7);
    }

    public void Bt08_Click(View v) {
        clicked(0, 8);
    }

    public void Bt09_Click(View v) {
        clicked(0, 9);
    }

    public void Bt10_Click(View v) {
        clicked(1, 0);
    }

    public void Bt11_Click(View v) {
        clicked(1, 1);
    }

    public void Bt12_Click(View v) {
        clicked(1, 2);
    }

    public void Bt13_Click(View v) {
        clicked(1, 3);
    }

    public void Bt14_Click(View v) {
        clicked(1, 4);
    }

    public void Bt15_Click(View v) {
        clicked(1, 5);
    }

    public void Bt16_Click(View v) {
        clicked(1, 6);
    }

    public void Bt17_Click(View v) {
        clicked(1, 7);
    }

    public void Bt18_Click(View v) {
        clicked(1, 8);
    }

    public void Bt19_Click(View v) {
        clicked(1, 9);
    }

    public void Bt20_Click(View v) {
        clicked(2, 0);
    }

    public void Bt21_Click(View v) {
        clicked(2, 1);
    }

    public void Bt22_Click(View v) {
        clicked(2, 2);
    }

    public void Bt23_Click(View v) {
        clicked(2, 3);
    }

    public void Bt24_Click(View v) {
        clicked(2, 4);
    }

    public void Bt25_Click(View v) {
        clicked(2, 5);
    }

    public void Bt26_Click(View v) {
        clicked(2, 6);
    }

    public void Bt27_Click(View v) {
        clicked(2, 7);
    }

    public void Bt28_Click(View v) {
        clicked(2, 8);
    }

    public void Bt29_Click(View v) {
        clicked(2, 9);
    }
    
    public void Bt30_Click(View v) {
        clicked(3, 0);
    }

    public void Bt31_Click(View v) {
        clicked(3, 1);
    }

    public void Bt32_Click(View v) {
        clicked(3, 2);
    }

    public void Bt33_Click(View v) {
        clicked(3, 3);
    }

    public void Bt34_Click(View v) {
        clicked(3, 4);
    }

    public void Bt35_Click(View v) {
        clicked(3, 5);
    }

    public void Bt36_Click(View v) {
        clicked(3, 6);
    }

    public void Bt37_Click(View v) {
        clicked(3, 7);
    }

    public void Bt38_Click(View v) {
        clicked(3, 8);
    }

    public void Bt39_Click(View v) {
        clicked(3, 9);
    }

    public void Bt40_Click(View v) {
        clicked(4, 0);
    }

    public void Bt41_Click(View v) {
        clicked(4, 1);
    }

    public void Bt42_Click(View v) {
        clicked(4, 2);
    }

    public void Bt43_Click(View v) {
        clicked(4, 3);
    }

    public void Bt44_Click(View v) {
        clicked(4, 4);
    }

    public void Bt45_Click(View v) {
        clicked(4, 5);
    }

    public void Bt46_Click(View v) {
        clicked(4, 6);
    }

    public void Bt47_Click(View v) {
        clicked(4, 7);
    }

    public void Bt48_Click(View v) {
        clicked(4, 8);
    }

    public void Bt49_Click(View v) {
        clicked(4, 9);
    }

    public void Bt50_Click(View v) {
        clicked(5, 0);
    }

    public void Bt51_Click(View v) {
        clicked(5, 1);
    }

    public void Bt52_Click(View v) {
        clicked(5, 2);
    }

    public void Bt53_Click(View v) {
        clicked(5, 3);
    }

    public void Bt54_Click(View v) {
        clicked(5, 4);
    }

    public void Bt55_Click(View v) {
        clicked(5, 5);
    }

    public void Bt56_Click(View v) {
        clicked(5, 6);
    }

    public void Bt57_Click(View v) {
        clicked(5, 7);
    }

    public void Bt58_Click(View v) {
        clicked(5, 8);
    }

    public void Bt59_Click(View v) {
        clicked(5, 9);
    }

    public void Bt60_Click(View v) {
        clicked(6, 0);
    }

    public void Bt61_Click(View v) {
        clicked(6, 1);
    }

    public void Bt62_Click(View v) {
        clicked(6, 2);
    }

    public void Bt63_Click(View v) {
        clicked(6, 3);
    }

    public void Bt64_Click(View v) {
        clicked(6, 4);
    }

    public void Bt65_Click(View v) {
        clicked(6, 5);
    }

    public void Bt66_Click(View v) {
        clicked(6, 6);
    }

    public void Bt67_Click(View v) {
        clicked(6, 7);
    }

    public void Bt68_Click(View v) {
        clicked(6, 8);
    }

    public void Bt69_Click(View v) {
        clicked(6, 9);
    }

    public void Bt70_Click(View v) {
        clicked(7, 0);
    }

    public void Bt71_Click(View v) {
        clicked(7, 1);
    }

    public void Bt72_Click(View v) {
        clicked(7, 2);
    }

    public void Bt73_Click(View v) {
        clicked(7, 3);
    }

    public void Bt74_Click(View v) {
        clicked(7, 4);
    }

    public void Bt75_Click(View v) {
        clicked(7, 5);
    }

    public void Bt76_Click(View v) {
        clicked(7, 6);
    }

    public void Bt77_Click(View v) {
        clicked(7, 7);
    }

    public void Bt78_Click(View v) {
        clicked(7, 8);
    }

    public void Bt79_Click(View v) {
        clicked(7, 9);
    }

    public void Bt80_Click(View v) {
        clicked(8, 0);
    }

    public void Bt81_Click(View v) {
        clicked(8, 1);
    }

    public void Bt82_Click(View v) {
        clicked(8, 2);
    }

    public void Bt83_Click(View v) {
        clicked(8, 3);
    }

    public void Bt84_Click(View v) {
        clicked(8, 4);
    }

    public void Bt85_Click(View v) {
        clicked(8, 5);
    }

    public void Bt86_Click(View v) {
        clicked(8, 6);
    }

    public void Bt87_Click(View v) {
        clicked(8, 7);
    }

    public void Bt88_Click(View v) {
        clicked(8, 8);
    }

    public void Bt89_Click(View v) {
        clicked(8, 9);
    }

    public void Bt90_Click(View v) {
        clicked(9, 0);
    }

    public void Bt91_Click(View v) {
        clicked(9, 1);
    }

    public void Bt92_Click(View v) {
        clicked(9, 2);
    }

    public void Bt93_Click(View v) {
        clicked(9, 3);
    }

    public void Bt94_Click(View v) {
        clicked(9, 4);
    }

    public void Bt95_Click(View v) {
        clicked(9, 5);
    }

    public void Bt96_Click(View v) {
        clicked(9, 6);
    }

    public void Bt97_Click(View v) {
        clicked(9, 7);
    }

    public void Bt98_Click(View v) {
        clicked(9, 8);
    }

    public void Bt99_Click(View v) {
        clicked(9, 9);
    }

    public void btnStart_Click(View v) {
        firstPlayerMove = true;
        lbl_left_player.setBackgroundColor(Color.RED);
        lbl_right_player.setBackgroundColor(Color.GREEN);
        gameEnd = false;
        winner = 0;
        if (etFirstGamerName.getText().toString().equals("")) etFirstGamerName.setText("O player");
        //Typeface someFontNameCondensed = Typeface.createFromAsset(getAssets(), "assets/NameOfFont-condensed.ttf");
        etFirstGamerName.setTypeface(null, Typeface.BOLD);
        if (etSecondGamerName.getText().toString().equals(""))
            etSecondGamerName.setText("X player");
        etSecondGamerName.setTypeface(null, Typeface.BOLD);
        leftPCplayer.setBackgroundColor(Color.YELLOW);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                array[i][j] = 0;
                board[i][j] = CellState.Empty;
                buttonBoard[i][j].setText("");
                buttonBoard[i][j].setTextColor(Color.DKGRAY);
                String buttonID = "Bt" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                Button buttonNumber = (Button) this.findViewById(resID);
                buttonNumber.setEnabled(true);
            }
        }

        for (int i = 0; i < rows/2; i++) {
            for (int j = i; j < cols - i; j++) {
                arrayOfStates[i][j].setWeightOfCell(i+1);
                arrayOfStates[j][i].setWeightOfCell(i+1);
                arrayOfStates[rows - i - 1][j].setWeightOfCell(i+1);
                arrayOfStates[j][rows - i - 1].setWeightOfCell(i+1);
/*                buttonBoard[i][j].setText("" + arrayOfStates[i][j].getWeightOfCell());
                buttonBoard[j][i].setText("" + arrayOfStates[j][i].getWeightOfCell());
                buttonBoard[rows - i - 1][j].setText("" + arrayOfStates[rows - i - 1][j].getWeightOfCell());
                buttonBoard[j][rows - i - 1].setText("" + arrayOfStates[j][rows - i - 1].getWeightOfCell());*/
            }
        }

        if (oPCplayer && xPCplayer) {
            dialogBox("Note. Both player use the same algorithm. Do you wont to repeat?");
            //MessageBox.Show("Note. Both player use the same algorithm.");
        }
        if (oPCplayer) {
            leftPCplayer.setEnabled(false);
            if (firstPlayerMove) {
                checkState(1);
            }
        }
        if (xPCplayer) {
            leftPCplayer.setBackgroundColor(Color.LTGRAY);
            rightPCplayer.setEnabled(false);
            if (!firstPlayerMove) {
                checkState(10);
            }
        }

    }

    public void leftPCplayer_Click(View v) {
        leftPCplayerFunction();
    }

    public void rightPCplayer_Click(View v) {
        rightPCplayerFunction();
    }

    public void btnExit_Click(View v) {
        System.exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showUserSettings();

/*        buttonBoard[0][0] = (Button) findViewById(R.id.Bt00);
        buttonBoard[0][1] = (Button) findViewById(R.id.Bt01);
        buttonBoard[0][2] = (Button) findViewById(R.id.Bt02);
        buttonBoard[1][0] = (Button) findViewById(R.id.Bt10);
        buttonBoard[1][1] = (Button) findViewById(R.id.Bt11);
        buttonBoard[1][2] = (Button) findViewById(R.id.Bt12);
        buttonBoard[2][0] = (Button) findViewById(R.id.Bt20);
        buttonBoard[2][1] = (Button) findViewById(R.id.Bt21);
        buttonBoard[2][2] = (Button) findViewById(R.id.Bt22);*/

        for(int i = 0; i < cols; i++) {
            for(int j = 0; j < rows; j++) {
                String buttonID = "Bt" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttonBoard[i][j] = ((Button) findViewById(resID));
//                buttons[i][j].setOnClickListener(this);
            }
        }
        for(int i = 0; i < cols; i++) {
            for(int j = 0; j < rows; j++) {
                arrayOfStates[i][j] = new ArrayOfStates();
            }
        }
        etFirstGamerName = (EditText) findViewById(R.id.etFirstGamerName);
        etSecondGamerName = (EditText) findViewById(R.id.etSecondGamerName);
        lbl_left_player = (EditText) findViewById(R.id.lbl_left_player);
        lbl_right_player = (EditText) findViewById(R.id.lbl_right_player);
        leftPCplayer = (Button) findViewById(R.id.leftPCplayer);
        rightPCplayer = (Button) findViewById(R.id.rightPCplayer);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = CellState.Empty;
                buttonBoard[i][j].setText("");
                buttonBoard[i][j].setTextColor(Color.DKGRAY);
                String buttonID = "Bt" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                Button buttonNumber = (Button) this.findViewById(resID);
                buttonNumber.setEnabled(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent i = new Intent(this, UserSettingActivity.class);
                startActivityForResult(i, RESULT_SETTINGS);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
                showUserSettings();
                break;
        }
    }

    private void showUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        StringBuilder builder = new StringBuilder();
        builder.append("\n Username: " + sharedPrefs.getString("prefUsername", "NULL"));
        builder.append("\n Sync Frequency: " + sharedPrefs.getString("prefSyncFrequency", "NULL"));
        String varName = sharedPrefs.getString("prefUsername", "NULL");
        if (varName != "NULL") {
            etFirstGamerName.setText(varName);
        }
    }
}
