package net.lwenstrom.musicquizbackend;

import net.lwenstrom.musicquizbackend.model.Playlist;
import net.lwenstrom.musicquizbackend.model.Song;
import net.lwenstrom.musicquizbackend.repo.PlaylistRepository;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;

public class PlaylistParser {

    private PlaylistRepository playlistRepository;
    public PlaylistParser(PlaylistRepository playlistRepository){
        this.playlistRepository = playlistRepository;
        System.out.println("testi");
        try {
            test();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

    }

    public void test() throws URISyntaxException, IOException {
        String url = "https://api.deezer.com/playlist/908622995";
        RestTemplate restTemplate = new RestTemplate();
        Playlist playlist = restTemplate.getForObject(url, Playlist.class);
        System.out.println(playlist.getDescription());
        System.out.println(playlist.getSongs().get(0).getAlbum().getTitle());
        playlistRepository.save(playlist);
    }
}
