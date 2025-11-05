package com.avaliacao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProducerInterval {
    private String producer;
    private Integer interval;
    private Integer previousWin;
    private Integer followingWin;
}