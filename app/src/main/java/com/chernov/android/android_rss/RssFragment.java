package com.chernov.android.android_rss;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.os.Handler;
import android.widget.Toast;

import java.util.List;

public class RssFragment extends ListFragment {

    Parcelable state;
    RssAdapter adapter;
    static final String TAG = "myLog";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // при смене ориентации экрана фрагмент сохраняет свое состояние. onDestroy не вызывается
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instance) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // запускаем сервис, парсим страницу
        if(adapter==null) {
            startService();
        }

        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set new items
        setListAdapter(adapter);
        // Restore previous state (including selected item index and scroll position)
        if(state != null) {
            getListView().onRestoreInstanceState(state);
            if(adapter!=null) adapter.notifyDataSetChanged();
        }
    }

    // общение между потоками через ResultReceiver
    private final ResultReceiver resultReceiver = new ResultReceiver(new Handler()) {
        @SuppressWarnings("unchecked")
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            /*
             *  если resultCode==1, принимаем List, создаем adapter и прикрепляем его к listView
             *  создаем и присоединяем адаптер к listview
            */

            if(resultCode==1) {
                adapter = new RssAdapter(getActivity(),
                        (List<Item>) resultData.getSerializable(RssService.ITEMS));
                setListAdapter(adapter);
            } else {
                Toast.makeText(getActivity(), getString(R.string.error),
                        Toast.LENGTH_LONG).show();
            }
        };
    };

    // запускаем сервис, парсим страницу
    private void startService() {
        Intent intent = new Intent(getActivity(), RssService.class);
        intent.putExtra(RssService.RECEIVER, resultReceiver);
        getActivity().startService(intent);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(adapter.getItem(position).getLink()));
        startActivity(intent);
    }

    @Override
    public void onPause() {
        Log.e(TAG, "onPause");
        // Save ListView state @ onPause
        state = getListView().onSaveInstanceState();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "fragment onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // если сервис не остановлен, останавливаем его
        if(!RssService.isStopped) {
            getActivity().stopService(new Intent(getActivity(), RssService.class));
        }
        // освобождаем ресурс
        if(adapter!=null) adapter.clear();
    }
}

