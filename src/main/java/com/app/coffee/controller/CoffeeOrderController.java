package com.app.coffee.controller;

import com.app.coffee.model.CoffeeOrder;
import com.app.coffee.service.CoffeeService;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.function.Supplier;

@RestController
@RequestMapping("/coffeeOrder")
public class CoffeeOrderController {

    private static final Logger logger = Logger.getLogger(CoffeeOrderController.class);

    @Autowired
    private CoffeeService coffeeService;

    @GetMapping
    public ResponseEntity<List<CoffeeOrder>> find() {
        if(coffeeService.find().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        logger.info((Supplier<String>) coffeeService.find());
        return ResponseEntity.ok(coffeeService.find());
    }

    @DeleteMapping
    public ResponseEntity<Boolean> delete() {
        try {
            coffeeService.delete();
            return ResponseEntity.noContent().build();
        }catch(Exception e) {
            logger.error(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<CoffeeOrder> create(@RequestBody JSONObject jsonCoffeeOrder) {
        try {
            if(coffeeService.isJSONValid(jsonCoffeeOrder.toString())) {
                CoffeeOrder orderCreated = coffeeService.create(jsonCoffeeOrder);
                var uri = ServletUriComponentsBuilder.fromCurrentRequest().path(orderCreated.getOrderNumber()).build().toUri();
                coffeeService.add(orderCreated);
                return ResponseEntity.created(uri).body(null);
            }else {
                return ResponseEntity.badRequest().body(null);
            }
        }catch(Exception e) {
            logger.error("JSON fields are not parsable. " + e);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }

    @PutMapping(path = "/{id}", produces = { "application/json" })
    public ResponseEntity<CoffeeOrder> update(@PathVariable("id") long id, @RequestBody JSONObject coffeeOrder) {
        try {
            if(coffeeService.isJSONValid(coffeeOrder.toString())) {
                CoffeeOrder orderToUpdate = coffeeService.findById(id);
                if(orderToUpdate == null){
                    logger.error("Travel not found.");
                    return ResponseEntity.notFound().build();
                }else {
                    orderToUpdate = coffeeService.update(orderToUpdate, coffeeOrder);
                    return ResponseEntity.ok(orderToUpdate);
                }
            }else {
                return ResponseEntity.badRequest().body(null);
            }
        }catch(Exception e) {
            logger.error("JSON fields are not parsable." + e);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }
}

