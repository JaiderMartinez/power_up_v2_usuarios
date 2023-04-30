package com.reto.usuario.domain.usecase;

import com.reto.usuario.domain.api.IUserUseCasePort;
import com.reto.usuario.domain.dto.AuthCredentials;
import com.reto.usuario.domain.exceptions.EmailExistsException;
import com.reto.usuario.domain.exceptions.EmptyFieldsException;
import com.reto.usuario.domain.exceptions.InvalidCellPhoneFormatException;
import com.reto.usuario.domain.exceptions.InvalidEmailFormatException;
import com.reto.usuario.domain.model.UserModel;
import com.reto.usuario.domain.spi.IRolPersistenceDomainPort;
import com.reto.usuario.domain.spi.IUserPersistenceDomainPort;
import com.reto.usuario.domain.utils.PasswordEncoderUtils;
import com.reto.usuario.domain.utils.TokenUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

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
        userModel.setRol(rolPersistenceDomainPort.findByNombre("PROPIETARIO"));
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
        if( user.getIdentificationDocument() == null || user.getName().replaceAll(" ", "").isEmpty() ||
                user.getLastName().replaceAll(" ", "").isEmpty() ||
                user.getCellPhone().replaceAll(" ", "").isEmpty() ||
                user.getEmail().replaceAll(" ", "").isEmpty() ||
                user.getPassword().replaceAll(" ", "").isEmpty() ) {
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
        String phoneUserNoSpaces = phoneUser.replaceAll(" ", "");
        if(phoneUserNoSpaces.startsWith("+")){
            if(!phoneUserNoSpaces.matches("\\+\\d+") || phoneUserNoSpaces.length() > 13 ) {
                throw new InvalidCellPhoneFormatException("The cell phone format is wrong");
            }
        } else {
            if (phoneUserNoSpaces.length() != 10 || !phoneUserNoSpaces.matches("\\d")) {
                throw new InvalidCellPhoneFormatException("The cell phone format is wrong");
            }
        }
    }

    @Override
    public String signInUseCase(AuthCredentials authCredentials) {
        UserModel user = findUsuarioByEmail( authCredentials.getEmail() );
        List<String> authority = new ArrayList<>();
        authority.add("ROLE_" + user.getRol().getName());
        return TokenUtils
                .createToken( user.getEmail(), authority, user.getName(), user.getLastName() );
    }

    @Override
    public UserModel findUsuarioByEmail(String email) {
        return userPersistenceDomainPort.findByEmail(email);
    }
}
