
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.cas.inventory.base.Inventory;
import org.guanzon.cas.inventory.base.PO_Quotation_Request;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */



/**
 *
 * @author user
 */
public class testPOQuotationRequest {
    
    static GRider instance;
    static PO_Quotation_Request record;
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
        
        if (!loadProperties()) {
            System.err.println("Unable to load config.");
            System.exit(1);
        } else {
            System.out.println("Config file loaded successfully.");
        }
        System.setProperty("sys.default.path.config", path);

        GRider instance = new GRider("gRider");

        if (!instance.logUser("gRider", "M001000001")){
            System.err.println(instance.getErrMsg());
            System.exit(1);
        }
        
        System.setProperty("sys.default.path.config", path);
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/");
        

        System.out.println("Connected");
        instance = MiscUtil.Connect();
        record = new PO_Quotation_Request(instance, false);
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
        
        loJSON = record.newTransaction();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }

//        loJSON = record.getModel().setStockID("M00124000001");
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }
        
        
        loJSON = record.getMasterModel().setBranchCd("M001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("M001", record.getMasterModel().getBranchCd());
        
        loJSON = record.getMasterModel().setTransactionDate(instance.getServerDate());
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(instance.getServerDate(), record.getMasterModel().getTransactionDate());
        
        loJSON = record.getMasterModel().setDestination("GK01");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("GK01", record.getMasterModel().getDestination());
        
        loJSON = record.getMasterModel().setReferenceNumber("10000");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("10000", record.getMasterModel().getReferenceNumber());
        
        loJSON = record.getMasterModel().setRemarks("Remarks for TESTING");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("Remarks for TESTING", record.getMasterModel().getRemarks());
        
        loJSON = record.getMasterModel().setExpectedPurchaseDate(instance.getServerDate());
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(instance.getServerDate(), record.getMasterModel().getExpectedPurchaseDate());

        loJSON = record.getMasterModel().setEntryNumber(1);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(1, record.getMasterModel().getEntryNumber());
        
        loJSON = record.getMasterModel().setCategoryCode("0002");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("0002", record.getMasterModel().getCategoryCode());
        
        loJSON = record.getMasterModel().setTranStatus("1");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("1", record.getMasterModel().getTransactionStatus());
//        
        loJSON = record.getMasterModel().setPreparedBy("M0012401");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("M0012401", record.getMasterModel().getPreparedBy());
        
        loJSON = record.getMasterModel().setPreparedDate(instance.getServerDate());
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(instance.getServerDate(), record.getMasterModel().getPreparedDate());
        
        loJSON = record.getMasterModel().setModifiedBy("M001000001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("M001000001", record.getMasterModel().getModifiedBy());
        
        loJSON = record.getMasterModel().setModifiedDate(instance.getServerDate());
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(instance.getServerDate(), record.getMasterModel().getModifiedDate());

        //////////////////////////////////////////////////////////
        
        loJSON = record.getDetailModel(1).setEntryNo(1);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(1, record.getDetailModel(1).getEntryNo());
        
        loJSON = record.getDetailModel(1).setStockID("M00120000001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("M00120000001", (record.getDetailModel(1).getStockID()));

        loJSON = record.getDetailModel(1).setDescript("Description Detail TESTING");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        Assert.assertEquals("Description Detail TESTING", record.getDetailModel(1).getDescript());
        
        loJSON = record.getDetailModel(1).setQuantity(2);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        Assert.assertEquals(2, record.getDetailModel(1).getQuantity());
        
        loJSON = record.getDetailModel(1).setUnitPrice(20.00);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        Assert.assertEquals(20.00, Double.parseDouble(record.getDetailModel(1).getUnitPrice().toString()), delta);
        
        loJSON = record.getDetailModel(1).setModifiedDate(instance.getServerDate());
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(instance.getServerDate(), record.getDetailModel(1).getModifiedDate());
        
        TransNox = record.getMasterModel().getTransactionNumber();
        loJSON = record.saveTransaction();
        
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        
    }
    
    @Test
    public void testOpenInventory() {
        JSONObject loJSON;
        System.out.println(TransNox);
        loJSON = record.openTransaction(TransNox);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("M001", record.getMasterModel().getBranchCd());
        Assert.assertEquals(instance.getServerDate(), record.getMasterModel().getTransactionDate());
        Assert.assertEquals("GK01", record.getMasterModel().getDestination());
        Assert.assertEquals("10000", record.getMasterModel().getReferenceNumber());
        Assert.assertEquals("Remarks for TESTING", record.getMasterModel().getRemarks());
        Assert.assertEquals(instance.getServerDate(), record.getMasterModel().getExpectedPurchaseDate());
        Assert.assertEquals(1, record.getMasterModel().getEntryNumber());
        Assert.assertEquals("0002", record.getMasterModel().getCategoryCode());
        
        Assert.assertEquals("1", record.getMasterModel().getTransactionStatus());
        Assert.assertEquals("M0012401", record.getMasterModel().getPreparedBy());
        
        Assert.assertEquals(instance.getServerDate(), record.getMasterModel().getPreparedDate());
        Assert.assertEquals("M001000001", record.getMasterModel().getModifiedBy());
        
        Assert.assertEquals(instance.getServerDate(), record.getMasterModel().getModifiedDate());
        
        //////////////////////////////////////////////////
        
        Assert.assertEquals(TransNox, record.getDetailModel(1).getTransactionNo());
        
        Assert.assertEquals(1, record.getDetailModel(1).getEntryNo());
        
        Assert.assertEquals("M00120000001", record.getDetailModel(1).getStockID());
        Assert.assertEquals("Description Detail TESTING", record.getDetailModel(1).getDescript());
       
        Assert.assertEquals(2, record.getDetailModel(1).getQuantity());
        Assert.assertEquals(20.00, Double.parseDouble(record.getDetailModel(1).getUnitPrice().toString()), delta);
        
        Assert.assertEquals(instance.getServerDate(), record.getDetailModel(1).getModifiedDate());
        
        
    }
    
    @Test
    public void testUpdateInventory() {
        JSONObject loJSON;
        loJSON = record.openTransaction(TransNox);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        loJSON = record.updateTransaction();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
//        record.getSubUnit().getMaster().get(0).setQuantity(10);
//        record.getSubUnit().getMaster().get(1).setQuantity(5);
        
//        Assert.assertEquals(10, record.getSubUnit().getMaster().get(0).getQuantity());
//        Assert.assertEquals(5, record.getSubUnit().getMaster().get(1).getQuantity());

        loJSON = record.saveTransaction();
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
