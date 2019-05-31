package com.nonme.drawandpaint;

import java.util.LinkedList;
import java.util.List;

public class ActionLab {
    private List<Action> mActions;
    private int mCurrentAction;
    private static ActionLab sActionLab;
    public static ActionLab get() {
        if(sActionLab == null)
            sActionLab = new ActionLab();
        return sActionLab;
    }
    private ActionLab() {
        mCurrentAction = -1;
        mActions = new LinkedList<>();
    }
    public void addAction(Action action) {
        for(int i = mCurrentAction+1; i < mActions.size(); ) {
            mActions.remove(i);
        }
        mActions.add(action);
        mCurrentAction++;
    }
    public void redoAction() {
        if(mCurrentAction == mActions.size()-1)
            return;
        mCurrentAction++;
        mActions.get(mCurrentAction).redo();
    }
    public void undoAction() {
        if(mCurrentAction == -1)
            return;
        mActions.get(mCurrentAction).undo();
        mCurrentAction--;
    }
    public List<Action> getDrawables() {
        return mActions;
    }
    public int getCurrentAction() {
        return mCurrentAction;
    }
}
