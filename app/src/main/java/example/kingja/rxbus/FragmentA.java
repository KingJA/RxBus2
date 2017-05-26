package example.kingja.rxbus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kingja.rxbus2.Callback;
import com.kingja.rxbus2.RxBus;
import com.kingja.rxbus2.Subscribe;

/**
 * Description:TODO
 * Create Time:2017/5/25 17:21
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class FragmentA extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        RxBus.getDefault().register(this);
        rootView = inflater.inflate(R.layout.fragment_a, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((Button) rootView.findViewById(R.id.btn_sendEventB)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().post(new EventB(FragmentA.this));
            }
        });
        ((Button) rootView.findViewById(R.id.btn_sendEventMain)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().post(new EventMain(FragmentA.this));
            }
        });
        ((Button) rootView.findViewById(R.id.btn_unregisterEventA)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().unRegister(FragmentA.this, EventA.class);
            }
        });
        ((Button) rootView.findViewById(R.id.btn_unregisterEventC)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().unRegister(FragmentA.this, EventC.class);
            }
        });
        ((Button) rootView.findViewById(R.id.btn_unregisterAll)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().unRegister(FragmentA.this);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    @Subscribe
    public void receiveEventA(EventA event) {
        ((TextView) rootView.findViewById(R.id.tv_eventMsg)).setText(event.getMsg());
    }
    @Subscribe
    public void receiveEventC(EventC event) {
        ((TextView) rootView.findViewById(R.id.tv_eventMsg)).setText(event.getMsg());
    }
}
