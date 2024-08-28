/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.cas.inventory.stock;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.appdriver.iface.GTranDet;
import org.guanzon.cas.inventory.base.InvMaster;
import org.guanzon.cas.inventory.base.Inventory;
import org.guanzon.cas.inventory.models.Model_Inv_Stock_Req_Cancel_Detail;
import org.guanzon.cas.inventory.models.Model_Inv_Stock_Req_Cancel_Master;
import org.guanzon.cas.inventory.models.Model_Inv_Stock_Request_Detail;
import org.json.simple.JSONObject;

/**
 *
 * @author User
 */
public class InvStockReqCancel implements GTranDet {

    GRider poGRider;
    boolean pbWthParent;
    int pnEditMode;
    String psTranStatus;

    private boolean p_bWithUI = true;
    InvStockRequest poRequest;
    Model_Inv_Stock_Req_Cancel_Master poModelMaster;
    ArrayList<Model_Inv_Stock_Req_Cancel_Detail> poModelDetail;
    
    JSONObject poJSON;

    public void setWithUI(boolean fbValue){
        p_bWithUI = fbValue;
    }
    public InvStockReqCancel(GRider foGRider, boolean fbWthParent) {
        poGRider = foGRider;
        pbWthParent = fbWthParent;
        poModelMaster = new Model_Inv_Stock_Req_Cancel_Master(foGRider);
        poModelDetail = new ArrayList<>();
        poModelDetail.add(new Model_Inv_Stock_Req_Cancel_Detail(foGRider));
        poRequest = new InvStockRequest(foGRider, fbWthParent);
        pnEditMode = EditMode.UNKNOWN;
    }

    @Override
    public JSONObject newTransaction() {

        poJSON = new JSONObject();
        poJSON = poModelMaster.newRecord();

        if ("error".equals((String) poJSON.get("result"))) {
            poJSON.put("result", "error");
            poJSON.put("message", "No record to load.");
            return poJSON;
        }

        poJSON = new JSONObject();

        poModelDetail.get(getItemCount() - 1).newRecord();
        poJSON = poModelDetail.get(getItemCount() - 1).setTransactionNumber(poModelMaster.getTransactionNumber());
        if ("error".equals((String) poJSON.get("result"))) {
            poJSON.put("result", "error");
            poJSON.put("message", "No record to load.");
            return poJSON;

        }

        poJSON.put("result", "success");
        return poJSON;
    }

    @Override
    public JSONObject openTransaction(String fsValue) {
        
        poJSON = new JSONObject();
        poModelMaster.openRecord("sTransNox = " + SQLUtil.toSQL(fsValue));
        if ("error".equals((String) poJSON.get("result"))) {
            return poJSON;
        }

        OpenModelDetail(poModelMaster.getTransactionNumber());

        return poJSON;

    }

    @Override
    public JSONObject updateTransaction() {
        JSONObject loJSON = new JSONObject();

        if (poModelMaster.getEditMode() == EditMode.UPDATE) {
            loJSON.put("result", "success");
            loJSON.put("message", "Edit mode has changed to update.");
        } else {
            loJSON.put("result", "error");
            loJSON.put("message", "No record loaded to update.");
        }

        return loJSON;
    }

    @Override
    public JSONObject saveTransaction() {
        
        poJSON = new JSONObject();
        if (!pbWthParent) {
            poGRider.beginTrans();
        }

        if (getItemCount() >= 1) {
            for (int lnCtr = 0; lnCtr <= getItemCount() - 1; lnCtr++) {
                poModelDetail.get(lnCtr).setEntryNumber(lnCtr + 1);
                poJSON = poModelDetail.get(lnCtr).saveRecord();

                if ("error".equals((String) poJSON.get("result"))) {

                    if (!pbWthParent) {
                        poGRider.rollbackTrans();
                    }
                    return poJSON;
                }

            }

        } else {
            poJSON.put("result", "error");
            poJSON.put("message", "Unable to Save empty Transaction.");
            return poJSON;
        }
        poModelMaster.setEntryNumber(poModelDetail.size());
        poJSON = poModelMaster.saveRecord();
        if ("success".equals((String) poJSON.get("result"))) {
            if (!pbWthParent) {
                poGRider.commitTrans();
            }
        } else {
            if (!pbWthParent) {
                poGRider.rollbackTrans();
                poJSON.put("result", "error");
                poJSON.put("message", "Unable to Save Transaction.");
            }
        }

        return poJSON;
    }

