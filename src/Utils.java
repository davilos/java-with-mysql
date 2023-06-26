import java.sql.*;
import java.util.Scanner;

public class Utils {
    static Scanner scanner = new Scanner(System.in);
    static Connection connection;

    public static void connect() {
        System.out.print("Digite o seu usuário: ");
        String user = scanner.nextLine();

        System.out.print("Informe a senha do seu usuário: ");
        String password = scanner.nextLine();

        String SERVER_URL = "jdbc:mysql://localhost:3306/jmysql?useSSL=false";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(SERVER_URL, user, password);
            menu();
        } catch (Exception e) {
            if (e instanceof ClassNotFoundException) {
                System.out.println("\nVerifique o driver de conexão.");
            } else {
                System.out.println("\nVerifique se o servidor está ativo!");
            }
            System.exit(-42);
            connection = null;
        }
    }

    public static void disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
            System.exit(0);
        }
    }

    public static void list() {
        try {
            String countQuery = "SELECT COUNT(*) AS count FROM produtos"; // Conta quantas linhas existem na tabela
            Statement statement = connection.createStatement();
            ResultSet resultSetCount = statement.executeQuery(countQuery);

            resultSetCount.next();

            if (resultSetCount.getInt("count") > 0) {
                String selectQuery = "SELECT * FROM produtos";
                ResultSet resultSet = statement.executeQuery(selectQuery);
                System.out.println("id | Nome         |       Preço | Estoque ");

                while (resultSet.next()) {
                    System.out.print(resultSet.getInt(1) + "   " + resultSet.getString(2) +
                            "       " + resultSet.getFloat(3) + "   " + resultSet.getInt(4) +
                            "\n");
                }
                resultSet.close();
            } else {
                System.out.println("\nNão existem produtos cadastrados!");
            }

            statement.close();
            resultSetCount.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("\nErro ao buscar produtos.");
            System.exit(-42);
        }
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
                case "5" -> {
                    try {
                        disconnect();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "m", "M", "" -> menu();
                default -> System.out.println("Opção inválida!");
            }
        }
    }
}
