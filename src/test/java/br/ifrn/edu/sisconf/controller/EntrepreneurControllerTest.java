package br.ifrn.edu.sisconf.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import br.ifrn.edu.sisconf.domain.Entrepreneur;
import br.ifrn.edu.sisconf.domain.dtos.AddressRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Entrepreneur.EntrepreneurRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Person.PersonRequestDTO;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationRecord;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationResponse;
import br.ifrn.edu.sisconf.mapper.EntrepreneurMapper;
import br.ifrn.edu.sisconf.repository.CityRepository;
import br.ifrn.edu.sisconf.repository.EntrepreneurRepository;
import br.ifrn.edu.sisconf.service.keycloak.KeycloakUserService;
import br.ifrn.edu.sisconf.util.JwtTestUtil;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(SecurityTestConfig.class)
public class EntrepreneurControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntrepreneurRepository entrepreneurRepository;

    @Autowired
    private EntrepreneurMapper entrepreneurMapper;

    @MockitoBean
    private KeycloakUserService keycloakUserService;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private CityRepository cityRepository;

    private EntrepreneurRequestDTO entrepreneurRequestDTO;

    private Entrepreneur entrepreneur;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        entrepreneurRequestDTO = new EntrepreneurRequestDTO(
            "Inovação Global Tecnológica e Consultoria Avançada" +
            " para Soluções Inteligentes em Desenvolvimento Sustentável" +
            " e Tecnologia Integr",
            new PersonRequestDTO(
                "k",
                "g",
                "111.111.111-11",
                "11.111.111/1111-11",
                "(11) 91111-1111",
                "abcd1234",
                "abcd1234",
                "teste@gmail.com",
                new AddressRequestDTO(
                    "Rua dos Jacarandás Floridos do Bairro" +
                    " das Águas Claras em Comemoração ao Centenário" +
                    " da Fundação da Cidade de Esperança",
                    "59911-111",
                    "Residencial das Palmeiras Altas e Horizontes" +
                    " Verdejantes do Vale Encantado em Celebração às" +
                    " Belezas Naturais da Região Serrana",
                    10,
                    cityRepository.findAll().getFirst().getId()
                )
            )
        );
        entrepreneur = entrepreneurMapper.toEntity(entrepreneurRequestDTO);
        entrepreneur.getPerson().setKeycloakId(UUID.randomUUID().toString());
        entrepreneur = entrepreneurRepository.save(entrepreneur);
    }

    @AfterEach
    public void tearDown() {
        entrepreneurRepository.deleteAll();
    }

    @Test
    public void meShouldReturnLoggedEntrepreneurWhenValidKeycloakIdAndToken() throws Exception {
        String testToken = JwtTestUtil.getToken(entrepreneur.getPerson().getEmail());
        Jwt jwt = JwtTestUtil.getJwt(testToken, entrepreneur.getPerson());
        String entrepreneurData = objectMapper.writeValueAsString(
            entrepreneurMapper.toResponseDTO(entrepreneur)
        );
        when(jwtDecoder.decode(testToken)).thenReturn(jwt);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/entrepreneurs/me")
                .header("Authorization", "Bearer " + testToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(entrepreneurData));
    }

    @Test
    public void meShouldReturnNotFoundWhenValidTokenButEntrepreneurDoesNotExist() throws Exception {
        entrepreneur.getPerson().setKeycloakId("");
        String testToken = JwtTestUtil.getToken(entrepreneur.getPerson().getEmail());
        Jwt jwt = JwtTestUtil.getJwt(testToken, entrepreneur.getPerson());
        when(jwtDecoder.decode(testToken)).thenReturn(jwt);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/entrepreneurs/me")
                .header("Authorization", "Bearer " + testToken))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldCreateEntrepreneurWhenValidRequestData() throws Exception {
        entrepreneurRepository.deleteAll();
        String requestBody = objectMapper.writeValueAsString(entrepreneurRequestDTO);
        when(keycloakUserService.create(new UserRegistrationRecord(
                entrepreneurRequestDTO.getPerson().getFirstName(), 
                entrepreneurRequestDTO.getPerson().getLastName(), 
                entrepreneurRequestDTO.getPerson().getPassword(), 
                entrepreneurRequestDTO.getPerson().getEmail(), 
                KeycloakConstants.ENTREPRENEUR_GROUP_NAME
            )))
            .thenReturn(new UserRegistrationResponse(
                    UUID.randomUUID().toString(), 
                    entrepreneurRequestDTO.getPerson().getFirstName(), 
                    entrepreneurRequestDTO.getPerson().getLastName(), 
                    entrepreneurRequestDTO.getPerson().getEmail()
                ));
        mockMvc.perform(MockMvcRequestBuilders.post(
            "/api/entrepreneurs", 
            new ArrayList<>()
            )
            .contentType("application/json")
            .content(requestBody)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        verify(keycloakUserService, times(1)).create(
            any(UserRegistrationRecord.class)
        );
    }
}
