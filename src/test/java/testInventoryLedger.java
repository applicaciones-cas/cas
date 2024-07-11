/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.util.Date;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.inventory.base.Inventory;
import org.guanzon.cas.inventory.base.InvLedger;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author User
 */
public class testInventoryLedger{

    static GRider instance;
    static InvLedger record;

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

        instance = new GRider("gRider");

        if (!instance.logUser("gRider", "M001000001")){
            System.err.println(instance.getErrMsg());
            System.exit(1);
        }

        System.out.println("Connected");
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/");
        

        instance = MiscUtil.Connect();
        record = new InvLedger(instance, false);
    }

    @Test
    public void testNewRecord() {
        JSONObject loJSON;
//        loJSON = record.openRecord("M00124000003");
        loJSON = record.newRecord();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        if(record.getMaster().size() >= 1){
            if(record.getMaster().get(record.getMaster().size()-1).getStockID() != null){
                loJSON = record.addLedger();
                if ("error".equals((String) loJSON.get("result"))) {
                    Assert.fail((String) loJSON.get("message"));
                }
            }
        }
        loJSON = record.getMaster().get(record.getMaster().size()-1).setStockID("M00124000003");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setSourceCode("DA");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        loJSON = record.getMaster().get(record.getMaster().size()-1).setSourceNo("P0W124000017");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setWHouseID("015");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setWareHouseName("Building G-2F");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setLedgerNo(1);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        loJSON = record.getMaster().get(record.getMaster().size()-1).setTransactDate(CommonUtils.toDate(instance.getServerDate().toString()));
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setQuantityIn(5);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        loJSON = record.getMaster().get(record.getMaster().size()-1).setQuantityIssue(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        loJSON = record.getMaster().get(record.getMaster().size()-1).setQuantityOnHand(30);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        loJSON = record.getMaster().get(record.getMaster().size()-1).setQuantityOrder(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setQuantityOut(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setPurchasePrice(500);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        loJSON = record.getMaster().get(record.getMaster().size()-1).setUnitPrice(400);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setBranchCode("M001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        loJSON = record.getMaster().get(record.getMaster().size()-1).setExpiryDate(CommonUtils.toDate(instance.getServerDate().toString()));
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
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


