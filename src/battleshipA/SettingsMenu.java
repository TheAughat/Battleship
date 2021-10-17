package battleshipA;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.*;

public class SettingsMenu implements ActionListener, ChangeListener, MouseListener {
	
	JFrame frame;
	JSlider textSpeedBar;
	private JButton applyBtn, quitBtn;
	private JLabel txtspeed, fontselect, fontStyleSelect;
	private JComboBox<String> fontList, fontStyleList;
	
	//Couldn't resist naming these variables eyemoji (eye-emoji) and eyeCon (eye-icon) since they deal with an image of a literal eye
	private JLabel eyeCon;
	private ImageIcon eyemoji;
	
	String[] systemFonts;
	
	boolean fontsWarningShown;
	
	static Color defaultColor;
	
	
	SettingsMenu(){
		//Get all fonts available to Java on the machine it's running on, and store their names in systemFonts[]
		systemFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		
		//If this variable is false, the user will receive a warning about how the fonts can glitch sometimes
		//If it is true, the warning won't play. It is initially set to true so that the message does not pop up when the SettingsMenu() constructor is called
		//Immediately after the constructor being called, it can be set to false (in the other class).
		fontsWarningShown = true;
		//All of this is done so that the user does not have to see the message about the glitches every single time they try to change the font.
		
		//Create the GUI
		GUIConstruction();
	}
	
	public void setTextSpeed(int value) {
		textSpeedBar.setValue(value);
	}
	
	public void setEyeConVisible(boolean visibility) {
		eyeCon.setVisible(visibility);
	}
	
	
	private void GUIConstruction(){
		frame = new JFrame();
		frame.setTitle("Settings");
		frame.setSize(340, 320);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLayout(null);
		frame.setIconImage(BattleshipGUI.img.getImage());
		frame.setResizable(false);
		
		textSpeedBar = new JSlider();
		textSpeedBar.setBounds(10, 30, 300, 20);
		textSpeedBar.addChangeListener(this);
		
		txtspeed = new JLabel("Text Speed: " + textSpeedBar.getValue());
		txtspeed.setBounds(12, 10, 120, 20);
		txtspeed.setFont(BattleshipGUI.fontface);
		
		applyBtn = new JButton("Apply Changes");
		applyBtn.setBounds(20, 240, 130, 20);
		applyBtn.addActionListener(this);
		applyBtn.setFocusable(false);
		
		quitBtn = new JButton("Back");
		quitBtn.setBounds(170, 240, 130, 20);
		quitBtn.addActionListener(this);
		quitBtn.setFocusable(false);
		
		fontselect = new JLabel("Select Font: ");
		fontselect.setBounds(12, 70, 100, 20);
		fontselect.setFont(BattleshipGUI.fontface);
		
		fontList = new JComboBox<String>(systemFonts);
		fontList.addActionListener(this);
		fontList.setBounds(105, 71, 200, 21);
		fontList.setSelectedItem("Ink Free");
		fontList.setFont(BattleshipGUI.fontface);
		
		fontStyleSelect = new JLabel("Select Font Style: ");
		fontStyleSelect.setBounds(12, 130, 120, 20);
		fontStyleSelect.setFont(BattleshipGUI.fontface);
		
		String[] fontstyles = {"Plain", "Bold"};
		fontStyleList = new JComboBox<String>(fontstyles);
		fontStyleList.addActionListener(this);
		fontStyleList.setBounds(144, 130, 160, 21);
		fontStyleList.setSelectedItem("Bold");
		fontStyleList.setFont(BattleshipGUI.fontface);
		
		frame.add(txtspeed);
		frame.add(applyBtn);
		frame.add(quitBtn);
		frame.add(textSpeedBar);
		frame.add(fontselect);
		frame.add(fontList);
		frame.add(fontStyleSelect);
		frame.add(fontStyleList);
		
		eyemoji = new ImageIcon(getClass().getResource("eyemoji.png"));
		eyeCon = new JLabel("God-Eye:  On   ", eyemoji, JLabel.CENTER);
		eyeCon.setHorizontalTextPosition(JLabel.LEFT);
		eyeCon.setBounds(10, 174, 175, 40);
		eyeCon.setFont(BattleshipGUI.fontface);
		eyeCon.addMouseListener(this);
		
		frame.add(eyeCon);
		eyeCon.setVisible(false);
		
//		frame.setVisible(true);
	}

//	public static void main(String[] args) {
//		new SettingsMenu();
//	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == quitBtn) {
			frame.setVisible(false);
			
