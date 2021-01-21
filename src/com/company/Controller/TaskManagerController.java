package com.company.Controller;

import com.company.Model.Task;
import com.company.Model.TaskManagerModel;
import com.company.Model.Tasks;
import com.company.View.Main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.SortedMap;


public class TaskManagerController {

    private TaskManagerModel model;

   public TaskManagerController(TaskManagerModel model) {
        this.model = model;
    }

    public void menu() throws IOException {

        int answer;
        boolean isWork = true;
        do{
            System.out.println("*************TASK MANAGER******************");
            System.out.println("1. Створити нове завдання.");
            System.out.println("2. Переглянути активні завдання.");
            System.out.println("3. Видалити завадняя.");
            System.out.println("4. Змінити завдання.");
            System.out.println("5. Переглянути календар.");
            System.out.println("6. Вихід.");
            System.out.print("Виберіть дію 1-6:");
            System.out.println("\n*****************************************");
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                answer = Integer.parseInt(reader.readLine());
            } catch (Exception e) {
                System.out.println("Ви ввели не коректне значення, спробуйте ще раз.");
                Main.logger.error("Incorrect value ");
                continue;
            }

            if (answer < 1 || answer > 6) {
                System.out.println("Не коректна дія.");
                Main.logger.info("Incorrect action");
                continue;
            }
            System.out.println("**********************************\n");
            isWork = doAction(answer);
        }while(isWork);
    }

    public boolean doAction(int action) throws IOException {
        switch (action) {
            case 1:
                model.addToTaskList(model.createTask());
                break;
            case 2:
                showTaskList();
                break;
            case 3:
                removeTaskAction();
                break;
            case 4:
                changeTaskAction();
                break;
            case 5:
                showCalendar();
                break;
            case 6:
                return false;
        }
        return true;
    }

    private void changeTaskAction() throws IOException {

        if (model.getTaskList().size() == 0) {
            System.out.println("Нічого змінювати :(");
            Main.logger.info("There is nothing to change");
        } else {
            showTaskList();
            int index= -1;
            try {
                System.out.print("Виберіть номер завдання (1-" + model.getTaskList().size() + "):");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                index = Integer.parseInt(reader.readLine()) - 1;
            } catch (Exception e) {
                System.out.print("Введені дані не вірні. Спробувати ще раз? (true or false): ");
                Main.logger.info("Incorrect data");
                boolean answer;
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    answer = Boolean.parseBoolean(reader.readLine());
                } catch (Exception ex) {
                    return;
                }

                if(answer) {
                    changeTaskAction();
                } else {
                    return;
                }
            }

            if (index >= 0 && index < model.getTaskList().size()) {
                System.out.println("Старе завдання: "+model.getTaskList().getTask(index).showTask());
                model.changeTask(index);
                System.out.println("Завдання успішно оновлене");
                Main.logger.info("Task has been update successfully");
                System.out.println("Список після зміни:");
                showTaskList();
            } else {
                System.out.print("Такого номеру немає! Спробуйте ще раз.");
                Main.logger.info("No suck number");
                changeTaskAction();
            }

        }
    }

    private void removeTaskAction() throws IOException {
        if(model.getTaskList().size() == 0) {
            System.out.println("Немає завдання для видалення");
            Main.logger.info("No such task to delete");
        } else {
            System.out.println("Список після видалення: ");
            showTaskList();
            int index = -1;
            try {
                System.out.print("Виберіть номер завдання (1-" + model.getTaskList().size() + "):");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                index = Integer.parseInt(reader.readLine()) - 1;
            } catch (Exception e) {
                System.out.print("Введені дані не вірні. Спробувати ще раз? (true or false): ");
                Main.logger.error("Incorrect data");
                boolean answer;
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    answer = Boolean.parseBoolean(reader.readLine());
                } catch (Exception ex) {
                    return;
                }

                if(answer) {
                    removeTaskAction();
                } else {
                    return;
                }
            }

            if (index >= 0 && index < model.getTaskList().size()) {
                model.removeTask(index);
                System.out.println("Завдання успішно видалене");
                Main.logger.info("Task was successfully deleted");
                System.out.println("Список після видалення: ");
                showTaskList();
            } else {
                System.out.print("Ви ввели не коректний номер! Спробуйте ще раз");
                Main.logger.info("Incorrect number");
                removeTaskAction();
            }
        }
    }

    private void showTaskList() {
        int i =1;
        for (Task task: model.getTaskList()) {
            System.out.println(("№"+(i++)+"Task:")+task.showTask());
        }
    }

    private  void showCalendar() throws IOException {
        LocalDateTime start;
        LocalDateTime end;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        try {
            System.out.println("Введіть період часу:");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Початковий час календаря (dd-MM-yyyy HH:mm): ");
            start = LocalDateTime.parse(reader.readLine(), formatter);
            System.out.print("Кінечний час календаря (dd-MM-yyyy HH:mm): ");
            end = LocalDateTime.parse(reader.readLine(), formatter);
            model.setCalendar(Tasks.calendar(model.getTaskList(), start, end));
        } catch (Exception e) {
            System.out.print("Введені дані не вірні. Спробувати ще раз? (true or false): ");
            Main.logger.error("Incorrect data");
            boolean answer;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                answer = Boolean.parseBoolean(reader.readLine());
            } catch (Exception ex) {
                return;
            }

            if(answer) {
                showCalendar();
            } else {
                return;
            }
        }

        System.out.println("Календарь:");
        System.out.println("**********************************\n");
        for (SortedMap.Entry<LocalDateTime, Set<Task>> entry: model.getCalendar().entrySet()) {
            System.out.println("Час: "+entry.getKey());
            for (Task task: entry.getValue()) {
                System.out.println("Назва: "+ task.getTitle());
            }
        }
        System.out.println("**********************************\n");
    }
}
