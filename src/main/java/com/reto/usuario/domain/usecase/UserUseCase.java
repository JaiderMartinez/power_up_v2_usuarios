package com.reto.usuario.domain.usecase;

import com.reto.usuario.domain.api.IUserUseCasePort;
import com.reto.usuario.domain.exceptions.EmailExistsException;
import com.reto.usuario.domain.exceptions.EmptyFieldsException;
import com.reto.usuario.domain.exceptions.InvalidCellPhoneFormatException;
import com.reto.usuario.domain.exceptions.InvalidEmailFormatException;
import com.reto.usuario.domain.exceptions.RolNotFoundException;
import com.reto.usuario.domain.model.RolModel;
import com.reto.usuario.domain.model.UserModel;
import com.reto.usuario.domain.spi.IRolPersistenceDomainPort;
import com.reto.usuario.domain.spi.IUserPersistenceDomainPort;
import com.reto.usuario.domain.utils.PasswordEncoderUtils;

public class UserUseCase implements IUserUseCasePort {

    private final IUserPersistenceDomainPort userPersistenceDomainPort;
    private final IRolPersistenceDomainPort rolPersistenceDomainPort;

    public UserUseCase(IUserPersistenceDomainPort userPersistenceDomainPort,
                       IRolPersistenceDomainPort rolesPersistenceDomainPort) {
        this.userPersistenceDomainPort = userPersistenceDomainPort;
        this.rolPersistenceDomainPort = rolesPersistenceDomainPort;
    }

    @Override
    public void registerUserWithOwnerRole(UserModel userModel) {
        restrictionsWhenSavingAUser(userModel);
        userModel.setPassword(PasswordEncoderUtils.passwordEncoder().encode(userModel.getPassword()));
        RolModel rol = rolPersistenceDomainPort.findByName("PROPIETARIO");
        userModel.setRol(rol);
        userPersistenceDomainPort.saveUser(userModel);
    }

    @Override
    public void registerUserWithEmployeeRole(UserModel userModel) {
        restrictionsWhenSavingAUser(userModel);
        userModel.setPassword(PasswordEncoderUtils.passwordEncoder().encode(userModel.getPassword()));
        RolModel rolModel = rolPersistenceDomainPort.findByIdRol(userModel.getRol().getIdRol());
        if(rolModel == null) {
            throw new RolNotFoundException("The rol not found");
        } else if(!rolModel.getName().equals("EMPLEADO") ) {
            throw new RolNotFoundException("The rol is different from employee");
        }
        userModel.setRol(rolModel);
        userPersistenceDomainPort.saveUser(userModel);
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
        return userPersistenceDomainPort.findByEmail(email);
    }
}
