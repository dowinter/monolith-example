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

import java.util.List;
import java.util.Optional;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.example.model.InventoryItem;
import org.example.service.InventoryService;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://www.cdi-spec.org/faq/#accordion6
@Model
public class InventoryController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private InventoryService inventoryService;

    private long enteredId;

    private boolean availabilityChecked;
    private boolean available;

    @Produces
    @Named
    public List<InventoryItem> getInventoryItems() {
        return inventoryService.getAllItems();
    }

    public long getEnteredId() {
        return enteredId;
    }

    public void setEnteredId(long enteredId) {
        this.enteredId = enteredId;
    }

    public void checkAvailability() {
        Optional<InventoryItem> inventoryItem = inventoryService.getItem(enteredId);

        this.available = inventoryItem.map(item -> inventoryService.checkAvailability(item, 1))
                .orElse(false);
        availabilityChecked = true;
    }

    public boolean getAvailabilityChecked() {
        return availabilityChecked;
    }

    public boolean isAvailable() {
        return available;
    }
}
