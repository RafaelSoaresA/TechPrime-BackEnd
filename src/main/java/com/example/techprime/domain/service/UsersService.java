package com.example.techprime.domain.service;

import com.example.techprime.domain.entities.Customer;
import com.example.techprime.domain.entities.HistoryInvoice;
import com.example.techprime.domain.entities.User;
import com.example.techprime.domain.repository.CustomersRepository;
import com.example.techprime.domain.repository.UsersRepository;
import com.example.techprime.dto.AuthDto;
import com.example.techprime.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class UsersService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomersRepository customersRepository;


    @Transactional(propagation = Propagation.REQUIRED)
    public User create(UserDto userDto) {
        Optional<User> usersOptional = this.usersRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .credit(userDto.getCredit())
                .role(userDto.getRole())
                .deleted(false)
                .build();

        if(!Objects.equals(usersOptional.get().getRole(), "admin")){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autorizado, é necessário o perfil 'admin'");
        }

        usersOptional = this.usersRepository.findByEmail(user.getEmail());

        if(usersOptional.isPresent()){
            throw new RuntimeException("Usuário já existe");
        }

        user = user.toBuilder().password(passwordEncoder.encode(userDto.getPassword())).build();

        log.info("Criando usuário... : {}", user);
        try {
            return this.usersRepository.save(user);
        }
        catch (Exception e) {
            log.error("Erro ao criar usuário: {}", e);
        }
        return null;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public User createCustomer(User user) {
        Optional<User> usersOptional = this.usersRepository.findByEmail(user.getEmail());

        if(usersOptional.isPresent()){
            throw new RuntimeException("Usuário já existe");
        }

        user = user.toBuilder().password(passwordEncoder.encode(user.getPassword())).build();

        log.info("Criando usuário... : {}", user);
        try {
            return this.usersRepository.save(user);
        }
        catch (Exception e) {
            log.error("Erro ao criar usuário: {}", e);
        }
        return null;
    }

    public List<User> listUsers() {
        return usersRepository.findAll();
    }

    public User update(UserDto userDTO) {
        Optional<User> userAuthenticated= this.usersRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if(userAuthenticated.isEmpty()){
            throw new RuntimeException("USUÁRIO NÃO ENCONTRADO");
        }
        User user;
        if (!userDTO.getId().equals(userAuthenticated.get().getId())) {
            if(userAuthenticated.get().getRole().equals("admin")){
                Optional<User> usersOptional = usersRepository.findByid(userDTO.getId());
                if(usersOptional.isEmpty()){
                    throw new RuntimeException("USUÁRIO NÃO ENCONTRADO");
                }
                user = usersOptional.get();

                if (!user.getRole().equals(userDTO.getRole())) {
                    if(userAuthenticated.get().getRole().equals("admin")) {
                        user.setRole(userDTO.getRole());
                    } else {
                        throw new RuntimeException("Acesso não permitido");
                    }
                }

                if (user.getCredit() != (userDTO.getCredit())) {
                    if(userAuthenticated.get().getRole().equals("admin")) {
                        user.setCredit(userDTO.getCredit());
                    } else {
                        throw new RuntimeException("Acesso não permitido");
                    }
                }

            } else {
                throw new RuntimeException("Acesso não permitido");
            }

        } else {
            user = userAuthenticated.get();

            if(userAuthenticated.get().getRole().equals("customer")){

                Optional<Customer> customerOptional = customersRepository.findByUser(user);

                if(customerOptional.isEmpty()){
                    throw new RuntimeException("CUSTOMER NÃO ENCONTRADO");
                }
                Customer customer = customerOptional.get();
                customer.setFullname(userDTO.getCustomerDto().getFullname());
                customer.setPhone1(userDTO.getCustomerDto().getPhone1());
                customer.setPhone2(userDTO.getCustomerDto().getPhone2());
                customersRepository.save(customer);

                user.setEmail(userDTO.getEmail());
                user.setName(userDTO.getCustomerDto().getFullname());
            }
        }

        user = user.toBuilder().password(passwordEncoder.encode(userDTO.getPassword())).build();

        return this.usersRepository.save(user);
    }

    public User updateUser(User user) {
        return usersRepository.save(user);
    }

    public Optional<User> findUserById(Integer id) {
        return usersRepository.findById(id);
    }


    public User findByEmail(String email) {
        return this.usersRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Optional<User> findByUserAuthenticated(){
        return usersRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public AuthDto auth(AuthDto authDto) {
        User users = this.findByEmail(authDto.getEmail());

        if(users.getDeleted() != null && users.getDeleted()){
            throw new RuntimeException("Usuário desabilitado");
        }
        if (!this.passwordEncoder.matches(authDto.getPassword(), users.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        StringBuilder password = new StringBuilder().append(users.getEmail()).append(":").append(users.getPassword());

        return AuthDto.builder().email(users.getEmail()).token(
                Base64.getEncoder().withoutPadding().encodeToString(password.toString().getBytes())
        ).id(users.getId()).password(users.getPassword()).role(users.getRole())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> usersOptional = this.usersRepository.findByEmail(username);

        return usersOptional.map(users -> new org.springframework.security.core.userdetails.User(users.getEmail(), users.getPassword(), new ArrayList<GrantedAuthority>()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
    }
}