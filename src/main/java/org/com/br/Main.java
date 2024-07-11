package org.com.br;

import org.com.br.models.DeviceRoute;
import org.com.br.models.Graph;
import org.com.br.sources.XmlBuilderDrive;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {

        init("src/main/resources/Rota.xml");

    }
    public static void init(String file){

        XmlBuilderDrive rota = new XmlBuilderDrive(file);

        Graph<DeviceRoute> graph1 = rota.parseRotaTypeDeviceRoute(rota.getNodeList());

        graph1.itemizeRoots();

        graph1.generateRotas();

        System.out.println("");

        //rota.exportExcel(graph1.getRoutes());

    }
}