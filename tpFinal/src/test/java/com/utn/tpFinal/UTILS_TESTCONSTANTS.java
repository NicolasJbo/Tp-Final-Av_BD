package com.utn.tpFinal;



import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.dto.*;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UTILS_TESTCONSTANTS {

    public static List<Sort.Order> getOrders(String field1, String field2){
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, field1));
        orders.add(new Sort.Order(Sort.Direction.ASC, field2));

        return orders;
    }
    public static List getGrandAuthorityClient(){
        List list = new ArrayList<SimpleGrantedAuthority>();
        list.add(new SimpleGrantedAuthority("CLIENT"));
        return list;
    }
    public static List getGrandAuthorityInvalid(){
        List list = new ArrayList<SimpleGrantedAuthority>();
        list.add(new SimpleGrantedAuthority("INVALID"));
        return list;
    }
    //--------------------------------------- D T O 's-------------------------------------------------------------------------------
    public static UserDto getUserDto(Integer idClient){
        UserDto userDto = UserDto.builder().id(idClient).mail("carlos@gmail.com").build();
        return userDto;
    }

    public static List<ResidenceDto> getResidendesDTO_List(){

        EnergyMeterDto energyMeterDto = EnergyMeterDto.builder().brandName("brand1").serialNumber("001").modelName("model1").passWord("1234").build();
        EnergyMeterDto energyMeterDto2 = EnergyMeterDto.builder().brandName("brand1").serialNumber("002").modelName("model1").passWord("1234").build();

        ResidenceDto residenceDto =ResidenceDto.builder().street("calle1").number(123).client("nicolas").energyMeter(energyMeterDto).id(1).build();
        ResidenceDto residenceDto2 =ResidenceDto.builder().street("calle2").number(222).client("lautaro").energyMeter(energyMeterDto2).id(2).build();

        List<ResidenceDto> residences = new ArrayList<ResidenceDto>();
        residences.add(residenceDto);
        residences.add(residenceDto2);

        return residences;
    }
    public static List<BillDto> getBillsDTO_List(){
        BillDto billDto1 = BillDto.builder().billCode(111).finalAmount(120.3F).residence("casa1").build();
        BillDto billDto2 = BillDto.builder().billCode(222).finalAmount(120.3F).residence("casa2").build();

        List<BillDto> list = new ArrayList<BillDto>();
        list.add(billDto1);

        list.add(billDto2);
        return list;
    }
    public static List<MeasureDto> getMeasureDTO_List()  {
        MeasureDto measureDto1= MeasureDto.builder().kw(100F).serialNumber("serial1").password("1111").price(150).date("2020-02-02").build();
        MeasureDto measureDto2= MeasureDto.builder().kw(200F).serialNumber("serial2").password("2222").price(120).date("2020-03-02").build();

        List<MeasureDto> list = new ArrayList<MeasureDto>();
        list.add(measureDto1);
        list.add(measureDto2);

        return list;
    }

    public static ClientDto getClientDTO(Integer id){
        ClientDto client1 = ClientDto.builder().client("Carlos").dni("11111111").birthday("2020-02-02").build();
        ClientDto client2 = ClientDto.builder().client("Mati").dni("22222222").birthday("2020-02-02").build();
        ClientDto rta= new ClientDto();
        switch (id){
            case 1:
                rta= client1;
                break;
            case 2:
                rta= client2;
                break;
        }
        return rta;
    }
    //---------------------------------------------------------F E C H A S -------------------------------------------------------

    public static Date getFecha(Integer id) throws ParseException {
        Date fecha1 =  new SimpleDateFormat("yyyy-MM-dd").parse("2020-02-02");
        Date fecha2 =  new SimpleDateFormat("yyyy-MM-dd").parse("2020-03-02");
        Date rta=new Date();
        switch (id){
            case 1:
                rta= fecha1;
                break;
            case 2:
                rta= fecha2;
                break;
        }
        return rta;
    }
    //-----------------------------------------------------------------------------------------------------------------------------

    public static Client getClient(Integer idClient) throws ParseException {
        Client client=  Client.builder().id(1).name("Carlos").lastName("Perez").birthday(getFecha(1)).dni("1111111").build();

        return client;
    }

}
