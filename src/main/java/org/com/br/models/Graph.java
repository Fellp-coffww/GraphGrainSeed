package org.com.br.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class Graph<TYPE> {

private ArrayList<Vertice<TYPE>> vertices;
private ArrayList<Vertice<DeviceRoute>> verticesClone = new ArrayList<>();
private ArrayList<Edge<TYPE>> edges;
private ArrayList<Vertice<TYPE>> roots = new ArrayList<>();
private ArrayList<String> routes = new ArrayList<>();
private ArrayList<Route> routeArrayList = new ArrayList<>();
private ArrayList<String> microRoutes = new ArrayList<>();
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
                printRoutes((Vertice<DeviceRoute>) this.getVertices().get(i), "", 0);
                //analyseMicroRoutes((Vertice<DeviceRoute>) this.getVertices().get(i), "", new Route());
            }
        }
    }

    public void generateMicroRotas(){


        for (int i = 0; i < this.getVertices().size(); i++) {
            if(this.getVertices().get(i).getInitialEdge().size() == 0){
                printMicroRoutes(this.getVertices().get(i), "");
            }
        }
    }

    public void analyseMicroRoutes(Vertice<DeviceRoute> begin, String path, Route route){
        verticesClone.add(begin);
        String temp = path;
        Route routeClone = route;
        if(begin.getFinalEdge().size() == 0){
            if(!microRoutes.contains(temp + begin.getData().getValue() + ARROW)) {
                temp = temp + begin.getData().getValue() + ARROW;
                microRoutes.add(temp);
                routeArrayList.add(route);
                routeClone = null;
            }
        }else{
            if(begin.getFinalEdge().size() > 1){
                if(!microRoutes.contains(temp + begin.getData().getValue() + ARROW)){
                temp = temp + begin.getData().getValue() + ARROW;
                microRoutes.add(temp);
                route.getRoute().add(begin.getData());
                }
                temp = "";
                for (int i = 0; i < begin.getFinalEdge().size(); i++) {
                    analyseMicroRoutes(getVertice(begin.getFinalEdge().get(i).getEnd().getData()), temp, routeClone);
                }
            }
                temp = temp + begin.getData().getValue() + ARROW;
                analyseMicroRoutes(getVertice(begin.getFinalEdge().get(0).getEnd().getData()), temp, routeClone);
        }
    }

    public void printMicroRoutes(Vertice<TYPE> begin, String path){
        String temp = "";
        if(begin.getFinalEdge().size() == 0){
            temp = temp + begin.getSubData() + ARROW;
            routes.add(temp);
            System.out.println( begin.getSubData() + ARROW);
        }else {
            System.out.print(begin.getSubData() + ARROW);
            for (int i = 0; i < begin.getFinalEdge().size(); i++) {
                temp = "";
                printMicroRoutes(getVertice(begin.getFinalEdge().get(i).getEnd().getData()),temp);
            }
        }
    }

    public void printRoutes(Vertice<DeviceRoute> begin, String path, int index){
        String temp = path;
        int microRoute = index;

        if(begin.getFinalEdge().size() == 0 || this.roots.contains(begin.getFinalEdge().get(0).getEnd())) {
            System.out.println(temp + begin.getData() + ARROW);
            routes.add(temp + begin.getData() + ARROW);
            verticesClone.add(begin);
        }else {
            temp = temp +  begin.getData() + ARROW;
            if(begin.getFinalEdge().size() > 1){
                microRoute += 1;
            }
            begin.getData().setIdMicroRoute(Integer.toString(microRoute));
            for (int i = 0; i < begin.getFinalEdge().size(); i++) {
                if(!verticesClone.contains(begin.getData())){
                printRoutes(getVertice(begin.getFinalEdge().get(i).getEnd().getData()), temp, microRoute);}
            }
            verticesClone.add(begin);
        }
    }

}
