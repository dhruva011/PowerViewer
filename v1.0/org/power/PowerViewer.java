package org.power;

import java.awt.BorderLayout;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;

public class PowerViewer extends JFrame implements KeyListener{
	Manager m;
	static long BUFFER_SIZE = 50000;
	private static final long serialVersionUID = 13081991L;
	JFrame powerFrame;
	JMenuBar menu;
	JMenu file;
	JMenu edit;
	JMenuItem open, newFile,save, exit;
	JMenuItem undo,paste, selectAll ;
	JMenuItem aboutUs ;
	JMenuItem bufferSize ;
	JMenu format;
	JMenu view;
	JMenu config;
	JMenu help;
	JFileChooser fileChooser;
	JTextArea textArea;
	JLabel statusBar;
	JCheckBoxMenuItem status,wordwrap;
	Clipboard clip ;
	final String aboutText=
			"<html><h2>PowerViewer</h2><hr>"
			+"<p align=right>Developed By - Punit Sharma"
			+"<hr>"
			+"<strong>Write To me </strong><br>"
			+"for any bug or suggestion<p align=center>"
			+"<hr><em><strong>dhruva011@gmail.com</strong></em><hr><html>";

	long totalPages;
	long currentPageNum=1;
	
	public static void main(String[] args) {
			new PowerViewer();
	}
	
	PowerViewer() {
		powerFrame = new JFrame("PowerViewer");
		
		createAndAddMenuBar();
		
		textArea = new JTextArea();
		fileChooser = new JFileChooser();
		powerFrame.setLayout(new BorderLayout());
		powerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        powerFrame.add(textArea);
        
		addAllListener();
		//f.add(new JScrollPane(ta),BorderLayout.CENTER);
		powerFrame.add(new JScrollPane(textArea),BorderLayout.CENTER);
		powerFrame.add(new JLabel("  "),BorderLayout.EAST);
		powerFrame.add(new JLabel("  "),BorderLayout.WEST);
		statusBar=new JLabel("||       PowerViewer  "+"\n",JLabel.RIGHT);
		powerFrame.add(statusBar,BorderLayout.SOUTH);
		powerFrame.setSize(800, 600);
		
		powerFrame.setVisible(true);
	}
	
	void createAndAddMenuBar() {
		//Create the menu bar
			file = new JMenu("File");
			newFile = new JMenuItem("New");
			open = new JMenuItem("Open");		
			save = new JMenuItem("Save As");
			exit = new JMenuItem("Exit");
			file.add(newFile);
			file.add(open);
			file.add(save);
			file.add(exit);
			
			edit = new JMenu("Edit");
			undo = new  JMenuItem("Undo                 Ctrl+Z");
			paste = new JMenuItem("Paste                Ctrl+V");
			selectAll = new JMenuItem("Select All       Ctrl+A ");
			edit.add(undo);
			edit.add(paste);
			edit.add(selectAll);
			
			view = new JMenu("View");
			status = new JCheckBoxMenuItem("Status Bar");
			status.setSelected(true);
			view.add(status);
			
			format = new JMenu("Format");
			wordwrap = new JCheckBoxMenuItem("Word Wrap");
			format.add(wordwrap);
			
			config = new JMenu("Config");
			bufferSize = new JMenuItem("Buffer Size");
			config.add(bufferSize);
			
			help = new JMenu("Help");
			aboutUs = new JMenuItem("About PowerViewer");
			help.add(aboutUs);
			
			menu = new JMenuBar();
			menu.add(file);
			//menu.add(edit);
			menu.add(format);
			menu.add(view);
			//for v2.0
			//menu.add(config);
			menu.add(help);
			
			powerFrame.setJMenuBar(menu);
	}

	void addAllListener() {
		OpenListener openL = new OpenListener();
		NewListener NewL = new NewListener();
		SaveListener saveL = new SaveListener();
		ExitListener exitL = new ExitListener();
		StatusBarListener statusL = new StatusBarListener();
		WordWrapListener wordwL = new WordWrapListener();
		AboutUsListener abtusL = new AboutUsListener();
		//BufferUpdateListener bsl = new BufferUpdateListener();
		open.addActionListener(openL);
		newFile.addActionListener(NewL);
		save.addActionListener(saveL);
		exit.addActionListener(exitL);
		status.addActionListener(statusL);
		wordwrap.addActionListener(wordwL);
		aboutUs.addActionListener(abtusL);
		textArea.addKeyListener(this);
		//bufferSize.addActionListener(bsl);
	}
	
