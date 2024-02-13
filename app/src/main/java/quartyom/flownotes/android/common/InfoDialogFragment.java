package quartyom.flownotes.android.common;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import quartyom.flownotes.android.R;

public class InfoDialogFragment extends DialogFragment {
    public final int messageId;

    public InfoDialogFragment(int messageId) {
        this.messageId = messageId;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setMessage(messageId).setNeutralButton(R.string.ok, null).create();
    }
}