			//Sets the location of the main game GUI. This is done so that if the player moved the Settings Menu around, the GUI opens in the same spot
			//99 and 285 need to be removed from the original values because otherwise the main GUI does not appear in the center of the spot 
			BattleshipGUI.frame.setLocation(frame.getLocation().x -= 99, frame.getLocation().y -= 285);
			
			BattleshipGUI.frame.setVisible(true);
		}
		else if (e.getSource() == applyBtn) {
			
			//The slider goes from 1 to 100, but the TIMER_DELAY variable for the text speed is changed from 0 to 50
			//This is because the way the variable works causes 0 to be the fastest speed and 50 to be very slow.
			//(TIMER_DELAY is the gap between each character in the message being printed on the screen)
			//TIMER_DELAY could be set to be over 50 too, but 50 is already way too slow, so nobody would like want it to be beyond that.
			BattleshipGUI.TIMER_DELAY = (textSpeedBar.getValue() == 0)?50:50 - (textSpeedBar.getValue() / 2);
			//This is why the slider's 1 to 100 must be converted to 0 to 50 reversed
			
			updateFontSelection();
			JOptionPane.showMessageDialog(frame, "All changes applied successfully!", "Success!", 1);
		}
		
		if (e.getSource() == fontStyleList) {
			switch(fontStyleList.getSelectedItem().toString()) {
			case "Plain":
				BattleshipGUI.fontStyle = Font.PLAIN;
				break;
			
			case "Bold":
				BattleshipGUI.fontStyle = Font.BOLD;
				break;
				
			default:
				
			}
		}
		else if (e.getSource() == fontList) {
			BattleshipGUI.fontName = fontList.getSelectedItem().toString();
			if (!fontsWarningShown) {
				
				//Set to true after the message is shown once, so that the user doesn't have to get it popping on the screen all the time
				fontsWarningShown = true;
				
				JOptionPane.showMessageDialog(frame, "Warning: Some fonts do not work correctly with the size of the game screen,     "
						+ "\nand may cause visible glitches.", "Warning!", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	//Reset the font in most places in the game after changing it
	private void updateFontSelection() {
		if (BattleshipGUI.fontName.equals("Ink Free")) {
			BattleshipGUI.fontface = new Font(BattleshipGUI.fontName, (int)BattleshipGUI.fontStyle, 15);
		}
		else {
			BattleshipGUI.fontface = new Font(BattleshipGUI.fontName, (int)BattleshipGUI.fontStyle, 13);
		}
		
		eyeCon.setFont(BattleshipGUI.fontface);
		fontList.setFont(BattleshipGUI.fontface);
		txtspeed.setFont(BattleshipGUI.fontface);
		fontselect.setFont(BattleshipGUI.fontface);
		fontStyleList.setFont(BattleshipGUI.fontface);
		fontStyleSelect.setFont(BattleshipGUI.fontface);
		BattleshipGUI.instr.setFont(BattleshipGUI.fontface);
		BattleshipGUI.settingsBtn.setFont(BattleshipGUI.fontface);
		BattleshipGUI.textbox.setFont(BattleshipGUI.fontface);
		BattleshipGUI.quitgame.setFont(BattleshipGUI.fontface);
		
		for (int i=0; i<10; i++) {
			for (int j=0; j<10; j++) {
				BattleshipGUI.mapmain[i][j].setFont(BattleshipGUI.fontface);
			}
		}
		BattleshipGUI.title.setFont(new Font(BattleshipGUI.fontName, (int)BattleshipGUI.fontStyle, 40));
		BattleshipGUI.credits.setFont(new Font(BattleshipGUI.fontName, (int)BattleshipGUI.fontStyle, 17));
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		//Used for changing the numeric slider value on the user's end
		if (e.getSource() == textSpeedBar) {
			txtspeed.setText("Text Speed: " + textSpeedBar.getValue());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (Launcher.godeye) {
			eyeCon.setIcon(null);
			eyeCon.setText("God-Eye:  Off   ");
			eyeCon.setBounds(-18, 174, 175, 40);
			Launcher.godeye = false;
		}
		else {
			eyeCon.setIcon(eyemoji);
			eyeCon.setText("God-Eye:  On   ");
			eyeCon.setBounds(10, 174, 175, 40);
			Launcher.godeye = true;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		eyeCon.setForeground(Color.blue);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		eyeCon.setForeground(new Color(0, 168, 239));
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		eyeCon.setForeground(new Color(0, 168, 239));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		eyeCon.setForeground(defaultColor);
	}

}
