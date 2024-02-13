package quartyom.flownotes.android.common;

import com.google.gson.Gson;

import java.io.File;
import java.text.DateFormat;

import quartyom.flownotes.core.FlowManager;
import quartyom.flownotes.core.Focus;

public class FocusAndroid extends Focus {
    private static final String EX_TAG = "FocusAndroid";
    public final Gson gson;
    public FocusAndroid(FlowManagerAndroid flowManager, String flowPathName){
        super(flowManager, flowPathName);
        this.gson = flowManager.gson;
        initialize();
    }

    @Override
    public void initialize() {
        if (flow.notesNumber <= 0) {
            newNote();
        }
        else {
            setNewest();
        }
    }

    @Override
    public boolean newNote() {
        flow.notesNumber++;                 // now we have 1 more note in the flow
        saveFlow(flowPathName, flow);
        setNewest();
        return true;
    }

    @Override
    public void saveNote() {
        String prefix = flowPathName + "/" + currentNoteNumber;

        if (FlowManagerAndroid.isEmptyString(getInitialNoteText())) {   // if first save then initial
            currentNote.initialText = currentNote.currentText;
            FlowManagerAndroid.toFile(prefix + "i.txt", currentNote.initialText);
        }
        else {                                                          // else saved
            currentNote.savedText = currentNote.currentText;
            FlowManagerAndroid.toFile(prefix + "s.txt", currentNote.savedText);
        }
        saveCurrentNote();
    }

    @Override
    public void saveCurrentNote() {
        String prefix = flowPathName + "/" + currentNoteNumber;
        FlowManagerAndroid.toFile(prefix + "c.txt", currentNote.currentText);
    }

    @Override
    public String getInitialNoteText() {
        if (currentNote.initialText == null) {
            String prefix = flowPathName + "/" + currentNoteNumber;
            currentNote.initialText = FlowManagerAndroid.fromFile(prefix + "i.txt");
        }
        return currentNote.initialText;
    }

    @Override
    public String getSavedNoteText() {
        if (currentNote.savedText == null) {
            String prefix = flowPathName + "/" + currentNoteNumber;
            currentNote.savedText = FlowManagerAndroid.fromFile(prefix + "s.txt");
            if (FlowManagerAndroid.isEmptyString(currentNote.savedText)) {
                currentNote.savedText = getInitialNoteText();
            }
        }
        return currentNote.savedText;
    }

    @Override
    public String getCurrentNoteText() {
        if (currentNote.currentText == null) {
            String prefix = flowPathName + "/" + currentNoteNumber;
            currentNote.currentText = FlowManagerAndroid.fromFile(prefix + "c.txt");
        }
        return currentNote.currentText;
    }

    DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);


    @Override
    public String getInitialTime() {
        File file = new File(flowPathName + "/" + currentNoteNumber + "i.txt");
        if (file.exists()) {
            return dateFormat.format(file.lastModified());
        }
        else {
           return "";
        }
    }

    @Override
    public void clearNote() {
        currentNote.initialText = null;
        currentNote.savedText = null;
        currentNote.currentText = null;
    }


}
