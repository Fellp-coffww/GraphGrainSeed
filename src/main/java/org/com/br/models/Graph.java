package org.com.br.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashSet;


@Getter
@Setter
@AllArgsConstructor
public class Graph<TYPE> {
private ArrayList<Vertice<TYPE>> vertices;
private ArrayList<Vertice<DeviceRoute>> verticesClone = new ArrayList<>();
private ArrayList<Edge<DeviceRoute>> edgesClone = new ArrayList<>();
private ArrayList<String> standAlone;
private ArrayList<Edge<TYPE>> edges;
private ArrayList<Vertice<TYPE>> roots = new ArrayList<>();
private ArrayList<String> routes = new ArrayList<>();
private ArrayList<Route> routeArrayList = new ArrayList<>();
private ArrayList<String> microRoutes = new ArrayList<>();
private ArrayList<String> microRoutesWithRoute = new ArrayList<>();
private static final String ARROW = " ---> ";


    public void itemizeRoots(){
        for (int i = 0; i < this.getVertices().size(); i++) {
            if(this.getVertices().get(i).getInitialEdge().size() == 0) {
                verticesClone.clear();
                roots.add(this.getVertices().get(i));
            }
        }
    }

    public Graph() {
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();

    }

    public void addVertice(TYPE data, TYPE subData){
    Vertice<TYPE> vertice =  new Vertice(data, subData);
    this.vertices.add(vertice);
}

    public void addVertice(TYPE data){
        Vertice<TYPE> vertice =  new Vertice(data);
        this.vertices.add(vertice);
    }
    public void addEdge(TYPE initial, TYPE end){

        Vertice<TYPE> initialData = this.getVertice(initial);
        Vertice<TYPE> endData = this.getVertice(end);
        Edge<TYPE> edge = new Edge<TYPE>(initialData, endData);
        initialData.addElementEnd(edge);
        endData.addElementBegin(edge);
        this.edges.add(edge);
    }

    public Vertice<TYPE> getVertice(TYPE dado){
        Vertice<TYPE> vertice = null;
        for(int i=0; i < this.vertices.size(); i++){
            if (this.vertices.get(i).getData().equals(dado)){
                vertice = this.vertices.get(i);
                break;
            }
        }
        return  vertice;
    }

    public Vertice<DeviceRoute> getVertice(DeviceRoute dado){
        Vertice<TYPE> vertice = null;
        for(int i=0; i < this.vertices.size(); i++){
            if (this.vertices.get(i).getData().equals(dado)){
                vertice = this.vertices.get(i);
                break;
            }
        }
        return (Vertice<DeviceRoute>) vertice;
    }

    public void generateRotas(){
        for (int i = 0; i < this.getVertices().size(); i++) {
            if(this.getVertices().get(i).getInitialEdge().size() == 0){
                analyseMicroRoutes((Vertice<DeviceRoute>) this.getVertices().get(i), "", new Route());
                filterMicroRoutes();
                printRoutes((Vertice<DeviceRoute>) this.getVertices().get(i), "", 0);
                printRoutesWithMicroRoutes((Vertice<DeviceRoute>) this.getVertices().get(i), "", 0);

            }
        }
    }

    public void filterMicroRoutes(){
        ArrayList<String> tempArrayList = new ArrayList<>();
        for (int i = 0; i < microRoutes.size(); i++) {
            String temp = microRoutes.get(i).replace(ARROW, " ").replace("--->", "");
            String[] temp1 = temp.split(" ");
            if (temp1.length == 1) {
                tempArrayList.add(((temp).trim() + " --->").trim());
                microRoutes.remove(temp);
            }
        }
        LinkedHashSet<String> conjunto = new LinkedHashSet<>(tempArrayList);
        standAlone = new ArrayList<>(conjunto);

    }


    public void analyseMicroRoutes(Vertice<DeviceRoute> begin, String path, Route route){
        verticesClone.add(begin);
        boolean found = false;
        String temp = path;
        Route routeClone = route;

        for (int i = 0; i < begin.getFinalEdge().size(); i++) {
            if(begin.getFinalEdge().get(i).getEnd().getInitialEdge().size() > 1){
                found = true;
            }
        }

        if(begin.getFinalEdge().isEmpty()){
            if(!microRoutes.contains((temp + begin.getData().getValue() + ARROW).trim())) {
                temp = temp + begin.getData().getValue() + ARROW;
                microRoutes.add(temp.trim());
                routeArrayList.add(route);
            }
        }else{
            if(begin.getFinalEdge().size() > 1 || found ){

                if(!microRoutes.contains((temp + begin.getData().getValue() + ARROW).trim())){
                temp = temp + begin.getData().getValue() + ARROW;
                microRoutes.add(temp.trim());
                route.getRoute().add(begin.getData());
                }
                temp = "";
                for (int i = 0; i < begin.getFinalEdge().size(); i++) {
                        analyseMicroRoutes(getVertice(begin.getFinalEdge().get(i).getEnd().getData()), temp,routeClone);
                }

            }
                temp = temp + begin.getData().getValue() + ARROW;
                analyseMicroRoutes(getVertice(begin.getFinalEdge().get(0).getEnd().getData()), temp, routeClone);
        }
    }


