/*
 * Chris Card
 * Nathan Harvey
 * 11/15/12
 * This intent service will be spawned to allow for sync tasks to be done in background
 */

package csci422.CandN.to_dolist;

import android.app.IntentService;
import android.content.Intent;

public class SyncFiles extends IntentService {

	public static final String HELPER = "csci422.CandN.to_dolist.ToDoHelper";


	public SyncFiles(String name) {
		super("SyncFiles");
	}

	@Override
	protected void onHandleIntent(Intent i) 
	{
		ToDoHelper help = (ToDoHelper)i.getExtras().get(HELPER);

		FileSync.getInstance().syncWithCal(help);

		//TODO: if we sync with file put here
	}

}
