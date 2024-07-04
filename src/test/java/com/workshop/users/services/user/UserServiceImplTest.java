package com.workshop.users.services.user;

import com.workshop.users.api.controller.Data.DataInitzializerController;
import com.workshop.users.api.controller.Data.DataToUserControllerTesting;
import com.workshop.users.api.dto.Login;
import com.workshop.users.api.dto.UserDto;
import com.workshop.users.exceptions.AuthenticateException;
import com.workshop.users.exceptions.CantCreateCartException;
import com.workshop.users.exceptions.NotFoundUserException;
import com.workshop.users.exceptions.RegisterException;
import com.workshop.users.model.UserEntity;
import com.workshop.users.repositories.CartRepository;
import com.workshop.users.repositories.CartRepositoryImpl;
import com.workshop.users.repositories.CountryDAORepository;
import com.workshop.users.repositories.UserDAORepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserDAORepository userDAORepository;
    CountryDAORepository countryDAORepository;

    private CartRepository cartRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDAORepository = Mockito.mock(UserDAORepository.class);
        countryDAORepository = Mockito.mock(CountryDAORepository.class);
        cartRepository = mock(CartRepository.class);
        userService = new UserServiceImpl(userDAORepository,cartRepository);
    }

    @AfterEach
    void tearDown() {
        DataToMockInUserServiceImplTest.USER_1.setName("Manuel");
        DataToMockInUserServiceImplTest.USER_1.setId(2L);
        DataToMockInUserServiceImplTest.USER_1.setEmail("manuel@example.com");
        DataToMockInUserServiceImplTest.USER_1.setName("Manuel");
        DataToMockInUserServiceImplTest.USER_1.setPassword("2B8sda2?_");
        DataToMockInUserServiceImplTest.USER_1.setLastName("Salamanca");
        DataToMockInUserServiceImplTest.USER_1.setPhone("839234012");
        DataToMockInUserServiceImplTest.USER_1.setBirthDate(UserDto.convertDateToLocalDate("2000/01/14"));
        DataToMockInUserServiceImplTest.USER_1.setFidelityPoints(50);
    }

    @Nested
    @DisplayName("when get user by id")
    class GetUserById {

        @Test
        @DisplayName("givenNonExistingId_whenGetUserById_thenThrowsRunTimeException ")
        void getUserByIdAddingNotExistingUser() {
            //Given
            Mockito.when(userDAORepository.findById(3L))
                    .thenReturn(Optional.empty());
            //When and Then
            assertThrows(NotFoundUserException.class, () -> userService.getUserById(3L));
            verify(userDAORepository).findById(Mockito.anyLong());
        }

        @Test
        @DisplayName("givenNull_whenGetUserById_thenThrowsRunTimeException")
        void getUserByIdAddingNull() {
            //When
            assertThrows(NotFoundUserException.class, () -> userService.getUserById(null));
        }

        @Test
        @DisplayName("givenId_whenGetUserById_thenReturnTheAssociatedUser")
        void getUserById() throws NotFoundUserException {
            //Given
            Mockito.when(userDAORepository.findById(2L))
                    .thenReturn(Optional.of(DataToMockInUserServiceImplTest.USER_1));
            //When
            UserDto user_id_2 = userService.getUserById(2L);

            //Then
            assertEquals("Manuel", user_id_2.getName());
            assertEquals(3L, user_id_2.getAddress().getId());
            assertEquals(1L, user_id_2.getCountry().getId());
            verify(userDAORepository).findById(Mockito.anyLong());
        }



}

