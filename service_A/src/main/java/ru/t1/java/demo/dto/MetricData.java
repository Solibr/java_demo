package ru.t1.java.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricData {

    private long timeMillis;

    private String methodName;

    private Object[] args;

}
