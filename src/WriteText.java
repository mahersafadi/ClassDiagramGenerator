import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JTextArea;


public class WriteText {
	JButton btnSave = new JButton();
	private JTextArea logTA = new JTextArea();
	
	private JButton getBtnSave() {
		
		if(btnSave == null) {
			btnSave = new JButton();
			btnSave.setToolTipText("Save scan output");
			btnSave.setText("Save");
			btnSave.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try	{
						String s = logTA.getText();
						File f = new File("audits.txt");				
						FileWriter fw = new FileWriter(f);
						fw.write(s);
				}		
					catch(IOException ioe) {
						System.out.println("Exception Caught : " +ioe.getMessage());
						}
					}
				});
			}
		return btnSave;
		
	}
	
}


