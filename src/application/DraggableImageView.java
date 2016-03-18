package application;

import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class DraggableImageView extends ImageView {
	
	
	public DraggableImageView()
	{
		super();
		initialize();
	}

	public void initialize() {
		// TODO Auto-generated method stub
		this.setOnDragDetected(event -> {
		Dragboard db = this.startDragAndDrop(TransferMode.ANY);

         /* put a string on dragboard */
         ClipboardContent content = new ClipboardContent();
         content.putImage(this.getImage());
         db.setContent(content);

         event.consume();
		});
		
		this.setOnDragDone(event -> {
			if(event.getTransferMode() == TransferMode.MOVE)
			{
				((ImageView)event.getSource()).setImage(null);
			}
			event.consume();
		});
		
		this.setOnDragDropped(event -> {
			Dragboard dragboard = event.getDragboard();
			boolean success = false;
			if (dragboard.hasUrl()) {
                System.out.println(dragboard.getUrl());
                
                this.setImage(new Image(dragboard.getUrl()));
                
                success = true;
            }else if (dragboard.hasFiles()){
                System.out.println(dragboard.getFiles().get(0).getAbsolutePath());
            }
            System.out.println(dragboard.getString());
                /* let the source know whether the string was successfully
                 * transferred and used */
            event.setDropCompleted(success);

            event.consume();
			event.setDropCompleted(success);
			event.consume();
		});
		
		this.setOnDragExited(event -> {
			this.setEffect(null);
			event.consume();
		});
		
		this.setOnDragEntered(event -> {
			if (event.getGestureSource() != this && (event.getDragboard().hasString() || event.getDragboard().hasFiles())) {
			this.setEffect(new Glow());
			}
			event.consume();
		});
		
		this.setOnDragOver(event -> {
			 if (event.getGestureSource() != this && (event.getDragboard().hasString() || event.getDragboard().hasFiles())) 
			{
				event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
			}
		});
	}
	
	
	
}
