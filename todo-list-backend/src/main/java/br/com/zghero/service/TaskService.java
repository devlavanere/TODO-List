package br.com.zghero.service;

import br.com.zghero.model.Status;
import br.com.zghero.model.Task;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaskService {
    private List<Task> tasks = new ArrayList<>();
    private static final String FILE_NAME = "tasks.csv";

    public TaskService() {
        loadFromFile();
    }

    // Create
    public void addTask(Task task) {
        tasks.add(task);

        // Ordenação
        tasks.sort(new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                return Integer.compare(t1.getPrioridade(), t2.getPrioridade());
            }
        });

        saveToFile();
    }

    // READ
    public List<Task> getAllTasks() {
        return tasks;
    }

    // UPDATE
    public boolean updateTaskStatus(String id, Status newStatus) {
        for(Task t : tasks) {
            if(t.getId().equalsIgnoreCase(id)){
                t.setStatus(newStatus);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    // DELETE
    public boolean deleteTask(String id) {
        for(int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            if(t.getId().equalsIgnoreCase(id)){
                tasks.remove(i);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    // Filtros

    public List<Task> filterByCategory(String categoria) {
        List<Task> listaFiltrada = new ArrayList<>();
        for(Task t : tasks){
            if(t.getCategoria().equalsIgnoreCase(categoria)) {
                listaFiltrada.add(t);
            }
        }
        return listaFiltrada;
    }

    public List<Task> filterByPriority(int prioridade) {
        List<Task> listaFiltrada = new ArrayList<>();
        for(Task t : tasks) {
            if(t.getPrioridade() == prioridade) {
                listaFiltrada.add(t);
            }
        }
        return listaFiltrada;
    }

    public List<Task> filterByStatus(Status status) {
        List<Task> listaFiltrada = new ArrayList<>();
        for(Task t : tasks) {
            if(t.getStatus() == status) {
                listaFiltrada.add(t);
            }
        }
        return listaFiltrada;
    }

    public List<Task> filterByDate(LocalDate dataTermino) {
        List<Task> listaFiltrada = new ArrayList<>();
        for(Task t : tasks) {
            if(t.getDataTermino() == dataTermino) {
                listaFiltrada.add(t);
            }
        }
        return listaFiltrada;
    }

    // DASHBOARD (ESTATÍSTICAS)
    public void printDashBoard() {
        int contTodo = 0;
        int contDoing = 0;
        int contDone = 0;

        // Vare a lista de tarefas e, dependendo do status, adiciona +1 no contador correspondente
        for(Task t : tasks) {
            if(t.getStatus() == Status.TODO) {
                contTodo++;
            } else if (t.getStatus() == Status.DOING) {
                contDoing++;
            } else if(t.getStatus() == Status.DONE){
                contDone++;
            }
        }
        System.out.println("=== ESTATÍSTICAS: TODO (" + contTodo + ") | DOING (" + contDoing + ") | DONE (" + contDone + ") ===");
    }

    // Salvar no arquivo
    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Task t : tasks) {
                writer.println(t.getId() + ";" + t.getNome() + ";" + t.getDescricao() + ";" +
                        t.getDataTermino() + ";" + t.getPrioridade() + ";" + t.getCategoria() + ";" + t.getStatus());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados.");
        }
    }

    // Carregar do arquivo
    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] d = line.split(";");
                if (d.length == 7) {
                    tasks.add(new Task(d[0], d[1], d[2], LocalDate.parse(d[3]), Integer.parseInt(d[4]), d[5], Status.valueOf(d[6])));
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar dados salvos.");
        }
    }
}
