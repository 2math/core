package com.futurist_labs.android.base_library.ui.versions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.futurist_labs.android.base_library.R;
import com.futurist_labs.android.base_library.model.BaseLibraryConfiguration;
import com.futurist_labs.android.base_library.utils.IntentUtils;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {

    public static void show(Context context) {
        Intent starter = new Intent(context, UpdateActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openAppInPlayStore(UpdateActivity.this,
                        BaseLibraryConfiguration.getInstance().APPLICATION_ID);
            }
        });
    }
}
