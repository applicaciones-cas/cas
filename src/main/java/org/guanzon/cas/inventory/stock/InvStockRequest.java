package org.guanzon.cas.inventory.stock;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.appdriver.iface.GTranDet;
import org.guanzon.cas.inventory.base.InvMaster;
import org.guanzon.cas.inventory.base.Inventory;
import org.guanzon.cas.inventory.models.Model_Inv_Stock_Request_Detail;
import org.guanzon.cas.inventory.models.Model_Inv_Stock_Request_Master;
import org.guanzon.cas.parameters.Category;
import org.guanzon.cas.parameters.Inv_Type;
import org.json.simple.JSONObject;

/**
 *
 * @author Unclejo
 */
public class InvStockRequest implements GTranDet {

    GRider poGRider;
    boolean pbWthParent;
    int pnEditMode;
    String psTranStatus;

    private boolean p_bWithUI = true;
    Model_Inv_Stock_Request_Master poModelMaster;
    ArrayList<Model_Inv_Stock_Request_Detail> poModelDetail;

    JSONObject poJSON;

    public void setWithUI(boolean fbValue){
        p_bWithUI = fbValue;
    }
    public InvStockRequest(GRider foGRider, boolean fbWthParent) {
        poGRider = foGRider;
        pbWthParent = fbWthParent;

        poModelMaster = new Model_Inv_Stock_Request_Master(foGRider);
        poModelDetail = new ArrayList<>();
        poModelDetail.add(new Model_Inv_Stock_Request_Detail(foGRider));
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
        Category loCateg = new Category(poGRider, true);
        switch (poGRider.getDivisionCode()) {
            case "0"://mobilephone
                loCateg.openRecord("0002");
                break;

            case "1"://motorycycle
                loCateg.openRecord("0001");
                break;

            case "2"://Auto Group - Honda Cars
            case "5"://Auto Group - Nissan
            case "6"://Auto Group - Any
                loCateg.openRecord("0003");
                break;

            case "3"://Hospitality
            case "4"://Pedritos Group
                loCateg.openRecord("0004");
                break;

            case "7"://Guanzon Services Office
                 break;

            case "8"://Main Office
                break;
        }
        poModelMaster.setCategoryCode((String) loCateg.getMaster("sCategrCd"));
        poModelMaster.setCategoryName((String) loCateg.getMaster("sDescript"));


        poJSON = new JSONObject();

        poModelDetail.get(getItemCount() - 1).newRecord();
        poJSON = poModelDetail.get(getItemCount() - 1).setTransactionNumber(poModelMaster.getTransactionNumber());
        if ("error".equals((String) poJSON.get("result"))) {
            poJSON.put("result", "error");
            poJSON.put("message", "No record to load.");
            return poJSON;

        }
        
        pnEditMode = EditMode.ADDNEW;

        poJSON.put("result", "success");
        return poJSON;
    }

    @Override
    public JSONObject openTransaction(String fsValue) {
        
        poJSON = new JSONObject();
        poModelMaster.openRecord(fsValue);
        if ("error".equals((String) poJSON.get("result"))) {
            return poJSON;
        }

        OpenModelDetail(poModelMaster.getTransactionNumber());
        
        pnEditMode = EditMode.READY;

        return poJSON;

    }

    @Override
    public JSONObject updateTransaction() {
        poJSON = new JSONObject();

//        if (poModelMaster.getEditMode() == EditMode.UPDATE) {
//            loJSON.put("result", "success");
//            loJSON.put("message", "Edit mode has changed to update.");
//        } else {
//            loJSON.put("result", "error");
//            loJSON.put("message", "No record loaded to update.");
//        }
     System.out.print("\n updateRecord editmode == " + pnEditMode + "\n");
//        pnEditMode = EditMode.UPDATE;
        poJSON = new JSONObject();
        if (pnEditMode != EditMode.READY && pnEditMode != EditMode.UPDATE){
            poJSON.put("result", "error");
            poJSON.put("message", "Invalid edit mode.");
            return poJSON;
        }
        pnEditMode = EditMode.UPDATE;
        poJSON.put("result", "success");
        poJSON.put("message", "Update mode success.");
         System.out.print("\n updateRecord editmode2 == " + pnEditMode + "\n");
        return poJSON;

    }

