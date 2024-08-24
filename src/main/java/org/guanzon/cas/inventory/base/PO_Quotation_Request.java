package org.guanzon.cas.inventory.base;

import java.math.BigDecimal;
import org.guanzon.appdriver.iface.GTranDet;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.cas.model.inventory.Model_PO_Quotation_Request_Detail;
import org.guanzon.cas.model.inventory.Model_PO_Quotation_Request_Master;

import org.guanzon.cas.parameters.Category;
import org.guanzon.cas.parameters.Category_Level2;
import org.guanzon.cas.parameters.Inv_Type;

import org.json.simple.JSONObject;

/**
 *
 * @author user
 */
public class PO_Quotation_Request implements GTranDet {

    private GRider poGRider;
    private boolean pbWthParent;
    private int pnEditMode;
    private String psTranStatus;

    Model_PO_Quotation_Request_Master poModelMaster;
    ArrayList<Model_PO_Quotation_Request_Detail> poModelDetail;

    JSONObject poJSON;

    public PO_Quotation_Request(GRider foGRider, boolean fbWthParent) {
        poGRider = foGRider;
        pbWthParent = fbWthParent;

        poModelMaster = new Model_PO_Quotation_Request_Master(poGRider);
        poModelDetail = new ArrayList<Model_PO_Quotation_Request_Detail>();
        pnEditMode = EditMode.UNKNOWN;
    }

    @Override
    public JSONObject newTransaction() {
        poModelMaster = new Model_PO_Quotation_Request_Master(poGRider);

        poModelMaster.newRecord();
        //set the transaction no.
        poModelMaster.setTransactionNo(MiscUtil.getNextCode(poModelMaster.getTable(), "sTransNox",
                true, poGRider.getConnection(), poGRider.getBranchCode()));

        poModelDetail = new ArrayList<Model_PO_Quotation_Request_Detail>();

        poJSON = new JSONObject();
        poJSON.put("result", "success");
        return poJSON;
    }

