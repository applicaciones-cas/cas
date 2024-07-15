/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.cas.inventory.base.InvMaster;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author User
 */
public class testInventoryMaster{

    static GRider instance;
    static InvMaster record;
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

        GRider instance = new GRider("gRider");

        if (!instance.logUser("gRider", "M001000001")){
            System.err.println(instance.getErrMsg());
            System.exit(1);
        }

        System.out.println("Connected");
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/");
        

        instance = MiscUtil.Connect();
        record = new InvMaster(instance, false);
        record.setRecordStatus("0123");
    }

    @Test
    public void testNewInventory() {
        JSONObject loJSON;
        
//        loJSON = record.SearchInventory("M00124000053", true);
        loJSON = record.newRecord();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }

        loJSON = record.getInvModel().setStockID("M00124000054");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        loJSON = record.getInvModel().setBarcode("230000012708");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("230000012708", record.getInvModel().getBarcode());
        
        loJSON = record.getInvModel().setDescription("sasasasa");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("sasasasa", record.getInvModel().getDescription());
        
        loJSON = record.getInvModel().setBriefDescription("");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("", record.getInvModel().getBriefDescription());
        
        loJSON = record.getInvModel().setAltBarcode("230000012708");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("230000012708", record.getInvModel().getAltBarcode());
        
        loJSON = record.getInvModel().setCategCd1("0001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("0001", record.getInvModel().getCategCd1());
        
        loJSON = record.getInvModel().setCategCd2("0003");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("0003", record.getInvModel().getCategCd2());
        
        loJSON = record.getInvModel().setCategCd3("0001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("0001", record.getInvModel().getCategCd3());

        loJSON = record.getInvModel().setCategCd4("0001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("0001", record.getInvModel().getCategCd4());
        
        loJSON = record.getInvModel().setBrandCode("M0012401");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("M0012401", record.getInvModel().getBrandCode());
        
        loJSON = record.getInvModel().setBrandName("Samasung");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("Samasung", record.getInvModel().getBrandName());
//        
        loJSON = record.getInvModel().setModelCode("");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("", record.getInvModel().getModelCode());
        
        loJSON = record.getInvModel().setModelName("");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("", (record.getInvModel().getModelName())==null?"":record.getInvModel().getModelName());
        
        loJSON = record.getInvModel().setColorCode("M001001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("M001001", record.getInvModel().getColorCode());
        
        loJSON = record.getInvModel().setColorName("ORANGE");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("ORANGE", record.getInvModel().getColorName());

        loJSON = record.getInvModel().setMeasureID("");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("", record.getInvModel().getMeasureID());
        
        loJSON = record.getInvModel().setMeasureName("");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("", (record.getInvModel().getMeasureName())==null?"":record.getInvModel().getModelName());

        loJSON = record.getInvModel().setUnitPrice(1800.00);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        Assert.assertEquals(1800.00, Double.parseDouble(record.getInvModel().getUnitPrice().toString()), delta);
        
        loJSON = record.getInvModel().setSelPrice(2000.00);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(2000.0, Double.parseDouble(record.getInvModel().getSelPrice().toString()), delta);
        
        loJSON = record.getInvModel().setDiscountLvl1(0.0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0.0, Double.parseDouble(record.getInvModel().getDiscountLevel1().toString()), delta);
        
        loJSON = record.getInvModel().setDiscountLvl2(0.0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0.0, Double.parseDouble(record.getInvModel().getDiscountLevel2().toString()), delta);
        
        loJSON = record.getInvModel().setDiscountLevel3(0.0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0.0, Double.parseDouble(record.getInvModel().getDiscountLevel3().toString()), delta);
        
        loJSON = record.getInvModel().setDealerDiscount(0.0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0.0, Double.parseDouble(record.getInvModel().getDealerDiscount().toString()), delta);
        
        loJSON = record.getInvModel().setMinLevel(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, Integer.parseInt(record.getInvModel().getMinLevel().toString()), 0);
        
        loJSON = record.getInvModel().setMaxLevel(1);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, Integer.parseInt(record.getInvModel().getMaxLevel().toString()), 0);
        
        loJSON = record.getInvModel().setComboInv("0");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("0", record.getInvModel().getComboInv());
        
        loJSON = record.getInvModel().setSerialze("1");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("1", record.getInvModel().getSerialze());

        loJSON = record.getInvModel().setWthPromo("0");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("0", record.getInvModel().getWthPromo());
        
        loJSON = record.getInvModel().setUnitType("");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("", record.getInvModel().getUnitType());
        
        loJSON = record.getInvModel().setInvStatx("1");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        Assert.assertEquals("1", record.getInvModel().getInvStatx());
        loJSON = record.getInvModel().setShlfLife(1);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(1, record.getInvModel().getShlfLife(), 0);
        
        loJSON = record.getInvModel().setSupersed("");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("", record.getInvModel().getSupersed());
        
        loJSON = record.getModel().setLocatnCode("M001241");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("M001241", record.getModel().getLocatnCode());
        
        loJSON = record.getModel().setLocationnName("BLDG. 1-A");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("BLDG. 1-A", record.getModel().getLocationnName());
        
        
         loJSON = record.getModel().setWareHouseID("001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("001", record.getModel().getWareHouseID());
        
        
         loJSON = record.getModel().setWareHouseNm("Building 1");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("Building 1", record.getModel().getWareHouseNm());
        
        
         loJSON = record.getModel().setBinNumber(1);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(1, record.getModel().getBinNumber());
        
        
         loJSON = record.getModel().setBegQtyxx(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, record.getModel().getBegQtyxx());
        
        
         loJSON = record.getModel().setClassify("F");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        Assert.assertEquals("F", record.getModel().getClassify());
        
         loJSON = record.getModel().setAvgMonSl(0.00);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        Assert.assertEquals(0.00, record.getModel().getAvgMonSl());
        
         loJSON = record.getModel().setResvOrdr(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        Assert.assertEquals(0, record.getModel().getResvOrdr());
        
         loJSON = record.getModel().setQtyOnHnd(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        Assert.assertEquals(0, record.getModel().getQtyOnHnd());
        
        loJSON = record.getInvModel().setRecdStat("1");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("1", record.getInvModel().getRecdStat());
        
        loJSON = record.getModel().setStockID("M00124000054");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        } 
        
        Assert.assertEquals("M00124000054", record.getModel().getStockID());
        
        loJSON = record.getModel().setBranchCd("M001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        } 
        
        Assert.assertEquals("M001", record.getModel().getBranchCd());
        TransNox = record.getInvModel().getStockID();
//        loJSON = record.saveRecord();
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }
    }
        
     @Test
    public void testOpenInventory() {
        JSONObject loJSON;
        System.out.println(TransNox);
        loJSON = record.openRecord("M00124000054");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("230000012708", record.getInvModel().getBarcode());
        Assert.assertEquals("230000012708", record.getInvModel().getAltBarcode());
        Assert.assertEquals("sasasasa", record.getInvModel().getDescription());
        Assert.assertEquals("", record.getInvModel().getBriefDescription());
        Assert.assertEquals("0001", record.getInvModel().getCategCd1());
        Assert.assertEquals("0003", record.getInvModel().getCategCd2());
        Assert.assertEquals("0001", record.getInvModel().getCategCd3());
        Assert.assertEquals("0001", record.getInvModel().getCategCd4());
        
        Assert.assertEquals("M0012401", record.getInvModel().getBrandCode());
        Assert.assertEquals("Samasung", record.getInvModel().getBrandName());
        
        Assert.assertEquals("", record.getInvModel().getModelCode());
        Assert.assertEquals("", (record.getInvModel().getModelName())==null?"":record.getInvModel().getModelName());
        
        Assert.assertEquals("M001001", record.getInvModel().getColorCode());
        Assert.assertEquals("ORANGE", record.getInvModel().getColorName());
        
        Assert.assertEquals("", record.getInvModel().getMeasureID());
        Assert.assertEquals("", (record.getInvModel().getMeasureName())==null?"":record.getInvModel().getMeasureName());
        double delta = 0.00;
        Assert.assertEquals(1800.0, Double.parseDouble(record.getInvModel().getUnitPrice().toString()), delta);
        Assert.assertEquals(2000.0, Double.parseDouble(record.getInvModel().getSelPrice().toString()), delta);
        
        Assert.assertEquals(0.0, Double.parseDouble(record.getInvModel().getDiscountLevel1().toString()), delta);
        Assert.assertEquals(0.0, Double.parseDouble(record.getInvModel().getDiscountLevel2().toString()), delta);
        Assert.assertEquals(0.0, Double.parseDouble(record.getInvModel().getDiscountLevel3().toString()), delta);
        Assert.assertEquals(0.0, Double.parseDouble(record.getInvModel().getDealerDiscount().toString()), delta);
        
        Assert.assertEquals(0, Integer.parseInt(record.getInvModel().getMinLevel().toString()), 0);
        Assert.assertEquals(0, Integer.parseInt(record.getInvModel().getMaxLevel().toString()), 0);
        Assert.assertEquals("0", record.getInvModel().getComboInv());
        Assert.assertEquals("1", record.getInvModel().getSerialze());
        Assert.assertEquals("0", record.getInvModel().getWthPromo());
        Assert.assertEquals("", record.getInvModel().getUnitType());
        Assert.assertEquals("1", record.getInvModel().getInvStatx());
        Assert.assertEquals(1, record.getInvModel().getShlfLife(), 0);
        Assert.assertEquals("", record.getInvModel().getSupersed());
        Assert.assertEquals("1", record.getInvModel().getRecdStat());
        
        
        Assert.assertEquals("M001242", record.getModel().getLocatnCode());
        Assert.assertEquals("BLDG. 1-B", record.getModel().getLocationnName());
        Assert.assertEquals("001", record.getModel().getWareHouseID());        
        Assert.assertEquals("Building 1", record.getModel().getWareHouseNm());
        Assert.assertEquals(1, Integer.parseInt(record.getModel().getBinNumber().toString()), 0);        
        
        
        Assert.assertEquals(0, record.getModel().getBegQtyxx());        
        Assert.assertEquals("F", record.getModel().getClassify());
        Assert.assertEquals(0, Integer.parseInt(record.getModel().getAvgMonSl().toString()));        
        Assert.assertEquals(0, Integer.parseInt(record.getModel().getResvOrdr().toString()));   
        Assert.assertEquals(0, Integer.parseInt(record.getModel().getQtyOnHnd().toString()));
    }
     @Test
    public void testUpdateInventory() {
        JSONObject loJSON;
        loJSON = record.openRecord("M00124000054");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        loJSON = record.updateRecord();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        record.getModel().setLocatnCode("M001242");
        record.getModel().setLocationnName("BLDG. 1-B");
        
        Assert.assertEquals("M001242", record.getModel().getLocatnCode());
        Assert.assertEquals("BLDG. 1-B", record.getModel().getLocationnName());

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
