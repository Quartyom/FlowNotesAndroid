package quartyom.flownotes.android.explorer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import quartyom.flownotes.android.R;
import quartyom.flownotes.android.databinding.FocusTileBinding;
import quartyom.flownotes.android.focus.FocusActivity;

public class FocusListAdapter extends RecyclerView.Adapter<FocusListAdapter.ViewHolder> {
    public final List<FocusListItem> items = new ArrayList<>();
    public final LayoutInflater inflater;
    public final ExplorerActivity activity;

    public FocusListAdapter(ExplorerActivity activity, LayoutInflater inflater){
        this.activity = activity;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FocusTileBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FocusListItem item = items.get(position);
        holder.binding.initialTime.setText(item.initialTime);
        holder.binding.currentNoteText.setText(item.currentNoteText);
        holder.binding.noteNumber.setText(activity.getString(R.string.focusNumberText, item.noteNumber, ""));
        holder.noteNumber = item.noteNumber;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private FocusTileBinding binding;
        private long noteNumber;

        public ViewHolder(FocusTileBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(view -> {
                activity.startActivity(new Intent(activity, FocusActivity.class)
                        .putExtra("flowPath", activity.getIntent().getStringExtra("flowPath"))
                        .putExtra("noteNumber", noteNumber)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            });
        }
    }
}
