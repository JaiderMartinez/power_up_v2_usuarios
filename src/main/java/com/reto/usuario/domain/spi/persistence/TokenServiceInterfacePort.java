package com.reto.usuario.domain.spi.persistence;

import java.util.List;

public interface TokenServiceInterfacePort {

    String generateTokenAccess(String email, List<String> authority, String nameFromUser, String lastNameFromUser);
}
