package com.rahul.graphqldemo.controller;

import com.rahul.graphqldemo.dto.DisplayPattern;
import com.rahul.graphqldemo.model.entity.CityEntity;
import com.rahul.graphqldemo.service.CityService;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.rahul.graphqldemo.controller.CityController.CITY;

@RestController
@RequestMapping(CITY)
@RequiredArgsConstructor
public class CityController {
    static final String CITY = "cities";
    @Autowired
    CityService cityService;

    @Value("classpath:city.graphqls")
    private Resource schemaResource;

    private GraphQL graphQL;

    @PostConstruct
    public void loadGraphqlSchema() throws IOException {
        File schemaFile = schemaResource.getFile();
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(schemaFile);
        RuntimeWiring wiring = buildWiring();
        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry,wiring);
        graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private RuntimeWiring buildWiring() {
        DataFetcher<List<CityEntity>> fetcher1 = data->{
            return (List<CityEntity>) cityService.getAll();
        };
        return RuntimeWiring.newRuntimeWiring().type("Query", typeWriting->
            typeWriting.dataFetcher("getAllCities",fetcher1)).build();

    }

    @PostMapping
    public String saveCity(@RequestBody CityEntity city){
        cityService.save(city);
        return "Saved Successfully";
    }

    @GetMapping
    public List<CityEntity> getAllCities(){
        return cityService.getAll();
    }

    @PostMapping("/get-all")
    public ResponseEntity<?> getAll(@RequestBody String query){
        ExecutionResult executionResult = graphQL.execute(query);
        return new ResponseEntity<>(executionResult, HttpStatus.OK);
    }

    @GetMapping("/test-map-sort")
    public void testMapSort(){
        DisplayPattern displayPattern1 = new DisplayPattern(2,"DEF");
        DisplayPattern displayPattern2 = new DisplayPattern(1,"ABC");
        Map<String, DisplayPattern> map = new HashMap<>();
        map.put("A-1",displayPattern1);
        map.put("D-2",displayPattern2);
        map.entrySet().forEach(m->System.out.println(m));
        Map<String, DisplayPattern> lowestMap = map.entrySet().stream()
                .min(Comparator.comparing(m -> m.getValue().getPriority())).stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        lowestMap.entrySet().forEach(m->System.out.println(m));
    }

    @GetMapping("/limit-list/{limit}")
    public List<DisplayPattern> limitList(@PathVariable("limit") int limitValue){
        List<DisplayPattern> displayPatternList = List.of(
                new DisplayPattern(1,"ABC"),
                new DisplayPattern(2,"DEF"),
                new DisplayPattern(3,"GHI")
        );
        return displayPatternList.stream().limit(limitValue).collect(Collectors.toList());
    }

    @GetMapping("/map")
    public void mapping(){
        List<String> strList = List.of("a", "b", "c", "d");
        List<Integer> intList = List.of(1,2,3,4);

        intList.stream().map(strList::get).forEach(System.out::println);
    }

}
