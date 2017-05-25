package example.kingja.rxbus;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.kingja.rxbus2.Callback;
import com.kingja.rxbus2.RxBus;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxBus.getDefault().register(this,String.class, new Callback<String>() {
            @Override
            public void onReceive(String s) {
                Log.e("MainActivity", "MainActivity onReceive: " + s);
                ((TextView) findViewById(R.id.tv_eventMsg)).setText( "MainActivity receive msg: " + s);
            }
        });
    }

    public void goSecond(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}
