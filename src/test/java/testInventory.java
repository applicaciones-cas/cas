/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.inventory.base.Inventory;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * @author User
 */
public class testInventory{

    static GRider instance;
    static Inventory record;
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
    public void testNewInventory() {
        JSONObject loJSON;
        
        loJSON = record.newRecord();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }

//        loJSON = record.getModel().setStockID("M00124000001");
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }
        loJSON = record.getModel().setBarcode("230000012708");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("230000012708", record.getModel().getBarcode());
        
        loJSON = record.getModel().setDescription("sasasasa");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("sasasasa", record.getModel().getDescription());
        
        loJSON = record.getModel().setBriefDescription("");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("", record.getModel().getBriefDescription());
        
        loJSON = record.getModel().setAltBarcode("230000012708");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("230000012708", record.getModel().getAltBarcode());
        
        loJSON = record.getModel().setCategCd1("0001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("0001", record.getModel().getCategCd1());
        
        loJSON = record.getModel().setCategCd2("0005");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("0005", record.getModel().getCategCd2());
        
        loJSON = record.getModel().setCategCd3("0001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("0001", record.getModel().getCategCd3());

        loJSON = record.getModel().setCategCd4("0001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("0001", record.getModel().getCategCd4());
        
        loJSON = record.getModel().setBrandID("M0012401");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("M0012401", record.getModel().getBrandID());
        
        loJSON = record.getModel().setBrandName("Samasung");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("Samasung", record.getModel().getBrandName());
//        
        loJSON = record.getModel().setModelID("M0012401");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("M0012401", record.getModel().getModelID());
        
        loJSON = record.getModel().setModelName("RAIDER GTR 180");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("RAIDER GTR 180", (record.getModel().getModelName())==null?"":record.getModel().getModelName());
        
        loJSON = record.getModel().setColorID("M001001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("M001001", record.getModel().getColorID());
        
        loJSON = record.getModel().setColorName("ORANGE");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("ORANGE", record.getModel().getColorName());

        loJSON = record.getModel().setMeasureID("");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("", record.getModel().getMeasureID());
        
        loJSON = record.getModel().setMeasureName("");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("", (record.getModel().getMeasureName())==null?"":record.getModel().getMeasureName());

        loJSON = record.getModel().setUnitPrice(1800.00);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        Assert.assertEquals(1800.00, Double.parseDouble(record.getModel().getUnitPrice().toString()), delta);
        
        loJSON = record.getModel().setSelPrice(2000.00);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(2000.0, Double.parseDouble(record.getModel().getSelPrice().toString()), delta);
        
        loJSON = record.getModel().setDiscountLvl1(0.0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0.0, Double.parseDouble(record.getModel().getDiscountLevel1().toString()), delta);
        
        loJSON = record.getModel().setDiscountLvl2(0.0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0.0, Double.parseDouble(record.getModel().getDiscountLevel2().toString()), delta);
        
        loJSON = record.getModel().setDiscountLevel3(0.0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0.0, Double.parseDouble(record.getModel().getDiscountLevel3().toString()), delta);
        
        loJSON = record.getModel().setDealerDiscount(0.0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0.0, Double.parseDouble(record.getModel().getDealerDiscount().toString()), delta);
        
        loJSON = record.getModel().setMinLevel(0);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(0, Integer.parseInt(record.getModel().getMinLevel().toString()), 0);
        
        loJSON = record.getModel().setMaxLevel(1);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(1, Integer.parseInt(record.getModel().getMaxLevel().toString()), 0);
        
        loJSON = record.getModel().setComboInv("0");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("0", record.getModel().getComboInv());
        
        loJSON = record.getModel().setSerialze("1");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("1", record.getModel().getSerialze());

        loJSON = record.getModel().setWthPromo("0");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("0", record.getModel().getWthPromo());
        
        loJSON = record.getModel().setUnitType("");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("", record.getModel().getUnitType());
        
        loJSON = record.getModel().setInvStatx("1");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        Assert.assertEquals("1", record.getModel().getInvStatx());
        loJSON = record.getModel().setShlfLife(1);
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals(1, record.getModel().getShlfLife(), 0);
        
        loJSON = record.getModel().setSupersed("");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("", record.getModel().getSupersed());
        
        loJSON = record.getModel().setRecdStat("1");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("1", record.getModel().getRecdStat());
        TransNox = record.getModel().getStockID();
        loJSON = record.saveRecord();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        
    }
     @Test
    public void testOpenInventory() {
        JSONObject loJSON;
        System.out.println(TransNox);
        loJSON = record.openRecord("M00124000086");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        Assert.assertEquals("230000012708", record.getModel().getBarcode());
        Assert.assertEquals("230000012708", record.getModel().getAltBarcode());
        Assert.assertEquals("sasasasa", record.getModel().getDescription());
        Assert.assertEquals("", record.getModel().getBriefDescription());
        Assert.assertEquals("0001", record.getModel().getCategCd1());
        Assert.assertEquals("0003", record.getModel().getCategCd2());
        Assert.assertEquals("0001", record.getModel().getCategCd3());
        Assert.assertEquals("0001", record.getModel().getCategCd4());
        
        Assert.assertEquals("M0012401", record.getModel().getBrandID());
        Assert.assertEquals("Samasung", record.getModel().getBrandName());
        
        Assert.assertEquals("", record.getModel().getModelID());
        Assert.assertEquals("", (record.getModel().getModelName())==null?"":record.getModel().getModelName());
        
        Assert.assertEquals("M001001", record.getModel().getColorID());
        Assert.assertEquals("ORANGE", record.getModel().getColorName());
        
        Assert.assertEquals("", record.getModel().getMeasureID());
        Assert.assertEquals("", (record.getModel().getMeasureName())==null?"":record.getModel().getModelName());
       
        Assert.assertEquals(1800.0, Double.parseDouble(record.getModel().getUnitPrice().toString()), delta);
        Assert.assertEquals(2000.0, Double.parseDouble(record.getModel().getSelPrice().toString()), delta);
        
        Assert.assertEquals(0.0, Double.parseDouble(record.getModel().getDiscountLevel1().toString()), delta);
        Assert.assertEquals(0.0, Double.parseDouble(record.getModel().getDiscountLevel2().toString()), delta);
        Assert.assertEquals(0.0, Double.parseDouble(record.getModel().getDiscountLevel3().toString()), delta);
        Assert.assertEquals(0.0, Double.parseDouble(record.getModel().getDealerDiscount().toString()), delta);
        
        Assert.assertEquals(0, Integer.parseInt(record.getModel().getMinLevel().toString()), 0);
        Assert.assertEquals(1, Integer.parseInt(record.getModel().getMaxLevel().toString()), 0);
        Assert.assertEquals("0", record.getModel().getComboInv());
        Assert.assertEquals("1", record.getModel().getSerialze());
        Assert.assertEquals("0", record.getModel().getWthPromo());
        Assert.assertEquals("", record.getModel().getUnitType());
        Assert.assertEquals("1", record.getModel().getInvStatx());
        Assert.assertEquals(1, record.getModel().getShlfLife(), 0);
        Assert.assertEquals("", record.getModel().getSupersed());
        Assert.assertEquals("1", record.getModel().getRecdStat());
        
    }
     @Test
    public void testUpdateInventory() {
        JSONObject loJSON;
        loJSON = record.openRecord("M00124000051");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        loJSON = record.updateRecord();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        record.getSubUnit().getMaster().get(0).setQuantity(10);
        record.getSubUnit().getMaster().get(1).setQuantity(5);
        
        Assert.assertEquals(10, record.getSubUnit().getMaster().get(0).getQuantity());
        Assert.assertEquals(5, record.getSubUnit().getMaster().get(1).getQuantity());

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

