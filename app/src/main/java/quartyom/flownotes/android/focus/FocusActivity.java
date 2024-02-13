package quartyom.flownotes.android.focus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import quartyom.flownotes.android.R;
import quartyom.flownotes.android.common.FlowManagerAndroid;
import quartyom.flownotes.android.common.FocusAndroid;
import quartyom.flownotes.android.common.GuideManager;
import quartyom.flownotes.android.databinding.FocusBinding;
import quartyom.flownotes.android.explorer.ExplorerActivity;
import quartyom.flownotes.core.Focus;

public class FocusActivity extends AppCompatActivity {
    private Focus focus;
    private FlowManagerAndroid flowManager;
    private FocusBinding binding;
    private String flowPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = getLayoutInflater();  // layout
        binding = FocusBinding.inflate(inflater);
        setContentView(binding.getRoot());

        flowManager = FlowManagerAndroid.getInstance(this);     // loading
        assertNotNull(flowManager, "flowManager");
        flowPath = getIntent().getStringExtra("flowPath");
        assertNotNull(flowPath, "flowPath");
        loadFocus();

        // buttons assignment
        binding.focusInitial.setOnClickListener(view -> {
            if (!FlowManagerAndroid.isEmptyString(focus.getInitialNoteText())) {  // or no?
                autoSave();
                updateUIInitial();
            }
        });

        binding.focusSaved.setOnClickListener(view -> {
            if (!FlowManagerAndroid.isEmptyString(focus.getInitialNoteText())) {
                autoSave();
                updateUISaved();
            }
        });

        binding.focusCurrent.setOnClickListener(view -> updateUICurrent());

        binding.focusNumberUp.setOnClickListener(view -> {
            if (focus.isSingleNote()) { return; }
            if (focus.isNewest()) { focus.setOldest(); }
            else { focus.numberUp(); }

            updateUICurrent();
            updateOldestNewestUI();
        });

        binding.focusNumberDown.setOnClickListener(view -> {
            if (focus.isSingleNote()) { return; }
            if (focus.isOldest()) { focus.setNewest(); }
            else { focus.numberDown(); }

            updateUICurrent();
            updateOldestNewestUI();
        });

        binding.focusNew.setOnClickListener(view -> {
            focus.newNote();
            updateUIInitial();
            updateOldestNewestUI();
        });

        binding.focusBack.setOnClickListener(view -> finish());

        binding.focusSave.setOnClickListener(view -> {
            fullSave();
            updateUICurrent();
        });

        binding.focusExploreFlow.setOnClickListener(view -> {
            startActivity(new Intent(this, ExplorerActivity.class).putExtra("flowPath", flowPath));
        });

        binding.focusEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // to show that it's now current state
                binding.focusNumber.setText(getString(R.string.focusNumberText, focus.getNumber(), getString(R.string.currentNote)));
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.focusFlowName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                flowManager.renameFlow(flowPath, charSequence.toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

    }

    @Override
    protected void onPause(){
        super.onPause();
        autoSave();
    }

    private void loadFocus() {
        if (focus != null && focus.flowPathName.equals(flowPath)) {}
        else { focus = new FocusAndroid(flowManager, flowPath); }
    }

    @Override
    protected void onResume(){
        super.onResume();

        loadFocus();
        long noteNumber = getIntent().getLongExtra("noteNumber", -1);
        if (noteNumber != -1) {
            focus.setNumber(noteNumber);
        }
        binding.focusFlowName.setText(focus.getFlowName());
        updateUICurrent();
        updateOldestNewestUI();
        GuideManager.tryInFocus(this);
    }

    private void autoSave() {
        autoSave(binding.focusEditText.getText().toString());
    }
    private void autoSave(String text) {    // saves into current state
        focus.setCurrentNoteText(text);
        focus.saveCurrentNote();
    }

    private void fullSave() {   // save into initial state (if first save) or into saved state
        String actualText = binding.focusEditText.getText().toString();
        focus.setCurrentNoteText(actualText);
        focus.saveNote();
        //focus.saveCurrentNote();
    }

    private void updateUIInitial() {
        updateInitialTime();
        binding.focusEditText.setText(focus.getInitialNoteText());
        binding.focusNumber.setText(getString(R.string.focusNumberText, focus.getNumber(), getString(R.string.initialNote)));
    }

    private void updateUISaved() {
        updateInitialTime();
        binding.focusEditText.setText(focus.getSavedNoteText());
        binding.focusNumber.setText(getString(R.string.focusNumberText, focus.getNumber(), getString(R.string.savedNote)));
    }

    private void updateUICurrent() {
        updateInitialTime();
        binding.focusEditText.setText(focus.getCurrentNoteText());
        binding.focusNumber.setText(getString(R.string.focusNumberText, focus.getNumber(), getString(R.string.currentNote)));
    }

    private void updateInitialTime() {
        String initialTime = focus.getInitialTime();
        if (initialTime.equals("")) {
            binding.focusInitialTime.setText(R.string.noteNotSaved);
        }
        else {
            binding.focusInitialTime.setText(getString(R.string.noteCreated, initialTime));
        }
    }

    private void updateOldestNewestUI() {
        if (focus.isNewest()) { binding.focusNumberUp.setImageResource(android.R.drawable.ic_media_previous); }
        else { binding.focusNumberUp.setImageResource(android.R.drawable.ic_media_play); }

        if (focus.isOldest()) { binding.focusNumberDown.setImageResource(android.R.drawable.ic_media_previous); }
        else { binding.focusNumberDown.setImageResource(android.R.drawable.ic_media_play); }
    }


    private void assertNotNull(Object object, String name){
        if (object == null) {
            Log.d(getLocalClassName(), name + " is null");
            Toast.makeText(this, R.string.errorOccurred, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}