package org.guanzon.cas.inventory.stock.request.cancel;

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
import org.guanzon.cas.inventory.models.Model_Inv_Stock_Req_Cancel_Detail;
import org.guanzon.cas.inventory.models.Model_Inv_Stock_Req_Cancel_Master;
import org.guanzon.cas.inventory.models.Model_Inv_Stock_Request_Detail;
import org.guanzon.cas.inventory.stock.Inv_Request;
import org.guanzon.cas.inventory.stock.Inv_Request_SP;
import org.guanzon.cas.inventory.stock.request.RequestCancelController;
import org.guanzon.cas.inventory.stock.request.RequestControllerFactory;
import org.json.simple.JSONObject;

/**
 *
 * @author Unclejo
 */
public class InvRequestCancel implements GTranDet {

    GRider poGRider;
    boolean pbWthParent;
    int pnEditMode;
    String psTranStatus;

    private boolean p_bWithUI = true;
    Inv_Request poRequest;
    Model_Inv_Stock_Req_Cancel_Master poModelMaster;
    ArrayList<Model_Inv_Stock_Req_Cancel_Detail> poModelDetail;

    RequestControllerFactory.RequestType type;
    RequestControllerFactory.RequestCategoryType category_type;
    JSONObject poJSON;

    public void setWithUI(boolean fbValue) {
        p_bWithUI = fbValue;
    }

    public InvRequestCancel(GRider foGRider, boolean fbWthParent) {
        poGRider = foGRider;
        pbWthParent = fbWthParent;
        poModelMaster = new Model_Inv_Stock_Req_Cancel_Master(foGRider);
        poModelDetail = new ArrayList<>();
        poModelDetail.add(new Model_Inv_Stock_Req_Cancel_Detail(foGRider));
        poRequest = new Inv_Request(foGRider, fbWthParent);
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

        poModelDetail = new ArrayList<>();
        poModelDetail.add(new Model_Inv_Stock_Req_Cancel_Detail(poGRider));
        poRequest = new Inv_Request(poGRider, pbWthParent);
        poJSON = poModelDetail.get(0).setTransactionNumber(poModelMaster.getTransactionNumber());
        if ("error".equals((String) poJSON.get("result"))) {
            poJSON.put("result", "error");
            poJSON.put("message", "No record to load.");
            return poJSON;

        }

        poJSON.put("result", "success");
        poJSON.put("message", "initialized new record.");
        pnEditMode = EditMode.ADDNEW;
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

        return poJSON;

    }

