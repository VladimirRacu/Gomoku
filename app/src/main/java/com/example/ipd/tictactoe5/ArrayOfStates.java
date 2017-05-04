package com.example.ipd.tictactoe5;

/**
 * Created by Vlad on 10-Dec-16.
 */

public class ArrayOfStates {
    protected int stateOfCell;  //0 - not selected; 1 - left player; 2 - right player
    protected int weightOfCell; // weights which depend of position on the board, e.g. the center is better to complete the 5-selected sequence
    protected int leftPlayerWeight;
    protected int rightPlayerWeight;
    protected int strategyLeftPotential;
    protected int strategyRightPotential;
    protected int fullPotential;
    protected boolean isSelected;


    public ArrayOfStates() {
        this.stateOfCell = 0;
        this.weightOfCell = 0;
        this.leftPlayerWeight = 0;
        this.rightPlayerWeight = 0;
        this.strategyLeftPotential = 0;
        this.strategyRightPotential = 0;
        this.fullPotential = 0;
        this.isSelected = false;
    }
    public int getStateOfCell() {
        return stateOfCell;
    }

    public void setStateOfCell(int _stateOfCell) {
        this.stateOfCell = _stateOfCell;
        if(_stateOfCell != 0){
            this.isSelected = true;
        }
    }

    public int getWeightOfCell() {
        return weightOfCell;
    }

    public void setWeightOfCell(int _weightOfCell) {
        this.weightOfCell = _weightOfCell;
        this.fullPotential = getFullPotential() + _weightOfCell;
    }

    public int getStrategyLeftPotential() {
        return strategyLeftPotential;
    }

    public void setStrategyLeftPotential(int _strategyLeftPotential) {
        this.strategyLeftPotential = _strategyLeftPotential;
        this.fullPotential = getWeightOfCell() + getLeftPlayerWeight() + getRightPlayerWeight() + getStrategyRightPotential() + _strategyLeftPotential;
    }

    public int getStrategyRightPotential() {
        return strategyRightPotential;
    }

    public void setStrategyRightPotential(int _strategyRightPotential) {
        this.strategyRightPotential = _strategyRightPotential;
        this.fullPotential = getWeightOfCell() + getLeftPlayerWeight() + getRightPlayerWeight() + getStrategyLeftPotential() + _strategyRightPotential;
    }

    public int getLeftPlayerWeight() {
        return leftPlayerWeight;
    }

    public void setLeftPlayerWeight(int _leftPlayerWeight) {
        this.leftPlayerWeight = _leftPlayerWeight;
        this.fullPotential = getWeightOfCell() + getStrategyLeftPotential() + getStrategyRightPotential() + getRightPlayerWeight() + _leftPlayerWeight;
    }

    public int getRightPlayerWeight() {
        return rightPlayerWeight;
    }

    public void setRightPlayerWeight(int _rightPlayerWeight) {
        this.rightPlayerWeight = _rightPlayerWeight;
        this.fullPotential = getWeightOfCell() + getStrategyLeftPotential() + getStrategyRightPotential() + getLeftPlayerWeight() + _rightPlayerWeight;
    }

    public int getFullPotential() {
        return fullPotential;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