    public void printRoutes(Vertice<DeviceRoute> begin, String path, int index){
        String temp = path;
        int microRoute = index;
        Vertice<DeviceRoute> beginClone = begin;
        if(verticesClone.contains(begin)){
           for (int i = 0; i < begin.getFinalEdge().size(); i++) {
                    if (edgesClone.contains(begin.getFinalEdge().get(i))){
                        //temp = temp + begin.getData() + ARROW;
                        beginClone.getFinalEdge().remove(i);
                        printRoutes(getVertice(begin.getFinalEdge().get(i).getEnd().getData()), temp, microRoute);
                    }
           }
        }
        if(begin.getFinalEdge().isEmpty() || this.roots.contains(begin.getFinalEdge().get(0).getEnd())) {
            System.out.println(temp + begin.getData() + ARROW);
            routes.add(temp + begin.getData() + ARROW);
            verticesClone.clear();
            edgesClone.clear();
        }else {
            temp = temp +  begin.getData() + ARROW;
            for (int i = 0; i < begin.getFinalEdge().size(); i++) {
                if(!verticesClone.contains(begin.getData())){
                    edgesClone.add(begin.getFinalEdge().get(i));
                    verticesClone.add(begin);
                    printRoutes(getVertice(begin.getFinalEdge().get(i).getEnd().getData()), temp, microRoute);
                }
            }
        }
    }

    public void printRoutesWithMicroRoutes(Vertice<DeviceRoute> begin, String path, int index){
        String temp = path;
        int microRoute = index;
        Vertice<DeviceRoute> beginClone = begin;
        if(verticesClone.contains(begin)){
            for (int i = 0; i < begin.getFinalEdge().size(); i++) {
                if (edgesClone.contains(begin.getFinalEdge().get(i))){
                    //temp = temp + begin.getData() + ARROW;
                    beginClone.getFinalEdge().remove(i);
                    printRoutesWithMicroRoutes(getVertice(begin.getFinalEdge().get(i).getEnd().getData()), temp, microRoute);
                }
            }
        }
        if(begin.getFinalEdge().isEmpty() || this.microRoutesWithRoute.contains(begin.getFinalEdge().get(0).getEnd())) {
            temp = temp + begin.getData() + ARROW;
            String consult = formatString(temp);
            if(standAlone.contains(consult)){
                consult = "";
            }else{
                if(microRoutes.contains(consult)){
                    temp = temp.replace(consult, formatMicroRoute(consult) + ARROW);
                }
            }

            System.out.println(temp);
            microRoutesWithRoute.add(temp) ;
            verticesClone.clear();
            edgesClone.clear();
        }else {
            temp = temp +  begin.getData() + ARROW;
            String consult = formatString(temp);

            if(standAlone.contains(consult)){
                consult = "";
            }else{
            if(microRoutes.contains(consult)){
                temp = temp.replace(consult, formatMicroRoute(consult) + ARROW);
            }
            }
            for (int i = 0; i < begin.getFinalEdge().size(); i++) {
                if(!verticesClone.contains(begin.getData())){
                    edgesClone.add(begin.getFinalEdge().get(i));
                    verticesClone.add(begin);
                    printRoutesWithMicroRoutes(getVertice(begin.getFinalEdge().get(i).getEnd().getData()), temp, microRoute);
                }
            }
        }
    }

    public String formatMicroRoute(String route){

        String[] temp = route.replace(" ---> ", " ").replace("--->", " ").split(" ");
        return temp[0] + ":" + temp[temp.length-1];

    }


    public String formatString(String temp){

        String consult = temp;
        if(temp.contains(":")){
            String[] splitted = consult.split(" ");
            for (int i = 0; i < splitted.length; i++) {
                if (splitted[i].contains(":")) {
                    splitted[i] = "";
                    splitted[i+1] = "";
                }
                if(standAlone.contains(splitted[i].trim() + " --->")){
                    splitted[i] = "";
                    splitted[i+1] = "";
                };
            }
            consult = "";
            for (int i = 0; i < splitted.length; i++) {
                if(!splitted[i].equals("")){
                    consult = consult + splitted[i] + " ";
                }
            }
        }
        return consult.trim();
    }

}
