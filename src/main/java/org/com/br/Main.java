package org.com.br;

import org.com.br.models.DeviceRoute;
import org.com.br.models.Graph;
import org.com.br.sources.XmlBuilderDrive;

public class Main {
    public static void main(String[] args) {

        init("GraphGrainSeed/src/main/resources/Rota.xml");

    }
    public static void init(String file){

        XmlBuilderDrive rota = new XmlBuilderDrive(file);

        Graph<DeviceRoute> graph1 = rota.parseRotaTypeDeviceRoute(rota.getNodeList());

        graph1.itemizeRoots();

        graph1.generateRotas();

        rota.exportExcel(graph1.getRoutes(), graph1.getMicroRoutes(), graph1.getMicroRoutesWithRoute());

    }
}