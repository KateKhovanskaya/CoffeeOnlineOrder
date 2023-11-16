package com.app.coffee.controller;

import com.app.coffee.model.CoffeeOrder;
import com.app.coffee.service.CoffeeService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Supplier;
import org.apache.log4j.Logger;

@RestController
@RequestMapping("/coffeeOrder")
public class CoffeeOrderController {
    private static final Logger logger = Logger.getLogger(String.valueOf(CoffeeOrderController.class));

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
                var uri = ServletUriComponentsBuilder.fromCurrentRequest().path(travelCreated.getOrderNumber()).build().toUri();

                if(travelService.isStartDateGreaterThanEndDate(travelCreated)){
                    logger.error("The start date is greater than end date.");
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
                }else {
                    travelService.add(travelCreated);
                    return ResponseEntity.created(uri).body(null);
                }
            }else {
                return ResponseEntity.badRequest().body(null);
            }
        }catch(Exception e) {
            logger.error("JSON fields are not parsable. " + e);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }
}
