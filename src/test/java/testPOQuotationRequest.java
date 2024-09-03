
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.cas.inventory.base.Inventory;
import org.guanzon.cas.inventory.base.PO_Quotation_Request;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */



/**
 *
 * @author user
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testPOQuotationRequest {
    
    static GRider instance;
    static PO_Quotation_Request record;
    static String TransNox;
    static double delta = 0.00;
    String hardcodedDate = "2024-01-30";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    Date parsedDate;
    
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
    public void testANewTransaction() {
        JSONObject loJSON;
        record.setTransactionStatus("12340");
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
        
        ///////////////////////
        try {
            parsedDate = dateFormat.parse(hardcodedDate);  
            loJSON = record.getMasterModel().setTransactionDate(parsedDate); 
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ///////////////////////
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        try {
            parsedDate = dateFormat.parse(hardcodedDate);  
            Assert.assertEquals(parsedDate, record.getMasterModel().getTransactionDate());
        } catch (ParseException e) {
            e.printStackTrace();
            
        }
        
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
        
        try {
            parsedDate = dateFormat.parse(hardcodedDate);  
            loJSON = record.getMasterModel().setExpectedPurchaseDate(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
            Assert.assertEquals(parsedDate, record.getMasterModel().getExpectedPurchaseDate());

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
        
        try {
            parsedDate = dateFormat.parse(hardcodedDate);  
            loJSON = record.getMasterModel().setPreparedDate(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        try {
            parsedDate = dateFormat.parse(hardcodedDate); 
            Assert.assertEquals(parsedDate, record.getMasterModel().getPreparedDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        loJSON = record.getMasterModel().setModifiedBy("M001000001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("M001000001", record.getMasterModel().getModifiedBy());
        
        try {
            parsedDate = dateFormat.parse(hardcodedDate);  
            loJSON = record.getMasterModel().setModifiedDate(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        try {
            parsedDate = dateFormat.parse(hardcodedDate); 
            Assert.assertEquals(parsedDate, record.getMasterModel().getModifiedDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //////////////////////////////////////////////////////////
        
        
        
        loJSON = record.searchDetail(record.getItemCount()-1, "sStockIDx", "AL115C-40C6", true);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("M00124000019", record.getDetailModel(record.getItemCount()-1).getStockID());

        
        
        loJSON = record.getDetailModel(record.getItemCount()-1).setQuantity(2);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        Assert.assertEquals(2, record.getDetailModel(record.getItemCount()-1).getQuantity());
        
        loJSON = record.getDetailModel(0).setUnitPrice(20.00);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        Assert.assertEquals(20.00, Double.parseDouble(record.getDetailModel(record.getItemCount()-1).getUnitPrice().toString()), delta);
        
        
        //////////////
        
        loJSON = record.searchDetail(record.getItemCount()-1, "sStockIDx", "AL115C-40C6", true);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("M00124000019", record.getDetailModel(record.getItemCount()-1).getStockID());

        
        
        loJSON = record.getDetailModel(record.getItemCount()-1).setQuantity(2);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        Assert.assertEquals(2, record.getDetailModel(record.getItemCount()-1).getQuantity());
        
        loJSON = record.getDetailModel(0).setUnitPrice(20.00);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        Assert.assertEquals(20.00, Double.parseDouble(record.getDetailModel(record.getItemCount()-1).getUnitPrice().toString()), delta);
        
        
        
        
//        try {
//            parsedDate = dateFormat.parse(hardcodedDate); 
//            loJSON = record.getDetailModel(0).setModifiedDate(parsedDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }
//        
//        try {
//            parsedDate = dateFormat.parse(hardcodedDate); 
//            Assert.assertEquals(parsedDate, record.getDetailModel(0).getModifiedDate());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        
        TransNox = record.getMasterModel().getTransactionNumber();
        loJSON = record.saveTransaction();
        
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        
    }
    
    @Test
    public void testBOpenTransaction() {
        JSONObject loJSON;
        System.out.println(TransNox);
        loJSON = record.openTransaction("M00124000006");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("M001", record.getMasterModel().getBranchCd());
//        Assert.assertEquals(parsedDate, record.getMasterModel().getTransactionDate());
        Assert.assertEquals("GK01", record.getMasterModel().getDestination());
        Assert.assertEquals("10000", record.getMasterModel().getReferenceNumber());
        Assert.assertEquals("Remarks for TESTING", record.getMasterModel().getRemarks());
//        Assert.assertEquals(parsedDate, record.getMasterModel().getExpectedPurchaseDate());
        Assert.assertEquals(1, record.getMasterModel().getEntryNumber());
        Assert.assertEquals("0002", record.getMasterModel().getCategoryCode());
        
        Assert.assertEquals("1", record.getMasterModel().getTransactionStatus());
        Assert.assertEquals("M001000001", record.getMasterModel().getPreparedBy());
        
//        Assert.assertEquals(parsedDate, record.getMasterModel().getPreparedDate());
        Assert.assertEquals("M001000001", record.getMasterModel().getModifiedBy());
        
//        Assert.assertEquals(parsedDate, record.getMasterModel().getModifiedDate());
        
        //////////////////////////////////////////////////
        
        Assert.assertEquals("M00124000006", record.getDetailModel(record.getItemCount()-1).getTransactionNo());
        
       
        
        Assert.assertEquals("M00124000019", record.getDetailModel(record.getItemCount()-1).getStockID());
    
       
        Assert.assertEquals(2, record.getDetailModel(record.getItemCount()-1).getQuantity());
        Assert.assertEquals(20.00, Double.parseDouble(record.getDetailModel(record.getItemCount()-1).getUnitPrice().toString()), delta);
        
//        Assert.assertEquals(parsedDate, record.getDetailModel(1).getModifiedDate());
        
        
    }
    
    @Test
    public void testCUpdateTransaction() {
        JSONObject loJSON;
        loJSON = record.openTransaction("M00124000006");
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
