package com.chernov.android.android_rss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RssFragment extends ListFragment {

    RssAdapter adapter;
    List<Item> items = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // при смене ориентации экрана фрагмент сохраняет свое состояние. onDestroy не вызывается
        setRetainInstance(true);
        // создаем адаптер
        adapter = new RssAdapter(getActivity(), items);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instance) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // запускаем сервис, парсим страницу
        startService();

        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set new items
        setListAdapter(adapter);
    }

    // общение между потоками через ResultReceiver
    private final ResultReceiver resultReceiver = new ResultReceiver(new Handler()) {
        @SuppressWarnings("unchecked")
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            /*
             *  если resultCode==1, принимаем List
             *  после передергиваем адаптер
            */

            if(resultCode==1) {
                items.clear();
                items.addAll((List<Item>) resultData.getSerializable(RssService.ITEMS));
            } else {
                Toast.makeText(getActivity(), getString(R.string.error),
                        Toast.LENGTH_LONG).show();
            }
            adapter.notifyDataSetChanged();
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

        FragmentManager manager = getActivity().getSupportFragmentManager();
        Fragment fragment = new RssWebViewFragment(adapter.getItem(position).getLink());
        manager.beginTransaction()
              .replace(R.id.fragmentContainer, fragment)
              .commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

