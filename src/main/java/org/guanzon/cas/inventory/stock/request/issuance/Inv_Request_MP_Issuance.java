package org.guanzon.cas.inventory.stock.request.issuance;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.cas.inventory.base.InvMaster;
import org.guanzon.cas.inventory.base.Inventory;
import org.guanzon.cas.inventory.base.InventoryTrans;
import org.guanzon.cas.inventory.models.Model_Inv_Stock_Request_Detail;
import org.guanzon.cas.inventory.models.Model_Inv_Stock_Request_Master;
import org.guanzon.cas.inventory.stock.Inv_Request;
import org.guanzon.cas.inventory.stock.request.RequestControllerFactory.RequestType;
import org.guanzon.cas.inventory.stock.request.RequestControllerFactory.RequestCategoryType;
import org.guanzon.cas.inventory.stock.request.RequestIssuanceController;
import org.guanzon.cas.parameters.Category;
import org.guanzon.cas.parameters.Inv_Type;
import org.json.simple.JSONObject;

/**
 *
 * @author Unclejo
 */
public class Inv_Request_MP_Issuance implements RequestIssuanceController {

    GRider poGRider;
    boolean pbWthParent;
    int pnEditMode;
    String psTranStatus;

    private boolean p_bWithUI = true;
    Inv_Request poRequest;
    Model_Inv_Stock_Request_Master poModelMaster;
    ArrayList<Model_Inv_Stock_Request_Master> poMasterList;
    ArrayList<Model_Inv_Stock_Request_Detail> poModelDetail;
    RequestType type;
    RequestCategoryType category_type;
    // Create a backup list to store deleted records temporarily
    private List<Model_Inv_Stock_Request_Detail> backupRecords = new ArrayList<>();

    int roqSaveCount = 0;
    JSONObject poJSON;

    public void setWithUI(boolean fbValue) {
        p_bWithUI = fbValue;
    }

    public Inv_Request_MP_Issuance(GRider foGRider, boolean fbWthParent) {
        poGRider = foGRider;
        pbWthParent = fbWthParent;

        poModelMaster = new Model_Inv_Stock_Request_Master(foGRider);
        poMasterList = new ArrayList<>();
        poMasterList.add(new Model_Inv_Stock_Request_Master(foGRider));
        poModelDetail = new ArrayList<>();
        poModelDetail.add(new Model_Inv_Stock_Request_Detail(foGRider));
        poRequest = new Inv_Request(foGRider, fbWthParent);
        pnEditMode = EditMode.UNKNOWN;
    }

    @Override
    public JSONObject newTransaction() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public JSONObject openTransaction(String fsValue) {

        poJSON = new JSONObject();

        poModelMaster = new Model_Inv_Stock_Request_Master(poGRider);
        poModelMaster.openRecord(fsValue);
        if ("error".equals((String) poJSON.get("result"))) {
            return poJSON;
        }

        poJSON = OpenModelDetail(poModelMaster.getTransactionNumber());

        pnEditMode = EditMode.READY;

        return poJSON;

    }

    @Override
    public JSONObject updateTransaction() {
        poJSON = new JSONObject();
        poJSON = new JSONObject();
        if (pnEditMode != EditMode.READY && pnEditMode != EditMode.UPDATE) {
            poJSON.put("result", "error");
            poJSON.put("message", "Invalid edit mode.");
            return poJSON;
        }
        pnEditMode = EditMode.UPDATE;

//        AddModelDetail();
        poJSON.put("result", "success");
        poJSON.put("message", "Update mode success.");
        System.out.print("  updateRecord editmode2 == " + pnEditMode + " ");
        return poJSON;

    }

