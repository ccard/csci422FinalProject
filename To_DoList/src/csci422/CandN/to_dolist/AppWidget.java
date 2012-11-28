/*
* Chris Card
* Nathan Harvey
*10/26/12
*This class manages the widget to be displayed
*/
package csci422.CandN.to_dolist;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;


public class AppWidget extends AppWidgetProvider {
	
	//this allows for a force update of the widget if it has been created
	public static AppWidget widg = null;
	private static Context ctxt;
	private static AppWidgetManager mgr;
	private static int[] appWidgetIds;
		
	@Override
	public void onUpdate(Context ctxt, AppWidgetManager mgr, int[] appWidgetIds)
	{
		if(null == ctxt) ctxt = AppWidget.ctxt;
		if(null == mgr) mgr = AppWidget.mgr;
		if(null == appWidgetIds) appWidgetIds = AppWidget.appWidgetIds;
		
		AppWidget.widg = AppWidget.this;
		
		AppWidget.ctxt = ctxt;
		AppWidget.mgr = mgr;
		AppWidget.appWidgetIds = appWidgetIds;
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
		{
			onHCUpdate(ctxt, mgr, appWidgetIds);	
		}
		else
		{
			ctxt.startService(new Intent(ctxt, WidgetService.class));
		}
	}

	@TargetApi(11)
	public void onHCUpdate(Context ctxt, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		for (int i = 0; i < appWidgetIds.length; i++) 
		{
			Intent svcIntent = new Intent(ctxt, ListWidgetService.class);

			svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
			svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
			
			RemoteViews widget = new RemoteViews(ctxt.getPackageName(), R.layout.widget);
			
			widget.setRemoteAdapter(appWidgetIds[i], R.id.tasks, svcIntent);
			
			Intent clickIntent = new Intent(ctxt, CheckWidget.class); 
			PendingIntent clickPI = PendingIntent.getActivity(ctxt, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			widget.setPendingIntentTemplate(R.id.tasks, clickPI);
			
			appWidgetManager.updateAppWidget(appWidgetIds[i], widget);	
		}
		super.onUpdate(ctxt, appWidgetManager, appWidgetIds);
	}

}
