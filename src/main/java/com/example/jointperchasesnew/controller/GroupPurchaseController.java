package com.example.jointperchasesnew.controller;

import com.example.jointperchasesnew.dto.GroupPurchaseDto;
import com.example.jointperchasesnew.representation.GroupPurchaseRepresentation;
import com.example.jointperchasesnew.service.GroupPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/")
public class GroupPurchaseController {
    private GroupPurchaseService groupPurchaseService;


    @GetMapping("/groupPurchase/{productName}")
    public ResponseEntity<GroupPurchaseRepresentation> getGroupPurchase(@PathVariable String productName) {
        GroupPurchaseRepresentation representation = groupPurchaseService.getGroupPurchaseByProductName(productName);
        return ResponseEntity.ok(representation);
    }

    @PostMapping("/groupPurchase")
    public ResponseEntity<GroupPurchaseRepresentation> createGroupPurchase(@RequestBody GroupPurchaseDto groupPurchaseDto) {
        GroupPurchaseRepresentation representation = groupPurchaseService.createGroupPurchase(groupPurchaseDto);

        String encodedProductName = UriUtils.encodePath(groupPurchaseDto.getProductName(), StandardCharsets.UTF_8);

        return ResponseEntity.created(URI.create("/groupPurchase/" + encodedProductName)).body(representation);
    }

    @GetMapping("/groupPurchases")
    public ResponseEntity<CollectionModel<GroupPurchaseRepresentation>> getAllGroupPurchases() {
        CollectionModel<GroupPurchaseRepresentation> groupPurchases = groupPurchaseService.getAllGroupPurchases();
        return ResponseEntity.ok(groupPurchases);
    }

    @DeleteMapping("/groupPurchase/{productName}")
    public ResponseEntity<Void> cancelGroupPurchase(@PathVariable String productName) {
        groupPurchaseService.cancelGroupPurchase(productName);
        return ResponseEntity.noContent().build();
    }

    @Autowired
    public void setGroupPurchaseService(GroupPurchaseService groupPurchaseService) {
        this.groupPurchaseService = groupPurchaseService;
    }
}
