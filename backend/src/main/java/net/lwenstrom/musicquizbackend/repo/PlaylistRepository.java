package net.lwenstrom.musicquizbackend.repo;

import net.lwenstrom.musicquizbackend.model.Playlist;
import org.springframework.data.repository.CrudRepository;

public interface PlaylistRepository extends CrudRepository<Playlist, Integer> {
}
