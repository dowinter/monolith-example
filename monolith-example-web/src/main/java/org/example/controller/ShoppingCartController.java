/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.example.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.example.model.ShoppingCart;
import org.example.model.StockReservation;
import org.example.service.ShoppingCartService;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://www.cdi-spec.org/faq/#accordion6
@Named
@SessionScoped
public class ShoppingCartController implements Serializable {
    @Inject
    private ShoppingCartService shoppingCartService;

    private ShoppingCart currentCart;

    private long enteredItemId;
    private int enteredCount;

    @Produces
    @Named
    public List<StockReservation> getShoppingCartItems() {
        return currentCart.getStockReservations();
    }

    @PostConstruct
    public void createNewShoppingCart() {
        this.currentCart = shoppingCartService.createShoppingCart();
    }

    public void addToShoppingCart() {
        shoppingCartService.reserveItem(currentCart, this.enteredItemId, this.enteredCount);
    }

    public void setCurrentCart(ShoppingCart currentCart) {
        this.currentCart = currentCart;
    }

    public long getEnteredItemId() {
        return enteredItemId;
    }

    public void setEnteredItemId(long enteredItemId) {
        this.enteredItemId = enteredItemId;
    }

    public int getEnteredCount() {
        return enteredCount;
    }

    public void setEnteredCount(int enteredCount) {
        this.enteredCount = enteredCount;
    }

    public ShoppingCart getCurrentCart() {
        return currentCart;
    }
}
