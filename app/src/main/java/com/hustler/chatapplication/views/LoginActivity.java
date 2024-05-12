package com.hustler.chatapplication.views;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.hustler.chatapplication.R;
import com.hustler.chatapplication.databinding.ActivityLoginBinding;
import com.hustler.chatapplication.viewmodel.MyViewModel;

public class LoginActivity extends AppCompatActivity {

    MyViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(MyViewModel.class);
        ActivityLoginBinding activityLoginBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_login);

        activityLoginBinding.setVModel(viewModel);
    }
}