@Nested
@DisplayName("When try to update User")
class UpdateUser{
    @Test
    @DisplayName("Given an user to update when update user then return the user dto updated")
    void testUpdateUser() throws Exception {
        UserEntity userEntity = DataToMockInUserServiceImplTest.USER_1;

        UserDto userDtoUpdated = UserDto.builder()
                .name("Manuel updated")
                .lastName("Salamanca updated")
                .password("2B8sda2?_")
                .phone("963258741")
                .email("manuelupdated@example.com")
                .birthDate("2000/01/14")
                .fidelityPoints(0)
                .country(DataToUserControllerTesting.COUNTRY_ESPANYA)
                .address(DataToUserControllerTesting.ADDRESS_CALLE_VARAJAS)
                .build();

        Mockito.when(userDAORepository.findById(2L)).thenReturn(Optional.of(userEntity));
        Mockito.when(userDAORepository.save(any(UserEntity.class))).thenReturn(UserDto.toEntity(userDtoUpdated));
        UserDto updatedUser = userService.updateUser(2L, userDtoUpdated);

        Assertions.assertThat(updatedUser.getName()).isEqualTo("Manuel updated");
        Assertions.assertThat(updatedUser.getLastName()).isEqualTo("Salamanca updated");
        Assertions.assertThat(updatedUser.getEmail()).isEqualTo("manuelupdated@example.com");
        Assertions.assertThat(updatedUser.getBirthDate()).isEqualTo(userDtoUpdated.getBirthDate());
        Assertions.assertThat(updatedUser.getPassword()).isEqualTo("2B8sda2?_");
        Assertions.assertThat(updatedUser.getFidelityPoints()).isZero();
        Assertions.assertThat(updatedUser.getPhone()).isEqualTo("963258741");
        Assertions.assertThat(updatedUser.getAddress()).isNotNull();

        verify(userDAORepository).findById(2L);
        verify(userDAORepository).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Given an user to update that not exists when update user then throw UserNotFoundException")
    void testUpdateUserThrowError()  {

        UserDto userDtoUpdated = UserDto.builder()
                .name("Manuel updated")
                .lastName("Salamanca updated")
                .password("2B8sda2?_")
                .phone("963258741")
                .email("manuelupdated@example.com")
                .birthDate("2000/01/14")
                .fidelityPoints(60)
                .country(DataToUserControllerTesting.COUNTRY_ESPANYA)
                .address(DataToUserControllerTesting.ADDRESS_CALLE_VARAJAS)
                .build();

        Mockito.when(userDAORepository.findById(2L)).thenReturn(Optional.empty());
        Mockito.when(userDAORepository.save(any(UserEntity.class))).thenReturn(UserDto.toEntity(userDtoUpdated));
         assertThatThrownBy(()->userService.updateUser(2L, userDtoUpdated))
                    .isInstanceOf(NotFoundUserException.class)
                    .hasMessage("The user with the id 2 was not found.");

        verify(userDAORepository).findById(2L);
        verify(userDAORepository,times(0)).save(any(UserEntity.class));
    }
}


    @Nested
    @DisplayName("When get user by email")
    class GetUserByEmail{
        @Test
        @DisplayName("Given existed email return good user")
        void getUserByExistingEmail() {
            UserDto userExpected = UserEntity.fromEntity(DataToMockInUserServiceImplTest.USER_1);
            //Given
            when(userDAORepository.findByEmail("manuel@example.com")).thenReturn(Optional.of(DataToMockInUserServiceImplTest.USER_1));
            //When
            UserDto userDto = userService.getUserByEmail("manuel@example.com");
            //Then
            assertThat(userDto).isEqualTo(userExpected);
        }

        @Test
        @DisplayName("Given a non-existent email return null")
        void getUserByNonExistingEmail() {
            //Given
                when(userDAORepository.findByEmail("paquito@perez.com")).thenReturn(Optional.empty());
            //When
            assertThatThrownBy(()->
                userService.getUserByEmail("paquito@perez.com"))
            .isInstanceOf(AuthenticateException.class);
        }


        @Test
        @DisplayName("Given an existing Id then throw a RegisterException")
        void addUserWithExistingId() {
            UserDto userWithExistingId = DataInitzializerController.USER_REGISTERED;
            userWithExistingId.setId(1L);
            when(userDAORepository.findById(userWithExistingId.getId())).thenReturn(Optional.of(new UserEntity()));

            assertThatThrownBy(() -> userService.addUser(userWithExistingId))
                    .isInstanceOf(RegisterException.class)
                    .hasMessage("There's an error registering the user");
        }

    }

