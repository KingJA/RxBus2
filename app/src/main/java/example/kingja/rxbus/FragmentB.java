package example.kingja.rxbus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kingja.rxbus2.RxBus;
import com.kingja.rxbus2.Subscribe;

/**
 * Description:TODO
 * Create Time:2017/5/25 17:21
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class FragmentB extends Fragment {
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_b, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((Button) rootView.findViewById(R.id.btn_sendEventA)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().post(new EventA(FragmentB.this));
            }
        });
        ((Button) rootView.findViewById(R.id.btn_sendEventMain)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().post(new EventMain(FragmentB.this));
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RxBus.getDefault().register(this);
    }
    @Subscribe
    public void receiveEventB(EventB event) {
        ((TextView) rootView.findViewById(R.id.tv_eventMsg)).setText(event.getMsg());
    }
    @Subscribe
    public void receiveEventC(EventC event) {
        ((TextView) rootView.findViewById(R.id.tv_eventMsg)).setText(event.getMsg());
    }
}
