package basketball;

import junit.extensions.abbot.*;
import abbot.tester.*;
import java.awt.event.*;
import javax.swing.*;


private string clickType;
    public class playersPanelTest() {
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                clickType = ev.getActionCommand();                            
            }
        };

        GetPlayersPanel submit = new createSubmitButton(createSubmitButton.SUBMIT);
        GetPlayersPanel undo = new createUndoButton(createUndoButton.SUBMIT);
        GetPlayersPanel start = new createStartButton(createStartButton.START);
        GetPlayersPanel load = new createLoadButton(createLoadButton.LOAD);
        GetPlayersPanel instruction = new createInstructionButton(createInstructionButton.INSTRUCTION);
        GetPlayersPanel settings = new createSettingsButton(createSettingsButton.SETTINGS);
        GetPlayersPanel home = new createHomeButton(createHomeButton.HOME);
        GetPlayersPanel close = new createCloseButton(createCloseButton.CLOSE);

        submit.addActionListener(al);
        undo.addActionListener(al);
        start.addActionListener(al);
        load.addActionListener(al);
        instruction.addActionListener(al);
        settings.addActionListener(al);
        home.addActionListener(al);
        close.addActionListener(al);

        JPanel pane = new JPanel();
        pane.add(submit);
        pane.add(undo);
        pane.add(start);
        pane.add(load);
        pane.add(instruction);
        pane.add(settings);
        pane.add(home);
        pane.add(close);
        // ComponentTestFixture provides the frame
        showFrame(pane);
        

        clickType = null;
        tester.actionClick();        
        assertEquals("Action failed (submit)", createSubmitButton.SUBMIT, clickType);
        clickType = null;
        tester.actionClick(right);        
        assertEquals("Action failed (right)", createUndoButton.UNDO, clickType);
        clickType = null;
        tester.actionClick(up);        
        assertEquals("Action failed (up)", createStartButton.START, clickType);
        clickType = null;
        tester.actionClick(down);        
        assertEquals("Action failed (down)", createLoadButton.LOAD, clickType);
        clickType = null;
        tester.actionClick();        
        assertEquals("Action failed (left)", createInstructionButton.INSTRUCTION, clickType);
        clickType = null;
        tester.actionClick();        
        assertEquals("Action failed (left)", createSettingsButton.SETTINGS, clickType);
        clickType = null;
        tester.actionClick();        
        assertEquals("Action failed (left)", createHomeButton.HOME, clickType);
        clickType = null;
        tester.actionClick();        
        assertEquals("Action failed (left)", createCloseButton.CLOSE, clickType);
    }
