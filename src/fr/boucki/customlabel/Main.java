package fr.boucki.customlabel;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import fr.boucki.customlabel.frame.AuthFrame;

public class Main 
{
	
	
	public static void main(String[] args)
	{
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AuthFrame auth = new AuthFrame();
		auth.setVisible(true);
	}
}
