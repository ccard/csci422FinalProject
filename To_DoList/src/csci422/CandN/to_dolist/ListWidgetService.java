/*
*Chris Card
*Nathan Harvey
*10/26/12
*This class manages the creation of listview factory and runs as a service
*/
package csci422.CandN.to_dolist;

import android.annotation.TargetApi;
import android.content.Intent;
import android.widget.RemoteViewsService;

@TargetApi(11)
public class ListWidgetService extends RemoteViewsService {

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) 
	{
		return(new ListViewsFactory(this.getApplicationContext(), intent));
	}

}
