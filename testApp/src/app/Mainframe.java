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
import java.util.ArrayList;
import java.util.List;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

public class Mainframe {
    private MediaPlayer mediaPlayer;
    private File[] list;
    private PlaylistHandler playlistHandler;
    private List<File> listFile = new ArrayList<>();
    public static String status;

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

    public void startMediaPlayer() {

            String path = listFile.get(playlistHandler.getCurrInPlaylist()).toURI().toString();
            Media media = new Media(path);
            mediaPlayer = new MediaPlayer(media);
            
            mediaView.setMediaPlayer(mediaPlayer);

            DoubleProperty widthProp = mediaView.fitWidthProperty();
            DoubleProperty heightProp = mediaView.fitHeightProperty();

            widthProp.bind(pane.widthProperty());
            heightProp.bind(pane.heightProperty());
            
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
            status = mediaList.getItems().get(playlistHandler.getCurrInPlaylist());
            App.primaryStage.setTitle("Now Playing: " + status);
            mediaPlayer.play();

            mediaList.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    if(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                        mediaPlayer.pause();
                    }
                    playlistHandler.setCurrent(mediaList.getSelectionModel().getSelectedIndex());
                    startMediaPlayer();
                }
            });
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

        System.out.println(selectedDirectory.getAbsolutePath());
        list = selectedDirectory.listFiles(mediaFilter);
        for(int i=0; i<list.length; i++) {
            mediaList.getItems().add(list[i].getName());
            System.out.println(list[i].getAbsolutePath());
            listFile.add(list[i]);
        }
        System.out.println(listFile);
        playlistHandler = new PlaylistHandler(listFile);
        startMediaPlayer();
    }

    public void chooseFileMethod() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        listFile.add(file);
        System.out.println("list file from choosefilemethod: " + listFile);
        mediaList.getItems().add(new String(file.getName()));
        playlistHandler = new PlaylistHandler(listFile);
        startMediaPlayer();
    }
    
    public void play() {
        String s = "";
        if(mediaPlayer != null)
        if(mediaPlayer.getStatus()==MediaPlayer.Status.PLAYING){
            mediaPlayer.pause();
            // set icon to play
            s += "Paused: ";
            playOrPauseBtn.setGlyphName("PLAY");
        }
        if (mediaPlayer.getStatus()==MediaPlayer.Status.PAUSED){
            mediaPlayer.play();
            s += "Now Playing: ";
            // set icon to pause
            playOrPauseBtn.setGlyphName("PAUSE");
            mediaPlayer.setRate(1);
        }
        if (mediaPlayer.getStatus()==MediaPlayer.Status.STOPPED){
            mediaPlayer.play();
            s += "Now Playing: ";
            playOrPauseBtn.setGlyphName("PAUSE");
            mediaPlayer.setRate(1);
        }
        App.primaryStage.setTitle(s + status);

    }
    
    public void stop() {
        mediaPlayer.stop();
        playOrPauseBtn.setGlyphName("PLAY");
        App.primaryStage.setTitle("Stopped");
    }

    public void slowRate() {
        if(mediaPlayer.rateProperty().getValue() == 0.5) {
            mediaPlayer.setRate(1.0);
        }
        else {
            mediaPlayer.setRate(0.5);
        }
        System.out.println("Playing rate: " + mediaPlayer.rateProperty().getValue());
    }

    public void fastRate() {
        if(mediaPlayer.rateProperty().getValue() == 1.5) {
            mediaPlayer.setRate(1.0);
        }
        else {
            mediaPlayer.setRate(1.5);
        }
        System.out.println("Playing rate: " + mediaPlayer.rateProperty().getValue());
    }

    public void skip10() {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(10))); 
    }
    
    public void back10() {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-10))); 
    }

    public void stepBackward() {
        mediaPlayer.pause();
        playlistHandler.getPrevInPlaylist();
        startMediaPlayer();
    }

    public void stepForward() {
        mediaPlayer.pause();
        playlistHandler.getNextInPlaylist();
        System.out.println(playlistHandler.getCurrInPlaylist());
        startMediaPlayer();
    }

    @FXML
    void initialize() {
        
    }
}
