package com.app.coffee.service;

import com.app.coffee.factory.CoffeeOrderFactory;
import com.app.coffee.factory.impl.CoffeeOrderFactoryImpl;
import com.app.coffee.model.CoffeeOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoffeeService {
    private CoffeeOrderFactory factory;
    private List<CoffeeOrder> coffeeOrders;

    public void createCoffeeOrderFactorty(){
        if(factory == null){
            factory = new CoffeeOrderFactoryImpl();
        }
    }

    public void createCoffeeOrderList(){
        if(coffeeOrders == null){
            coffeeOrders = new ArrayList<>();
        }
    }

    public boolean isJSONValid(String jsonInString){
        try{
            return new ObjectMapper().readTree(jsonInString) != null;
        } catch(IOException e){
            return false;
        }
    }

    private long parseId(JSONObject coffeeOrder) {
        return Long.valueOf((int) coffeeOrder.get("id"));
    }

    private BigDecimal parseAmount(JSONObject coffeeOrder) {
        return new BigDecimal((String) coffeeOrder.get("amount"));
    }

    private String parseCoffeeName(JSONObject coffeeOrder){
        return (String) coffeeOrder.get("coffeeName");
    }

    private int parseCoffeeQuantaty(JSONObject coffeeOrder){
        return (Integer) coffeeOrder.get("coffeeQuantaty");
    }

    private void setCoffeeOrderValues(JSONObject jsonCoffeeOrder, CoffeeOrder coffeeOrder) {

        String orderNumber = (String) jsonCoffeeOrder.get("orderNumber");

        coffeeOrder.setOrderNumber(orderNumber != null ? orderNumber : coffeeOrder.getOrderNumber());
        coffeeOrder.setAmount(jsonCoffeeOrder.get("amount") != null ? parseAmount(jsonCoffeeOrder) : coffeeOrder.getAmount());
        coffeeOrder.setCoffeeName(jsonCoffeeOrder.get("coffeeName") != null ? parseCoffeeName(jsonCoffeeOrder) : coffeeOrder.getCoffeeName());
        coffeeOrder.setCoffeeQuantaty(jsonCoffeeOrder.get("coffeeQuantaty") != null ? parseCoffeeQuantaty(jsonCoffeeOrder) : coffeeOrder.getCoffeeQuantaty());
    }

    public CoffeeOrder create(JSONObject jsonCoffeeOrder) {

        createCoffeeOrderFactorty();

        CoffeeOrder coffeeOrder = factory.createCoffeeOrder();
        coffeeOrder.setId(parseId(jsonCoffeeOrder));
        setCoffeeOrderValues(jsonCoffeeOrder, coffeeOrder);

        return coffeeOrder;
    }

    public CoffeeOrder update(CoffeeOrder coffeeOrder, JSONObject jsonCoffeeOrder) {

        setCoffeeOrderValues(jsonCoffeeOrder, coffeeOrder);
        return coffeeOrder;
    }

    public void add(CoffeeOrder coffeeOrder) {
        createCoffeeOrderList();
        coffeeOrders.add(coffeeOrder);
    }

    public List<CoffeeOrder> find() {
        createCoffeeOrderList();
        return coffeeOrders;
    }

    public CoffeeOrder findById(long id) {
        return coffeeOrders.stream().filter(t -> id == t.getId()).toList().get(0);
    }

    public void delete() {
        coffeeOrders.clear();
    }
    public void clearObjects() {
        coffeeOrders = null;
        factory = null;
    }


}
