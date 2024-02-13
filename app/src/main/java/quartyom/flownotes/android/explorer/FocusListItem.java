package quartyom.flownotes.android.explorer;

public class FocusListItem {
    public String initialTime;
    public long noteNumber;
    public String currentNoteText;

    public FocusListItem() {}

    public FocusListItem(String initialTime, long noteNumber, String currentNoteText) {
        this.initialTime = initialTime;
        this.noteNumber = noteNumber;
        this.currentNoteText = currentNoteText;
    }
}
