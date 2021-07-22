package fr.boucki.customlabel.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.simpleyaml.configuration.file.YamlFile;

import fr.boucki.customlabel.utils.Auth;
import fr.boucki.customlabel.utils.SpringUtilities;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.UploadedMedia;

public class TweetFrame extends JFrame
{
	
	private Auth auth;
	private JPanel contentPane;
	private File[] medias;
	
	public TweetFrame(Auth auth)
	{	    
		
		super("CustomLabel - Tweeter");
		
		medias = new File[0];
		
		this.auth = auth;
		this.contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0,0));
		this.setContentPane(contentPane);
		
		this.setResizable(false);
		this.setPreferredSize(new Dimension(500,300));
		
		
		
		JPanel tweet = new JPanel();
		tweet.setLayout(new SpringLayout());
		
		JTextField lienReponse = new JTextField();
		lienReponse.setPreferredSize(new Dimension(500,50));
		JLabel lrl = new JLabel("Lien réponse:");
		lrl.setLabelFor(lienReponse);
	
		JTextArea contenuTweet = new JTextArea("",500,100);
		contenuTweet.setLineWrap(true);
		contenuTweet.setWrapStyleWord(true);
		contenuTweet.setFont(new Font("Calibri", Font.PLAIN, 16));

		
		contenuTweet.setPreferredSize(new Dimension(500,200));
		JLabel ctl = new JLabel("Contenu:");
		ctl.setLabelFor(contenuTweet);
		
		
		JPanel select = new JPanel();
		select.setLayout(new GridLayout(1,3));;
		JLabel photoVideoLabel = new JLabel("Importer");
		photoVideoLabel.setLabelFor(select);
		
		JButton video = new JButton("Vidéo");
		video.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(false);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Vidéos", "mov", "mp4");
				chooser.setFileFilter(filter);
				chooser.showOpenDialog(null);
			    medias = chooser.getSelectedFiles();
			    photoVideoLabel.setText("1 vidéo séléctionnée");
			}
		});
		
		JButton photo = new JButton("Photo");
		photo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(true);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Photos", "jpg", "gif","png");
				chooser.setFileFilter(filter);
				chooser.showOpenDialog(null);
			    medias = chooser.getSelectedFiles();
			    photoVideoLabel.setText(medias.length+" photo(s) séléctionnée(s)");
			}
		});
		
		JButton supprimer = new JButton("Supprimer");
		
		supprimer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				medias = new File[0];
				photoVideoLabel.setText("Importer");
			}
		});
		
		select.add(supprimer);
		select.add(video);
		select.add(photo);

		JButton envoyer = new JButton("Envoyer");
		
		envoyer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) 
			{
			    Twitter twitter = auth.getTwitter();
			    
			    StatusUpdate status = null;
			  
				try {
					
					if(lienReponse.getText().length()>0)
					{
						URI uri = new URI(lienReponse.getText());
						String[] segments = uri.getPath().split("/");
						String idStr = segments[segments.length-1];
						Long id = Long.parseLong(idStr);
						Status toReply = twitter.showStatus(id);
						status = new StatusUpdate("@"+toReply.getUser().getScreenName()+" "+contenuTweet.getText()).inReplyToStatusId(id);
					} else {
						status = new StatusUpdate(contenuTweet.getText());
						System.out.println("Pas en réponse");
					}
					
					if(medias.length>0) {
						long[] mediaIds = new long[medias.length];
						
						int i=0;
						for(File f : medias) {
							if(i<4) {
								UploadedMedia media = twitter.uploadMedia(f);
								mediaIds[i] = media.getMediaId();
								i++;
							}
						}
						status.setMediaIds(mediaIds);
					}
					
					twitter.updateStatus(status);

				    JOptionPane.showMessageDialog(new JFrame(), "Le tweet a bien été envoyé ! ", "Information",JOptionPane.INFORMATION_MESSAGE);
					
				} catch (TwitterException | URISyntaxException | NumberFormatException e1) {
					JOptionPane.showMessageDialog(new JFrame(), "Erreur,as-tu bien mis une URL complète ? as-tu mis les bon identifiants? as-tu pas trop mis de caractères (ou tweet vide) ? Si le problème persiste, contacte @Bookee0", "Information",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					
				}
			}
		});
		
		JButton deco = new JButton("Déconnexion");
		
		deco.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				final YamlFile pass = new YamlFile("p.yml");
		        try {
		            if (!pass.exists()) {
		                pass.createNewFile(true);
		            } 
		            pass.load();
		        } catch (final Exception ex) {
		            ex.printStackTrace();
		        }
				
		        pass.set("ACCESS_TOKEN","");
		        pass.set("ACCESS_TOKEN_SECRET","");
		        pass.set("CONSUMER_KEY","");
		        pass.set("CONSUMER_SECRET","");
			
			    try {
			        pass.save();
			    } catch (final IOException ex) {
			        ex.printStackTrace();
			        dispose();
			    }
			    
			    dispose();
			}
		});
		
		tweet.add(lrl);
		tweet.add(lienReponse);
		
		tweet.add(ctl);
		tweet.add(contenuTweet);
	
		tweet.add(photoVideoLabel);
		tweet.add(select);
		
		tweet.add(deco);
		tweet.add(envoyer);
	
		
		SpringUtilities.makeCompactGrid(tweet,
                4, 2, //rows, cols
                6, 6, //initX, initY
                6, 6); //xPad, yPad
		
		contentPane.add(tweet);
		this.pack();
	}
}
