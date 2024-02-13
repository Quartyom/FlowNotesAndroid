package quartyom.flownotes.core;

import java.util.ArrayList;
import java.util.List;

// Manages all the flows
public abstract class FlowManager {
    public abstract boolean initialize();    // creates flows directory if not exists and reads data to dirsList
    public abstract String createFlow(String flowName);  // creates directory with data.json inside, updates flowsList, returns pathName or null
    public abstract boolean renameFlow(String pathName, String flowName);  // renames flow in data.json
    public abstract boolean deleteFlow(String pathName);  // deletes directory with its contents

    public abstract Flow loadFlow(String pathName);
    protected abstract boolean saveFlow(String pathName, Flow flow);
    public abstract List<String> getDirsList();
    public abstract void swapDirs(int i0, int i1);

}
