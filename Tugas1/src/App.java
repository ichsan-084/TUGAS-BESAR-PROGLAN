import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class App extends Application {

    private ArrayList<TextField> inputKataList;
    private TextArea outputKata;
    private ChoiceBox<Integer> jumlahKolomChoiceBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Welcome to Generate Random Word");

        // Membuat komponen GUI
        inputKataList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            TextField textField = new TextField();
            textField.setPromptText("Input " + i);
            inputKataList.add(textField);
        }

        jumlahKolomChoiceBox = new ChoiceBox<>();
        for (int i = 2; i <= 10; i++) {
            jumlahKolomChoiceBox.getItems().add(i);
        }
        jumlahKolomChoiceBox.setValue(2); // Default: 2 kolom

        Button buttonGenerate = new Button("Generate");
        buttonGenerate.setOnAction(e -> generateRandomWords());

        outputKata = new TextArea();
        outputKata.setEditable(false);

        // Membuat layout GUI menggunakan BorderPane
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20, 20, 20, 20));

        // Menambahkan komponen ke layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        int row = 0;
        for (TextField textField : inputKataList) {
            gridPane.add(new Label("Input " + (row + 1) + ":"), 0, row);
            gridPane.add(textField, 1, row);
            row++;
        }

        // Tambahkan ChoiceBox untuk memilih jumlah kolom
        gridPane.add(new Label("Jumlah Kolom:"), 0, row);
        gridPane.add(jumlahKolomChoiceBox, 1, row);

        borderPane.setLeft(gridPane);
        borderPane.setBottom(buttonGenerate);
        borderPane.setCenter(outputKata);

        // Mendapatkan ukuran layar utama
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();

        // Membuat scene dan menampilkan stage
        Scene scene = new Scene(borderPane, screenWidth, screenHeight);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void generateRandomWords() {
        int jumlahKolom = jumlahKolomChoiceBox.getValue();

        ArrayList<String> kataList = new ArrayList<>();

        for (int i = 0; i < jumlahKolom; i++) {
            String kata = inputKataList.get(i).getText();
            if (kata.isEmpty()) {
                outputKata.setText("You cannot generate. All input fields must be filled.");
                return;
            }
            kataList.add(kata);
        }

        Random random = new Random();
        int randomIndex = random.nextInt(kataList.size());

        String randomWord = kataList.get(randomIndex);
        outputKata.setText("Output: " + randomWord);

        // Save both input and output to the file
        saveInputAndOutputToFile(kataList, randomWord);
    }

    private void saveInputAndOutputToFile(ArrayList<String> kataList, String randomWord) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Results Generate.txt", true))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(formatter);

            writer.write("Waktu dan Tanggal: " + formattedDateTime);
            writer.newLine();
            writer.write("Inputan: \"" + String.join("\" | \"", kataList) + "\"");
            writer.newLine();
            writer.write("Output: \"" + randomWord + "\"");
            writer.newLine();
            writer.newLine(); // Spasi antara entri

            System.out.println("Input dan output berhasil disimpan ke dalam file.");
        } catch (IOException e) {
            System.out.println("Gagal menyimpan input dan output ke dalam file: " + e.getMessage());
        }
    }
}
