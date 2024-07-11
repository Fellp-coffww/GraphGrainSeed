package org.com.br.sources;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.com.br.models.DeviceRoute;
import org.com.br.models.Graph;
import org.com.br.models.Route;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Getter
@Setter
public class XmlBuilderDrive {

    private static final String MX_CELL = "mxCell";
    private static final String SOURCE = "source";
    private static final String TARGET = "target";
    private static final String VALUE = "value";
    private static final String ID = "id";
    private static final String VERTEX = "vertex";

    private Graph<String> graph;
    private String path;
    private NodeList nodeList;
    private ArrayList<DeviceRoute> deviceRouteArrayList = new ArrayList<>();

    public XmlBuilderDrive(String path) {
        this.path = path;
        try {
            File file = new File(this.path);
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(file);
            doc.getDocumentElement().normalize();
            this.nodeList = doc.getElementsByTagName(MX_CELL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método que determina o tipo de objeto DrawIO
    public Integer typeObjectDrawIO(Element element) {
        if (!element.getAttribute(SOURCE).isEmpty() && element.getAttribute(VALUE).isEmpty()) {
            return 1; // Edge
        } else if (!element.getAttribute(ID).isEmpty() && !element.getAttribute(VALUE).isEmpty() && !element.getAttribute(VERTEX).isEmpty()) {
            return 2; // Vertex
        }
        return 999; // Unknown
    }

    // Método que faz o parse dos elementos 'mxCell' e constrói o grafo
    public Graph<String> parseRota(NodeList nodeList) {
        graph = new Graph<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            switch (typeObjectDrawIO(element)) {
                case 1:
                    if (graph.getVertice(element.getAttribute(SOURCE)) == null) {
                        graph.addVertice(element.getAttribute(SOURCE), "");
                    }
                    if (graph.getVertice(element.getAttribute(TARGET)) == null) {
                        graph.addVertice(element.getAttribute(TARGET), "");
                    }
                    graph.addEdge(element.getAttribute(SOURCE), element.getAttribute(TARGET));
                    break;
                case 2:
                    if (graph.getVertice(element.getAttribute(ID)) == null) {
                        graph.addVertice(element.getAttribute(ID), element.getAttribute(VALUE));
                    } else {
                        graph.getVertice(element.getAttribute(ID)).setSubData(element.getAttribute(VALUE));
                    }
                    break;
                default:
                    break;
            }
        }
        return this.graph;
    }

    // Método que exporta rotas para um arquivo Excel
    public void exportExcel(ArrayList<String> routes) {
        DateTimeFormatter dt1 = DateTimeFormatter.ofPattern("dd_MM_YYYY_hh_mm");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Rotas");
        int rowIndex = 0;
        for (String route : routes) {
            String[] routeArray = route.replace(" ---> ", " ").split(" ");
            Row row = sheet.createRow(rowIndex);
            int cellIndex = 0;
            for (String cellValue : routeArray) {
                if (!cellValue.isEmpty()) {
                    Cell cell = row.createCell(cellIndex);
                    cell.setCellValue(cellValue);
                    cellIndex++;
                }
            }
            rowIndex++;
        }
        try (FileOutputStream fileOut = new FileOutputStream("rotas"+ LocalDateTime.now().format(dt1)+".xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método que faz o parse dos elementos 'mxCell' e constrói o grafo com DeviceRoute
    public Graph<DeviceRoute> parseRotaTypeDeviceRoute(NodeList nodeList) {
        Graph<DeviceRoute> graph1 = new Graph<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            if (typeObjectDrawIO(element) == 2) {
                DeviceRoute deviceRoute = new DeviceRoute(element.getAttribute(ID), element.getAttribute(VALUE), "0", "0",2);
                graph1.addVertice(deviceRoute);
                deviceRouteArrayList.add(deviceRoute);
            }
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            if (typeObjectDrawIO(element) == 1) {
                DeviceRoute source = findById(element.getAttribute(SOURCE));
                DeviceRoute target = findById(element.getAttribute(TARGET));
                graph1.addEdge(source, target);
            }
        }
        return graph1;
    }

    // Método que retorna uma lista de rotas a partir dos elementos 'mxCell'
    public Route routeExport(NodeList nodeList) {
        Route routeList = new Route();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            if (typeObjectDrawIO(element) == 2) {
                DeviceRoute deviceRoute = new DeviceRoute(element.getAttribute(ID), element.getAttribute(VALUE), "0", "0",2);
                routeList.getRoute().add(deviceRoute);
            }
        }
        return routeList;
    }

    // Método para encontrar um DeviceRoute pelo ID
    public DeviceRoute findById(String id) {
        for (DeviceRoute deviceRoute : deviceRouteArrayList) {
            if (id.equals(deviceRoute.getId())) {
                return deviceRoute;
            }
        }
        return null;
    }




}
