package com.udacity.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.sync.QuoteSyncJob;
import com.udacity.stockhawk.ui.DataActivity;
import com.udacity.stockhawk.ui.MainActivity;

import timber.log.Timber;

/**
 * Created by mahmoud on 4/21/2017.
 */
public class StockWidgetProvider extends AppWidgetProvider {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        int[] appids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, StockWidgetProvider.class));
        AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(appids, R.id.stock_widget_list_view);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {

            Intent intent = new Intent(context, StockRemoteViewService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.stock_widget);
            rv.setRemoteAdapter(appWidgetId, R.id.stock_widget_list_view, intent);

            Intent clickIntentTemplate = new Intent(context, DataActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clickIntentTemplate, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.stock_widget_list_view, pendingIntent);

            rv.setEmptyView(R.id.stock_widget_list_view, R.id.widget_empty_view);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }

    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }
}
