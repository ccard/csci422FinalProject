/*
 * Chris card
 * Nathan Harvey
 * 11/15/12
 * This class provides code to sync with google calandars or other files
 * and will be started by a intent service and will be using a singleton pattern
 */

package csci422.CandN.to_dolist;

public class FileSync {

	private static FileSync instance = null;
	
	private FileSync()
	{
		
	}
	
	public static FileSync getInstance()
	{
		if(instance == null)
		{
			instance = new FileSync();
		}
		
		return instance;
	}
}
