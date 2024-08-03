/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.cas.inventory.base;

import java.sql.Connection;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.appdriver.iface.GRecord;
import org.guanzon.cas.model.inventory.Model_Inv_Ledger;
import org.guanzon.cas.model.inventory.Model_Inv_Master;
import org.guanzon.cas.model.inventory.Model_Inv_Serial;
import org.guanzon.cas.model.inventory.Model_Inventory;
import org.guanzon.cas.model.parameters.Model_Inv_Location;
import org.guanzon.cas.parameters.Inv_Location;
import org.guanzon.cas.parameters.Warehouse;
import org.guanzon.cas.validators.inventory.Validator_Inventory_Master;
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
    private Inv_Location poLocation;

    
    public InvMaster(GRider foGRider, boolean fbWthParent) {
        poGRider = foGRider;
        pbWthParent = fbWthParent;

        poModel = new Model_Inv_Master(foGRider);
        poInventory = new Inventory(foGRider, pbWthParent);
        poSerial = new InvSerial(foGRider, pbWthParent);
        poLedger = new InvLedger(foGRider, pbWthParent);
        poLocation = new Inv_Location(foGRider, pbWthParent);
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
        poInventory.setRecordStatus(psTranStatus);
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
//        pnEditMode = poModel.getEditMode();
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
        Validator_Inventory_Master validator = new Validator_Inventory_Master(poModel);
        poModel.setModifiedDate(poGRider.getServerDate());

        if (!validator.isEntryOkay()){
            poJSON.put("result", "error");
            poJSON.put("message", validator.getMessage());
            return poJSON;

        }
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
    
    public JSONObject SearchInventory(String fsStockIDx, boolean fbByCode){
        poJSON = poInventory.searchRecord(fsStockIDx, fbByCode);
        if ("success".equals((String) poJSON.get("result"))){
            poJSON = poModel.openRecord(poInventory.getModel().getStockID());
            if ("error".equals((String) poJSON.get("result"))){
                 ShowMessageFX.Information("No Inventory found in your warehouse.\nPlease save the record to create.", "Computerized Acounting System", "Inventory Detail");
                 poJSON = newRecord();
            }else {
                pnEditMode = EditMode.READY;
            }
            
            poModel.setStockID(poInventory.getModel().getStockID());
            poModel.setBranchCd(poGRider.getBranchCode());
            poModel.setBarCodex(poInventory.getModel().getBarcode());
            poModel.setDescript(poInventory.getModel().getDescription());
        }      
        return poJSON;
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
    public Model_Inventory getInvModel(){
        return poInventory.getModel();
    }
    public Model_Inv_Serial getSerialModel(){
        return poSerial.getModel();
    }
    public Model_Inv_Ledger getLedgerModel(){
        return poLedger.getModel();
    }
    public Model_Inv_Location getLocationModel(){
        return poLocation.getModel();
    }
    
    public JSONObject SearchMaster(int fnCol, String fsValue, boolean fbByCode){
        String lsHeader = "";
        String lsColName = "";
        String lsColCrit = "";
        String lsSQL = "";
        JSONObject loJSON;
        
//        if (fsValue.equals("") && fbByCode) return "";
        
        switch(fnCol){
            case 3: //sWHouseID
                Warehouse loWarehouse = new Warehouse(poGRider, false);
                
                loWarehouse.setRecordStatus(psTranStatus);
                loJSON = loWarehouse.searchRecord(fsValue, fbByCode);
                
                if (loJSON != null){
                    setMaster(fnCol, (String) loWarehouse.getMaster("sWHouseID"));
                    return setMaster("xWHouseNm", (String) loWarehouse.getMaster("sWHouseNm"));
                } else {
                    loJSON.put("result", "error");
                    loJSON.put("message", "No record found.");
                    return loJSON;
                }
            case 4: //sLocatnCd
                
                
                lsSQL = "SELECT" +
                            "  a.sLocatnCd" +
                            ", a.sDescript" +
                            ", a.sWHouseID" +
                            ", a.sSectnIDx" +
                            ", a.cRecdStat" +
                            ", a.sModified" +
                            ", a.dModified" +
                            ", b.sWHouseNm xWHouseNm" +
                            ", c.sSectnNme xSectnNme" +
                        " FROM Inv_Location a" +
                            " LEFT JOIN Warehouse b ON a.sWHouseID = b.sWHouseID" +
                            " LEFT JOIN Section c ON a.sSectnIDx = c.sSectnIDx" ;
                String lsCondition = "";

                if (psTranStatus.length() > 1) {
                    for (int lnCtr = 0; lnCtr <= psTranStatus.length() - 1; lnCtr++) {
                        lsCondition += ", " + SQLUtil.toSQL(Character.toString(psTranStatus.charAt(lnCtr)));
                    }

                    lsCondition = "a.cRecdStat IN (" + lsCondition.substring(2) + ")";
                } else {
                    lsCondition = "a.cRecdStat = " + SQLUtil.toSQL(psTranStatus);
                }

                if (fbByCode)
                    lsSQL = MiscUtil.addCondition(lsSQL, "a.sBarCodex = " + SQLUtil.toSQL(fsValue));
                else
                    lsSQL = MiscUtil.addCondition(lsSQL, "a.sDescript LIKE " + SQLUtil.toSQL(fsValue + "%"));
                
                
//                if(!poModel.getWareHouseID().isEmpty()){
//                    lsSQL = MiscUtil.addCondition(lsSQL, "a.sMainCatx = " + SQLUtil.toSQL(poModel.getWareHouseID()));
//                }
                
                lsSQL = MiscUtil.addCondition(lsSQL, lsCondition);
                System.out.println(lsSQL);
                loJSON = ShowDialogFX.Search(
                                poGRider, 
                                lsSQL, 
                                fsValue, 
                                "Code»Name»Warehouse»Section",
                                "sLocatnCd»sDescript»xWHouseNm»xSectnNme",
                                "a.sLocatnCd»a.sDescript»b.sWHouseNm»c.sSectnNme",
                                fbByCode ? 0 : 1);
                if (loJSON != null) {
                    setMaster(fnCol, (String) loJSON.get("sLocatnCd"));
                    setMaster("xLocatnNm", (String) loJSON.get("sDescript"));
//                    setMaster("sSectnIDx", (String) loJSON.get("sSectnIDx"));
                    setMaster("sWHouseID", (String) loJSON.get("sWHouseID"));
                    setMaster("xWHouseNm", (String) loJSON.get("xWHouseNm"));
                    
//                    setMaster(6, (String) loJSON.get("sMainCatx"));
////                    setMaster("xCategNm1", (String)loCategory.getMaster("sDescript"));
//                    setMaster("xCategNm1", (String)loJSON.get("xMainCatx"));
//                    setMaster("xMainCatx", (String)loJSON.get("sMainCatx"));
//                    System.out.println("sInvTypCd = " + setMaster("sInvTypCd", (String) loJSON.get("sCategrCd")));
                    return setMaster("xSectnNme", (String) loJSON.get("xSectnNme"));
                    
                }else {
                    loJSON = new JSONObject();
                    loJSON.put("result", "error");
                    loJSON.put("message", "No record selected.");
                    return loJSON;
                }
                
                
                
                
                
//                Inv_Location loLocation = new Inv_Location(poGRider, false);
//                loLocation.setRecordStatus(psTranStatus);
//                loJSON = loLocation.searchRecord(fsValue, fbByCode);
//                
//                if (loJSON != null){
//                    setMaster(fnCol, (String) loLocation.getMaster("sLocatnCd"));
//                    return setMaster("xLocatnNm", (String) loLocation.getMaster("sDescript"));
//                } else {
//                    loJSON.put("result", "error");
//                    loJSON.put("message", "No record found.");
//                    return loJSON;
//                }
            default:
                return null;
                
        }
    }
    
    public JSONObject SearchMaster(String fsCol, String fsValue, boolean fbByCode){
        return SearchMaster(poModel.getColumn(fsCol), fsValue, fbByCode);
    }
}
