package br.ifrn.edu.sisconf.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.OptimisticLockingFailureException;

import br.ifrn.edu.sisconf.constants.KeycloakConstants;
import br.ifrn.edu.sisconf.domain.Address;
import br.ifrn.edu.sisconf.domain.City;
import br.ifrn.edu.sisconf.domain.Entrepreneur;
import br.ifrn.edu.sisconf.domain.Person;
import br.ifrn.edu.sisconf.domain.dtos.Entrepreneur.EntrepreneurRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Person.PersonRequestDTO;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationRecord;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationResponse;
import br.ifrn.edu.sisconf.dto.keycloak.UserUpdateRecord;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.EntrepreneurMapper;
import br.ifrn.edu.sisconf.repository.EntrepreneurRepository;
import br.ifrn.edu.sisconf.service.keycloak.KeycloakUserService;
import br.ifrn.edu.sisconf.util.EntrepreneurTestUtil;
import br.ifrn.edu.sisconf.util.PersonTestUtil;

public class EntrepreneurServiceTest {
    @Mock
    private PersonService personService;

    @Mock
    private KeycloakUserService keycloakUserService;

    @Mock
    private EntrepreneurRepository entrepreneurRepository;

    @Mock
    private EntrepreneurMapper entrepreneurMapper;

    @Mock
    private StockService stockService;

    @InjectMocks
    private EntrepreneurService entrepreneurService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Add /me tests

    @Test
    public void shouldCreateEntrepreneurWhenValidDTO() {
        var entrepreneur = EntrepreneurTestUtil.createValidEntrepreneur();
        var entrepreneurCreateRequestDTO = EntrepreneurTestUtil.toValidRequestDTO(entrepreneur);
        var userRegistrationRecord = new UserRegistrationRecord(
            entrepreneurCreateRequestDTO.getPerson().getFirstName(),
            entrepreneurCreateRequestDTO.getPerson().getLastName(),
            entrepreneurCreateRequestDTO.getPerson().getPassword(),
            entrepreneurCreateRequestDTO.getPerson().getEmail(),
            KeycloakConstants.ENTREPRENEUR_GROUP_NAME
            );
        final String keycloakId = UUID.randomUUID().toString();
        entrepreneur.getPerson().setKeycloakId(keycloakId);
        var userRegistrationResponse = new UserRegistrationResponse(
            keycloakId, 
            entrepreneurCreateRequestDTO.getPerson().getFirstName(), 
            entrepreneurCreateRequestDTO.getPerson().getLastName(), 
            entrepreneurCreateRequestDTO.getPerson().getEmail()
        );
        var expectedEntrepreneurResponseDTO = EntrepreneurTestUtil.toResponseDTO(entrepreneur);
        when(keycloakUserService.create(userRegistrationRecord)).thenReturn(userRegistrationResponse);
        when(entrepreneurMapper.toEntity(entrepreneurCreateRequestDTO)).thenReturn(entrepreneur);
        when(entrepreneurRepository.save(entrepreneur)).thenReturn(entrepreneur);
        when(entrepreneurMapper.toResponseDTO(entrepreneur)).thenReturn(expectedEntrepreneurResponseDTO);

        var entrepreneurResponseDTO = entrepreneurService.save(entrepreneurCreateRequestDTO);

        verify(keycloakUserService).create(userRegistrationRecord);
        verify(entrepreneurRepository).save(entrepreneur);
        assertEquals(expectedEntrepreneurResponseDTO, entrepreneurResponseDTO);
        assertNotNull(entrepreneurResponseDTO);
    }

    @Test
    public void shouldRevertEntrepreneurCreationWhenSaveFails() {
        var entrepreneur = EntrepreneurTestUtil.createValidEntrepreneur();
        var entrepreneurCreateRequestDTO = EntrepreneurTestUtil.toValidRequestDTO(entrepreneur);
        var userRegistrationRecord = new UserRegistrationRecord(
            entrepreneurCreateRequestDTO.getPerson().getFirstName(),
            entrepreneurCreateRequestDTO.getPerson().getLastName(),
            entrepreneurCreateRequestDTO.getPerson().getPassword(),
            entrepreneurCreateRequestDTO.getPerson().getEmail(),
            KeycloakConstants.ENTREPRENEUR_GROUP_NAME
            );
        final String keycloakId = UUID.randomUUID().toString();
        entrepreneur.getPerson().setKeycloakId(keycloakId);
        var userRegistrationResponse = new UserRegistrationResponse(
            keycloakId, 
            entrepreneurCreateRequestDTO.getPerson().getFirstName(), 
            entrepreneurCreateRequestDTO.getPerson().getLastName(), 
            entrepreneurCreateRequestDTO.getPerson().getEmail()
        );
        var expectedEntrepreneurResponseDTO = EntrepreneurTestUtil.toResponseDTO(entrepreneur);
        when(keycloakUserService.create(userRegistrationRecord)).thenReturn(userRegistrationResponse);
        when(entrepreneurMapper.toEntity(entrepreneurCreateRequestDTO)).thenReturn(entrepreneur);
        when(entrepreneurRepository.save(entrepreneur)).thenThrow(OptimisticLockingFailureException.class);
        when(entrepreneurMapper.toResponseDTO(entrepreneur)).thenReturn(expectedEntrepreneurResponseDTO);

        assertThrows(
            OptimisticLockingFailureException.class,
            () -> entrepreneurService.save(entrepreneurCreateRequestDTO) 
        );

        verify(keycloakUserService).deleteById(keycloakId);
    }

