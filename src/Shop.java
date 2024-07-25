import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Shop {
    private Set<Notebook> storage = new HashSet<>();
    private Map<Filter, String> filters = new HashMap<>();

    public static void main(String[] args) {
        Shop shop = new Shop();
        shop.menu();
    }

    private void menu() {
        boolean run = true;
        Scanner scanner = new Scanner(System.in);
        while (run) {
            System.out.println("""
                                        
                        Выберите пункт меню:
                    1 - Добавить ноутбук
                    2 - Вывести список
                    3 - Добавить фильтр
                    4 - Удалить все фильтры
                    0 - Выход
                    """);
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> addNotebook();
                    case 2 -> show();
                    case 3 -> setFilter();
                    case 4 -> clearFilters();
                    case 0 -> run = false;
                    default -> System.out.println("Неверный выбор, повторите ввод.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный выбор, повторите ввод.");
            }
        }
    }

    private void addNotebook() {
        String name;
        int ram;
        int hdd;
        String os;
        String color;

        try {
            name = inputString("Введите название ноутбука");
            ram = inputDigit("Введите объем ОЗУ");
            hdd = inputDigit("Введите объем HDD");
            os = inputString("Введите название операционной системы");
            color = inputString("Введите цвет ноутбука");
        } catch (AbortInputException e) {
            return;
        }
        Notebook newNotebook = new Notebook(name, ram, hdd, os, color);
        storage.add(newNotebook);
    }

    private void show() {
        List<Notebook> printList = new ArrayList<>(storage);
        for (Map.Entry<Filter, String> filter : filters.entrySet()) {
            switch (filter.getKey()) {
                case RAM -> printList.removeIf(n -> n.getRam() < Integer.parseInt(filter.getValue()));
                case HDD -> printList.removeIf(n -> n.getHdd() < Integer.parseInt(filter.getValue()));
                case OS -> printList.removeIf(n -> !n.getOs().equalsIgnoreCase(filter.getValue()));
                case COLOR -> printList.removeIf(n -> !n.getColor().equalsIgnoreCase(filter.getValue()));
            }
        }
        System.out.println("Ноутбуки, подходящие под условия:");
        printList.forEach(System.out::println);
    }

    private void setFilter() {
        System.out.println("Выберите фильтр:");
        Filter[] filterList = Filter.values();
        for (int i = 0; i < filterList.length; i++) {
            System.out.println(i + " - " + filterList[i]);
        }
        System.out.println("Другой ввод - Выход");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        try {
            switch (input) {
                case "0" -> filters.put(filterList[0], Integer.toString(inputDigit("Введите минимальный объем ОЗУ")));
                case "1" -> filters.put(filterList[1], Integer.toString(inputDigit("Введите минимальный объем HDD")));
                case "2" -> filters.put(filterList[2], inputString("Введите название операционной системы"));
                case "3" -> filters.put(filterList[3], inputString("Введите цвет ноутбука"));
            }
        } catch (AbortInputException e) {
            return;
        }
    }

    private void clearFilters() {
        filters.clear();
        System.out.println("Фильтры сброшены.");
    }

    private int inputDigit(String message) {
        while (true) {
            String input = inputString(message);
            try {
                int data = Integer.parseInt(input);
                if (data <= 0) throw new NumberFormatException();
                return data;
            } catch (NumberFormatException e) {
                System.out.println("Недопустимые данные, повторите ввод.");
            }
        }
    }

    private String inputString(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message + " (пустая строка для выхода): ");
        String input = scanner.nextLine();
        if (input.isEmpty()) throw new AbortInputException();
        return input;
    }
}