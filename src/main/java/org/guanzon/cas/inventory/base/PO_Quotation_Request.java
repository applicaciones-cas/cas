package org.guanzon.cas.inventory.base;

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
import org.guanzon.cas.inventory.models.Model_PO_Quotation_Request_Detail;
import org.guanzon.cas.inventory.models.Model_PO_Quotation_Request_Master;
import org.guanzon.cas.parameters.Category_Level2;
import org.guanzon.cas.parameters.Color;
import org.guanzon.cas.parameters.Inv_Type;
import org.guanzon.cas.parameters.Measure;

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
        poModelDetail = new ArrayList<Model_PO_Quotation_Request_Detail>();
        poModelMaster.newRecord();
        //set the transaction no.
        poModelMaster.setTransactionNo(MiscUtil.getNextCode(poModelMaster.getTable(), "sTransNox",
                true, poGRider.getConnection(), poGRider.getBranchCode()));

        Model_PO_Quotation_Request_Detail loNewEntity = new Model_PO_Quotation_Request_Detail(poGRider);
        loNewEntity.newRecord();
        loNewEntity.setQuantity(0);
        loNewEntity.setUnitPrice(0.0);
        poModelDetail.add(loNewEntity);

        poJSON = new JSONObject();
        poJSON.put("result", "success");
        return poJSON;
    }

    @Override
    public JSONObject openTransaction(String fsValue) {

        Model_PO_Quotation_Request_Master poModelMaster = new Model_PO_Quotation_Request_Master(poGRider);
//        Branch loBranch = new Branch(poGRider, true);
        Category_Level2 loCatLevel2 = new Category_Level2(poGRider, true);
        Inv_Type loInvType = new Inv_Type(poGRider, true);

        //open the master table
        poModelMaster.openRecord(SQLUtil.toSQL(fsValue));
        if ("error".equals((String) poJSON.get("result"))) {
            return poJSON;
        }

        //open and set the affected table dummy column
        //set the master openrecord ito
        poJSON = searchMaster("sBranchCd", poModelMaster.getBranchCd(), true);
        if ("error".equals(
                (String) poJSON.get("result"))) {
            return poJSON;
        }

        poJSON = searchMaster("sDestinat", poModelMaster.getDestination(), true);

        if ("error".equals(
                (String) poJSON.get("result"))) {
            return poJSON;
        }

        poJSON = loCatLevel2.openRecord(poModelMaster.getCategoryCode());
        poJSON = loInvType.openRecord((String) loCatLevel2.getMaster("sInvTypCd"));

        poModelMaster.setCategoryName((String) loCatLevel2.getMaster("xCategrNm"));
        poModelMaster.setCategoryName((String) loCatLevel2.getMaster("xInvTypNm"));
        if ("error".equals(
                (String) poJSON.get("result"))) {
            return poJSON;
        }

        poJSON = openTransactionDetail(poModelMaster.getTransactionNumber());
        if (poModelMaster.getEntryNumber() != poModelDetail.size()) {
            poJSON.put("result", "success");
            poJSON.put("message", "Record loaded successfully.");
        }

        if ("error".equals(
                (String) poJSON.get("result"))) {
            return poJSON;
        }

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
                poModelDetail.get(lnCtr).setTransactionNo(poModelMaster.getTransactionNumber());
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

        poModelMaster.setEntryNumber(poModelDetail.size());
        poModelMaster.setBranchCd(poGRider.getBranchCode());
        poModelMaster.setPreparedDate(null);
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
    public JSONObject closeTransaction(String fsTransNox) {
        poJSON = new JSONObject();
        openTransaction(fsTransNox);
        if ("error".equals((String) poJSON.get("result"))) {
            return poJSON;
        }
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
    public JSONObject postTransaction(String fsTransNox) {
        poJSON = new JSONObject();
        openTransaction(fsTransNox);
        if ("error".equals((String) poJSON.get("result"))) {
            return poJSON;
        }
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
    public JSONObject voidTransaction(String fsTransNox) {
        poJSON = new JSONObject();
        openTransaction(fsTransNox);
        if ("error".equals((String) poJSON.get("result"))) {
            return poJSON;
        }
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

        openTransaction(fsTransNox);
        if ("error".equals((String) poJSON.get("result"))) {
            return poJSON;
        }
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
        return setDetail(fnRow, poModelDetail.get(fnRow).getColumn(fnCol), foData);
    }

    @Override
    public JSONObject setDetail(int fnRow, String fsCol, Object foData) {
        poJSON = new JSONObject();

        switch (fsCol) {
            case "nQuantity":
            case "sDescript":
                poJSON = poModelDetail.get(fnRow).setValue(fsCol, foData);
                if ("error".equals((String) poJSON.get("result"))) {
                    return poJSON;
                }

                if (poModelDetail.get(fnRow).getQuantity() > 0
                        && (!poModelDetail.get(fnRow).getDescript().isEmpty() || !poModelDetail.get(fnRow).getStockID().isEmpty())) {
                    AddModelDetail();
                }
            default:
                return poModelDetail.get(fnRow).setValue(fsCol, foData);
        }

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
            case "sDescript": //sDescript
            case "sStockIDx": //3 //8-xCategrNm //9-xInvTypNm
                Inventory loInventory = new Inventory(poGRider, true);
                loInventory.setRecordStatus(psTranStatus);
                loJSON = loInventory.searchRecord(fsValue, fbByCode);

                if (loJSON != null) {
                    System.out.println((String) loInventory.getMaster("sStockIDx"));
                    setDetail(fnRow, "sStockIDx", (String) loInventory.getMaster("sStockIDx"));
                    setDetail(fnRow, "xCategrNm", (String) loInventory.getMaster("xCategNm2"));
                    setDetail(fnRow, "sDescript", (String) loInventory.getMaster("sDescript"));
                    loJSON = setDetail(fnRow, "xInvTypNm", (String) loInventory.getMaster("xInvTypNm"));

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
        return searchDetail(fnRow, poModelDetail.get(fnRow).getColumn(fnColumn), fsValue, fbByCode);

    }

    @Override
    public JSONObject searchWithCondition(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public JSONObject searchTransaction(String fsColNme, String fsValue, boolean fbByCode) {
        String lsCondition = "";
        String lsFilter = "";

        if (psTranStatus.length() > 1) {
            for (int lnCtr = 0; lnCtr <= psTranStatus.length() - 1; lnCtr++) {
                lsCondition += ", " + SQLUtil.toSQL(Character.toString(psTranStatus.charAt(lnCtr)));
            }

            lsCondition = "cTranStat" + " IN (" + lsCondition.substring(2) + ")";
        } else {
            lsCondition = "cTranStat" + " = " + SQLUtil.toSQL(psTranStatus);
        }

        String lsSQL = MiscUtil.addCondition(poModelMaster.makeSelectSQL(), lsCondition);

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
                return searchTransaction(fsColNme, fsValue, fbByCode);

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
            case "sCategrCd": //9 //17-xCategrNm

                Category_Level2 loCategory2 = new Category_Level2(poGRider, true);
                loCategory2.setRecordStatus("01");
                loJSON = loCategory2.searchRecord(fsValue, fbByCode);

                Inv_Type loInvType = new Inv_Type(poGRider, true);
                loInvType.openRecord((String) loCategory2.getMaster("sInvTypCd"));

                if (loJSON != null) {
                    setMaster("sCategrCd", (String) loCategory2.getMaster("sCategrCd"));
                    setMaster("xCategrNm", (String) loCategory2.getMaster("sDescript"));
                    return setMaster("xInvTypNm", (String) loCategory2.getMaster("xInvTypNm"));

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
        return searchMaster(poModelMaster.getColumn(fnCol), fsValue, fbByCode);
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
        boolean lsModelRequired = poModelDetail.get(poModelDetail.size() - 1).getQuantity() > 0;
        if (lsModelRequired) {
            poModelDetail.add(new Model_PO_Quotation_Request_Detail(poGRider));
            poModelDetail.get(poModelDetail.size() - 1).newRecord();
            poModelDetail.get(poModelDetail.size() - 1).setTransactionNo(poModelMaster.getTransactionNumber());
            poModelDetail.get(poModelDetail.size() - 1).setQuantity(0);
            poModelDetail.get(poModelDetail.size() - 1).setUnitPrice(0.0);
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "Information");
            poJSON.put("message", "Please Fill up Required Record Fist!");
        }

        return poJSON;
    }

    public void RemoveModelDetail(int fnRow) {
        poModelDetail.remove(fnRow);

    }

    public Inventory GetInventory(String fsPrimaryKey, boolean fbByCode) {
        Inventory instance = new Inventory(poGRider, fbByCode);
        instance.openRecord(fsPrimaryKey);
        return instance;
    }

    public Color GetColor(String fsPrimaryKey, boolean fbByCode) {
        Color instance = new Color(poGRider, fbByCode);
        instance.openRecord(fsPrimaryKey);
        return instance;
    }

    public Measure GetMeasure(String fsPrimaryKey, boolean fbByCode) {
        Measure instance = new Measure(poGRider, fbByCode);
        instance.openRecord(fsPrimaryKey);
        return instance;
    }

}