    @Override
    public JSONObject openTransaction(String fsValue) {

        poModelMaster = new Model_PO_Quotation_Request_Master(poGRider);

        poModelMaster.openRecord(SQLUtil.toSQL(fsValue));
        if ("error".equals((String) poJSON.get("result"))) {
            return poJSON;
        }
        //set the master
        poJSON = searchMaster("sBranchCd", poModelMaster.getBranchCd(), true);

        openTransactionDetail(poModelMaster.getTransactionNumber());
        if (poModelMaster.getEntryNumber() != poModelDetail.size()) {
            poJSON.put("result", "success");
            poJSON.put("message", "Record loaded successfully.");
        }

        if ("error".equals(
                (String) poJSON.get("result"))) {
            return poJSON;
        }
        poJSON = searchMaster("sDestinat", poModelMaster.getDestination(), true);

        if ("error".equals(
                (String) poJSON.get("result"))) {
            return poJSON;
        }

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
        if (!pbWthParent) {
            poGRider.beginTrans();
        }

        if (getItemCount() >= 1) {
            for (int lnCtr = 0; lnCtr <= getItemCount() - 1; lnCtr++) {
                poModelDetail.get(lnCtr).setEntryNo(lnCtr + 1);
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

        poModelMaster.setEntryNumber(poModelDetail.size();
        
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
    public Model_PO_Quotation_Request_Detail getDetailModel(int fnRow) {
        return poModelDetail.get(fnRow);

    }

    @Override
    public JSONObject setDetail(int fnRow, int fnCol, Object foData) {
        return poModelDetail.get(fnRow).setValue(fnCol, foData);
    }

    @Override
    public JSONObject setDetail(int fnRow, String fsCol, Object foData) {
        return poModelDetail.get(fnRow).setValue(fsCol, foData);
    }

    @Override
    public JSONObject searchDetail(int fnRow, String fsColumn, String fsValue, boolean fbByCode) {

        String lsHeader = "";
        String lsColName = "";
        String lsColCrit = "";
        String lsSQL = "";
        String lsCondition = "";
        JSONObject loJSON;

        switch (fsColumn) {

            case "sStockIDx": //3 //8-xCategrNm //9-xInvTypNm
                Inventory loInventory = new Inventory(poGRider, true);
                loInventory.setRecordStatus(psTranStatus);
                loJSON = loInventory.searchRecord(fsValue, fbByCode);

                if (loJSON != null) {
                    setDetail(fnRow, "sStockIDx", (String) loInventory.getMaster("sStockIDx"));
                    searchDetail(fnRow, "xCategrNm", (String) loInventory.getMaster("sCategCd2"), fbByCode);
                    searchDetail(fnRow, "xInvTypNm", (String) loInventory.getMaster("sInvTypCd"), fbByCode);

                    return loJSON;

                } else {
                    loJSON = new JSONObject();
                    loJSON.put("result", "error");
                    loJSON.put("message", "No record found.");
                    return loJSON;
                }

            case "xCategrNm": //8

                Category_Level2 loCategory2 = new Category_Level2(poGRider, true);
                loCategory2.setRecordStatus("01");
                loJSON = loCategory2.searchRecord(fsValue, fbByCode);

                if (loJSON != null) {
                    setDetail(fnRow, 8, (String) loCategory2.getMaster("sDescript"));
                    return loJSON;

                } else {

                    loJSON = new JSONObject();
                    loJSON.put("result", "error");
                    loJSON.put("message", "No record found.");
                    return loJSON;
                }

            case "xInvTypNm": //
                Inv_Type loInvType = new Inv_Type(poGRider, true);
                loInvType.setRecordStatus("01");
                loJSON = loInvType.searchRecord(fsValue, fbByCode);

                if (loJSON != null) {
                    setMaster(9, (String) loInvType.getMaster("sDescript"));
                    return loJSON;

                } else {

                    loJSON = new JSONObject();
                    loJSON.put("result", "error");
                    loJSON.put("message", "No record found.");
                    return loJSON;
                }

            default:
                return null;

        }
    }

    @Override
    public JSONObject searchDetail(int fnRow, int fnColumn, String fsValue, boolean fbByCode) {

        String lsHeader = "";
        String lsColName = "";
        String lsColCrit = "";
        String lsSQL = "";
        String lsCondition = "";
        JSONObject loJSON;

        switch (fnColumn) {

            case 3: //sStockIDx //8-xCategrNm //9-xInvTypNm
                Inventory loInventory = new Inventory(poGRider, true);
                loInventory.setRecordStatus(psTranStatus);
                loJSON = loInventory.searchRecord(fsValue, fbByCode);

                if (loJSON != null) {
                    setDetail(fnRow, 3, (String) loInventory.getMaster("sStockIDx"));
                    searchDetail(fnRow, 8, (String) loInventory.getMaster("sCategCd2"), fbByCode);
                    searchDetail(fnRow, 9, (String) loInventory.getMaster("sInvTypCd"), fbByCode);

                    return loJSON;

                } else {
                    loJSON = new JSONObject();
                    loJSON.put("result", "error");
                    loJSON.put("message", "No record found.");
                    return loJSON;
                }

            case 8: //xCategrNm

                Category_Level2 loCategory2 = new Category_Level2(poGRider, true);
                loCategory2.setRecordStatus("01");
                loJSON = loCategory2.searchRecord(fsValue, fbByCode);

                if (loJSON != null) {
                    setDetail(fnRow, 8, (String) loCategory2.getMaster("sDescript"));
                    return loJSON;

                } else {

                    loJSON = new JSONObject();
                    loJSON.put("result", "error");
                    loJSON.put("message", "No record found.");
                    return loJSON;
                }

            case 9: //xInvTypNm
                Inv_Type loInvType = new Inv_Type(poGRider, true);
                loInvType.setRecordStatus("01");
                loJSON = loInvType.searchRecord(fsValue, fbByCode);

                if (loJSON != null) {
                    setMaster(9, (String) loInvType.getMaster("sDescript"));
                    return loJSON;

                } else {

                    loJSON = new JSONObject();
                    loJSON.put("result", "error");
                    loJSON.put("message", "No record found.");
                    return loJSON;
                }

            default:
                return null;

        }
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

    @Override
    public JSONObject searchMaster(String fsColNme, String fsValue, boolean fbByCode) {

        String lsHeader = "";
        String lsColName = "";
        String lsColCrit = "";
        String lsSQL = "";
        String lsCondition = "";
        JSONObject loJSON;
//        if (fsValue.equals("") && fbByCode) return null;

        switch (fsColNme) {
            case "sTransNox": // 1
                lsHeader = "Transaction No»Date»Refer No";
                lsColName = "sTransNox»dTransact»sReferNox";
                lsColCrit = "sTransNox»dTransact»sReferNox";

                if (psTranStatus.length() > 1) {
                    for (int lnCtr = 0; lnCtr <= psTranStatus.length() - 1; lnCtr++) {
                        lsCondition += ", " + SQLUtil.toSQL(Character.toString(psTranStatus.charAt(lnCtr)));
                    }

                    lsCondition = "cRecdStat IN (" + lsCondition.substring(2) + ")";
                } else {
                    lsCondition = "cRecdStat = " + SQLUtil.toSQL(psTranStatus);
                }

                lsSQL = MiscUtil.addCondition(poModelMaster.makeSelectSQL(), " sTransNox LIKE "
                        + SQLUtil.toSQL(fsValue + "%") + " AND " + lsCondition);

                poJSON = ShowDialogFX.Search(poGRider,
                        lsSQL,
                        fsValue,
                        lsHeader,
                        lsColName,
                        lsColCrit,
                        fbByCode ? 0 : 1);

                if (poJSON != null) {
                    return poModelMaster.openRecord((String) poJSON.get("sTransNox"));
                } else {
                    poJSON.put("result", "error");
                    poJSON.put("message", "No record loaded to update.");
                    return poJSON;
                }
//            case "sBranchCd": //2 //15-xBranchNm
//                Branch loBranch = new Branch(poGRider, true);
//                loBranch.setRecordStatus(psTranStatus);
//                loJSON = loBranch.searchRecord(fsValue, fbByCode);
//
//                if (loJSON != null) {
//                    setMaster("sBranchCd", (String) loBranch.getMaster("sBranchCd"));
//                    setMaster("xBranchNm", (String) loBranch.getMaster("sBranchNm"));
//
//                    return loJSON;
//
//                } else {
//                    loJSON = new JSONObject();
//                    loJSON.put("result", "error");
//                    loJSON.put("message", "No record found.");
//                    return loJSON;
//                }
//
//            case "sDestinat": //4 //16-xDestinat
//                Branch loDestinat = new Branch(poGRider, true);
//                loDestinat.setRecordStatus(psTranStatus);
//                loJSON = loDestinat.searchRecord(fsValue, fbByCode);
//
//                if (loJSON != null) {
//                    setMaster("sDestinat", (String) loDestinat.getMaster("sBranchCd"));
//                    setMaster("xDestinat", (String) loDestinat.getMaster("sBranchNm"));
//
//                    return loJSON;
//
//                } else {
//                    loJSON = new JSONObject();
//                    loJSON.put("result", "error");
//                    loJSON.put("message", "No record found.");
//                    return loJSON;
//                }
            case "sCategCd": //8 //17-xCategrNm

                Category_Level2 loCategory2 = new Category_Level2(poGRider, true);
                loCategory2.setRecordStatus("01");
                loJSON = loCategory2.searchRecord(fsValue, fbByCode);

                if (loJSON != null) {
                    setMaster("sCategrCd", (String) loCategory2.getMaster("sCategrCd"));
                    setMaster("xCategrNm", (String) loCategory2.getMaster("sDescript"));

                    return searchMaster("xInvTypNm", (String) loCategory2.getMaster("sInvTypCd"), fbByCode);

                } else {

                    loJSON = new JSONObject();
                    loJSON.put("result", "error");
                    loJSON.put("message", "No record found.");
                    return loJSON;
                }

            case "xInvTypNm": //18

                Inv_Type loInvType = new Inv_Type(poGRider, true);
                loInvType.setRecordStatus("01");
                loJSON = loInvType.searchRecord(fsValue, fbByCode);

                if (loJSON != null) {
                    setMaster("xInvTypNm", (String) loInvType.getMaster("sDescript"));
                    return loJSON;

                } else {

                    loJSON = new JSONObject();
                    loJSON.put("result", "error");
                    loJSON.put("message", "No record found.");
                    return loJSON;
                }

            default:
                return null;
        }
    }

    @Override
    public JSONObject searchMaster(int fnCol, String fsValue, boolean fbByCode) {

        String lsHeader = "";
        String lsColName = "";
        String lsColCrit = "";
        String lsSQL = "";
        String lsCondition = "";
        JSONObject loJSON;

        switch (fnCol) {
            case 1: // Transaction No.
                lsHeader = "Transaction No»Date»Refer No";
                lsColName = "sTransNox»dTransact»sReferNox";
                lsColCrit = "sTransNox»dTransact»sReferNox";

                if (psTranStatus.length() > 1) {
                    for (int lnCtr = 0; lnCtr <= psTranStatus.length() - 1; lnCtr++) {
                        lsCondition += ", " + SQLUtil.toSQL(Character.toString(psTranStatus.charAt(lnCtr)));
                    }

                    lsCondition = "cRecdStat IN (" + lsCondition.substring(2) + ")";
                } else {
                    lsCondition = "cRecdStat = " + SQLUtil.toSQL(psTranStatus);
                }

                lsSQL = MiscUtil.addCondition(poModelMaster.makeSelectSQL(), " sTransNox LIKE "
                        + SQLUtil.toSQL(fsValue + "%") + " AND " + lsCondition);

                poJSON = ShowDialogFX.Search(poGRider,
                        lsSQL,
                        fsValue,
                        lsHeader,
                        lsColName,
                        lsColCrit,
                        fbByCode ? 0 : 1);

                if (poJSON != null) {
                    return poModelMaster.openRecord((String) poJSON.get("sTransNox"));
                } else {
                    poJSON.put("result", "error");
                    poJSON.put("message", "No record loaded to update.");
                    return poJSON;
                }
//uncomment if parameter for branch 
//            case 2: //sBranchCd //15-xBranchNm
//                Branch loBranch = new Branch(poGRider, true);
//                loBranch.setRecordStatus(psTranStatus);
//                loJSON = loBranch.searchRecord(fsValue, fbByCode);
//
//                if (loJSON != null) {
//                    setMaster(2, (String) loBranch.getMaster("sBranchCd"));
//                    setMaster(15, (String) loBranch.getMaster("sBranchNm"));
//
//                    return loJSON;
//
//                } else {
//                    loJSON = new JSONObject();
//                    loJSON.put("result", "error");
//                    loJSON.put("message", "No record found.");
//                    return loJSON;
//                }
//
//            case 4: //sDestinat //16-xDestinat
//                Branch loDestinat = new Branch(poGRider, true);
//                loDestinat.setRecordStatus(psTranStatus);
//                loJSON = loDestinat.searchRecord(fsValue, fbByCode);
//
//                if (loJSON != null) {
//                    setMaster(4, (String) loDestinat.getMaster("sBranchCd"));
//                    setMaster(16, (String) loDestinat.getMaster("sBranchNm"));
//
//                    return loJSON;
//
//                } else {
//                    loJSON = new JSONObject();
//                    loJSON.put("result", "error");
//                    loJSON.put("message", "No record found.");
//                    return loJSON;
//                }
            case 8: //sCategCd //17xCategrNm

                Category_Level2 loCategory2 = new Category_Level2(poGRider, true);
                loCategory2.setRecordStatus("01");
                loJSON = loCategory2.searchRecord(fsValue, fbByCode);

                if (loJSON != null) {
                    setMaster(8, (String) loCategory2.getMaster("sCategrCd"));
                    setMaster(17, (String) loCategory2.getMaster("sDescript"));

                    return searchMaster(18, (String) loCategory2.getMaster("sInvTypCd"), fbByCode);

                } else {

                    loJSON = new JSONObject();
                    loJSON.put("result", "error");
                    loJSON.put("message", "No record found.");
                    return loJSON;
                }

            case 18: //xInvTypNm

                Inv_Type loInvType = new Inv_Type(poGRider, true);
                loInvType.setRecordStatus("01");
                loJSON = loInvType.searchRecord(fsValue, fbByCode);

                if (loJSON != null) {
                    setMaster(18, (String) loInvType.getMaster("sDescript"));
                    return loJSON;

                } else {

                    loJSON = new JSONObject();
                    loJSON.put("result", "error");
                    loJSON.put("message", "No record found.");
                    return loJSON;
                }

            default:
                return null;
        }
    }

    @Override
    public Model_PO_Quotation_Request_Master getMasterModel() {
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

    public JSONObject openTransactionDetail(String fsTransNo) {

        poModelDetail = new ArrayList<Model_PO_Quotation_Request_Detail>();
        try {
            String lsSQL = MiscUtil.addCondition(poModelDetail.get(0).makeSQL(), "sTransNox = " + SQLUtil.toSQL(fsTransNo));
            ResultSet loRS = poGRider.executeQuery(lsSQL);

            while (loRS.next()) {
                poModelDetail.get(poModelDetail.size() - 1).openRecord(loRS.getString("sTransNox"), loRS.getString("sStockIDx"));

                if ("success".equals((String) poJSON.get("success"))) {
                    AddModelDetail();
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
            poModelDetail.add(new Model_PO_Quotation_Request_Detail(poGRider));
            poModelDetail.get(poModelDetail.size() - 1).newRecord();
            poModelDetail.get(poModelDetail.size() - 1).setTransactionNo(poModelMaster.getTransactionNumber());

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
