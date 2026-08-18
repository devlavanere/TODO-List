package br.com.zghero.ui;

/*
 * Autor: Michel Lavanere Sampaio
 * ZG-Hero Project - TODO List Backend MVP
 */

import br.com.zghero.model.Status;
import br.com.zghero.model.Task;
import br.com.zghero.service.TaskService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskService service = new TaskService();

        while(true) {
            System.out.println("\n------------------------------------------------");
            service.printDashBoard();
            System.out.println("------------------------------------------------");
            System.out.println("1. Adicionar Tarefa");
            System.out.println("2. Listar Todas as Tarefas");
            System.out.println("3. Filtrar Tarefas (Categoria, Prioridade, Status, Data)");
            System.out.println("4. Atualizar Status (Update)");
            System.out.println("5. Deletar Tarefa");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            String entrada = scanner.nextLine();
            int opcao;

            try {
                opcao = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                continue;
            }

            switch(opcao) {
                case 1:
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Descrição: ");
                    String desc = scanner.nextLine();
                    System.out.print("Data de Término (Formato YYYY-MM-DD): ");
                    LocalDate data = LocalDate.parse(scanner.nextLine());
                    System.out.print("Prioridade (1 a 5): ");
                    int prio = Integer.parseInt(scanner.nextLine());
                    System.out.print("Categoria: ");
                    String cat = scanner.nextLine();

                    service.addTask(new Task(nome, desc, data, prio, cat, Status.TODO));
                    System.out.println("Tarefa adicionada! Lista reordenada pela prioridade.");
                    break;
                case 2:
                    printList(service.getAllTasks());
                    break;
                case 3:
                    System.out.println("Filtrar por: 1-Categoria | 2-Prioridade | 3-Status | 4-Data");
                    String opFiltro = scanner.nextLine();
                    if (opFiltro.equals("1")) {
                        System.out.print("Categoria: ");
                        printList(service.filterByCategory(scanner.nextLine()));
                    } else if (opFiltro.equals("2")) {
                        System.out.print("Prioridade (1-5): ");
                        printList(service.filterByPriority(Integer.parseInt(scanner.nextLine())));
                    } else if (opFiltro.equals("3")) {
                        System.out.print("Status (TODO, DOING, DONE): ");
                        printList(service.filterByStatus(Status.valueOf(scanner.nextLine().toUpperCase())));
                    } else if (opFiltro.equals("4")) {
                        System.out.print("Data (YYYY-MM-DD): ");
                        printList(service.filterByDate(LocalDate.parse(scanner.nextLine())));
                    }
                    break;
                case 4:
                    System.out.print("Digite o ID da Tarefa: ");
                    String idUpdate = scanner.nextLine();
                    System.out.print("Novo Status (TODO, DOING, DONE): ");
                    Status novoStatus = Status.valueOf(scanner.nextLine().toUpperCase());
                    if (service.updateTaskStatus(idUpdate, novoStatus)) {
                        System.out.println("Tarefa atualizada!");
                    } else {
                        System.out.println("Tarefa não encontrada.");
                    }
                    break;

                case 5:
                    System.out.print("Digite o ID da Tarefa para deletar: ");
                    if(service.deleteTask(scanner.nextLine())){
                        System.out.println("Tarefa deletada!");
                    } else {
                        System.out.println("Tarefa não encontrada.");
                    }
                case 0:
                    System.out.println("Saindo e salvando dados... Até logo!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void printList(List<Task> list) {
        if (list.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada.");
            return;
        }
        for (Task t : list) {
            System.out.println(t.toString());
        }
    }
}
