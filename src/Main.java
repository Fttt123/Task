import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Main {
    static InputStreamReader inputStreamReader = null;
    static BufferedReader bufferedReader = null;
    static URL url = null;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите запрос: ");
        url = new URL("https://ru.wikipedia.org/w/api.php?action=query&list=search&utf8=&format=json&srsearch=\"" + ScannerString(scanner) + "\"" );
        MakeRequest();
        if (inputStreamReader != null) {
            inputStreamReader.close();
        }
        if (bufferedReader != null) {
            bufferedReader.close();
        }
    }

    private static void MakeRequest() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
            inputStreamReader = new InputStreamReader(connection.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            String bufferLine = "";
            boolean lineCheck = false, read = false;
            for (int i = 0; i < line.length() - 13; i++) {
                if ((line.substring(i, i + 11)).equals("\"snippet\":\"")) {
                    read = lineCheck = true;
                    i += 11;
                }
                if ((line.substring(i, i + 13)).equals("\"timestamp\":\"")) {
                    lineCheck = false;
                    i += 13;
                    bufferLine = bufferLine.replace("<span class=\\\"searchmatch\\\">", "");
                    bufferLine = bufferLine.replace("</span>", "");
                    System.out.println(bufferLine.substring(0, bufferLine.length() - 2));
                    bufferLine = "";
                }
                if (lineCheck) bufferLine += line.charAt(i);
            }
            if (!read)
                System.out.print("Нечего не найдено по запросу");
            return;
        }
        System.out.print("Нечего не найдено по запросу или проблемы с соединением (попробуйте еще)");
    }

    private static String ScannerString(Scanner scanner){
        String bool = scanner.next();
        if (!bool.equals(""))
            return bool;
        System.out.print("Вы не ввели запрос: ");
        return ScannerString(scanner);
    }

}