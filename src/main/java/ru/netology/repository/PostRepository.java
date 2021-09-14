package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

// Stub
@Repository
public class PostRepository {

    private AtomicLong idCounter;
    private Map<Long, Post> repository;

    public PostRepository() {
        repository = new ConcurrentHashMap<>();
        idCounter = new AtomicLong(0);
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
            var id = idCounter.incrementAndGet();
            post.setId(id);
            repository.put(id, post);
        } else {
            if (repository.containsKey(post.getId())) {
                repository.replace(post.getId(), post);
            } else {
                var id = idCounter.incrementAndGet();
                ;
                post.setId(id);
                repository.put(id, post);
            }
        }
        return post;
    }

    public void removeById(long id) {
        if (repository.containsKey(id)) {
            repository.remove(id);
        } else {
            throw new NotFoundException();
        }
    }
}
