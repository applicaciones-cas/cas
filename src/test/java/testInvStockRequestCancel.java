
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.cas.inventory.stock.InvStockReqCancel;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Unclejo
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testInvStockRequestCancel {
 
    static GRider instance;
    static InvStockReqCancel record;
    static String lsTransNox = "";
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

        instance = new GRider("gRider");

        if (!instance.logUser("gRider", "M001000001")){
            System.err.println(instance.getErrMsg());
            System.exit(1);
        }
        
        System.setProperty("sys.default.path.config", path);
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/");
        

        System.out.println("Connected");
        instance = MiscUtil.Connect();
        record = new InvStockReqCancel(instance, false);
        record.setTransactionStatus("01234");
        record.setWithUI(false);
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
    public void testNewTransaction() {
        JSONObject loJSON;
        loJSON = record.BrowseRequest("sTransNox", "M00124000001", true);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }   
        
//        Uncomment this code if setDetail on BrowseRequest is commented the code  
//        loJSON = record.searchDetail(0, 3, "93210-448G4", true);
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }
//        
//        Assert.assertEquals(1, record.getDetailModel().get(0).getEntryNumber());
//        Assert.assertEquals("M00124000005", record.getDetailModel().get(0).getStockID());
//        Assert.assertEquals(5, record.getDetailModel().get(0).getQuantity());
//        Assert.assertEquals("93210-448G4", record.getDetailModel().get(0).getBarcode());
//        Assert.assertEquals("tuppet  oring", record.getDetailModel().get(0).getDescription());
//        Assert.assertEquals("Motorcycle", record.getDetailModel().get(0).getCategoryName());
//        Assert.assertEquals("Spare Parts", record.getDetailModel().get(0).getCategoryName2());
//        Assert.assertEquals("Finished Products", record.getDetailModel().get(0).getCategoryType());
//        Assert.assertEquals("This is detail 01 notes entry 1.", record.getDetailModel().get(0).getNotes());
//        record.AddModelDetail();
//        loJSON = record.searchDetail(1, 3, "93210-71462-10", true);//93210-71462-10
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }
//        Assert.assertEquals(2, record.getDetailModel().get(1).getEntryNumber());
//        Assert.assertEquals("M00124000006", record.getDetailModel().get(1).getStockID());
//        Assert.assertEquals(5, record.getDetailModel().get(1).getQuantity());
//        Assert.assertEquals("93210-71462-10", record.getDetailModel().get(1).getBarcode());
//        Assert.assertEquals("O-RING", record.getDetailModel().get(1).getDescription());
//        Assert.assertEquals("Motorcycle", record.getDetailModel().get(1).getCategoryName());
//        Assert.assertEquals("Spare Parts", record.getDetailModel().get(1).getCategoryName2());
//        Assert.assertEquals("Finished Products", record.getDetailModel().get(1).getCategoryType());
//        Assert.assertEquals("This is detail 02 notes entry 2.", record.getDetailModel().get(1).getNotes());
        
//        This function checks if all inventory stock request details are loaded.
//        comment this code if setDetail on BrowseRequest is commented the code 
        for(int lnCtr = 0; lnCtr < record.getDetailModel().size(); lnCtr++){
            Assert.assertEquals("M00124000001", record.getDetailModel().get(lnCtr).getOrderNumber());
            switch(lnCtr){
                case 0:
                        Assert.assertEquals(1, record.getDetailModel().get(lnCtr).getEntryNumber());
                        Assert.assertEquals("M00124000001", record.getDetailModel().get(lnCtr).getStockID());
                        Assert.assertEquals(3, record.getDetailModel().get(lnCtr).getQuantity());
                        Assert.assertEquals("230000012708", record.getDetailModel().get(lnCtr).getBarcode());
                        Assert.assertEquals("sasasasa", record.getDetailModel().get(lnCtr).getDescription());
                        Assert.assertEquals("Motorcycle", record.getDetailModel().get(lnCtr).getCategoryName());
                        Assert.assertEquals("Sports", record.getDetailModel().get(lnCtr).getCategoryName2());
                        Assert.assertEquals("Finished Products", record.getDetailModel().get(lnCtr).getCategoryType());
                        Assert.assertEquals("This is detail 01 notes entry 1.", record.getDetailModel().get(lnCtr).getNotes());
                        break;
                    case 1:
                        Assert.assertEquals(2, record.getDetailModel().get(lnCtr).getEntryNumber());
                        Assert.assertEquals("M00124000002", record.getDetailModel().get(lnCtr).getStockID());
                        Assert.assertEquals(4, record.getDetailModel().get(lnCtr).getQuantity());
                        Assert.assertEquals("5TL-E4170-00-00", record.getDetailModel().get(lnCtr).getBarcode());
                        Assert.assertEquals("DIAPHRAGM ASS'Y", record.getDetailModel().get(lnCtr).getDescription());
                        Assert.assertEquals("Motorcycle", record.getDetailModel().get(lnCtr).getCategoryName());
                        Assert.assertEquals("Spare Parts", record.getDetailModel().get(lnCtr).getCategoryName2());
                        Assert.assertEquals("Finished Products", record.getDetailModel().get(lnCtr).getCategoryType());
                        Assert.assertEquals("This is detail 02 notes entry 2.", record.getDetailModel().get(lnCtr).getNotes());
                        break;
                    case 2:
                        Assert.assertEquals(3, record.getDetailModel().get(lnCtr).getEntryNumber());
                        Assert.assertEquals("M00124000003", record.getDetailModel().get(lnCtr).getStockID());
                        Assert.assertEquals(5, record.getDetailModel().get(lnCtr).getQuantity());
                        Assert.assertEquals("5LW-E1351-00-00", record.getDetailModel().get(lnCtr).getBarcode());
                        Assert.assertEquals("GASKET,CYLINDER 1", record.getDetailModel().get(lnCtr).getDescription());
                        Assert.assertEquals("Motorcycle", record.getDetailModel().get(lnCtr).getCategoryName());
                        Assert.assertEquals("Spare Parts", record.getDetailModel().get(lnCtr).getCategoryName2());
                        Assert.assertEquals("Finished Products", record.getDetailModel().get(lnCtr).getCategoryType());
                        Assert.assertEquals("This is detail 03 notes entry 3.", record.getDetailModel().get(lnCtr).getNotes());
                        break;
                    default:
            }
        }
        
        loJSON = record.saveTransaction();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
