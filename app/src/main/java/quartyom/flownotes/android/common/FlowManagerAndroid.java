package quartyom.flownotes.android.common;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import quartyom.flownotes.core.Flow;
import quartyom.flownotes.core.FlowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class FlowManagerAndroid extends FlowManager {
    private static final String EX_TAG = "FlowManagerAndroid";
    public final Gson gson = new Gson();
    private Random random = new Random();

    private static FlowManagerAndroid instance;

    public static FlowManagerAndroid getInstance(AppCompatActivity activity){
        if (instance == null) {
            instance = new FlowManagerAndroid(activity.getFilesDir());
        }
        return instance;
    }

    private FlowManagerAndroid(File filesDir) {
        flowsPath = filesDir + "/flows";
        filesPath = filesDir.getPath();
        if (!initialize()){
            Log.d(EX_TAG, "Couldn't initialize");
        }
    }

    private final String flowsPath;
    private final String filesPath;
    private List<String> dirsList;

    @Override
    public boolean initialize() {
        File directory = new File(flowsPath);

        if (directory.exists() || directory.mkdirs()) {
            return loadDirsList();
        }
        return false;
    }

    private String generateDirName(){
        return flowsPath + "/" + System.currentTimeMillis() + "_" + random.nextInt(1000);
    }

    @Override
    public String createFlow(String flowName) {
        String dirName;
        File dir;
        String flowPath;
        while (true) {      // getting a unique name of dir
            dirName = generateDirName();
            dir = new File(dirName);
            if (!dir.exists()) {
                flowPath = dirName + "/data.json";
                break;
            }
        }

        if (!dir.mkdirs()) {        // attempt to create a dir of flow
            return null;
        }

        Flow flow = new Flow(flowName, System.currentTimeMillis(), 0);

        if (!toJson(gson, flowPath, flow)) {
            return null;
        }

        dirsList.add(dirName);
        saveDirsList();
        return dirName;
    }

    @Override
    public boolean renameFlow(String pathName, String flowName) {
        String filename = pathName + "/data.json";
        Flow flow = fromJson(gson, filename, Flow.class);
        if (flow != null) {
            flow.name = flowName;
            return toJson(gson, filename, flow);
        }
        return false;
    }

    @Override
    public boolean deleteFlow(String pathName) {
        if (deleteFolder(new File(pathName))) {
            dirsList.remove(pathName);
            saveDirsList();
            return true;
        }
        return false;
    }

    @Override
    public Flow loadFlow(String pathName) {
        String filename = pathName + "/data.json";
        return fromJson(gson, filename, Flow.class);
    }

    @Override
    public boolean saveFlow(String pathName, Flow flow) {
        String filename = pathName + "/data.json";
        return toJson(gson, filename, flow);
    }

    @Override
    public List<String> getDirsList() {
        return dirsList;
    }

    @Override
    public void swapDirs(int i0, int i1) {
        Collections.swap(dirsList, i0, i1);
        saveDirsList();
    }

    private void saveDirsList() {
        String filename = filesPath + "/contents.json";
        toJson(gson, filename, dirsList);
    }

    private boolean loadDirsList() {
        if (dirsList == null) { dirsList = new ArrayList<>(); }
        else { dirsList.clear(); }

        String filename = filesPath + "/contents.json";
        List<String> contents = fromJson(gson, filename, dirsList.getClass());
        if (contents == null) { contents = repairContents(); }

        String[] files = new File(flowsPath).list();
        if (files == null) { return false; }

        HashSet<String> fileSystemSet = new HashSet<>();
        for (String name: files) { fileSystemSet.add(flowsPath + "/" + name); }

        for (String name : contents) {      // loads if flow from contents exists in file system
            if (fileSystemSet.contains(name)) {
                dirsList.add(name);
                fileSystemSet.remove(name);
            }
            else {
                Log.d(EX_TAG, name + " is absent in file system");
            }
        }
        dirsList.addAll(fileSystemSet);
        return true;
    }

    public List<String> repairContents() {      // perhaps, it's an unnecessary method
        String[] files = new File(flowsPath).list();
        List<String> contents = new ArrayList<>();
        if (files != null) {
            for (String name : files) {
                String pathName = flowsPath + "/" + name;
                if (loadFlow(pathName) != null) {
                    contents.add(pathName);
                }
            }
        }
        return contents;
    }

    public static boolean toJson(Gson gson, String filename, Object object) {
        return toFile(filename, gson.toJson(object));
    }

    public static <T> T fromJson(Gson gson, String filename, Class<T> classOfT) {
        try(FileInputStream fis = new FileInputStream(filename);
            InputStreamReader sr = new InputStreamReader(fis)) {
            return gson.fromJson(sr, classOfT);
        } catch (IOException e) {
            Log.d(EX_TAG, e.getMessage());
            return null;
        }
    }

    public static boolean toFile(String filename, String data){
        if (data == null) { return true; }
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(data.getBytes());
            return true;
        }
        catch (IOException e) {
            Log.d(EX_TAG, e.getMessage());
            return false;
        }
    }

    public static String fromFile(String filename) {
        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        catch (IOException e) {
            Log.d(EX_TAG, e.getMessage());
            return null;
        }

        return content.toString();
    }

    public static boolean createDirs(String dirPath) {
        File directory = new File(dirPath);
        return directory.exists() || directory.mkdirs();
    }

    public static boolean deleteFolder(File folder) {
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteFolder(file);
                    } else {
                        file.delete();
                    }
                }
            }
            return folder.delete();
        }
        else {
            return false;
        }
    }

    public static boolean isEmptyString(String s) {
        return s == null || s.trim().equals("");
    }


}
