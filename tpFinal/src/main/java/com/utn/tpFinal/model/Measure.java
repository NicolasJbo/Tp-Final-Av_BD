package com.utn.tpFinal.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.utn.tpFinal.model.dto.MeasureDto;
import com.utn.tpFinal.model.dto.MeasureSenderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name ="measures")
public class Measure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Date MUST be completed.")
    @Past(message = "INVALID Date.")
    private Date date;

    @Positive(message = "Total Must be positive number")
    private Float kw;

    @Positive(message = "Total Must be positive number")
    private Float price;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name="id_residence")
    private Residence residence;

    //Factura 1tMany NULL NO FACTURADO

    public static Measure from(MeasureSenderDto measureSenderDto) throws ParseException {
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return Measure.builder()
                .kw(measureSenderDto.getKw())
                .date(formatoDelTexto.parse(measureSenderDto.getDate()))
                .build();
    }





}
