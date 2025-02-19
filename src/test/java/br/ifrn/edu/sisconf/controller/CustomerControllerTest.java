package br.ifrn.edu.sisconf.controller;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.ifrn.edu.sisconf.SecurityTestConfig;
import br.ifrn.edu.sisconf.constants.KeycloakConstants;
import br.ifrn.edu.sisconf.domain.Customer;
import br.ifrn.edu.sisconf.domain.dtos.AddressRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Customer.CustomerRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Person.PersonRequestDTO;
import br.ifrn.edu.sisconf.domain.enums.CustomerCategory;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationRecord;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationResponse;
import br.ifrn.edu.sisconf.mapper.CustomerMapper;
import br.ifrn.edu.sisconf.repository.CityRepository;
import br.ifrn.edu.sisconf.repository.CustomerRepository;
import br.ifrn.edu.sisconf.service.keycloak.KeycloakUserService;
import br.ifrn.edu.sisconf.util.JwtTestUtil;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(SecurityTestConfig.class)
public class CustomerControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @MockitoBean
    private KeycloakUserService keycloakUserService;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private CityRepository cityRepository;

    private CustomerRequestDTO customerRequestDTO;
    private Customer customer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        var city = cityRepository.findAll().getFirst();
        customerRequestDTO = new CustomerRequestDTO(
            CustomerCategory.MARKETER,
            new PersonRequestDTO(
                "Maximiliano Bartholomew Alexander Christopher William" +
                " Ferdinand Jonathan Percival Archibald Montgomery Fitzwilliam III of" +
                " Strathmore, Keeper of Scrolls, Protector of the Seal, Defender of the Realm," +
                " Lord of the",
                "Maximiliano Bartholomew Alexander Christopher William" +
                " Ferdinand Jonathan Percival Archibald Montgomery Fitzwilliam III of" +
                " Strathmore, Keeper of Scrolls, Protector of the Seal, Defender of the Realm," +
                " Lord of the",
                "111.111.111-11",
                null,
                "(11) 91111-1111",
                "abcd1234",
                "abcd1234",
                "teste@gmail.com",
                new AddressRequestDTO(
                    "Rua dos Jacarandás Floridos do Bairro das Águas Claras em Comemoração" +
                    " ao Centenário da Fundação da Cidade de Esperança",
                    "59911-111",
                    "Residencial das Palmeiras Altas e Horizontes Verdejantes do Vale Encantado" +
                    "em Celebração às Belezas Naturais da Região Serrana",
                    22,
                    city.getId()
                )
            )
        );
        customer = customerMapper.toEntity(customerRequestDTO);
        customer.getPerson().setKeycloakId(UUID.randomUUID().toString());
        customer.getPerson().getAddress().setCity(city);
        customer = customerRepository.save(customer);
    }

    @AfterEach
    public void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    public void shouldReturnCustomerDataWhenMeValidKeycloakId() throws Exception {
        String tokenString = JwtTestUtil.getToken(customer.getPerson().getEmail());
        Jwt jwt = JwtTestUtil.getJwt(tokenString, customer.getPerson());
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);

        String expectedResponse = objectMapper.writeValueAsString(
            customerMapper.toResponseDTO(customer)
        );

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/customers/me", new ArrayList<>())
            .header("Authorization", "Bearer " + tokenString)
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
    }

    @Test
    public void shouldReturnNotFoundWhenCustomerMeInvalidKeycloakId() throws Exception {
        String tokenString = JwtTestUtil.getToken(customer.getPerson().getEmail());
        customer.getPerson().setKeycloakId("");
        Jwt jwt = JwtTestUtil.getJwt(tokenString, customer.getPerson());
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/customers/me", new ArrayList<>())
            .header("Authorization", "Bearer " + tokenString)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldCreateCustomerWhenValidRequestData() throws Exception {
        customerRepository.deleteAll();
        when(keycloakUserService.create(new UserRegistrationRecord(
            customerRequestDTO.getPerson().getFirstName(), 
            customerRequestDTO.getPerson().getLastName(), 
            customerRequestDTO.getPerson().getPassword(), 
            customerRequestDTO.getPerson().getEmail(), 
            KeycloakConstants.CUSTOMER_GROUP_NAME))
        ).thenReturn(new UserRegistrationResponse(
            UUID.randomUUID().toString(),
            customerRequestDTO.getPerson().getFirstName(), 
            customerRequestDTO.getPerson().getLastName(), 
            customerRequestDTO.getPerson().getEmail())
        );
        String requestBody = objectMapper.writeValueAsString(customerRequestDTO);

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/customers", new ArrayList<>())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void shouldUpdateCustomerWhenValidRequestData() throws Exception {
        // Update data
        var city = cityRepository.findAll().getLast();
        customerRequestDTO.setCategory(CustomerCategory.ENTREPRENEUR);
        customerRequestDTO.setPerson(new PersonRequestDTO(
            "Nome do Teste",
            "Sobrenome do Teste",
            "222.222.222-22",
            null,
            "(22) 92222-2222",
            null,
            null,
            null,
            new AddressRequestDTO(
                "Nova Rua do Teste",
                "22222-222",
                "Novo Bairro do Teste",
                13,
                city.getId()
            )
        ));
        String requestBody = objectMapper.writeValueAsString(customerRequestDTO);

        // Post update request expected data
        customerMapper.updateEntityFromDTO(customerRequestDTO, customer);
        customer.getPerson().getAddress().setCity(city);
        String expectedResponse = objectMapper.writeValueAsString(customerMapper.toResponseDTO(customer));

        // Get mocked JWT and return it when decoder gets called
        String tokenString = JwtTestUtil.getToken(customerRequestDTO.getPerson().getEmail());
        Jwt jwt = JwtTestUtil.getJwt(tokenString, customer.getPerson());
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/customers/{id}", customer.getId())
            .header("Authorization", "Bearer " + tokenString)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
    }

    @Test
    public void shouldNotUpdateCustomerUnupdatableFieldsInRequestData() throws Exception {
        // Update data
        var city = cityRepository.findAll().getLast();
        customerRequestDTO.setCategory(CustomerCategory.ENTREPRENEUR);
        customerRequestDTO.setPerson(new PersonRequestDTO(
            "Nome do Teste",
            "Sobrenome do Teste",
            "222.222.222-22",
            null,
            "(22) 92222-2222",
            "novasenha",
            "novasenha",
            "novo.email.do.teste@email.com",
            new AddressRequestDTO(
                "Nova Rua do Teste",
                "22222-222",
                "Novo Bairro do Teste",
                13,
                city.getId()
            )
        ));
        String requestBody = objectMapper.writeValueAsString(customerRequestDTO);

        // Get mocked JWT and return it when decoder gets called
        String tokenString = JwtTestUtil.getToken(customerRequestDTO.getPerson().getEmail());
        Jwt jwt = JwtTestUtil.getJwt(tokenString, customer.getPerson());
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/customers/{id}", customer.getId())
            .header("Authorization", "Bearer " + tokenString)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldReturnNotFoundCustomerUpdateCorrespondingIdDoesNotExist() throws Exception {
        // Update data
        var city = cityRepository.findAll().getLast();
        customerRequestDTO.setCategory(CustomerCategory.ENTREPRENEUR);
        customerRequestDTO.setPerson(new PersonRequestDTO(
            "Nome do Teste",
            "Sobrenome do Teste",
            "222.222.222-22",
            null,
            "(22) 92222-2222",
            null,
            null,
            null,
            new AddressRequestDTO(
                "Nova Rua do Teste",
                "22222-222",
                "Novo Bairro do Teste",
                13,
                city.getId()
            )
        ));
        String requestBody = objectMapper.writeValueAsString(customerRequestDTO);

        // Get mocked JWT and return it when decoder gets called
        String tokenString = JwtTestUtil.getToken(customerRequestDTO.getPerson().getEmail());
        Jwt jwt = JwtTestUtil.getJwt(tokenString, customer.getPerson());
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/customers/{id}", -1L)
            .header("Authorization", "Bearer " + tokenString)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldDeleteCustomerWhenCorrespondingIdExists() throws Exception {
        String tokenString = JwtTestUtil.getToken(customer.getPerson().getEmail());
        Jwt jwt = JwtTestUtil.getJwt(tokenString, customer.getPerson());
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/customers/{id}", customer.getId())
            .header("Authorization", "Bearer " + tokenString)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void shouldReturnNotFoundWhenDeleteCustomerCorrespondingIdDoesNotExist() throws Exception {
        String tokenString = JwtTestUtil.getToken(customer.getPerson().getEmail());
        Jwt jwt = JwtTestUtil.getJwt(tokenString, customer.getPerson());
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/customers/{id}", -1L)
            .header("Authorization", "Bearer " + tokenString)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