    @Override
    public JSONObject deleteTransaction(String fsValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JSONObject closeTransaction(String fsValue) {
        poJSON = new JSONObject();
        if (poModelMaster.getEditMode() == EditMode.READY || poModelMaster.getEditMode() == EditMode.UPDATE) {
            poJSON = poModelMaster.setTransactionStatus(TransactionStatus.STATE_CLOSED);
            if ("error".equals((String) poJSON.get("result"))) {
                return poJSON;
            }

            poJSON = poModelMaster.saveRecord();
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
        }
        return poJSON;
    }

    @Override
    public JSONObject postTransaction(String fsValue) {
        poJSON = new JSONObject();

        if (poModelMaster.getEditMode() == EditMode.READY
                || poModelMaster.getEditMode() == EditMode.UPDATE) {

            poJSON = poModelMaster.setModifiedBy(poGRider.getUserID());
            if ("error".equals((String) poJSON.get("result"))) {
                return poJSON;
            }
            poJSON = poModelMaster.setModifiedDate(poGRider.getServerDate());
            if ("error".equals((String) poJSON.get("result"))) {
                return poJSON;
            }
            poJSON = poModelMaster.setTransactionStatus(TransactionStatus.STATE_POSTED);
            if ("error".equals((String) poJSON.get("result"))) {
                return poJSON;
            }

            poJSON = poModelMaster.saveRecord();
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
        }
        return poJSON;
    }

    @Override
    public JSONObject voidTransaction(String string) {
        poJSON = new JSONObject();

        if (poModelMaster.getEditMode() == EditMode.READY
                || poModelMaster.getEditMode() == EditMode.UPDATE) {
            poJSON = poModelMaster.setTransactionStatus(TransactionStatus.STATE_VOID);

            if ("error".equals((String) poJSON.get("result"))) {
                return poJSON;
            }

            poJSON = poModelMaster.saveRecord();
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
        }
        return poJSON;
    }

    @Override
    public JSONObject cancelTransaction(String fsTransNox) {
        poJSON = new JSONObject();

        if (poModelMaster.getEditMode() == EditMode.READY
                || poModelMaster.getEditMode() == EditMode.UPDATE) {
            poJSON = poModelMaster.setTransactionStatus(TransactionStatus.STATE_CANCELLED);

            if ("error".equals((String) poJSON.get("result"))) {
                return poJSON;
            }

            poJSON = poModelMaster.saveRecord();
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
        }
        return poJSON;
    }

    @Override
    public int getItemCount() {
        return poModelDetail.size();
    }


    @Override
    public Model_Inv_Stock_Req_Cancel_Detail getDetailModel(int fnRow) {
        return poModelDetail.get(fnRow);
    }
//    @Override
    public ArrayList<Model_Inv_Stock_Req_Cancel_Detail> getDetailModel() {
        return poModelDetail;
    }

    @Override
    public JSONObject setDetail(int fnRow, int fnCol, Object foData) {
        return poModelDetail.get(fnRow).setValue(fnCol, foData);
    }

    @Override
    public JSONObject setDetail(int fnRow, String fsCol, Object foData) {
        return setDetail(fnRow, poModelDetail.get(fnRow).getColumn(fsCol), foData);
    }

    @Override
    public JSONObject searchDetail(int fnRow, String fsCol, String fsValue, boolean fbByCode) {
        return searchDetail(fnRow, poModelDetail.get(fnRow).getColumn(fsCol), fsValue, fbByCode);
    }

    @Override
    public JSONObject searchDetail(int fnRow, int fnCol, String fsValue, boolean fbByCode) {
        poJSON = new JSONObject();

        switch (fnCol) {

            case 3: //sBarCodex
                String lsSQL = new Model_Inv_Stock_Request_Detail(poGRider).getSQL();
//                lsSQL =  MiscUtil.addCondition(lsSQL, fsValue);
                
                lsSQL = MiscUtil.addCondition(lsSQL, "a.sTransNox = " + SQLUtil.toSQL(poModelMaster.getOrderNumber()));
                if (fbByCode)
                    lsSQL = MiscUtil.addCondition(lsSQL, "b.sBarCodex = " + SQLUtil.toSQL(fsValue));
                else {
                    lsSQL = MiscUtil.addCondition(lsSQL, "b.sBarCodex LIKE " + SQLUtil.toSQL("%" +fsValue + "%"));
                }


                if (p_bWithUI){
                    poJSON = ShowDialogFX.Search(poGRider,
                            lsSQL,
                            fsValue,
                            "Stock ID»Barcode»Name",
                            "sTransNox»xBarCodex»xDescript",
                            "a.sStockIDx»b.sBarCodex»b.sDescript",
                            fbByCode ? 1 : 2);

                    if (poJSON != null) {
                        poJSON = poRequest.OpenModelDetailByStockID((String)poJSON.get("sTransNox"), (String)poJSON.get("sStockIDx"));
                        
                        if (poJSON != null) {
                            setDetail(fnRow, "sOrderNox", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size()-1).getTransactionNumber());
                            setDetail(fnRow, "sStockIDx", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size()-1).getStockID());
                            setDetail(fnRow, "nQuantity", (int) poRequest.getDetailModel().get(poRequest.getDetailModel().size()-1).getQuantity());
                            setDetail(fnRow, "sNotesxxx", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size()-1).getNotes());
                            setDetail(fnRow, "xBarCodex", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size()-1).getBarcode());
                            setDetail(fnRow, "xDescript", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size()-1).getDescription());
                        }else{
                            
                            poJSON.put("result", "error");
                            poJSON.put("message", "No record loaded to update.");
                            return poJSON;
                        }

                    } else {
                        poJSON.put("result", "error");
                        poJSON.put("message", "No record loaded to update.");
                        return poJSON;
                    }
                }
                
                //use for testing 
                lsSQL += " LIMIT 1";
                System.out.println(lsSQL);
                ResultSet loRS = poGRider.executeQuery(lsSQL);
                String lsStockId = "";
                try {
                    if (!loRS.next()){
                        MiscUtil.close(loRS);
                        poJSON.put("result", "error");
                        poJSON.put("message", "No record loaded.");
                        return poJSON;
                    }

                    lsSQL = loRS.getString("sTransNox");
                    lsStockId = loRS.getString("sStockIDx");
                    MiscUtil.close(loRS);
                } catch (SQLException ex) {
                    Logger.getLogger(InvStockRequest.class.getName()).log(Level.SEVERE, null, ex);
                }
                poJSON = poRequest.OpenModelDetailByStockID(lsSQL, lsStockId);
                if (poJSON != null) {
                        setDetail(fnRow, "sOrderNox", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size()-1).getTransactionNumber());
                        setDetail(fnRow, "sStockIDx", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size()-1).getStockID());
                        setDetail(fnRow, "nQuantity", (int) poRequest.getDetailModel().get(poRequest.getDetailModel().size()-1).getQuantity());
                        setDetail(fnRow, "sNotesxxx", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size()-1).getNotes());
                        setDetail(fnRow, "xBarCodex", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size()-1).getBarcode());
                        setDetail(fnRow, "xDescript", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size()-1).getDescription());;
                }else{

                    poJSON.put("result", "error");
                    poJSON.put("message", "No record loaded to update.");
                    return poJSON;
                }
        }
        return poJSON;
    }

    @Override
    public JSONObject searchWithCondition(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public JSONObject searchTransaction(String fsColumn, String fsValue, boolean fbByCode) {
        String lsCondition = "";
        String lsFilter = "";

        if (psTranStatus.length() > 1) {
            for (int lnCtr = 0; lnCtr <= psTranStatus.length() - 1; lnCtr++) {
                lsCondition += ", " + SQLUtil.toSQL(Character.toString(psTranStatus.charAt(lnCtr)));
            }

            lsCondition = fsColumn + " IN (" + lsCondition.substring(2) + ")";
        } else {
            lsCondition = fsColumn + " = " + SQLUtil.toSQL(psTranStatus);
        }

        if (!fbByCode) {
            lsFilter = fsColumn + " LIKE " + SQLUtil.toSQL(fsValue);
        } else {
            lsFilter = fsColumn + " = " + SQLUtil.toSQL(fsValue);
        }

        String lsSQL = MiscUtil.addCondition(poModelMaster.makeSelectSQL(), " sTransNox LIKE "
                + SQLUtil.toSQL(fsValue + "%") + " AND " + lsCondition);

        poJSON = new JSONObject();

        if (p_bWithUI){
            poJSON = ShowDialogFX.Search(poGRider,
                    lsSQL,
                    fsValue,
                    "Transaction No»Date»Order No",
                    "sTransNox»dTransact»sOrderNox",
                    "sTransNox»dTransact»sOrderNox",
                    fbByCode ? 0 : 1);

            if (poJSON != null) {
                return openTransaction((String) poJSON.get("sTransNox"));

            } else {
                poJSON.put("result", "error");
                poJSON.put("message", "No record loaded to update.");
                return poJSON;
            }
        }
        //use for testing 
        if (fbByCode)
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sTransNox = " + SQLUtil.toSQL(fsValue)) + lsCondition;
        else {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sTransNox LIKE " + SQLUtil.toSQL("%" + fsValue + "%")) +  lsCondition;
            lsSQL += " LIMIT 1";
        }
        
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        
        try {
            if (!loRS.next()){
                MiscUtil.close(loRS);
                poJSON.put("result", "error");
                poJSON.put("message", "No record loaded.");
                return poJSON;
            }
            
            lsSQL = loRS.getString("sTransNox");
            MiscUtil.close(loRS);
        } catch (SQLException ex) {
            Logger.getLogger(InvStockRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return openTransaction(lsSQL);
    }

    @Override
    public JSONObject searchMaster(String string, String string1, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public JSONObject searchMaster(int i, String string, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Model_Inv_Stock_Req_Cancel_Master getMasterModel() {
        return poModelMaster;
    }

    @Override
    public JSONObject setMaster(int fnCol, Object foData) {
        return poModelMaster.setValue(fnCol, foData);
    }

    @Override
    public JSONObject setMaster(String fsCol, Object foData) {
        return poModelMaster.setValue(fsCol, foData);
    }

    @Override
    public int getEditMode() {
        return pnEditMode;
    }

    @Override
    public void setTransactionStatus(String fsValue) {
        psTranStatus = fsValue;
    }

    public JSONObject OpenModelDetail(String fsTransNo) {

        try {
            String lsSQL = MiscUtil.addCondition(poModelDetail.get(0).makeSQL(), "sTransNox = " + SQLUtil.toSQL(fsTransNo));
            ResultSet loRS = poGRider.executeQuery(lsSQL);

            while (loRS.next()) {

                poModelDetail.add(new Model_Inv_Stock_Req_Cancel_Detail(poGRider));
                poJSON = poModelDetail.get(poModelDetail.size() - 1).openRecord(loRS.getString("sTransNox"), loRS.getString("sStockIDx"));
                if ("error".equals((String) poJSON.get("result"))) {
                    return poJSON;
                } else {
                    poJSON = new JSONObject();
                    poJSON.put("result", "error");
                    poJSON.put("message", "No record loaded to Detail.");

                }
            }

            return poJSON;

        } catch (SQLException ex) {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", ex.getMessage());

            return poJSON;
        }
    }

    public JSONObject AddModelDetail() {
        String lsModelRequired = poModelDetail.get(poModelDetail.size() - 1).getStockID();
        if (!lsModelRequired.isEmpty()) {
            poModelDetail.add(new Model_Inv_Stock_Req_Cancel_Detail(poGRider));
            poModelDetail.get(poModelDetail.size() - 1).newRecord();
            poModelDetail.get(poModelDetail.size() - 1).setTransactionNumber(poModelMaster.getTransactionNumber());

        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "Information");
            poJSON.put("message", "Please Fill up Required Record Fist!");

        }

        return poJSON;
    }

    public void RemoveModelDetail(int fnRow) {
        poModelDetail.remove(fnRow - 1);
    }
    
    //process for inventory stock request
    /*This function use for browsing Inventory Stock Request 
    //fetching inventory request and set data for Inventory Stock Request Cancel
    */
    public JSONObject BrowseRequest(String fsColumn, String fsValue, boolean fbByCode){
        poJSON = new JSONObject();
        poRequest.setWithUI(p_bWithUI);
        poRequest.setTransactionStatus(psTranStatus);
        poJSON = poRequest.searchTransaction(fsColumn, fsValue, fbByCode);
        if("error".equalsIgnoreCase((String)poJSON.get("result"))){
            return poJSON;
        }
           
        if("error".equalsIgnoreCase((String)newTransaction().get("result"))){
            return poJSON;
        }
        setMaster("sBranchCd",poRequest.getMasterModel().getBranchCode());
        setMaster("xBranchNm",poRequest.getMasterModel().getBranchName());
        setMaster("sCategrCd",poRequest.getMasterModel().getCategoryCode());
        setMaster("xCategrNm",poRequest.getMasterModel().getCategoryName());
        setMaster("sOrderNox",poRequest.getMasterModel().getTransactionNumber());
        if(p_bWithUI){
            if(ShowMessageFX.YesNo("Do you want to display all request item from this transaction " + poRequest.getMasterModel().getTransactionNumber() + "?", "Computerized Acounting System", "Inventory Stock Request Cancel")){
                poModelDetail = new ArrayList<>();
                for(int lnCtr = 0; lnCtr < poRequest.getDetailModel().size()-1; lnCtr++){
                    poModelDetail.add(new Model_Inv_Stock_Req_Cancel_Detail(poGRider));
                    setDetail(lnCtr, "sOrderNox", (String) poRequest.getDetailModel().get(lnCtr).getTransactionNumber());
                    setDetail(lnCtr, "sStockIDx", (String) poRequest.getDetailModel().get(lnCtr).getStockID());
                    setDetail(lnCtr, "nQuantity", (int) poRequest.getDetailModel().get(lnCtr).getQuantity());
                    setDetail(lnCtr, "sNotesxxx", (String) poRequest.getDetailModel().get(lnCtr).getNotes());
                    setDetail(lnCtr, "xBarCodex", (String) poRequest.getDetailModel().get(lnCtr).getBarcode());
                    setDetail(lnCtr, "xDescript", (String) poRequest.getDetailModel().get(lnCtr).getDescription());
                }
            }
        } 
        poModelDetail = new ArrayList<>();
        System.out.println(poRequest.getDetailModel().size()-1);
        for(int lnCtr = 0; lnCtr <= poRequest.getDetailModel().size()-1; lnCtr++){
            poModelDetail.add(new Model_Inv_Stock_Req_Cancel_Detail(poGRider));
            
            setDetail(lnCtr, "sOrderNox", (String) poRequest.getDetailModel().get(lnCtr).getTransactionNumber());
            setDetail(lnCtr, "sStockIDx", (String) poRequest.getDetailModel().get(lnCtr).getStockID());
            setDetail(lnCtr, "nQuantity", (int) poRequest.getDetailModel().get(lnCtr).getQuantity());
            setDetail(lnCtr, "sNotesxxx", (String) poRequest.getDetailModel().get(lnCtr).getNotes());
            setDetail(lnCtr, "xBarCodex", (String) poRequest.getDetailModel().get(lnCtr).getBarcode());
            setDetail(lnCtr, "xDescript", (String) poRequest.getDetailModel().get(lnCtr).getDescription());
            System.out.println("poRequest.getStockID() " + lnCtr + " = " + (String) poRequest.getDetailModel().get(lnCtr).getStockID());
        }
//        poModelMaster.sett
        return poJSON;
    }
    //process for inventory stock request
    /*This function use for browsing Inventory Stock Request 
    //fetching inventory request detail and set data for Inventory Stock Request Cancel
    */
    public JSONObject BrowseRequestDetail(int fnRow, int fnCol, String fsValue, boolean fbByCode){
        poJSON = new JSONObject();
//        poJSON = poRequest.searchTransaction(fsColumn, fsValue, fbByCode);
        switch (fnCol) {

            case 3: //sBarCodex
                String lsSQL = new Model_Inv_Stock_Request_Detail(poGRider).getSQL();
//                lsSQL =  MiscUtil.addCondition(lsSQL, fsValue);
                lsSQL = MiscUtil.addCondition(lsSQL, "a.sTransNox = " + SQLUtil.toSQL(poModelMaster.getOrderNumber()));
                lsSQL = MiscUtil.addCondition(lsSQL, "b.sBarCodex LIKE " + SQLUtil.toSQL("%" +fsValue + "%"));


                if (p_bWithUI){
                    poJSON = ShowDialogFX.Search(poGRider,
                            lsSQL,
                            fsValue,
                            "Stock ID»Barcode»Name",
                            "sTransNox»xBarCodex»xDescript",
                            "a.sStockIDx»b.sBarCodex»b.sDescript",
                            fbByCode ? 1 : 2);

                    if (poJSON != null) {
                        poJSON = poRequest.OpenModelDetailByStockID(fsValue, lsSQL);
                        
                        if (poJSON != null) {
                            setDetail(fnRow, "sStockIDx", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size()-1).getStockID());
                            setDetail(fnRow, "xBarCodex", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size()-1).getBarcode());
                            setDetail(fnRow, "xDescript", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size()-1).getDescription());
                            setDetail(fnRow, "nQuantity", (int) poRequest.getDetailModel().get(poRequest.getDetailModel().size()-1).getQuantity());
                            setDetail(fnRow, "sNotesxxx", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size()-1).getNotes());
                        }else{
                            
                            poJSON.put("result", "error");
                            poJSON.put("message", "No record loaded to update.");
                            return poJSON;
                        }

                    } else {
                        poJSON.put("result", "error");
                        poJSON.put("message", "No record loaded to update.");
                        return poJSON;
                    }
                }
        }
//        poModelMaster.sett
        return poJSON;
    }
}
