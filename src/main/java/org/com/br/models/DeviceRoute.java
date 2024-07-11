package org.com.br.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRoute {

    private String id;

    private String value;

    private String idMicroRoute;

    private String idColorMicroRoute;

    private int type;

    @Override
    public String toString() {
        return value;
    }
}
