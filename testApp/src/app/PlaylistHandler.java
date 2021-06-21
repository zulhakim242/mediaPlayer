package app;

import javafx.scene.control.ListView;

public class PlaylistHandler {
    private int currentlyPlaying = 0;
    private int previous;
    private int next;
    private int listLength;
    private ListView<String> listFile;
    
    PlaylistHandler(ListView<String> listFile) {
        this.listFile = listFile;
    }

    public void setListLength(int listLength) {
        this.listLength = listLength;
        // System.out.println("List total: " + listLength);
    }

    public int startPlaylist() {
        currentlyPlaying = 0;
        next = currentlyPlaying + 1;
        previous = currentlyPlaying - 1;
        return currentlyPlaying;
    }

    public int getCurrInPlaylist() {
        return currentlyPlaying;// listFile.getSelectionModel().getSelectedIndex();
    }

    public int getPrevInPlaylist() {
        if(currentlyPlaying == 0) {
            next = currentlyPlaying;
            currentlyPlaying = listLength-1;
            previous = currentlyPlaying-1;
        }
        else {
            currentlyPlaying = previous;
            previous--;
            next = currentlyPlaying+1;
        }
        return currentlyPlaying;
    }

    public int getNextInPlaylist() {
        if(currentlyPlaying == (listLength-1)) {
            previous = currentlyPlaying-1;
            currentlyPlaying = 0;
            next = currentlyPlaying+1;
        } else {
            currentlyPlaying = next;
            previous = currentlyPlaying-1;
            next++;
        }
        return currentlyPlaying;    
    }

    public void setCurrent(int i){
        currentlyPlaying = i;
        if(currentlyPlaying == 0) {
            next = i + 1;   
            previous = listLength-1;
        }
        else if(currentlyPlaying == (listLength-1)) {
            previous = currentlyPlaying-1;
            next = 0;
        } else {
            previous = currentlyPlaying-1;
            next = currentlyPlaying+1;
        }
        
    }

}
