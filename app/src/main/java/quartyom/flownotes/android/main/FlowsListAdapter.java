package quartyom.flownotes.android.main;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import quartyom.flownotes.android.common.FlowManagerAndroid;
import quartyom.flownotes.android.focus.FocusActivity;
import quartyom.flownotes.android.databinding.FlowTileBinding;

public class FlowsListAdapter extends RecyclerView.Adapter<FlowsListAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    public final List<FlowListItem> items = new ArrayList<>();
    public final MainActivity activity;

    public FlowsListAdapter(MainActivity activity, LayoutInflater inflater){
        this.activity = activity;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FlowTileBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FlowListItem item = items.get(position);
        holder.binding.flowNameText.setText(item.flowName);
        holder.binding.lastNoteText.setText(item.lastNoteText);
        holder.flowPath = item.flowPathName;
        // holder.position = position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void swap(int firstPosition, int secondPosition) {
        FlowManagerAndroid.getInstance(activity).swapDirs(firstPosition, secondPosition);   // physical swap
        Collections.swap(items, firstPosition, secondPosition);
        notifyItemMoved(firstPosition, secondPosition);                     // ui swap
    }

    public void requestRemove(int i) {
        FlowListItem item = items.get(i);
        DeleteFlowDialogFragment dialogFragment = new DeleteFlowDialogFragment(this, item.flowName, i);
        dialogFragment.show(activity.getSupportFragmentManager(), "FlowsListAdapter");
    }
    public void remove(int i) {
        FlowListItem item = items.get(i);
        if (FlowManagerAndroid.getInstance(activity).deleteFlow(item.flowPathName)) {    // physical deletion
            items.remove(i);     // ui deletion
            notifyItemRemoved(i);    // info for adapter
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private FlowTileBinding binding;
        private String flowPath;
        // private int position = -1;
        public ViewHolder(FlowTileBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(view -> {
                activity.startActivity(new Intent(activity, FocusActivity.class).putExtra("flowPath", flowPath));
                activity.positionToUpdate = getAdapterPosition();
            });

        }
    }
}
