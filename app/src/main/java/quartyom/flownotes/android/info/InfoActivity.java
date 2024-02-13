package quartyom.flownotes.android.info;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;

import quartyom.flownotes.android.common.GuideManager;
import quartyom.flownotes.android.databinding.InformationBinding;

public class InfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = getLayoutInflater();  // short reference
        InformationBinding binding = InformationBinding.inflate(inflater);
        setContentView(binding.getRoot());

        binding.infoBack.setOnClickListener(view -> finish());

        binding.infoGuideButton.setOnClickListener(view -> GuideManager.reset());
    }
}
