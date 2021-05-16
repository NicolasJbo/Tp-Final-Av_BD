package com.utn.tpFinal.service;

import com.utn.tpFinal.exception.ExceptionDiferentId;
import com.utn.tpFinal.util.PostResponse;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.Tariff;
import com.utn.tpFinal.repository.TariffRepository;
import com.utn.tpFinal.util.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class TariffService {
    private static final  String TARIFF_PATH="tariff";
    @Autowired
    private TariffRepository tariffRepository;

//-------------------------------------------->> BRAND <<--------------------------------------------


    public PostResponse addTariff(Tariff tariff) {
        tariffRepository.save(tariff);
        return PostResponse.builder()
                .status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildUrl(TARIFF_PATH,tariff.getName()))
                .build();

    }

    public Tariff getTariffById(Integer id) {
        return tariffRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public List<Tariff> getAll(){
            return tariffRepository.findAll();
    }

    public void addResidenceToTariff(Residence residence, Tariff tariff) {
        tariff.getResidencesList().add(residence);
        tariffRepository.save(tariff);
    }

    public List<Residence> getResidencesByTariff(Integer idTariff) {
        Tariff tariff = getTariffById(idTariff);
        return tariff.getResidencesList();
    }

    public PostResponse removeTariffById(Integer idTariff) {
        tariffRepository.deleteById(idTariff);
        return PostResponse.builder()
                .status(HttpStatus.OK)
                .url(EntityURLBuilder.buildUrl(TARIFF_PATH))
                .build();
    }

    public Tariff modifyTariff(Tariff tariff)  {
        Tariff tar= getTariffById(tariff.getId());
        tar=tariff;
        return tariffRepository.save(tar);
    }
}
