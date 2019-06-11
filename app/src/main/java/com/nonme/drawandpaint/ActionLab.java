package com.nonme.drawandpaint;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.nonme.actions.Action;

public class ActionLab {
    private ArrayList<List<Action> > mActions;
    private ArrayList<Integer> mCurrentAction;
    private int mCurrentLayer;
    private static ActionLab sActionLab;
    public static ActionLab get() {
        if(sActionLab == null)
            sActionLab = new ActionLab();
        return sActionLab;
    }
    private ActionLab() {
        mCurrentAction = new ArrayList<>();
        for(int i = 0; i < 3; ++i)
            mCurrentAction.add(-1);
        mCurrentLayer = 0;
        mActions = new ArrayList<>();
        for(int i = 0; i < 3; ++i)
            mActions.add(new LinkedList<>());
    }
    public void addAction(Action action) {
        for(int i = mCurrentAction.get(mCurrentLayer)+1; i < mActions.get(mCurrentLayer).size(); ) {
            mActions.get(mCurrentLayer).remove(i);
        }
        mActions.get(mCurrentLayer).add(action);
        mCurrentAction.set(mCurrentLayer, mCurrentAction.get(mCurrentLayer)+1);
    }
    public void redoAction() {
        if(mCurrentAction.get(mCurrentLayer) == mActions.get(mCurrentLayer).size()-1)
            return;
        mCurrentAction.set(mCurrentLayer, mCurrentAction.get(mCurrentLayer)+1);
        mActions.get(mCurrentLayer).get(mCurrentAction.get(mCurrentLayer)).redo();
    }
    public void undoAction() {
        if(mCurrentAction.get(mCurrentLayer) == -1)
            return;
        mActions.get(mCurrentLayer).get(mCurrentAction.get(mCurrentLayer)).undo();
        mCurrentAction.set(mCurrentLayer, mCurrentAction.get(mCurrentLayer)-1);
    }
    public List<Action> getDrawables() {
        return mActions.get(mCurrentLayer);
    }
    public Action getLastDrawable() {
        if(!mActions.get(mCurrentLayer).isEmpty())
            return mActions.get(mCurrentLayer).get(mActions.get(mCurrentLayer).size()-1);
        else return null;
    }
    public int getCurrentAction() {
        return mCurrentAction.get(mCurrentLayer);
    }
    public void setCurrentLayer(int layer) {
        mCurrentLayer = layer;
    }
    public void clear() {
        mActions.clear();
        mCurrentAction.set(mCurrentLayer, -1);
    }
}
