package fr.boucki.customlabel.utils;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class Auth 
{
	
	private String ACCESS_TOKEN;
	private String ACCESS_TOKEN_SECRET;
	private String CONSUMER_KEY;
	private String CONSUMER_SECRET;
	
	private Twitter twitter;
	
	public Auth(String apiKeyPassword,String apiSecretPassword,String accessTokenPassword,String accessSecretPassword)
	{
		ACCESS_TOKEN = accessTokenPassword;
		ACCESS_TOKEN_SECRET = accessSecretPassword;
		CONSUMER_KEY = apiKeyPassword;
		CONSUMER_SECRET = apiSecretPassword;
		
		
		this.twitter = TwitterFactory.getSingleton();
		
		this.twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		this.twitter.setOAuthAccessToken(new AccessToken(ACCESS_TOKEN, ACCESS_TOKEN_SECRET));
	}
	
	public Twitter getTwitter()
	{
		return this.twitter;
	}
	
	public String getAccessToken()
	{
		return ACCESS_TOKEN;
	}
	
	public String getAccessTokenSecret()
	{
		return ACCESS_TOKEN_SECRET;
	}
	
	public String getConsumerKey()
	{
		return CONSUMER_KEY;
	}
	
	public String getConsumerSecret()
	{
		return CONSUMER_SECRET;
	}
}
