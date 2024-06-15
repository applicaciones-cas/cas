/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.cas.inventory.base;

import java.sql.Connection;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.appdriver.iface.GRecord;
import org.guanzon.cas.inventory.models.Model_Inventory;
import org.guanzon.cas.parameters.Brand;
import org.guanzon.cas.parameters.Category;
import org.guanzon.cas.parameters.Category_Level2;
import org.guanzon.cas.parameters.Category_Level3;
import org.guanzon.cas.parameters.Category_Level4;
import org.guanzon.cas.parameters.Color;
import org.guanzon.cas.parameters.Inv_Type;
import org.guanzon.cas.parameters.Model;
import org.json.simple.JSONObject;

/**
 *
 * @author User
 */
public class Inventory implements GRecord{
    GRider poGRider;
    boolean pbWthParent;
    String psBranchCd;
    boolean pbWtParent;
    public JSONObject poJSON;
    
    int pnEditMode;
    String psMessagex;
    String psTranStatus;
    
    private Model_Inventory poModel;

    
    public Inventory(GRider foGRider, boolean fbWthParent) {
        poGRider = foGRider;
        pbWthParent = fbWthParent;

        poModel = new Model_Inventory(foGRider);
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
            
            poModel = new Model_Inventory(poGRider);
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
        
        poModel = new Model_Inventory(poGRider);
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
        String lsSQL = "";
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
    public Model_Inventory getModel() {
        return poModel;
    }
    public JSONObject SearchMaster(int fnCol, String fsValue, boolean fbByCode){
        String lsHeader = "";
        String lsColName = "";
        String lsColCrit = "";
        String lsSQL = "";
        JSONObject loJSON;
        
//        if (fsValue.equals("") && fbByCode) return null;
                
        switch(fnCol){
            case 6: //sCategCd1
                Category loCategory = new Category(poGRider, true); 
                loCategory.setRecordStatus(psTranStatus);
                loJSON = loCategory.searchRecord(fsValue, fbByCode);
                
                if (loJSON != null){
                    setMaster(fnCol, (String) loCategory.getMaster("sCategrCd"));
                    setMaster("xCategNm1", (String)loCategory.getMaster("sDescript"));
                    return setMaster("xCategNm1", (String)loCategory.getMaster("sDescript"));
                } else {
                    loJSON.put("result", "error");
                    loJSON.put("message", "No record found.");
                    return loJSON;
                }
            case 7: //sCategCd2
                Category_Level2 loCategory2 = new Category_Level2(poGRider, true);
                loCategory2.setRecordStatus(psTranStatus);
                loJSON = loCategory2.searchRecord(fsValue, fbByCode);
                 
                if (loJSON != null){
                    setMaster(fnCol, (String) loCategory2.getMaster("sCategrCd"));
                    setMaster("xCategNm2", (String) loCategory2.getMaster("sDescript"));
                    return setMaster("xCategNm2", (String) loJSON.get("sDescript"));
                } else {
                    loJSON.put("result", "error");
                    loJSON.put("message", "No record found.");
                    return loJSON;
                }
            case 8: //sCategCd3
                Category_Level3 loCategory3 = new Category_Level3(poGRider, true);
                loCategory3.setRecordStatus(psTranStatus);
                loJSON = loCategory3.searchRecord(fsValue, fbByCode);
                
                if (loJSON != null){
                    setMaster(fnCol, (String) loCategory3.getMaster("sCategrCd"));
                    setMaster("xCategNm3", (String) loCategory3.getMaster("sDescript"));
                    return setMaster("xCategNm3", (String) loJSON.get("sDescript"));
                } else {
                    loJSON.put("result", "error");
                    loJSON.put("message", "No record found.");
                    return loJSON;
                }
            case 9: //sCategCd4
                Category_Level4 loCategory4 = new Category_Level4(poGRider, true);
                loCategory4.setRecordStatus(psTranStatus);
                loJSON = loCategory4.searchRecord(fsValue, fbByCode);
                
                if (loJSON != null){
                    setMaster(fnCol, (String) loCategory4.getMaster("sCategrCd"));
                    setMaster("xCategNm4", (String) loCategory4.getMaster("sDescript"));
                    return setMaster("xCategNm4", (String) loJSON.get("sDescript"));
                } else {
                    loJSON.put("result", "error");
                    loJSON.put("message", "No record found.");
                    return loJSON;
                }
            case 10: //sBrandCde
                Brand loBrand = new Brand(poGRider, true);
                loBrand.setRecordStatus(psTranStatus);
                loJSON = loBrand.searchRecord(fsValue, fbByCode);
                                
                if (loJSON != null){
                    setMaster(fnCol, (String) loJSON.get("sBrandCde"));
                    setMaster("xBrandNme", (String) loJSON.get("sBrandNme"));
                    return setMaster("xModelNme", (String) loJSON.get("sBrandNme"));
                } else {
                    loJSON.put("result", "error");
                    loJSON.put("message", "No record found.");
                    return loJSON;
                }
            case 11: //sModelCde
                Model loModel = new Model(poGRider, false);
                loModel.setRecordStatus(psTranStatus);
                loJSON = loModel.searchRecord(fsValue, fbByCode);
                
                if (loJSON != null){
                    setMaster(fnCol, (String) loJSON.get("sModelCde"));
                    return setMaster("xModelNme", (String) loJSON.get("sModelNme"));
                } else {
                    loJSON.put("result", "error");
                    loJSON.put("message", "No record found.");
                    return loJSON;
                }
            case 12: //sColorCde
                Color loColor = new Color(poGRider, false);
                loColor.setRecordStatus(psTranStatus);
                loJSON = loColor.searchRecord(fsValue, fbByCode);
                
                if (loJSON != null){
                    setMaster(fnCol, (String) loJSON.get("sColorCde"));
                    return setMaster("xColorNme", (String) loJSON.get("sColorCNme"));
                } else {
                    loJSON.put("result", "error");
                    loJSON.put("message", "No record found.");
                    return loJSON;
                }
//            case 13: //sInvTypCd
//                Inv_Type loInvType = new Inv_Type(poGRider, false);
//                
//                loJSON = loInvType.searchRecord(fsValue, fbByCode);
//                
//                if (loJSON != null){
//                    setMaster(fnCol, (String) loJSON.get("sInvTypCd"));
//                    return (String) loJSON.get("sDescript");
//                } else {
//                    setMaster(fnCol, "");
//                    return "";
//                }
//            case 29: //sMeasurID
//                Measure loMeasure = new Measure(poGRider, psBranchCd, false);
//                
//                loJSON = loMeasure.searchMeasure(fsValue, fbByCode);
//                
//                if (loJSON != null){
//                    setMaster(fnCol, (String) loJSON.get("sMeasurID"));
//                    return (String) loJSON.get("sMeasurNm");
//                } else {
//                    setMaster(fnCol, "");
//                    return "";
//                }
            default:
                return null;
        }
    }
    
    public JSONObject SearchMaster(String fsCol, String fsValue, boolean fbByCode){
        return SearchMaster(poModel.getColumn(fsCol), fsValue, fbByCode);
    }
}
