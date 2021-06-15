package com.utn.tpFinal;



import com.utn.tpFinal.model.*;
import com.utn.tpFinal.model.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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

    public static List getGrandAuthorityEmployee(){
        List list = new ArrayList<SimpleGrantedAuthority>();
        list.add(new SimpleGrantedAuthority("EMPLOYEE"));
        return list;
    }

    public static List getGrandAuthorityInvalid(){
        List list = new ArrayList<SimpleGrantedAuthority>();
        list.add(new SimpleGrantedAuthority("INVALID"));
        return list;
    }

    public static  List<MeterBrand>getMeterBrand_List(){
        List<EnergyMeter>asd=new ArrayList<>();

        EnergyMeter e = EnergyMeter.builder()
                .model(MeterModel.builder().id(1).name("model1").energyMeters(asd).build())
                .brand(MeterBrand.builder().id(1).name("brand1").energyMeters(asd).build())
                .passWord("1234")
                .serialNumber("001")
                .id(1)
                .build();
        asd.add(e);
        List<MeterBrand> list = new ArrayList<>();
        list.add(MeterBrand.builder().id(1).name("brand1").energyMeters(asd).build());
        list.add(MeterBrand.builder().id(2).name("brand2").energyMeters(asd).build());
        return list;
    }

    public static  List<MeterModel>getMeterModel_List(){
        List<EnergyMeter>asd=new ArrayList<>();

        EnergyMeter e = EnergyMeter.builder()
                .model(MeterModel.builder().id(1).name("model1").energyMeters(asd).build())
                .brand(MeterBrand.builder().id(1).name("brand1").energyMeters(asd).build())
                .passWord("1234")
                .serialNumber("001")
                .id(1)
                .build();

        asd.add(e);
        List<MeterModel> list = new ArrayList<>();
        list.add(MeterModel.builder().id(1).name("model1").energyMeters(asd).build());
        list.add(MeterModel.builder().id(2).name("model2").energyMeters(asd).build());
        return list;
    }

    public static EnergyMeter getEnergyMeter(Integer id){
        return EnergyMeter.builder()
                .model(getMeterModel_List().get(0))
                .brand(getMeterBrand_List().get(0))
                .passWord("1234")
                .serialNumber("001")
                .id(id)
                .build();
    }

    public static Measure getMeasure(Integer id) throws ParseException {
        return Measure.builder()
                .id(id)
                .kw(100F)
                .date(getFecha(1))
                .price(100F)
                .build();
    }

    public static MeasureSenderDto getMeasureSenderDto() throws ParseException {
        return MeasureSenderDto.builder()
                .kw(100F)
                .date("2021-06-02 12:12:12")
                .password("1234")
                .serialNumber("001")
                .build();
    }
    public static  List<Tariff> getTariff_List(){
        Tariff tariff1= getTariff(1);
        Tariff tariff2=getTariff(2);
        List<Tariff> tariffList=new ArrayList<>();
        tariffList.add(tariff1);
        tariffList.add(tariff2);

        return  tariffList;
    }


    //--------------------------------------- D T O 's-------------------------------------------------------------------------------
    public static RegisterDto getRegisterDTO() throws ParseException {
        return RegisterDto.builder().dni("11111111")
                .name("carlos")
                .lastName("apellido")
                .password("123")
                .birthday(getFecha(1))
                .mail("mail@gmail.com").build();
    }
    public static UserDto getUserDto(Integer idClient){
        UserDto userDto = UserDto.builder().id(idClient).mail("carlos@gmail.com").isClient(true).build();
        return userDto;
    }

    public static UserDto getUserDtoEmployee(Integer idClient){
        UserDto userDto = UserDto.builder().id(idClient).mail("admin@gmail.com").isClient(false).build();
        return userDto;
    }

    public static List<Residence> getResidenceList() throws ParseException {
        List<Residence>residences = new ArrayList<>();

        residences.add(getResidence(4));
        residences.add(getResidence(6));

        return residences;

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
    public static  List<TariffDto> getTariffDTO_List(){
        TariffDto tariffDto1=TariffDto.builder().tariff("A1").amount(Float.valueOf(10)).build();
        TariffDto tariffDto2=TariffDto.builder().tariff("b2").amount(Float.valueOf(20)).build();
        List<TariffDto> tariffDtoList=new ArrayList<>();
        tariffDtoList.add(tariffDto1);
        tariffDtoList.add(tariffDto2);

        return  tariffDtoList;
    }
    public static  List<EnergyMeterDto> getEnergyMeterDTO_List(){
        EnergyMeterDto energyMeterDto1= EnergyMeterDto.builder().brandName("marca1").modelName("model1").serialNumber("A1").passWord("111").build();
        EnergyMeterDto energyMeterDto2= EnergyMeterDto.builder().brandName("marca2").modelName("model2").serialNumber("B2").passWord("222").build();
        List<EnergyMeterDto> energyMeterDtoList=new ArrayList<>();
        energyMeterDtoList.add(energyMeterDto1);
        energyMeterDtoList.add(energyMeterDto2);
        return energyMeterDtoList;
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

    public static LoginRequestDto getLoginRequestDTO(){
        return LoginRequestDto.builder().mail("mail@gmail.com")
                .password("1234")
                .build();
    }
    public static  LoginResponseDto getLoginResponse(){
        return LoginResponseDto.builder().token("123abc").build();
    }

    public static  User getUser(Integer idUser) throws ParseException {
        return User.builder()
                .id(idUser)
                .mail("mail1@gmail.com")
                .password("123")
                .isClient(false)
                .client(getClient(1))
                .build();
    }
    public static  User getUserCLient() throws ParseException {
        return User.builder().id(1)
                .mail("mail1@gmail.com")
                .password("123")
                .isClient(true)
                .client(getClient(1))
                .build();
    }
    //---------------------------------------------------------F E C H A S -------------------------------------------------------

    public static Date getFecha(Integer num) throws ParseException {
        Date fecha1 =  new SimpleDateFormat("yyyy-MM-dd").parse("2020-02-02");
        Date fecha2 =  new SimpleDateFormat("yyyy-MM-dd").parse("2020-03-02");
        Date rta=new Date();
        switch (num){
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
        Client client=  Client.builder()
                    .id(idClient)
                    .name("Carlos")
                    .lastName("Perez")
                    .birthday(getFecha(1))
                    .dni("1111111")
                    .residencesList(Collections.emptyList())
                    .build();

        return client;
    }

    public static Tariff getTariff(Integer idTariff) {
        return Tariff.builder().id(idTariff)
                    .name("TariffExample")
                    .amount(50F)
                    .build();
    }

    public static Residence getResidence(Integer idResidence) throws ParseException {
        return Residence.builder()
                .id(idResidence)
                .number(1234)
                .client(getClient(1))
                .energyMeter(getEnergyMeter(idResidence))
                .street("Siempre Viva")
                .bills(Collections.emptyList())
                .floor(String.valueOf(idResidence+4))
                .apartament("B")
                .tariff(getTariff(idResidence))
                .build();

    }

    public static Bill getBill(Integer idBill) throws ParseException {
        return Bill.builder()
                .id(idBill)
                .isPaid(false)
                .energyMeter(getEnergyMeter(idBill))
                .finalAmount(1500F)
                .initialDate(getFecha(1))
                .initialMedition(5F)
                .finalDate(getFecha(2))
                .finalMedition(8F)
                .totalEnergy(350F)
                .expirationDate(getFecha(2))
                .tariff(getTariff(idBill))
                .residence(getResidence(idBill))
                .build();
    }
}
