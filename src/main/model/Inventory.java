/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.model;

import java.util.concurrent.atomic.AtomicInteger;
import javafx.collections.ObservableList;

/**
 *
 * @author Drew
 */
public class Inventory {

    private ObservableList<Part> allParts;
    private ObservableList<Product> allProducts;
    private static AtomicInteger IdGenerator = new AtomicInteger(1000);
    private static AtomicInteger ProductIdGenerator = new AtomicInteger(0);
    
    public Inventory(ObservableList<Part> allParts, ObservableList<Product> allProducts) {
        this.allParts = allParts;
        this.allProducts = allProducts;
    }

    public void addPart(Part part) {
        allParts.add(part);
        part.setId(IdGenerator.getAndIncrement());

    }
    public void modifyPart(Part part){
        allParts.add(part);
        
    }

    public void addProduct(Product product) {
        allProducts.add(product);
        product.setId(ProductIdGenerator.getAndIncrement());

    }
    public void modifyProduct(Product product){
        allProducts.add(product);
    }

    public Part lookupPart(String name) {
        try {
            int id = Integer.parseInt(name);
            for (Part part : allParts) {
                if (part.getId() == id) {
                    return part;
                }
            }
            return null;
        } catch (NumberFormatException e) {
            for (Part part : allParts) {
                if (part.getName().equals(name)) {
                    return part;
                }
            }
            return null;
        }
    }
        
    

    

//    public Part lookupPart(int partId) {
//        for (Part part : allParts) {
//            if (part.getId() == partId) {
//                return part;
//            }
//        }
//
//        return null;
//
//    }

    public Product lookupProduct(String name) {
        try {
            int id = Integer.parseInt(name);
            for (Product product : allProducts) {
                if (product.getId() == id) {
                    return product;
                }
            }
            return null;
        } catch (NumberFormatException e) {
            for (Product product : allProducts) {
                if (product.getName().equals(name)) {
                    return product;
                }
            }
            return null;
        }
    }

    public ObservableList<Part> getAllParts() {
        return allParts;
    }

    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}