//        
    }
    
    @Test
    public void testSearchTransaction() {
        JSONObject loJSON;
        loJSON = record.searchTransaction("sTransNox", "M00124000001", true);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        if(record.getDetailModel().size()>0){
            for(int lnCtr = 0; lnCtr < record.getDetailModel().size(); lnCtr++){
                
                Assert.assertEquals("M00124000008", record.getDetailModel().get(lnCtr).getOrderNumber());
                switch(lnCtr){
                    case 0:
                        Assert.assertEquals(1, record.getDetailModel().get(lnCtr).getEntryNumber());
                        Assert.assertEquals("M00124000005", record.getDetailModel().get(lnCtr).getStockID());
                        Assert.assertEquals(5, record.getDetailModel().get(lnCtr).getQuantity());
                        Assert.assertEquals("93210-448G4", record.getDetailModel().get(lnCtr).getBarcode());
                        Assert.assertEquals("tuppet  oring", record.getDetailModel().get(lnCtr).getDescription());
                        Assert.assertEquals("Motorcycle", record.getDetailModel().get(lnCtr).getCategoryName());
                        Assert.assertEquals("Spare Parts", record.getDetailModel().get(lnCtr).getCategoryName2());
                        Assert.assertEquals("Finished Products", record.getDetailModel().get(lnCtr).getCategoryType());
                        Assert.assertEquals("This is detail 01 notes entry 1.", record.getDetailModel().get(lnCtr).getNotes());
                        break;
                    case 1:
                        Assert.assertEquals(2, record.getDetailModel().get(lnCtr).getEntryNumber());
                        Assert.assertEquals("M00124000006", record.getDetailModel().get(lnCtr).getStockID());
                        Assert.assertEquals(5, record.getDetailModel().get(lnCtr).getQuantity());
                        Assert.assertEquals("93210-71462-10", record.getDetailModel().get(lnCtr).getBarcode());
                        Assert.assertEquals("O-RING", record.getDetailModel().get(lnCtr).getDescription());
                        Assert.assertEquals("Motorcycle", record.getDetailModel().get(lnCtr).getCategoryName());
                        Assert.assertEquals("Spare Parts", record.getDetailModel().get(lnCtr).getCategoryName2());
                        Assert.assertEquals("Finished Products", record.getDetailModel().get(lnCtr).getCategoryType());
                        Assert.assertEquals("This is detail 02 notes entry 2.", record.getDetailModel().get(lnCtr).getNotes());
                        break;
                }
            }
        }
        
    }
    @Test
    public void testCloseTransaction() {
        JSONObject loJSON;
        loJSON = record.openTransaction("M00124000002");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        if(record.getDetailModel().size()>0){
            for(int lnCtr = 0; lnCtr < record.getDetailModel().size(); lnCtr++){
                
                Assert.assertEquals("M00124000001", record.getDetailModel().get(lnCtr).getOrderNumber());
                switch(lnCtr){
                    case 0:
                        Assert.assertEquals(1, record.getDetailModel().get(lnCtr).getEntryNumber());
                        Assert.assertEquals("M00124000001", record.getDetailModel().get(lnCtr).getStockID());
                        Assert.assertEquals(3, record.getDetailModel().get(lnCtr).getQuantity());
                        Assert.assertEquals("230000012708", record.getDetailModel().get(lnCtr).getBarcode());
                        Assert.assertEquals("sasasasa", record.getDetailModel().get(lnCtr).getDescription());
                        Assert.assertEquals("Motorcycle", record.getDetailModel().get(lnCtr).getCategoryName());
                        Assert.assertEquals("Sports", record.getDetailModel().get(lnCtr).getCategoryName2());
                        Assert.assertEquals("Finished Products", record.getDetailModel().get(lnCtr).getCategoryType());
                        Assert.assertEquals("This is detail 01 notes entry 1.", record.getDetailModel().get(lnCtr).getNotes());
                        break;
                    case 1:
                        Assert.assertEquals(2, record.getDetailModel().get(lnCtr).getEntryNumber());
                        Assert.assertEquals("M00124000002", record.getDetailModel().get(lnCtr).getStockID());
                        Assert.assertEquals(4, record.getDetailModel().get(lnCtr).getQuantity());
                        Assert.assertEquals("5TL-E4170-00-00", record.getDetailModel().get(lnCtr).getBarcode());
                        Assert.assertEquals("DIAPHRAGM ASS'Y", record.getDetailModel().get(lnCtr).getDescription());
                        Assert.assertEquals("Motorcycle", record.getDetailModel().get(lnCtr).getCategoryName());
                        Assert.assertEquals("Spare Parts", record.getDetailModel().get(lnCtr).getCategoryName2());
                        Assert.assertEquals("Finished Products", record.getDetailModel().get(lnCtr).getCategoryType());
                        Assert.assertEquals("This is detail 02 notes entry 2.", record.getDetailModel().get(lnCtr).getNotes());
                        break;
                }
            }
        }
        loJSON = record.closeTransaction("M00124000001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
    }
    
     @Test
    public void testPostTransaction() {
        JSONObject loJSON;
        loJSON = record.openTransaction("M00124000002");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        if(record.getDetailModel().size()>0){
            for(int lnCtr = 0; lnCtr < record.getDetailModel().size(); lnCtr++){
                
                Assert.assertEquals("M00124000001", record.getDetailModel().get(lnCtr).getOrderNumber());
                switch(lnCtr){
                    case 0:
                        Assert.assertEquals(1, record.getDetailModel().get(lnCtr).getEntryNumber());
                        Assert.assertEquals("M00124000001", record.getDetailModel().get(lnCtr).getStockID());
                        Assert.assertEquals(3, record.getDetailModel().get(lnCtr).getQuantity());
                        Assert.assertEquals("230000012708", record.getDetailModel().get(lnCtr).getBarcode());
                        Assert.assertEquals("sasasasa", record.getDetailModel().get(lnCtr).getDescription());
                        Assert.assertEquals("Motorcycle", record.getDetailModel().get(lnCtr).getCategoryName());
                        Assert.assertEquals("Sports", record.getDetailModel().get(lnCtr).getCategoryName2());
                        Assert.assertEquals("Finished Products", record.getDetailModel().get(lnCtr).getCategoryType());
                        Assert.assertEquals("This is detail 01 notes entry 1.", record.getDetailModel().get(lnCtr).getNotes());
                        break;
                    case 1:
                        Assert.assertEquals(2, record.getDetailModel().get(lnCtr).getEntryNumber());
                        Assert.assertEquals("M00124000002", record.getDetailModel().get(lnCtr).getStockID());
                        Assert.assertEquals(4, record.getDetailModel().get(lnCtr).getQuantity());
                        Assert.assertEquals("5TL-E4170-00-00", record.getDetailModel().get(lnCtr).getBarcode());
                        Assert.assertEquals("DIAPHRAGM ASS'Y", record.getDetailModel().get(lnCtr).getDescription());
                        Assert.assertEquals("Motorcycle", record.getDetailModel().get(lnCtr).getCategoryName());
                        Assert.assertEquals("Spare Parts", record.getDetailModel().get(lnCtr).getCategoryName2());
                        Assert.assertEquals("Finished Products", record.getDetailModel().get(lnCtr).getCategoryType());
                        Assert.assertEquals("This is detail 02 notes entry 2.", record.getDetailModel().get(lnCtr).getNotes());
                        break;
                }
            }
        }
        loJSON = record.postTransaction("M00124000001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
    }
    @Test
    public void testCanceTransaction() {
        JSONObject loJSON;
        loJSON = record.openTransaction("M00124000002");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        if(record.getDetailModel().size()>0){
            for(int lnCtr = 0; lnCtr < record.getDetailModel().size(); lnCtr++){
                
                Assert.assertEquals("M00124000001", record.getDetailModel().get(lnCtr).getOrderNumber());
                switch(lnCtr){
                    case 0:
                        Assert.assertEquals(1, record.getDetailModel().get(lnCtr).getEntryNumber());
                        Assert.assertEquals("M00124000001", record.getDetailModel().get(lnCtr).getStockID());
                        Assert.assertEquals(3, record.getDetailModel().get(lnCtr).getQuantity());
                        Assert.assertEquals("230000012708", record.getDetailModel().get(lnCtr).getBarcode());
                        Assert.assertEquals("sasasasa", record.getDetailModel().get(lnCtr).getDescription());
                        Assert.assertEquals("Motorcycle", record.getDetailModel().get(lnCtr).getCategoryName());
                        Assert.assertEquals("Sports", record.getDetailModel().get(lnCtr).getCategoryName2());
                        Assert.assertEquals("Finished Products", record.getDetailModel().get(lnCtr).getCategoryType());
                        Assert.assertEquals("This is detail 01 notes entry 1.", record.getDetailModel().get(lnCtr).getNotes());
                        break;
                    case 1:
                        Assert.assertEquals(2, record.getDetailModel().get(lnCtr).getEntryNumber());
                        Assert.assertEquals("M00124000002", record.getDetailModel().get(lnCtr).getStockID());
                        Assert.assertEquals(4, record.getDetailModel().get(lnCtr).getQuantity());
                        Assert.assertEquals("5TL-E4170-00-00", record.getDetailModel().get(lnCtr).getBarcode());
                        Assert.assertEquals("DIAPHRAGM ASS'Y", record.getDetailModel().get(lnCtr).getDescription());
                        Assert.assertEquals("Motorcycle", record.getDetailModel().get(lnCtr).getCategoryName());
                        Assert.assertEquals("Spare Parts", record.getDetailModel().get(lnCtr).getCategoryName2());
                        Assert.assertEquals("Finished Products", record.getDetailModel().get(lnCtr).getCategoryType());
                        Assert.assertEquals("This is detail 02 notes entry 2.", record.getDetailModel().get(lnCtr).getNotes());
                        break;
                }
            }
        }
        loJSON = record.cancelTransaction("M00124000001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
    }
    @Test
    public void testVoidTransaction() {
        JSONObject loJSON;
        loJSON = record.openTransaction("M00124000002");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        if(record.getDetailModel().size()>0){
            for(int lnCtr = 0; lnCtr < record.getDetailModel().size(); lnCtr++){
                
                Assert.assertEquals("M00124000001", record.getDetailModel().get(lnCtr).getOrderNumber());
                switch(lnCtr){
                    case 0:
                        Assert.assertEquals(1, record.getDetailModel().get(lnCtr).getEntryNumber());
                        Assert.assertEquals("M00124000001", record.getDetailModel().get(lnCtr).getStockID());
                        Assert.assertEquals(3, record.getDetailModel().get(lnCtr).getQuantity());
                        Assert.assertEquals("230000012708", record.getDetailModel().get(lnCtr).getBarcode());
                        Assert.assertEquals("sasasasa", record.getDetailModel().get(lnCtr).getDescription());
                        Assert.assertEquals("Motorcycle", record.getDetailModel().get(lnCtr).getCategoryName());
                        Assert.assertEquals("Sports", record.getDetailModel().get(lnCtr).getCategoryName2());
                        Assert.assertEquals("Finished Products", record.getDetailModel().get(lnCtr).getCategoryType());
                        Assert.assertEquals("This is detail 01 notes entry 1.", record.getDetailModel().get(lnCtr).getNotes());
                        break;
                    case 1:
                        Assert.assertEquals(2, record.getDetailModel().get(lnCtr).getEntryNumber());
                        Assert.assertEquals("M00124000002", record.getDetailModel().get(lnCtr).getStockID());
                        Assert.assertEquals(4, record.getDetailModel().get(lnCtr).getQuantity());
                        Assert.assertEquals("5TL-E4170-00-00", record.getDetailModel().get(lnCtr).getBarcode());
                        Assert.assertEquals("DIAPHRAGM ASS'Y", record.getDetailModel().get(lnCtr).getDescription());
                        Assert.assertEquals("Motorcycle", record.getDetailModel().get(lnCtr).getCategoryName());
                        Assert.assertEquals("Spare Parts", record.getDetailModel().get(lnCtr).getCategoryName2());
                        Assert.assertEquals("Finished Products", record.getDetailModel().get(lnCtr).getCategoryType());
                        Assert.assertEquals("This is detail 02 notes entry 2.", record.getDetailModel().get(lnCtr).getNotes());
                        break;
                }
            }
        }
        loJSON = record.voidTransaction("M00124000001");
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

