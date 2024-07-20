/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        record = new InvLedger(instance, false);
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
        loJSON = record.getMaster().get(record.getMaster().size()-1).setStockID("M00124000004");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("M00124000004", record.getMaster().get(record.getMaster().size()-1).getStockID());
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setSourceCode("DA");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("DA", record.getMaster().get(record.getMaster().size()-1).getSourceCode());
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setSourceNo("P0W124000018");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("P0W124000018", record.getMaster().get(record.getMaster().size()-1).getSourceNo());
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setWHouseID("015");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("015", record.getMaster().get(record.getMaster().size()-1).getWHouseID());
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setWareHouseName("Building G-2F");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("Building G-2F", record.getMaster().get(record.getMaster().size()-1).getWareHouseName());
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setLedgerNo(1);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(1, record.getMaster().get(record.getMaster().size()-1).getLedgerNo(), 0);
        System.out.println("dTransact = " + CommonUtils.dateFormat(instance.getServerDate(), "yyyy-MM-dd"));
//        Date trans_date = new Date(instance.getServerDate().toString());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        
            Date dateObject;
        try {
            dateObject = formatter.parse(CommonUtils.dateFormat(instance.getServerDate(), "yyyy-MM-dd"));
            
            loJSON = record.getMaster().get(record.getMaster().size()-1).setTransactDate(dateObject);
            
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }
            
            
            loJSON = record.getMaster().get(record.getMaster().size()-1).setExpiryDate(dateObject);
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }
        } catch (ParseException ex) {
            Logger.getLogger(testInventoryLedger.class.getName()).log(Level.SEVERE, null, ex);
        }
//        
        
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setQuantityIn(5);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(5, record.getMaster().get(record.getMaster().size()-1).getQuantityIn(), 0);
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setQuantityIssue(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getMaster().get(record.getMaster().size()-1).getQuantityIssue(), 0);
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setQuantityOnHand(30);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(30, record.getMaster().get(record.getMaster().size()-1).getQuantityOnHand(), 0);
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setQuantityOrder(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getMaster().get(record.getMaster().size()-1).getQuantityOrder(), 0);
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setQuantityOut(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getMaster().get(record.getMaster().size()-1).getQuantityOut(), 0);
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setPurchasePrice(500);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(500.00, Double.parseDouble(record.getMaster().get(record.getMaster().size()-1).getPurchasePrice().toString()), 0.00);
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setUnitPrice(400);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(400.00, Double.parseDouble(record.getMaster().get(record.getMaster().size()-1).getUnitPrice().toString()), 0.00);
        
        loJSON = record.getMaster().get(record.getMaster().size()-1).setBranchCode("M001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        Assert.assertEquals("M001", record.getMaster().get(record.getMaster().size()-1).getBranchCode());
        
        
        loJSON = record.saveRecord();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
    }
    
    @Test
    public void testOpenRecord() {
        JSONObject loJSON;
        loJSON = record.openRecord("M00124000003");
//        loJSON = record.newRecord();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
//        if(record.getMaster().size() >= 1){
//            if(record.getMaster().get(record.getMaster().size()-1).getStockID() != null){
//                loJSON = record.addLedger();
//                if ("error".equals((String) loJSON.get("result"))) {
//                    Assert.fail((String) loJSON.get("message"));
//                }
//            }
//        }
        
        Assert.assertEquals("M00124000003", record.getMaster(record.getMaster().size()-1, "sStockIDx"));
        Assert.assertEquals("DA", record.getMaster(record.getMaster().size()-1, "sSourceCd"));
        Assert.assertEquals("P0W124000016", record.getMaster(record.getMaster().size()-1, "sSourceNo"));
        Assert.assertEquals("015", record.getMaster(record.getMaster().size()-1, "sWHouseID"));
        Assert.assertEquals("Building G-2F", record.getMaster(record.getMaster().size()-1, "xWHouseNm"));
//        Assert.assertEquals("M00124000003", record.getMaster().get(record.getMaster().size()-1).getStockID());

        Assert.assertEquals(1, record.getMaster().get(record.getMaster().size()-1).getLedgerNo(), 0);
        Assert.assertEquals(5, record.getMaster().get(record.getMaster().size()-1).getQuantityIn(), 0);
        Assert.assertEquals(0, record.getMaster().get(record.getMaster().size()-1).getQuantityIssue(), 0);
        Assert.assertEquals(30, record.getMaster().get(record.getMaster().size()-1).getQuantityOnHand(), 0);
        Assert.assertEquals(0, record.getMaster().get(record.getMaster().size()-1).getQuantityOrder(), 0);
        Assert.assertEquals(0, record.getMaster().get(record.getMaster().size()-1).getQuantityOut(), 0);
        Assert.assertEquals(500.00, Double.parseDouble(record.getMaster().get(record.getMaster().size()-1).getPurchasePrice().toString()), 0.00);
        Assert.assertEquals(400.00, Double.parseDouble(record.getMaster().get(record.getMaster().size()-1).getUnitPrice().toString()), 0.00);
        Assert.assertEquals("M001", record.getMaster().get(record.getMaster().size()-1).getBranchCode());
        
    }

    @AfterClass
    public static void tearDownClass() {
        record = null;
        instance = null;
    }
}


