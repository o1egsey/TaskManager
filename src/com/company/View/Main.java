package com.company.View;

import com.company.Controller.TaskManagerController;
import com.company.Model.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.SortedMap;

public class Main {
    private AbstractTaskList taskList;
    private SortedMap<LocalDateTime, Set<Task>> calendar;
    private File dataFile;

    public static Logger logger = Logger.getLogger(Main.class);

    public Main() {
        dataFile = new File(System.getProperty("user.dir") + "\\info.txt");
        taskList = new ArrayTaskList();
        try {
            if (!dataFile.exists()) {
                dataFile.createNewFile();
            } else {
                readTaskListFromRes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        calendar = null;
    }

    public void readTaskListFromRes() throws IOException {
        TaskIO.readText(taskList, dataFile);
    }

    public void writeTaskListFromRes() throws IOException {
        TaskIO.writeText(taskList, dataFile);
    }

    public static void main(String[] args) {
        TaskManagerModel model = new TaskManagerModel();
        TaskManagerController controller = new TaskManagerController(model);
        try {
            controller.menu();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

