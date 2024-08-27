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
import org.json.simple.JSONObject;

/**
 *
 * @author Maynard
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
        poModelDetail = new ArrayList<Model_Inv_Stock_Request_Detail>();
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
                Inventory loInventory = new Inventory(poGRider, true);
                loInventory.setRecordStatus(psTranStatus);
                loInventory.setWithUI(p_bWithUI);
                poJSON = loInventory.searchRecordByStockID(fsValue, fbByCode);

                if (poJSON != null) {
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
                        setDetail(fnRow, "nQtyOnHnd", (int) loInvMaster.getModel().getQtyOnHnd());
                        setDetail(fnRow, "nResvOrdr", (int) loInvMaster.getModel().getResvOrdr());
                        setDetail(fnRow, "nBackOrdr", (int) loInvMaster.getModel().getBackOrdr());
                        setDetail(fnRow, "nAvgMonSl", (int) loInvMaster.getModel().getAvgMonSl());
                        setDetail(fnRow, "nMaxLevel", (int) loInvMaster.getModel().getMaxLevel());
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
            String lsSQL = MiscUtil.addCondition(poModelDetail.get(0).makeSQL(), "sTransNox = " + SQLUtil.toSQL(fsTransNo));
            ResultSet loRS = poGRider.executeQuery(lsSQL);

            while (loRS.next()) {

                poModelDetail.add(new Model_Inv_Stock_Request_Detail(poGRider));
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
            poModelDetail.add(new Model_Inv_Stock_Request_Detail(poGRider));
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
}
