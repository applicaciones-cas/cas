/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.cas.inventory.base;

import java.sql.Connection;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.appdriver.iface.GRecord;
import org.guanzon.cas.inventory.models.Model_Inv_Master;
import org.guanzon.cas.inventory.models.Model_Inventory;
import org.json.simple.JSONObject;

/**
 *
 * @author User
 */
public class InvMaster implements GRecord{
    GRider poGRider;
    boolean pbWthParent;
    String psBranchCd;
    boolean pbWtParent;
    public JSONObject poJSON;
    
    int pnEditMode;
    String psMessagex;
    String psTranStatus;
    
    private Model_Inv_Master poModel;
    private Inventory poInventory;
    private InvSerial poSerial;
    private InvLedger poLedger;

    
    public InvMaster(GRider foGRider, boolean fbWthParent) {
        poGRider = foGRider;
        pbWthParent = fbWthParent;

        poModel = new Model_Inv_Master(foGRider);
        poInventory = new Inventory(foGRider, pbWthParent);
        poSerial = new InvSerial(foGRider, pbWthParent);
        poLedger = new InvLedger(foGRider, pbWthParent);
        pnEditMode = EditMode.UNKNOWN;
    }

    
    @Override
    public JSONObject setMaster(int fnCol, Object foData) {
        
        JSONObject obj = new JSONObject();
        obj.put("pnEditMode", pnEditMode);
        if (pnEditMode != EditMode.UNKNOWN){
            // Don't allow specific fields to assign values
            if(!(fnCol == poModel.getColumn("cRecdStat") ||
                fnCol == poModel.getColumn("sModified") ||
                fnCol == poModel.getColumn("dModified"))){
               obj =  poModel.setValue(fnCol, foData);
               
//                obj.put(fnCol, pnEditMode);
            }
        }
        return obj;
    }

    @Override
    public JSONObject setMaster(String fsCol, Object foData) {
        return setMaster(poModel.getColumn(fsCol), foData);
    }
    @Override
    public int getEditMode() {
        return pnEditMode;
    }

    private Connection setConnection(){
        Connection foConn;
        
        if (pbWthParent){
            foConn = (Connection) poGRider.getConnection();
            if (foConn == null) foConn = (Connection) poGRider.doConnect();
        }else foConn = (Connection) poGRider.doConnect();
        
        return foConn;
    }
    
    private JSONObject checkData(JSONObject joValue){
        if(pnEditMode == EditMode.READY || pnEditMode == EditMode.UPDATE){
            if(joValue.containsKey("continue")){
                if(true == (boolean)joValue.get("continue")){
                    joValue.put("result", "success");
                    joValue.put("message", "Record saved successfully.");
                }
            }
        }
        return joValue;
    }

    @Override
    public void setRecordStatus(String fsValue) {
        psTranStatus = fsValue;
    }

    @Override
    public Object getMaster(int fnCol) {
        if(pnEditMode == EditMode.UNKNOWN)
            return null;
        else 
            return poModel.getValue(fnCol);
    }

    @Override
    public Object getMaster(String fsCol) {
        return getMaster(poModel.getColumn(fsCol));
    }
    
    @Override
    public JSONObject newRecord() {
        
            poJSON = new JSONObject();
        try{
            
            poModel = new Model_Inv_Master(poGRider);
            Connection loConn = null;
            loConn = setConnection();
            poModel.newRecord();

            //init detail
            //init detail
//            poLedger = new ArrayList<>();
            
            if (poModel == null){
                
                poJSON.put("result", "error");
                poJSON.put("message", "initialized new record failed.");
                return poJSON;
            }else{
                poJSON.put("result", "success");
                poJSON.put("message", "initialized new record.");
                pnEditMode = EditMode.ADDNEW;
            }
               
        }catch(NullPointerException e){
            
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
        }
        
        return poJSON;
    }

    @Override
    public JSONObject openRecord(String fsValue) {
        
        pnEditMode = EditMode.READY;
        poJSON = new JSONObject();
        
        poModel = new Model_Inv_Master(poGRider);
        poJSON = poModel.openRecord(fsValue);
        
        return poJSON;
    }

