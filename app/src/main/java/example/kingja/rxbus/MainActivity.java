package example.kingja.rxbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kingja.rxbus2.RxBus;
import com.kingja.rxbus2.Subscribe;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_fragmentA, new FragmentA()).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_fragmentB, new FragmentB()).commit();
        RxBus.getDefault().register(this);
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

    @Subscribe
    public void receiveEventMain(EventMain event) {
        ((TextView) findViewById(R.id.tv_main_eventMsg)).setText(event.getMsg());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unregister(this);
    }
}
