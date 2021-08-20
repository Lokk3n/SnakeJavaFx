package MainPackage;

import Snake.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class FileManager {



    static FileChooser fileChooser;
    static Stage mainStage;


    public static void initialize(Stage stage){
        mainStage = stage;
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Save Files", "*.sav")
        );
    }

    public static boolean readFile(){
        File dataFile = fileChooser.showOpenDialog(mainStage);
        FileReader fileReader = null;
        boolean success = false;
        String message = null;
        if(dataFile != null) {
            try {
                fileReader = new FileReader(dataFile);
                Scanner line = new Scanner(fileReader);


                int height = line.nextInt();
                int width = line.nextInt();
                int speed = line.nextInt();
                if(height < 15 || width < 15 || speed < 1) throw new DataCorruptedException("Incorrect configuration");
                Configuration.mapHeight = height;
                Configuration.mapWidth = width;
                Configuration.speedMultiplier = speed;

                boolean direction = line.nextBoolean();
                System.out.println("Direction: " + direction);
                int amount = line.nextInt();
                if(Math.abs(amount) > 1) throw new DataCorruptedException("Incorrect step loaded. Should be either 1 or -1");
                System.out.println("Amount: " + amount);
                line.nextLine(); //jakiś biały znak tu jest
                Scanner word = new Scanner(line.nextLine());

                int foodx = word.nextInt();
                System.out.println(foodx);
                int foody = word.nextInt();
                System.out.println(foody);
                if(foodx > width || foodx < 1 || foody > height || foody < 1) throw new DataCorruptedException("Incorrect food coordinates");

                Territory territory = new Territory(Configuration.mapHeight, Configuration.mapWidth); //musi być tutaj żeby physicale mogły dodać listenery
                Food food = new Food(foodx, foody);


                List<Coordinate> coordinates = new LinkedList<>();
                while (line.hasNextLine()) {
                    word = new Scanner(line.nextLine());
                    try {
                        int x = word.nextInt();
                        int y = word.nextInt();
                        int prevx = word.nextInt();
                        int prevy = word.nextInt();
                        if (x > width || x < 1 || y > height || y < 1 || prevx > width || prevx < 1 || prevy > height || prevy < 1)
                            throw new DataCorruptedException("Incorrect vertebra coordinates");
                        coordinates.add(new Coordinate(x, y, prevx, prevy));

                    } catch (DataCorruptedException e){
                        throw e;
                    } catch (Exception e) {
                        System.out.println("Exception while reading coordinates: " + e.getMessage());
                        throw e;
                    } finally {
                        word.close();
                    }

                }
                Head head = new Head(coordinates.get(0).y, coordinates.get(0).x, coordinates.get(0).prevy, coordinates.get(0).prevx, direction, amount);
                head.setNextTarget(direction, amount, true);
                Tail tail = new Tail(coordinates.get(coordinates.size()-1).x, coordinates.get(coordinates.size()-1).y, coordinates.get(coordinates.size()-1).prevx, coordinates.get(coordinates.size()-1).prevy);
                coordinates.remove(coordinates.get(0));
                coordinates.remove(coordinates.get(coordinates.size()-1));
                Vertebra previousVertebra = head;
                for(Coordinate xy : coordinates){

                    Vertebra newVertebra = new Vertebra();

                    newVertebra.setXPosition(xy.x);
                    newVertebra.setYPosition(xy.y);;
                    newVertebra.setPreviousXPosition(xy.x);
                    newVertebra.setPreviousYPosition(xy.y);

                    previousVertebra.setBackVertebra(newVertebra);
                    newVertebra.setFrontVertebra(previousVertebra);

                    previousVertebra = newVertebra;

                }
                previousVertebra.setBackVertebra(tail);
                tail.setFrontVertebra(previousVertebra);

                Controller.getInstance().startLoadedGame(territory, head, food);
                territory.getSnakeTail().addObserver(ScoreManager.getInstance());
                success = true;

            } catch (IOException e) {
                message = "IOException while reading:\nMessage:\n" + e.getMessage() + "\nCause:\n" + e.getCause() + "\nStack trace:\n" + Arrays.toString(e.getStackTrace());
                System.out.println(message);
                CommunicationManager.displayError(message);
            } catch(DataCorruptedException e) {
                message = e.getMessage();
                CommunicationManager.displayError(message);
            } catch (Exception e) {
                message = "Unknown exception while reading:\nMessage:\n" + e.getMessage() + "\nCause:\n" + e.getCause() + "\nStack trace:\n" + Arrays.toString(e.getStackTrace());
                System.out.println(message);
                CommunicationManager.displayError(message);
            } finally {

                try {
                    if (fileReader != null) {
                        fileReader.close();
                    }
                } catch (IOException e) {
                    message = "IOException while closing fileReader: " + e.getMessage();
                    System.out.println(message);
                    CommunicationManager.displayError(message);
                    success = false;
                }
            }
        }
        return success;
    }

    public static void writeFile(){
        Head head = Territory.getInstance().getSnakeHead();

        List<Coordinate> coordinates = new LinkedList<>();
        Vertebra vertebra = head;
        while(vertebra != null){
            coordinates.add(new Coordinate(
                    vertebra.getXPosition(),
                    vertebra.getYPosition(),
                    vertebra.getPreviousXPosition(),
                    vertebra.getPreviousYPosition()
                    ));
            vertebra = vertebra.getBackVertebra();
        }

        Coordinate food = new Coordinate(
                Territory.getInstance().getFood().getXPosition(),
                Territory.getInstance().getFood().getYPosition(),
                Territory.getInstance().getFood().getPreviousXPosition(),
                Territory.getInstance().getFood().getPreviousYPosition()
        );

        File dataFile = fileChooser.showSaveDialog(mainStage);

        FileWriter fileWriter = null;
        if(dataFile != null) {
            try {
                fileWriter = new FileWriter(dataFile);
                fileWriter.write(Territory.getInstance().getHeight() + "\n");
                fileWriter.write(Territory.getInstance().getWidth() + "\n");
                fileWriter.write(Configuration.speedMultiplier + "\n");
                fileWriter.write(Territory.getInstance().getSnakeHead().getPreviousDirection() + "\n");
                fileWriter.write(Territory.getInstance().getSnakeHead().getPreviousAmount() + "\n");
                fileWriter.write(food.toString() + "\n");
                for (Coordinate xy :
                        coordinates) {
                    fileWriter.write(xy.toString() + "\n");
                }
            } catch (IOException e) {
                CommunicationManager.displayError(e.getMessage());
            } finally {

                try {
                    if (fileWriter != null) {
                        fileWriter.flush();
                        fileWriter.close();
                    }
                } catch (IOException e) {
                    CommunicationManager.displayError(e.getMessage());
                }
            }
        }
    }
}


class Coordinate {
    final int x;
    final int y;
    final int prevx;
    final int prevy;

    Coordinate(int x, int y, int prevx, int prevy){
        this.x = x;
        this.y = y;
        this.prevx = prevx;
        this.prevy = prevy;
    }

    @Override
    public String toString(){
        return this.x + " " + this.y + " " + this.prevx + " " + this.prevy;
    }
}
