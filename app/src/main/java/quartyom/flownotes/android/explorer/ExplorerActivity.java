package quartyom.flownotes.android.explorer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;

import quartyom.flownotes.android.R;
import quartyom.flownotes.android.common.FlowManagerAndroid;
import quartyom.flownotes.android.common.FocusAndroid;
import quartyom.flownotes.android.common.GuideManager;
import quartyom.flownotes.android.databinding.ExplorerBinding;
import quartyom.flownotes.android.focus.FocusActivity;
import quartyom.flownotes.android.main.MainActivity;
import quartyom.flownotes.core.Flow;
import quartyom.flownotes.core.Focus;

public class ExplorerActivity extends AppCompatActivity {

    private FlowManagerAndroid flowManager;
    private Flow flow;
    private Focus focus;
    private FocusListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = getLayoutInflater();  // short reference
        ExplorerBinding binding = ExplorerBinding.inflate(inflater);
        setContentView(binding.getRoot());

        flowManager = FlowManagerAndroid.getInstance(this);     // loading
        assertNotNull(flowManager, "flowManager");
        String flowPath = getIntent().getStringExtra("flowPath");
        assertNotNull(flowPath, "flowPath");

        flow = flowManager.loadFlow(flowPath);
        binding.explorerFlowName.setText(flow.name);

        if (focus != null && focus.flowPathName.equals(flowPath)) {}
        else { focus = new FocusAndroid(flowManager, flowPath); }

        adapter = new FocusListAdapter(this, inflater);
        loadItems();

        binding.explorerRecyclerView.setAdapter(adapter);
        binding.explorerRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        binding.explorerBack.setOnClickListener(view -> finish());

        binding.explorerNew.setOnClickListener(view -> {
            focus.newNote();
            startActivity(new Intent(this, FocusActivity.class)
                    .putExtra("flowPath", getIntent().getStringExtra("flowPath"))
                    .putExtra("noteNumber", focus.getNumber())
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });

        binding.explorerFlowName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                flowManager.renameFlow(flowPath, charSequence.toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.explorerExploreFlow.setOnClickListener(view ->finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        GuideManager.tryInExplorer(this);
    }

    private void loadItems() {
        if (!adapter.items.isEmpty()) { return; }

        focus.setOldest();
        for (long i = 0; i < focus.getFlowNotesNumber(); i++) {
            String currentNoteText = focus.getCurrentNoteText();
            currentNoteText = FlowManagerAndroid.isEmptyString(currentNoteText) ? MainActivity.newNoteText : currentNoteText;
            String initialTime = focus.getInitialTime();
            if (initialTime.equals("")) {
                initialTime = getString(R.string.noteNotSaved);
            }
            adapter.items.add(new FocusListItem(initialTime, i, currentNoteText));
            focus.numberUp();
        }
    }

    private void assertNotNull(Object object, String name){
        if (object == null) {
            Log.d(getLocalClassName(), name + " is null");
            Toast.makeText(this, R.string.errorOccurred, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
