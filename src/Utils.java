import java.util.Scanner;

public class Utils {
    static Scanner scanner = new Scanner(System.in);

    public static void list() {
        System.out.println("Listando produtos...");
    }

    public static void insert() {
        System.out.println("Inserindo produtos...");
    }

    public static void update() {
        System.out.println("Atualizando produtos...");
    }

    public static void delete() {
        System.out.println("Deletando produtos...");
    }

    public static void menu() {
        System.out.println("==================Gerenciamento de Produtos===============");
        System.out.println("Selecione uma opção: ");
        System.out.println("1 - Listar produtos.");
        System.out.println("2 - Inserir produtos.");
        System.out.println("3 - Atualizar produtos.");
        System.out.println("4 - Deletar produtos.");
        System.out.println("5 - Sair do sistema.");
        System.out.println("Para listar o menu novamente digite 'm' ou pressione 'enter'");

        while (true) {
            System.out.print("\n-> ");
            switch (scanner.nextLine()) {
                case "1" -> list();
                case "2" -> insert();
                case "3" -> update();
                case "4" -> delete();
                case "5" -> System.exit(0);
                case "m", "M", "" -> menu();
                default -> System.out.println("Opção inválida!");
            }
        }
    }
}
