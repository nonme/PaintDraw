package com.nonme.drawandpaint;

import android.view.View;

import java.util.HashMap;

public class ToolSwitcher {
    private HashMap<Integer, Command> commandMap = new HashMap<>();
    public void register(int buttonIndex, Command command) {
        commandMap.put(buttonIndex, command);
    }
    public void execute(int buttonIndex) {
        Command command = commandMap.get(buttonIndex);
        command.execute();

    }
}
