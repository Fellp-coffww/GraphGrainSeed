package org.com.br.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vertice<TYPE> {

        private TYPE data;
        private TYPE subData;
        private ArrayList<Edge<TYPE>> initialEdge;
        private ArrayList<Edge<TYPE>> finalEdge;

        public Vertice(TYPE data, TYPE subData) {
                this.data = data;
                this.subData = subData;
                initialEdge = new ArrayList<>();
                finalEdge = new ArrayList<>();
        }

        public Vertice(TYPE data) {
                this.data = data;
                initialEdge = new ArrayList<>();
                finalEdge = new ArrayList<>();
        }



        public void addElementBegin(Edge edge){
                this.initialEdge.add(edge);
        }

        public void addElementEnd(Edge edge){
                this.finalEdge.add(edge);
        }
}
