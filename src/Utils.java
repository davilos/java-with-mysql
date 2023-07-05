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
        System.out.print("\nDigite o código do produto: ");
        int id = Integer.parseInt(scanner.nextLine());

        String searchQuery = "SELECT * FROM produtos WHERE id=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(searchQuery,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.last();
            int row = resultSet.getRow(); // Retorna o número da última linha (0 se não exista linhas).

            resultSet.beforeFirst();

            if (row > 0) {
                System.out.print("\nDigite o nome do produto: ");
                String name = scanner.nextLine();

                System.out.print("\nDigite o preço do produto: R$");
                float price = scanner.nextFloat();

                System.out.print("\nDigite a quantidade em estoque: ");
                int number = scanner.nextInt();

                scanner.nextLine(); // Consume a quebra de linha, para evitar que o menu apareça sozinho.

                String updateQuery = "UPDATE produtos SET nome=?, preco=?, estoque=? WHERE id=?";

                PreparedStatement preparedStatement1 = connection.prepareStatement(updateQuery);

                preparedStatement1.setString(1, name);
                preparedStatement1.setFloat(2, price);
                preparedStatement1.setInt(3, number);
                preparedStatement1.setInt(4, id);

                preparedStatement1.executeUpdate();

                preparedStatement.close();
                preparedStatement1.close();

                System.out.printf("%nO produto '%s' foi atualizado com sucesso!%n", name);
            } else {
                System.out.println("Não existe produto com o id informado!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao atualizar produto.");
            System.exit(-42);
        }
    }

    public static void delete() {
        String deleteQuery = "DELETE FROM produtos WHERE id=?";
        String countQuery = "SELECT COUNT(*) AS count FROM produtos WHERE id=?";

        System.out.print("\nDigite o código do produto: ");
        int id = Integer.parseInt(scanner.nextLine());

        try {
            PreparedStatement countStatement = connection.prepareStatement(countQuery);
            countStatement.setInt(1, id);

            ResultSet countSet = countStatement.executeQuery();

            countSet.next();

            if (countSet.getInt("count") > 0) {
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, id);

                deleteStatement.executeUpdate();

                System.out.printf("%nO produto foi removido com sucesso!%n");

                countStatement.close();
                deleteStatement.close();
                countSet.close();
            } else {
                System.out.println("Não existe produto com o id informado!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao deletar produto.");
            System.exit(-42);
        }
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
