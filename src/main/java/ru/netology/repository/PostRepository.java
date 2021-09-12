package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// Stub
public class PostRepository {

    private static Long idCounter = 0L;
    private Map<Long, Post> repository;

    public PostRepository() {
        repository = new ConcurrentHashMap<>();
    }

    public List<Post> all() {
        return repository.values().stream().collect(Collectors.toList());
    }

    public Optional<Post> getById(long id) {
        return repository.containsKey(id) ? Optional.of(repository.get(id))
                                          : Optional.empty();
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            post.setId(++idCounter);
            repository.put(idCounter, post);
        } else {
            if (repository.containsKey(post.getId())) {
                repository.replace(post.getId(), post);
            } else {
                post.setId(++idCounter);
                repository.put(idCounter, post);
            }
        }
        return post;
    }

    public void removeById(long id) {
        if (repository.containsKey(id)){
            repository.remove(id);
        } else{
            throw new NotFoundException();
        }
    }
}
