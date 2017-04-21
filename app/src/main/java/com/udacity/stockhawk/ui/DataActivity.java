package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class DataActivity extends AppCompatActivity {

    public static final String SYMBOL_EXTRA = "symbol extra";

    private static final int LOADER_FLAG = 55454;


    private String mSymbol;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.graph)
    GraphView mGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        ButterKnife.bind(this);

        mSymbol = getIntent().getStringExtra(SYMBOL_EXTRA);
        getSupportActionBar().setTitle(mSymbol);

        getSupportLoaderManager().initLoader(LOADER_FLAG, null,mLoaderCallbacks);
    }

    LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getApplicationContext(),
                    Contract.Quote.makeUriForStock(mSymbol),
                    null,
                    null,
                    null,
                    null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            data.moveToFirst();
            String values = data.getString(Contract.Quote.POSITION_HISTORY);
            String[]lines = values.split(System.getProperty("line.separator"));;
            mGraph.removeAllSeries();
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

            Timber.d("values" + values);
            Timber.d("something");
            if(lines.length > 11)
                for (int i = 11; i>=0 ; i--) {
                    String[] xy = lines[i].split(",");
                    series.appendData(new DataPoint(new Date(Long.valueOf(xy[0])), Double.valueOf(xy[1])), true, lines.length);
                }

            mGraph.addSeries(series);
            mGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));
            mGraph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space


        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

}