    @Override
    public JSONObject updateRecord() {
        
        
        poJSON = new JSONObject();
        if (pnEditMode != EditMode.READY && pnEditMode != EditMode.UPDATE){
            poJSON.put("result", "error");
            poJSON.put("message", "Invalid edit mode.");
            return poJSON;
        }
        pnEditMode = EditMode.UPDATE;
        poJSON.put("result", "success");
        poJSON.put("message", "Update mode success.");
        return poJSON;
    }

    @Override
    public JSONObject saveRecord() {
        poJSON = new JSONObject();
        if (!pbWthParent) {
            poGRider.beginTrans();
        }
//        ValidatorInterface validator = ValidatorFactory.make(ValidatorFactory.TYPE.AR_Client_Master, poModel);
//        poModel.setModifiedDate(poGRider.getServerDate());
//
//        if (!validator.isEntryOkay()){
//            poJSON.put("result", "error");
//            poJSON.put("message", validator.getMessage());
//            return poJSON;
//
//        }
        poJSON = poModel.saveRecord();

        if ("success".equals((String) poJSON.get("result"))) {
            if (!pbWthParent) {
                poGRider.commitTrans();
            }
        } else {
            if (!pbWthParent) {
                poGRider.rollbackTrans();
            }
        }

        return poJSON;
    }

    @Override 
    public JSONObject deleteRecord(String fsValue) {
         poJSON = new JSONObject();

        poJSON = new JSONObject();
        if (pnEditMode == EditMode.READY || pnEditMode == EditMode.UPDATE) {
            if (poGRider.getUserLevel() < UserRight.SUPERVISOR){
                poJSON.put("result", "error");
                poJSON.put("message", "User is not allowed delete transaction.");
                return poJSON;
            }
            String lsSQL = "DELETE FROM " + poModel.getTable()+
                                " WHERE sClientID = " + SQLUtil.toSQL(fsValue);

            if (!lsSQL.equals("")){
                if (poGRider.executeQuery(lsSQL, poModel.getTable(), poGRider.getBranchCode(), "") > 0) {
                    poJSON.put("result", "success");
                    poJSON.put("message", "Record deleted successfully.");
                } else {
                    poJSON.put("result", "error");
                    poJSON.put("message", poGRider.getErrMsg());
                }
            }
        }else {
            poJSON.put("result", "error");
            poJSON.put("message", "Invalid update mode. Unable to save record.");
            return poJSON;
        }
        
        return poJSON;
    }

    @Override
    public JSONObject deactivateRecord(String string) {
        poJSON = new JSONObject();

        if (poModel.getEditMode() == EditMode.READY || poModel.getEditMode() == EditMode.UPDATE) {
            poJSON = poModel.setRecdStat(TransactionStatus.STATE_CLOSED);

            if ("error".equals((String) poJSON.get("result"))) {
                return poJSON;
            }

            poJSON = poModel.saveRecord();
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
        }
        return poJSON;
    }

    @Override
    public JSONObject activateRecord(String string) {
        
        poJSON = new JSONObject();

        if (poModel.getEditMode() == EditMode.READY || poModel.getEditMode() == EditMode.UPDATE) {
            poJSON = poModel.setRecdStat(TransactionStatus.STATE_CLOSED);

            if ("error".equals((String) poJSON.get("result"))) {
                return poJSON;
            }

            poJSON = poModel.saveRecord();
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
        }
        return poJSON;
    }