	class OpenListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(powerFrame)) {
				totalPages = fileChooser.getSelectedFile().length()/BUFFER_SIZE;
				totalPages = totalPages<=0?1:totalPages;
				currentPageNum=1;
				try {
				SharedData.setData("");
				if(m==null) {
					m = new Manager(fileChooser.getSelectedFile());
					Manager.setBUFFER_SIZE(BUFFER_SIZE);
					m.init();
				}
				else {
					Manager.getFileManager().destroy();
					Manager.setFile(fileChooser.getSelectedFile());
					m.reset();
				}
				Thread.sleep(100);
				textArea.setText(SharedData.getData().trim());
				statusBar.setText("Buffer Size "+BUFFER_SIZE+" Bytes    |    "+"Page "+currentPageNum+"/"+totalPages+"  ");
				textArea.setCaretPosition(0);
				}
				catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	class StatusBarListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JCheckBoxMenuItem lstnr = (JCheckBoxMenuItem)e.getSource();
			//System.out.println(lstnr.isSelected());
			if(lstnr.isSelected())
				statusBar.setVisible(true);
			else
				statusBar.setVisible(false);
		}
	}
	
	class WordWrapListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JCheckBoxMenuItem lstnr = (JCheckBoxMenuItem)e.getSource();
			//System.out.println(lstnr.isSelected());
			textArea.setLineWrap(lstnr.isSelected());
		}
	}
	
	class AboutUsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(powerFrame,aboutText,"PowerViewer",JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	class BufferUpdateListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String result = (String)JOptionPane.showInputDialog(
		               powerFrame,
		               "Configure the Buffer Size(In Bytes)", 
		               "Configuration",            
		               JOptionPane.PLAIN_MESSAGE,
		               null,            
		               null, 
		               BUFFER_SIZE+""
		            );
			
		            if(result != null && result.length() > 0){
		            	try {
		            		long input = Long.parseLong(result);
		            		if(input>=500 && input<=100000) {
		            			BUFFER_SIZE=input;
		            			statusBar.setText("Buffer size updated successfully to "+BUFFER_SIZE+" Bytes  ");
		            			m=null;
		            			textArea.setText("");
		            			JOptionPane.showMessageDialog(powerFrame, "Buffer size updated successfully to "+BUFFER_SIZE+" Bytes. Now reopen the file.", "Success!", JOptionPane.INFORMATION_MESSAGE);
		            		}
		            		else {
		            			JOptionPane.showMessageDialog(powerFrame, "Buffer size should be in between 500 to 100000 Bytes ", "Invalid Input", JOptionPane.ERROR_MESSAGE);
		            			statusBar.setText("Buffer size should be in between 500 to 100000 Bytes  ");
		            		}
		            	}
		            	catch(Exception ex) {
		            		JOptionPane.showMessageDialog(powerFrame, "Invaid Buffer Size", "Invalid Input", JOptionPane.ERROR_MESSAGE);
		            	}
		            }else {
		            	statusBar.setText("Buffer size should be in between 500 to 100000 Bytes  ");
		            }
		}
	}
	
	class SaveListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(powerFrame)) {
				File file = fileChooser.getSelectedFile();
				PrintWriter out = null;
				try {
					out = new PrintWriter(file);
					String output = textArea.getText();
					System.out.println(output);
					out.println(output);
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					try {
						out.flush();
						} catch(Exception ex1) 
						{
							
						}
					try {
						out.close();
						} catch(Exception ex1) {
							
						}
				}
			}
		}
	}
	
	class NewListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			textArea.setText("");
		}
	}
	class ExitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	
	
	class PasteListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Transferable cliptran = clip.getContents(PowerViewer.this);
	         try
	             {
	             String sel = (String) cliptran.getTransferData(DataFlavor.stringFlavor);
	             textArea.replaceRange(sel,textArea.getSelectionStart(),textArea.getSelectionEnd());
	         }
	         catch(Exception exc)
	             {
	             System.out.println("not string flavour");
	         }
			
		}
	}



	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		handleKey(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	void handleKey(KeyEvent e) {
		if(m==null)
			return;
		int keyCode = e.getKeyCode();
	    switch( keyCode ) { 
	        case KeyEvent.VK_UP:
	            // handle up 
	            break;
	        case KeyEvent.VK_DOWN:
	            // handle down 
	            break;
	        case KeyEvent.VK_LEFT:
	        	try {
	        		m.scrollUp();
	        		if(currentPageNum>1)
	        		currentPageNum--;
	        		textArea.setText(SharedData.getData());
	        		statusBar.setText("Buffer Size "+BUFFER_SIZE+" Bytes    |    "+"Page "+currentPageNum+"/"+totalPages+"  ");
		            textArea.setCaretPosition(0);
	        	} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
	        		e1.printStackTrace();
	        	} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
	        	}
	            break;
	        case KeyEvent.VK_RIGHT :
	        	try {
	        		m.scrollDown();
	        		if(currentPageNum<totalPages)
	        		currentPageNum++;
	        		textArea.setText(SharedData.getData());
	        		statusBar.setText("Buffer Size "+BUFFER_SIZE+" Bytes    |    "+"Page "+currentPageNum+"/"+totalPages+"  ");
		            textArea.setCaretPosition(0);
	        	} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
	        		e1.printStackTrace();
	        	} catch (IOException e1) {
				// TODO Auto-generated catch block
	        		e1.printStackTrace();
	        	}
	            break;
	     }
	}
	
}