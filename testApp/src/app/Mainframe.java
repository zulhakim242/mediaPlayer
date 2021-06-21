package app;

import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.io.File;
import java.io.FilenameFilter;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

public class Mainframe {
    private MediaPlayer mediaPlayer;
    private File[] list;
    private PlaylistHandler playlistHandler;
    
    @FXML
    private MediaView mediaView;

    @FXML
    private Slider progressBar;

    @FXML
    private Slider volumeSlider;

    @FXML
    private ListView<String> mediaList;

    @FXML
    private FontAwesomeIconView playOrPauseBtn;
    
    @FXML
    private StackPane pane;

    public void startMediaPlayer(String path) {
        if(path != null) {
            Media media = new Media(path);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            DoubleProperty widthProp = mediaView.fitWidthProperty();
            DoubleProperty heightProp = mediaView.fitHeightProperty();

            widthProp.bind(pane.widthProperty());
            heightProp.bind(pane.heightProperty());
            //widthProp.bind(Bindings.selectDouble(pane.widthProperty()/* mediaView.sceneProperty() */, "width"));
            //heightProp.bind(Bindings.selectDouble(pane.heightProperty()/* mediaView.sceneProperty() */, "height")); 
            
            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> progressBar.setValue(newValue.toSeconds()));

            progressBar.setOnMousePressed(event -> mediaPlayer.seek(Duration.seconds(progressBar.getValue())));

            progressBar.setOnMouseDragged(event -> mediaPlayer.seek(Duration.seconds(progressBar.getValue())));

            mediaPlayer.setOnReady(() -> {
                Duration total = media.getDuration();
                progressBar.setMax(total.toSeconds());
            });

            volumeSlider.setValue(mediaPlayer.getVolume() * 100);
            volumeSlider.valueProperty().addListener(arg0 -> mediaPlayer.setVolume(volumeSlider.getValue()/100));
            playOrPauseBtn.setGlyphName("PAUSE");
            mediaPlayer.play();
        }
    }

    public void chooseDirectoryMethod() {
        FilenameFilter mediaFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                if(lowercaseName.endsWith(".mp3") || lowercaseName.endsWith(".mp4")) {
                    return true;
                }
                else {
                    return false;
                }
            }
        };

        DirectoryChooser directorychooser = new DirectoryChooser();
        File selectedDirectory = directorychooser.showDialog(null);
        if(selectedDirectory != null) {
            mediaList.getItems().clear(); // clear the ListView if new directory is opened
        }

        System.out.println(selectedDirectory.getAbsolutePath());
        list = selectedDirectory.listFiles(mediaFilter);
        for(int i=0; i<list.length; i++) {
            mediaList.getItems().add(list[i].getName());
            System.out.println(list[i].getAbsolutePath());
        }
        
        playlistHandler = new PlaylistHandler(mediaList);
        playlistHandler.setListLength(list.length);
        playlistHandler.startPlaylist();
        startMediaPlayer(list[0].toURI().toString());

        mediaList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                if(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    mediaPlayer.pause();
                }
                playlistHandler.setCurrent(mediaList.getSelectionModel().getSelectedIndex());
                String p = list[playlistHandler.getCurrInPlaylist()].toURI().toString();
                startMediaPlayer(p);
                
            }
        });
    }

    public void chooseFileMethod() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        String path = file.toURI().toString();
        mediaList.getItems().add(new String(file.getName()));
        startMediaPlayer(path);
    }
    
    public void play() {
        if(mediaPlayer != null)
        if(mediaPlayer.getStatus()==MediaPlayer.Status.PLAYING){
            mediaPlayer.pause();
            // set icon to play
            playOrPauseBtn.setGlyphName("PLAY");
        }
        if (mediaPlayer.getStatus()==MediaPlayer.Status.PAUSED){
            mediaPlayer.play();
            // set icon to pause
            playOrPauseBtn.setGlyphName("PAUSE");
            mediaPlayer.setRate(1);
        }
        if (mediaPlayer.getStatus()==MediaPlayer.Status.STOPPED){
            mediaPlayer.play();
            playOrPauseBtn.setGlyphName("PAUSE");
            mediaPlayer.setRate(1);
        }
    }
    
    /* public void pause() {
        mediaPlayer.pause();
    } */
    
    public void stop() {
        mediaPlayer.stop();
        playOrPauseBtn.setGlyphName("PLAY");
    }

    public void slowRate() {
        mediaPlayer.setRate(0.5);
    }

    public void fastRate() {
        mediaPlayer.setRate(1.5);
    }

    public void skip10() {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(10))); 
    }
    
    public void back10() {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-10))); 
    }

    public void stepBackward() {
        mediaPlayer.pause();
        String path = list[playlistHandler.getPrevInPlaylist()].toURI().toString();
        System.out.println(path);
        startMediaPlayer(path);
    }

    public void stepForward() {
        mediaPlayer.pause();
        String path = list[playlistHandler.getNextInPlaylist()].toURI().toString();
        System.out.println(path);
        startMediaPlayer(path);
    }

    @FXML
    void initialize() {
        
    }
}
