package quartyom.flownotes.android.common;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.File;

import quartyom.flownotes.android.R;
import quartyom.flownotes.android.main.DeleteFlowDialogFragment;
import quartyom.flownotes.android.main.FlowsListAdapter;

public class GuideManager {
    private static Gson gson;
    private static String filePath;
    private static Guide guide;

    public static void initialize(Gson gson, File filesDir) {
        GuideManager.gson = gson;
        GuideManager.filePath = filesDir + "/guide.json";
        GuideManager.guide = FlowManagerAndroid.fromJson(gson, filePath, Guide.class);
        if (GuideManager.guide == null) {
            guide = new Guide();
        }
    }

    private static void save() {
        FlowManagerAndroid.toJson(gson, filePath, guide);
    }

    public static void reset() {
        guide = new Guide();
        save();
    }

    public static void tryInMenu(AppCompatActivity activity) {
        if (!guide.mainMenu) {
            InfoDialogFragment dialogFragment = new InfoDialogFragment(R.string.guideMenuText);
            dialogFragment.show(activity.getSupportFragmentManager(), activity.getLocalClassName());

            guide.mainMenu = true;
            save();
        }
    }

    public static void tryInFocus(AppCompatActivity activity) {
        if (!guide.focus) {
            InfoDialogFragment dialogFragment = new InfoDialogFragment(R.string.guideFocusText);
            dialogFragment.show(activity.getSupportFragmentManager(), activity.getLocalClassName());

            guide.focus = true;
            save();
        }
    }

    public static void tryInExplorer(AppCompatActivity activity) {
        if (!guide.explorer) {
            InfoDialogFragment dialogFragment = new InfoDialogFragment(R.string.guideExplorerText);
            dialogFragment.show(activity.getSupportFragmentManager(), activity.getLocalClassName());

            guide.explorer = true;
            save();
        }
    }



}