    @Test
    public void shouldSuccesfullyRemoveEntrepreneurDeleteByIdWhenIdValid() {
        var entrepreneur = Instancio.create(Entrepreneur.class);
        entrepreneur.setId(1L);
        when(entrepreneurRepository.findById(entrepreneur.getId())).thenReturn(
            Optional.of(entrepreneur)
        );

        entrepreneurService.deleteById(entrepreneur.getId(), ""); // Add keycloak valid ID

        verify(entrepreneurRepository, times(1)).deleteById(entrepreneur.getId());
        verify(keycloakUserService, times(1)).deleteById(
            entrepreneur.getPerson().getKeycloakId()
        );
    }

    @Test
    public void shouldNotRemoveEntrepreneurDeleteByIdWhenIdInvalid() {
        when(entrepreneurRepository.findById(-1L)).thenReturn(
            Optional.empty()
        );
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> entrepreneurService.deleteById(-1L, "") // Add test for invalid keycloakID
        );

        assertEquals("Empreendedor com id -1 não existe", exception.getMessage());
        verify(stockService, never()).deleteByEntrepreneurId(-1L);
        verify(entrepreneurRepository, never()).deleteById(-1L);
        verify(keycloakUserService, never()).deleteById(
            null
        );
    }

    @Test
    public void shouldUpdateEntrepreneurWhenIdAndDataValid() {
        var entrepreneur = EntrepreneurTestUtil.createValidEntrepreneur();
        entrepreneur.setId(1L);
        var updateEntrepreneurRequestDTO = EntrepreneurTestUtil.toValidRequestDTO(entrepreneur);

        updateEntrepreneurRequestDTO.getPerson().setEmail(null);
        updateEntrepreneurRequestDTO.getPerson().setPassword(null);
        updateEntrepreneurRequestDTO.getPerson().setPassword2(null);
        updateEntrepreneurRequestDTO.setBusinessName("Novo Nome");
        updateEntrepreneurRequestDTO.getPerson().setFirstName("Novo First Name");
        updateEntrepreneurRequestDTO.getPerson().setLastName("Novo Last Name");
        updateEntrepreneurRequestDTO.getPerson().setCpf("987.654.321-00");
        updateEntrepreneurRequestDTO.getPerson().setCnpj("98.765.432/1000-00");
        updateEntrepreneurRequestDTO.getPerson().setPhone("(22) 92222-2222");
        updateEntrepreneurRequestDTO.getPerson().getAddress().setStreet("Nova Rua");
        updateEntrepreneurRequestDTO.getPerson().getAddress().setNeighbourhood("Nova Vizinhança");
        updateEntrepreneurRequestDTO.getPerson().getAddress().setNumber(100);
        updateEntrepreneurRequestDTO.getPerson().getAddress().setZipCode("22222-222");
        updateEntrepreneurRequestDTO.getPerson().getAddress().setCity(20L);

        var userUpdateRecord = PersonTestUtil.createUserUpdateRecord(
            entrepreneur.getPerson().getKeycloakId(), 
            updateEntrepreneurRequestDTO.getPerson()
        );
        var updatedEntrepreneur = new Entrepreneur(
            updateEntrepreneurRequestDTO.getBusinessName(),
            new Person(
                entrepreneur.getPerson().getKeycloakId(),
                updateEntrepreneurRequestDTO.getPerson().getFirstName(),
                updateEntrepreneurRequestDTO.getPerson().getLastName(),
                entrepreneur.getPerson().getEmail(),
                updateEntrepreneurRequestDTO.getPerson().getCpf(),
                updateEntrepreneurRequestDTO.getPerson().getCnpj(),
                updateEntrepreneurRequestDTO.getPerson().getPhone(),
                new Address(
                    updateEntrepreneurRequestDTO.getPerson().getAddress().getStreet(),
                    updateEntrepreneurRequestDTO.getPerson().getAddress().getZipCode(),
                    updateEntrepreneurRequestDTO.getPerson().getAddress().getNeighbourhood(),
                    updateEntrepreneurRequestDTO.getPerson().getAddress().getNumber(),
                    new City()
                ),
                null,
                null
            ),
            null
        );
        updatedEntrepreneur.getPerson().setEntrepreneur(updatedEntrepreneur);
        updatedEntrepreneur.getPerson().getAddress().getCity().setId(
            updateEntrepreneurRequestDTO.getPerson().getAddress().getCity()
        );
        var expectedResponseDTO = EntrepreneurTestUtil.toResponseDTO(updatedEntrepreneur);

        when(entrepreneurRepository.findById(entrepreneur.getId())).thenReturn(
            Optional.of(entrepreneur)
        );
        when(entrepreneurRepository.save(entrepreneur)).thenReturn(entrepreneur);
        when(entrepreneurMapper.toResponseDTO(entrepreneur)).thenReturn(
            expectedResponseDTO
        );

        var actualResponseDTO = entrepreneurService.update(entrepreneur.getId(), updateEntrepreneurRequestDTO, ""); // keycloakID Valid add test
        
        verify(keycloakUserService, times(1)).update(
            userUpdateRecord
        );
        verify(entrepreneurMapper, times(1)).updateEntityFromDTO(
            updateEntrepreneurRequestDTO, 
            entrepreneur
        );
        assertEquals(expectedResponseDTO, actualResponseDTO);
    }

    @Test
    public void shouldRevertEntrepreneurChangesWhenUpdateFails() {
        var entrepreneur = EntrepreneurTestUtil.createValidEntrepreneur();
        entrepreneur.setId(1L);
        var updateEntrepreneurRequestDTO = EntrepreneurTestUtil.toValidRequestDTO(entrepreneur);

        updateEntrepreneurRequestDTO.getPerson().setEmail(null);
        updateEntrepreneurRequestDTO.getPerson().setPassword(null);
        updateEntrepreneurRequestDTO.getPerson().setPassword2(null);
        updateEntrepreneurRequestDTO.setBusinessName("Novo Nome");
        updateEntrepreneurRequestDTO.getPerson().setFirstName("Novo First Name");
        updateEntrepreneurRequestDTO.getPerson().setLastName("Novo Last Name");
        updateEntrepreneurRequestDTO.getPerson().setCpf("987.654.321-00");
        updateEntrepreneurRequestDTO.getPerson().setCnpj("98.765.432/1000-00");
        updateEntrepreneurRequestDTO.getPerson().setPhone("(22) 92222-2222");
        updateEntrepreneurRequestDTO.getPerson().getAddress().setStreet("Nova Rua");
        updateEntrepreneurRequestDTO.getPerson().getAddress().setNeighbourhood("Nova Vizinhança");
        updateEntrepreneurRequestDTO.getPerson().getAddress().setNumber(100);
        updateEntrepreneurRequestDTO.getPerson().getAddress().setZipCode("22222-222");
        updateEntrepreneurRequestDTO.getPerson().getAddress().setCity(20L);

        var updatedEntrepreneur = new Entrepreneur(
            updateEntrepreneurRequestDTO.getBusinessName(),
            new Person(
                entrepreneur.getPerson().getKeycloakId(),
                updateEntrepreneurRequestDTO.getPerson().getFirstName(),
                updateEntrepreneurRequestDTO.getPerson().getLastName(),
                entrepreneur.getPerson().getEmail(),
                updateEntrepreneurRequestDTO.getPerson().getCpf(),
                updateEntrepreneurRequestDTO.getPerson().getCnpj(),
                updateEntrepreneurRequestDTO.getPerson().getPhone(),
                new Address(
                    updateEntrepreneurRequestDTO.getPerson().getAddress().getStreet(),
                    updateEntrepreneurRequestDTO.getPerson().getAddress().getZipCode(),
                    updateEntrepreneurRequestDTO.getPerson().getAddress().getNeighbourhood(),
                    updateEntrepreneurRequestDTO.getPerson().getAddress().getNumber(),
                    new City()
                ),
                null,
                null
            ),
            null
        );
        updatedEntrepreneur.getPerson().setEntrepreneur(updatedEntrepreneur);
        updatedEntrepreneur.getPerson().getAddress().getCity().setId(
            updateEntrepreneurRequestDTO.getPerson().getAddress().getCity()
        );
        when(entrepreneurRepository.findById(entrepreneur.getId())).thenReturn(
            Optional.of(entrepreneur)
        );
        when(entrepreneurRepository.save(entrepreneur)).thenThrow(OptimisticLockingFailureException.class);
        
        assertThrows(
            OptimisticLockingFailureException.class,
            () -> entrepreneurService.update(entrepreneur.getId(), updateEntrepreneurRequestDTO, "") 
        );

        var oldUserRecord = new UserUpdateRecord(
            entrepreneur.getPerson().getKeycloakId(), 
            entrepreneur.getPerson().getFirstName(), 
            entrepreneur.getPerson().getLastName()
        );

        verify(keycloakUserService).update(oldUserRecord);
    }

    @Test
    public void shouldNotUpdateEntrepreneurWhenIdInvalid() {
        when(entrepreneurRepository.findById(-1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> entrepreneurService.update(
                -1L,
                new EntrepreneurRequestDTO(
                    "Novo Nome",
                    new PersonRequestDTO()
                ),
                "" // Add test for invalid keycloakID
            )
        );

        assertEquals("Empreendedor com id -1 não existe", exception.getMessage());
    }
}
