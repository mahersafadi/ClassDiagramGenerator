package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import com.controller.Handler;

public class MainScreen extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static int start = 300;

	
	
	 
		
			 JTextArea textArea = new JTextArea(15, 50);
			 JButton open = new JButton("open");
			 JButton generate = new JButton("generate");
	    	 Handler handler=null;
	    	 File f=null;
	    	 public MainScreen(Handler h){
	    		JFrame frame = new JFrame("Automatic Class Generation From Text Specification");
	    		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    		frame.addWindowListener(new WindowListener() {
	    			@Override
	    			public void windowOpened(WindowEvent arg0) {
						// TODO Auto-generated method stub
	    				System.out.println("Window is opened now");
	    			}
					
	    			@Override
	    			public void windowIconified(WindowEvent arg0) {
	    				// TODO Auto-generated method stub
						System.out.println("Window icon field");
	    			}
					
	    			@Override
	    			public void windowDeiconified(WindowEvent arg0) {
						// TODO Auto-generated method stub
						System.out.println("Window Deiconified");
	    			}
					
	    			@Override
	    			public void windowDeactivated(WindowEvent arg0) {
						// TODO Auto-generated method stub
						System.out.println("Window Deactivated");
	    			}
					
	    			@Override
	    			public void windowClosing(WindowEvent arg0) {
						// TODO Auto-generated method stub
						System.out.println("Window Closing");
						System.exit(ABORT);
	    			}
					
	    			@Override
	    			public void windowClosed(WindowEvent arg0) {
						// TODO Auto-generated method stub
						System.out.println("Window Closed");
	    			}
					
	    			@Override
	    			public void windowActivated(WindowEvent arg0) {
						// TODO Auto-generated method stub
						System.out.println("Window Activated");
	    			}
	    		 }
	    		);
				JPanel panel = new JPanel();
				panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
				panel.setOpaque(true); 
				handler=h;
				textArea.setWrapStyleWord(true);
				//textArea.setEditable(true);
				//textArea.setFont(Font.getFont(Font.SANS_SERIF));
				JScrollPane scroller = new JScrollPane(textArea);
				scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				JPanel inputpanel = new JPanel();
				//inputpanel.setLayout(new FlowLayout());
				inputpanel.setLayout(new BoxLayout(inputpanel, BoxLayout.X_AXIS));
				JTextField input = new JTextField(20);
				
				open.addActionListener(new ActionListener() {
			            public void actionPerformed(ActionEvent e) {
			                browseButtonActionPerformed(e);
			            }
			    });
				 
				generate.addActionListener(new ActionListener() {
			            public void actionPerformed(ActionEvent e) {
			                generateButtonActionPerformed(e);
			            }
			    });
				 
				DefaultCaret caret = (DefaultCaret) textArea.getCaret();
				caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
				 
				inputpanel.add(open);
				inputpanel.add(generate);
				panel.add(inputpanel);
				panel.add(scroller);
				frame.getContentPane().add(BorderLayout.CENTER, panel); 
				frame.pack();
				frame.setLocationByPlatform(true);
				try 
				{
				    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
				    e.printStackTrace();
				}

				frame.setVisible(true); 
				frame.setResizable(false); 
				input.requestFocus();
				} 
		
	
	 private  void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {
	        JFileChooser fc = new JFileChooser();
	       // fc.setFileFilter(new JPEGImageFileFilter());
	        int res = fc.showOpenDialog(null);
	        // We have an image!
	        try {
	            if (res == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                f=file;
	                setTarget(file);
	               
	                loadFileContent(file);
	            } // Oops!
	            else {
	                JOptionPane.showMessageDialog(null,
	                        "You must select one file to be the reference.", "Aborting...",
	                        JOptionPane.WARNING_MESSAGE);
	            }
	        } catch (Exception iOException) {
	        }

	    }

	 private  void generateButtonActionPerformed(java.awt.event.ActionEvent evt)
	 {
		 handler.readTextlines(f);
		 
	 }


public  void setTarget (File file)
{ 
   
   handler.scenario_Path=file.getPath(); 	
}
public  void loadFileContent(File file) throws FileNotFoundException
{
	 // Open the file that is the first 
	  // command line parameter

	  FileInputStream fstream = new FileInputStream(handler.scenario_Path);
	  // Get the object of DataInputStream
	  DataInputStream in = new DataInputStream(fstream);
	  BufferedReader br = new BufferedReader(new InputStreamReader(in));
	  String strLine;
	  try {
		while ((strLine = br.readLine()) != null)
		  {
			  textArea.append(strLine+"\n");
			  
		  }
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}

	
