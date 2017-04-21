package com.udacity.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import timber.log.Timber;

/**
 * Created by mahmoud on 4/21/2017.
 */
public class StockRemoteViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        Timber.d("public class StockRemoteViewService extends RemoteViewsService {\n");
        return new StockRemoteViewsFactory(this.getApplicationContext(), intent);

    }

    class StockRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{
        private final int mAppWidgetId;
        private Context mContext;
        private Cursor mCursor;

        public StockRemoteViewsFactory(Context mContext, Intent intent) {
            this.mContext = mContext;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            mCursor = mContext.getContentResolver().query(Contract.Quote.URI,
                    null, null,
                    null, null);
            if (mCursor != null){
                mCursor.moveToFirst();
                Timber.d("length = " + mCursor.getCount());
            }
        }

        @Override
        public void onDataSetChanged() {
            mCursor = mContext.getContentResolver().query(Contract.Quote.URI,
                    null, null,
                    null, null);
            if (mCursor != null){
                mCursor.moveToFirst();
                Timber.d("length = " + mCursor.getCount());
            }
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (mCursor == null){
                Timber.d("null cursor");
                return 0;
            }else {
                return mCursor.getCount();
            }
        }

        @Override
        public RemoteViews getViewAt(int i) {
            Timber.d("size = " + getCount());
            mCursor.moveToPosition(i);
            String symbol = mCursor.getString(mCursor.getColumnIndex(Contract.Quote.COLUMN_SYMBOL));

            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
            rv.setTextViewText(R.id.symbol, symbol);


            float rawAbsoluteChange = mCursor.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
            float price = mCursor.getFloat(Contract.Quote.POSITION_PRICE);
            if(rawAbsoluteChange > 0){
                rv.setTextViewText(R.id.change,  "+"+ String.valueOf(rawAbsoluteChange));
            }else{
                rv.setTextViewText(R.id.change,  String.valueOf(rawAbsoluteChange));
            }
            rv.setTextViewText(R.id.price, "$"+String.valueOf(price));
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
