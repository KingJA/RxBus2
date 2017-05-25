package example.kingja.rxbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.kingja.rxbus2.Callback;
import com.kingja.rxbus2.RxBus;

public class SecondActivity extends AppCompatActivity {
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

    }

    public void send(View view) {
        RxBus.getDefault().post("来自SecondActivity"+(++count));
    }
}
