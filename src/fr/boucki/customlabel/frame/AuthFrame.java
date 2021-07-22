package fr.boucki.customlabel.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;

import org.simpleyaml.configuration.file.YamlFile;

import fr.boucki.customlabel.utils.Auth;
import fr.boucki.customlabel.utils.CharToString;
import fr.boucki.customlabel.utils.SpringUtilities;

public class AuthFrame extends JFrame
{
	
	private JPanel contentPane;
	private boolean isDisplay;
	
	public AuthFrame()
	{
		super("CustomLabel - Authentification");
		
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0,0));
		
		this.isDisplay = false;
		
		JPanel auth = new JPanel();
		auth.setLayout(new SpringLayout());
		
		this.setContentPane(contentPane);
		this.setPreferredSize(new Dimension(500,200));
		
		JLabel apiKeyLabel = new JLabel("Consumer Key:");
		JLabel apiSecretLabel = new JLabel("Consumer Secret:");
		JLabel accessTokenLabel = new JLabel("Access Token:");
		JLabel accessSecretLabel = new JLabel("Access Secret:");
		
		final JPasswordField apiKeyPassword= new JPasswordField();
		apiKeyPassword.setEchoChar('*');
		apiKeyLabel.setLabelFor(apiKeyPassword);
		
		final JPasswordField apiSecretPassword = new JPasswordField();
		apiSecretPassword.setEchoChar('*');
		apiSecretLabel.setLabelFor(apiSecretPassword);
		
		final JPasswordField accessTokenPassword = new JPasswordField();
		accessTokenPassword.setEchoChar('*');
		accessTokenLabel.setLabelFor(accessTokenPassword);
	
		final JPasswordField accessSecretPassword = new JPasswordField();
		accessSecretPassword.setEchoChar('*');
		accessSecretLabel.setLabelFor(accessSecretPassword);	
		
		final YamlFile pass = new YamlFile("p.yml");
        try {
            if (!pass.exists()) {
                pass.createNewFile(true);
            } 
            pass.load();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        
        String ACCESS_TOKEN = pass.getString("ACCESS_TOKEN");
        String ACCESS_TOKEN_SECRET = pass.getString("ACCESS_TOKEN_SECRET");
        String CONSUMER_KEY = pass.getString("CONSUMER_KEY");
        String CONSUMER_SECRET = pass.getString("CONSUMER_SECRET");
        
        if(ACCESS_TOKEN != null)
        	accessTokenPassword.setText(ACCESS_TOKEN);
        if(ACCESS_TOKEN_SECRET != null)
        	accessSecretPassword.setText(ACCESS_TOKEN_SECRET);
        if(CONSUMER_KEY != null)
        	apiKeyPassword.setText(CONSUMER_KEY);
        if(CONSUMER_SECRET != null)
        	apiSecretPassword.setText(CONSUMER_SECRET);
	
		
		JButton authButton = new JButton("Enregistrer");
		authButton.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) 
			 {
				 try {
					 if(apiKeyPassword.getPassword().length<=0 ||
						apiSecretPassword.getPassword().length<=0 ||
						accessTokenPassword.getPassword().length<=0 ||
						accessSecretPassword.getPassword().length<=0)
						 	throw new Exception();
					 
					 Auth authentification = new Auth(
							 					CharToString.convert(apiKeyPassword.getPassword()),
							 					CharToString.convert(apiSecretPassword.getPassword()),
							 					CharToString.convert(accessTokenPassword.getPassword()),
							 					CharToString.convert(accessSecretPassword.getPassword()));
					 
			        pass.set("ACCESS_TOKEN",CharToString.convert(accessTokenPassword.getPassword()));
			        pass.set("ACCESS_TOKEN_SECRET",CharToString.convert(accessSecretPassword.getPassword()));
			        pass.set("CONSUMER_KEY",CharToString.convert(apiKeyPassword.getPassword()));
			        pass.set("CONSUMER_SECRET",CharToString.convert(apiSecretPassword.getPassword()));
					 
				    try {
				        pass.save();
				    } catch (final IOException ex) {
				        ex.printStackTrace();
				    }
					 
					 TweetFrame tf = new TweetFrame(authentification);
					 tf.setVisible(true);
					 dispose();
				 } catch(Exception ex) {
					 JOptionPane.showMessageDialog(null,"Merci de remplir tous les champs!","Erreur", JOptionPane.WARNING_MESSAGE);
				 }
			 }
		});
		
		JButton displayButton = new JButton("Afficher MDP");
		displayButton.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                    	if(isDisplay) {
                    		apiKeyPassword.setEchoChar('*');	
                    		apiSecretPassword.setEchoChar('*');
                    		accessTokenPassword.setEchoChar('*');
                    		accessSecretPassword.setEchoChar('*');
                    	} else {
                    		apiKeyPassword.setEchoChar((char) 0);
                    		apiSecretPassword.setEchoChar((char) 0);
                    		accessTokenPassword.setEchoChar((char) 0);
                    		accessSecretPassword.setEchoChar((char) 0);
                    	}
                    	isDisplay = !isDisplay;
                    }
                });
		
		auth.add(apiKeyLabel);
		auth.add(apiKeyPassword);
			
		auth.add(apiSecretLabel);
		auth.add(apiSecretPassword);	
		
		auth.add(accessTokenLabel);
		auth.add(accessTokenPassword);
		
		auth.add(accessSecretLabel);
		auth.add(accessSecretPassword);
		
		auth.add(accessSecretLabel);
		auth.add(accessSecretPassword);
		
		auth.add(displayButton);
		auth.add(authButton);
		
		SpringUtilities.makeCompactGrid(auth,
	                5, 2, //rows, cols
	                6, 6, //initX, initY
	                6, 6); //xPad, yPad

		
		contentPane.add(auth);
		
		this.pack();
	}
}
