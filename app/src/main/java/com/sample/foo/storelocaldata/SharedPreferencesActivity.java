package com.sample.foo.storelocaldata;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sample.foo.storelocaldata.databinding.ActivitySharedPreferencesBinding;

import java.util.Map;

public class SharedPreferencesActivity extends AppCompatActivity {

    private ActivitySharedPreferencesBinding sharedPreferencesBinding;
    private String TAG = "SharedPrefActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_shared_preferences);

        sharedPreferencesBinding.addPrefButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyString = sharedPreferencesBinding.keyEditView.getText().toString();
                String valueString = sharedPreferencesBinding.valueEditView.getText().toString();
                if(keyString.isEmpty() || valueString.isEmpty()) {
                    Toast.makeText(SharedPreferencesActivity.this,
                            "Please enter a Key and Value", Toast.LENGTH_LONG).show();
                }
                else {
                    String fileNameString = sharedPreferencesBinding.fileNameEditView.getText().toString();
                    SharedPreferences sharedPreferences;
                    if(fileNameString.isEmpty()) {
                        sharedPreferences = getPreferences(MODE_PRIVATE);
                    }
                    else {
                        sharedPreferences = getSharedPreferences(fileNameString, MODE_PRIVATE);
                    }
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(keyString, valueString);
                    editor.commit();
                    Log.d(TAG, "Saved key " + keyString + ", value " + valueString);
                    Toast.makeText(SharedPreferencesActivity.this,
                            "Saved key " + keyString + ", value " + valueString,
                            Toast.LENGTH_SHORT).show();

                    sharedPreferencesBinding.keyEditView.setText("");
                    sharedPreferencesBinding.valueEditView.setText("");
                }
            }
        });

        sharedPreferencesBinding.loadPrefButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileNameString = sharedPreferencesBinding.fileNameEditView.getText().toString();
                SharedPreferences sharedPreferences;
                if(fileNameString.isEmpty()){
                    Toast.makeText(SharedPreferencesActivity.this,
                            "Loading SharedPrefernces with no filename",
                            Toast.LENGTH_SHORT).show();
                    sharedPreferences = getPreferences(MODE_PRIVATE);
                }
                else {
                    sharedPreferences = getSharedPreferences(fileNameString, MODE_PRIVATE);
                }
                Map<String, String> allPreferences = (Map<String, String>) sharedPreferences.getAll();
                Log.d(TAG, "allPreferences has " + allPreferences.size());

                SharedPrefListAdapter adapter = new SharedPrefListAdapter(allPreferences);
                sharedPreferencesBinding.recycleView.setAdapter(adapter);
                sharedPreferencesBinding.recycleView.setLayoutManager(
                        new LinearLayoutManager(SharedPreferencesActivity.this));
            }
        });

//        sharedPreferencesBinding.recycleView
    }
}
