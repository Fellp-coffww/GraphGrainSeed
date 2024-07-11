package org.com.br.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Edge<TYPE> {

    private Vertice<TYPE> begin;
    private Vertice<TYPE> end;



}
