package quartyom.flownotes.core;

import java.util.List;

// Flow data model
// All fields are public, because it's all ruled via FLowManager
public class Flow {
    public String name;
    public long created;
    public long notesNumber;

    public Flow(){}
    public Flow(String name, long created, long notesNumber){
        this.name = name;
        this.created = created;
        this.notesNumber = notesNumber;
    }

}
