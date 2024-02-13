package quartyom.flownotes.android.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import quartyom.flownotes.android.R;
import quartyom.flownotes.android.common.InfoDialogFragment;

public class FlowAdapterCallback extends ItemTouchHelper.SimpleCallback {
    FlowsListAdapter adapter;
    AppCompatActivity activity;

    public FlowAdapterCallback(AppCompatActivity activity, FlowsListAdapter adapter) {
        super(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.ACTION_STATE_IDLE);
        this.adapter = adapter;
        this.activity = activity;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        adapter.swap(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.requestRemove(viewHolder.getAdapterPosition());
    }
}