    @Test
    @DisplayName("Given an userdto when add user then save the user")
    void addUser()  {
        UserDto userDtoToSave = UserDto.builder()
                .name("Manuel updated")
                .lastName("Salamanca updated")
                .password("2B8sda2?_")
                .phone("963258741")
                .email("manuelupdated@example.com")
                .birthDate("2000/01/14")
                .fidelityPoints(60)
                .country(DataInitzializerController.COUNTRY_SPAIN)
                .address(DataToUserControllerTesting.ADDRESS_CALLE_VARAJAS)
                .build();
        UserDto userDtoAux = userDtoToSave;
        userDtoAux.setId(1L);
        userDtoAux.setFidelityPoints(0);
        when(userDAORepository.save(any(UserEntity.class))).thenReturn(UserDto.toEntity(userDtoAux));
        when(cartRepository.createCart(anyLong())).thenReturn(true);
        UserDto userSaved = userService.addUser(userDtoToSave);

        Assertions.assertThat(userSaved.getName()).isEqualTo("Manuel updated");
        Assertions.assertThat(userSaved.getLastName()).isEqualTo("Salamanca updated");
        Assertions.assertThat(userSaved.getEmail()).isEqualTo("manuelupdated@example.com");
        Assertions.assertThat(Login.BCRYPT.matches(userSaved.getPassword(),userDtoToSave.getPassword())).isTrue();
        Assertions.assertThat(userSaved.getFidelityPoints()).isZero();
        Assertions.assertThat(userSaved.getPhone()).isEqualTo("963258741");
        Assertions.assertThat(userSaved.getAddress()).isNotNull();
    }

    @Test
    @DisplayName("Given an good userdto but have an error creating the cart  when add user then throw an error")
    void addUserCantCreateCartException()  {
        UserDto userDtoToSave = UserDto.builder()
                .name("Manuel updated")
                .lastName("Salamanca updated")
                .password("2B8sda2?_")
                .phone("963258741")
                .email("manuelotherd@example.com")
                .birthDate("2000/01/14")
                .fidelityPoints(60)
                .country(DataInitzializerController.COUNTRY_SPAIN)
                .address(DataToUserControllerTesting.ADDRESS_CALLE_VARAJAS)
                .build();
        UserDto userDtoAux = userDtoToSave;
        userDtoAux.setId(1L);
        userDtoAux.setFidelityPoints(0);
        when(userDAORepository.save(any(UserEntity.class))).thenReturn(UserDto.toEntity(userDtoAux));
        when(cartRepository.createCart(anyLong())).thenThrow(new CantCreateCartException("Error creating a cart"));
        assertThatThrownBy(()->userService.addUser(userDtoToSave))
                .isInstanceOf(CantCreateCartException.class)
                .hasMessage("Error creating a cart");
    }

    @Nested
    @DisplayName("When try to update fildelity points")
    class UpdateFidelityPoints {
        @Test
        @DisplayName("Given a valid user then update fidelity points")
        void updateFidelityPointsTest() throws NotFoundUserException {
            UserEntity userUpdated = DataToMockInUserServiceImplTest.USER_1_UPDATED;
            when(userDAORepository.findById(1L)).thenReturn(Optional.of(DataToMockInUserServiceImplTest.USER_1));
            when(userDAORepository.save(any(UserEntity.class))).thenReturn(userUpdated);

            UserDto userDto = userService.updateFidelityPoints(1L, 70);
            assertThat(userDto.getFidelityPoints()).isEqualTo(120);

        }
        @Test
        @DisplayName("Given a non exist user then throw notFoundUserException")
        void updateFidelityPointsTestThrowNotFoundError() {
            when(userDAORepository.findById(9999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.updateFidelityPoints(9999L, 70))
                    .isInstanceOf(NotFoundUserException.class)
                    .hasMessage("The user with the id 9999 was not found.");

        }

    }



}