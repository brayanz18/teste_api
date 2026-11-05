
package api_teste.ds.services;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api_teste.ds.exceptions.AuthorizationException;
import api_teste.ds.models.User;
import api_teste.ds.models.dtos.UserCreateDTO;
import api_teste.ds.models.dtos.UserUpdateDTO;
import api_teste.ds.models.enums.ProfileEnums;
import api_teste.ds.repositories.UserRepository;
import api_teste.ds.security.UserSpringSecurity;
import api_teste.ds.services.exceptions.DataBindingViolationException;
import api_teste.ds.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Recupera o usuário autenticado
    public static UserSpringSecurity authenticated() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken
                || !(authentication.getPrincipal() instanceof UserSpringSecurity)) {

            return null;
        }

        return (UserSpringSecurity) authentication.getPrincipal();
    }

    // Busca usuário pelo ID
    @Transactional(readOnly = true)
    public User findById(Long id) {

        Optional<User> user = this.userRepository.findById(id);

        User obj = user.orElseThrow(() -> new ObjectNotFoundException(
                "Usuário não encontrado! Id: " + id
                + ", Tipo: " + User.class.getName()));

        UserSpringSecurity userSS = authenticated();

        if (Objects.isNull(userSS)
                || (!userSS.hasRole(ProfileEnums.ADMIN)
                && !id.equals(userSS.getId()))) {

            throw new AuthorizationException("Acesso negado!");
        }

        return obj;
    }

    // Busca usuário pelo username
    @Transactional(readOnly = true)
    public User findByUsername(String username) {

        User user = this.userRepository.findByUsername(username);

        if (user == null) {
            throw new ObjectNotFoundException(
                    "Usuário não encontrado: " + username);
        }

        return user;
    }

    // Converte DTO de criação para entidade
    public User fromDTO(UserCreateDTO objDto) {

        User obj = new User();

        obj.setUsername(objDto.getUsername());

        // Criptografa a senha usando BCrypt
        obj.setPassword(passwordEncoder.encode(objDto.getPassword()));

        return obj;
    }

    // Converte DTO de atualização
    public User fromDTO(UserUpdateDTO objDto) {

        User obj = new User();

        obj.setId(objDto.getId());

        obj.setPassword(passwordEncoder.encode(objDto.getPassword()));

        return obj;
    }

    // Cadastra um novo usuário
    @Transactional
    public User create(User obj) {

        obj.setId(null);

        // Verifica se o username já está cadastrado
        if (this.userRepository.findByUsername(obj.getUsername()) != null) {

            throw new DataBindingViolationException(
                    "Esse nome de usuário já está cadastrado. Escolha outro.");
        }

        return this.userRepository.save(obj);
    }

    // Atualiza a senha do usuário
    @Transactional
    public User update(User obj) {

        User newObj = findById(obj.getId());

        newObj.setPassword(obj.getPassword());

        return this.userRepository.save(newObj);
    }

    // Deleta um usuário
    @Transactional
    public void delete(Long id) {

        findById(id);

        try {

            this.userRepository.deleteById(id);

        } catch (Exception e) {

            throw new DataBindingViolationException(
                    "Não é possível excluir o usuário pois há entidades relacionadas.");
        }
    }
}
