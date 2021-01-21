package com.company.Model;


import com.company.View.Main;

import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.SortedMap;

public class TaskManagerModel {
    private static Logger log = Logger.getLogger(TaskManagerModel.class.getName());
    private AbstractTaskList taskList;
    private SortedMap<LocalDateTime, Set<Task>> calendar;
    private File dataFile;

   public TaskManagerModel() {
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

    public AbstractTaskList getTaskList() {
        return taskList;
    }

    public SortedMap<LocalDateTime, Set<Task>> getCalendar() {
        return calendar;
    }

    public void setCalendar(SortedMap<LocalDateTime, Set<Task>> calendar) {
        this.calendar = calendar;
    }



    public void readTaskListFromRes() throws IOException {
        TaskIO.readText(taskList, dataFile);
    }

    public void writeTaskListFromRes() throws IOException {
        TaskIO.writeText(taskList, dataFile);
    }

    public Task createTask() {
        Task createdTask = null;
        String title;
        LocalDateTime start;
        LocalDateTime end;
        boolean isRepeated;
        int interval;
        boolean active;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Введіть назву: ");
            title = reader.readLine();
            System.out.print("Завдання з інтервалом чи ні? (true or false): ");
            isRepeated = Boolean.parseBoolean(reader.readLine());
            if (!isRepeated) {
                System.out.print("Введіть час початку (dd-MM-yyyy HH:mm): ");
                start = LocalDateTime.parse(reader.readLine(), formatter);
                createdTask = new Task(title, start);
            } else {
                System.out.print("Початковий час (dd-MM-yyyy HH:mm): ");
                start = LocalDateTime.parse(reader.readLine(), formatter);
                System.out.print("Кінцевий час (dd-MM-yyyy HH:mm): ");
                end = LocalDateTime.parse(reader.readLine(), formatter);
                System.out.print("Введіть інтервал (сек): ");
                interval = Integer.parseInt(reader.readLine());
                createdTask = new Task(title, start, end, interval);
            }
            System.out.print("Завдання активне? (true or false): ");
            active = Boolean.parseBoolean(reader.readLine());
            createdTask.setActive(active);
        } catch (Exception e) {
            System.out.print("Введені дані не вірні. Спробувати ще раз?(true or false): ");
            Main.logger.error("Incorrect data");
            boolean answer;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                answer = Boolean.parseBoolean(reader.readLine());
            } catch (Exception ex) {
                return null;
            }

            if(answer) {
                return createTask();
            } else {
                return null;
            }
        }
        return createdTask;
    }

    public void addToTaskList(Task task) throws IOException {
        if(task == null)
            return;

        taskList.add(task);
        writeTaskListFromRes();
    }

    public void changeTask(int index) throws IOException {
        Task changedTask = createTask();
        if(changedTask == null)
            return;
        taskList.setTask(index, changedTask);
        writeTaskListFromRes();
    }

    public void removeTask(int index) throws IOException {
        taskList.remove(taskList.getTask(index));
        writeTaskListFromRes();
    }
}
