
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.cas.inventory.models.PurchaseOrder;
import org.guanzon.cas.inventory.stock.InvStockRequest;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author unclejo
 */


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testInvStockRequest {
 
    static GRider instance;
    static InvStockRequest record;
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
        record = new InvStockRequest(instance, false);
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

        loJSON = record.newTransaction();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }    
        
        lsTransNox = record.getMasterModel().getTransactionNumber();
        //set master information
        loJSON = record.getMasterModel().setBranchCode("M001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(lsTransNox, record.getMasterModel().getTransactionNumber());
        
        loJSON = record.getMasterModel().setCategoryCode("0001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("0001", record.getMasterModel().getCategoryCode());
        
        loJSON = record.getMasterModel().setTransaction(SQLUtil.toDate("2024-08-24",SQLUtil.FORMAT_SHORT_DATE));
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(CommonUtils.toDate("2024-08-24"), record.getMasterModel().getTransaction());
        
        loJSON = record.getMasterModel().setReferenceNumber("");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("", record.getMasterModel().getReferenceNumber());
        
        loJSON = record.getMasterModel().setRemarks("This is a Test for Purchase Order");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("This is a Test for Purchase Order", record.getMasterModel().getRemarks());
        
        loJSON = record.getMasterModel().setIssNotes("This is a Notes");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("This is a Notes", record.getMasterModel().getIssNotes());
        
        loJSON = record.getMasterModel().setCurrentInventory(12);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(12, record.getMasterModel().getCurrentInventory(),0.0);
        
        loJSON = record.getMasterModel().setEstimatedInventory(12);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(12, record.getMasterModel().getEstimatedInventory(),0.0);
        
        loJSON = record.getMasterModel().setApproved("");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("", record.getMasterModel().getApproved());
        
        loJSON = record.getMasterModel().setApproveCode(null);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(null, record.getMasterModel().getApproveCode());
        
        loJSON = record.getMasterModel().setEntryNumber(1);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(1, record.getMasterModel().getEntryNumber(),0);
        
        loJSON = record.getMasterModel().setSourceCode("SlSl");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("SlSl", record.getMasterModel().getSourceCode());
        
        loJSON = record.getMasterModel().setSourceNumber("M00124000001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("M00124000001", record.getMasterModel().getSourceNumber());
        
        loJSON = record.getMasterModel().setConfirm("0");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("0", record.getMasterModel().getConfirm());
        
        loJSON = record.getMasterModel().setTransactionStatus(TransactionStatus.STATE_OPEN);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(TransactionStatus.STATE_OPEN, record.getMasterModel().getTransactionStatus());
        
        loJSON = record.getMasterModel().setStartEncDate(instance.getServerDate());
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(instance.getServerDate(), record.getMasterModel().getStartEncDate());
        
//        used only in Posting method
//        loJSON = record.getMasterModel().setPostedDate(instance.getServerDate());
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }
        loJSON = record.getMasterModel().setModifiedBy(instance.getUserID());
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(instance.getUserID(), record.getMasterModel().getModifiedBy());
        
        loJSON = record.getMasterModel().setModifiedDate(instance.getServerDate());
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(instance.getServerDate(), record.getMasterModel().getModifiedDate());
        
        //set detail information
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setTransactionNumber(record.getMasterModel().getTransactionNumber());
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(record.getMasterModel().getTransactionNumber(), record.getDetailModel().get(record.getDetailModel().size()-1).getTransactionNumber());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setEntryNumber(record.getDetailModel().size());
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(record.getDetailModel().size(), record.getDetailModel().get(record.getDetailModel().size()-1).getEntryNumber());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setStockID("M00124000004");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("M00124000004", record.getDetailModel().get(record.getDetailModel().size()-1).getStockID());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setQuantity(5);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(5, record.getDetailModel().get(record.getDetailModel().size()-1).getQuantity());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setClassify("F");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setRecordOrder(5);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(5, record.getDetailModel().get(record.getDetailModel().size()-1).getRecordOrder());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setQuantityOnHand(146);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(146, record.getDetailModel().get(record.getDetailModel().size()-1).getQuantityOnHand());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setReservedOrder(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getDetailModel().get(record.getDetailModel().size()-1).getReservedOrder());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setBackOrder(5);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(5, record.getDetailModel().get(record.getDetailModel().size()-1).getBackOrder());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setOnTransit(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getDetailModel().get(record.getDetailModel().size()-1).getOnTransit());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setAverageMonthlySalary(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getDetailModel().get(record.getDetailModel().size()-1).getAverageMonthlySalary());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setMaximumLevel(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getDetailModel().get(record.getDetailModel().size()-1).getMaximumLevel());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setApproved(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getDetailModel().get(record.getDetailModel().size()-1).getApproved());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setCancelled(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getDetailModel().get(record.getDetailModel().size()-1).getCancelled());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setIssueQuantity(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getDetailModel().get(record.getDetailModel().size()-1).getIssueQuantity());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setOrderQuantity(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getDetailModel().get(record.getDetailModel().size()-1).getOrderQuantity());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setAllocatedQuantity(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getDetailModel().get(record.getDetailModel().size()-1).getAllocatedQuantity());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setReceived(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getDetailModel().get(record.getDetailModel().size()-1).getReceived());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setNotes("This is detail 01 notes entry 1.");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("This is detail 01 notes entry 1.", record.getDetailModel().get(record.getDetailModel().size()-1).getNotes());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setModifiedDate(instance.getServerDate());
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(instance.getServerDate(), record.getDetailModel().get(record.getDetailModel().size()-1).getModifiedDate());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setBarcode("5MX-E2119-00-00");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("5MX-E2119-00-00", record.getDetailModel().get(record.getDetailModel().size()-1).getBarcode());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setDescription("SEAL, VALVE STEM");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("SEAL, VALVE STEM", record.getDetailModel().get(record.getDetailModel().size()-1).getDescription());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setCategoryName("Motorcycle");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("Motorcycle", record.getDetailModel().get(record.getDetailModel().size()-1).getCategoryName());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setCategoryName2("Spare Parts");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("Spare Parts", record.getDetailModel().get(record.getDetailModel().size()-1).getCategoryName2());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setCategoryType("Finished Products");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("Finished Products", record.getDetailModel().get(record.getDetailModel().size()-1).getCategoryType());
        
        //set detail 2 information
        loJSON = record.AddModelDetail();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setTransactionNumber(record.getMasterModel().getTransactionNumber());
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(record.getMasterModel().getTransactionNumber(), record.getDetailModel().get(record.getDetailModel().size()-1).getTransactionNumber());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setEntryNumber(record.getDetailModel().size());
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(record.getDetailModel().size(), record.getDetailModel().get(record.getDetailModel().size()-1).getEntryNumber());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setStockID("M00124000004");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("M00124000004", record.getDetailModel().get(record.getDetailModel().size()-1).getStockID());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setQuantity(5);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(5, record.getDetailModel().get(record.getDetailModel().size()-1).getQuantity());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setClassify("F");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setRecordOrder(5);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(5, record.getDetailModel().get(record.getDetailModel().size()-1).getRecordOrder());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setQuantityOnHand(146);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(146, record.getDetailModel().get(record.getDetailModel().size()-1).getQuantityOnHand());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setReservedOrder(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getDetailModel().get(record.getDetailModel().size()-1).getReservedOrder());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setBackOrder(5);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(5, record.getDetailModel().get(record.getDetailModel().size()-1).getBackOrder());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setOnTransit(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getDetailModel().get(record.getDetailModel().size()-1).getOnTransit());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setAverageMonthlySalary(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getDetailModel().get(record.getDetailModel().size()-1).getAverageMonthlySalary());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setMaximumLevel(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getDetailModel().get(record.getDetailModel().size()-1).getMaximumLevel());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setApproved(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getDetailModel().get(record.getDetailModel().size()-1).getApproved());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setCancelled(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getDetailModel().get(record.getDetailModel().size()-1).getCancelled());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setIssueQuantity(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getDetailModel().get(record.getDetailModel().size()-1).getIssueQuantity());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setOrderQuantity(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getDetailModel().get(record.getDetailModel().size()-1).getOrderQuantity());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setAllocatedQuantity(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getDetailModel().get(record.getDetailModel().size()-1).getAllocatedQuantity());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setReceived(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getDetailModel().get(record.getDetailModel().size()-1).getReceived());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setNotes("This is detail 02 notes entry 2.");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("This is detail 02 notes entry 2.", record.getDetailModel().get(record.getDetailModel().size()-1).getNotes());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setModifiedDate(instance.getServerDate());
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(instance.getServerDate(), record.getDetailModel().get(record.getDetailModel().size()-1).getModifiedDate());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setBarcode("5MX-E2119-00-00");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("5MX-E2119-00-00", record.getDetailModel().get(record.getDetailModel().size()-1).getBarcode());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setDescription("SEAL, VALVE STEM");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("SEAL, VALVE STEM", record.getDetailModel().get(record.getDetailModel().size()-1).getDescription());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setCategoryName("Motorcycle");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("Motorcycle", record.getDetailModel().get(record.getDetailModel().size()-1).getCategoryName());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setCategoryName2("Spare Parts");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("Spare Parts", record.getDetailModel().get(record.getDetailModel().size()-1).getCategoryName2());
        
        loJSON = record.getDetailModel().get(record.getDetailModel().size()-1).setCategoryType("Finished Products");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("Finished Products", record.getDetailModel().get(record.getDetailModel().size()-1).getCategoryType());
//        
        
        loJSON = record.saveTransaction();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
    }
    
    @Test
    public void testOpenDetail() {
         JSONObject loJSON;
        loJSON = record.searchTransaction("sTransNox", "M00124000001", true);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        if(record.getDetailModel().size()>0){
            for(int lnCtr = 0; lnCtr < record.getDetailModel().size(); lnCtr++){
                
                Assert.assertEquals("M00124000001", record.getDetailModel().get(lnCtr).getTransactionNumber());
                switch(lnCtr){
                    case 0:
                        Assert.assertEquals(1, record.getDetailModel().get(lnCtr).getEntryNumber());
                        Assert.assertEquals("M00124000001", record.getDetailModel().get(lnCtr).getStockID());
                        Assert.assertEquals(8, record.getDetailModel().get(lnCtr).getQuantity());
                        Assert.assertEquals("230000012708", record.getDetailModel().get(lnCtr).getBarcode());
                        Assert.assertEquals("sasasasa", record.getDetailModel().get(lnCtr).getDescription());
                        Assert.assertEquals("Motorcycle", record.getDetailModel().get(lnCtr).getCategoryName());
                        Assert.assertEquals("Sports", record.getDetailModel().get(lnCtr).getCategoryName2());
                        Assert.assertEquals("Sports", record.getDetailModel().get(lnCtr).getCategoryType());
                        Assert.assertEquals("This is detail 01 notes entry 1.", record.getDetailModel().get(lnCtr).getNotes());
                        break;
                    case 1:
                        Assert.assertEquals(2, record.getDetailModel().get(lnCtr).getEntryNumber());
                        Assert.assertEquals("M00124000002", record.getDetailModel().get(lnCtr).getStockID());
                        Assert.assertEquals(8, record.getDetailModel().get(lnCtr).getQuantity());
                        Assert.assertEquals("5TL-E4170-00-00", record.getDetailModel().get(lnCtr).getBarcode());
                        Assert.assertEquals("DIAPHRAGM ASS'Y", record.getDetailModel().get(lnCtr).getDescription());
                        Assert.assertEquals("Motorcycle", record.getDetailModel().get(lnCtr).getCategoryName());
                        Assert.assertEquals("Spare Parts", record.getDetailModel().get(lnCtr).getCategoryName2());
                        Assert.assertEquals("Spare Parts", record.getDetailModel().get(lnCtr).getCategoryType());
                        Assert.assertEquals("This is detail 02 notes entry 2.", record.getDetailModel().get(lnCtr).getNotes());
                        break;
                    case 2:
                        Assert.assertEquals(3, record.getDetailModel().get(lnCtr).getEntryNumber());
                        Assert.assertEquals("M00124000003", record.getDetailModel().get(lnCtr).getStockID());
                        Assert.assertEquals(8, record.getDetailModel().get(lnCtr).getQuantity());
                        Assert.assertEquals("5LW-E1351-00-00", record.getDetailModel().get(lnCtr).getBarcode());
                        Assert.assertEquals("GASKET,CYLINDER 1", record.getDetailModel().get(lnCtr).getDescription());
                        Assert.assertEquals("Motorcycle", record.getDetailModel().get(lnCtr).getCategoryName());
                        Assert.assertEquals("Spare Parts", record.getDetailModel().get(lnCtr).getCategoryName2());
                        Assert.assertEquals("Spare Parts", record.getDetailModel().get(lnCtr).getCategoryType());
                        Assert.assertEquals("This is detail 03 notes entry 3.", record.getDetailModel().get(lnCtr).getNotes());
                        break;
                    default:
                        break;
                }
            }
        }
        
    }
    
    @Test
    public void testUpdateDetail() {
         JSONObject loJSON;
        loJSON = record.openTransaction("M00124000001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        loJSON = record.updateTransaction();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        record.getDetailModel().get(0).setQuantity(8);
        Assert.assertEquals(8, record.getDetailModel().get(1).getQuantity());
        
        record.getDetailModel().get(1).setQuantity(8);
        Assert.assertEquals(8, record.getDetailModel().get(1).getQuantity());
        
        record.getDetailModel().get(2).setQuantity(8);
        Assert.assertEquals(8, record.getDetailModel().get(1).getQuantity());
        
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