    @Override
    public JSONObject saveTransaction() {

        poJSON = new JSONObject();
        if (!pbWthParent) {
            poGRider.beginTrans();
        }

        poJSON = deleteRecord();
        if ("error".equals((String) poJSON.get("result"))) {
            if (!pbWthParent) {
                poGRider.rollbackTrans();
            }
            return poJSON;
        }
        poJSON = SaveDetail();
        if ("error".equals((String) poJSON.get("result"))) {
            if (!pbWthParent) {
                restoreData();
                poGRider.rollbackTrans();
            }
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
            }
            restoreData();
            poJSON.put("result", "error");
            poJSON.put("message", "Unable to Save Transaction.");
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

        if (poModelMaster.getEditMode() == EditMode.READY
                || poModelMaster.getEditMode() == EditMode.UPDATE) {

            if (poModelMaster.getTransactionStatus().equalsIgnoreCase(TransactionStatus.STATE_CLOSED)) {
                poJSON.put("result", "success");
                poJSON.put("message", "This transaction was already close.");
                return poJSON;

            }
            if ("error".equals((String) isProcessed("close").get("result"))) {
                return poJSON;
            }

            poJSON = poModelMaster.setModifiedBy(poGRider.getUserID());
            if ("error".equals((String) poJSON.get("result"))) {
                return poJSON;
            }
            poJSON = poModelMaster.setModifiedDate(poGRider.getServerDate());
            if ("error".equals((String) poJSON.get("result"))) {
                return poJSON;
            }
            poJSON = poModelMaster.setTransactionStatus(TransactionStatus.STATE_CLOSED);
            if ("error".equals((String) poJSON.get("result"))) {
                return poJSON;
            }
            InventoryTrans loInvTrans = new InventoryTrans(poGRider, pbWthParent);
            loInvTrans.newTransaction();
            for (int lnCtr = 0; lnCtr < getItemCount(); lnCtr++) {
                loInvTrans.setDetail(lnCtr, "sStockIDx", getDetailModel(lnCtr).getStockID());
                loInvTrans.setDetail(lnCtr, "nQuantity", (Integer.parseInt(getDetailModel(lnCtr).getQuantity().toString()) - getDetailModel(lnCtr).getIssueQuantity()));
            }
            poJSON = loInvTrans.Purchase(poModelMaster.getTransactionNumber(), poGRider.getServerDate(), EditMode.ADDNEW);

            if ("error".equals((String) poJSON.get("result"))) {
                if (!pbWthParent) {
                    poGRider.rollbackTrans();
                }
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

    private JSONObject isProcessed(String lsMessage) {
        poJSON = new JSONObject();
        if (poModelMaster.getTransactionStatus().equalsIgnoreCase(TransactionStatus.STATE_POSTED)
                || poModelMaster.getTransactionStatus().equalsIgnoreCase(TransactionStatus.STATE_CANCELLED)
                || poModelMaster.getTransactionStatus().equalsIgnoreCase(TransactionStatus.STATE_VOID)) {

            poJSON.put("result", "error");
            poJSON.put("message", "Unable to " + lsMessage + " proccesed transaction.");
            return poJSON;
        }
        poJSON.put("result", "success");
        poJSON.put("message", "Okay.");
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
                if (poModelMaster.getCategoryCode().isEmpty()) {
                    poJSON.put("result", "error");
                    poJSON.put("message", "Please choose a category first..");
                    return poJSON;
                }
                if (poModelDetail.get(fnRow).getStockID().isEmpty()) {
                    poJSON.put("result", "success");
                    poJSON.put("message", "Record already loaded.");
                    return poJSON;
                }
                Inventory loInventory = new Inventory(poGRider, true);
                loInventory.setRecordStatus(psTranStatus);
                loInventory.setWithUI(p_bWithUI);
                poJSON = loInventory.searchRecordWithContition(fsValue, "a.sCategCd1 = " + SQLUtil.toSQL(poModelMaster.getCategoryCode()) + " AND a.sCategCd2 = " + SQLUtil.toSQL("0007"), fbByCode);

                if (poJSON != null) {
                    for (int lnCtr = 0; lnCtr < poModelDetail.size(); lnCtr++) {
                        if (poModelDetail.get(lnCtr).getStockID().equalsIgnoreCase((String) loInventory.getModel().getStockID())) {
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
                    setDetail(fnRow, "xBrandNme", (String) loInventory.getModel().getBrandName());
                    setDetail(fnRow, "xModelNme", (String) loInventory.getModel().getModelName());
                    setDetail(fnRow, "xModelDsc", (String) loInventory.getMaster("xModelDsc"));
                    setDetail(fnRow, "xColorNme", (String) loInventory.getModel().getColorName());
                    setDetail(fnRow, "xMeasurNm", (String) loInventory.getModel().getMeasureName());
                    InvMaster loInvMaster = new InvMaster(poGRider, true);
                    loInvMaster.setRecordStatus(psTranStatus);
                    loInvMaster.setWithUI(p_bWithUI);
                    poJSON = loInvMaster.openRecord(loInventory.getModel().getStockID());

//                    if (poJSON != null) {
                    if ("success".equals((String) poJSON.get("result"))) {
                        setDetail(fnRow, "cClassify", (String) loInvMaster.getModel().getClassify());
                        setDetail(fnRow, "nQtyOnHnd", loInvMaster.getModel().getQtyOnHnd());
                        setDetail(fnRow, "nResvOrdr", loInvMaster.getModel().getResvOrdr());
                        setDetail(fnRow, "nBackOrdr", loInvMaster.getModel().getBackOrdr());
                        setDetail(fnRow, "nAvgMonSl", loInvMaster.getModel().getAvgMonSl());
                        setDetail(fnRow, "nMaxLevel", loInvMaster.getModel().getMaxLevel());
                    } else {
                        setDetail(fnRow, 3, "");
                        setDetail(fnRow, "xBarCodex", "");
                        setDetail(fnRow, "xDescript", "");
                        setDetail(fnRow, "xCategr01", "");
                        setDetail(fnRow, "xCategr02", "");
                        setDetail(fnRow, "xInvTypNm", "");
                        setDetail(fnRow, "xBrandNme", "");
                        setDetail(fnRow, "xModelNme", "");
                        setDetail(fnRow, "xModelDsc", "");
                        setDetail(fnRow, "xColorNme", "");
                        setDetail(fnRow, "xMeasurNm", "");
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

        String lsSQL = MiscUtil.addCondition(getSQL(), " a.sTransNox LIKE "
                + SQLUtil.toSQL(fsValue + "%") + " AND LEFT(a.sTransNox, 4) = " + SQLUtil.toSQL(poGRider.getBranchCode()) + " AND " + lsCondition
                + " AND f.sCategCd1 = '0002'") + " GROUP BY a.sTransNox ASC";

        poJSON = new JSONObject();
        System.out.println("searchTransaction = " + lsSQL);
        if (p_bWithUI) {
            poJSON = ShowDialogFX.Search(poGRider,
                    lsSQL,
                    fsValue,
                    "Transaction No»Branch»Date»Remarks",
                    "sTransNox»xBranchNm»dTransact»sRemarksx",
                    "a.sTransNox»b.sBranchNm»a.dTransact»a.sRemarksx",
                    fbByCode ? 0 : 1);

            if (poJSON == null || "error".equals((String) poJSON.get("result"))) {
                poJSON = new JSONObject();
                poJSON.put("result", "error");
                poJSON.put("message", "No record loaded.");
                return poJSON;
            } else {
                return openTransaction((String) poJSON.get("sTransNox"));
            }
        }
        //use for testing 
        lsSQL += " LIMIT 1";
        System.out.println(lsSQL);
        ResultSet loRS = poGRider.executeQuery(lsSQL);

        try {
            if (!loRS.next()) {
                MiscUtil.close(loRS);
                poJSON.put("result", "error");
                poJSON.put("message", "No record loaded.");
                return poJSON;
            }

            lsSQL = loRS.getString("sTransNox");
            MiscUtil.close(loRS);
        } catch (SQLException ex) {
            Logger.getLogger(Inv_Request_MP_Issuance.class.getName()).log(Level.SEVERE, null, ex);
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
        switch (fnColumn) {
            case 3: //sCategrCd
                Category loCategory = new Category(poGRider, true);
                loCategory.setRecordStatus(psTranStatus);
                poJSON = loCategory.searchRecord(fsValue, fbByCode);

                if (poJSON != null) {
                    setMaster(fnColumn, (String) loCategory.getMaster("sCategrCd"));
                    return setMaster("xCategrNm", (String) loCategory.getMaster("sDescript"));
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

                if (poJSON != null) {

                    setMaster(fnColumn, (String) loType.getMaster("sInvTypCd"));
                    return setMaster("xInvTypNm", (String) loType.getMaster("sDescript"));
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
            System.out.println("lsSQL value = " + lsSQL);
            lsSQL = MiscUtil.addCondition(lsSQL, "j.sBranchCd = " + SQLUtil.toSQL(poModelMaster.getBranchCode()));
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
            poModelDetail = new ArrayList<>();
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

    /*process for inventory stock request
     * This function use for browsing Inventory Stock Request 
     * fetching inventory request detail and set data for Inventory Stock Request Cancel
     * fetching detail by stock id
     */
    public JSONObject OpenModelDetailByStockID(String fsTransNo, String fsStockID) {

        try {
            String lsSQL = MiscUtil.addCondition(new Model_Inv_Stock_Request_Detail(poGRider).makeSelectSQL(), "sTransNox = " + SQLUtil.toSQL(fsTransNo));
            lsSQL = MiscUtil.addCondition(lsSQL, "sStockIDx = " + SQLUtil.toSQL(fsStockID));
            System.out.println(lsSQL);
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

    public JSONObject AddModelDetail() {
        poJSON = new JSONObject();
        if (poModelDetail.isEmpty()) {
            poModelDetail.add(new Model_Inv_Stock_Request_Detail(poGRider));
            poModelDetail.get(0).newRecord();
            poModelDetail.get(0).setTransactionNumber(poModelMaster.getTransactionNumber());
            poJSON.put("result", "success");
            poJSON.put("message", "Inventory request add record.");

        } else {
//            Validator_Inv_Stock_Request_SP_Approval_Detail validator = new Validator_Inv_Stock_Request_SP_Approval_Detail(poModelDetail.get(poModelDetail.size()-1));
//            if (!validator.isEntryOkay()){
//                poJSON.put("result", "error");
//                poJSON.put("message", validator.getMessage());
//                return poJSON;
//
//            }
            poModelDetail.add(new Model_Inv_Stock_Request_Detail(poGRider));
            poModelDetail.get(poModelDetail.size() - 1).newRecord();
            poModelDetail.get(poModelDetail.size() - 1).setTransactionNumber(poModelMaster.getTransactionNumber());

            poJSON.put("result", "success");
            poJSON.put("message", "Inventory request add record.");
        }
        System.out.println(poModelDetail.size());

        return poJSON;
    }

    public JSONObject AddModelMasterList() {
        poJSON = new JSONObject();
        if (poMasterList.isEmpty()) {
            poMasterList.add(new Model_Inv_Stock_Request_Master(poGRider));
            poMasterList.get(0).newRecord();
            poJSON.put("result", "success");
            poJSON.put("message", "Inventory request add record.");

        } else {
//            Validator_Inv_Stock_Request_SP_Approval_Detail validator = new Validator_Inv_Stock_Request_SP_Approval_Detail(poModelDetail.get(poModelDetail.size()-1));
//            if (!validator.isEntryOkay()){
//                poJSON.put("result", "error");
//                poJSON.put("message", validator.getMessage());
//                return poJSON;
//
//            }
            poMasterList.add(new Model_Inv_Stock_Request_Master(poGRider));
            poMasterList.get(poMasterList.size() - 1).newRecord();

            poJSON.put("result", "success");
            poJSON.put("message", "Inventory request add record.");
        }
        System.out.println(poModelDetail.size());

        return poJSON;
    }

    private JSONObject SaveDetail() {
        poJSON = new JSONObject();
        // Check if there are items to process
        if (getItemCount() >= 1) {
            // Loop in reverse order to avoid index shifting when removing elements
            for (int lnCtr = getItemCount() - 1; lnCtr >= 0; lnCtr--) {
                // Check if StockID or Barcode is empty
                if (poModelDetail.get(lnCtr).getStockID().isEmpty() || poModelDetail.get(lnCtr).getBarcode().isEmpty()) {
                    // Remove the empty record
                    poModelDetail.remove(lnCtr);
                }
            }
            // After cleaning, check if any valid items are left
            if (getItemCount() > 0) {

                int lnCtr = 0;
                int lnModified = 0;
                double lnQuantity = 0;
                double lnApproved = 0;
                double lnCancelld = 0;
                double lnIssueQty = 0;
                boolean lbReqApproval = false;
                for (lnCtr = 0; lnCtr <= getItemCount() - 1; lnCtr++) {
                    lnQuantity = Double.parseDouble(String.valueOf(poModelDetail.get(lnCtr).getQuantity()));
                    lnApproved = Double.parseDouble(String.valueOf(poModelDetail.get(lnCtr).getApproved()));
                    lnCancelld = Double.parseDouble(String.valueOf(poModelDetail.get(lnCtr).getCancelled()));
                    lnIssueQty = Double.parseDouble(String.valueOf(poModelDetail.get(lnCtr).getIssueQuantity()));

                    InvMaster loInvMaster = new InvMaster(poGRider, true);
                    loInvMaster.openRecord(poModelDetail.get(lnCtr).getStockID());
                    if (lnIssueQty > 0) {
                        lnModified++;
                    }

                    if (loInvMaster != null) {
                        if (Double.valueOf(loInvMaster.getMaster("nQtyOnHnd").toString()) < lnIssueQty) {
                            poJSON.put("result", "error");
                            poJSON.put("message", "Unable to save. The issued quantity for an item exceeds the available quantity.");
                            return poJSON;
                        }

                    }
                }
                if (lbReqApproval) {
                    if (poGRider.getUserLevel() < UserRight.SUPERVISOR) {
                        JSONObject loJSON = ShowDialogFX.getUserApproval(poGRider);
                        if ("success".equals((String) loJSON.get("result"))) {
                            if ((int) loJSON.get("nUserLevl") < UserRight.SUPERVISOR) {
                                restoreData();
                                poJSON.put("result", "error");
                                poJSON.put("message", "Only managerial accounts can approved transactions.(Authentication failed!!!)");
                                return poJSON;
                            }
                            System.out.println("loJSON = " + loJSON.toJSONString());
                            poModelMaster.setApproved((String) loJSON.get("sUserIDxx"));
                            poModelMaster.setApprovedDate(poGRider.getServerDate());
//                            poModelMaster.setApproveCode();
                        } else {
                            restoreData();
                            poJSON.put("result", "error");
                            poJSON.put("message", "Seek Manager's Approval for this Stock Request!.(Authentication required!!!)");
                            return poJSON;
                        }
                    } else {
                        poModelMaster.setApproved(poGRider.getUserID());
                        poModelMaster.setApprovedDate(poGRider.getServerDate());
                    }
                }

                for (lnCtr = 0; lnCtr < getItemCount(); lnCtr++) {
                    poModelDetail.get(lnCtr).setEditMode(EditMode.ADDNEW);
                    poModelDetail.get(lnCtr).setEntryNumber(lnCtr + 1);

//                    Validator_Inv_Stock_Request_SP_Approval_Detail validator = new Validator_Inv_Stock_Request_SP_Approval_Detail(poModelDetail.get(poModelDetail.size()-1));
//                    if (!validator.isEntryOkay()){
//                        restoreData();
//                        poJSON.put("result", "error");
//                        poJSON.put("message", validator.getMessage());
//                        return poJSON;
//
//                    }
                    poJSON = poModelDetail.get(lnCtr).saveRecord();

                    if ("error".equals((String) poJSON.get("result"))) {

                        restoreData();
                        return poJSON;
                    }
                    // Proceed with saving remaining items
                    poJSON.put("result", "success");
                    poJSON.put("message", "Save item record successfuly.");
                }

            } else {
                poJSON.put("result", "error");
                poJSON.put("message", "Unable to Save empty Transaction.");
                return poJSON;
            }
        } else {
            poJSON.put("result", "error");
            poJSON.put("message", "Unable to Save empty Transaction.");
            return poJSON;
        }

        return poJSON;
    }

    public void RemoveModelDetail(int fnRow) {
        if (poModelDetail.size() >= 1) {
            poModelDetail.remove(fnRow);
            if (poModelDetail.size() == 0) {
                AddModelDetail();
            }
        }
    }

    public JSONObject deleteRecord() {
        poJSON = new JSONObject();
        if (pnEditMode == EditMode.READY || pnEditMode == EditMode.UPDATE) {
            String lsSQLs = MiscUtil.addCondition(new Model_Inv_Stock_Request_Detail(poGRider).getSQL(), "a.sTransNox = " + SQLUtil.toSQL(poModelMaster.getTransactionNumber()));
            ResultSet loRS = poGRider.executeQuery(lsSQLs);
            System.out.println(lsSQLs);
            backupRecords = new ArrayList<>();
            int lnctr = 0;
            try {
                while (loRS.next()) {

                    backupRecords.add(new Model_Inv_Stock_Request_Detail(poGRider));
                    poJSON = backupRecords.get(lnctr).openRecord(loRS.getString("sTransNox"), loRS.getString("sStockIDx"));
                    if ("error".equals((String) poJSON.get("result"))) {
                        return poJSON;
                    }
                    lnctr++;
                }
            } catch (SQLException ex) {
                Logger.getLogger(Inv_Request_MP_Issuance.class.getName()).log(Level.SEVERE, null, ex);
            }
            Model_Inv_Stock_Request_Detail model = new Model_Inv_Stock_Request_Detail(poGRider);
            String lsSQL = "DELETE FROM " + model.getTable()
                    + " WHERE sTransNox = " + SQLUtil.toSQL(poModelMaster.getTransactionNumber());

            if (!lsSQL.equals("")) {
                if (poGRider.executeQuery(lsSQL, model.getTable(), poGRider.getBranchCode(), "") > 0) {
                    poJSON.put("result", "success");
                    poJSON.put("message", "Record deleted successfully.");
                }
            }
        }

        return poJSON;
    }

    @Override
    public void setType(RequestType types) {
        type = types;
    }

    @Override
    public void setCategoryType(RequestCategoryType type) {
        category_type = type;
    }

    public JSONObject LoadModelMasterList() {

        try {
            String lsSQL = getSQL() + " GROUP BY a.sTransNox";
            System.out.println(lsSQL);
            ResultSet loRS = poGRider.executeQuery(lsSQL);
            poMasterList = new ArrayList<>();
            while (loRS.next()) {

                poMasterList.add(new Model_Inv_Stock_Request_Master(poGRider));
                poJSON = poMasterList.get(poMasterList.size() - 1).openRecord(loRS.getString("sTransNox"));
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

    private String getSQL() {
        return "SELECT "
                + "  a.sTransNox, "
                + "  a.sBranchCd, "
                + "  a.sCategrCd, "
                + "  a.dTransact, "
                + "  a.sReferNox, "
                + "  a.sRemarksx, "
                + "  a.sIssNotes, "
                + "  a.nCurrInvx, "
                + "  a.nEstInvxx, "
                + "  a.sApproved, "
                + "  a.dApproved, "
                + "  a.sAprvCode, "
                + "  a.nEntryNox, "
                + "  a.sSourceCd, "
                + "  a.sSourceNo, "
                + "  a.cConfirmd, "
                + "  a.cTranStat, "
                + "  a.dStartEnc, "
                + "  a.sModified, "
                + "  a.dModified, "
                + "  b.sBranchNm    xBranchNm, "
                + "  c.sDescript    xCategrNm, "
                + "  d.sDescript    xCategNm1, "
                + "  f.sBarCodex "
                + "FROM Inv_Stock_Request_Master a "
                + "  LEFT JOIN Branch b "
                + "    ON a.sBranchCd = b.sBranchCd "
                + "  LEFT JOIN Category c "
                + "    ON a.sCategrCd = c.sCategrCd "
                + "  LEFT JOIN Category_Level2 d "
                + "    ON c.sCategrCd = d.sMainCatx "
                + "  LEFT JOIN Inv_Stock_Request_Detail e "
                + "	on  e.sTransNox = a.sTransNox "
                + "  LEFT JOIN Inventory f "
                + "	on f.sStockIDx = e.sStockIDx ";
    }


    @Override
    public ArrayList<Model_Inv_Stock_Request_Detail> getDetailModel() {
        return poModelDetail;
    }

    @Override
    public ArrayList<Model_Inv_Stock_Request_Master> getMasterModelList() {
        return poMasterList;
    }

    @Override
    public void cancelUpdate() {
        poJSON = new JSONObject();
        if (!backupRecords.isEmpty()) {
            if (!pbWthParent) {         
                poGRider.beginTrans();
            }     
            for (int lnCtr1 = 0; lnCtr1 < backupRecords.size(); lnCtr1++) { 
                backupRecords.get(lnCtr1).setIssueQuantity(0);
            }                        

            poModelDetail.clear();  // Clear the current details
            poModelDetail.addAll(backupRecords);  // Restore from backup
            poJSON = SaveDetail();

            if ("error".equals((String) poJSON.get("result"))) {
                if (!pbWthParent) {
                    poGRider.rollbackTrans();
                }
            }
            if (!pbWthParent) {
                poGRider.commitTrans();
            }

        }
    }

    private void restoreData() {
        if (!backupRecords.isEmpty()) {
            if (!pbWthParent) {
                poGRider.beginTrans();
            }
            for (int lnCtr = 0; lnCtr < getItemCount() && lnCtr < backupRecords.size(); lnCtr++) {
                Model_Inv_Stock_Request_Detail model = poModelDetail.get(lnCtr);
                if (model.getStockID().equalsIgnoreCase(backupRecords.get(lnCtr).getStockID())) {
                    backupRecords.get(lnCtr).setIssueQuantity(model.getIssueQuantity());
                }
            }

            poModelDetail.clear();  // Clear the current details
            poModelDetail.addAll(backupRecords);  // Restore from backup

            if ("error".equals((String) poJSON.get("result"))) {
                if (!pbWthParent) {
                    poGRider.rollbackTrans();
                }
            }
            if (!pbWthParent) {
                poGRider.commitTrans();
            }

        }
    }
}
