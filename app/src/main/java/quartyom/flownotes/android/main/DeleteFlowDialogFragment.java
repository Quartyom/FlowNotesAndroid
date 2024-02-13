package quartyom.flownotes.android.main;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;


import androidx.annotation.NonNull;

import quartyom.flownotes.android.common.FlowManagerAndroid;
import quartyom.flownotes.android.R;

public class DeleteFlowDialogFragment extends DialogFragment {
    String flowName;
    FlowsListAdapter adapter;
    int position;
    public DeleteFlowDialogFragment(FlowsListAdapter adapter, String flowName, int position) {
        this.flowName = flowName;
        this.adapter = adapter;
        this.position = position;
    }
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setMessage(adapter.activity.getString(R.string.deletionConfinm, flowName))
                .setPositiveButton(R.string.yes, (dialogInterface, which) -> adapter.remove(position))
                .setNegativeButton(R.string.no, (dialogInterface, which) -> adapter.notifyItemChanged(position))
                .create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        adapter.notifyItemChanged(position);
    }
}
