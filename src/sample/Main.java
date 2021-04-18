package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main extends Application {

    public ArrayList<Line> needles = new ArrayList<Line>(); // objetos de agujas almacenados
    public Group content = new Group(); // grupo de las lineas lanzadas (agujas)
    public Line[] columns = new Line[5]; // arreglo de columnas
    public Random randy = new Random();
    public int dVerticales = 200; // distancia entre verticales
    public static double nExperiments, nSuccess = 0;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        columns();
        generateNeedles((int)nExperiments);
        results();
        stage.setTitle("AGUJAS DE BUFFON");
        stage.setScene(new Scene(content, 800, 600));
        stage.show();
    }

    public void columns() {
        for (int i = 0; i < columns.length; i++) {
            columns[i] = new Line(dVerticales * i, 0, dVerticales * i, 600);
            columns[i].setStrokeWidth(3);
            content.getChildren().add(columns[i]);
        }
    }

    public void generateNeedles(int num) {
        double x, y, dx, dy, theta;

        for (int i = 0; i < num; i++) {
            // valores pseudoaleatorios
            // parte entera + parte decimal
            theta = -1 * (randy.nextInt(180) + randy.nextDouble());
            y = randy.nextInt(601) + randy.nextDouble();
            x = randy.nextInt(801) + randy.nextDouble();
            // calcular el segundo par de coordenadas de la aguja
            dy = y + Math.sin(Math.toRadians(theta)) * (dVerticales);
            dx = x + Math.cos(Math.toRadians(theta)) * (dVerticales);
            // agregar la aguja al ArrayList<Line>
            needles.add(new Line(x, y, dx, dy));
            // validar si cruzan o no una columna
            if (intersects(x, dx)) {
                nSuccess++;
                needles.get(i).setStroke(Color.RED);
            } else {
                needles.get(i).setStroke(Color.BLUE);
            }
        }
        content.getChildren().addAll(needles);
    }

    public boolean intersects(double x, double dx) {
        // comprobar la colision con la coordenada x de cada columna
        // coordenada x de columna i = dVerticales * i
        for (int i = 0; i < columns.length; i++){
            if ((x < dVerticales * i && dx > dVerticales * i) || (dx < dVerticales * i && x > dVerticales * i)){
                return true;
            }
        }
        return false;
    }

    public double approximatePI() {
        return (2 * (double)(nExperiments / nSuccess));
    }

    public void results() {
        String leftAlignFormat = "| %-16s | %-25s | %-25s |%n";
        // imprimir tabla de pares de coordenadas dentro del circulo
        System.out.println("\n*** AGUJAS LANZADAS ***");
        System.out.format("+------------------+--------------------------+-----------------------------+%n");
        System.out.format("| AGUJA N          | (x1, y1)                 | (x2, y2)                   |%n");
        System.out.format("+------------------+--------------------------+-----------------------------+%n");
        for (int k = 0; k < nExperiments; k++) {
            System.out.format(leftAlignFormat,
                    "AGUJA " + (k + 1),
                    // (x1, y1)
                    "(" + String.format("%.6f", needles.get(k).getStartX()) + ", " + String.format("%.6f", needles.get(k).getStartY()) + ")",
                    // (x2, y2)
                    "(" + String.format("%.6f", needles.get(k).getEndX()) + ", " + String.format("%.6f", needles.get(k).getEndY()) + ")");
        }
        System.out.format("+------------------+---------------------------+---------------------------+%n");
        // imprimir resultados
        System.out.println("\n*** RESULTADOS ***");
        System.out.println("EXITOS (ROJO)    = " + (int)nSuccess);
        System.out.println("FALLOS (AZUL)    = " + (int)(nExperiments - nSuccess));
        System.out.println("APROXIMADO DE PI = " + approximatePI());
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        do {
            System.out.print("NUMERO DE AGUJAS A LANZAR: ");
            nExperiments = scan.nextInt();
        } while (nExperiments < 0 || nExperiments > 1000000);
        scan.close();
        launch(args);
    }
}