    @Override
    public JSONObject searchRecord(String fsValue, boolean fbByCode) {
      String lsCondition = "";

        if (psTranStatus.length() > 1) {
            for (int lnCtr = 0; lnCtr <= psTranStatus.length() - 1; lnCtr++) {
                lsCondition += ", " + SQLUtil.toSQL(Character.toString(psTranStatus.charAt(lnCtr)));
            }

            lsCondition = "cRecdStat IN (" + lsCondition.substring(2) + ")";
        } else {
            lsCondition = "cRecdStat = " + SQLUtil.toSQL(psTranStatus);
        }
        String lsSQL = poModel.makeSelectSQL();
        if (fbByCode)
            lsSQL = MiscUtil.addCondition(lsSQL, "sStockIDx = " + SQLUtil.toSQL(fsValue)) + " AND " + lsCondition;
        else
            lsSQL = MiscUtil.addCondition(lsSQL, "sDescript LIKE " + SQLUtil.toSQL("%" + fsValue + "%")) + " AND " + lsCondition;

    

        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                fsValue,
                "Stock ID»Barcode»Name",
                "sStockIDx»sBarCodex»sDescript",
                "sStockIDx»sBarCodex»sDescript",
                fbByCode ? 0 : 1);

        if (poJSON
                != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"));
        } else {
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
            return poJSON;
        }
    }
    @Override
    public Model_Inv_Master getModel() {
        return poModel;
    }
    
    private JSONObject openInvRecord(String fsStockIDx){
        poJSON = new JSONObject();
        if (fsStockIDx.equals("")){
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
            return poJSON;
        }
        
        return poInventory.openRecord(fsStockIDx);
    }
    public Object getInventory(String fsCol){
        return getInventory(poModel.getColumn(fsCol));
    }
    public Object getInventory(int fnCol){
        return poInventory.getMaster(fnCol);
    }
    
    
    private String getSQ_Inventory(){
        String lsSQL = "SELECT " +
                            "  a.sStockIDx" +
                            ", a.sBarCodex" + 
                            ", a.sDescript" + 
                            ", a.sBriefDsc" + 
                            ", a.sAltBarCd" + 
                            ", a.sCategCd1" + 
                            ", a.sCategCd2" + 
                            ", a.sCategCd3" + 
                            ", a.sCategCd4" + 
                            ", a.sBrandCde" + 
                            ", a.sModelCde" + 
                            ", a.sColorCde" + 
                            ", a.sInvTypCd" + 
                            ", a.nUnitPrce" + 
                            ", a.nSelPrice" + 
                            ", a.nDiscLev1" + 
                            ", a.nDiscLev2" + 
                            ", a.nDiscLev3" + 
                            ", a.nDealrDsc" + 
                            ", a.cComboInv" + 
                            ", a.cWthPromo" + 
                            ", a.cSerialze" + 
                            ", a.cUnitType" + 
                            ", a.cInvStatx" + 
                            ", a.sSupersed" + 
                            ", a.cRecdStat" + 
                            ", b.sDescript xBrandNme" + 
                            ", c.sDescript xModelNme" + 
                            ", d.sDescript xInvTypNm" + 
                            ", e.sMeasurNm" + 
                            ", a.cWthExprt" + 
                        " FROM Inventory a" + 
                            " LEFT JOIN Brand b" + 
                                " ON a.sBrandCde = b.sBrandCde" + 
                            " LEFT JOIN Model c" + 
                                " ON a.sModelCde = c.sModelCde" + 
                            " LEFT JOIN Inv_Type d" + 
                                " ON a.sInvTypCd = d.sInvTypCd" +
                            " LEFT JOIN Measure e" + 
                                " ON e.sMeasurID = a.sMeasurID";
        
        //validate result based on the assigned inventory type.
//        if (!System.getProperty("store.inventory.type").isEmpty())
//            lsSQL = MiscUtil.addCondition(lsSQL, "a.sInvTypCd IN " + CommonUtils.getParameter(System.getProperty("store.inventory.type")));
        
        return lsSQL;
    }
    
    private String getSQ_AllStock(){
        String lsSQL =  "SELECT" +
                            " a.sStockIDx," +
                            " a.sBarCodex xReferNox," +
                            " a.sDescript," +
                            " a.sBriefDsc," +
                            " a.sAltBarCd," +
                            " a.sCategCd1," +
                            " a.sCategCd2," +
                            " a.sCategCd3," +
                            " a.sCategCd4," +
                            " a.sBrandCde," +
                            " a.sModelCde," +
                            " a.sColorCde," +
                            " a.sInvTypCd," +
                            " a.nUnitPrce," +
                            " a.nSelPrice," +
                            " a.nDiscLev1," +
                            " a.nDiscLev2," +
                            " a.nDiscLev3," +
                            " a.nDealrDsc," +
                            " a.cComboInv," +
                            " a.cWthPromo," +
                            " a.cSerialze," +
                            " a.cUnitType," +
                            " a.cInvStatx," +
                            " a.sSupersed," +
                            " a.cRecdStat," +
                            " b.sDescript xBrandNme," +
                            " c.sDescript xModelNme," +
                            " d.sDescript xInvTypNm," +
                            " e.sMeasurNm," +
                            " f.nQtyOnHnd," +
                            " '' sReferNo1," +
                            " '' sSerialID" +
                        " FROM Inventory a" + 
                            " LEFT JOIN Brand b" + 
                                    " ON a.sBrandCde = b.sBrandCde" + 
                            " LEFT JOIN Model c" + 
                                    " ON a.sModelCde = c.sModelCde" + 
                            " LEFT JOIN Inv_Type d" + 
                                    " ON a.sInvTypCd = d.sInvTypCd" + 
                            " LEFT JOIN Measure e" + 
                                    " ON e.sMeasurID = a.sMeasurID," +
                            " Inv_Master f" + 
                        " WHERE a.sStockIDx = f.sStockIDx" + 
                            " AND f.sBranchCd = " + SQLUtil.toSQL(psBranchCd) +
                            " AND a.cRecdStat = " + SQLUtil.toSQL(RecordStatus.ACTIVE) +
                            " AND a.cSerialze = '0'"; //" AND f.nQtyOnHnd > 0"
        
        //validate result based on the assigned inventory type.
//        if (!System.getProperty("store.inventory.type").isEmpty())
//            lsSQL = lsSQL + " AND a.sInvTypCd IN " + CommonUtils.getParameter(System.getProperty("store.inventory.type"));
//        
        lsSQL = lsSQL + " UNION SELECT" +
                            " a.sStockIDx," + 
                            " g.sSerial01 xReferNox," + 
                            " a.sDescript," + 
                            " a.sBriefDsc," +
                            " a.sAltBarCd," +
                            " a.sCategCd1," +
                            " a.sCategCd2," +
                            " a.sCategCd3," +
                            " a.sCategCd4," +
                            " a.sBrandCde," +
                            " a.sModelCde," +
                            " a.sColorCde," +
                            " a.sInvTypCd," +
                            " a.nUnitPrce," +
                            " a.nSelPrice," +
                            " a.nDiscLev1," +
                            " a.nDiscLev2," +
                            " a.nDiscLev3," +
                            " a.nDealrDsc," +
                            " a.cComboInv," +
                            " a.cWthPromo," +
                            " a.cSerialze," +
                            " a.cUnitType," +
                            " a.cInvStatx," +
                            " a.sSupersed," +
                            " a.cRecdStat," +
                            " b.sDescript xBrandNme," + 
                            " c.sDescript xModelNme," + 
                            " d.sDescript xInvTypNm," + 
                            " e.sMeasurNm," + 
                            " 1 nQtyOnHnd," + 
                            " IFNULL(g.sSerial02, '') xReferNo1," +  
                            " g.sSerialID" +
                        " FROM Inventory a" +  
                            " LEFT JOIN Brand b" +  
                                " ON a.sBrandCde = b.sBrandCde" +  
                            " LEFT JOIN Model c" +  
                                " ON a.sModelCde = c.sModelCde" +  
                            " LEFT JOIN Inv_Type d" +  
                                " ON a.sInvTypCd = d.sInvTypCd" +  
                            " LEFT JOIN Measure e" +  
                                " ON e.sMeasurID = a.sMeasurID," + 
                            " Inv_Master f," + 
                            " Inv_Serial g" + 
                        " WHERE a.sStockIDx = f.sStockIDx" + 
                            " AND f.sStockIDx = g.sStockIDx" + 
                            " AND a.cSerialze = '1'" + 
                            " AND g.cLocation = '1'" + 
                            " AND g.cSoldStat = '0'" + 
                            " AND f.sBranchCd = " + SQLUtil.toSQL(psBranchCd) + 
                            " AND a.cRecdStat = " + SQLUtil.toSQL(RecordStatus.ACTIVE); //" AND f.nQtyOnHnd > 0"
        
        //validate result based on the assigned inventory type.
//        if (!System.getProperty("store.inventory.type").isEmpty())
//            lsSQL = lsSQL + " AND a.sInvTypCd IN " + CommonUtils.getParameter(System.getProperty("store.inventory.type"));
//        
        return lsSQL;
    }
    
    private String getSQ_SoldStock(){
        String lsSQL =  "SELECT" +
                            " a.sStockIDx," +
                            " a.sBarCodex xReferNox," +
                            " a.sDescript," +
                            " a.sBriefDsc," +
                            " a.sAltBarCd," +
                            " a.sCategCd1," +
                            " a.sCategCd2," +
                            " a.sCategCd3," +
                            " a.sCategCd4," +
                            " a.sBrandCde," +
                            " a.sModelCde," +
                            " a.sColorCde," +
                            " a.sInvTypCd," +
                            " a.nUnitPrce," +
                            " a.nSelPrice," +
                            " a.nDiscLev1," +
                            " a.nDiscLev2," +
                            " a.nDiscLev3," +
                            " a.nDealrDsc," +
                            " a.cComboInv," +
                            " a.cWthPromo," +
                            " a.cSerialze," +
                            " a.cUnitType," +
                            " a.cInvStatx," +
                            " a.sSupersed," +
                            " a.cRecdStat," +
                            " b.sDescript xBrandNme," +
                            " c.sDescript xModelNme," +
                            " d.sDescript xInvTypNm," +
                            " e.sMeasurNm," +
                            " f.nQtyOnHnd," +
                            " '' sReferNo1," +
                            " '' sSerialID" +
                        " FROM Inventory a" + 
                            " LEFT JOIN Brand b" + 
                                    " ON a.sBrandCde = b.sBrandCde" + 
                            " LEFT JOIN Model c" + 
                                    " ON a.sModelCde = c.sModelCde" + 
                            " LEFT JOIN Inv_Type d" + 
                                    " ON a.sInvTypCd = d.sInvTypCd" + 
                            " LEFT JOIN Measure e" + 
                                    " ON e.sMeasurID = a.sMeasurID," +
                            " Inv_Master f" + 
                        " WHERE a.sStockIDx = f.sStockIDx" + 
                            " AND f.sBranchCd = " + SQLUtil.toSQL(psBranchCd) +
                            " AND a.cRecdStat = " + SQLUtil.toSQL(RecordStatus.ACTIVE) +
                            " AND a.cSerialze = '0'";
        
        //validate result based on the assigned inventory type.
        if (!System.getProperty("store.inventory.type").isEmpty())
            lsSQL = lsSQL + " AND a.sInvTypCd IN " + CommonUtils.getParameter(System.getProperty("store.inventory.type"));
        
        lsSQL = lsSQL + " UNION SELECT" +
                            " a.sStockIDx," + 
                            " g.sSerial01 xReferNox," + 
                            " a.sDescript," + 
                            " a.sBriefDsc," +
                            " a.sAltBarCd," +
                            " a.sCategCd1," +
                            " a.sCategCd2," +
                            " a.sCategCd3," +
                            " a.sCategCd4," +
                            " a.sBrandCde," +
                            " a.sModelCde," +
                            " a.sColorCde," +
                            " a.sInvTypCd," +
                            " a.nUnitPrce," +
                            " a.nSelPrice," +
                            " a.nDiscLev1," +
                            " a.nDiscLev2," +
                            " a.nDiscLev3," +
                            " a.nDealrDsc," +
                            " a.cComboInv," +
                            " a.cWthPromo," +
                            " a.cSerialze," +
                            " a.cUnitType," +
                            " a.cInvStatx," +
                            " a.sSupersed," +
                            " a.cRecdStat," +
                            " b.sDescript xBrandNme," + 
                            " c.sDescript xModelNme," + 
                            " d.sDescript xInvTypNm," + 
                            " e.sMeasurNm," + 
                            " 1 nQtyOnHnd," + 
                            " IFNULL(g.sSerial02, '') xReferNo1," +  
                            " g.sSerialID" +
                        " FROM Inventory a" +  
                            " LEFT JOIN Brand b" +  
                                " ON a.sBrandCde = b.sBrandCde" +  
                            " LEFT JOIN Model c" +  
                                " ON a.sModelCde = c.sModelCde" +  
                            " LEFT JOIN Inv_Type d" +  
                                " ON a.sInvTypCd = d.sInvTypCd" +  
                            " LEFT JOIN Measure e" +  
                                " ON e.sMeasurID = a.sMeasurID," + 
                            " Inv_Master f," + 
                            " Inv_Serial g" + 
                        " WHERE a.sStockIDx = f.sStockIDx" + 
                            " AND f.sStockIDx = g.sStockIDx" + 
                            " AND a.cSerialze = '1'" + 
                            " AND f.sBranchCd = " + SQLUtil.toSQL(psBranchCd) + 
                            " AND a.cRecdStat = " + SQLUtil.toSQL(RecordStatus.ACTIVE);
        
        //validate result based on the assigned inventory type.
//        if (!System.getProperty("store.inventory.type").isEmpty())
//            lsSQL = lsSQL + " AND a.sInvTypCd IN " + CommonUtils.getParameter(System.getProperty("store.inventory.type"));
        
        return lsSQL;
    }
    
    private String getSQ_Stock(){
        String lsSQL =  "SELECT " +
                    "  a.sStockIDx" +
                    ", a.sBarCodex" + 
                    ", a.sDescript" + 
                    ", a.sBriefDsc" + 
                    ", a.sAltBarCd" + 
                    ", a.sCategCd1" + 
                    ", a.sCategCd2" + 
                    ", a.sCategCd3" + 
                    ", a.sCategCd4" + 
                    ", a.sBrandCde" + 
                    ", a.sModelCde" + 
                    ", a.sColorCde" + 
                    ", a.sInvTypCd" + 
                    ", a.nUnitPrce" + 
                    ", a.nSelPrice" + 
                    ", a.nDiscLev1" + 
                    ", a.nDiscLev2" + 
                    ", a.nDiscLev3" + 
                    ", a.nDealrDsc" + 
                    ", a.cComboInv" + 
                    ", a.cWthPromo" + 
                    ", a.cSerialze" + 
                    ", a.cUnitType" + 
                    ", a.cInvStatx" + 
                    ", a.sSupersed" + 
                    ", a.cRecdStat" + 
                    ", b.sDescript xBrandNme" + 
                    ", c.sDescript xModelNme" + 
                    ", d.sDescript xInvTypNm" + 
                    ", e.sMeasurNm" + 
                    ", f.nQtyOnHnd" + 
                " FROM Inventory a" + 
                    " LEFT JOIN Brand b" + 
                        " ON a.sBrandCde = b.sBrandCde" + 
                    " LEFT JOIN Model c" + 
                        " ON a.sModelCde = c.sModelCde" + 
                    " LEFT JOIN Inv_Type d" + 
                        " ON a.sInvTypCd = d.sInvTypCd" +
                    " LEFT JOIN Measure e" + 
                        " ON e.sMeasurID = a.sMeasurID" + 
                    ", Inv_Master f" + 
                " WHERE a.sStockIDx = f.sStockIDx" + 
                    " AND f.sBranchCd = " + SQLUtil.toSQL(psBranchCd);
        
        //validate result based on the assigned inventory type.
//        if (!System.getProperty("store.inventory.type").isEmpty())
//            lsSQL = MiscUtil.addCondition(lsSQL, "a.sInvTypCd IN " + CommonUtils.getParameter(System.getProperty("store.inventory.type")));
//        
        return lsSQL;
    }
    
}
