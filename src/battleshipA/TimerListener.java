package battleshipA;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class TimerListener implements ActionListener {
	
	//This variable holds the number of the character of the message that is being printed to the textbox in the GUI
	int index = 0;
	
	public void actionPerformed(ActionEvent e) {
		if (index < BattleshipGUI.msgprnt.length()) {
			//Keep adding a new character from msgprnt to the textbox as long as the index variable is lower in than the message's length
			BattleshipGUI.textbox.append(String.valueOf(BattleshipGUI.msgprnt.charAt(index)));
			index++;
		}
		else {
			//Once the index is equal to the message's length, stop the timer from running
			((Timer)e.getSource()).stop();
			//Make sure the instructions button is clickable once the message has finished printing
			BattleshipGUI.instr.setEnabled(true);
		}
	}
}
