package com.reto.usuario.domain.spi.clients;

import com.reto.usuario.domain.model.EmployeeRestaurantClientModel;

public interface IEmployeeRestaurantClientSmallSquare {

    void saveUserEmployeeToARestaurant(EmployeeRestaurantClientModel employeeRestaurantClientRequestDto, String tokenWithPrefixBearer);
}
