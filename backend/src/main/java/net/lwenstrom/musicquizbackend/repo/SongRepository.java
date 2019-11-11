package net.lwenstrom.musicquizbackend.repo;

import net.lwenstrom.musicquizbackend.model.Song;
import org.springframework.data.repository.CrudRepository;

public interface SongRepository extends CrudRepository<Song, Integer> {
}
