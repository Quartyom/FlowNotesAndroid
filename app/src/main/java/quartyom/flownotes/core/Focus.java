package quartyom.flownotes.core;

public abstract class Focus {
    public final FlowManager flowManager;

    public Focus(FlowManager flowManager, String flowPathName){
        this.flowManager = flowManager;
        this.flowPathName = flowPathName;
        this.flow = flowManager.loadFlow(flowPathName);
        currentNote = new Note();
    }

    public final String flowPathName;
    protected final Flow flow;
    protected long currentNoteNumber;
    protected Note currentNote;

    public long getFlowNotesNumber() {
        return flow.notesNumber;
    }

    public boolean isSingleNote() {
        return flow.notesNumber == 1;
    }

    public boolean isNewest(){
        return currentNoteNumber + 1 == flow.notesNumber;
    }

    public boolean isOldest(){
        return currentNoteNumber == 0;
    }

    public abstract void clearNote();
    public void numberUp(){
        if (currentNoteNumber + 1 < flow.notesNumber) {
            currentNoteNumber++;
            clearNote();
        }
    }

    public void numberDown(){
        if (currentNoteNumber > 0) {
            currentNoteNumber--;
            clearNote();
        }
    }

    public void setNumber(long noteNumber){
        if (0 <= noteNumber && noteNumber < flow.notesNumber && noteNumber != currentNoteNumber) {
            currentNoteNumber = noteNumber;
            clearNote();
        }
    }

    public long getNumber(){
        return currentNoteNumber;
    }

    public void setOldest(){
        if (flow.notesNumber > 0) {
            currentNoteNumber = 0;
            clearNote();
        }
    }
    public void setNewest(){
        if (flow.notesNumber > 0) {
            currentNoteNumber = flow.notesNumber - 1;
            clearNote();
        }
    }

    public String getFlowName(){
        return flow.name;
    }

    public abstract void initialize(); // creates the first note if not created
    public abstract boolean newNote();
    public abstract void saveNote();    // saves note, if not initial, saves as initial
    public abstract void saveCurrentNote();     // saves current note

    public abstract String getInitialNoteText();    // loads if necessary
    public abstract String getSavedNoteText();
    public abstract String getCurrentNoteText();
    public abstract String getInitialTime();
    public void setCurrentNoteText(String text) { currentNote.currentText = text; }

    protected boolean saveFlow(String pathName, Flow flow) {
        return flowManager.saveFlow(pathName, flow);
    }
}
