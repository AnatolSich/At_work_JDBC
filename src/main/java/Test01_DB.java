import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public class Test01_DB {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String LOGIN = "postgres";
    private static final String PASSWORD = "slonyara";

    public static void main(String[] args) {
        //по объявлению  Driver driver = new Driver();
        // нужный для БД драйвер подтягивается автоматически

        Driver driver = new org.postgresql.Driver();

        try {
            //регистрируем драйвер в DriverManager и создаем Connection
            DriverManager.registerDriver(driver);
            Connection connect = DriverManager.getConnection(URL, LOGIN, PASSWORD);


            //читаем и выполняем файл-скрипт .sql по-строчно
            PreparedStatement pr_statement;
            ResultSet resultSet;

            String str = ""; //строка изначально должна инициализироваться не NULL
            Scanner scanner = null;
            try {
                scanner = new Scanner(new File("D:/Work/Workspace/JDBC_ttt/src/main/resources/initDB.sql"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (scanner.hasNext())
                str += scanner.nextLine() + "\r\n";//считываем скрипт в переменную
            scanner.close();

            pr_statement = connect.prepareStatement(str);
            pr_statement.execute();
            System.out.println("Table creation is done successfully!");


            //Объявляем Statement для заполнения таблицы
            Statement stat = connect.createStatement();
            //Наполняем таблицу с помощью одиночного запроса
            stat.execute("INSERT INTO users(name, age, email) VALUES ('Борисов Геннадий Иннокентьевич', 1950, 'aborigen@gmail.com' )");

            //Наполняем таблицу с помощью пакета запросов
            stat.addBatch("INSERT INTO users(name, age, email) VALUES ('Константинов Игорь Васильевич', 1959, 'konst1959@ukr.net' )");
            stat.addBatch("INSERT INTO users(name, age, email) VALUES ('Куракса Надежда Васильевна', 1951, 'nad-kur5685@ukr.net' )");
            stat.addBatch("INSERT INTO users(name, age, email) VALUES ('Вирченко Вадим Геннадиевич', 1986, 'virch-vadim@gmail.com' )");
            stat.addBatch("INSERT INTO users(name, age, email) VALUES ('Билык Алексей Викторович', 1979, 'bilyk1979alex@mail.ru' )");
            stat.addBatch("INSERT INTO users(name, age, email) VALUES ('Козлов Сергей Александрович', 1984, 'kozlov_s1984@yandex.ru' )");
            stat.addBatch("INSERT INTO users(name, age, email) VALUES ('Шкоденко Владимир Петрович', 1978, 'shkod_vova1978@rambler.ru' )");

            stat.executeBatch();
            stat.clearBatch();

            //Вывод таблицы в консоль
            ResultSet result = stat.executeQuery("SELECT * FROM users");
            while (result.next()) {
                System.out.printf("%6s%35s%6s%30s%5s%n",
                        result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4),
                        result.getString(5));
            }
            System.out.println("------------------------------------------------------------------");

            /*PreparedStatement pr_stat = connect.prepareStatement("DELETE FROM ? WHERE email LIKE '%mail.ru';" +
                    "UPDATE ? SET email = replace(email, 'rambler.ru', 'ukr.net');" +
                    "UPDATE ? SET email = replace(email, 'yandex.ru', 'meta.ua');");*/
           /* PreparedStatement pr_stat = connect.prepareStatement("DELETE FROM users WHERE email LIKE '%'||?;" +
                    "UPDATE users SET email = replace(email, ?, 'ukr.net');" +
                    "UPDATE users SET email = replace(email, ?, 'meta.ua');");*/

            PreparedStatement pr_stat = connect.prepareStatement("DELETE FROM users WHERE email LIKE '%'||?;");

            pr_stat.setString(1, "mail.ru");
            pr_stat.execute();
            pr_stat.setString(1, "rambler.ru");
            pr_stat.execute();
            pr_stat.setString(1, "yandex.ru");
            // pr_stat.setString(2,"rambler.ru");
            // pr_stat.setString(3,"yandex.ru");
            pr_stat.execute();

            //Вывод таблицы в консоль
            ResultSet resultFinish = stat.executeQuery("SELECT * FROM users");
            while (resultFinish.next()) {
                System.out.printf("%6s%35s%6s%30s%5s%n",
                        resultFinish.getInt(1),
                        resultFinish.getString(2),
                        resultFinish.getString(3),
                        resultFinish.getString(4),
                        resultFinish.getString(5));
            }
            System.out.println("------------------------------------------------------------------");

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
