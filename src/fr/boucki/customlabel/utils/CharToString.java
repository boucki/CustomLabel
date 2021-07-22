package fr.boucki.customlabel.utils;

public class CharToString 
{
	public static String convert(char[] tab)
	{
		String t = "";
		for(char c : tab)
		{
			t += c;
		}
		return t;
	}
}
