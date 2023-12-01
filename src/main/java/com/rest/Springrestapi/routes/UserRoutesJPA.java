package com.rest.Springrestapi.routes;

import com.rest.Springrestapi.database.UserDaoService;
import com.rest.Springrestapi.exceptions.UserNotFoundError;
import com.rest.Springrestapi.model.Post;
import com.rest.Springrestapi.model.User;
import com.rest.Springrestapi.repository.PostRepository;
import com.rest.Springrestapi.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserRoutesJPA {

    private UserDaoService service;
    private UserRepository repository;
    private PostRepository postRepository;

    public UserRoutesJPA(UserRepository repository, PostRepository postRepository){
        this.repository = repository;
        this.postRepository = postRepository;
    }
    @GetMapping("/jpa/users")
    public List<User> getAllUsers(){
        return repository.findAll();
    }

    @GetMapping("/jpa/users/{id}")
    public EntityModel<User> getAUser(@PathVariable int id){
        Optional<User> user = repository.findById(id);

        if (user.isEmpty()){
            throw new UserNotFoundError("Can not find user with id : "+id);
        }

        var entityModel = EntityModel.of(user.get());
        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).getAllUsers());
        entityModel.add(link.withRel("all-users"));

        return entityModel;
    }

    @PostMapping("/jpa/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        User newUser = repository.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/jpa/users/{id}")
    public void deleteUser(@PathVariable int id){
        Optional<User> user = repository.findById(id);

        if (user.isEmpty()){
            throw new UserNotFoundError("Can not find user with id : "+id);
        }
        repository.deleteById(id);

    }

    @GetMapping("/jpa/users/{id}/posts")
    public List<Post> retriveUserPost(@PathVariable int id){
        Optional<User> user = repository.findById(id);

        if (user.isEmpty()){
            throw new UserNotFoundError("Can not find user with id : "+id);
        }
        return user.get().getPost();
    }

    @PostMapping("/jpa/users/{id}/posts")
    public ResponseEntity<Object> createUserPost(@PathVariable int id, @Valid @RequestBody Post post){
        Optional<User> user = repository.findById(id);

        if (user.isEmpty()){
            throw new UserNotFoundError("Can not find user with id : "+id);
        }
        post.setUser(user.get());
        var savedPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedPost.getId()).toUri();
        return ResponseEntity.created(location).build();
    }
}
