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

                System.out.print("\n--------------------------------------------------");
                System.out.printf("%n%-" + 5 + "s %-" + 20 + "s %-" + 15 + "s %-" + 5 + "s%n",
                        "id", "Nome", "Preço", "Estoque");
                System.out.println("--------------------------------------------------");

                while (resultSet.next()) {
                    System.out.printf("%-" + 5 + "d %-" + 20 + "s %-" + 15 + ".2f %-" + 5 + "d%n",
                            resultSet.getInt(1), resultSet.getString(2),
                            resultSet.getFloat(3), resultSet.getInt(4));
                }
                System.out.println("--------------------------------------------------");
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
        System.out.print("\nDigite o nome do produto: ");
        String name = scanner.nextLine();

        System.out.print("\nDigite o preço do produto: R$");
        float price = scanner.nextFloat();

        System.out.print("\nDigite a quantidade em estoque: ");
        int number = scanner.nextInt();

        scanner.nextLine(); // Consume a quebra de linha, para evitar que o menu apareça sozinho.

        String insertQuery = "INSERT INTO produtos (nome, preco, estoque) VALUES (?, ?, ?)"; // Prevenindo o SQL Injection

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, name);
            preparedStatement.setFloat(2, price);
            preparedStatement.setInt(3, number);

            preparedStatement.executeUpdate();
            preparedStatement.close();

            System.out.printf("%nO produto '%s' foi inserido com sucesso!%n", name);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("\nErro ao salvar produto.");
            System.exit(-42);
        }
    }

    public static void update() {
        System.out.println("Atualizando produtos...");
    }

    public static void delete() {
        System.out.println("Deletando produtos...");
    }

    public static void menu() {
        System.out.println("\n==================Gerenciamento de Produtos===============");
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
