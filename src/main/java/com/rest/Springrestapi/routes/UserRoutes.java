package com.rest.Springrestapi.routes;

import com.rest.Springrestapi.database.UserDaoService;
import com.rest.Springrestapi.exceptions.UserNotFoundError;
import com.rest.Springrestapi.model.User;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class UserRoutes {

    private UserDaoService service;

    public UserRoutes(UserDaoService service){
        this.service = service;
    }
    @GetMapping("/users")
    public List<User> getAllUsers(){
        return service.findAll();
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> getAUser(@PathVariable int id){
        User user = service.findOne(id);

        if (user == null){
            throw new UserNotFoundError("Can not find user with id : "+id);
        }

        var entityModel = EntityModel.of(user);
        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).getAllUsers());
        entityModel.add(link.withRel("all-users"));

        return entityModel;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        User newUser = service.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id){
        User user = service.findOne(id);

        if (user == null){
            throw new UserNotFoundError("Can not find user with id : "+id);
        }
        service.deleteById(id);

    }
}
