/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


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
    static InventorySubUnit record;

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

        System.out.println("Connected");
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/");
        

        instance = MiscUtil.Connect();
        record = new InventorySubUnit(instance, false);
    }

    @Test
    public void testProgramFlow() {
        JSONObject loJSON;
        loJSON = record.openRecord("M00124000051");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        if(record.getMaster().size() >= 1){
            if(!record.getMaster().get(record.getMaster().size()-1).getStockID().isEmpty() || !record.getMaster().get(record.getMaster().size()-1).getSubItemID().isEmpty()){
                loJSON = record.addSubUnit();
                if ("error".equals((String) loJSON.get("result"))) {
                    Assert.fail((String) loJSON.get("message"));
                }
            }
        }
//        loJSON = record.getMaster().get(record.getMaster().size()-1).setStockID("M00124000051");
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }
//        Assert.assertEquals("M00124000051", record.getModel().getStockID());
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setEntryNox(record.getMaster().size());
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(record.getMaster().size(), record.getModel().getEntryNox(), 0);
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setSubItemID("M00124000004");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("M00124000004", record.getModel().getStockID());
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setQuantity(5);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(5, Integer.parseInt(record.getModel().getQuantity().toString()), 0);
//        for(int lnCtr = 1; lnCtr <= record.getMaster().size(); lnCtr++){
//            
//            loJSON = record.getMaster().get(record.getMaster().size()).setStockID("M00124000001");
//            if ("error".equals((String) loJSON.get("result"))) {
//                Assert.fail((String) loJSON.get("message"));
//            }
//            loJSON = record.getMaster().get(record.getMaster().size()).setEntryNox(record.getMaster().size());
//            if ("error".equals((String) loJSON.get("result"))) {
//                Assert.fail((String) loJSON.get("message"));
//            }
//            loJSON = record.getMaster().get(record.getMaster().size()).setSubItemID("M00124000004");
//            if ("error".equals((String) loJSON.get("result"))) {
//                Assert.fail((String) loJSON.get("message"));
//            }
//            loJSON = record.getMaster().get(record.getMaster().size()).setQuantity(5);
//            if ("error".equals((String) loJSON.get("result"))) {
//                Assert.fail((String) loJSON.get("message"));
//            }
//        }
        
        loJSON = record.saveRecord();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
    }

    @AfterClass
    public static void tearDownClass() {
        record = null;
        instance = null;
    }
}


