package com.reto.usuario.domain.model;

public class EmployeeRestaurantClientModel {

    private Long idUserEmployee;
    private Long idRestaurant;

    public EmployeeRestaurantClientModel() {
    }

    public EmployeeRestaurantClientModel(Long idUserEmployee, Long idRestaurant) {
        this.idUserEmployee = idUserEmployee;
        this.idRestaurant = idRestaurant;
    }

    public Long getIdUserEmployee() {
        return idUserEmployee;
    }

    public void setIdUserEmployee(Long idUserEmployee) {
        this.idUserEmployee = idUserEmployee;
    }

    public Long getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(Long idRestaurant) {
        this.idRestaurant = idRestaurant;
    }
}
