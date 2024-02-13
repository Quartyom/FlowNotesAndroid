package quartyom.flownotes.android.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import java.util.List;

import quartyom.flownotes.android.R;
import quartyom.flownotes.android.common.FlowManagerAndroid;
import quartyom.flownotes.android.common.GuideManager;
import quartyom.flownotes.android.focus.FocusActivity;
import quartyom.flownotes.android.common.FocusAndroid;
import quartyom.flownotes.android.databinding.ListOfFlowsBinding;
import quartyom.flownotes.android.info.InfoActivity;
import quartyom.flownotes.core.Flow;
import quartyom.flownotes.core.Focus;

public class MainActivity extends AppCompatActivity {
    private FlowManagerAndroid flowManager;
    private FlowsListAdapter adapter;

    public static String newFlowName = "New flow";
    public static String newNoteText = "Initial text";

    private boolean isEditFlowsOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = getLayoutInflater();  // short reference
        ListOfFlowsBinding binding = ListOfFlowsBinding.inflate(inflater);
        setContentView(binding.getRoot());

        adapter = new FlowsListAdapter(this, inflater);
        List<FlowListItem> items = adapter.items;   // short reference

        appInit();
        loadItems();

        binding.flowsRecyclerView.setAdapter(adapter);
        binding.flowsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        FlowAdapterCallback flowAdapterCallback = new FlowAdapterCallback(this, adapter);
        new ItemTouchHelper(flowAdapterCallback).attachToRecyclerView(binding.flowsRecyclerView);

        binding.infoButton.setOnClickListener(view -> startActivity(new Intent(this, InfoActivity.class)));

        binding.editFlowsButton.setOnClickListener(view -> {
            if (isEditFlowsOn) {
                isEditFlowsOn = false;
                binding.editFlowsButton.setColorFilter(getColor(R.color.light_gray));
                flowAdapterCallback.setDefaultDragDirs(ItemTouchHelper.ACTION_STATE_IDLE);
                flowAdapterCallback.setDefaultSwipeDirs(ItemTouchHelper.ACTION_STATE_IDLE);
            }
            else {
                isEditFlowsOn = true;
                binding.editFlowsButton.setColorFilter(getColor(R.color.purple));
                flowAdapterCallback.setDefaultDragDirs(ItemTouchHelper.UP | ItemTouchHelper.DOWN);
                flowAdapterCallback.setDefaultSwipeDirs(ItemTouchHelper.START | ItemTouchHelper.END);
            }
        });

        binding.newFlowButton.setOnClickListener(view -> {
            String pathName = flowManager.createFlow(newFlowName);
            if (pathName != null) {
                items.add(new FlowListItem(newFlowName, pathName, newNoteText));
                adapter.notifyItemInserted(items.size() - 1);
                startActivity(new Intent(this, FocusActivity.class).putExtra("flowPath", pathName));
                positionToUpdate = items.size() - 1;
            }
            else {
                Toast.makeText(this, R.string.flowNotCreated, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void appInit() {
        newFlowName = getResources().getString(R.string.newFlowName);
        newNoteText = getResources().getString(R.string.newNoteText);

        flowManager = FlowManagerAndroid.getInstance(this);
        GuideManager.initialize(flowManager.gson, getFilesDir());
    }

    public int positionToUpdate = -1;   // for case if the flow if edited in another activity

    @Override
    protected void onStart() {
        super.onStart();
        if (positionToUpdate != -1) {
            adapter.items.set(positionToUpdate, loadItem(adapter.items.get(positionToUpdate).flowPathName));    // reload
            adapter.notifyItemChanged(positionToUpdate);
            positionToUpdate = -1;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GuideManager.tryInMenu(this);
    }

    private void loadItems(){
        if (!adapter.items.isEmpty()) { return; }

        for (String dirName : flowManager.getDirsList()) {
            adapter.items.add(loadItem(dirName));
        }
    }

    private FlowListItem loadItem(String dirName){
        Flow flow = flowManager.loadFlow(dirName);
        Focus focus = new FocusAndroid(flowManager, dirName);
        String currentNoteText = focus.getCurrentNoteText();
        currentNoteText = FlowManagerAndroid.isEmptyString(currentNoteText) ? newNoteText : currentNoteText;
        return new FlowListItem(flow.name, dirName, currentNoteText);
    }

}