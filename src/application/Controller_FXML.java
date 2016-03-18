package application;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class Controller_FXML implements Initializable{

	@FXML
    private StackPane paneImageContainer;

    @FXML
    private Text textAngle;

    @FXML
    private Slider sliderAngle;

    @FXML
    private TextField textFieldTransparence;

    @FXML
    private Slider sliderTransparance;

    @FXML
    private CheckBox checkBoxBlur;

    @FXML
    private GridPane gridPaneContainer;

    @FXML
    private Spinner<Integer> spinerHauteur;

    @FXML
    private Spinner<Integer> spinerLargeur;

    @FXML
    private Spinner<Integer> spinerIterations;

    @FXML
    private Button buttonReset;

    @FXML
    private Button buttonQuitter;
    
    private BoxBlur boxBlur = new BoxBlur(0,0,0);
    private BoxBlur newBoxBlur = new BoxBlur(0,0,0);
    @FXML
    void quitter(ActionEvent event) {
    	System.exit(0);
    }

    @FXML
    void reset(ActionEvent event) {
    	sliderAngle.setValue(0);
    	sliderTransparance.setValue(0);
    	initialisationSpinners();
    	checkBoxBlur.setSelected(false);
    }
    
    private DraggableImageView dragableImageView = new DraggableImageView();
   

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		Image image = new Image("application/Javafx_logo.png");
		Image imageNonPhoto = new Image("application/nocamera.jpeg");
		
		dragableImageView.setImage(image);
		dragableImageView.getStyleClass().add("innerShadow");
		
		dragableImageView.setPreserveRatio(true);
		dragableImageView.fitWidthProperty().bind(paneImageContainer.widthProperty());
		dragableImageView.fitHeightProperty().bind(paneImageContainer.heightProperty());
		
		
		//CREATION MENU CONTEXTUEL
		final ContextMenu cm = new ContextMenu();
		MenuItem cmItem = new MenuItem("Delet Image");
		cmItem.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				dragableImageView.setImage(imageNonPhoto);
			}
		});
		
		cm.getItems().add(cmItem);
		dragableImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
				{

					@Override
					public void handle(MouseEvent event) {
						if(event.getButton() == MouseButton.SECONDARY)
						{
							cm.show(dragableImageView, event.getScreenX(), event.getScreenY());
						}
					}
				
				}
			);
				
		
		paneImageContainer.getChildren().add(dragableImageView);
		
		initialisationSpinners();
		setBinding();
		//activationDesactivationButtonReset();
		
		
	}
	
	private void setBinding()
	{
		textAngle.textProperty().bind(Bindings.format("%.0f°", sliderAngle.valueProperty()));
		dragableImageView.rotateProperty().bind(sliderAngle.valueProperty());
		
		//textFieldTransparence.textProperty().bind(Bindings.format("%.0f", sliderTransparance.valueProperty()));
		
		Bindings.bindBidirectional(textFieldTransparence.textProperty(), sliderTransparance.valueProperty(), new DecimalFormat("##0"));
		
		//IntegerBinding binding = new When(textFieldTransparence.textProperty().equals(null)).then(sliderTransparance.valueProperty().set(0));
		
		
		dragableImageView.opacityProperty().bind(sliderTransparance.valueProperty().divide(100).subtract(1).multiply(-1));
		gridPaneContainer.disableProperty().bind(checkBoxBlur.selectedProperty().not());
		
		
		checkBoxBlur.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				
				if(!newValue){
					
					paneImageContainer.setEffect(newBoxBlur);
				}
				else
				{
					paneImageContainer.setEffect(boxBlur);
				}
				
//				if(newValue | sliderAngle.getValue() !=0 | sliderTransparance.getValue() != 0)
//				{
//					buttonReset.setDisable(false);
//				}
//				else
//				{
//					buttonReset.setDisable(true);
//				}
			}
		});
		
		boxBlur.heightProperty().bind(spinerHauteur.valueProperty());
		boxBlur.widthProperty().bind(spinerLargeur.valueProperty());
		boxBlur.iterationsProperty().bind(spinerIterations.valueProperty());
		
		buttonReset.disableProperty().bind(checkBoxBlur.selectedProperty().not().
				and(spinerHauteur.valueProperty().isEqualTo(5)).
				and(spinerLargeur.valueProperty().isEqualTo(5)).
				and(spinerIterations.valueProperty().isEqualTo(1)).
				and(sliderTransparance.valueProperty().isEqualTo(0)).
				and(sliderAngle.valueProperty().isEqualTo(0)));
		
	}
	
	private void initialisationSpinners()
	{
		spinerHauteur.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 127, 5));
		spinerLargeur.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 127, 5));
		spinerIterations.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,3, 1));
	}
	
	@SuppressWarnings("unused")
	private void activationDesactivationButtonReset()
	{
		sliderAngle.valueProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				Double sliderNewValue = (Double) newValue;
				if(sliderNewValue !=0 | sliderTransparance.getValue() !=0 | checkBoxBlur.isSelected()) 
				{
					buttonReset.setDisable(false);
				}
				else
				{
					buttonReset.setDisable(true);
				}
			}

		});
		
		sliderTransparance.valueProperty().addListener(new ChangeListener<Object>() {
			
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				Double sliderNewValue = (Double) newValue;
				if(sliderNewValue !=0 | sliderAngle.getValue()!= 0 | checkBoxBlur.isSelected()) 
				{
					buttonReset.setDisable(false);
				}
				else
				{
					buttonReset.setDisable(true);
				}
			}
			
		});
	}
}
