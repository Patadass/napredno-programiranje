import java.util.ArrayList;
import java.util.List;

class Song{
    public String name;
    public String artist;

    public Song(String name, String artist){
        this.name = name;
        this.artist = artist;
    }

    @Override
    public String toString(){
        return String.format("Song{title=%s, artist=%s}", name, artist);
    }
}

class MP3Player{
    public List<Song> songs;
    private int pos;
    private boolean isPlaying;
    private boolean megaNotPlaying;

    public MP3Player(List<Song> songs){
        this.songs = songs;
        pos = 0;
        isPlaying = false;
        megaNotPlaying = false;
    }

    public void pressPlay(){
        if(isPlaying){
            System.out.println("Song is already playing");
            return;
        }
        System.out.println(String.format("Song %d is playing", pos));
        isPlaying = true;
        megaNotPlaying = false;
    }

    public void pressStop(){
        if(!isPlaying && megaNotPlaying){
            System.out.println("Songs are already stopped");
            return;
        }
        if(!isPlaying){
            pos = 0;
            System.out.println("Songs are stopped");
            megaNotPlaying = true;
            return;
        }
        System.out.println(String.format("Song %d is paused", pos));
        isPlaying = false;
        megaNotPlaying = false;
    }

    public void pressFWD(){
        pos++;
        if(pos >= songs.size()){
            pos = 0;
        }
        isPlaying = false;
        megaNotPlaying = false;
        System.out.println("Forward...");
    }

    public void pressREW(){
        pos--;
        if(pos < 0){
            pos = songs.size() - 1;
        }
        isPlaying = false;
        megaNotPlaying = false;
        System.out.println("Reward...");
    }

    public void printCurrentSong(){
        System.out.println(songs.get(pos));
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("MP3Player{currentSong = ").append(pos).append(", songList = [");
        for(int i = 0;i < songs.size();i++){
            sb.append(songs.get(i).toString());
            if(i + 1 < songs.size()){
                sb.append(", ");
            }
            
        }
        sb.append("]}");
        return sb.toString();
    }
}

public class PatternTest {
    public static void main(String args[]) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);
        
        
        System.out.println(player.toString());
        System.out.println("First test");
        
        
        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();
        
        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();
        
        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();
        
        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();
        
        
        System.out.println(player.toString());
        System.out.println("Second test");
        
        
        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();
        
        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();
        
        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();
        
        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();
        
        
        System.out.println(player.toString());
        System.out.println("Third test");
        
        
        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();
        
        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();
        
        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();
        
        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();
        
        
        System.out.println(player.toString());  
    }
}

//Vasiot kod ovde
