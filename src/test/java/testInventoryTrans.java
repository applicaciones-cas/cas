/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.inventory.base.InvMaster;
import org.guanzon.cas.inventory.base.InventoryTrans;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author User
 */
public class testInventoryTrans{

    static GRider instance;
    static InventoryTrans record;
    static String TransNox;
    static double delta = 0.00;;
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

        if (!loadProperties()) {
            System.err.println("Unable to load config.");
            System.exit(1);
        } else {
            System.out.println("Config file loaded successfully.");
        }
        instance = new GRider("gRider");

        if (!instance.logUser("gRider", "M001000001")){
            System.err.println(instance.getErrMsg());
            System.exit(1);
        }
        
        System.setProperty("sys.default.path.config", path);
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/");
        

        System.out.println("Connected");
        instance = MiscUtil.Connect();
        record = new InventoryTrans(instance, false);
//        record.setRecordStatus("0123");
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
    public void testNewInventory() {
        JSONObject loJSON;
        
        try {
        String dateString = "2024-08-08"; // Example date string
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        Date date;
//        loJSON = record.SearchInventory("M00124000053", true);
        loJSON = record.newTransaction();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
         

        loJSON = record.setMaster(record.getMaster().size() - 1, "sStockIDx", "M00124000002");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        loJSON = record.setMaster(0, "nQtyOnHnd", 5);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
       
        Assert.assertEquals(5, record.getMaster(0, "nQtyOnHnd"));
        
        loJSON = record.setMaster(0, "nResvOrdr", 5);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
       
        Assert.assertEquals(5, record.getMaster(0, "nResvOrdr"));
        
        loJSON = record.setMaster(0, "nBackOrdr", 5);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
       
        Assert.assertEquals(5, record.getMaster(0, "nBackOrdr"));
        
        loJSON = record.setMaster(0, "nQtyOrder", 5);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
       
        Assert.assertEquals(5, record.getMaster(0, "nQtyOrder"));
        
        
        
        loJSON = record.setMaster(0, "nUnitPrce", 1800.00);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
       
        Assert.assertEquals(1800.00, record.getMaster(0, "nUnitPrce"));
        
        loJSON = record.setMaster(0, "nPurPrice", 2000.00);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
       
        Assert.assertEquals(2000.00, record.getMaster(0, "nPurPrice"));
        
//        loJSON = record.setMaster(0, "nLedgerNo", record.getMaster().size());
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }
//       
//        Assert.assertEquals(record.getMaster().size(), record.getMaster(0, "nLedgerNo"));
        
        loJSON = record.setMaster(0, "sReplacID", "");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
       
        Assert.assertEquals("", record.getMaster(0, "sReplacID"));
        
        date = dateFormat.parse("2024-08-29");
        loJSON = record.setMaster(0, "dExpiryxx", date);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        loJSON = record.setMaster(0, "nQuantity", 12);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
       
        Assert.assertEquals(12, record.getMaster(0, "nQuantity"));
        
    
       System.out.println(date);
        Assert.assertEquals(date, record.getMaster(0, "dExpiryxx"));
       
//        TransNox = record.getInvModel().getStockID();
        
            date = dateFormat.parse(dateString);
            System.out.println("Parsed Date: " + date);
         loJSON = record.Sales("M0012400002", date, EditMode.ADDNEW);
            
        if ("error".equals((String) loJSON.get("result"))) {
            instance.rollbackTrans();
            Assert.fail((String) loJSON.get("message"));
        }
        } catch (ParseException ex) {
            Logger.getLogger(testInventoryTrans.class.getName()).log(Level.SEVERE, null, ex);
        }  
        
    }
    @AfterClass 
    public static void tearDownClass() {
        record = null;
        instance = null;
    }
}
