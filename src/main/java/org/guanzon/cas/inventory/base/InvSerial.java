/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.cas.inventory.base;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.appdriver.iface.GRecord;
import org.guanzon.cas.inventory.models.Model_Inv_Ledger;
import org.guanzon.cas.inventory.models.Model_Inv_Serial;
import org.guanzon.cas.inventory.models.Model_Inv_Serial;
import org.json.simple.JSONObject;

/**
 *
 * @author User
 */
public class InvSerial implements GRecord{
    

    GRider poGRider;
    boolean pbWthParent;
    int pnEditMode;
    String psTranStatus;
    
    ArrayList<Model_Inv_Serial> poModel;
    JSONObject poJSON;

    public InvSerial(GRider foGRider, boolean fbWthParent) {
        poGRider = foGRider;
        pbWthParent = fbWthParent;
        pnEditMode = EditMode.UNKNOWN;
    }
    @Override
    public JSONObject setMaster(int fnCol, Object foData) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public JSONObject setMaster(String fsCol, Object foData) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    
    }

    @Override
    public int getEditMode() {
        return pnEditMode;
    }
    
    @Override
    public void setRecordStatus(String fsValue) {
        
        psTranStatus = fsValue;
    }

    @Override
    public Object getMaster(int fnCol) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object getMaster(String fsCol) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    @Override
    public JSONObject newRecord() {
         poJSON = new JSONObject();
        try{
            

            //init detail
            //init detail
            poModel = new ArrayList<>();
            
            poJSON = addLedger();
                
                
            poJSON.put("result", "success");
            poJSON.put("message", "initialized new record.");
            pnEditMode = EditMode.ADDNEW;
               
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
        
        poJSON = OpenInvSerial(fsValue);
        return poJSON;
    }

    @Override
    public JSONObject updateRecord() {
        JSONObject loJSON = new JSONObject();
        
        if (pnEditMode != EditMode.READY && pnEditMode != EditMode.UPDATE){
            loJSON.put("result", "success");
            loJSON.put("message", "Edit mode has changed to update.");
        } else {
            loJSON.put("result", "error");
            loJSON.put("message", "No record loaded to update.");
        }

        return loJSON;
    }

    @Override
    public JSONObject saveRecord() {
        if (!pbWthParent) {
            poGRider.beginTrans();
        }

        poJSON = saveLedger();

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
            
            Model_Inv_Serial model = new Model_Inv_Serial(poGRider);
            String lsSQL = "DELETE FROM " + model.getTable()+
                                " WHERE sSerialID = " + SQLUtil.toSQL(fsValue);

            if (!lsSQL.equals("")){
                if (poGRider.executeQuery(lsSQL, model.getTable(), poGRider.getBranchCode(), "") > 0) {
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public JSONObject activateRecord(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public JSONObject searchRecord(String fsValue, boolean fbByCode) {
        String lsCondition = "";
        Model_Inv_Serial model = new Model_Inv_Serial(poGRider);
        String lsSQL = MiscUtil.addCondition(model.getSQL(), "a.sSerialID = " + SQLUtil.toSQL(fsValue));

        poJSON = new JSONObject();

        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                fsValue,
                "Stock ID»Ledger No»Source No»Date",
                "sStockIDx»nLedgerNo»sSourceCd»dTransact",
                "sStockIDx»nLedgerNo»sSourceCd»dTransact",
                fbByCode ? 0 : 1);

        if (poJSON != null) {
            return openRecord(fsValue);
        } else {
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
            return poJSON;
        }
    }

    @Override
    public Model_Inv_Serial getModel() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    public ArrayList<Model_Inv_Serial> getMaster(){return poModel;}
    public void setMaster(ArrayList<Model_Inv_Serial> foObj){poModel = foObj;}
    
    
    public void setMaster(int fnRow, int fnIndex, Object foValue){ poModel.get(fnRow).setValue(fnIndex, foValue);}
    public void setMaster(int fnRow, String fsIndex, Object foValue){ poModel.get(fnRow).setValue(fsIndex, foValue);}
    public Object getMaster(int fnRow, int fnIndex){return poModel.get(fnRow).getValue(fnIndex);}
    public Object getMaster(int fnRow, String fsIndex){return poModel.get(fnRow).getValue(fsIndex);}
    
    
    public JSONObject addLedger(){
        poJSON = new JSONObject();
        if (poModel.isEmpty()){
            poModel.add(new Model_Inv_Serial(poGRider));
            poModel.get(0).newRecord();
            poJSON.put("result", "success");
            poJSON.put("message", "Inventory add record.");
            

        } else {
            
//            ValidatorInterface validator = ValidatorFactory.make(ValidatorFactory.TYPE.AP_Client_Ledger, poModel.get(poModel.size()-1));
//            Validator_Client_Address  validator = new Validator_Client_Address(paAddress.get(paAddress.size()-1));
//            if(!validator.isEntryOkay()){
//                poJSON.put("result", "error");
//                poJSON.put("message", validator.getMessage());
//                return poJSON;
//            }
            poModel.add(new Model_Inv_Serial(poGRider));
            poModel.get(poModel.size()-1).newRecord();
            
            poJSON.put("result", "success");
            poJSON.put("message", "Address add record.");
        }
        return poJSON;
    }
    
    public JSONObject OpenInvSerial(String fsValue){
        String lsSQL = "SELECT" +
                        "   a.sSerialID" +
                        " , a.sBranchCd" +
                        " , a.sSerial01" +
                        " , a.sSerial02" +
                        " , a.nUnitPrce" +
                        " , a.sStockIDx" +
                        " , a.cLocation" +
                        " , a.cSoldStat" +
                        " , a.cUnitType" +
                        " , a.sCompnyID" +
                        " , a.sWarranty" +
                        " , a.dModified" +
                        " , b.sBarCodex xBarCodex" +
                        " , b.sDescript xDescript" +
                        " , c.sBranchNm xBranchNm " +
                        "FROM Inv_Serial a" +
                        "    LEFT JOIN Inventory b ON a.sStockIDx = b.sStockIDx" +
                        "    LEFT JOIN Branch c ON a.sBranchCd = c.sBranchCd";
        lsSQL = MiscUtil.addCondition(lsSQL, "a.sStockIDx  = " + SQLUtil.toSQL(fsValue));
        lsSQL = MiscUtil.addCondition(lsSQL, "a.sBranchCd  = " + SQLUtil.toSQL(poGRider.getBranchCode()));
        System.out.println("open SERial SQL == \n" +lsSQL);
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        poJSON = new JSONObject();
        try {
            int lnctr = 0;
            if (MiscUtil.RecordCount(loRS) > 0) {
                poModel = new ArrayList<>();
                while(loRS.next()){
                        poModel.add(new Model_Inv_Serial(poGRider));
                        poModel.get(poModel.size() - 1).openRecord(loRS.getString("sSerialID"));
                        
                        pnEditMode = EditMode.UPDATE;
                        lnctr++;
                        poJSON.put("result", "success");
                        poJSON.put("message", "Record loaded successfully.");
                    } 
                
                System.out.println("lnctr = " + lnctr);
                
            }else{
                poModel = new ArrayList<>();
                addLedger();
                poJSON.put("result", "error");
                poJSON.put("continue", true);
                poJSON.put("message", "No record selected.");
            }
            
            MiscUtil.close(loRS);
        } catch (SQLException e) {
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
        }
        return poJSON;
    }
    private JSONObject saveLedger(){
        
        JSONObject obj = new JSONObject();
        if (poModel.size()<= 0){
            obj.put("result", "error");
            obj.put("message", "No client address detected. Please encode client address.");
            return obj;
        }
        
        int lnCtr;
        String lsSQL;
        
        for (lnCtr = 0; lnCtr <= poModel.size() -1; lnCtr++){
            poModel.get(lnCtr).setStockID(poModel.get(lnCtr).getStockID());
//            Validator_Client_Address validator = new Validator_Client_Address(paAddress.get(lnCtr));
//            
//            ValidatorInterface validator = ValidatorFactory.make(ValidatorFactory.TYPE.AP_Client_Ledger, poModel.get(lnCtr));
//            poModel.get(lnCtr).setModifiedDate(poGRider.getServerDate());
//            
//            if (!validator.isEntryOkay()){
//                obj.put("result", "error");
//                obj.put("message", validator.getMessage());
//                return obj;
//            
//            }
            obj = poModel.get(lnCtr).saveRecord();

        }    
        
        return obj;
    }
    public JSONObject OpenInvSerialWithCondition(String fsValue, String lsCondition){
        
        poJSON =  new JSONObject();
        String lsSQL = "SELECT" +
                        "   a.sSerialID" +
                        " , a.sBranchCd" +
                        " , a.sSerial01" +
                        " , a.sSerial02" +
                        " , a.nUnitPrce" +
                        " , a.sStockIDx" +
                        " , a.cLocation" +
                        " , a.cSoldStat" +
                        " , a.cUnitType" +
                        " , a.sCompnyID" +
                        " , a.sWarranty" +
                        " , a.dModified" +
                        " , b.sBarCodex xBarCodex" +
                        " , b.sDescript xDescript" +
                        " , c.sBranchNm xBranchNm " +
                        "FROM Inv_Serial a" +
                        "    LEFT JOIN Inventory b ON a.sStockIDx = b.sStockIDx" +
                        "    LEFT JOIN Branch c ON a.sBranchCd = c.sBranchCd";
        lsSQL = MiscUtil.addCondition(lsSQL, "a.sStockIDx  = " + SQLUtil.toSQL(fsValue));
        lsSQL = MiscUtil.addCondition(lsSQL, "a.sBranchCd  = " + SQLUtil.toSQL(poGRider.getBranchCode()));
        lsSQL = MiscUtil.addCondition(lsSQL, lsCondition);
        System.out.println("open SERial SQL with condition == \n" + lsSQL);
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        poJSON = new JSONObject();
        try {
            int lnctr = 0;
            if (MiscUtil.RecordCount(loRS) > 0) {
                poModel = new ArrayList<>();
                while(loRS.next()){
                        poModel.add(new Model_Inv_Serial(poGRider));
                        poModel.get(poModel.size() - 1).openRecord(loRS.getString("sSerialID"));
                        
                        pnEditMode = EditMode.UPDATE;
                        lnctr++;
                        poJSON.put("result", "success");
                        poJSON.put("message", "Record loaded successfully.");
                    } 
                
                System.out.println("lnctr = " + lnctr);
                
            }else{
                poModel = new ArrayList<>();
//                addLedger();
                poJSON.put("result", "error");
//                poJSON.put("continue", true);
                poJSON.put("message", "No record found.");
            }
            
            MiscUtil.close(loRS);
        } catch (SQLException e) {
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
        }
        return poJSON;
    }

}
