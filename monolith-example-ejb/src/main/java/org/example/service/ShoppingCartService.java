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
package org.example.service;

import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.example.data.InventoryRepository;
import org.example.data.ShoppingCartRepository;
import org.example.model.InventoryItem;
import org.example.model.ShoppingCart;
import org.example.model.StockReservation;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class ShoppingCartService {
    @Inject
    private ShoppingCartRepository shoppingCarts;

    @Inject
    private InventoryService inventoryService;

    public ShoppingCart createShoppingCart() {
        ShoppingCart cart = new ShoppingCart();
        shoppingCarts.save(cart);

        return cart;
    }

    @Transactional(Transactional.TxType.MANDATORY)
    public boolean reserveItem(ShoppingCart cart, long itemId, int count) {
        Optional<InventoryItem> item = inventoryService.getItem(itemId);
        if (!item.isPresent() || !inventoryService.checkAvailability(item.get(), count)) {
            return false;
        }

        inventoryService.removeStock(item.get(), count);
        shoppingCarts.addReservation(cart, new StockReservation().withItem(item.get()).withReservedCount(count));

        return true;
    }

    public Optional<ShoppingCart> getShoppingCart(long shoppingCartId) {
        return shoppingCarts.findById(shoppingCartId);
    }
}
