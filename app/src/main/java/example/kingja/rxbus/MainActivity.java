package example.kingja.rxbus;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.kingja.rxbus2.Callback;
import com.kingja.rxbus2.RxBus;

public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private TextView tv_eventMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportFragmentManager().beginTransaction().add(R.id.fl_fragmentA, new FragmentA()).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_fragmentB, new FragmentB()).commit();

        RxBus.getDefault().register(this, EventMain.class, new Callback<EventMain>() {
            @Override
            public void onReceive(EventMain event) {
                ((TextView) findViewById(R.id.tv_main_eventMsg)).setText(event.getMsg());
            }
        });
    }

    public void sendEventA(View view) {
        RxBus.getDefault().post(new EventA(this));
    }

    public void sendEventB(View view) {
        RxBus.getDefault().post(new EventB(this));
    }

    public void sendEventC(View view) {
        RxBus.getDefault().post(new EventC(this));
    }

}
