package quartyom.flownotes.android.main;

public class FlowListItem {
    public String flowName;
    public String flowPathName;
    public String lastNoteText;

    public FlowListItem(){}
    public FlowListItem(String flowName, String flowPathName, String lastNoteText) {
        this.flowName = flowName;
        this.flowPathName = flowPathName;
        this.lastNoteText = lastNoteText;
    }
}
