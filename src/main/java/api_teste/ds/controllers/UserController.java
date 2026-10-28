package api_teste.ds.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import api_teste.ds.models.User;
import api_teste.ds.models.dtos.UserCreateDTO;
import api_teste.ds.models.dtos.UserUpdateDTO;
import api_teste.ds.services.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        User obj = this.userService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    // Recebe apenas username e password por meio do DTO de criação.
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody UserCreateDTO objDto) {
        User obj = this.userService.fromDTO(objDto);
        obj = this.userService.create(obj);

        URI url = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(obj.getId())
                .toUri();

        return ResponseEntity.created(url).build();
    }

    // Recebe apenas id e password; username permanece inalterado.
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @Valid @RequestBody UserUpdateDTO objDto,
            @PathVariable Long id) {

        objDto.setId(id);
        User obj = this.userService.fromDTO(objDto);
        this.userService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}