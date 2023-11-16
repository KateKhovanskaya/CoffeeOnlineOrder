package com.app.coffee.factory.impl;

import com.app.coffee.factory.CoffeeOrderFactory;
import com.app.coffee.model.CoffeeOrder;

public class CoffeeOrderFactoryImpl  implements CoffeeOrderFactory {
    @Override
    public CoffeeOrder createCoffeeOrder(){
        return new CoffeeOrder();
    }
}
