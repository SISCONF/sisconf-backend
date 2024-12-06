package br.ifrn.edu.sisconf.service;

import br.ifrn.edu.sisconf.domain.dtos.CustomerCreateRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.CustomerResponseDTO;
import br.ifrn.edu.sisconf.mapper.CustomerMapper;
import br.ifrn.edu.sisconf.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    public CustomerResponseDTO save(CustomerCreateRequestDTO customerCreateRequestDTO) {
        var customer = customerMapper.toEntity(customerCreateRequestDTO);
        customerRepository.save(customer);
        return customerMapper.toResponse(customer);
    }
}