    @Override
    public JSONObject saveTransaction() {
        
        poJSON = new JSONObject();
        if (!pbWthParent) {
            poGRider.beginTrans();
        }

        if (getItemCount() >= 1) {
            for (int lnCtr = 0; lnCtr <= getItemCount() - 1; lnCtr++) {
                if(lnCtr>=0){
                    if(poModelDetail.get(lnCtr).getStockID().isEmpty() || poModelDetail.get(lnCtr).getBarcode().isEmpty()){
                        poModelDetail.remove(lnCtr);
                        if (lnCtr>poModelDetail.size()-1){
                            break;
//                            poJSON.put("result", "error");
//                            poJSON.put("continue",true);
//                            poJSON.put("message", "No inventory item detected.");
//                            return poJSON;
                        }
    //                    System.out.println("size = " + poSubUnit.getMaster().size());
                    }
                }
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
            
            if (poModelMaster.getTransactionStatus().equalsIgnoreCase(TransactionStatus.STATE_CLOSED)){
                poJSON.put("result", "error");
                poJSON.put("message", "This transaction was already close.");
                return poJSON;
            }
            if ("error".equals((String) isProcessed("close").get("result"))) {
                return poJSON;
            }
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
            if ("error".equals((String) isProcessed("post").get("result"))) {
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
            if ("error".equals((String) isProcessed("void").get("result"))) {
                return poJSON;
            }
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
            if ("error".equals((String) isProcessed("cancel").get("result"))) {
                return poJSON;
            }
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
    private JSONObject isProcessed(String lsMessage){
        poJSON = new JSONObject();
        if (poModelMaster.getTransactionStatus().equalsIgnoreCase(TransactionStatus.STATE_POSTED)
            || poModelMaster.getTransactionStatus().equalsIgnoreCase(TransactionStatus.STATE_CANCELLED)
            || poModelMaster.getTransactionStatus().equalsIgnoreCase(TransactionStatus.STATE_VOID)) {
            
            poJSON.put("result", "error");
            poJSON.put("message","Unable to " + lsMessage + " proccesed transaction.");
            return poJSON;
        }
        poJSON.put("result", "success");
        poJSON.put("message","Okay.");
        return poJSON;
    }

    @Override
    public int getItemCount() {
        return poModelDetail.size();
    }


    @Override
    public Model_Inv_Stock_Request_Detail getDetailModel(int fnRow) {
        return poModelDetail.get(fnRow);

    }
//    @Override
    public ArrayList<Model_Inv_Stock_Request_Detail> getDetailModel() {
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
                if(poModelMaster.getCategoryCode().isEmpty()){
                    poJSON.put("result", "error");
                    poJSON.put("message", "Please choose a category first..");
                    return poJSON;
                }
                Inventory loInventory = new Inventory(poGRider, true);
                loInventory.setRecordStatus(psTranStatus);
                loInventory.setWithUI(p_bWithUI);
                poJSON = loInventory.searchRecordWithContition(fsValue, "sCategCd1 = " + SQLUtil.toSQL(poModelMaster.getCategoryCode()), fbByCode);

                if (poJSON != null) {
                    for(int lnCtr = 0; lnCtr < poModelDetail.size(); lnCtr++){
                        if(poModelDetail.get(lnCtr).getStockID().equalsIgnoreCase((String) loInventory.getModel().getStockID())){
                            poJSON = new JSONObject();
                            poJSON.put("result", "error");
                            poJSON.put("message", "Inventory item request already added.");
                            return poJSON;
                        }
                    }
                    setDetail(fnRow, 3, (String) loInventory.getModel().getStockID());
                    setDetail(fnRow, "xBarCodex", (String) loInventory.getModel().getBarcode());
                    setDetail(fnRow, "xDescript", (String) loInventory.getModel().getDescription());
                    setDetail(fnRow, "xCategr01", (String) loInventory.getModel().getCategName1());
                    setDetail(fnRow, "xCategr02", (String) loInventory.getModel().getCategName2());
                    setDetail(fnRow, "xInvTypNm", (String) loInventory.getModel().getInvTypNm());
                    InvMaster loInvMaster = new InvMaster(poGRider, true);
                    loInvMaster.setRecordStatus(psTranStatus);
                    loInvMaster.setWithUI(p_bWithUI);
                    poJSON = loInvMaster.openRecord(loInventory.getModel().getStockID());

                    if (poJSON != null) {
                        setDetail(fnRow, "cClassify", (String) loInvMaster.getModel().getClassify());
                        setDetail(fnRow, "nQtyOnHnd",  loInvMaster.getModel().getQtyOnHnd());
                        setDetail(fnRow, "nResvOrdr",  loInvMaster.getModel().getResvOrdr());
                        setDetail(fnRow, "nBackOrdr",  loInvMaster.getModel().getBackOrdr());
                        setDetail(fnRow, "nAvgMonSl",  loInvMaster.getModel().getAvgMonSl());
                        setDetail(fnRow, "nMaxLevel",  loInvMaster.getModel().getMaxLevel());
                    }
                    return poJSON;

                } else {
                    poJSON = new JSONObject();
                    poJSON.put("result", "error");
                    poJSON.put("message", "No record found.");
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

            lsCondition = "a.cTranStat IN (" + lsCondition.substring(2) + ")";
        } else {
            lsCondition = "a.cTranStat = " + SQLUtil.toSQL(psTranStatus);
        }

        String lsSQL = MiscUtil.addCondition(poModelMaster.getSQL(), " a.sTransNox LIKE "
                + SQLUtil.toSQL(fsValue + "%") + " AND " + lsCondition);

        poJSON = new JSONObject();

        if (p_bWithUI){
            poJSON = ShowDialogFX.Search(poGRider,
                    lsSQL,
                    fsValue,
                    "Transaction No»Date»Refer No",
                    "sTransNox»dTransact»sReferNox",
                    "sTransNox»dTransact»sReferNox",
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
        lsSQL += " LIMIT 1";
        System.out.println(lsSQL);
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
    public JSONObject searchMaster(String fsCol, String fsVal, boolean fbByCode) {
        return searchMaster(poModelMaster.getColumn(fsCol), fsVal, fbByCode);
    }

    @Override
    public JSONObject searchMaster(int fnColumn, String fsValue, boolean fbByCode) {
        poJSON = new JSONObject();
        switch(fnColumn){
            case 3: //sCategrCd
                Category loCategory = new Category(poGRider, true);
                loCategory.setRecordStatus(psTranStatus);
                poJSON = loCategory.searchRecord(fsValue, fbByCode);

                if (poJSON != null){
                    setMaster(fnColumn, (String) loCategory.getMaster("sCategrCd"));
                    return setMaster("xCategrNm", (String)loCategory.getMaster("sDescript"));
                } else {
                    
                    poJSON = new JSONObject();
                    poJSON.put("result", "error");
                    poJSON.put("message", "No record found.");
                    return poJSON;
                }
            case 4: //sInvTypCd
                Inv_Type loType = new Inv_Type(poGRider, true);
                loType.setRecordStatus(psTranStatus);
                poJSON = loType.searchRecord(fsValue, fbByCode);

                if (poJSON != null){

                    setMaster(fnColumn, (String) loType.getMaster("sInvTypCd"));
                    return setMaster("xInvTypNm", (String)loType.getMaster("sDescript"));
                } else {
                    
                    poJSON = new JSONObject();
                    poJSON.put("result", "error");
                    poJSON.put("message", "No record found.");
                    return poJSON;
                }
                
            default:
                return null;
                
        }
    }

    @Override
    public Model_Inv_Stock_Request_Master getMasterModel() {
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
            String lsSQL = MiscUtil.addCondition(new Model_Inv_Stock_Request_Detail(poGRider).getSQL(), "a.sTransNox = " + SQLUtil.toSQL(fsTransNo));
            ResultSet loRS = poGRider.executeQuery(lsSQL);
            poModelDetail = new ArrayList<>();
            while (loRS.next()) {

                poModelDetail.add(new Model_Inv_Stock_Request_Detail(poGRider));
                poJSON = poModelDetail.get(poModelDetail.size() - 1).openRecord(loRS.getString("sTransNox"), loRS.getString("sStockIDx"));
                if ("error".equals((String) poJSON.get("result"))) {
                    return poJSON;
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
    

    public JSONObject SearchDetailRequest(String fsTransNo, String fsStockID) {

        poJSON = new JSONObject();
        try {
            String lsSQL = MiscUtil.addCondition(poModelDetail.get(0).makeSQL(), "sTransNox = " + SQLUtil.toSQL(fsTransNo));
            lsSQL = MiscUtil.addCondition(lsSQL, "sStockIDx = " + SQLUtil.toSQL(fsStockID));
            ResultSet loRS = poGRider.executeQuery(lsSQL);
            poModelDetail =  new ArrayList<>();
            while (loRS.next()) {

                poModelDetail.add(new Model_Inv_Stock_Request_Detail(poGRider));
                poJSON = poModelDetail.get(poModelDetail.size() - 1).openRecord(loRS.getString("sTransNox"));
                if ("error".equals((String) poJSON.get("result"))) {
                    return poJSON;
                }
                
                poJSON.put("result", "success");
                poJSON.put("message", "Record successfully loaded to Detail.");
            }

            return poJSON;

        } catch (SQLException ex) {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", ex.getMessage());

            return poJSON;
        }
    }
    

    //process for inventory stock request
    /* This function use for browsing Inventory Stock Request 
     * fetching inventory request detail and set data for Inventory Stock Request Cancel
     * fetching detail by stock id
    */
    public JSONObject OpenModelDetailByStockID(String fsTransNo, String fsStockID) {

        try {
            String lsSQL = MiscUtil.addCondition(new Model_Inv_Stock_Request_Detail(poGRider).makeSelectSQL(), "sTransNox = " + SQLUtil.toSQL(fsTransNo));
            lsSQL = MiscUtil.addCondition(lsSQL, "sStockIDx = " + SQLUtil.toSQL(fsStockID));
            System.out.println(lsSQL);
            ResultSet loRS = poGRider.executeQuery(lsSQL);
            poModelDetail =  new ArrayList<>();
            while (loRS.next()) {

                poModelDetail.add(new Model_Inv_Stock_Request_Detail(poGRider));
                poJSON = poModelDetail.get(poModelDetail.size() - 1).openRecord(loRS.getString("sTransNox"), loRS.getString("sStockIDx"));
                if ("error".equals((String) poJSON.get("result"))) {
                    return poJSON;
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

//    public JSONObject AddModelDetail() {
//        String lsModelRequired = poModelDetail.get(poModelDetail.size() - 1).getStockID();
//        if (!lsModelRequired.isEmpty()) {
//            poModelDetail.add(new Model_Inv_Stock_Request_Detail(poGRider));
//            poModelDetail.get(poModelDetail.size() - 1).newRecord();
//            poModelDetail.get(poModelDetail.size() - 1).setTransactionNumber(poModelMaster.getTransactionNumber());
//
//        } else {
//            poJSON = new JSONObject();
//            poJSON.put("result", "Information");
//            poJSON.put("message", "Please Fill up Required Record Fist!");
//
//        }
//
//        return poJSON;
//    }

    public JSONObject AddModelDetail() {
        poJSON = new JSONObject();
        if (poModelDetail.isEmpty()){
            poModelDetail.add(new Model_Inv_Stock_Request_Detail(poGRider));
            poModelDetail.get(0).newRecord();
            poModelDetail.get(0).setEntryNumber(poModelDetail.size());
            poModelDetail.get(0).setTransactionNumber(poModelMaster.getTransactionNumber());
            poJSON.put("result", "success");
            poJSON.put("message", "Inventory request add record.");
            

        } else {
            poModelDetail.add(new Model_Inv_Stock_Request_Detail(poGRider));
            poModelDetail.get(poModelDetail.size()-1).newRecord();
            poModelDetail.get(poModelDetail.size()-1).setEntryNumber(poModelDetail.size());
            poModelDetail.get(poModelDetail.size() - 1).setTransactionNumber(poModelMaster.getTransactionNumber());
            
            poJSON.put("result", "success");
            poJSON.put("message", "Inventory request add record.");
        }
        System.out.println(poModelDetail.size());

        return poJSON;
    }

    public void RemoveModelDetail(int fnRow) {
        if(poModelDetail.size()==0){
            AddModelDetail();
        }
        poModelDetail.remove(fnRow - 1);

    }
    
}