    @Override
    public JSONObject updateTransaction() {
        poJSON = new JSONObject();
        if (pnEditMode != EditMode.READY && pnEditMode != EditMode.UPDATE) {
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
    public JSONObject saveTransaction() {

        poJSON = new JSONObject();
        if (!pbWthParent) {
            poGRider.beginTrans();
        }

        if (getItemCount() >= 1) {
            for (int lnCtr = 0; lnCtr <= getItemCount() - 1; lnCtr++) {
                if (lnCtr >= 0) {
                    if (poModelDetail.get(lnCtr).getStockID().isEmpty() || poModelDetail.get(lnCtr).getBarcode().isEmpty()) {
                        poModelDetail.remove(lnCtr);
                        if (lnCtr <= poModelDetail.size() - 1) {
                            break;
                        }
                    }
                }
                poModelDetail.get(lnCtr).setTransactionNumber(poModelMaster.getTransactionNumber());
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
            poJSON.put("message", "No record of item detected.");
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
        if (poModelMaster.getEditMode() == EditMode.READY || poModelMaster.getEditMode() == EditMode.UPDATE) {
            if (poModelMaster.getTransactionStatus().equalsIgnoreCase(TransactionStatus.STATE_CLOSED)) {
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
            if (getItemCount() >= 1) {
                for (int lnCtr = 0; lnCtr <= getItemCount() - 1; lnCtr++) {
                    poModelDetail.get(lnCtr).setTransactionNumber(poModelMaster.getTransactionNumber());
                    poModelDetail.get(lnCtr).setEntryNumber(lnCtr + 1);
                    poJSON = updateRequestDetail(poModelDetail.get(lnCtr).getStockID(), poModelDetail.get(lnCtr).getQuantity());
                    if ("error".equals((String) poJSON.get("result"))) {
                        if (!pbWthParent) {
                            poGRider.rollbackTrans();
                        }
                        return poJSON;
                    }
                    poJSON.put("result", "success");
                    poJSON.put("message", "Record save successfully");

                }
                poJSON = poRequest.openTransaction(poModelMaster.getOrderNumber());
                if ("error".equals((String) poJSON.get("result"))) {
                    if (!pbWthParent) {
                        poGRider.rollbackTrans();
                    }
                    return poJSON;
                }
                if (getItemCount() == poRequest.getItemCount()) {
                    poRequest.getMasterModel().setTransactionStatus(TransactionStatus.STATE_CANCELLED);
                    poJSON = poRequest.getMasterModel().saveRecord();
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
    public Model_Inv_Stock_Req_Cancel_Detail getDetailModel(int fnRow) {
        return poModelDetail.get(fnRow);
    }
//    @Override

    public ArrayList<Model_Inv_Stock_Req_Cancel_Detail> getDetailModel() {
        return poModelDetail;
    }

    @Override
    public JSONObject setDetail(int fnRow, int fnCol, Object foData) {
        if (fnCol == 5) {
            int nUnserved = Integer.parseInt(String.valueOf(poRequest.getDetailModel().get(poRequest.getDetailModel().size() - 1).getQuantity()))
                    - (Integer.parseInt(String.valueOf(poRequest.getDetailModel().get(poRequest.getDetailModel().size() - 1).getIssueQuantity()))
                    + Integer.parseInt(String.valueOf(poRequest.getDetailModel().get(poRequest.getDetailModel().size() - 1).getCancelled()))
                    + Integer.parseInt(String.valueOf(poRequest.getDetailModel().get(poRequest.getDetailModel().size() - 1).getOrderQuantity())));

            int lnQty = Integer.parseInt(String.valueOf(foData));
            //check lnQty input value is lesser than or equal to unserve qty
            if (lnQty <= nUnserved) {
                //if true save the qty value of foData
                poModelDetail.get(fnRow).setValue(fnCol, foData);
                return poModelDetail.get(fnRow).setValue(13, nUnserved);
            } else {
                //if false set the qty value from nUnserved value
                return poModelDetail.get(fnRow).setValue(fnCol, nUnserved);
            }
        }
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

            case 3: //sBarCodex/sStockID
                String lsSQL = new Model_Inv_Stock_Request_Detail(poGRider).getSQL();
//                lsSQL =  MiscUtil.addCondition(lsSQL, fsValue);

                lsSQL = MiscUtil.addCondition(lsSQL, "a.sTransNox = " + SQLUtil.toSQL(poModelMaster.getOrderNumber()));
                if (fbByCode) {
                    lsSQL = MiscUtil.addCondition(lsSQL, "b.sBarCodex = " + SQLUtil.toSQL(fsValue));
                } else {
                    lsSQL = MiscUtil.addCondition(lsSQL, "b.sBarCodex LIKE " + SQLUtil.toSQL("%" + fsValue + "%"));
                }

                if (p_bWithUI) {
                    poJSON = ShowDialogFX.Search(poGRider,
                            lsSQL,
                            fsValue,
                            "Stock ID»Barcode»Name",
                            "sTransNox»xBarCodex»xDescript",
                            "a.sStockIDx»b.sBarCodex»b.sDescript",
                            fbByCode ? 1 : 2);

                    if (poJSON != null) {
                        poJSON = poRequest.OpenModelDetailByStockID((String) poJSON.get("sTransNox"), (String) poJSON.get("sStockIDx"));

                        if (poJSON != null) {
                            for (int lnCtr = 0; lnCtr < poModelDetail.size(); lnCtr++) {
                                if (poModelDetail.get(lnCtr).getStockID().equalsIgnoreCase((String) poRequest.getDetailModel(0).getStockID())) {
                                    poJSON = new JSONObject();
                                    poJSON.put("result", "error");
                                    poJSON.put("message", "Inventory item request already added.");
                                    return poJSON;
                                }
                            }

                            setDetail(fnRow, "sOrderNox", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size() - 1).getTransactionNumber());
                            setDetail(fnRow, "sStockIDx", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size() - 1).getStockID());
                            setDetail(fnRow, "nQuantity", (int) poRequest.getDetailModel().get(poRequest.getDetailModel().size() - 1).getCancelled());
                            setDetail(fnRow, "sNotesxxx", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size() - 1).getNotes());
                            setDetail(fnRow, "xBarCodex", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size() - 1).getBarcode());
                            setDetail(fnRow, "xDescript", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size() - 1).getDescription());
                            setDetail(fnRow, "xCategr01", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size() - 1).getCategoryName());
                            setDetail(fnRow, "xCategr02", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size() - 1).getCategoryName2());
                            setDetail(fnRow, "xInvTypNm", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size() - 1).getCategoryType());

                            poJSON.put("result", "success");
                            poJSON.put("message", "Record successfully loaded.");
                            return poJSON;
                        } else {

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
                    if (!loRS.next()) {
                        MiscUtil.close(loRS);
                        poJSON.put("result", "error");
                        poJSON.put("message", "No record loaded.");
                        return poJSON;
                    }

                    lsSQL = loRS.getString("sTransNox");
                    lsStockId = loRS.getString("sStockIDx");
                    MiscUtil.close(loRS);
                } catch (SQLException ex) {
                    Logger.getLogger(Inv_Request_Cancel.class.getName()).log(Level.SEVERE, null, ex);
                }
                poJSON = poRequest.OpenModelDetailByStockID(lsSQL, lsStockId);
                if (poJSON != null) {
                    for (int lnCtr = 0; lnCtr < poModelDetail.size(); lnCtr++) {
                        if (poModelDetail.get(lnCtr).getStockID().equalsIgnoreCase((String) poRequest.getDetailModel(0).getStockID())) {
                            poJSON = new JSONObject();
                            poJSON.put("result", "error");
                            poJSON.put("message", "Inventory item request already added.");
                            return poJSON;
                        }
                    }

                    setDetail(fnRow, "sOrderNox", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size() - 1).getTransactionNumber());
                    setDetail(fnRow, "sStockIDx", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size() - 1).getStockID());
                    setDetail(fnRow, "nQuantity", (int) poRequest.getDetailModel().get(poRequest.getDetailModel().size() - 1).getCancelled());
                    setDetail(fnRow, "sNotesxxx", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size() - 1).getNotes());
                    setDetail(fnRow, "xBarCodex", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size() - 1).getBarcode());
                    setDetail(fnRow, "xDescript", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size() - 1).getDescription());
                    setDetail(fnRow, "xCategr01", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size() - 1).getCategoryName());
                    setDetail(fnRow, "xCategr02", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size() - 1).getCategoryName2());
                    setDetail(fnRow, "xInvTypNm", (String) poRequest.getDetailModel().get(poRequest.getDetailModel().size() - 1).getCategoryType());
                } else {

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
        if (pnEditMode == EditMode.UNKNOWN && pnEditMode == EditMode.READY) {
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

            if (p_bWithUI) {
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
                if (!loRS.next()) {
                    MiscUtil.close(loRS);
                    poJSON.put("result", "error");
                    poJSON.put("message", "No record loaded.");
                    return poJSON;
                }

                lsSQL = loRS.getString("sTransNox");
                MiscUtil.close(loRS);
            } catch (SQLException ex) {
                Logger.getLogger(Inv_Request_Cancel.class.getName()).log(Level.SEVERE, null, ex);
            }

            return openTransaction(lsSQL);
        } else {
            return BrowseRequest(fsColumn, fsValue, fbByCode);
        }
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
        poJSON = new JSONObject();
        try {
            String lsSQL = MiscUtil.addCondition(new Model_Inv_Stock_Req_Cancel_Detail(poGRider).getSQL(), "a.sTransNox = " + SQLUtil.toSQL(fsTransNo));
            ResultSet loRS = poGRider.executeQuery(lsSQL);

            int lnctr = 0;
            if (MiscUtil.RecordCount(loRS) > 0) {
                poModelDetail = new ArrayList<>();
                while (loRS.next()) {
                    poModelDetail.add(new Model_Inv_Stock_Req_Cancel_Detail(poGRider));
                    poJSON = poModelDetail.get(poModelDetail.size() - 1).openRecord(loRS.getString("sTransNox"), loRS.getString("sStockIDx"));
                    if ("error".equals((String) poJSON.get("result"))) {
                        return poJSON;
                    }
                    pnEditMode = EditMode.UPDATE;
                    lnctr++;
                    poJSON.put("result", "success");
                    poJSON.put("message", "Record loaded successfully.");
                }

                AddModelDetail();
                System.out.println("lnctr = " + lnctr);

            } else {
                poModelDetail = new ArrayList<>();
                AddModelDetail();
                poJSON.put("result", "error");
                poJSON.put("continue", true);
                poJSON.put("message", "No record found.");
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
        if (poModelDetail.isEmpty()) {
            poModelDetail.add(new Model_Inv_Stock_Req_Cancel_Detail(poGRider));
            poModelDetail.get(0).newRecord();
            poModelDetail.get(0).setEntryNumber(poModelDetail.size());
            poModelDetail.get(0).setTransactionNumber(poModelMaster.getTransactionNumber());
            poJSON.put("result", "success");
            poJSON.put("message", "Inventory request add record.");

        } else {
            poModelDetail.add(new Model_Inv_Stock_Req_Cancel_Detail(poGRider));
            poModelDetail.get(poModelDetail.size() - 1).newRecord();
            poModelDetail.get(poModelDetail.size() - 1).setEntryNumber(poModelDetail.size());
            poModelDetail.get(poModelDetail.size() - 1).setTransactionNumber(poModelMaster.getTransactionNumber());

            poJSON.put("result", "success");
            poJSON.put("message", "Inventory request add record.");
        }
        System.out.println(poModelDetail.size());

        return poJSON;
    }

    public void RemoveModelDetail(int fnRow) {
        if (poModelDetail.size() == 0) {
            AddModelDetail();
        }
        poModelDetail.remove(fnRow);

    }

    /**
     * @return This function process for inventory stock request. This function
     * use for browsing Inventory Stock Request fetching inventory request and
     * set data for Inventory Stock Request Cancel
     */
    public JSONObject BrowseRequest(String fsColumn, String fsValue, boolean fbByCode) {
        poJSON = new JSONObject();
        try {
            poRequest.setType(type);
            poRequest.setCategoryType(category_type);
            poRequest.setTransactionStatus("01");
            poRequest.setWithUI(p_bWithUI);
            poJSON = poRequest.searchTransaction(fsColumn, fsValue, fbByCode);
            if ("error".equalsIgnoreCase((String) poJSON.get("result"))) {
                return poJSON;
            }

            setMaster("sBranchCd", poRequest.getMasterModel().getBranchCode());
            setMaster("xBranchNm", poRequest.getMasterModel().getBranchName());
            setMaster("sCategrCd", poRequest.getMasterModel().getCategoryCode());
            setMaster("xCategrNm", poRequest.getMasterModel().getCategoryName());
            setMaster("sOrderNox", poRequest.getMasterModel().getTransactionNumber());
            setMaster("dStartEnc", poRequest.getMasterModel().getStartEncDate());
            System.out.println("getTransactionNumber = " + poRequest.getMasterModel().getTransactionNumber());
            if (p_bWithUI) {
                if (ShowMessageFX.YesNo("Do you want to display all request item from this transaction " + poRequest.getMasterModel().getTransactionNumber() + "?", "Computerized Acounting System", "Inventory Stock Request Cancel")) {
                    poModelDetail = new ArrayList<>();
                    for (int lnCtr = 0; lnCtr < poRequest.getDetailModel().size(); lnCtr++) {
                        poModelDetail.add(new Model_Inv_Stock_Req_Cancel_Detail(poGRider));
                        poModelDetail.get(lnCtr).newRecord();

                        setDetail(lnCtr, "nEntryNox", (int) poRequest.getDetailModel().get(lnCtr).getEntryNumber());
                        setDetail(lnCtr, "sOrderNox", (String) poRequest.getDetailModel().get(lnCtr).getTransactionNumber());
                        setDetail(lnCtr, "sStockIDx", (String) poRequest.getDetailModel().get(lnCtr).getStockID());
                        setDetail(lnCtr, "nQuantity", (int) poRequest.getDetailModel().get(lnCtr).getCancelled());
                        setDetail(lnCtr, "sNotesxxx", (String) poRequest.getDetailModel().get(lnCtr).getNotes());
                        setDetail(lnCtr, "xBarCodex", (String) poRequest.getDetailModel().get(lnCtr).getBarcode());
                        setDetail(lnCtr, "xDescript", (String) poRequest.getDetailModel().get(lnCtr).getDescription());
                        setDetail(lnCtr, "xCategr01", (String) poRequest.getDetailModel().get(lnCtr).getCategoryName());
                        setDetail(lnCtr, "xCategr02", (String) poRequest.getDetailModel().get(lnCtr).getCategoryName2());
                        setDetail(lnCtr, "xInvTypNm", (String) poRequest.getDetailModel().get(lnCtr).getCategoryType());
                    }
                } else {
                    poModelDetail = new ArrayList<>();
                    poJSON = AddModelDetail();
                }
            } else {
                poModelDetail = new ArrayList<>();
//            This code assume that user click no in ShowMessageFX;
//            poJSON = AddModelDetail();

//            This code assume that user click yes in ShowMessageFX;
//            set all request detail to request detail cancel;
                for (int lnCtr = 0; lnCtr <= poRequest.getDetailModel().size() - 1; lnCtr++) {
                    poModelDetail.add(new Model_Inv_Stock_Req_Cancel_Detail(poGRider));
                    poModelDetail.get(lnCtr).newRecord();

                    setDetail(lnCtr, "nEntryNox", (int) poRequest.getDetailModel().get(lnCtr).getEntryNumber());
                    setDetail(lnCtr, "sOrderNox", (String) poRequest.getDetailModel().get(lnCtr).getTransactionNumber());
                    setDetail(lnCtr, "sStockIDx", (String) poRequest.getDetailModel().get(lnCtr).getStockID());
                    setDetail(lnCtr, "nQuantity", (int) poRequest.getDetailModel().get(lnCtr).getCancelled());
                    setDetail(lnCtr, "sNotesxxx", (String) poRequest.getDetailModel().get(lnCtr).getNotes());
                    setDetail(lnCtr, "xBarCodex", (String) poRequest.getDetailModel().get(lnCtr).getBarcode());
                    setDetail(lnCtr, "xDescript", (String) poRequest.getDetailModel().get(lnCtr).getDescription());
                    setDetail(lnCtr, "xCategr01", (String) poRequest.getDetailModel().get(lnCtr).getCategoryName());
                    setDetail(lnCtr, "xCategr02", (String) poRequest.getDetailModel().get(lnCtr).getCategoryName2());
                    setDetail(lnCtr, "xInvTypNm", (String) poRequest.getDetailModel().get(lnCtr).getCategoryType());

                }
            }
        } catch (NullPointerException e) {

        }

        return poJSON;
    }

    private JSONObject updateRequestDetail(String lsStockID, int fnQty) {
        poJSON = poRequest.OpenModelDetailByStockID(poModelMaster.getOrderNumber(), lsStockID);

        if ("error".equals((String) poJSON.get("result"))) {
            if (!pbWthParent) {
                poGRider.rollbackTrans();
            }
            return poJSON;
        }

        for (int lnctr = 0; lnctr < poRequest.getDetailModel().size(); lnctr++) {
            Model_Inv_Stock_Request_Detail poModel = poRequest.getDetailModel(lnctr);
            if (poModel.getStockID().equals(lsStockID)) {
                poModel.setCancelled(fnQty);
                poJSON = poModel.saveRecord();

                if ("error".equals((String) poJSON.get("result"))) {
                    if (!pbWthParent) {
                        poGRider.rollbackTrans();
                    }
                }

                poJSON.put("result", "success");
                poJSON.put("message", "Record save successfully");
            }
        }

        return poJSON;
    }

//    @Override
    public void setType(RequestControllerFactory.RequestType types) {
        type = types;
    }

//    @Override
    public void setCategoryType(RequestControllerFactory.RequestCategoryType type) {
        category_type = type;
    }

}
