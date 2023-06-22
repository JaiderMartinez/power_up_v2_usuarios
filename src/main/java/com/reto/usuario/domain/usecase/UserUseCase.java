package com.reto.usuario.domain.usecase;

import com.reto.usuario.domain.api.IUserUseCasePort;
import com.reto.usuario.domain.model.EmployeeRestaurantClientModel;
import com.reto.usuario.domain.exceptions.EmailExistsException;
import com.reto.usuario.domain.exceptions.EmptyFieldsException;
import com.reto.usuario.domain.exceptions.InvalidCellPhoneFormatException;
import com.reto.usuario.domain.exceptions.InvalidEmailFormatException;
import com.reto.usuario.domain.exceptions.RolNotFoundException;
import com.reto.usuario.domain.exceptions.UserNotFoundException;
import com.reto.usuario.domain.spi.clients.IEmployeeRestaurantClientSmallSquare;
import com.reto.usuario.domain.model.RolModel;
import com.reto.usuario.domain.model.UserModel;
import com.reto.usuario.domain.spi.persistence.IRolPersistenceDomainPort;
import com.reto.usuario.domain.spi.persistence.IUserPersistenceDomainPort;
import com.reto.usuario.domain.exceptions.EmailNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserUseCase implements IUserUseCasePort {

    private final IUserPersistenceDomainPort userPersistenceDomainPort;
    private final IRolPersistenceDomainPort rolPersistenceDomainPort;
    private final PasswordEncoder passwordEncoder;
    private final IEmployeeRestaurantClientSmallSquare employeeRestaurantClientSmallSquare;
    private static final String ROLE_OWNER = "PROPIETARIO";
    private static final String ROLE_EMPLOYEE = "EMPLEADO";
    private static final String ROLE_CUSTOMER = "CLIENTE";

    public UserUseCase(IUserPersistenceDomainPort userPersistenceDomainPort, IRolPersistenceDomainPort rolesPersistenceDomainPort,
                       PasswordEncoder passwordEncoder, IEmployeeRestaurantClientSmallSquare employeeRestaurantClientSmallSquare) {
        this.userPersistenceDomainPort = userPersistenceDomainPort;
        this.rolPersistenceDomainPort = rolesPersistenceDomainPort;
        this.passwordEncoder = passwordEncoder;
        this.employeeRestaurantClientSmallSquare = employeeRestaurantClientSmallSquare;
    }

    @Override
    public UserModel registerUserWithOwnerRole(UserModel userModel) {
        restrictionsWhenSavingAUser(userModel);
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        final RolModel rolOwnerFound = this.rolPersistenceDomainPort.findByName(ROLE_OWNER);
        userModel.setRol(rolOwnerFound);
        return this.userPersistenceDomainPort.saveUser(userModel);
    }

    @Override
    public UserModel registerUserWithEmployeeRole(UserModel userModel, String tokenWithBearerPrefix, Long idRestaurant) {
        restrictionsWhenSavingAUser(userModel);
        final RolModel rolEmployeeFound = findRoleByIdAndCompareRoleName(ROLE_EMPLOYEE, userModel.getRol().getIdRol());
        userModel.setPassword(this.passwordEncoder.encode(userModel.getPassword()));
        userModel.setRol(rolEmployeeFound);
        final UserModel resultWhenSaveAnEmployeeUser = this.userPersistenceDomainPort.saveUser(userModel);
        EmployeeRestaurantClientModel employeeRestaurantRequestDto = new EmployeeRestaurantClientModel(resultWhenSaveAnEmployeeUser.getIdUser(), idRestaurant);
        this.employeeRestaurantClientSmallSquare.saveUserEmployeeToARestaurant(employeeRestaurantRequestDto, tokenWithBearerPrefix);
        return resultWhenSaveAnEmployeeUser;
    }

    @Override
    public UserModel registerUserWithCustomerRole(UserModel userCustomerRequest) {
        restrictionsWhenSavingAUser(userCustomerRequest);
        final RolModel roleCustomerFound = findRoleByIdAndCompareRoleName(ROLE_CUSTOMER, userCustomerRequest.getRol().getIdRol());
        userCustomerRequest.setPassword(this.passwordEncoder.encode(userCustomerRequest.getPassword()));
        userCustomerRequest.setRol(roleCustomerFound);
        return this.userPersistenceDomainPort.saveUser(userCustomerRequest);
    }

    private RolModel findRoleByIdAndCompareRoleName(String roleName, Long idRol) {
        RolModel rolModel = this.rolPersistenceDomainPort.findByIdRol(idRol);
        if(rolModel == null) {
            throw new RolNotFoundException("The rol not found");
        } else if(!rolModel.getName().equals(roleName) ) {
            throw new RolNotFoundException("The rol is different to the requested");
        }
        return rolModel;
    }

    private void restrictionsWhenSavingAUser(UserModel userModel) {
        if(userPersistenceDomainPort.existsByEmail(userModel.getEmail())) {
            throw new EmailExistsException("The email " + userModel.getEmail()  + " already exists");
        }
        validateEmailStructure(userModel.getEmail());
        validateUserFieldsEmpty(userModel);
        validateUserCellPhone(userModel.getCellPhone());
    }

    private void validateUserFieldsEmpty(UserModel user) {
        if( user.getIdentificationDocument() == null || user.getName().replace(" ", "").isEmpty() ||
                user.getLastName().replace(" ", "").isEmpty() ||
                user.getCellPhone().replace(" ", "").isEmpty() ||
                user.getEmail().replace(" ", "").isEmpty() ||
                user.getPassword().replace(" ", "").isEmpty() ) {
            throw new EmptyFieldsException("Fields cannot be empty");
        }
    }

    private void validateEmailStructure(String email) {
        int atPosition = email.lastIndexOf( '@' );
        int dotPosition = email.lastIndexOf( '.' );
        if( email.endsWith(".") || atPosition == -1 || dotPosition == -1 ||
                dotPosition < atPosition ) {
            throw new InvalidEmailFormatException("Wrong email structure");
        }

    }

    private void validateUserCellPhone(String phoneUser) {
        String phoneUserNoSpaces = phoneUser.replace(" ", "");
        if(phoneUserNoSpaces.startsWith("+")){
            if(!phoneUserNoSpaces.matches("\\+\\d+") || phoneUserNoSpaces.length() != 13 ) {
                throw new InvalidCellPhoneFormatException("The cell phone format is wrong");
            }
        } else {
            if (phoneUserNoSpaces.length() != 10 || !phoneUserNoSpaces.matches("\\d+")) {
                throw new InvalidCellPhoneFormatException("The cell phone format is wrong");
            }
        }
    }

    @Override
    public UserModel findUserByEmail(String email) {
        UserModel userModel = userPersistenceDomainPort.findByEmail(email);
        if (userModel == null) {
            throw new EmailNotFoundException("Email not found");
        }
        return userModel;
    }

    @Override
    public UserModel getUserById(Long idUser) {
        UserModel userModel = userPersistenceDomainPort.findById(idUser);
        if(userModel == null) {
            throw new UserNotFoundException("User not found");
        }
        return userModel;
    }
}

