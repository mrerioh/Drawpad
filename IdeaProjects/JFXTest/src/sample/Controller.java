package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller {

    @FXML
    private ColorPicker colorpicker;

    @FXML
    private TextField saveid;

    @FXML
    private Boolean tool = false;

    @FXML
    private Canvas canvas;

    private GraphicsContext brush;

    private ArrayList<Image> stack = new ArrayList<>();

    public void initialize(){

        brush = canvas.getGraphicsContext2D();
        canvas.setOnMousePressed(e->{
            double size = 6;
            double x = e.getX() - size/2;
            double y = e.getY() - size/2;

            brush.setFill(colorpicker.getValue());
            brush.fillRect(x,y,size,size);
        });
        canvas.setOnMouseDragged(e->{
            double size = 6;
            double x = e.getX() - size/2;
            double y = e.getY() - size/2;

            brush.setFill(colorpicker.getValue());
            brush.fillRect(x,y,size,size);

        });
        canvas.setOnMouseReleased(e->{
                Image snapshot = canvas.snapshot(null,null);
                stack.add(snapshot);
                Image current = stack.get(stack.size()-1);
                brush.drawImage(current,0,0);
        });
    }

    @FXML
    public void toolselected(ActionEvent event){
        brush.clearRect(0, 0, 800, 600);
        Image snapshot = canvas.snapshot(null,null);
        stack.add(snapshot);

    }

    @FXML
    public void popStack(ActionEvent event){
        System.out.println(stack.size());
        if(!stack.isEmpty()) {
            stack.remove(stack.size()-1);
            try {
                Image current = stack.get(stack.size() - 1);
                brush.drawImage(current, 0, 0);
            }catch(Exception e){
                System.out.println("No stack!");
                brush.clearRect(0, 0, 800, 600);
            }
        }
    }

    @FXML
    public void save(ActionEvent event){
        String filename = saveid.getText();
        try{
            Image snapshot= canvas.snapshot(null,null);
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot,null),"png", new File(filename + ".png"));
        } catch(Exception e){
            System.out.println("Failed to save");
        }
    }
}
