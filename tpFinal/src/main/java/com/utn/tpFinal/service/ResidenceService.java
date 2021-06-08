package com.utn.tpFinal.service;

import com.utn.tpFinal.exception.*;
import com.utn.tpFinal.model.*;
import com.utn.tpFinal.model.dto.BillDto;
import com.utn.tpFinal.model.dto.MeasureDto;
import com.utn.tpFinal.model.dto.ResidenceDto;
import com.utn.tpFinal.model.proyection.MeasuresById;
import com.utn.tpFinal.repository.BillRepository;
import com.utn.tpFinal.repository.MeasureRepository;
import com.utn.tpFinal.repository.ResidenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class ResidenceService {
    @Autowired
    private ResidenceRepository residenceRepository;
    @Autowired
    private EnergyMeterService  energyMeterService;
    @Autowired
    private TariffService tariffService;
    @Autowired
    private MeasureRepository measureRepository;
    @Autowired
    private BillRepository billRepository;





   /* @Autowired
    public ResidenceService(ResidenceRepository residenceRepository, EnergyMeterService energyMeterService, TariffService tariffService) {
        this.residenceRepository = residenceRepository;
        this.energyMeterService = energyMeterService;
        this.tariffService = tariffService;
    }*/


//-------------------------------------------->> M E T O D O S <<--------------------------------------------

    public Residence addResidence(Residence residence) {
        return  residenceRepository.save(residence);
    }

    public Page<ResidenceDto> getAll(Specification<Residence> residenceSpecification,Integer page,Integer size,List<Order> orders) throws NoContentException {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<Residence> residences = residenceRepository.findAll(residenceSpecification,pageable);

        Page<ResidenceDto> dtoPage= Page.empty();

      if(!residences.isEmpty())
         dtoPage=residences.map(r -> ResidenceDto.from(r));


        return dtoPage;
    }

    public Residence getResidenceById(Integer id) throws ResidenceNotExists {
        return residenceRepository.findById(id)
                .orElseThrow(() -> new ResidenceNotExists(this.getClass().getSimpleName(), "getResidenceById"));
    }

    public Page<Residence> getResidenceByClientId(Integer idClient, Pageable pageable) {
        return residenceRepository.findByClientId(idClient, pageable);
    }

    public Page<Residence> getResidencesByTariffId(Integer idTariff, Pageable pageable) {
        return residenceRepository.findByTariffId(idTariff, pageable);
    }

    public void addClientToResidence(Client client, Residence residence) {
        residence.setClient(client);
        residenceRepository.save(residence);
    }

    public void addEnergyMeterToResidence(Integer idResidence, Integer idEnergyMeter) throws ResidenceNotExists, EnergyMeterNotExists, ResidenceDefined {

        EnergyMeter energyMeter = energyMeterService.getEnergyMeterById(idEnergyMeter);
        if(energyMeter.getResidence() != null) { //si el medidor ya tiene un domicilio
            throw new ResidenceDefined(this.getClass().getSimpleName(), "addEnergyMeterToResidence");
        }
        Residence residence = getResidenceById(idResidence);
        energyMeterService.addResidenceToMeter(residence, energyMeter);
        residence.setEnergyMeter(energyMeter);
        residenceRepository.save(residence);
    }

    public void addTariffToResidence(Integer idResidence, Integer idTariff) throws ResidenceNotExists, TariffNotExists {
        Residence residence = getResidenceById(idResidence);
        //retrive the Tariff to add at the residence
        Tariff tariff= tariffService.getTariffById(idTariff);
        //Add residence to Tariff
        tariffService.addResidenceToTariff(residence,tariff);
        //add the meter to the residence
        residence.setTariff(tariff);
        residenceRepository.save(residence);
    }

    public void removeResidenceById(Integer idResidence) throws ResidenceNotExists {
        if(!residenceRepository.existsById(idResidence))
            throw new ResidenceNotExists(this.getClass().getSimpleName(),"removeResidenceById" );

        residenceRepository.deleteById(idResidence);
    }

    public Residence modifyResidence(Integer idResidence, Residence residence) throws ResidencesDoNotMatch, ResidenceNotExists {
        Residence r=getResidenceById(idResidence);

        if(r.getId() != residence.getId())
            throw new ResidencesDoNotMatch(this.getClass().getSimpleName(),"modifyResidence" );
        r=residence;
        return  residenceRepository.save(r);
    }

    public Residence getResidenceByEnergyMeterId(Integer id){
       return residenceRepository.findByEnergyMeterId(id);
    }

    public Page<MeasureDto> getResidenceMeasuresByDates(Integer idResidence, Date from, Date to, Integer page,Integer size,List<Order> orders) throws NoContentException {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<Measure> measures = measureRepository.findByResidenceIdAndDateBetween(idResidence, from, to, pageable);

        if(measures.isEmpty())//TODO muestra mal
            throw new NoContentException(this.getClass().getSimpleName(), "getClientUnpaidBillsByResidence");

        Page<MeasureDto> measuresdto = measures.map(m -> MeasureDto.from(m));
        return measuresdto;
    }


    public Page<BillDto> getResidenceUnpaidBills(Integer idResidence, Integer page, Integer size, List<Order> orders) throws ResidenceNotExists, NoContentException {

        if(!residenceRepository.existsById(idResidence))
            throw new ResidenceNotExists(this.getClass().getSimpleName(), "getResidenceUnpaidBills");

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<Bill> bills = billRepository.findByIsPaidFalseAndResidenceId(idResidence, pageable);

        if(bills.isEmpty())
            throw new NoContentException(this.getClass().getSimpleName(), "getClientUnpaidBills");

        Page<BillDto> billsDto = bills.map(b-> BillDto.from(b));

        return billsDto;
    }
}
