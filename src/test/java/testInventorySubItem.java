/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.inventory.base.Inventory;
import org.guanzon.cas.inventory.base.InventorySubUnit;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author User
 */
public class testInventorySubItem{

    static GRider instance;
    static Inventory record;

    @BeforeClass
    public static void setUpClass() {
        String path;
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            path = "D:/GGC_Maven_Systems";
        }
        else{
            path = "/srv/GGC_Maven_Systems";
        }
        System.setProperty("sys.default.path.config", path);

        GRider instance = new GRider("gRider");

        if (!instance.logUser("gRider", "M001000001")){
            System.err.println(instance.getErrMsg());
            System.exit(1);
        }
        
        System.setProperty("sys.default.path.config", path);
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/");
        
        if (!loadProperties()) {
            System.err.println("Unable to load config.");
            System.exit(1);
        } else {
            System.out.println("Config file loaded successfully.");
        }

        System.out.println("Connected");
        instance = MiscUtil.Connect();
        record = new Inventory(instance, false);
    }
    private static boolean loadProperties() {
        try {
            Properties po_props = new Properties();
            po_props.load(new FileInputStream("D:\\GGC_Maven_Systems\\config\\cas.properties"));

            System.setProperty("store.branch.code", po_props.getProperty("store.branch.code"));
            System.setProperty("store.inventory.industry", po_props.getProperty("store.inventory.category"));
            
            return true;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }


    @Test
    public void testOpenSubUnit() {
        JSONObject loJSON;
        loJSON = record.openRecord("M00124000051");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        if(record.getSubUnit().getMaster().size() >= 1){
            if(!record.getSubUnit().getMaster().get(record.getSubUnit().getMaster().size()-1).getStockID().isEmpty() || !record.getSubUnit().getMaster().get(record.getSubUnit().getMaster().size()-1).getSubItemID().isEmpty()){
                loJSON = record.addSubUnit();
                if ("error".equals((String) loJSON.get("result"))) {
                    Assert.fail((String) loJSON.get("message"));
                }
            }
        }
        Assert.assertEquals("A2", record.getSubUnit().getMaster(0, "xBarCodeU"));
        Assert.assertEquals("SUGAR 1/2 SACK", record.getSubUnit().getMaster(0, "xDescripU"));
        Assert.assertEquals("Kilograms", record.getSubUnit().getMaster(0, "xMeasurNm"));
        Assert.assertEquals(10.0, Double.parseDouble(record.getSubUnit().getMaster(0, "nQuantity").toString()),0.0);
        
        
        Assert.assertEquals("A3", record.getSubUnit().getMaster(1, "xBarCodeU"));
        Assert.assertEquals("SUGAR 5 KG", record.getSubUnit().getMaster(1, "xDescripU"));
        Assert.assertEquals("Kilograms", record.getSubUnit().getMaster(1, "xMeasurNm"));
        Assert.assertEquals(5.0, Double.parseDouble(record.getSubUnit().getMaster(1, "nQuantity").toString()),0.0);
        
//     
    }
    
    @Test
    public void testUpdateSubUnit() {
        JSONObject loJSON;
        loJSON = record.openRecord("M00124000051");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        if(record.getSubUnit().getMaster().size() >= 1){
            if(!record.getSubUnit().getMaster().get(record.getSubUnit().getMaster().size()-1).getStockID().isEmpty() || !record.getSubUnit().getMaster().get(record.getSubUnit().getMaster().size()-1).getSubItemID().isEmpty()){
                loJSON = record.addSubUnit();
                if ("error".equals((String) loJSON.get("result"))) {
                    Assert.fail((String) loJSON.get("message"));
                }
            }
        }
////        record.getMaster().get(0).setStockID("M00124000051");
//         record.getSubUnit().setMaster(0, "sStockIDx", "M00124000051");
//        Assert.assertEquals("M00124000051", record.getSubUnit().getMaster(0, "sStockIDx"));
//        Assert.assertEquals("A2", record.getSubUnit().getMaster(0, "xBarCodeU"));
//        Assert.assertEquals("SUGAR 1/2 SACK", record.getSubUnit().getMaster(0, "xDescripU"));
//        Assert.assertEquals("Kilograms", record.getSubUnit().getMaster(0, "xMeasurNm"));
//        Assert.assertEquals(10.0, Double.parseDouble(record.getSubUnit().getMaster(0, "nQuantity").toString()),0.0);
//        
//        
////        record.getMaster().get(1).setStockID("M00124000051");
//         record.getSubUnit().setMaster(1, "sStockIDx", "M00124000051");
        Assert.assertEquals("M00124000051", record.getSubUnit().getMaster(1, "sStockIDx"));
        Assert.assertEquals("A3", record.getSubUnit().getMaster(1, "xBarCodeU"));
        Assert.assertEquals("SUGAR 5 KG", record.getSubUnit().getMaster(1, "xDescripU"));
        Assert.assertEquals("Kilograms", record.getSubUnit().getMaster(1, "xMeasurNm"));
        Assert.assertEquals(5.0, Double.parseDouble(record.getSubUnit().getMaster(1, "nQuantity").toString()),0.0);
        
//        record.addSubUnit();
//        
//////        record.setMaster(2, "sStockIDx", "M00124000051");
//        record.getMaster().get(record.getMaster().size()-1).setStockID("M00124000051");
//        Assert.assertEquals("M00124000051", record.getMaster(record.getMaster().size()-1, "sStockIDx"));
//        record.getMaster().get(record.getMaster().size()-1).setSubItemID("M00124000087");
//        Assert.assertEquals("M00124000087", record.getMaster(record.getMaster().size()-1, "sItmSubID"));
//        
//        record.setMaster(record.getMaster().size()-1, "xBarCodeU", "a4");
//        Assert.assertEquals("a4", record.getMaster(record.getMaster().size()-1, "xBarCodeU"));
//        
//        record.setMaster(record.getMaster().size()-1, "xDescripU", "sugar 1/4 kg");
//        Assert.assertEquals("sugar 1/4 kg", record.getMaster(record.getMaster().size()-1, "xDescripU"));
//        
//        record.setMaster(record.getMaster().size()-1, "xMeasurNm", "Kilograms");
//        Assert.assertEquals("Kilograms", record.getMaster(record.getMaster().size()-1, "xMeasurNm"));
//        
//        record.getMaster().get(record.getMaster().size()-1).setQuantity(3);
//        Assert.assertEquals(3.0, Double.parseDouble(record.getMaster(record.getMaster().size()-1, "nQuantity").toString()),0.0);
//        System.out.println(record.getMaster().size());
        loJSON = record.saveRecord();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }

//        Assert.assertEquals("230000012708",  record.getMaster().get(record.getMaster().size()-1).get());
//        Assert.assertEquals("sasasasa",  record.getMaster().get(record.getMaster().size()-1).getDescription());
//        Assert.assertEquals("",  record.getMaster().get(record.getMaster().size()-1).getBriefDescription());
//        Assert.assertEquals("0001",  record.getMaster().get(record.getMaster().size()-1).getCategCd1());
//        Assert.assertEquals("0003",  record.getMaster().get(record.getMaster().size()-1).getCategCd2());tockID("M00124000051");
//     
    }

    @AfterClass
    public static void tearDownClass() {
        record = null;
        instance = null;
    }
}


