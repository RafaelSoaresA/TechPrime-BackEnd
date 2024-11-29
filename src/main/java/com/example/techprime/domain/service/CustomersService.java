package com.example.techprime.domain.service;

import com.example.techprime.domain.entities.Customer;
import com.example.techprime.domain.entities.CustomerAddress;
import com.example.techprime.domain.entities.User;
import com.example.techprime.domain.repository.CustomerAddressRepository;
import com.example.techprime.domain.repository.CustomersRepository;
import com.example.techprime.dto.CustomerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomersService {

    private final UsersService usersService;
    private final CustomersRepository customersRepository;
    private final CustomerAddressRepository customerAddressRepository;


    public Customer create(CustomerDto customerDto) {

        Optional<Customer> customerOptional = this.customersRepository.findByDocument(customerDto.getDocument());

        if(customerOptional.isPresent()){
            throw new RuntimeException("Cliente já cadastrado");
        }

        User userBuilder = User.builder()
                .name(customerDto.getUserDto().getName())
                .email(customerDto.getUserDto().getEmail())
                .password(customerDto.getUserDto().getPassword())
                .role("customer")
                .build();

        User user = usersService.createCustomer(userBuilder);

        Customer customerBuilder = Customer.builder()
                .user(user)
                .fullname(customerDto.getFullname())
                .document(customerDto.getDocument())
                .birthDate(customerDto.getBirthDate())
                .phone1(customerDto.getPhone1())
                .phone2(customerDto.getPhone2())
                .gender(customerDto.getGender())
                .customerType(customerDto.getCustomerType())
                .build();

        log.info("Criando customer... : {}", customerBuilder);
        Customer customer = null;
        try {
            customer = this.customersRepository.save(customerBuilder);
        } catch (Exception e) {
            log.error("Erro ao criar customer: {}", e);
        }

        CustomerAddress billingAddressBuilder = CustomerAddress.builder()
                .customer(customer)
                .customerAddressType("B")
                .nickname(customerDto.getBillingAddressDto().getNickname())
                .zipCode(customerDto.getBillingAddressDto().getZipCode())
                .addressType(customerDto.getBillingAddressDto().getAddressType())
                .address(customerDto.getBillingAddressDto().getAddress())
                .number(customerDto.getBillingAddressDto().getNumber())
                .complement(customerDto.getBillingAddressDto().getComplement())
                .neighborhood(customerDto.getBillingAddressDto().getNeighborhood())
                .city(customerDto.getBillingAddressDto().getCity())
                .state(customerDto.getBillingAddressDto().getState())
                .status("S")
                .build();

        log.info("Criando customer billing address repository... : {}", billingAddressBuilder);
        try {
            CustomerAddress billingCustomerAddress = this.customerAddressRepository.save(billingAddressBuilder);
        } catch (Exception e) {
            log.error("Erro ao criar customer biiling address: {}", e);
        }

        CustomerAddress deliveryAddressBuilder = CustomerAddress.builder()
                .customer(customer)
                .customerAddressType("D")
                .nickname(customerDto.getDeliveryAddressDto().getNickname())
                .zipCode(customerDto.getDeliveryAddressDto().getZipCode())
                .addressType(customerDto.getDeliveryAddressDto().getAddressType())
                .address(customerDto.getDeliveryAddressDto().getAddress())
                .number(customerDto.getDeliveryAddressDto().getNumber())
                .complement(customerDto.getDeliveryAddressDto().getComplement())
                .neighborhood(customerDto.getDeliveryAddressDto().getNeighborhood())
                .city(customerDto.getDeliveryAddressDto().getCity())
                .state(customerDto.getDeliveryAddressDto().getState())
                .status("S")
                .build();

        log.info("Criando customer delivery address repository... : {}", deliveryAddressBuilder);
        try {
            CustomerAddress deliveryCustomerAddress = this.customerAddressRepository.save(deliveryAddressBuilder);
        } catch (Exception e) {
            log.error("Erro ao criar customer delivery address: {}", e);
        }

        return customer;
    }

    public List<CustomerAddress> getCustomerAddress(){
        Optional<User> usersOptional = usersService.findByUserAuthenticated();

        if(usersOptional.isPresent()){
            Optional<Customer> customerOptional = customersRepository.findByUser(usersOptional.get());
            if(customerOptional.isPresent()){
                return customerAddressRepository.findByCustomer(customerOptional.get());
            }
        }
        return null;
    }

    public CustomerAddress saveDeliveryAddress(CustomerAddress customerAddress){
        Optional<User> usersOptional = usersService.findByUserAuthenticated();
        if(usersOptional.isEmpty()){
           throw new RuntimeException("USUÁRIO NÃO ENCONTRADO");
        }
        Optional<Customer> customerOptional = customersRepository.findByUser(usersOptional.get());

        if(customerOptional.isEmpty()){
            throw new RuntimeException("CUSTOMER NÃO ENCONTRADO");
        }

        CustomerAddress deliveryAddressBuilder = CustomerAddress.builder()
                .customer(customerOptional.get())
                .customerAddressType("D")
                .nickname(customerAddress.getNickname())
                .zipCode(customerAddress.getZipCode())
                .addressType(customerAddress.getAddressType())
                .address(customerAddress.getAddress())
                .number(customerAddress.getNumber())
                .complement(customerAddress.getComplement())
                .neighborhood(customerAddress.getNeighborhood())
                .city(customerAddress.getCity())
                .state(customerAddress.getState())
                .status("S")
                .build();

        return customerAddressRepository.save(deliveryAddressBuilder);
    }

    public CustomerAddress updateBillingAddress(CustomerAddress customerAddress){
        Optional<User> usersOptional = usersService.findByUserAuthenticated();

        if(usersOptional.isEmpty()){
            throw new RuntimeException("USUÁRIO NÃO ENCONTRADO");
        }
        Optional<Customer> customerOptional = customersRepository.findByUser(usersOptional.get());

        if(customerOptional.isEmpty()){
            throw new RuntimeException("CUSTOMER NÃO ENCONTRADO");
        }
        List<CustomerAddress> customerAddressList = customerAddressRepository.findByCustomer(customerOptional.get());

        CustomerAddress customerAddressBilling = customerAddressList.stream().filter(obj -> Objects.equals(obj.getCustomerAddressType(), "B")).findFirst().get();

        customerAddressBilling.setZipCode(customerAddress.getZipCode());
        customerAddressBilling.setZipCode(customerAddress.getZipCode());
        customerAddressBilling.setAddressType(customerAddress.getAddressType());
        customerAddressBilling.setAddress(customerAddress.getAddress());
        customerAddressBilling.setNumber(customerAddress.getNumber());
        customerAddressBilling.setComplement(customerAddress.getComplement());
        customerAddressBilling.setNeighborhood(customerAddress.getNeighborhood());
        customerAddressBilling.setCity(customerAddress.getCity());
        customerAddressBilling.setState(customerAddress.getState());

        return customerAddressRepository.save(customerAddressBilling);
    }

    public Customer getCustomerByUserId(){
        Optional<User> usersOptional = usersService.findByUserAuthenticated();

        if(usersOptional.isEmpty()){
            throw new RuntimeException("USUÁRIO NÃO ENCONTRADO");
        }
        Optional<Customer> customerOptional = customersRepository.findByUser(usersOptional.get());

        if(customerOptional.isEmpty()){
            throw new RuntimeException("CUSTOMER NÃO ENCONTRADO");
        }
        return customerOptional.get();
    }
}
