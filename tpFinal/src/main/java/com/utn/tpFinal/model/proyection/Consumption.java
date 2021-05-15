package com.utn.tpFinal.model.proyection;


import org.springframework.beans.factory.annotation.Value;

public interface Consumption {
    @Value("#{target.nameClient + ' ' + target.lastnameClient}")
    String getClient();
    Float getTotalEnergy();
    Float getTotalAmount();
}
